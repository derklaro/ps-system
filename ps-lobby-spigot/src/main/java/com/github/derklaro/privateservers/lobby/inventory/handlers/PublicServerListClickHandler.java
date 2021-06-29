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

package com.github.derklaro.privateservers.lobby.inventory.handlers;

import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.InventoryConfiguration;
import com.github.derklaro.privateservers.lobby.inventory.ClickHandler;
import com.github.derklaro.privateservers.lobby.inventory.InventoryHandler;
import com.github.derklaro.privateservers.lobby.inventory.PublicServersInventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PublicServerListClickHandler implements ClickHandler {

  private final PublicServersInventoryHandler inventoryHandler;
  private final InventoryConfiguration.PublicServerListConfiguration configuration;

  public PublicServerListClickHandler(Configuration configuration, InventoryHandler inventoryHandler) {
    this.inventoryHandler = inventoryHandler.getPublicServersInventoryHandler();
    this.configuration = configuration.getPublicServerListConfiguration();
  }

  @Override
  public boolean handleClick(@NotNull Player player, @NotNull Inventory inventory, @NotNull ItemStack itemStack, int slot) {
    CloudService cloudService = this.inventoryHandler.getTargetService(slot);
    if (cloudService != null) {
      if (this.shouldSend(player, cloudService)) {
        cloudService.createConnectionRequest(player.getUniqueId()).fire();
      } else {
        HandlerUtils.notifyNotAllowed(player);
      }
    }
    return true;
  }

  private boolean shouldSend(@NotNull Player player, @NotNull CloudService cloudService) {
    InventoryConfiguration.ItemLayout itemLayout;
    if (cloudService.getCloudServiceConfiguration().isWhitelist()) {
      itemLayout = this.configuration.getServerWithWhitelistLayout();
    } else {
      itemLayout = this.configuration.getOpenServerLayout();
    }

    return HandlerUtils.canUse(player, itemLayout);
  }
}
