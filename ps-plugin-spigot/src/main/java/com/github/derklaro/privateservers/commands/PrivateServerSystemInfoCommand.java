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
package com.github.derklaro.privateservers.commands;

import com.github.derklaro.privateservers.api.Constants;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.module.ModuleContainer;
import com.github.derklaro.privateservers.api.module.ModuleLoader;
import com.github.derklaro.privateservers.api.module.ModuleState;
import com.github.derklaro.privateservers.api.module.annotation.ModuleDescription;
import com.github.derklaro.privateservers.api.translation.ComponentRenderer;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PrivateServerSystemInfoCommand implements TabExecutor {

  private final ModuleLoader moduleLoader;
  private final CloudSystem detectedCloudSystem;
  private final PluginDescriptionFile pluginDescription;

  public PrivateServerSystemInfoCommand(ModuleLoader moduleLoader, CloudSystem detectedCloudSystem, PluginDescriptionFile pluginDescription) {
    this.moduleLoader = moduleLoader;
    this.detectedCloudSystem = detectedCloudSystem;
    this.pluginDescription = pluginDescription;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission(Constants.SYSTEM_INFO_COMMAND_USE_PERM)) {
      if (sender instanceof Player) {
        BukkitComponentRenderer.renderAndSend((Player) sender, Message.COMMAND_NOT_ALLOWED.build());
      } else {
        sender.sendMessage(ComponentRenderer.renderToString(Message.COMMAND_NOT_ALLOWED.build()));
      }

      return true;
    }

    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("version")) {
        sender.sendMessage("§6PrivateServers " + this.pluginDescription.getVersion());
        sender.sendMessage("§7GitHub: https://github.com/derklaro/ps-system");
        sender.sendMessage("§7Copyright 2019-2021 derklaro and contributors");
        sender.sendMessage("§7PrivateServers is licensed under the terms of the MIT license");
        return true;
      } else if (args[0].equalsIgnoreCase("modules")) {
        Collection<ModuleContainer> modules = this.moduleLoader.getModules();

        StringBuilder message = new StringBuilder(String.format("§7%d modules are loaded", modules.size()));
        if (!modules.isEmpty()) {
          message.append(": ").append(modules.stream().map(module -> {
            String prefix = module.getState() == ModuleState.ENABLED ? "§a" : "§c";
            return prefix + module.getDescription().getId() + "§7";
          }).collect(Collectors.joining(", ")));
        }

        sender.sendMessage(message.toString());
        return true;
      } else if (args[0].equalsIgnoreCase("cloud")) {
        sender.sendMessage("§7The detected cloud system is §6" + this.detectedCloudSystem.getName()
          + " §8(§7detected by using class " + this.detectedCloudSystem.getIdentifierClass() + "§8)");
        return true;
      }
    } else if (args.length == 2 && args[0].equalsIgnoreCase("module")) {
      ModuleContainer container = this.moduleLoader.getModuleById(args[1]).orElse(null);
      if (container == null) {
        sender.sendMessage("§cNo module with id " + args[1] + " loaded");
      } else {
        ModuleDescription description = container.getDescription();
        String moduleInfo = String.format(
          "§7%s (%s) by %s\n§7%s\n§7Version: %s\n§7State: %s\nWebsite: %s",
          description.getId(), description.getDisplayName(), String.join(", ", description.getAuthors()),
          description.getDescription(), description.getVersion(), container.getState(), description.getWebsite()
        );
        sender.sendMessage(moduleInfo);
      }
      return true;
    }

    sender.sendMessage("§c/pssinfo <version,modules,cloud>");
    sender.sendMessage("§c/pssinfo <module> <id>");
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (sender.hasPermission(Constants.SYSTEM_INFO_COMMAND_USE_PERM)) {
      if (args.length == 1) {
        return ImmutableList.of("version", "modules", "module", "cloud");
      } else if (args.length == 2) {
        return this.moduleLoader.getModules().stream()
          .map(module -> module.getDescription().getId())
          .collect(Collectors.toList());
      }
    }
    return ImmutableList.of();
  }
}
