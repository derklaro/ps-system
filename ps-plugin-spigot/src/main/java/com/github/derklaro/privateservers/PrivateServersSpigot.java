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
package com.github.derklaro.privateservers;

import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.cloud.CloudDetector;
import com.github.derklaro.privateservers.api.module.ModuleLoader;
import com.github.derklaro.privateservers.api.task.TaskManager;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudSystemDetector;
import com.github.derklaro.privateservers.common.module.DefaultModuleLoader;
import com.github.derklaro.privateservers.task.SpigotTaskManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PrivateServersSpigot extends JavaPlugin implements Plugin {

    private static PrivateServersSpigot instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    public static PrivateServersSpigot getInstance() {
        return instance;
    }

    @Override
    public @NotNull TaskManager getTaskManager() {
        return SpigotTaskManager.INSTANCE;
    }

    @Override
    public @NotNull CloudDetector getCloudSystemDetector() {
        return DefaultCloudSystemDetector.DEFAULT_INSTANCE;
    }

    @Override
    public @NotNull ModuleLoader getModuleLoader() {
        return DefaultModuleLoader.INSTANCE;
    }
}
