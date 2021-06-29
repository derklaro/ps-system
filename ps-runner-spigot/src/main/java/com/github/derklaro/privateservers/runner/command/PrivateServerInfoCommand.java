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
import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateServerInfoCommand implements CommandExecutor {

  private final CloudServiceManager manager;

  public PrivateServerInfoCommand(CloudSystem cloudSystem) {
    this.manager = cloudSystem.getCloudServiceManager();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    CloudService cloudService = this.manager.getCurrentCloudService().orElse(null);
    if (cloudService == null || !(sender instanceof Player)) {
      sender.sendMessage("§cUnable to process command");
      return true;
    }

    Player player = (Player) sender;
    if (!player.hasPermission(Constants.SERVER_INFO_COMMAND_USE_PERM)) {
      BukkitComponentRenderer.renderAndSend(player, Message.COMMAND_NOT_ALLOWED.build());
      return true;
    }

    CloudServiceConfiguration config = cloudService.getCloudServiceConfiguration();
    BukkitComponentRenderer.renderAndSend(player, Message.PRIVATE_SERVER_INFO.build(
      cloudService.getOwnerName(),
      cloudService.getOwnerUniqueId(),
      config.isPublicService() ? Message.VISIBILITY_PUBLIC.build() : Message.VISIBILITY_PRIVATE.build(),
      config.hasWhitelist() ? Message.WHITELIST_ON.build() : Message.WHITELIST_OFF.build()
    ));
    return true;
  }
}
