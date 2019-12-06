package de.derklaro.privateservers.cloudsystems.cloudnet.v3;

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
import de.dytanic.cloudnet.common.collection.Iterables;
import de.dytanic.cloudnet.common.concurrent.ITask;
import de.dytanic.cloudnet.common.concurrent.ITaskListener;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.*;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.ProxyFallback;
import de.dytanic.cloudnet.ext.bridge.ProxyFallbackConfiguration;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.wrapper.Wrapper;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class CloudNetV3 extends CloudSystem {

    public CloudNetV3() {
        AbstractBossService abstractBossService = new Service(isLobby());
        PrivateServers.getInstance().setAbstractBossService(abstractBossService);
    }

    @Override
    public Double<Boolean, Object> startPrivateServer(UUID requester, String template) {
        ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(PrivateServers.getInstance().getPrivateServerGroup());
        if (serviceTask == null) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getServerGroupNotFound());
        }

        if (hasPrivateServer(requester)) {
            return new Double<>(false, PrivateServers.getInstance().getMessages().getAlreadyStarted());
        }

        Collection<String> groups = serviceTask.getGroups();
        if (!groups.contains(PrivateServers.getInstance().getPrivateServerGroup())) {
            groups.add(PrivateServers.getInstance().getPrivateServerGroup());
        }

        ServiceTemplate defaultTemplate = CollectionUtils.filter(serviceTask.getTemplates(), e -> e.getName().equals("default"));
        ServiceTemplate startTemplate;
        if (template == null) {
            ServiceTemplate serviceTemplate = CollectionUtils.filter(serviceTask.getTemplates(), e -> e.getPrefix().equals(requester.toString()));
            if (serviceTemplate == null) {
                startTemplate = new ServiceTemplate(
                        requester.toString(),
                        requester.toString(),
                        "local"
                );
                serviceTask.getTemplates().add(startTemplate);
                CloudNetDriver.getInstance().getServiceTaskProvider().addPermanentServiceTask(serviceTask);
            } else
                startTemplate = serviceTemplate;
        } else {
            ServiceTemplate serviceTemplate = CollectionUtils.filter(serviceTask.getTemplates(), e -> e.getPrefix().equals(template));
            if (serviceTemplate == null) {
                startTemplate = new ServiceTemplate(
                        template,
                        template,
                        "local"
                );
                serviceTask.getTemplates().add(startTemplate);
                CloudNetDriver.getInstance().getServiceTaskProvider().addPermanentServiceTask(serviceTask);
            } else
                startTemplate = serviceTemplate;
        }

        ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceFactory().createCloudService(
                serviceTask.getName(),
                serviceTask.getRuntime(),
                true,
                false,
                Iterables.newArrayList(),
                Iterables.newArrayList(new ServiceTemplate[]{defaultTemplate, startTemplate}),
                Iterables.newArrayList(),
                serviceTask.getGroups(),
                new ProcessConfiguration(
                        ServiceEnvironmentType.MINECRAFT_SERVER,
                        serviceTask.getProcessConfiguration().getMaxHeapMemorySize(),
                        Iterables.newArrayList()
                ),
                JsonDocument.newDocument()
                        .append("owner", requester)
                        .append("message", PrivateServers.getInstance().getMessages())
                        .append("plugins", PrivateServers.getInstance().getMobConfig().getMobConfig().getInstallablePlugins())
                        .append("blocked", PrivateServers.getInstance().getMobConfig().getMobConfig().getBlockedCommands())
                        .append("settings", PrivateServers.getInstance().getMobConfig().getMobConfig())
                        .append("autoStop", PrivateServers.getInstance().getMobConfig().getMobConfig().isStopOnOwnerLeave()),
                null
        );
        return new Double<>(true, serviceInfoSnapshot);
    }

    @Override
    public void stopPrivateServer(UUID owner) {
        CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServicesAsync().addListener(new ITaskListener<Collection<ServiceInfoSnapshot>>() {
            @Override
            public void onComplete(ITask<Collection<ServiceInfoSnapshot>> task, Collection<ServiceInfoSnapshot> serviceInfoSnapshots) {
                serviceInfoSnapshots.forEach(e -> {
                    if (e.getConfiguration().getProperties().contains("owner")
                            && e.getConfiguration().getProperties().get("owner", UUID.class).equals(owner)) {
                        CloudNetDriver.getInstance().getCloudServiceProvider(e.getServiceId().getUniqueId()).setCloudServiceLifeCycle(ServiceLifeCycle.DELETED);
                    }
                });
            }
        }).addListener(new ITaskListener<Collection<ServiceInfoSnapshot>>() {
            @Override
            public void onFailure(ITask<Collection<ServiceInfoSnapshot>> task, Throwable th) {
                th.printStackTrace();
            }
        });
    }

    @Override
    public boolean sendPlayer(UUID owner) {
        ICloudPlayer iCloudPlayer = BridgePlayerManager.getInstance().getOnlinePlayer(owner);
        if (iCloudPlayer == null) {
            return false;
        }

        for (ServiceInfoSnapshot cloudService : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices()) {
            if (cloudService.getConfiguration().getProperties().contains("owner")
                    && cloudService.getConfiguration().getProperties().get("owner", UUID.class).equals(owner)) {
                BridgePlayerManager.getInstance().proxySendPlayer(iCloudPlayer, cloudService.getServiceId().getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPrivateServer(UUID uuid) {
        for (ServiceInfoSnapshot cloudService : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices()) {
            if (cloudService.getConfiguration().getProperties().contains("owner")
                    && cloudService.getConfiguration().getProperties().get("owner", UUID.class).equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public UUID getOwner() {
      return Wrapper.getInstance().getServiceConfiguration().getProperties().get("owner", UUID.class);
    }

    @Override
    public boolean isAutoStopOnOwnerLeave() {
        return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getConfiguration().getProperties().getBoolean("autoStop");
    }

    @Override
    public InstallablePlugins getInstallablePlugins() {
        return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getConfiguration().getProperties().get("plugins", new TypeToken<InstallablePlugins>() {
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
        return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getConfiguration().getProperties().get("message", new TypeToken<Message>() {
        }.getType());
    }

    @Override
    public MobConfig mobConfig() {
        return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getConfiguration().getProperties().get("settings", new TypeToken<MobConfig>() {
        }.getType());
    }

    @Override
    public boolean hasCustomTemplate(UUID uuid) {
        ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(PrivateServers.getInstance().getPrivateServerGroup());
        if (serviceTask == null) {
            return false;
        }

        ServiceTemplate serviceTemplate = CollectionUtils.filter(serviceTask.getTemplates(), e -> e.getName().equals(uuid.toString()));
        return serviceTemplate != null;
    }

    @Override
    public void deleteCustomTemplate(UUID uuid) {
        ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(PrivateServers.getInstance().getPrivateServerGroup());
        if (serviceTask == null) {
            return;
        }

        ServiceTemplate serviceTemplate = CollectionUtils.filter(serviceTask.getTemplates(), e -> e.getName().equals(uuid.toString()));
        if (serviceTemplate == null) {
            return;
        }

        CloudNetDriver.getInstance().sendCommandLine("lt delete " + serviceTask.getName() + " " + serviceTemplate.getPrefix());
    }

    @Override
    public List<String> getBlockedCommands() {
        return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getConfiguration().getProperties().get("blocked", new TypeToken<List<String>>() {
        }.getType());
    }

    private boolean isLobby() {
        for (ProxyFallbackConfiguration bungeeFallbackConfiguration : de.dytanic.cloudnet.ext.bridge.BridgeConfigurationProvider.load().getBungeeFallbackConfigurations()) {
            if (bungeeFallbackConfiguration.getDefaultFallbackTask().equals(Wrapper.getInstance().getCurrentServiceInfoSnapshot().getServiceId().getTaskName())) {
                return true;
            }

            for (ProxyFallback fallback : bungeeFallbackConfiguration.getFallbacks()) {
                if (fallback.getTask().equals(Wrapper.getInstance().getCurrentServiceInfoSnapshot().getServiceId().getTaskName())) {
                    return true;
                }
            }
        }

        return false;
    }
}
