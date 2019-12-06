package de.derklaro.privateservers.mobs.config;

import java.util.Collection;

public final class MobStartItems {

    private String name;

    private int size;

    private Collection<StartItem> serverItems;

    public MobStartItems(String name, int size, Collection<StartItem> serverItems) {
        this.name = name;
        this.size = size;
        this.serverItems = serverItems;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public Collection<StartItem> getServerItems() {
        return this.serverItems;
    }
}
