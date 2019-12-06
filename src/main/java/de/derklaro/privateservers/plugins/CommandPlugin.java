package de.derklaro.privateservers.plugins;

import de.derklaro.privateservers.PrivateServers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class CommandPlugin implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;
        if (!player.getUniqueId().equals(PrivateServers.getInstance().getOwner()))
            return false;

        if (strings.length == 1 && strings[0].equalsIgnoreCase("available")) {
            PrivateServers.getInstance().getCloudSystem().getInstallablePlugins().getPlugins().forEach(e ->
                    commandSender.sendMessage(" => " + e.getName() + " (" + ChatColor.translateAlternateColorCodes('&', e.getDisplayName()) + "§r)")
            );
            return true;
        }

        if (strings.length == 1 && strings[0].equalsIgnoreCase("gui")) {
            PrivateServers.getInstance().getPluginGUI().openPluginsInv(player);
            return true;
        }

        if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            StringBuilder stringBuilder = new StringBuilder();
            Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins()).forEach(plugin ->
                    stringBuilder.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED).append(plugin.getName()).append(", ")
            );
            commandSender.sendMessage("§7§lActive plugins §8(" + Bukkit.getServer().getPluginManager().getPlugins().length + "): §r" + stringBuilder.substring(0, stringBuilder.length() - 2));
            return true;
        }

        if (strings.length != 2) {
            commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginHelp());
            return true;
        }

        if (strings[0].equalsIgnoreCase("install")) {
            if (PluginInstaller.install(strings[1])) {
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginInstalled());
            } else
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginPluginNotAvailable());

            return true;
        } else if (strings[0].equalsIgnoreCase("uninstall")) {
            if (PluginInstaller.uninstall(strings[1])) {
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginUninstalled());
            } else
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginPluginNotAvailable());

            return true;
        }

        commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginHelp());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return new LinkedList<>();

        Player player = (Player) commandSender;
        if (!player.getUniqueId().equals(PrivateServers.getInstance().getOwner()))
            return new LinkedList<>();

        if (strings.length == 2 && strings[0].equalsIgnoreCase("install"))
            return available();

        if (strings.length == 2 && strings[0].equalsIgnoreCase("uninstall"))
            return available();

        return Arrays.asList("install", "uninstall", "available", "list", "gui");
    }

    private List<String> available() {
        List<String> out = new LinkedList<>();
        PrivateServers.getInstance().getCloudSystem().getInstallablePlugins().getPlugins().forEach(e -> out.add(e.getName()));
        return out;
    }
}
