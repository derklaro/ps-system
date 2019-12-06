package de.derklaro.privateservers.cloudsystems.utility;

import de.derklaro.privateservers.configuration.language.defaults.messages.Message;
import de.derklaro.privateservers.mobs.MobConfig;
import de.derklaro.privateservers.mobs.config.plugins.InstallablePlugins;
import de.derklaro.privateservers.mobs.config.plugins.Plugin;
import de.derklaro.privateservers.utility.Double;

import java.util.List;
import java.util.UUID;

public abstract class CloudSystem {

    public abstract Double<Boolean, Object> startPrivateServer(UUID requester, String template);

    public abstract void stopPrivateServer(UUID owner);

    public abstract boolean sendPlayer(UUID owner);

    public abstract boolean hasPrivateServer(UUID uuid);

    public abstract UUID getOwner();

    public abstract boolean isAutoStopOnOwnerLeave();

    public abstract InstallablePlugins getInstallablePlugins();

    public abstract Plugin getPlugin(String prefix);

    public abstract Message getMessage();

    public abstract MobConfig mobConfig();

    public abstract boolean hasCustomTemplate(UUID uuid);

    public abstract void deleteCustomTemplate(UUID uuid);

    public abstract List<String> getBlockedCommands();
}
