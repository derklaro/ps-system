package de.derklaro.privateservers.plugins;

import de.derklaro.privateservers.PrivateServers;
import de.derklaro.privateservers.mobs.config.ServerItems;
import de.derklaro.privateservers.mobs.config.plugins.Plugin;
import de.derklaro.privateservers.mobs.config.plugins.PluginGUIConfig;
import de.derklaro.privateservers.utility.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class PluginGUI implements Listener {

    private int size;

    private Inventory inventory;

    private Inventory checkInventory;

    public PluginGUI() {
        this.size = PrivateServers.getInstance().getCloudSystem().getInstallablePlugins().getPlugins().size();
        while (size % 9 != 0) {
            size++;
        }

        if (this.size > 54 || this.size < 0) {
            this.size = 54;
        }

        this.checkInventory = Bukkit.createInventory(null, 9, PrivateServers.getInstance().getMobConfig().getMobConfig().getPluginGUIConfig().getConfirmationInventoryName());
        this.inventory = Bukkit.createInventory(null, this.size, PrivateServers.getInstance().getMobConfig().getMobConfig().getPluginGUIConfig().getDisplayName());
        PluginGUIConfig.PluginItem serverItems = PrivateServers.getInstance().getMobConfig().getMobConfig().getPluginGUIConfig().getPluginItem();
        for (Plugin plugin : PrivateServers.getInstance().getMobConfig().getMobConfig().getInstallablePlugins().getPlugins()) {
            Material material = Material.getMaterial(serverItems.getItemName());
            if (material == null) {
                material = Material.CACTUS;
            }

            ItemStack itemStack = new ItemStack(material, 1, serverItems.getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getDisplayName()));
                itemStack.setItemMeta(itemMeta);
            }

            inventory.addItem(itemStack);
        }

        {
            ServerItems serverItems1 = PrivateServers.getInstance().getMobConfig().getMobConfig().getPluginGUIConfig().getInstallItem();
            Material material = Material.getMaterial(serverItems1.getItemName());
            if (material == null) {
                material = Material.CACTUS;
            }

            ItemStack itemStack = new ItemStack(material, 1, serverItems1.getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', serverItems1.getName()));
                List<String> formatted = new ArrayList<>();
                for (String lore : serverItems1.getLore())
                    formatted.add(ChatColor.translateAlternateColorCodes('&', lore));

                itemMeta.setLore(formatted);
                itemStack.setItemMeta(itemMeta);
            }

            checkInventory.setItem(serverItems1.getSlot(), itemStack);
        }

        ServerItems serverItems2 = PrivateServers.getInstance().getMobConfig().getMobConfig().getPluginGUIConfig().getUninstallItem();
        Material material = Material.getMaterial(serverItems2.getItemName());
        if (material == null) {
            material = Material.CACTUS;
        }

        ItemStack itemStack1 = new ItemStack(material, 1, serverItems2.getSubId());
        ItemMeta itemMeta1 = itemStack1.getItemMeta();
        if (itemMeta1 != null) {
            itemMeta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', serverItems2.getName()));
            List<String> formatted = new ArrayList<>();
            for (String lore : serverItems2.getLore())
                formatted.add(ChatColor.translateAlternateColorCodes('&', lore));

            itemMeta1.setLore(formatted);
            itemStack1.setItemMeta(itemMeta1);
        }

        checkInventory.setItem(serverItems2.getSlot(), itemStack1);
        Bukkit.getServer().getPluginManager().registerEvents(this, PrivateServers.getInstance());
    }

    public void openPluginsInv(Player player) {
        player.openInventory(this.inventory);
    }

    private Map<UUID, Plugin> confirmation = new HashMap<>();

    @EventHandler
    public void handle(final InventoryClickEvent event) {
        if (event.getClickedInventory().equals(this.inventory)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && !event.getCurrentItem().getType().equals(Material.AIR)) {
                Plugin plugin = CollectionUtils.filter(
                        PrivateServers.getInstance().getMobConfig().getMobConfig().getInstallablePlugins().getPlugins(),
                        e -> e.getDisplayName().equals(event.getCurrentItem().getItemMeta().getDisplayName())
                );
                if (plugin == null)
                    return;

                this.confirmation.put(event.getWhoClicked().getUniqueId(), plugin);
                event.getWhoClicked().openInventory(this.checkInventory);
            }
        } else if (event.getClickedInventory().equals(this.checkInventory)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && !event.getCurrentItem().getType().equals(Material.AIR)) {
                Plugin plugin = this.confirmation.get(event.getWhoClicked().getUniqueId());
                if (plugin == null)
                    return;

                if (event.getSlot() == PrivateServers.getInstance().getMobConfig().getMobConfig().getPluginGUIConfig().getInstallItem().getSlot()) {
                    this.confirmation.remove(event.getWhoClicked().getUniqueId());
                    event.getWhoClicked().closeInventory();
                    if (PluginInstaller.install(plugin.getName()))
                        event.getWhoClicked().sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginInstalled());
                    else
                        event.getWhoClicked().sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginPluginNotAvailable());
                } else if (event.getSlot() == PrivateServers.getInstance().getMobConfig().getMobConfig().getPluginGUIConfig().getUninstallItem().getSlot()) {
                    this.confirmation.remove(event.getWhoClicked().getUniqueId());
                    event.getWhoClicked().closeInventory();
                    if (PluginInstaller.uninstall(plugin.getName()))
                        event.getWhoClicked().sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginUninstalled());
                    else
                        event.getWhoClicked().sendMessage(PrivateServers.getInstance().getMessages().getCommandPluginPluginNotAvailable());
                }
            }
        }
    }

    @EventHandler
    public void handle(final InventoryCloseEvent event) {
        if (event.getInventory().equals(this.checkInventory))
            this.confirmation.remove(event.getPlayer().getUniqueId());
    }
}
