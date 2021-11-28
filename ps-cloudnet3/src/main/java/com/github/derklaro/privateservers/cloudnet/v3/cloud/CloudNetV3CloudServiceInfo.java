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

package com.github.derklaro.privateservers.cloudnet.v3.cloud;

import com.github.derklaro.privateservers.api.cloud.service.CloudServiceInfo;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

final class CloudNetV3CloudServiceInfo implements CloudServiceInfo {

  private final ServiceInfoSnapshot handle;

  public CloudNetV3CloudServiceInfo(ServiceInfoSnapshot handle) {
    this.handle = handle;
  }

  @Override
  public @NotNull InetSocketAddress getAddress() {
    return new InetSocketAddress(this.handle.getAddress().getHost(), this.handle.getAddress().getPort());
  }

  @Override
  public @NotNull String getMessageOfTheDay() {
    return BridgeServiceProperty.MOTD.get(this.handle).orElse("");
  }

  @Override
  public @NotNull String getState() {
    return BridgeServiceProperty.STATE.get(this.handle).orElse("");
  }

  @Override
  public int getOnlineCount() {
    return BridgeServiceProperty.ONLINE_COUNT.get(this.handle).orElse(0);
  }

  @Override
  public int getMaximumPlayerCount() {
    return BridgeServiceProperty.MAX_PLAYERS.get(this.handle).orElse(0);
  }

  @Override
  public int getHeapMemory() {
    return this.handle.getConfiguration().getProcessConfig().getMaxHeapMemorySize();
  }
}
