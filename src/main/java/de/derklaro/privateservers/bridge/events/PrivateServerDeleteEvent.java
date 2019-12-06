package de.derklaro.privateservers.bridge.events;

import org.bukkit.event.HandlerList;

import java.io.Serializable;
import java.util.UUID;

public final class PrivateServerDeleteEvent extends EventAble implements Serializable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private String serverName;

    private UUID owner;

    public PrivateServerDeleteEvent(String serverName, UUID owner) {
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
