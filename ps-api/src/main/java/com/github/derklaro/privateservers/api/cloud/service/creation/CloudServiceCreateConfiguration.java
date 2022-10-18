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

package com.github.derklaro.privateservers.api.cloud.service.creation;

import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.service.template.CloudServiceTemplate;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a service configuration which is used to create a new service.
 */
public interface CloudServiceCreateConfiguration
  extends Buildable<CloudServiceCreateConfiguration, CloudServiceCreateConfiguration.Builder> {
  /**
   * Get a new builder instance for the configuration.
   *
   * @return a new builder instance.
   */
  static Builder builder() {
    return new DefaultCloudServiceCreateConfiguration.DefaultBuilder();
  }

  /**
   * Get the group of which the server should be.
   *
   * @return the group of which the server should be.
   */
  @NotNull String group();

  /**
   * Get the template which should be used to create the new cloud service.
   *
   * @return the template which should be used to create the new cloud service.
   */
  @NotNull CloudServiceTemplate template();

  /**
   * Get the initial configuration for the new cloud service.
   *
   * @return the initial configuration for the new cloud service.
   */
  @NotNull CloudServiceConfiguration privateServerConfiguration();

  /**
   * Represents a builder for a create configuration.
   */
  interface Builder extends Buildable.Builder<CloudServiceCreateConfiguration> {
    /**
     * Sets the group which should be used to create the service on.
     *
     * @param group the group which should be used to create the service on.
     * @return the same instance of this class, for chaining.
     */
    @NotNull Builder group(@NotNull String group);

    /**
     * Sets the template which should be used to create the cloud service from.
     *
     * @param template the template which should be used to create the cloud service from.
     * @return the same instance of this class, for chaining.
     */
    @NotNull Builder template(@NotNull CloudServiceTemplate template);

    /**
     * Sets the initial configuration of the private service.
     *
     * @param cloudServiceConfiguration the initial configuration to use when creating the service.
     * @return the same instance of this class, for chaining.
     */
    @NotNull Builder privateServerConfiguration(@NotNull CloudServiceConfiguration cloudServiceConfiguration);
  }
}
