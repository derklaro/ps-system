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

package com.github.derklaro.privateservers.api.module;

import com.github.derklaro.privateservers.api.module.annotation.ModuleDescription;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * Represents a loaded module in the runtime.
 */
public interface ModuleContainer {
  /**
   * Get the description file of the module.
   *
   * @return the description file of the module.
   */
  @NotNull ModuleDescription getDescription();

  /**
   * Get the current state of the module.
   *
   * @return the current state of the module.
   */
  @NotNull ModuleState getState();

  /**
   * Get the class loader used to load the module main class.
   *
   * @return the class loader used to load the module main class.
   */
  @NotNull URLClassLoader getClassLoader();

  /**
   * Get the path where the module was loaded from.
   *
   * @return the path where the module was loaded from.
   */
  @NotNull Path getModulePath();

  /**
   * Get the main class of the module.
   *
   * @return the main class of the module.
   */
  @NotNull Class<?> getMainClass();

  /**
   * Get the instance of the module created during the load.
   *
   * @return the instance of the module.
   */
  @NotNull Object getInstance();

  /**
   * Sets the current state of this module.
   *
   * @param moduleState the new state of the module.
   */
  @ApiStatus.Internal
  void setModuleState(@NotNull ModuleState moduleState);
}
