/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 - 2021 Pasqual Koschmieder and contributors
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
package com.github.derklaro.privateservers.api.cloud;

import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.cloud.service.creation.CloudServiceCreateConfiguration;
import com.github.derklaro.privateservers.api.cloud.service.ServiceListener;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Cloud service manager which handles all private cloud services which are registered in the cloud
 */
public interface CloudServiceManager {

  @NotNull Unsafe getUnsafe();

  @NotNull Optional<CloudService> getCurrentCloudService();

  @NotNull Optional<CloudService> getCloudServiceByUniqueId(@NotNull UUID uniqueId);

  @NotNull Optional<CloudService> getCloudServiceByName(@NotNull String name);

  @NotNull Optional<CloudService> getCloudServiceByOwnerUniqueId(@NotNull UUID ownerUniqueId);

  @NotNull Optional<CloudService> getCloudServiceByOwnerName(@NotNull String ownerName);

  @NotNull CompletableFuture<CloudService> createCloudService(@NotNull CloudServiceCreateConfiguration configuration);

  @NotNull @UnmodifiableView Collection<CloudService> getPrivateCloudServices();

  void registerServiceListener(@NotNull ServiceListener listener);

  void unregisterServiceListener(@NotNull ServiceListener listener);

  @NotNull @UnmodifiableView Collection<ServiceListener> getServiceListeners();

  interface Unsafe {

    void handleCloudServiceStart(@NotNull CloudService cloudService);

    void handleCloudServiceUpdate(@NotNull CloudService cloudService);

    void handleCloudServiceStop(@NotNull CloudService cloudService);
  }
}
