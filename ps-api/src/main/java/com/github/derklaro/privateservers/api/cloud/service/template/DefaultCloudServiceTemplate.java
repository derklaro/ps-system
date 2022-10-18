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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final class DefaultCloudServiceTemplate implements CloudServiceTemplate {

  private final String templateName;
  private final String templateBackend;

  public DefaultCloudServiceTemplate(String templateName, String templateBackend) {
    this.templateName = Objects.requireNonNull(templateName, "templateName");
    this.templateBackend = Objects.requireNonNull(templateBackend, "templateBackend");
  }

  @Override
  public @NotNull String templateName() {
    return this.templateName;
  }

  @Override
  public @NotNull String templateBackend() {
    return this.templateBackend;
  }

  @Override
  public @NotNull CloudServiceTemplate.Builder toBuilder() {
    return CloudServiceTemplate.builder()
      .templateName(this.templateName)
      .templateBackend(this.templateBackend);
  }

  public static final class DefaultBuilder implements CloudServiceTemplate.Builder {

    private String templateName;
    private String templateBackend;

    @Override
    public @NotNull Builder templateName(@NotNull String templateName) {
      this.templateName = Objects.requireNonNull(templateName, "templateName");
      return this;
    }

    @Override
    public @NotNull Builder templateBackend(@NotNull String templateBackend) {
      this.templateBackend = Objects.requireNonNull(templateBackend, "templateBackend");
      return this;
    }

    @Override
    public @NotNull CloudServiceTemplate build() {
      return new DefaultCloudServiceTemplate(this.templateName, this.templateBackend);
    }
  }
}
