package de.derklaro.privateservers.mobs;

import de.derklaro.privateservers.mobs.config.*;
import de.derklaro.privateservers.mobs.config.knockback.Knockback;
import de.derklaro.privateservers.mobs.config.plugins.InstallablePlugins;
import de.derklaro.privateservers.mobs.config.plugins.PluginGUIConfig;
import de.derklaro.privateservers.mobs.config.titles.TitleConfig;

import java.util.List;

public final class MobConfig {

    private String language;

    private String privateServersServerGroup;

    private int spawnAfter;

    private boolean stopOnOwnerLeave;

    private List<String> blockedCommands;

    private InstallablePlugins installablePlugins;

    private TitleConfig titleConfig;

    private MobInventory mobInventory;

    private MobStartItems mobStartItems;

    private PluginGUIConfig pluginGUIConfig;

    private MobServiceItems mobServiceItems;

    private PermissionConfig permissionConfig;

    private ResetConfig resetConfig;

    private Knockback smallDistanceKnockBack;

    public MobConfig(String language, String privateServersServerGroup, int spawnAfter, boolean stopOnOwnerLeave, List<String> blockedCommands, InstallablePlugins installablePlugins, TitleConfig titleConfig, MobInventory mobInventory, MobStartItems mobStartItems, PluginGUIConfig pluginGUIConfig, MobServiceItems mobServiceItems, PermissionConfig permissionConfig, ResetConfig resetConfig, Knockback smallDistanceKnockBack) {
        this.language = language;
        this.privateServersServerGroup = privateServersServerGroup;
        this.spawnAfter = spawnAfter;
        this.stopOnOwnerLeave = stopOnOwnerLeave;
        this.blockedCommands = blockedCommands;
        this.installablePlugins = installablePlugins;
        this.titleConfig = titleConfig;
        this.mobInventory = mobInventory;
        this.mobStartItems = mobStartItems;
        this.pluginGUIConfig = pluginGUIConfig;
        this.mobServiceItems = mobServiceItems;
        this.permissionConfig = permissionConfig;
        this.resetConfig = resetConfig;
        this.smallDistanceKnockBack = smallDistanceKnockBack;
    }

    public String getLanguage() {
        return language.toLowerCase();
    }

    public String getPrivateServersServerGroup() {
        return this.privateServersServerGroup;
    }

    public int getSpawnAfter() {
        return this.spawnAfter;
    }

    public boolean isStopOnOwnerLeave() {
        return this.stopOnOwnerLeave;
    }

    public List<String> getBlockedCommands() {
        return this.blockedCommands;
    }

    public InstallablePlugins getInstallablePlugins() {
        return this.installablePlugins;
    }

    public TitleConfig getTitleConfig() {
        return this.titleConfig;
    }

    public MobInventory getMobInventory() {
        return this.mobInventory;
    }

    public MobStartItems getMobStartItems() {
        return this.mobStartItems;
    }

    public PluginGUIConfig getPluginGUIConfig() {
        return this.pluginGUIConfig;
    }

    public MobServiceItems getMobServiceItems() {
        return this.mobServiceItems;
    }

    public PermissionConfig getPermissionConfig() {
        return this.permissionConfig;
    }

    public ResetConfig getResetConfig() {
        return this.resetConfig;
    }

    public Knockback getSmallDistanceKnockBack() {
        return this.smallDistanceKnockBack;
    }
}
