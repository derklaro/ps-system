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
package com.github.derklaro.privateservers.lobby.command;

import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.lobby.npc.NpcManager;
import com.github.derklaro.privateservers.lobby.npc.database.DatabaseNPCObject;
import com.github.derklaro.privateservers.lobby.npc.database.NpcDatabase;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class CommandCreateNPC implements CommandExecutor {

  private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");

  private final NpcManager npcManager;
  private final NpcDatabase npcDatabase;

  public CommandCreateNPC(NpcManager npcManager, NpcDatabase npcDatabase) {
    this.npcManager = npcManager;
    this.npcDatabase = npcDatabase;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (!(commandSender instanceof Player)) {
      return false;
    }

    Player player = (Player) commandSender;
    if (!player.hasPermission("ps.command.npc.add")) {
      BukkitComponentRenderer.renderAndSend(player, Message.COMMAND_NOT_ALLOWED.build());
      return true;
    }

    if (strings.length != 1) {
      BukkitComponentRenderer.renderAndSend(player, Message.NPC_ADD_HELP.build());
      return true;
    }

    String textureUserName = strings[0];
    if (!NAME_PATTERN.matcher(textureUserName).matches()) {
      BukkitComponentRenderer.renderAndSend(player, Message.NPC_ADD_INVALID_USERNAME.build(textureUserName));
      return true;
    }

    this.npcDatabase.addNpc(DatabaseNPCObject.fromLocation(player.getLocation(), textureUserName));
    this.npcManager.createAndSpawnNpc(player.getLocation(), textureUserName);

    BukkitComponentRenderer.renderAndSend(player, Message.NPC_ADD_SUCCESSFUL.build());
    return true;
  }
}
