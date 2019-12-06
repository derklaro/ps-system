package de.derklaro.privateservers.mobs.config.plugins;

import de.derklaro.privateservers.mobs.config.ServerItems;
import org.bukkit.ChatColor;

public final class PluginGUIConfig {

    private String displayName;

    private String confirmationInventoryName;

    private PluginItem pluginItem;

    private ServerItems installItem;

    private ServerItems uninstallItem;

    public PluginGUIConfig(String displayName, String confirmationInventoryName, PluginItem pluginItem, ServerItems installItem, ServerItems uninstallItem) {
        this.displayName = displayName;
        this.confirmationInventoryName = confirmationInventoryName;
        this.pluginItem = pluginItem;
        this.installItem = installItem;
        this.uninstallItem = uninstallItem;
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public String getConfirmationInventoryName() {
        return ChatColor.translateAlternateColorCodes('&', confirmationInventoryName);
    }

    public PluginItem getPluginItem() {
        return this.pluginItem;
    }

    public ServerItems getInstallItem() {
        return this.installItem;
    }

    public ServerItems getUninstallItem() {
        return this.uninstallItem;
    }

    public static class PluginItem {
        private String itemName;

        private short subId;

        public PluginItem(String itemName, short subId) {
            this.itemName = itemName;
            this.subId = subId;
        }

        public String getItemName() {
            return this.itemName;
        }

        public short getSubId() {
            return this.subId;
        }
    }
}
