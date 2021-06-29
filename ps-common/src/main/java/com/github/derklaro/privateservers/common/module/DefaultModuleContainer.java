/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2021 Pasqual K. and contributors
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

package com.github.derklaro.privateservers.common.module;

import com.github.derklaro.privateservers.api.module.ModuleContainer;
import com.github.derklaro.privateservers.api.module.ModuleState;
import com.github.derklaro.privateservers.api.module.annotation.ModuleDescription;
import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;
import java.nio.file.Path;

class DefaultModuleContainer implements ModuleContainer {

  private final ModuleDescription description;
  private final Class<?> mainClass;
  private final Object instance;
  private final URLClassLoader classLoader;
  private final Path path;
  private ModuleState state;

  DefaultModuleContainer(ModuleDescription description, Class<?> mainClass, URLClassLoader classLoader, Path path, Object instance) {
    this.description = description;
    this.mainClass = mainClass;
    this.classLoader = classLoader;
    this.path = path;
    this.instance = instance;
    this.state = ModuleState.ENABLED;
  }

  @NotNull
  @Override
  public ModuleDescription getDescription() {
    return this.description;
  }

  @Override
  public @NotNull ModuleState getState() {
    return this.state;
  }

  @Override
  public @NotNull URLClassLoader getClassLoader() {
    return this.classLoader;
  }

  @Override
  public @NotNull Path getModulePath() {
    return this.path;
  }

  @Override
  public @NotNull Class<?> getMainClass() {
    return this.mainClass;
  }

  @Override
  public @NotNull Object getInstance() {
    return this.instance;
  }

  @Override
  public void setModuleState(@NotNull ModuleState moduleState) {
    this.state = moduleState;
  }
}
