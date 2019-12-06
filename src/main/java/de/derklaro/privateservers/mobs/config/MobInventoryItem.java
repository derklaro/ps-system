package de.derklaro.privateservers.mobs.config;

public final class MobInventoryItem {

    private String name;

    private String materialName;

    private int slot;

    private short subId;

    public MobInventoryItem(String name, String materialName, int slot, short subId) {
        this.name = name;
        this.materialName = materialName;
        this.slot = slot;
        this.subId = subId;
    }

    public String getName() {
        return this.name;
    }

    public String getMaterialName() {
        return this.materialName;
    }

    public int getSlot() {
        return this.slot;
    }

    public short getSubId() {
        return this.subId;
    }
}
