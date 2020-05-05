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
package com.github.derklaro.privateservers.cloudnet.v3.cloud;

import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudServiceManager;
import com.github.derklaro.privateservers.common.util.EmptyArrayList;
import de.dytanic.cloudnet.common.concurrent.ITask;
import de.dytanic.cloudnet.common.concurrent.ITaskListener;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.driver.service.ServiceTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

class CloudNETV3CloudServiceManager extends DefaultCloudServiceManager {

    static final DefaultCloudServiceManager INSTANCE = new CloudNETV3CloudServiceManager();

    @Override
    public @NotNull CompletableFuture<CloudService> createCloudService(@NotNull String group, @NotNull String templateName, @NotNull String templateBackend,
                                                                       @NotNull CloudServiceConfiguration cloudServiceConfiguration) {
        ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(group);
        if (serviceTask == null) {
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<CloudService> future = new CompletableFuture<>();
        this.createCloudService(serviceTask, new ServiceTemplate("PrivateServers", templateName, templateBackend), cloudServiceConfiguration, future);
        return future;
    }

    private void createCloudService(@NotNull ServiceTask task, @NotNull ServiceTemplate template,
                                    @NotNull CloudServiceConfiguration cloudServiceConfiguration, @NotNull CompletableFuture<CloudService> future) {
        CloudNetDriver.getInstance()
                .getCloudServiceFactory()
                .createCloudServiceAsync(
                        task.getName(),
                        task.getRuntime(),
                        true,
                        false,
                        EmptyArrayList.emptyList(),
                        Collections.singletonList(template),
                        EmptyArrayList.emptyList(),
                        task.getGroups(),
                        task.getProcessConfiguration(),
                        JsonDocument.newDocument("cloudServiceConfiguration", cloudServiceConfiguration),
                        null
                ).addListener(this.createListener(future));
    }

    @NotNull
    private ITaskListener<ServiceInfoSnapshot> createListener(@NotNull CompletableFuture<CloudService> future) {
        return new ITaskListener<ServiceInfoSnapshot>() {
            @Override
            public void onComplete(ITask<ServiceInfoSnapshot> task, ServiceInfoSnapshot serviceInfoSnapshot) {
                CloudNetDriver.getInstance().getCloudServiceProvider(serviceInfoSnapshot).start();
                future.complete(CloudNETV3CloudService.fromServiceInfoSnapshot(serviceInfoSnapshot).orElse(null));
            }

            @Override
            public void onCancelled(ITask<ServiceInfoSnapshot> task) {
                future.completeExceptionally(new TimeoutException());
            }

            @Override
            public void onFailure(ITask<ServiceInfoSnapshot> task, Throwable th) {
                future.completeExceptionally(th);
            }
        };
    }
}
