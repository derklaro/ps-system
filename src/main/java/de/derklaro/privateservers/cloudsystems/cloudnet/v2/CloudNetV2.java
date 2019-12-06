package de.derklaro.privateservers.cloudsystems.cloudnet.v2;

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
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.player.PlayerExecutorBridge;
import de.dytanic.cloudnet.lib.server.ServerConfig;
import de.dytanic.cloudnet.lib.server.ServerGroup;
import de.dytanic.cloudnet.lib.server.ServerGroupMode;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.dytanic.cloudnet.lib.server.template.Template;
import de.dytanic.cloudnet.lib.server.template.TemplateResource;
import de.dytanic.cloudnet.lib.utility.document.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class CloudNetV2 extends CloudSystem {

    public CloudNetV2() {
        AbstractBossService abstractBossService = new Service(
                CloudAPI.getInstance().getServerGroup(CloudAPI.getInstance().getServiceId().getGroup())
                        .getGroupMode().equals(ServerGroupMode.LOBBY)
                        || CloudAPI.getInstance().getServerGroup(CloudAPI.getInstance().getServiceId().getGroup())
                        .getGroupMode().equals(ServerGroupMode.STATIC_LOBBY)
        );
        PrivateServers.getInstance().setAbstractBossService(abstractBossService);
    }

    @Override
    public Double<Boolean, Object> startPrivateServer(UUID requester, String template) {
        if (!Validate.notNull(requester)) {
            return new Double<>(false, "Please give only non-null variables");
        }

        ServerInfo ofPlayer = CollectionUtils.filter(CloudAPI.getInstance().getServers(),
                serverInfo -> serverInfo.getServiceId().getGroup().equals(PrivateServers.getInstance().getPrivateServerGroup())
                        && serverInfo.getServerConfig().getProperties().contains("owner")
                        && serverInfo.getServerConfig().getProperties().getObject("owner", UUID.class).equals(requester));
        if (ofPlayer != null) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getAlreadyStarted());
        }

        ServerGroup serverGroup = CloudAPI.getInstance().getServerGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (serverGroup == null) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getServerGroupNotFound());
        }

        Template template1;
        if (template == null) {
            template1 = CollectionUtils.filter(serverGroup.getTemplates(), e -> e.getName().equals(requester.toString()));
            if (template1 == null) {
                template1 = new Template(
                        requester.toString(),
                        TemplateResource.LOCAL,
                        null,
                        new String[0],
                        new LinkedList<>()
                );
                serverGroup.getTemplates().add(template1);
                CloudAPI.getInstance().updateServerGroup(serverGroup);
            }
        } else {
            template1 = CollectionUtils.filter(serverGroup.getTemplates(), e -> e.getName().equals(template));
            if (template1 == null) {
                Template fallback = CollectionUtils.filter(serverGroup.getTemplates(), e -> e.getName().equals("fallback"));
                if (fallback != null) {
                    template1 = fallback;
                } else {
                    template1 = new Template(
                            "fallback",
                            TemplateResource.LOCAL,
                            null,
                            new String[0],
                            new LinkedList<>()
                    );
                    serverGroup.getTemplates().add(template1);
                    CloudAPI.getInstance().updateServerGroup(serverGroup);
                }
            }
        }

        CloudAPI.getInstance().startGameServer(
                serverGroup.toSimple(),
                new ServerConfig(
                        true,
                        "null",
                        new Document()
                                .append("owner", Document.GSON.toJsonTree(requester))
                                .append("plugins", Document.GSON.toJsonTree(PrivateServers.getInstance().getMobConfig().getMobConfig().getInstallablePlugins()))
                                .append("message", Document.GSON.toJsonTree(PrivateServers.getInstance().getMessages()))
                                .append("blocked", Document.GSON.toJsonTree(PrivateServers.getInstance().getMobConfig().getMobConfig().getBlockedCommands()))
                                .append("settings", Document.GSON.toJsonTree(PrivateServers.getInstance().getMobConfig().getMobConfig()))
                                .append("autoStop", PrivateServers.getInstance().getMobConfig().getMobConfig().isStopOnOwnerLeave()),
                        System.currentTimeMillis()
                ), template1
        );

        return new Double<>(true, "Service is now starting...");
    }

    @Override
    public void stopPrivateServer(UUID owner) {
        Preconditions.checkNotNull(owner);

        ServerInfo ofPlayer = CollectionUtils.filter(CloudAPI.getInstance().getServers(),
                serverInfo -> serverInfo.getServiceId().getGroup().equals(PrivateServers.getInstance().getPrivateServerGroup())
                        && serverInfo.getServerConfig().getProperties().contains("owner")
                        && serverInfo.getServerConfig().getProperties().getObject("owner", UUID.class).equals(owner));
        if (ofPlayer == null) {
            return;
        }

        if (ofPlayer.getTemplate().getName().equals(owner.toString())) {
            CloudAPI.getInstance().sendCloudCommand("copy " + ofPlayer.getServiceId().getServerId());
            Bukkit.getScheduler().runTaskLaterAsynchronously(PrivateServers.getInstance(), () -> CloudAPI.getInstance().stopServer(ofPlayer.getServiceId().getServerId()), 20);
            return;
        }

        CloudAPI.getInstance().stopServer(ofPlayer.getServiceId().getServerId());
    }

    @Override
    public boolean sendPlayer(UUID owner) {
        if (!Validate.notNull(owner)) {
            return false;
        }

        ServerInfo ofPlayer = CollectionUtils.filter(CloudAPI.getInstance().getServers(),
                serverInfo -> serverInfo.getServiceId().getGroup().equals(PrivateServers.getInstance().getPrivateServerGroup())
                        && serverInfo.getServerConfig().getProperties().contains("owner")
                        && serverInfo.getServerConfig().getProperties().getObject("owner", UUID.class).equals(owner));
        if (ofPlayer == null) {
            return false;
        }

        PlayerExecutorBridge.INSTANCE.sendPlayer(
                CloudAPI.getInstance().getOnlinePlayer(owner), ofPlayer.getServiceId().getServerId()
        );
        return true;
    }

    @Override
    public boolean hasPrivateServer(UUID uuid) {
        ServerInfo ofPlayer = CollectionUtils.filter(CloudAPI.getInstance().getServers(),
                serverInfo -> serverInfo.getServerConfig().getProperties().contains("owner")
                        && serverInfo.getServerConfig().getProperties().getObject("owner", UUID.class).equals(uuid));
        return ofPlayer != null;
    }

    @Override
    public UUID getOwner() {
        ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(CloudAPI.getInstance().getServiceId().getServerId());
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getServerConfig().getProperties().contains("owner")
                ? serverInfo.getServerConfig().getProperties().getObject("owner", UUID.class)
                : null;
    }

    @Override
    public boolean isAutoStopOnOwnerLeave() {
        ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(CloudAPI.getInstance().getServiceId().getServerId());
        if (serverInfo == null) {
            return false;
        }

        return serverInfo.getServerConfig().getProperties().getBoolean("autoStop");
    }

    @Override
    public InstallablePlugins getInstallablePlugins() {
        ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(CloudAPI.getInstance().getServiceId().getServerId());
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getServerConfig().getProperties().getObject("plugins", new TypeToken<InstallablePlugins>() {
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
        ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(CloudAPI.getInstance().getServiceId().getServerId());
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getServerConfig().getProperties().getObject("message", new TypeToken<Message>() {
        }.getType());
    }

    @Override
    public MobConfig mobConfig() {
        ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(CloudAPI.getInstance().getServiceId().getServerId());
        if (serverInfo == null) {
            return null;
        }

        return serverInfo.getServerConfig().getProperties().getObject("settings", new TypeToken<MobConfig>() {
        }.getType());
    }

    @Override
    public boolean hasCustomTemplate(UUID uuid) {
        ServerGroup serverGroup = CloudAPI.getInstance().getServerGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (serverGroup == null) {
            return false;
        }

        Template template = CollectionUtils.filter(serverGroup.getTemplates(), e -> e.getName().equals(uuid.toString()));
        return template != null;
    }

    @Override
    public void deleteCustomTemplate(UUID uuid) {
        ServerGroup serverGroup = CloudAPI.getInstance().getServerGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (serverGroup == null) {
            return;
        }

        Template template = CollectionUtils.filter(serverGroup.getTemplates(), e -> e.getName().equals(uuid.toString()));
        if (template == null) {
            return;
        }

        serverGroup.getTemplates().remove(template);
        CloudAPI.getInstance().updateServerGroup(serverGroup);
    }

    @Override
    public List<String> getBlockedCommands() {
        ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(CloudAPI.getInstance().getServiceId().getServerId());
        if (serverInfo == null) {
            return new ArrayList<>();
        }

        return serverInfo.getServerConfig().getProperties().getObject("blocked", new TypeToken<List<String>>() {
        }.getType());
    }
}
