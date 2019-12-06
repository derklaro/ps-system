package de.derklaro.privateservers.bridge.events;

import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class PrivateServerCloudRegisteredEvent extends EventAble {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private String serverName;

    private UUID owner;

    public PrivateServerCloudRegisteredEvent(String serverName, UUID owner) {
        this.serverName = serverName;
        this.owner = owner;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public String getServerName() {
        return this.serverName;
    }

    public UUID getOwner() {
        return this.owner;
    }
}
