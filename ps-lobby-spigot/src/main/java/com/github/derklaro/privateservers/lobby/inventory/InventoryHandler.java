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
package com.github.derklaro.privateservers.lobby.inventory;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.InventoryConfiguration;
import com.github.derklaro.privateservers.lobby.inventory.handlers.MainInventoryClickHandler;
import com.github.derklaro.privateservers.lobby.inventory.handlers.PublicServerListClickHandler;
import com.github.derklaro.privateservers.lobby.inventory.handlers.ServiceStartInventoryClickHandler;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

public class InventoryHandler {

  protected final Inventory mainInventory;
  protected final Inventory startTypeInventory;
  protected final Configuration configuration;

  protected ClickHandler mainInventoryClickHandler;
  protected ClickHandler serviceStartInventoryClickHandler;
  protected ClickHandler publicServersInventoryClickHandler;

  protected PublicServersInventoryHandler publicServersInventoryHandler;

  public InventoryHandler(Configuration configuration, CloudServiceManager manager) {
    this.configuration = configuration;

    this.mainInventory = this.buildMainInventory(configuration);
    this.startTypeInventory = this.buildStartInventory(configuration);
    this.publicServersInventoryHandler = new PublicServersInventoryHandler(this, manager);

    this.mainInventoryClickHandler = new MainInventoryClickHandler(configuration, this, manager);
    this.serviceStartInventoryClickHandler = new ServiceStartInventoryClickHandler(manager, configuration);
    this.publicServersInventoryClickHandler = new PublicServerListClickHandler(configuration, this);

    this.publicServersInventoryClickHandler = new PublicServerListClickHandler(configuration, this);
    manager.registerServiceListener(new InventoryRebuildListener(configuration, manager, this.publicServersInventoryHandler));
  }

  public void openMainInventory(@NotNull Player player) {
    player.openInventory(this.mainInventory);
  }

  public void openStartTypesInventory(@NotNull Player player) {
    player.openInventory(this.startTypeInventory);
  }

  public void handleInventoryClick(@NotNull InventoryClickEvent event) {
    ItemStack itemStack = event.getCurrentItem();
    if (event.getWhoClicked() instanceof Player && itemStack != null && itemStack.hasItemMeta()) {
      event.setCancelled(this.handleInventoryClick((Player) event.getWhoClicked(), event.getClickedInventory(),
        itemStack, event.getSlot()));
    }
  }

  public boolean handleInventoryClick(@NotNull Player player, @NotNull Inventory inventory, @NotNull ItemStack item, int slot) {
    if (inventory.equals(this.mainInventory)) {
      return this.mainInventoryClickHandler.handleClick(player, inventory, item, slot);
    } else if (inventory.getTitle() != null) {
      if (inventory.getTitle().equals(this.configuration.getPublicServerListConfiguration().getInventoryTitle())) {
        return this.publicServersInventoryClickHandler.handleClick(player, inventory, item, slot);
      } else if (inventory.getTitle().equals(this.configuration.getServiceTemplateStartItems().getInventoryTitle())) {
        return this.serviceStartInventoryClickHandler.handleClick(player, inventory, item, slot);
      }
    }

    return false;
  }

  public PublicServersInventoryHandler getPublicServersInventoryHandler() {
    return this.publicServersInventoryHandler;
  }

  @NotNull
  protected Inventory buildMainInventory(@NotNull Configuration configuration) {
    InventoryConfiguration.MainMenuConfiguration inventoryConfiguration = configuration.getMainMenuConfiguration();
    Inventory inventory = Bukkit.createInventory(null,
      inventoryConfiguration.getInventorySize(), inventoryConfiguration.getInventoryTitle());

    this.putItem(inventory, inventoryConfiguration.getStartServerLayout());
    this.putItem(inventory, inventoryConfiguration.getStopServerLayout());
    this.putItem(inventory, inventoryConfiguration.getJoinServerLayout());
    this.putItem(inventory, inventoryConfiguration.getPublicServerListLayout());

    return inventory;
  }

  @NotNull
  protected Inventory buildStartInventory(@NotNull Configuration configuration) {
    InventoryConfiguration.ServiceTypeStartInventory inventoryConfiguration = configuration.getServiceTemplateStartItems();
    Inventory inventory = Bukkit.createInventory(null,
      inventoryConfiguration.getInventorySize(), inventoryConfiguration.getInventoryTitle());

    for (InventoryConfiguration.ServiceItemMapping serviceItem : inventoryConfiguration.getServiceItems()) {
      Preconditions.checkArgument(serviceItem.getItemLayout().getSlot() >= 0, "Slot must be defined");
      this.putItem(inventory, serviceItem.getItemLayout());
    }

    return inventory;
  }

  protected int putItem(@NotNull Inventory inventory, @NotNull InventoryConfiguration.ItemLayout itemLayout) {
    return this.putItem(inventory, itemLayout, null);
  }

  protected int putItem(@NotNull Inventory inventory, @NotNull InventoryConfiguration.ItemLayout itemLayout, @Nullable CloudService service) {
    ItemStack itemStack = this.provideItemStack(itemLayout, service);
    if (itemStack != null) {
      int slot = itemLayout.getSlot();
      if (slot < 0) {
        slot = inventory.firstEmpty();
      }

      if (slot >= 0 && inventory.getSize() > slot) {
        inventory.setItem(slot, itemStack);
        return slot;
      }
    }
    return -1;
  }

  @Nullable
  protected ItemStack provideItemStack(@NotNull InventoryConfiguration.ItemLayout layout) {
    return this.provideItemStack(layout, null);
  }

  @Nullable
  protected ItemStack provideItemStack(@NotNull InventoryConfiguration.ItemLayout layout, @Nullable CloudService cloudService) {
    Material material = Material.getMaterial(layout.getMaterial());
    if (material != null) {
      ItemStack itemStack;
      if (layout.getSubId() == -1) {
        itemStack = new ItemStack(material);
      } else {
        itemStack = new ItemStack(material, 1, (byte) layout.getSubId());
      }

      ItemMeta itemMeta = itemStack.getItemMeta();
      if (itemMeta != null) {
        if (cloudService != null) {
          itemMeta.setDisplayName(this.replacePlaceHolders(layout.getDisplayName(), cloudService));
          itemMeta.setLore(layout.getLore().stream()
            .map(loreLine -> this.replacePlaceHolders(loreLine, cloudService))
            .collect(Collectors.toList()));
        } else {
          itemMeta.setDisplayName(layout.getDisplayName());
          itemMeta.setLore(layout.getLore());
        }

        itemStack.setItemMeta(itemMeta);
      }

      return itemStack;
    }
    return null;
  }

  protected @NotNull String replacePlaceHolders(@NotNull String input, @NotNull CloudService service) {
    return input
      .replace("%owner_name%", service.getOwnerName())
      .replace("%owner_uuid%", service.getOwnerUniqueId().toString())
      .replace("%service_name%", service.getName())
      .replace("%service_uuid%", service.getServiceUniqueId().toString())
      .replace("%service_group%", service.getCloudServiceConfiguration().getInitialGroup())
      .replace("%service_template%", service.getCloudServiceConfiguration().getInitialTemplate())
      .replace("%service_template_backend%", service.getCloudServiceConfiguration().getInitialTemplateBackend());
  }
}
