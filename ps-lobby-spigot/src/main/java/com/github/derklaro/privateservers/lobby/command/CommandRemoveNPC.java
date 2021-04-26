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

import com.github.derklaro.privateservers.PrivateServersSpigot;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class CommandRemoveNPC implements CommandExecutor {

  private static final MetadataValue NPC_REMOVE = new FixedMetadataValue(PrivateServersSpigot.getInstance(), true);

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (!(commandSender instanceof Player)) {
      return true;
    }

    Player player = (Player) commandSender;
    if (!player.hasPermission("ps.command.npc.remove")) {
      BukkitComponentRenderer.renderAndSend(player, Message.COMMAND_NOT_ALLOWED.build());
      return true;
    }

    if (player.hasMetadata("npc_remove")) {
      BukkitComponentRenderer.renderAndSend(player, Message.NPC_REMOVE_ALREADY_ACTIVATED.build());
      return true;
    }

    player.setMetadata("npc_remove", NPC_REMOVE);
    BukkitComponentRenderer.renderAndSend(player, Message.NPC_REMOVE_MODE_ACTIVATED.build());
    return true;
  }
}
