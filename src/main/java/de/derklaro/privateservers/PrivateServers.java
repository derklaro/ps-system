package de.derklaro.privateservers;

import com.google.common.base.Preconditions;
import de.derklaro.privateservers.cloudsystems.CloudFinder;
import de.derklaro.privateservers.cloudsystems.utility.CloudSystem;
import de.derklaro.privateservers.commands.CommandPrivateServers;
import de.derklaro.privateservers.configuration.MessageConfig;
import de.derklaro.privateservers.configuration.MobConfig;
import de.derklaro.privateservers.configuration.language.defaults.English;
import de.derklaro.privateservers.configuration.language.defaults.messages.Message;
import de.derklaro.privateservers.database.utils.DatabaseExt;
import de.derklaro.privateservers.database.utils.databse.MobDatabase;
import de.derklaro.privateservers.listeners.CommandBlock;
import de.derklaro.privateservers.listeners.PermissionListener;
import de.derklaro.privateservers.listeners.StopOnOwnerLeaveListener;
import de.derklaro.privateservers.listeners.StopWhenEmptyAfterLeave;
import de.derklaro.privateservers.mobs.MobSelector;
import de.derklaro.privateservers.mobs.config.Mob;
import de.derklaro.privateservers.plugins.CommandPlugin;
import de.derklaro.privateservers.plugins.PluginGUI;
import de.derklaro.privateservers.utility.api.AbstractBossService;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public final class PrivateServers extends JavaPlugin {

    private static PrivateServers instance;

    private Message message;

    private final DatabaseExt<Mob, UUID> mobDatabase = new MobDatabase();

    private MobConfig mobConfig;

    private MobSelector mobSelector;

    private PluginGUI pluginGUI;

    public AbstractBossService abstractBossService;

    private CloudSystem cloudSystem;

    private UUID owner;

    public static PrivateServers getInstance() {
        return PrivateServers.instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.cloudSystem = CloudFinder.findCloudSystem(null);
        if (this.cloudSystem == null)
            return;

        if (!abstractBossService.isLobbyInstance()) {
            UUID currentOwner = this.cloudSystem.getOwner();
            if (currentOwner == null) {
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            this.owner = currentOwner;
            if (this.cloudSystem.isAutoStopOnOwnerLeave()) {
                Bukkit.getPluginManager().registerEvents(new StopOnOwnerLeaveListener(), this);
            } else {
                Bukkit.getPluginManager().registerEvents(new StopWhenEmptyAfterLeave(), this);
            }

            CommandBlock commandBlock = new CommandBlock();
            Bukkit.getPluginManager().registerEvents(new PermissionListener(), this);
            Bukkit.getPluginManager().registerEvents(commandBlock, this);
            commandBlock.init();

            PluginCommand pluginCommand = Bukkit.getServer().getPluginCommand("plugin");
            CommandPlugin commandPlugin = new CommandPlugin();
            pluginCommand.setExecutor(commandPlugin);
            pluginCommand.setTabCompleter(commandPlugin);

            this.mobConfig = new MobConfig(this.cloudSystem.mobConfig());
            this.pluginGUI = new PluginGUI();
            this.message = this.cloudSystem.getMessage();
            if (this.message == null) {
                this.message = new English();
            }

            this.cloudSystem.sendPlayer(currentOwner);
            return;
        }

        Preconditions.checkArgument(new File("plugins/privateservers/database").mkdirs());

        this.mobConfig = new MobConfig();
        MessageConfig messageConfig = new MessageConfig(this.mobConfig.getMobConfig());
        this.message = messageConfig.getLoaded();
        this.mobDatabase.load();
        this.mobSelector = new MobSelector(this.mobDatabase, this.mobConfig.getMobConfig());
        this.mobSelector.load();

        PluginCommand pluginCommand = getServer().getPluginCommand("privateservers");
        CommandPrivateServers commandPrivateServers = new CommandPrivateServers();
        pluginCommand.setExecutor(commandPrivateServers);
        pluginCommand.setTabCompleter(commandPrivateServers);
        pluginCommand.setPermission("privateservers.main");
        pluginCommand.setDescription("Create or delete the private server mob");
    }

    @Override
    public void onDisable() {
        if (this.mobSelector != null)
            this.mobSelector.getSpawnedMobs().values().forEach(MobSelector.BukkitMob::despawn);
    }

    public String getPrivateServerGroup() {
        if (mobConfig == null)
            return "PrivateServers";

        return mobConfig.getMobConfig().getPrivateServersServerGroup();
    }

    public Message getMessages() {
        return this.message;
    }

    public DatabaseExt<Mob, UUID> getMobDatabase() {
        return this.mobDatabase;
    }

    public MobConfig getMobConfig() {
        return this.mobConfig;
    }

    public MobSelector getMobSelector() {
        return this.mobSelector;
    }

    public PluginGUI getPluginGUI() {
        return this.pluginGUI;
    }

    public CloudSystem getCloudSystem() {
        return this.cloudSystem;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setAbstractBossService(AbstractBossService abstractBossService) {
        this.abstractBossService = abstractBossService;
    }
}
