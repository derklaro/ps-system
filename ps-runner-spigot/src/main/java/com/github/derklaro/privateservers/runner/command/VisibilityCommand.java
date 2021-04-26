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
package com.github.derklaro.privateservers.runner.command;

import com.github.derklaro.privateservers.api.Constants;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class VisibilityCommand implements CommandExecutor, TabCompleter {

  private final CloudSystem cloudSystem;

  public VisibilityCommand(CloudSystem cloudSystem) {
    this.cloudSystem = cloudSystem;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    CloudService cloudService = this.cloudSystem.getCloudServiceManager().getCurrentCloudService().orElse(null);
    if (cloudService == null || !(sender instanceof Player)) {
      sender.sendMessage("§cUnable to process command");
      return true;
    }

    Player player = (Player) sender;
    if (!player.hasPermission(Constants.VISIBILITY_COMMAND_USE_PERM) || !player.getUniqueId().equals(cloudService.getOwnerUniqueId())) {
      BukkitComponentRenderer.renderAndSend(player, Message.COMMAND_NOT_ALLOWED.build());
      return true;
    }

    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("toggle")) {
        cloudService.getCloudServiceConfiguration().setPublicService(!cloudService.getCloudServiceConfiguration().isPublicService());
        cloudService.publishCloudServiceInfoUpdate();

        BukkitComponentRenderer.renderAndSend(player, cloudService.getCloudServiceConfiguration().isPublicService()
          ? Message.SERVER_NOW_PUBLIC.build() : Message.SERVER_NOW_PRIVATE.build());
        return true;
      }

      if (args[0].equalsIgnoreCase("public")) {
        if (!cloudService.getCloudServiceConfiguration().isPublicService()) {
          cloudService.getCloudServiceConfiguration().setPublicService(true);
          cloudService.publishCloudServiceInfoUpdate();
        }

        BukkitComponentRenderer.renderAndSend(player, Message.SERVER_NOW_PUBLIC.build());
        return true;
      }

      if (args[0].equalsIgnoreCase("private")) {
        if (cloudService.getCloudServiceConfiguration().isPublicService()) {
          cloudService.getCloudServiceConfiguration().setPublicService(false);
          cloudService.publishCloudServiceInfoUpdate();
        }

        BukkitComponentRenderer.renderAndSend(player, Message.SERVER_NOW_PRIVATE.build());
        return true;
      }
    }

    BukkitComponentRenderer.renderAndSend(player, Message.VISIBILITY_COMMAND_HELP.build());
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1) {
      return ImmutableList.of("toggle", "public", "private");
    }
    return ImmutableList.of();
  }
}
