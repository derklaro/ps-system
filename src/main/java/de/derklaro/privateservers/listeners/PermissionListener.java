package de.derklaro.privateservers.listeners;

import de.derklaro.privateservers.PrivateServers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PermissionListener implements Listener {

    @EventHandler
    public void handle(final PlayerJoinEvent event) {
        if (event.getPlayer().getUniqueId().equals(PrivateServers.getInstance().getCloudSystem().getOwner())) {
            Bukkit.getServer().dispatchCommand(
                    Bukkit.getConsoleSender(),
                    PrivateServers.getInstance().getMobConfig().getMobConfig().getPermissionConfig().getCommandOnOwnerJoin().replace("%name%", event.getPlayer().getName())
            );
            if (PrivateServers.getInstance().getMobConfig().getMobConfig().getPermissionConfig().isSetOP()) {
                event.getPlayer().setOp(true);
                event.getPlayer().sendMessage(PrivateServers.getInstance().getMobConfig().getMobConfig().getPermissionConfig().getOpMessage()
                        .replace("%name%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void handle(final PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId().equals(PrivateServers.getInstance().getCloudSystem().getOwner())) {
            Bukkit.getServer().dispatchCommand(
                    Bukkit.getConsoleSender(),
                    PrivateServers.getInstance().getMobConfig().getMobConfig().getPermissionConfig().getCommandOnOwnerLeave().replace("%name%", event.getPlayer().getName())
            );
        }
    }
}
