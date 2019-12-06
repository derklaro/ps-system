package de.derklaro.privateservers.mobs.config;

import java.util.Collection;

public final class MobInventory {

    private String name;

    private int size;

    private Collection<MobInventoryItem> items;

    public MobInventory(String name, int size, Collection<MobInventoryItem> items) {
        this.name = name;
        this.size = size;
        this.items = items;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public Collection<MobInventoryItem> getItems() {
        return this.items;
    }
}
