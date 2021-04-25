/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 - 2021 Pasqual Koschmieder and contributors
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
import com.github.derklaro.privateservers.api.module.ModuleState;
import com.github.derklaro.privateservers.api.module.annotation.ModuleDescription;
import com.github.derklaro.privateservers.api.module.exception.ModuleMainClassNotFoundException;
import com.github.derklaro.privateservers.common.util.Iterables;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class DefaultModuleLoader implements ModuleLoader {

  public static final ModuleLoader INSTANCE = new DefaultModuleLoader();
  private static final Path MODULE_PATH = Paths.get("plugins/ps/modules");

  private final Collection<Path> toLoad = new CopyOnWriteArrayList<>();
  private final Collection<ModuleContainer> moduleContainers = new CopyOnWriteArrayList<>();

  @Override
  public void detectModules() {
    if (Files.notExists(MODULE_PATH)) {
      try {
        Files.createDirectories(MODULE_PATH);
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(MODULE_PATH, path -> path.toString().endsWith(".jar"))) {
      for (Path path : stream) {
        this.toLoad.add(path);
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void loadModules(@NotNull Plugin loader) throws ModuleMainClassNotFoundException {
    for (Path path : this.toLoad) {
      try {
        ModuleDescription description = this.findModuleDescription(path);
        if (description == null) {
          throw new ModuleMainClassNotFoundException(path);
        }

        URLClassLoader classLoader = AccessController.doPrivileged(
          (PrivilegedExceptionAction<URLClassLoader>) () -> new URLClassLoader(new URL[]{path.toUri().toURL()}));

        Object instance;
        try {
          instance = classLoader.loadClass(description.getMainClass())
            .getDeclaredConstructor(Plugin.class).newInstance(loader);
        } catch (ClassNotFoundException exception) {
          throw new ModuleMainClassNotFoundException(description.getMainClass(), path);
        } catch (NoSuchMethodException exception) {
          System.err.println("No constructor with plugin as single argument in module " + path);
          continue;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
          exception.printStackTrace();
          continue;
        }

        ModuleContainer container = new DefaultModuleContainer(
          description,
          instance.getClass(),
          classLoader,
          path,
          instance
        );
        this.moduleContainers.add(container);
      } catch (PrivilegedActionException exception) {
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
        moduleContainer.setModuleState(ModuleState.DISABLED);
      } catch (IOException exception) {
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
    return Iterables.first(this.moduleContainers, moduleContainer -> moduleContainer.getDescription().getId().equals(id));
  }

  @Override
  public @NotNull @UnmodifiableView Collection<ModuleContainer> getModules() {
    return Collections.unmodifiableCollection(this.moduleContainers);
  }

  @Nullable
  private ModuleDescription findModuleDescription(@NotNull Path path) {
    try (JarFile jarFile = new JarFile(path.toFile())) {
      ZipEntry descriptionEntry = jarFile.getEntry("ps_module.json");
      if (descriptionEntry != null) {
        try (Reader reader = new InputStreamReader(jarFile.getInputStream(descriptionEntry), StandardCharsets.UTF_8)) {
          return new Gson().fromJson(reader, ModuleDescription.class);
        }
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return null;
  }
}
