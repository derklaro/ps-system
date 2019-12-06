package de.derklaro.privateservers.mobs.config;

import org.bukkit.ChatColor;

public final class ResetConfig {

    private String inventoryName;

    private int size;

    private ServerItems confirmItem;

    private ServerItems rejectItem;

    public ResetConfig(String inventoryName, int size, ServerItems confirmItem, ServerItems rejectItem) {
        this.inventoryName = inventoryName;
        this.size = size;
        this.confirmItem = confirmItem;
        this.rejectItem = rejectItem;
    }

    public String getInventoryName() {
        return ChatColor.translateAlternateColorCodes('&', inventoryName);
    }

    public int getSize() {
        return this.size;
    }

    public ServerItems getConfirmItem() {
        return this.confirmItem;
    }

    public ServerItems getRejectItem() {
        return this.rejectItem;
    }
}
