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
package com.github.derklaro.privateservers.runner;

import com.github.derklaro.privateservers.PrivateServersSpigot;
import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.module.annotation.Module;
import com.github.derklaro.privateservers.runner.command.PrivateServerInfoCommand;
import com.github.derklaro.privateservers.runner.command.VisibilityCommand;
import com.github.derklaro.privateservers.runner.command.WhitelistCommand;
import com.github.derklaro.privateservers.runner.listeners.CloudSystemPickedListener;
import com.github.derklaro.privateservers.runner.listeners.PlayerLoginListener;
import com.github.derklaro.privateservers.runner.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

@Module(
  id = "com.github.derklaro.privateservers.runner",
  displayName = "PrivateServerRunner",
  version = "1.1.0",
  description = "Module for the management on the private servers",
  authors = "derklaro"
)
public class PrivateServersSpigotRunner {

  public PrivateServersSpigotRunner(@NotNull Plugin plugin) {
    Bukkit.getPluginManager().registerEvents(new CloudSystemPickedListener(this), PrivateServersSpigot.getInstance());
  }

  public void handleCloudSystemPick(@NotNull CloudSystem cloudSystem) {
    CloudService cloudService = cloudSystem.getCloudServiceManager().getCurrentCloudService().orElse(null);
    if (cloudService == null) {
      return;
    }

    cloudService.createConnectionRequest(cloudService.getOwnerUniqueId()).fire();

    Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(cloudSystem), PrivateServersSpigot.getInstance());
    Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(cloudSystem), PrivateServersSpigot.getInstance());

    PluginCommand whitelistPluginCommand = PrivateServersSpigot.getInstance().getCommand("whitelist");
    if (whitelistPluginCommand != null) {
      WhitelistCommand whitelistCommand = new WhitelistCommand(cloudSystem);
      whitelistPluginCommand.setExecutor(whitelistCommand);
      whitelistPluginCommand.setTabCompleter(whitelistCommand);
    }

    PluginCommand visibilityPluginCommand = PrivateServersSpigot.getInstance().getCommand("visibility");
    if (visibilityPluginCommand != null) {
      VisibilityCommand visibilityCommand = new VisibilityCommand(cloudSystem);
      visibilityPluginCommand.setExecutor(visibilityCommand);
      visibilityPluginCommand.setTabCompleter(visibilityCommand);
    }

    PluginCommand privateServerInfoCommand = PrivateServersSpigot.getInstance().getCommand("psinfo");
    if (privateServerInfoCommand != null) {
      privateServerInfoCommand.setExecutor(new PrivateServerInfoCommand(cloudSystem));
    }
  }
}
