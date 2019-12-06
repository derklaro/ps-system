package de.derklaro.privateservers.mobs;

import de.derklaro.privateservers.PrivateServers;
import de.derklaro.privateservers.database.utils.DatabaseExt;
import de.derklaro.privateservers.mobs.config.*;
import de.derklaro.privateservers.mobs.config.titles.Title;
import de.derklaro.privateservers.utility.CollectionUtils;
import de.derklaro.privateservers.utility.Double;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class MobSelector {

    private final DatabaseExt<Mob, UUID> mobDatabase;

    private MobConfig mobConfig;

    private final Map<UUID, BukkitMob> spawnedMobs = new HashMap<>();

    private Inventory resetInventory;

    public MobSelector(DatabaseExt<Mob, UUID> mobDatabase, MobConfig mobConfig) {
        this.mobDatabase = mobDatabase;
        this.mobConfig = mobConfig;
    }

    public void load() {
        if (mobDatabase == null) {
            return;
        }

        this.setupResetInventory();
        new KnockBackHandler();

        Bukkit.getServer().getPluginManager().registerEvents(new BukkitListenerImpl(), PrivateServers.getInstance());
        if (PrivateServers.getInstance().getMobConfig().getMobConfig().getSpawnAfter() > 0) {
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(PrivateServers.getInstance(), () -> mobDatabase.getAll().forEach((k, v) -> {
                if (Bukkit.getWorld(v.getMobPosition().getWorld()) != null) {
                    new BukkitMob(v);
                }
            }), PrivateServers.getInstance().getMobConfig().getMobConfig().getSpawnAfter());
            return;
        }

        mobDatabase.getAll().forEach((k, v) -> {
            if (Bukkit.getWorld(v.getMobPosition().getWorld()) != null)
                new BukkitMob(v);
        });
    }

    private Location toLocation(Mob.MobPosition mobPosition) {
        return new Location(
                Bukkit.getWorld(mobPosition.getWorld()),
                mobPosition.getX(),
                mobPosition.getY(),
                mobPosition.getZ(),
                (float) mobPosition.getYaw(),
                (float) mobPosition.getPitch()
        );
    }

    private void setupResetInventory() {
        if (resetInventory != null)
            return;

        MobConfig mobConfig = PrivateServers.getInstance().getMobConfig().getMobConfig();
        this.resetInventory = Bukkit.createInventory(null, mobConfig.getResetConfig().getSize() % 9 != 0
                ? 54
                : mobConfig.getResetConfig().getSize(), mobConfig.getResetConfig().getInventoryName());
        {
            Material material = Material.getMaterial(mobConfig.getResetConfig().getConfirmItem().getItemName());
            if (material == null) {
                material = Material.CACTUS;
            }

            ItemStack itemStack = new ItemStack(material, 1, mobConfig.getResetConfig().getConfirmItem().getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', mobConfig.getResetConfig().getConfirmItem().getName()));
            List<String> out = new LinkedList<>();
            for (String x : mobConfig.getResetConfig().getConfirmItem().getLore()) {
                out.add(ChatColor.translateAlternateColorCodes('&', x));
            }

            itemMeta.setLore(out);
            itemStack.setItemMeta(itemMeta);
            this.resetInventory.setItem(mobConfig.getResetConfig().getConfirmItem().getSlot(), itemStack);
        }

        {
            Material material = Material.getMaterial(mobConfig.getResetConfig().getRejectItem().getItemName());
            if (material == null) {
                material = Material.CACTUS;
            }

            ItemStack itemStack = new ItemStack(material, 1, mobConfig.getResetConfig().getRejectItem().getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', mobConfig.getResetConfig().getRejectItem().getName()));
            List<String> out = new LinkedList<>();
            for (String x : mobConfig.getResetConfig().getRejectItem().getLore()) {
                out.add(ChatColor.translateAlternateColorCodes('&', x));
            }

            itemMeta.setLore(out);
            itemStack.setItemMeta(itemMeta);
            this.resetInventory.setItem(mobConfig.getResetConfig().getRejectItem().getSlot(), itemStack);
        }
    }

    private ItemStack create(ServerItems serverItems) {
        Material material = Material.getMaterial(serverItems.getItemName());
        if (material == null) {
            material = Material.CACTUS;
        }

        ItemStack itemStack = new ItemStack(material, 1, serverItems.getSubId());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', serverItems.getName()));
        itemMeta.setLore(format(serverItems.getLore()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean deleteMob(String name) {
        BukkitMob mob = findMobByName(name);
        if (mob == null) {
            return false;
        }

        PrivateServers.getInstance().getMobDatabase().forget(mob.mob.getUniqueID());
        handleDeleteMob(mob.mob);
        return true;
    }

    public void handleDeleteMob(Mob mob) {
        BukkitMob bukkitMob = findMobByName(mob.getName());
        if (bukkitMob == null) {
            return;
        }

        this.spawnedMobs.remove(bukkitMob.entity.getUniqueId()).despawn();
    }

    public Mob.MobPosition toPosition(Location location) {
        return new Mob.MobPosition(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public void handleCreate(Mob mob) {
        new BukkitMob(mob);
    }

    public BukkitMob findMobByName(String name) {
        return this.spawnedMobs
                .values()
                .stream()
                .filter(e -> e.mob.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private List<String> format(List<String> in) {
        List<String> formatted = new ArrayList<>();
        for (String s : in) {
            formatted.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        return formatted;
    }

    private Inventory newInventory() {
        MobInventory mobInventory = mobConfig.getMobInventory();
        Inventory inventory = Bukkit.createInventory(
                null,
                mobInventory.getSize() % 9 != 0
                        ? 54
                        : mobInventory.getSize(),
                this.formatInventoryName(mobInventory.getName())
        );
        for (MobInventoryItem item : mobInventory.getItems()) {
            Material material = Material.getMaterial(item.getMaterialName());
            if (material == null) {
                material = Material.CACTUS;
            }

            ItemStack itemStack = new ItemStack(material, 1, item.getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(item.getName());
                itemStack.setItemMeta(itemMeta);
            }

            inventory.setItem(item.getSlot(), itemStack);
        }

        return inventory;
    }

    private StartItem findStartItem(int slot) {
        return CollectionUtils.filter(mobConfig.getMobStartItems().getServerItems(), e -> e.getSlot() == slot);
    }

    private Inventory createStartInventory() {
        MobStartItems mobStartItems = mobConfig.getMobStartItems();
        Inventory inventory = Bukkit.createInventory(
                null,
                mobStartItems.getSize() % 9 != 0
                        ? 54
                        : mobStartItems.getSize(),
                ChatColor.translateAlternateColorCodes('&', mobStartItems.getName())
        );
        for (StartItem item : mobStartItems.getServerItems()) {
            Material material = Material.getMaterial(item.getItemName());
            if (material == null) {
                material = Material.CACTUS;
            }

            ItemStack itemStack = new ItemStack(material, 1, item.getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(item.getName());
                List<String> out = new LinkedList<>();
                for (String s : item.getLore())
                    out.add(ChatColor.translateAlternateColorCodes('&', s));

                itemMeta.setLore(out);
                itemStack.setItemMeta(itemMeta);
            }

            inventory.setItem(item.getSlot(), itemStack);
        }

        return inventory;
    }

    private BukkitMob findMobByInventory(Inventory inventory) {
        return this.spawnedMobs
                .values()
                .stream()
                .filter(e -> e.inventory.equals(inventory))
                .findFirst()
                .orElse(null);
    }

    private BukkitMob findMobByStartInventory(Inventory inventory) {
        return this.spawnedMobs
                .values()
                .stream()
                .filter(e -> e.startInventory.equals(inventory))
                .findFirst()
                .orElse(null);
    }

    private String formatInventoryName(String input) {
        if (input == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public Map<UUID, BukkitMob> getSpawnedMobs() {
        return this.spawnedMobs;
    }

    public class BukkitMob {

        private BukkitMob(Mob mob) {
            this.mob = mob;
            this.location = toLocation(mob.getMobPosition());
            this.inventory = MobSelector.this.newInventory();
            EntityType entityType = CollectionUtils.filter(EntityType.values(), e -> e.getEntityClass() != null
                    && e.getEntityClass().getSimpleName().equals(mob.getEntityClassName()));
            if (entityType != null) {
                this.entityType = entityType;
            } else {
                this.entityType = EntityType.WITCH;
            }

            this.addAllItems();
            this.spawn();
        }

        public Mob mob;

        private Location location;

        private Entity entity;

        private EntityType entityType;

        private Inventory inventory;

        private Inventory startInventory = createStartInventory();

        private void addAllItems() {
            Inventory inventory = this.inventory;
            inventory.setItem(mobConfig.getMobServiceItems().getConnectItem().getSlot(), create(mobConfig.getMobServiceItems().getConnectItem()));
            inventory.setItem(mobConfig.getMobServiceItems().getStartItem().getSlot(), create(mobConfig.getMobServiceItems().getStartItem()));
            inventory.setItem(mobConfig.getMobServiceItems().getStatusItem().getSlot(), create(mobConfig.getMobServiceItems().getStatusItem()));
            inventory.setItem(mobConfig.getMobServiceItems().getStopItem().getSlot(), create(mobConfig.getMobServiceItems().getStopItem()));
            inventory.setItem(mobConfig.getMobServiceItems().getResetItem().getSlot(), create(mobConfig.getMobServiceItems().getResetItem()));
        }

        public void despawn() {
            Bukkit.getServer().getScheduler().runTask(PrivateServers.getInstance(), () -> {
                spawnedMobs.remove(this.entity.getUniqueId());
                this.entity.remove();
                this.entity = null;
            });
        }

        private void spawn() {
            if (this.entity != null)
                this.despawn();

            Bukkit.getServer().getScheduler().runTask(PrivateServers.getInstance(), () -> {
                this.entity = this.location.getWorld().spawnEntity(this.location, this.entityType);
                this.entity.setCustomName(ChatColor.translateAlternateColorCodes('&', mob.getDisplayName()));
                this.entity.setCustomNameVisible(true);
                this.entity.setFireTicks(0);

                try {
                    this.entity.setOp(false);
                } catch (final UnsupportedOperationException ignored) {
                }

                if (this.entity instanceof Villager) {
                    ((Villager) this.entity).setProfession(Villager.Profession.FARMER);
                }

                ReflectionUtil.setNoAI(this.entity);
                if (!spawnedMobs.containsKey(this.entity.getUniqueId())) {
                    spawnedMobs.put(this.entity.getUniqueId(), this);
                }
            });
        }
    }

    private static class ReflectionUtil {

        private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        private static final String nmsPackage;

        static {
            nmsPackage = "net.minecraft.server." + version;
        }

        private static void setNoAI(Entity entity) {
            try {
                Class<?> nbt = Class.forName(nmsPackage + ".NBTTagCompound");
                Class<?> entityClazz = Class.forName(nmsPackage + ".Entity");
                Object object = nbt.newInstance();

                Object nmsEntity = entity.getClass().getMethod("getHandle").invoke(entity);
                try {
                    entityClazz.getMethod("e", nbt).invoke(nmsEntity, object);
                } catch (Exception ex) {
                    entityClazz.getMethod("save", nbt).invoke(nmsEntity, object);
                }

                object.getClass().getMethod("setInt", String.class, int.class).invoke(object, "NoAI", 1);
                object.getClass().getMethod("setInt", String.class, int.class).invoke(object, "Silent", 1);
                entityClazz.getMethod("f", nbt).invoke(nmsEntity, object);
            } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class BukkitListenerImpl implements Listener {

        private final Map<UUID, Integer> tasks = new HashMap<>();

        @EventHandler
        public void handle(final WorldLoadEvent event) {
            MobSelector.this.mobDatabase.getAll().forEach((k, v) -> {
                if (event.getWorld().getName().equals(v.getMobPosition().getWorld())) {
                    new BukkitMob(v);
                }
            });
        }

        @EventHandler
        public void handle(final WorldUnloadEvent event) {
            MobSelector.this.spawnedMobs.forEach((k, v) -> {
                if (v.mob.getMobPosition().getWorld().equals(event.getWorld().getName())) {
                    v.despawn();
                }
            });
        }

        @EventHandler
        public void handle(final EntityDamageEvent event) {
            if (event.getEntity().getType().isSpawnable()) {
                BukkitMob mob = MobSelector.this.getSpawnedMobs().get(event.getEntity().getUniqueId());
                if (mob != null) {
                    event.getEntity().setFireTicks(0);
                    event.setCancelled(true);
                }
            }
        }

        @EventHandler
        public void handle(final WorldSaveEvent event) {
            Collection<BukkitMob> inWorld = CollectionUtils.filterAll(MobSelector.this.getSpawnedMobs().values(),
                    mob -> mob.mob.getMobPosition().getWorld().equals(event.getWorld().getName()));
            if (inWorld.isEmpty()) {
                return;
            }

            inWorld.forEach(BukkitMob::despawn);
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(PrivateServers.getInstance(), () -> inWorld.forEach(BukkitMob::spawn), 60);
        }

        @EventHandler
        public void handle(final PlayerInteractEntityEvent event) {
            Player player = event.getPlayer();
            if (event.getRightClicked() != null) {
                BukkitMob mob = MobSelector.this.getSpawnedMobs().get(event.getRightClicked().getUniqueId());
                if (mob != null) {
                    event.setCancelled(true);
                    player.openInventory(mob.inventory);
                }
            }
        }

        @EventHandler
        public void handle(final InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null || event.getCurrentItem() == null) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            BukkitMob mob = MobSelector.this.findMobByInventory(event.getClickedInventory());
            if (mob == null) {
                return;
            }

            event.setCancelled(true);
            if (event.getSlot() == MobSelector.this.mobConfig.getMobServiceItems().getConnectItem().getSlot()) {
                if (!PrivateServers.getInstance().getCloudSystem().sendPlayer(event.getWhoClicked().getUniqueId())) {
                    player.closeInventory();
                    player.sendMessage(PrivateServers.getInstance().getMessages().getNoPrivateServerStarted());
                    return;
                }

                player.closeInventory();
                PrivateServers.getInstance().getCloudSystem().sendPlayer(player.getUniqueId());
                return;
            }

            if (event.getSlot() == MobSelector.this.mobConfig.getMobServiceItems().getStatusItem().getSlot()) {
                player.closeInventory();
                player.sendMessage(
                        PrivateServers.getInstance().getCloudSystem().hasPrivateServer(player.getUniqueId())
                                ? PrivateServers.getInstance().getMessages().getStatusPrivateServerOnline()
                                : PrivateServers.getInstance().getMessages().getStatusPrivateServerOffline()
                );
                return;
            }

            if (event.getSlot() == MobSelector.this.mobConfig.getMobServiceItems().getResetItem().getSlot()) {
                if (!PrivateServers.getInstance().getCloudSystem().hasCustomTemplate(player.getUniqueId())) {
                    player.closeInventory();
                    player.sendMessage(PrivateServers.getInstance().getMessages().getResetNoCustomTemplate());
                    return;
                }

                player.openInventory(MobSelector.this.resetInventory);
                return;
            }

            if (event.getSlot() == MobSelector.this.mobConfig.getMobServiceItems().getStopItem().getSlot()) {
                if (!PrivateServers.getInstance().getCloudSystem().hasPrivateServer(player.getUniqueId())) {
                    player.closeInventory();
                    player.sendMessage(PrivateServers.getInstance().getMessages().getNoPrivateServerStarted());
                    return;
                }

                player.closeInventory();
                PrivateServers.getInstance().getCloudSystem().stopPrivateServer(player.getUniqueId());
                player.sendMessage(PrivateServers.getInstance().getMessages().getStopSuccessful());
                return;
            }

            if (event.getSlot() == MobSelector.this.mobConfig.getMobServiceItems().getStartItem().getSlot()) {
                player.openInventory(mob.startInventory);
            }
        }

        @EventHandler
        public void startHandle(final InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null || event.getCurrentItem() == null) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            BukkitMob mob = MobSelector.this.findMobByStartInventory(event.getClickedInventory());
            if (mob == null) {
                return;
            }

            event.setCancelled(true);
            StartItem startItem = findStartItem(event.getSlot());
            if (startItem != null) {
                if (startItem.getStartPermission() != null && !player.hasPermission(startItem.getStartPermission())) {
                    player.closeInventory();
                    player.sendMessage(PrivateServers.getInstance().getMessages().getStartNoPermission());
                    return;
                }

                player.closeInventory();
                Double<Boolean, Object> result =
                        PrivateServers.getInstance().getCloudSystem().startPrivateServer(player.getUniqueId(), startItem.getTemplate());
                if (result.getFirst()) {
                    player.sendMessage(PrivateServers.getInstance().getMessages().getStartStarting());
                    AtomicInteger atomicInteger = new AtomicInteger(0);
                    int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(PrivateServers.getInstance(), () -> {
                        if (mobConfig.getTitleConfig().getTitles().isEmpty())
                            return;

                        Title title = mobConfig.getTitleConfig().getTitles().get(atomicInteger.get());
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName() + " title [{\"text\":\"" + title.getTitle() + "\"}]");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName() + " subtitle [{\"text\":\"" + title.getSubTitle() + "\"}]");

                        atomicInteger.set(atomicInteger.get() + 1);
                        if (atomicInteger.get() == mobConfig.getTitleConfig().getTitles().size())
                            atomicInteger.set(0);
                    }, 0, 40);
                    this.tasks.put(player.getUniqueId(), id);
                } else if (result.getSecond() instanceof String) {
                    player.sendMessage((String) result.getSecond());
                } else {
                    player.sendMessage(PrivateServers.getInstance().getMessages().getStartUnknownError());
                }
            }
        }

        @EventHandler
        public void handleReset(final InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)
                    || event.getClickedInventory() == null
                    || event.getCurrentItem() == null
                    || !event.getClickedInventory().equals(MobSelector.this.resetInventory))
                return;

            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (MobSelector.this.mobConfig.getResetConfig().getConfirmItem().getSlot() == event.getSlot()) {
                player.closeInventory();
                PrivateServers.getInstance().getCloudSystem().stopPrivateServer(player.getUniqueId());
                player.sendMessage(PrivateServers.getInstance().getMessages().getResetPleaseWait());
                Bukkit.getScheduler().runTaskLaterAsynchronously(PrivateServers.getInstance(), () -> {
                    PrivateServers.getInstance().getCloudSystem().deleteCustomTemplate(player.getUniqueId());
                    player.sendMessage(PrivateServers.getInstance().getMessages().getResetTemplateRemoved());
                }, 25);
                return;
            }

            if (MobSelector.this.mobConfig.getResetConfig().getRejectItem().getSlot() == event.getSlot()) {
                player.closeInventory();
                player.sendMessage(PrivateServers.getInstance().getMessages().getResetCancelled());
            }
        }

        @EventHandler
        public void handle(final PlayerQuitEvent event) {
            if (tasks.containsKey(event.getPlayer().getUniqueId()))
                Bukkit.getScheduler().cancelTask(tasks.remove(event.getPlayer().getUniqueId()));
        }
    }

    private class KnockBackHandler {

        private KnockBackHandler() {
            if (!MobSelector.this.mobConfig.getSmallDistanceKnockBack().isEnabled())
                return;

            Thread thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    MobSelector.this.spawnedMobs.forEach((k, v) -> {
                        Location location = v.location;
                        location.getWorld().getNearbyEntities(
                                location,
                                MobSelector.this.mobConfig.getSmallDistanceKnockBack().getDistance(),
                                MobSelector.this.mobConfig.getSmallDistanceKnockBack().getDistance(),
                                MobSelector.this.mobConfig.getSmallDistanceKnockBack().getDistance()
                        ).forEach(entity -> {
                            if (entity instanceof Player && !entity.hasPermission(MobSelector.this.mobConfig.getSmallDistanceKnockBack().getBypassPermission())) {
                                Bukkit.getScheduler().runTask(PrivateServers.getInstance(), () -> {
                                    Location entityLocation = entity.getLocation();
                                    entity.setVelocity(new Vector(
                                            entityLocation.getX() - location.getX(),
                                            entityLocation.getY() - location.getY(),
                                            entityLocation.getZ() - location.getZ())
                                            .normalize().multiply(MobSelector.this.mobConfig.getSmallDistanceKnockBack().getStrength()).setY(0.2D));
                                });
                            }
                        });
                    });

                    try {
                        Thread.sleep(10);
                    } catch (final InterruptedException ignored) {
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }
}
