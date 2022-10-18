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

package com.github.derklaro.privateservers.api.cloud;

import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.cloud.service.ServiceListener;
import com.github.derklaro.privateservers.api.cloud.service.creation.CloudServiceCreateConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Cloud service manager which handles all private cloud services which are registered in the cloud.
 */
public interface CloudServiceManager {
  /**
   * Get the current unsafe instance of this service manager.
   *
   * @return the current unsafe instance of this service manager.
   */
  @ApiStatus.Experimental
  @NotNull Unsafe getUnsafe();

  /**
   * Get all service which were detected in the system.
   *
   * @return all service which were detected in the system.
   */
  @NotNull @UnmodifiableView Collection<CloudService> getCloudServices();

  /**
   * Get the current cloud service on which the manager is running.
   *
   * @return the current cloud service on which the manager is running.
   */
  @NotNull Optional<CloudService> getCurrentCloudService();

  /**
   * Get a cloud service using it's unique id.
   *
   * @param uniqueId the unique id of the cloud service.
   * @return the associated cloud service to the unique id.
   */
  @NotNull Optional<CloudService> getCloudServiceByUniqueId(@NotNull UUID uniqueId);

  /**
   * Get a cloud service using it's name.
   *
   * @param name the name of the cloud service.
   * @return the associated cloud service to the name.
   */
  @NotNull Optional<CloudService> getCloudServiceByName(@NotNull String name);

  /**
   * Get a cloud service of which the provided player is the owner.
   *
   * @param ownerUniqueId the unique id of the player.
   * @return the cloud service of which the player is the owner.
   */
  @NotNull Optional<CloudService> getCloudServiceByOwnerUniqueId(@NotNull UUID ownerUniqueId);

  /**
   * Get a cloud service of which the provided player is the owner.
   *
   * @param ownerName the name of the player.
   * @return the cloud service of which the player is the owner.
   */
  @NotNull Optional<CloudService> getCloudServiceByOwnerName(@NotNull String ownerName);

  /**
   * Creates a new cloud service and wraps it into the an cloud service object.
   *
   * @param configuration the configuration on which the service should be based.
   * @return a future completed with the wrapped service.
   */
  @NotNull CompletableFuture<CloudService> createCloudService(@NotNull CloudServiceCreateConfiguration configuration);

  /**
   * Registers a service listener to this manager.
   *
   * @param listener the listener to register.
   */
  void registerServiceListener(@NotNull ServiceListener listener);

  /**
   * Unregisters a service listener from this manager.
   *
   * @param listener the listener to unregister.
   */
  void unregisterServiceListener(@NotNull ServiceListener listener);

  /**
   * Get all added service listeners to this manager.
   *
   * @return all added service listeners to this manager.
   */
  @NotNull @UnmodifiableView Collection<ServiceListener> getServiceListeners();

  /**
   * Provides access to internal api functionalities which may be helpful in certain edge cases.
   */
  interface Unsafe {
    /**
     * Handles the start of a cloud service.
     *
     * @param cloudService the service which started.
     */
    void handleCloudServiceStart(@NotNull CloudService cloudService);

    /**
     * Handles the update of a cloud service.
     *
     * @param cloudService the updated service.
     */
    void handleCloudServiceUpdate(@NotNull CloudService cloudService);

    /**
     * Handles the stop of a service.
     *
     * @param cloudService the service which stopped.
     */
    void handleCloudServiceStop(@NotNull CloudService cloudService);
  }
}
