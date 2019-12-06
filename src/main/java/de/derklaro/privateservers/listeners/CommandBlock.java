package de.derklaro.privateservers.listeners;

import de.derklaro.privateservers.PrivateServers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandBlock implements Listener {

    private final List<String> blocked = new ArrayList<>();

    public void init() {
        blocked.addAll(Arrays.asList("stop", "restart", "reload", "rl", "minecraft:stop", "minecraft:restart", "minecraft:reload", "minecraft:rl"));
        blocked.addAll(PrivateServers.getInstance().getCloudSystem().getBlockedCommands());
    }

    @EventHandler
    public void handle(final PlayerCommandPreprocessEvent event) {
        if (shouldBeBlocked(event.getMessage().replace("/", ""))
                && (!event.getPlayer().hasPermission("privateservers.ignoreblocks") || event.getPlayer().isOp())) {
            event.setCancelled(true);
        }
    }

    private boolean shouldBeBlocked(String in) {
        in = in.toLowerCase();
        for (String blockedCmd : blocked) {
            if (in.startsWith(blockedCmd.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
