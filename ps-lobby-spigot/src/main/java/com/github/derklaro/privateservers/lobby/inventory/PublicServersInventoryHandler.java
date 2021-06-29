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

package com.github.derklaro.privateservers.lobby.inventory;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.InventoryConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PublicServersInventoryHandler {

  private final InventoryHandler inventoryHandler;
  private final Map<Integer, CloudService> serviceToSlotMappings;

  protected volatile Inventory publicServersInventory;

  public PublicServersInventoryHandler(InventoryHandler inventoryHandler, CloudServiceManager cloudServiceManager) {
    this.inventoryHandler = inventoryHandler;
    this.serviceToSlotMappings = new ConcurrentHashMap<>();
    this.publicServersInventory = this.rebuildPublicServerListInventory(inventoryHandler.configuration, cloudServiceManager);
  }

  @NotNull
  protected Inventory rebuildPublicServerListInventory(@NotNull Configuration configuration, @NotNull CloudServiceManager manager) {
    InventoryConfiguration.PublicServerListConfiguration serverListConfiguration = configuration.getPublicServerListConfiguration();

    Inventory inventory;
    if (this.publicServersInventory == null) {
      inventory = Bukkit.createInventory(null,
        serverListConfiguration.getInventorySize(), serverListConfiguration.getInventoryTitle());
    } else {
      inventory = this.publicServersInventory;
      inventory.clear();
    }

    Map<Integer, CloudService> newMappings = new ConcurrentHashMap<>();

    int addedItemsAmount = 0;
    for (CloudService cloudService : manager.getCloudServices()) {
      if (cloudService.getCloudServiceConfiguration().isPublicService()) {
        if (++addedItemsAmount >= inventory.getSize()) {
          break;
        } else {
          int resultingSlot;
          if (cloudService.getCloudServiceConfiguration().isWhitelist()) {
            resultingSlot = this.inventoryHandler.putItem(inventory,
              serverListConfiguration.getServerWithWhitelistLayout(), cloudService);
          } else {
            resultingSlot = this.inventoryHandler.putItem(inventory,
              serverListConfiguration.getOpenServerLayout(), cloudService);
          }

          if (resultingSlot >= 0) {
            newMappings.put(resultingSlot, cloudService);
          }
        }
      }
    }

    this.serviceToSlotMappings.clear();
    this.serviceToSlotMappings.putAll(newMappings);

    return this.publicServersInventory = inventory;
  }

  public @Nullable CloudService getTargetService(int slot) {
    return this.serviceToSlotMappings.get(slot);
  }

  public void openPublicServersInventory(@NotNull Player player) {
    player.openInventory(this.publicServersInventory);
  }
}
