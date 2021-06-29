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

package com.github.derklaro.privateservers.api.cloud.service.creation;

import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.service.template.CloudServiceTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final class DefaultCloudServiceCreateConfiguration implements CloudServiceCreateConfiguration {

  private final String group;
  private final CloudServiceTemplate template;
  private final CloudServiceConfiguration privateServerConfiguration;

  public DefaultCloudServiceCreateConfiguration(String group, CloudServiceTemplate template,
                                                CloudServiceConfiguration privateServerConfiguration) {
    this.group = Objects.requireNonNull(group, "group");
    this.template = Objects.requireNonNull(template, "template");
    this.privateServerConfiguration = Objects.requireNonNull(privateServerConfiguration, "privateServerConfiguration");
  }

  @Override
  public @NotNull String group() {
    return this.group;
  }

  @Override
  public @NotNull CloudServiceTemplate template() {
    return this.template;
  }

  @Override
  public @NotNull CloudServiceConfiguration privateServerConfiguration() {
    return this.privateServerConfiguration;
  }

  @Override
  public @NotNull CloudServiceCreateConfiguration.Builder toBuilder() {
    return CloudServiceCreateConfiguration.builder()
      .group(this.group)
      .template(this.template)
      .privateServerConfiguration(this.privateServerConfiguration);
  }

  public static final class DefaultBuilder implements CloudServiceCreateConfiguration.Builder {

    private String group;
    private CloudServiceTemplate template;
    private CloudServiceConfiguration privateServerConfiguration;

    @Override
    public @NotNull Builder group(@NotNull String group) {
      this.group = Objects.requireNonNull(group, "group");
      return this;
    }

    @Override
    public @NotNull Builder template(@NotNull CloudServiceTemplate template) {
      this.template = Objects.requireNonNull(template, "template");
      return this;
    }

    @Override
    public @NotNull Builder privateServerConfiguration(@NotNull CloudServiceConfiguration cloudServiceConfiguration) {
      this.privateServerConfiguration = Objects.requireNonNull(cloudServiceConfiguration, "cloudServiceConfiguration");
      return this;
    }

    @Override
    public @NotNull CloudServiceCreateConfiguration build() {
      return new DefaultCloudServiceCreateConfiguration(this.group, this.template, this.privateServerConfiguration);
    }
  }
}
