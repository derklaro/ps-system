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

package com.github.derklaro.privateservers.api.cloud.service;

import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.connection.ConnectionRequest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a service running in the embedding system.
 */
public interface CloudService {
  /**
   * Get the fully qualified name of the service.
   *
   * @return the fully qualified name of the service.
   */
  @NotNull String getName();

  /**
   * Get the unique id of the service.
   *
   * @return the unique id of the service.
   */
  @NotNull UUID getServiceUniqueId();

  /**
   * Get the unique id of the private server owner.
   *
   * @return the unique id of the private server owner.
   */
  @NotNull UUID getOwnerUniqueId();

  /**
   * Get the name of the private server owner.
   *
   * @return the name of the private server owner.
   */
  @NotNull String getOwnerName();

  /**
   * Get the configuration of this private service.
   *
   * @return the configuration of this private service.
   */
  @NotNull CloudServiceConfiguration getCloudServiceConfiguration();

  /**
   * Sets the configuration of this private service. Updating of this service using
   * {@link #publishCloudServiceInfoUpdate()} is required in order for every other service
   * to get the change.
   *
   * @param cloudServiceConfiguration the new configuration to use
   */
  void setCloudServiceConfiguration(@NotNull CloudServiceConfiguration cloudServiceConfiguration);

  /**
   * Creates a connection request targeting this service.
   *
   * @param targetPlayerUniqueID the unique id of the player to connect.
   * @return the created connection request.
   */
  @NotNull ConnectionRequest createConnectionRequest(@NotNull UUID targetPlayerUniqueID);

  /**
   * Get a wrapper around more specific information of this service.
   *
   * @return a wrapper around more specific information of this service.
   */
  @NotNull CloudServiceInfo getServiceInfo();

  /**
   * Publishes an update of this service configuration into the embedding system.
   */
  void publishCloudServiceInfoUpdate();

  /**
   * Copies all files of this cloud service to the template used to create the service.
   */
  void copyCloudService();

  /**
   * Shuts the service down.
   */
  void shutdown();
}
