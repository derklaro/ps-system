package de.derklaro.privateservers.configuration;

import com.google.gson.reflect.TypeToken;
import de.derklaro.privateservers.mobs.config.*;
import de.derklaro.privateservers.mobs.config.knockback.Knockback;
import de.derklaro.privateservers.mobs.config.plugins.InstallablePlugins;
import de.derklaro.privateservers.mobs.config.plugins.Plugin;
import de.derklaro.privateservers.mobs.config.plugins.PluginGUIConfig;
import de.derklaro.privateservers.mobs.config.titles.Title;
import de.derklaro.privateservers.mobs.config.titles.TitleConfig;
import de.derklaro.privateservers.utility.configuration.Configuration;
import org.bukkit.Material;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class MobConfig {

    private de.derklaro.privateservers.mobs.MobConfig mobConfig;

    public MobConfig() {
        if (!Files.exists(Paths.get("plugins/privateservers/config.json"))) {
            new Configuration()
                    .addValue("config", new de.derklaro.privateservers.mobs.MobConfig(
                            "english",
                            "PrivateServers",
                            0,
                            false,
                            Collections.singletonList("icanhasbukkit"),
                            new InstallablePlugins(
                                    Arrays.asList(
                                            new Plugin("essentials",
                                                    "§6§lEssentials",
                                                    "https://hub.spigotmc.org/jenkins/job/Spigot-Essentials/lastStableBuild/artifact/Essentials/target/Essentials-2.x-SNAPSHOT.jar"
                                            ),
                                            new Plugin(
                                                    "signplugin",
                                                    "§6§lSignPlugin",
                                                    "http://api.spiget.org/v2/resources/66952/download"
                                            )
                                    )
                            ),
                            new TitleConfig(
                                    Arrays.asList(
                                            new Title("§a§lStarting §a● §7● §7●", "§6§lPlease wait."),
                                            new Title("§a§lStarting §7● §a● §7●", "§6§lPlease wait.."),
                                            new Title("§a§lStarting §7● §7● §a●", "§6§lPlease wait..."),
                                            new Title("§a§lStarting §7● §a● §7●", "§6§lPlease wait..")
                                    )
                            ),
                            new MobInventory(
                                    "§6§lPrivateServers",
                                    9,
                                    new ArrayList<>()
                            ),
                            new MobStartItems(
                                    "§a§lStart Server",
                                    9,
                                    Arrays.asList(
                                            new StartItem(
                                                    "§c§lBedWars",
                                                    Material.BEACON.name(),
                                                    "BedWars",
                                                    "private.servers.start.template.bedwars",
                                                    Collections.singletonList("§7§lStart a bedwars server"),
                                                    2,
                                                    (short) 0
                                            ),
                                            new StartItem(
                                                    "§2§lSkyWars",
                                                    Material.DIAMOND_HELMET.name(),
                                                    "SkyWars",
                                                    null,
                                                    Collections.singletonList("§7§lStart a skywars server"),
                                                    4,
                                                    (short) 0
                                            ),
                                            new StartItem(
                                                    "§9§lCustom",
                                                    Material.CAKE.name(),
                                                    null,
                                                    null,
                                                    Collections.singletonList("§7§lStart a custom server"),
                                                    6,
                                                    (short) 0
                                            )
                                    )
                            ),
                            new PluginGUIConfig(
                                    "§6§lInstallable plugins",
                                    "§7Confirmation",
                                    new PluginGUIConfig.PluginItem(
                                            Material.COOKED_BEEF.name(),
                                            (short) 0
                                    ),
                                    new ServerItems(
                                            "§a§lInstall",
                                            Material.EMERALD.name(),
                                            Collections.singletonList("§aInstall the plugin"),
                                            3,
                                            (short) 0
                                    ),
                                    new ServerItems(
                                            "§c§lUninstall",
                                            Material.CLAY_BRICK.name(),
                                            Collections.singletonList("§aUninstall the plugin"),
                                            5,
                                            (short) 0
                                    )
                            ),
                            new MobServiceItems(
                                    new ServerItems(
                                            "§a§lStart server",
                                            Material.EMERALD.name(),
                                            Collections.singletonList("§aStart server"),
                                            0,
                                            (short) 0
                                    ),
                                    new ServerItems(
                                            "§c§lStop server",
                                            Material.ANVIL.name(),
                                            Collections.singletonList("§cStop server"),
                                            2,
                                            (short) 0
                                    ),
                                    new ServerItems(
                                            "§3§lServer status",
                                            Material.APPLE.name(),
                                            Collections.singletonList("§3Server status"),
                                            4,
                                            (short) 0
                                    ),
                                    new ServerItems(
                                            "§5§lJoin server",
                                            Material.COOKIE.name(),
                                            Collections.singletonList("§5Join server"),
                                            6,
                                            (short) 0
                                    ),
                                    new ServerItems(
                                            "§b§lReset server",
                                            Material.BARRIER.name(),
                                            Collections.singletonList("§bReset server"),
                                            8,
                                            (short) 0
                                    )
                            ),
                            new PermissionConfig(
                                    "title %name% title [{\"text\":\"§6§lWelcome to your server\"}]",
                                    "say The owner %name% has left the server",
                                    "§7§o[Server: Opped %name%]",
                                    true
                            ),
                            new ResetConfig(
                                    "",
                                    9,
                                    new ServerItems(
                                            "§a§lReset server",
                                            Material.BUCKET.name(),
                                            Collections.singletonList("§aReset server"),
                                            3,
                                            (short) 0
                                    ),
                                    new ServerItems(
                                            "§c§lDon't delete",
                                            Material.WATER_BUCKET.name(),
                                            Collections.singletonList("§cDont't delete server"),
                                            5,
                                            (short) 0
                                    )
                            ),
                            new Knockback(
                                    true,
                                    "privateservers.knockback.bypass",
                                    1D,
                                    0.8D
                            )
                    )).write("plugins/privateservers/config.json");
        }

        this.mobConfig = Configuration.parse("plugins/privateservers/config.json")
                .getValue("config", new TypeToken<de.derklaro.privateservers.mobs.MobConfig>() {
                });
    }

    public MobConfig(de.derklaro.privateservers.mobs.MobConfig mobConfig) {
        this.mobConfig = mobConfig;
    }

    public de.derklaro.privateservers.mobs.MobConfig getMobConfig() {
        return this.mobConfig;
    }
}
