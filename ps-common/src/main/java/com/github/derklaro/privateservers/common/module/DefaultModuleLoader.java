/*
 * MIT License
 *
 * Copyright (c) 2020 Pasqual K. and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.derklaro.privateservers.common.module;

import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.module.ModuleContainer;
import com.github.derklaro.privateservers.api.module.ModuleLoader;
import com.github.derklaro.privateservers.api.module.annotation.Module;
import com.github.derklaro.privateservers.api.module.exception.ModuleMainClassNotFoundException;
import com.github.derklaro.privateservers.common.util.KeyValueAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class DefaultModuleLoader implements ModuleLoader {

    public static final ModuleLoader INSTANCE = new DefaultModuleLoader();

    private static final Path MODULE_PATH = Paths.get("plugins/ps/modules");

    private final Collection<ModuleContainer> moduleContainers = new CopyOnWriteArrayList<>();

    private final Collection<Path> toLoad = new CopyOnWriteArrayList<>();

    @Override
    public void detectModules() {
        if (!Files.exists(MODULE_PATH)) {
            try {
                Files.createDirectories(MODULE_PATH);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(MODULE_PATH, path -> path.toString().endsWith(".jar"))) {
            for (Path path : stream) {
                this.toLoad.add(path);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void loadModules(@NotNull Plugin loader) throws ModuleMainClassNotFoundException {
        for (Path path : this.toLoad) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{path.toUri().toURL()});
                List<KeyValueAccessor<Class<?>, Module>> mainClassPossibilities = this.findPossibilityMainClasses(path, classLoader);

                if (mainClassPossibilities.size() != 1) {
                    throw new ModuleMainClassNotFoundException();
                }

                KeyValueAccessor<Class<?>, Module> keyValueAccessor = mainClassPossibilities.get(0);

                Object instance;
                try {
                    instance = keyValueAccessor.getLeft().getDeclaredConstructor(Plugin.class).newInstance(loader);
                } catch (final NoSuchMethodException exception) {
                    System.err.println("No constructor with plugin as single argument in module " + path.toString());
                    continue;
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                    exception.printStackTrace();
                    continue;
                }

                ModuleContainer container = new DefaultModuleContainer(
                        keyValueAccessor.getRight(),
                        keyValueAccessor.getLeft(),
                        classLoader,
                        path,
                        instance
                );
                this.moduleContainers.add(container);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.toLoad.clear();
    }

    @Override
    public void disableModules() {
        for (ModuleContainer moduleContainer : this.moduleContainers) {
            try {
                moduleContainer.getClassLoader().close();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.moduleContainers.clear();
    }

    @Override
    public @NotNull Optional<ModuleContainer> getModuleByInstance(@NotNull Object instance) {
        if (instance instanceof ModuleContainer) {
            return Optional.of((ModuleContainer) instance);
        }

        return Optional.empty();
    }

    @Override
    public @NotNull Optional<ModuleContainer> getModuleById(@NotNull String id) {
        for (ModuleContainer moduleContainer : this.moduleContainers) {
            if (moduleContainer.getId().equals(id)) {
                return Optional.of(moduleContainer);
            }
        }

        return Optional.empty();
    }

    @Override
    public @NotNull @UnmodifiableView Collection<ModuleContainer> getModules() {
        return Collections.unmodifiableCollection(this.moduleContainers);
    }

    @NotNull
    private List<KeyValueAccessor<Class<?>, Module>> findPossibilityMainClasses(@NotNull Path path, @NotNull URLClassLoader classLoader) {
        List<KeyValueAccessor<Class<?>, Module>> classes = new ArrayList<>();

        try (JarInputStream stream = new JarInputStream(Files.newInputStream(path))) {
            JarEntry entry;
            while ((entry = stream.getNextJarEntry()) != null) {
                if (!entry.getName().endsWith(".class")) {
                    continue;
                }

                String className = entry.getName().replace("/", ".");
                className = className.replaceFirst("(?s)(.*).class", "$1");

                Class<?> clazz;
                try {
                    clazz = classLoader.loadClass(className);
                } catch (final ClassNotFoundException exception) {
                    continue;
                }

                Module module = clazz.getAnnotation(Module.class);
                if (module == null) {
                    continue;
                }

                classes.add(new KeyValueAccessor<>(clazz, module));
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return classes;
    }
}
