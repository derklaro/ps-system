package de.derklaro.privateservers.listeners;

import de.derklaro.privateservers.PrivateServers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public final class StopWhenEmptyAfterLeave implements Listener {

    private BukkitTask bukkitTask;

    @EventHandler
    public void handle(final PlayerQuitEvent event) {
        if ((Bukkit.getServer().getOnlinePlayers().size() - 1) == 0) {
            if (bukkitTask != null) {
                bukkitTask.cancel();
                bukkitTask = null;
            }

            bukkitTask = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(PrivateServers.getInstance(), () -> {
                if (Bukkit.getServer().getOnlinePlayers().size() == 0) {
                    PrivateServers.getInstance().getCloudSystem().stopPrivateServer(PrivateServers.getInstance().getOwner());
                }
            }, 1200);
        }
    }

    @EventHandler
    public void handle(final PlayerJoinEvent event) {
        if (bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
    }
}
