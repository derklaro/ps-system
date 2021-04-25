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
package com.github.derklaro.privateservers.cloudnet.v2.legacy.cloud;

import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.api.cloud.util.ConnectionRequest;
import com.github.derklaro.privateservers.cloudnet.v2.legacy.connection.CloudNetV2ConnectionRequest;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudService;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public final class CloudNetV2CloudService extends DefaultCloudService {

  private final ServerInfo serverInfo;

  private CloudNetV2CloudService(@NotNull ServerInfo serverInfo, @NotNull CloudServiceConfiguration cloudServiceConfiguration) {
    super(serverInfo.getServiceId().getServerId(), serverInfo.getServiceId().getUniqueId(), cloudServiceConfiguration);
    this.serverInfo = serverInfo;
  }

  @NotNull
  public static Optional<CloudService> fromServerInfo(@NotNull ServerInfo serverInfo) {
    CloudServiceConfiguration configuration = serverInfo.getServerConfig().getProperties().getObject("cloudServiceConfiguration", CloudServiceConfiguration.class);
    if (configuration == null) {
      return Optional.empty();
    }

    return Optional.of(new CloudNetV2CloudService(serverInfo, configuration));
  }

  @Override
  public @NotNull ConnectionRequest createConnectionRequest(@NotNull UUID targetPlayerUniqueID) {
    return CloudNetV2ConnectionRequest.of(this, targetPlayerUniqueID);
  }

  @Override
  public void publishCloudServiceInfoUpdate() {
    this.serverInfo.getServerConfig().getProperties().append("cloudServiceConfiguration", super.cloudServiceConfiguration);
    CloudAPI.getInstance().update(this.serverInfo);
  }

  @Override
  public void copyCloudService() {
    CloudAPI.getInstance().sendCloudCommand("copy " + this.serverInfo.getServiceId().getServerId());

    try {
      Thread.sleep(5000);
    } catch (InterruptedException ignored) {
    }
  }

  @Override
  public void shutdown() {
    if (super.cloudServiceConfiguration.isAutoSaveBeforeStop()) {
      this.copyCloudService();
    }

    CloudAPI.getInstance().stopServer(this.serverInfo.getServiceId().getServerId());
  }
}
