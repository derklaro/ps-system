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

package com.github.derklaro.privateservers.lobby.inventory;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.cloud.service.ServiceListener;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

final class InventoryRebuildListener implements ServiceListener {

  private final Configuration configuration;
  private final CloudServiceManager cloudServiceManager;
  private final PublicServersInventoryHandler inventoryHandler;

  public InventoryRebuildListener(Configuration configuration, CloudServiceManager cloudServiceManager, PublicServersInventoryHandler inventoryHandler) {
    this.configuration = configuration;
    this.cloudServiceManager = cloudServiceManager;
    this.inventoryHandler = inventoryHandler;
  }

  @Override
  public void handleServiceRegister(@NotNull CloudService cloudService) {
    this.inventoryHandler.rebuildPublicServerListInventory(this.configuration, this.cloudServiceManager);
  }

  @Override
  public void handleServerUpdate(@NotNull CloudService cloudService) {
    this.inventoryHandler.rebuildPublicServerListInventory(this.configuration, this.cloudServiceManager);
  }

  @Override
  public void handleServiceUnregister(@NotNull CloudService cloudService) {
    this.inventoryHandler.rebuildPublicServerListInventory(this.configuration, this.cloudServiceManager);
  }
}
