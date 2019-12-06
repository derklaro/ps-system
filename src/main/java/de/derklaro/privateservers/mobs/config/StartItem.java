package de.derklaro.privateservers.mobs.config;

import java.util.List;

public final class StartItem {

    private String name;

    private String itemName;

    private String template;

    private String startPermission;

    private List<String> lore;

    private int slot;

    private short subId;

    public StartItem(String name, String itemName, String template, String startPermission, List<String> lore, int slot, short subId) {
        this.name = name;
        this.itemName = itemName;
        this.template = template;
        this.startPermission = startPermission;
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

    public String getTemplate() {
        return this.template;
    }

    public String getStartPermission() {
        return this.startPermission;
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
