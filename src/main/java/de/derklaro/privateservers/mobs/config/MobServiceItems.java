package de.derklaro.privateservers.mobs.config;

public final class MobServiceItems {

    private ServerItems startItem;

    private ServerItems stopItem;

    private ServerItems statusItem;

    private ServerItems connectItem;

    private ServerItems resetItem;

    public MobServiceItems(ServerItems startItem, ServerItems stopItem, ServerItems statusItem, ServerItems connectItem, ServerItems resetItem) {
        this.startItem = startItem;
        this.stopItem = stopItem;
        this.statusItem = statusItem;
        this.connectItem = connectItem;
        this.resetItem = resetItem;
    }

    public ServerItems getStartItem() {
        return this.startItem;
    }

    public ServerItems getStopItem() {
        return this.stopItem;
    }

    public ServerItems getStatusItem() {
        return this.statusItem;
    }

    public ServerItems getConnectItem() {
        return this.connectItem;
    }

    public ServerItems getResetItem() {
        return this.resetItem;
    }
}
