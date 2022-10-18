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

package com.github.derklaro.privateservers.api.cloud.service.template;

import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a template which holds files for a service.
 */
public interface CloudServiceTemplate extends Buildable<CloudServiceTemplate, CloudServiceTemplate.Builder> {
  /**
   * Creates a new builder instance for a template.
   *
   * @return a new builder instance for a template.
   */
  static @NotNull CloudServiceTemplate.Builder builder() {
    return new DefaultCloudServiceTemplate.DefaultBuilder();
  }

  /**
   * Get the name of the template.
   *
   * @return the name of the template.
   */
  @NotNull String templateName();

  /**
   * Get the backend name of the template.
   *
   * @return the backend name of the template.
   */
  @NotNull String templateBackend();

  /**
   * Represents a builder for a template.
   */
  interface Builder extends Buildable.Builder<CloudServiceTemplate> {
    /**
     * Sets the name of the template.
     *
     * @param templateName the name of the template.
     * @return the same instance of this class, for chaining.
     */
    @NotNull Builder templateName(@NotNull String templateName);

    /**
     * Sets the name of the backend the template is located in.
     *
     * @param templateBackend the name of the backend the template is located in.
     * @return the same instance of this class, for chaining.
     */
    @NotNull Builder templateBackend(@NotNull String templateBackend);
  }
}
