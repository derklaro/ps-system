/*
 * MIT License
 *
 * Copyright (c) 2020 Pasqual K. and contributors
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

import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class WhitelistCommand implements CommandExecutor {

    public WhitelistCommand(CloudSystem cloudSystem) {
        this.cloudSystem = cloudSystem;
    }

    private final CloudSystem cloudSystem;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        CloudService cloudService = this.cloudSystem.getCloudServiceManager().getCurrentCloudService().orElse(null);
        if (cloudService == null) {
            commandSender.sendMessage("§cUnable to process command");
            return true;
        }

        if (strings.length == 0 || strings.length > 2) {
            commandSender.sendMessage("§7/whitelist add <name>");
            commandSender.sendMessage("§7/whitelist remove <name>");
            commandSender.sendMessage("§7/whitelist list");
            return true;
        }

        if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            Collection<String> whitelistedPlayers = cloudService.getCloudServiceConfiguration().getWhitelistedPlayers();
            StringBuilder builder = new StringBuilder();

            builder.append("§7Whitelisted players (").append(whitelistedPlayers.size()).append("): ");
            for (String whitelistedPlayer : whitelistedPlayers) {
                builder.append(whitelistedPlayer).append(", ");
            }

            commandSender.sendMessage(builder.substring(0, builder.length() - 2));
            return true;
        }

        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("add")) {
                if (cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().contains(strings[1])) {
                    commandSender.sendMessage("§cThis player is already whitelisted");
                    return true;
                }

                cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().add(strings[1]);
                cloudService.publishCloudServiceInfoUpdate();
                commandSender.sendMessage("§aSuccessfully §7whitelisted player " + strings[1]);
                return true;
            }

            if (strings[0].equalsIgnoreCase("remove")) {
                if (!cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().contains(strings[1])) {
                    commandSender.sendMessage("§cThis player is not whitelisted");
                    return true;
                }

                cloudService.getCloudServiceConfiguration().getWhitelistedPlayers().remove(strings[1]);
                cloudService.publishCloudServiceInfoUpdate();
                commandSender.sendMessage("§cRemoved §7whitelisted player " + strings[1]);
                return true;
            }
        }

        commandSender.sendMessage("§7/whitelist add <name>");
        commandSender.sendMessage("§7/whitelist remove <name>");
        commandSender.sendMessage("§7/whitelist list");
        return true;
    }
}
