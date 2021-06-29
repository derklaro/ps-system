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

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.service.creation.CloudServiceCreateConfiguration;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.InventoryConfiguration;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.lobby.inventory.ClickHandler;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class ServiceStartInventoryClickHandler implements ClickHandler {

  private final CloudServiceManager cloudServiceManager;
  private final InventoryConfiguration.ServiceTypeStartInventory startConfiguration;

  private final Set<UUID> waitingPlayers = new CopyOnWriteArraySet<>();

  public ServiceStartInventoryClickHandler(CloudServiceManager cloudServiceManager, Configuration configuration) {
    this.cloudServiceManager = cloudServiceManager;
    this.startConfiguration = configuration.getServiceTemplateStartItems();
  }

  @Override
  public boolean handleClick(@NotNull Player player, @NotNull Inventory inventory, @NotNull ItemStack itemStack, int slot) {
    for (InventoryConfiguration.ServiceItemMapping serviceItem : this.startConfiguration.getServiceItems()) {
      if (serviceItem.getItemLayout().getSlot() == slot) {
        this.startService(player, serviceItem);
        break;
      }
    }

    return true;
  }

  private void startService(@NotNull Player player, @NotNull InventoryConfiguration.ServiceItemMapping mapping) {
    if (HandlerUtils.canUse(player, mapping.getItemLayout())) {
      player.closeInventory();
      if (this.waitingPlayers.contains(player.getUniqueId())
        || this.cloudServiceManager.getCloudServiceByOwnerUniqueId(player.getUniqueId()).isPresent()) {
        BukkitComponentRenderer.renderAndSend(player, Message.ALREADY_SERVICE_RUNNING.build());
        return;
      }

      this.waitingPlayers.add(player.getUniqueId()); // disallow service starting for that player until we get a result from the cloud
      BukkitComponentRenderer.renderAndSend(player, Message.SERVICE_CREATED.build());

      this.cloudServiceManager.createCloudService(CloudServiceCreateConfiguration.builder()
        .group(mapping.getGroupName())
        .template(mapping.toTemplate())
        .privateServerConfiguration(new CloudServiceConfiguration(
          mapping.isDeleteOnOwnerLeave(),
          mapping.isCopyAfterStop(),
          mapping.getMaxIdleSeconds(),
          new ArrayList<>(),
          player.getUniqueId(),
          player.getName(),
          mapping.getGroupName(),
          mapping.getTemplateName(),
          mapping.getTemplateBackend(),
          false,
          false
        ))
        .build()
      ).thenAccept(service -> {
        this.waitingPlayers.remove(player.getUniqueId());
        if (service != null) {
          BukkitComponentRenderer.renderAndSend(player, Message.SERVER_CONNECT_SOON.build());
        } else {
          BukkitComponentRenderer.renderAndSend(player, Message.UNABLE_TO_CREATE_SERVICE.build());
        }
      });
    } else {
      HandlerUtils.notifyNotAllowed(player);
    }
  }
}
