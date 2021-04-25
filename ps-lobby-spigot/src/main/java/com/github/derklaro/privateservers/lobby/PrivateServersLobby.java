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
package com.github.derklaro.privateservers.lobby;

import com.github.derklaro.privateservers.PrivateServersSpigot;
import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.module.annotation.Module;
import com.github.derklaro.privateservers.configuration.JsonConfigurationLoader;
import com.github.derklaro.privateservers.lobby.command.CommandCreateNPC;
import com.github.derklaro.privateservers.lobby.command.CommandRemoveNPC;
import com.github.derklaro.privateservers.lobby.event.DatabaseChooseEvent;
import com.github.derklaro.privateservers.lobby.inventory.InventoryHandler;
import com.github.derklaro.privateservers.lobby.listeners.CloudSystemPickedListener;
import com.github.derklaro.privateservers.lobby.listeners.PlayerListener;
import com.github.derklaro.privateservers.lobby.npc.DefaultNpcManager;
import com.github.derklaro.privateservers.lobby.npc.NpcManager;
import com.github.derklaro.privateservers.lobby.npc.database.DatabaseNPCObject;
import com.github.derklaro.privateservers.lobby.npc.database.JsonNpcDatabase;
import com.github.derklaro.privateservers.lobby.npc.database.NpcDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

@Module(
  id = "com.github.derklaro.privateservers.lobby",
  displayName = "PrivateServersLobby",
  version = "1.0",
  description = "Lobby extension for the private server module",
  authors = "derklaro"
)
public class PrivateServersLobby {

  public PrivateServersLobby(@NotNull Plugin plugin) {
    Bukkit.getPluginManager().registerEvents(new CloudSystemPickedListener(this), PrivateServersSpigot.getInstance());
  }

  public void handleCloudSystemPick(@NotNull CloudSystem cloudSystem) {
    Configuration configuration = JsonConfigurationLoader.loadConfiguration();

    DatabaseChooseEvent databaseChooseEvent = new DatabaseChooseEvent(new JsonNpcDatabase(), configuration.getDatabaseProvider());
    Bukkit.getPluginManager().callEvent(databaseChooseEvent);

    NpcDatabase database = databaseChooseEvent.getDatabase();
    database.initialize();

    NpcManager npcManager = new DefaultNpcManager(configuration);
    for (DatabaseNPCObject npc : database.getAllNPCs()) {
      npcManager.createAndSpawnNpc(npc.getLocation(), npc.getTexturesProfileName());
    }

    PluginCommand removeNPCCommand = PrivateServersSpigot.getInstance().getCommand("removenpc");
    if (removeNPCCommand != null) {
      removeNPCCommand.setExecutor(new CommandRemoveNPC());
    }

    PluginCommand createNPCCommand = PrivateServersSpigot.getInstance().getCommand("createnpc");
    if (createNPCCommand != null) {
      createNPCCommand.setExecutor(new CommandCreateNPC());
    }

    Bukkit.getPluginManager().registerEvents(new PlayerListener(
      npcManager, database, new InventoryHandler(configuration, cloudSystem.getCloudServiceManager())
    ), PrivateServersSpigot.getInstance());
  }
}
