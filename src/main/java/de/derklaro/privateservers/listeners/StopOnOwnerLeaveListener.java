package de.derklaro.privateservers.listeners;

import de.derklaro.privateservers.PrivateServers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class StopOnOwnerLeaveListener implements Listener {

    @EventHandler
    public void handle(final PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId().equals(PrivateServers.getInstance().getCloudSystem().getOwner()))
            PrivateServers.getInstance().getCloudSystem().stopPrivateServer(event.getPlayer().getUniqueId());
    }
}
