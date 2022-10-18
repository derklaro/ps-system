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

package com.github.derklaro.privateservers.lobby.listeners;

import com.github.derklaro.privateservers.PrivateServersSpigot;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.lobby.inventory.InventoryHandler;
import com.github.derklaro.privateservers.lobby.npc.NpcManager;
import com.github.derklaro.privateservers.lobby.npc.database.NpcDatabase;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener {

  private final NpcManager npcManager;
  private final NpcDatabase npcDatabase;
  private final InventoryHandler inventoryHandler;

  public PlayerListener(NpcManager npcManager, NpcDatabase npcDatabase, InventoryHandler inventoryHandler) {
    this.npcManager = npcManager;
    this.npcDatabase = npcDatabase;
    this.inventoryHandler = inventoryHandler;
  }

  @EventHandler
  public void handle(@NotNull PlayerNPCInteractEvent event) {
    if (!this.npcManager.isManagedNpc(event.getNPC().getEntityId())) {
      return;
    }

    switch (event.getUseAction()) {
      case ATTACK:
        if (event.getPlayer().hasMetadata("npc_remove")) {
          this.npcManager.removeNpc(event.getNPC().getLocation());
          this.npcDatabase.removeNpc(event.getNPC().getLocation());

          event.getPlayer().removeMetadata("npc_remove", PrivateServersSpigot.getInstance());
          BukkitComponentRenderer.renderAndSend(event.getPlayer(), Message.NPC_REMOVE_SUCCESSFUL.build());
        }
        break;
      case INTERACT:
        this.inventoryHandler.openMainInventory(event.getPlayer());
        break;
      default:
        break;
    }
  }

  @EventHandler
  public void handleInventoryClick(@NotNull InventoryClickEvent event) {
    this.inventoryHandler.handleInventoryClick(event);
  }
}
