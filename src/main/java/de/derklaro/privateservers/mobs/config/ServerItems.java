package de.derklaro.privateservers.mobs.config;

import java.util.List;

public final class ServerItems {

    private String name;

    private String itemName;

    private List<String> lore;

    private int slot;

    private short subId;

    public ServerItems(String name, String itemName, List<String> lore, int slot, short subId) {
        this.name = name;
        this.itemName = itemName;
        this.lore = lore;
        this.slot = slot;
        this.subId = subId;
    }

    public String getName() {
        return this.name;
    }

    public String getItemName() {
        return this.itemName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public int getSlot() {
        return this.slot;
    }

    public short getSubId() {
        return this.subId;
    }
}
