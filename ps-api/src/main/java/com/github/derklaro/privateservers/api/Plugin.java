/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2022 Pasqual K. and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.derklaro.privateservers.api;

import com.github.derklaro.privateservers.api.cloud.CloudDetector;
import com.github.derklaro.privateservers.api.module.ModuleLoader;
import com.github.derklaro.privateservers.api.task.TaskManager;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a plugin running on any platform. It holds general abstractions for common api methods which may be
 * required for different platforms or modules.
 */
public interface Plugin {
  /**
   * Get the task manager instance of the platform the plugin is running on.
   *
   * @return the task manager instance.
   */
  @NotNull TaskManager getTaskManager();

  /**
   * Get the embedded cloud system detector, used to identify which underlying sub-system should be used to
   * start and manage services. The detector will (when initialized) also hold the current detected cloud system
   * instance.
   *
   * @return the embedded cloud system detector.
   */
  @NotNull CloudDetector getCloudSystemDetector();

  /**
   * Get the loader for the modules which loaded all modules supporting the base system.
   *
   * @return the current module loader instance.
   */
  @NotNull ModuleLoader getModuleLoader();
}
