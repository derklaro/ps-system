/*
 * MIT License
 *
 * Copyright (c) 2020 Pasqual K. and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.derklaro.privateservers.cloudnet.v2.cloud;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudServiceManager;
import com.github.derklaro.privateservers.common.util.EnumUtil;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.builders.ApiServerProcessBuilder;
import de.dytanic.cloudnet.lib.process.ServerProcessBuilder;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.dytanic.cloudnet.lib.server.template.Template;
import de.dytanic.cloudnet.lib.server.template.TemplateResource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class CloudNetV2CloudServiceManager extends DefaultCloudServiceManager {

    static final CloudServiceManager INSTANCE = new CloudNetV2CloudServiceManager();

    @Override
    public @NotNull CompletableFuture<CloudService> createCloudService(@NotNull String group, @NotNull String templateName,
                                                                       @NotNull String templateBackend, @NotNull CloudServiceConfiguration cloudServiceConfiguration) {
        ServerProcessBuilder builder = ApiServerProcessBuilder.create(group)
                .template(this.createTemplate(templateName, templateBackend));
        builder.getServerConfig().getProperties().append("cloudServiceConfiguration", cloudServiceConfiguration);
        return this.start(builder);
    }

    @NotNull
    private Template createTemplate(@NotNull String templateName, @NotNull String templateBackend) {
        return new Template(
                templateName,
                EnumUtil.findEnumField(TemplateResource.class, templateBackend.toUpperCase(), TemplateResource.LOCAL),
                null,
                new String[0],
                new ArrayList<>()
        );
    }

    @NotNull
    private CompletableFuture<CloudService> start(@NotNull ServerProcessBuilder builder) {
        CompletableFuture<CloudService> future = new CompletableFuture<>();
        builder.startServer().thenAccept(serverProcessMeta -> {
            ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(serverProcessMeta.getServiceId().getServerId());
            if (serverInfo == null) {
                future.complete(null);
            } else {
                future.complete(CloudNetV2CloudService.fromServerInfo(serverInfo).orElse(null));
            }
        }).exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        });
        return future;
    }

    @Override
    public @NotNull Collection<CloudService> getAllCurrentlyRunningPrivateServersFromCloudSystem() {
        return CloudAPI.getInstance().getServers()
                .stream()
                .filter(ServerInfo::isOnline)
                .map(e -> CloudNetV2CloudService.fromServerInfo(e).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull UUID getCurrentServiceUniqueID() {
        return CloudAPI.getInstance().getServiceId().getUniqueId();
    }
}
