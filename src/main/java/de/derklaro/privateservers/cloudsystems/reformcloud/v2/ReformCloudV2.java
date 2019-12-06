package de.derklaro.privateservers.cloudsystems.reformcloud.v2;

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
import de.derklaro.privateservers.utility.api.AbstractBossService;
import systems.reformcloud.reformcloud2.executor.api.common.ExecutorAPI;
import systems.reformcloud.reformcloud2.executor.api.common.configuration.JsonConfiguration;
import systems.reformcloud.reformcloud2.executor.api.common.groups.ProcessGroup;
import systems.reformcloud.reformcloud2.executor.api.common.groups.template.RuntimeConfiguration;
import systems.reformcloud.reformcloud2.executor.api.common.groups.template.Template;
import systems.reformcloud.reformcloud2.executor.api.common.groups.template.Version;
import systems.reformcloud.reformcloud2.executor.api.common.groups.template.backend.basic.FileBackend;
import systems.reformcloud.reformcloud2.executor.api.common.process.ProcessInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ReformCloudV2 extends CloudSystem {

    public ReformCloudV2() {
        AbstractBossService service = new Service(getSelf().isLobby());
        PrivateServers.getInstance().setAbstractBossService(service);
    }

    @Override
    public Double<Boolean, Object> startPrivateServer(UUID requester, String template) {
        ProcessGroup group = ExecutorAPI.getInstance().getProcessGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (group == null) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getServerGroupNotFound());
        }

        if (hasPrivateServer(requester)) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getAlreadyStarted());
        }

        Template reformCloudCopy;
        if (template == null) {
            reformCloudCopy = CollectionUtils.filter(group.getTemplates(), e -> e.getName().equals(requester.toString()));
            if (reformCloudCopy == null) {
                reformCloudCopy = new Template(
                        0,
                        requester.toString(),
                        false,
                        FileBackend.NAME,
                        "#",
                        new RuntimeConfiguration(512, new ArrayList<>(), new HashMap<>()),
                        Version.PAPER_1_8_8
                );
                group.getTemplates().add(reformCloudCopy);
                ExecutorAPI.getInstance().updateProcessGroup(group);
            }
        } else {
            reformCloudCopy = CollectionUtils.filter(group.getTemplates(), e -> e.getName().equals(template));
            if (reformCloudCopy == null) {
                reformCloudCopy = CollectionUtils.filter(group.getTemplates(), e -> e.getName().equals("fallback"));
                if (reformCloudCopy == null) {
                    reformCloudCopy = new Template(
                            0,
                            "fallback",
                            false,
                            FileBackend.NAME,
                            "#",
                            new RuntimeConfiguration(512, new ArrayList<>(), new HashMap<>()),
                            Version.PAPER_1_8_8
                    );
                    group.getTemplates().add(reformCloudCopy);
                    ExecutorAPI.getInstance().updateProcessGroup(group);
                }
            }
        }

        ExecutorAPI.getInstance().startProcess(
                group.getName(),
                reformCloudCopy.getName(),
                new JsonConfiguration()
                        .add("owner", requester)
                        .add("settings", PrivateServers.getInstance().getMobConfig().getMobConfig())
                        .add("message", PrivateServers.getInstance().getMessages())
                        .add("plugins", PrivateServers.getInstance().getMobConfig().getMobConfig().getInstallablePlugins())
                        .add("blocked", PrivateServers.getInstance().getMobConfig().getMobConfig().getBlockedCommands())
                        .add("autoStop", PrivateServers.getInstance().getMobConfig().getMobConfig().isStopOnOwnerLeave())
        );
        return new Double<>(true, "Service is now starting...");
    }

    @Override
    public void stopPrivateServer(UUID owner) {
        ProcessInformation server = of(owner);
        if (server == null) {
            return;
        }

        ExecutorAPI.getInstance().stopProcess(server.getProcessUniqueID());
    }

    @Override
    public boolean sendPlayer(UUID owner) {
        ProcessInformation server = of(owner);
        if (server == null) {
            return false;
        }

        ExecutorAPI.getInstance().connect(owner, server);
        return true;
    }

    @Override
    public boolean hasPrivateServer(UUID uuid) {
        return of(uuid) != null;
    }

    @Override
    public UUID getOwner() {
        return getSelf().getExtra().get("owner", UUID.class);
    }

    @Override
    public boolean isAutoStopOnOwnerLeave() {
        return getSelf().getExtra().getBoolean("autoStop");
    }

    @Override
    public InstallablePlugins getInstallablePlugins() {
        return getSelf().getExtra().get("plugins", new TypeToken<InstallablePlugins>() {});
    }

    @Override
    public Plugin getPlugin(String prefix) {
        InstallablePlugins plugins = getInstallablePlugins();
        if (plugins == null) {
            return null;
        }

        return CollectionUtils.filter(plugins.getPlugins(), e -> e.getName().equalsIgnoreCase(prefix));
    }

    @Override
    public Message getMessage() {
        return getSelf().getExtra().get("message", new TypeToken<Message>() {});
    }

    @Override
    public MobConfig mobConfig() {
        return getSelf().getExtra().get("settings", new TypeToken<MobConfig>() {});
    }

    @Override
    public boolean hasCustomTemplate(UUID uuid) {
        ProcessGroup group = ExecutorAPI.getInstance().getProcessGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (group == null) {
            return false;
        }

        return group.getTemplates().stream().anyMatch(e -> e.getName().equals(uuid.toString()));
    }

    @Override
    public void deleteCustomTemplate(UUID uuid) {
        ProcessGroup group = ExecutorAPI.getInstance().getProcessGroup(PrivateServers.getInstance().getPrivateServerGroup());
        if (group == null) {
            return;
        }

        Template template = CollectionUtils.filter(group.getTemplates(), e -> e.getName().equals(uuid.toString()));
        if (template == null) {
            return;
        }

        group.getTemplates().remove(template);
        ExecutorAPI.getInstance().updateProcessGroup(group);
    }

    @Override
    public List<String> getBlockedCommands() {
        return getSelf().getExtra().get("blocked", new TypeToken<List<String>>() {});
    }

    private ProcessInformation getSelf() {
        ProcessInformation self = ExecutorAPI.getInstance().getThisProcessInformation();
        Preconditions.checkNotNull(self);
        return self;
    }

    private ProcessInformation of(UUID uuid) {
        return CollectionUtils.filter(ExecutorAPI.getInstance().getAllProcesses(),
                e -> e.getExtra().getOrDefault("owner", UUID.class, UUID.randomUUID()).equals(uuid));
    }
}
