package de.derklaro.privateservers.cloudsystems.reformcloud.v1;

import com.google.common.base.Preconditions;
import com.google.gson.reflect.TypeToken;
import de.derklaro.privateservers.PrivateServers;
import de.derklaro.privateservers.api.boss.Service;
import de.derklaro.privateservers.cloudsystems.utility.CloudSystem;
import de.derklaro.privateservers.configuration.language.defaults.messages.Message;
import de.derklaro.privateservers.mobs.MobConfig;
import de.derklaro.privateservers.mobs.config.plugins.InstallablePlugins;
import de.derklaro.privateservers.mobs.config.plugins.Plugin;
import de.derklaro.privateservers.utility.CollectionUtils;
import de.derklaro.privateservers.utility.Double;
import de.derklaro.privateservers.utility.Validate;
import de.derklaro.privateservers.utility.api.AbstractBossService;
import org.bukkit.Bukkit;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.api.DefaultPlayerProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.ServerState;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.server.ServerGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ReformCloudV1 extends CloudSystem {

    public ReformCloudV1() {
        AbstractBossService abstractBossService = new Service(ReformCloudAPISpigot.getInstance()
                .getServerInfo().getServerGroup().getServerModeType().equals(ServerModeType.LOBBY));
        PrivateServers.getInstance().setAbstractBossService(abstractBossService);

        if (!abstractBossService.isLobbyInstance() && this.getOwner() != null) {
            ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
            serverInfo.setServerState(ServerState.HIDDEN);
            ReformCloudAPISpigot.getInstance().updateServerInfo(serverInfo);
        }
    }

    @Override
    public Double<Boolean, Object> startPrivateServer(UUID requester, String template) {
        if (!Validate.notNull(requester)) {
            return new Double<>(false, "Please give only non-null variables");
        }

        ServerInfo ofPlayer = CollectionUtils.filter(ReformCloudAPISpigot.getInstance().getAllRegisteredServers(),
                serverInfo -> serverInfo.getServerGroup().getName().equals(PrivateServers.getInstance().getPrivateServerGroup())
                        && serverInfo.getCloudProcess().getPreConfig().contains("owner")
                        && serverInfo.getCloudProcess().getPreConfig().getValue("owner", UUID.class).equals(requester));
        if (ofPlayer != null) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getAlreadyStarted());
        }

        ServerGroup serverGroup = ReformCloudAPISpigot.getInstance().getServerGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (serverGroup == null) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getServerGroupNotFound());
        }

        Template template1;
        if (template == null) {
            template1 = CollectionUtils.filter(serverGroup.getTemplates(), e -> e.getName().equals(requester.toString()));
            if (template1 == null) {
                template1 = new Template(
                        requester.toString(),
                        null,
                        TemplateBackend.CLIENT
                );
                serverGroup.getTemplates().add(template1);
                ReformCloudAPISpigot.getInstance().updateServerGroup(serverGroup);
            }
        } else {
            template1 = CollectionUtils.filter(serverGroup.getTemplates(), e -> e.getName().equals(template));
            if (template1 == null) {
                if (serverGroup.getTemplateOrElseNull("fallback") == null) {
                    template1 = new Template(
                            "fallback",
                            null,
                            TemplateBackend.CLIENT
                    );
                    serverGroup.getTemplates().add(template1);
                    ReformCloudAPISpigot.getInstance().updateServerGroup(serverGroup);
                } else {
                    template1 = serverGroup.getTemplate("fallback");
                }
            }
        }

        ReformCloudAPISpigot.getInstance().startGameServer(
                serverGroup,
                new Configuration()
                        .addValue("owner", requester)
                        .addValue("settings", PrivateServers.getInstance().getMobConfig().getMobConfig())
                        .addValue("message", PrivateServers.getInstance().getMessages())
                        .addValue("plugins", PrivateServers.getInstance().getMobConfig().getMobConfig()
                                .getInstallablePlugins())
                        .addValue("blocked",
                                PrivateServers.getInstance().getMobConfig().getMobConfig().getBlockedCommands())
                        .addBooleanValue("autoStop",
                                PrivateServers.getInstance().getMobConfig().getMobConfig()
                                        .isStopOnOwnerLeave()),
                template1.getName()
        );

        return new Double<>(true, true);
    }

    @Override
    public void stopPrivateServer(UUID owner) {
        Preconditions.checkNotNull(owner);
        ServerInfo ofPlayer = CollectionUtils.filter(ReformCloudAPISpigot.getInstance().getAllRegisteredServers(),
                serverInfo -> serverInfo.getServerGroup().getName().equals(PrivateServers.getInstance().getPrivateServerGroup())
                        && serverInfo.getCloudProcess().getPreConfig().contains("owner")
                        && serverInfo.getCloudProcess().getPreConfig().getValue("owner", UUID.class).equals(owner));
        if (ofPlayer == null) {
            return;
        }

        if (ofPlayer.getCloudProcess().getLoadedTemplate().getName().equals(owner.toString())) {
            ReformCloudAPISpigot.getInstance()
                    .dispatchConsoleCommand("copy " + ofPlayer.getCloudProcess().getName());

            Bukkit.getScheduler().runTaskLaterAsynchronously(PrivateServers.getInstance(), () -> ReformCloudAPISpigot.getInstance().stopServer(ofPlayer), 20);
            return;
        }

        ReformCloudAPISpigot.getInstance().stopServer(ofPlayer);
    }

    @Override
    public boolean sendPlayer(UUID owner) {
        if (!Validate.notNull(owner)) {
            return false;
        }

        ServerInfo ofPlayer = CollectionUtils.filter(ReformCloudAPISpigot.getInstance().getAllRegisteredServers(),
                serverInfo -> serverInfo.getCloudProcess().getPreConfig().contains("owner")
                        && serverInfo.getCloudProcess().getPreConfig().getValue("owner", UUID.class).equals(owner));
        if (ofPlayer == null) {
            return false;
        }

        DefaultPlayerProvider.instance.get().sendPlayer(owner, ofPlayer);
        return true;
    }

    @Override
    public boolean hasPrivateServer(UUID uuid) {
        ServerInfo ofPlayer = CollectionUtils.filter(ReformCloudAPISpigot.getInstance().getAllRegisteredServers(),
                serverInfo -> serverInfo.getServerGroup().getName().equals(PrivateServers.getInstance().getPrivateServerGroup())
                        && serverInfo.getCloudProcess().getPreConfig().contains("owner")
                        && serverInfo.getCloudProcess().getPreConfig().getValue("owner", UUID.class).equals(uuid));
        return ofPlayer != null;
    }

    @Override
    public UUID getOwner() {
        ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getCloudProcess().getPreConfig().contains("owner")
                ? serverInfo.getCloudProcess().getPreConfig().getValue("owner", UUID.class)
                : null;
    }

    @Override
    public boolean isAutoStopOnOwnerLeave() {
        ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        if (serverInfo == null) {
            return false;
        }

        return serverInfo.getCloudProcess().getPreConfig().getBooleanValue("autoStop");
    }

    @Override
    public InstallablePlugins getInstallablePlugins() {
        ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getCloudProcess().getPreConfig().getValue("plugins", new TypeToken<InstallablePlugins>() {
        }.getType());
    }

    @Override
    public Plugin getPlugin(String prefix) {
        InstallablePlugins installablePlugins = this.getInstallablePlugins();
        if (installablePlugins == null) {
            return null;
        }

        return CollectionUtils.filter(installablePlugins.getPlugins(), e -> e.getName().equalsIgnoreCase(prefix));
    }

    @Override
    public Message getMessage() {
        ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getCloudProcess().getPreConfig().getValue("message", new TypeToken<Message>() {
        });
    }

    @Override
    public MobConfig mobConfig() {
        ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getCloudProcess().getPreConfig().getValue("settings", new TypeToken<MobConfig>() {
        });
    }

    @Override
    public boolean hasCustomTemplate(UUID uuid) {
        ServerGroup serverGroup = ReformCloudAPISpigot.getInstance().getServerGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (serverGroup == null) {
            return false;
        }

        return serverGroup.getTemplateOrElseNull(uuid.toString()) != null;
    }

    @Override
    public void deleteCustomTemplate(UUID uuid) {
        ServerGroup serverGroup = ReformCloudAPISpigot.getInstance().getServerGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (serverGroup == null) {
            return;
        }

        Template template = serverGroup.getTemplateOrElseNull(uuid.toString());
        if (template == null) {
            return;
        }

        ReformCloudAPISpigot.getInstance().dispatchConsoleCommand(
                "delete servertemplate " + PrivateServers.getInstance().getPrivateServerGroup() + " "
                        + template.getName()
        );
    }

    @Override
    public List<String> getBlockedCommands() {
        ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        if (serverInfo == null) {
            return new ArrayList<>();
        }

        return serverInfo.getCloudProcess().getPreConfig().getValue("blocked", new TypeToken<List<String>>() {
        });
    }
}
