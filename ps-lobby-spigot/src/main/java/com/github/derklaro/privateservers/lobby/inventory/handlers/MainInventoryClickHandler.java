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
package com.github.derklaro.privateservers.lobby.inventory.handlers;

import com.github.derklaro.privateservers.PrivateServersSpigot;
import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.InventoryConfiguration;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.lobby.inventory.ClickHandler;
import com.github.derklaro.privateservers.lobby.inventory.InventoryHandler;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class MainInventoryClickHandler implements ClickHandler {

  private final InventoryHandler inventoryHandler;
  private final InventoryConfiguration.MainMenuConfiguration configuration;

  private final CloudServiceManager cloudServiceManager;

  public MainInventoryClickHandler(Configuration configuration, InventoryHandler inventoryHandler, CloudServiceManager cloudServiceManager) {
    this.inventoryHandler = inventoryHandler;
    this.cloudServiceManager = cloudServiceManager;
    this.configuration = configuration.getMainMenuConfiguration();
  }

  @Override
  public boolean handleClick(@NotNull Player player, @NotNull Inventory inventory, @NotNull ItemStack itemStack, int slot) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta.hasDisplayName()) {
      if (itemMeta.getDisplayName().equals(this.configuration.getStartServerLayout().getDisplayName())) {
        this.handleStartItemClick(player);
      } else if (itemMeta.getDisplayName().equals(this.configuration.getStopServerLayout().getDisplayName())) {
        this.handleStopItemClick(player);
      } else if (itemMeta.getDisplayName().equals(this.configuration.getJoinServerLayout().getDisplayName())) {
        this.handleConnectItemClick(player);
      } else if (itemMeta.getDisplayName().equals(this.configuration.getPublicServerListLayout().getDisplayName())) {
        this.handlePublicServerListItemClick(player);
      }
    }

    return true;
  }

  private void handleStartItemClick(@NotNull Player player) {
    InventoryConfiguration.ItemLayout startLayout = this.configuration.getStartServerLayout();
    if (HandlerUtils.canUse(player, startLayout)) {
      this.inventoryHandler.openStartTypesInventory(player);
    } else {
      HandlerUtils.notifyNotAllowed(player);
    }
  }

  private void handleStopItemClick(@NotNull Player player) {
    InventoryConfiguration.ItemLayout stopLayout = this.configuration.getStopServerLayout();
    if (HandlerUtils.canUse(player, stopLayout)) {
      player.closeInventory();

      CloudService service = this.cloudServiceManager.getCloudServiceByOwnerUniqueId(player.getUniqueId()).orElse(null);
      if (service == null) {
        BukkitComponentRenderer.renderAndSend(player, Message.NO_RUNNING_SERVER.build());
      } else {
        Bukkit.getScheduler().runTaskAsynchronously(PrivateServersSpigot.getInstance(), service::shutdown);
        BukkitComponentRenderer.renderAndSend(player, Message.SERVICE_NOW_STOPPING.build());
      }
    } else {
      HandlerUtils.notifyNotAllowed(player);
    }
  }

  private void handleConnectItemClick(@NotNull Player player) {
    InventoryConfiguration.ItemLayout joinLayout = this.configuration.getJoinServerLayout();
    if (HandlerUtils.canUse(player, joinLayout)) {
      player.closeInventory();

      CloudService cloudService = this.cloudServiceManager.getCloudServiceByOwnerUniqueId(player.getUniqueId()).orElse(null);
      if (cloudService != null) {
        BukkitComponentRenderer.renderAndSend(player, Message.NOW_CONNECTING.build());
        cloudService.createConnectionRequest(player.getUniqueId()).fire();
      } else {
        BukkitComponentRenderer.renderAndSend(player, Message.NO_RUNNING_SERVER.build());
      }
    } else {
      HandlerUtils.notifyNotAllowed(player);
    }
  }

  private void handlePublicServerListItemClick(@NotNull Player player) {
    InventoryConfiguration.ItemLayout publicServerListLayout = this.configuration.getPublicServerListLayout();
    if (HandlerUtils.canUse(player, publicServerListLayout)) {
      this.inventoryHandler.getPublicServersInventoryHandler().openPublicServersInventory(player);
    } else {
      HandlerUtils.notifyNotAllowed(player);
    }
  }
}
