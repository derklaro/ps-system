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

package com.github.derklaro.privateservers.runner.command;

import com.github.derklaro.privateservers.api.Constants;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class WhitelistCommand implements TabExecutor {

  private final CloudSystem cloudSystem;

  public WhitelistCommand(CloudSystem cloudSystem) {
    this.cloudSystem = cloudSystem;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    CloudService cloudService = this.cloudSystem.getCloudServiceManager().getCurrentCloudService().orElse(null);
    if (cloudService == null || !(commandSender instanceof Player)) {
      commandSender.sendMessage("§cUnable to process command");
      return true;
    }

    Player player = (Player) commandSender;
    if (!commandSender.hasPermission(Constants.WHITELIST_COMMAND_USE_PERM) && !player.getUniqueId().equals(cloudService.getOwnerUniqueId())) {
      BukkitComponentRenderer.renderAndSend(player, Message.COMMAND_NOT_ALLOWED.build());
      return true;
    }

    if (strings.length == 0 || strings.length > 2) {
      BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_COMMAND_HELP.build());
      return true;
    }

    if (strings.length == 1) {
      if (strings[0].equalsIgnoreCase("list")) {
        Collection<String> whitelistedPlayers = cloudService.getCloudServiceConfiguration().getWhitelistedPlayers();
        StringBuilder builder = new StringBuilder();

        builder.append("§7Whitelist (").append(whitelistedPlayers.size()).append("): ");
        for (String whitelistedPlayer : whitelistedPlayers) {
          builder.append(whitelistedPlayer).append(", ");
        }

        commandSender.sendMessage(builder.substring(0, builder.length() - 2));
        return true;
      }

      if (strings[0].equalsIgnoreCase("on")) {
        if (!cloudService.getCloudServiceConfiguration().isWhitelist()) {
          cloudService.getCloudServiceConfiguration().setWhitelist(true);
          cloudService.publishCloudServiceInfoUpdate();
        }

        BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_NOW_ON.build());
        return true;
      }

      if (strings[0].equalsIgnoreCase("off")) {
        if (cloudService.getCloudServiceConfiguration().isWhitelist()) {
          cloudService.getCloudServiceConfiguration().setWhitelist(false);
          cloudService.publishCloudServiceInfoUpdate();
        }

        BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_NOW_OFF.build());
        return true;
      }
    }

    if (strings.length == 2) {
      if (strings[0].equalsIgnoreCase("add")) {
        if (cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().contains(strings[1])) {
          BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_ALREADY.build());
          return true;
        }

        cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().add(strings[1]);
        cloudService.publishCloudServiceInfoUpdate();
        BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_COMMAND_ADD.build(strings[1]));
        return true;
      }

      if (strings[0].equalsIgnoreCase("remove")) {
        if (!cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().contains(strings[1])) {
          BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_NOT_ON.build());
          return true;
        }

        cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().remove(strings[1]);
        cloudService.publishCloudServiceInfoUpdate();
        BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_COMMAND_REMOVE.build(strings[1]));
        return true;
      }
    }

    BukkitComponentRenderer.renderAndSend(player, Message.WHITELIST_COMMAND_HELP.build());
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1) {
      return ImmutableList.of("add", "remove", "on", "off", "list");
    }
    return ImmutableList.of();
  }
}
