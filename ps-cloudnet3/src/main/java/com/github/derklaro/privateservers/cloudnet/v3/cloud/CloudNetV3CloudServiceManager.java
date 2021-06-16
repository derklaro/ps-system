/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 - 2021 Pasqual Koschmieder and contributors
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
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.cloud.service.creation.CloudServiceCreateConfiguration;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudServiceManager;
import de.dytanic.cloudnet.common.concurrent.ITask;
import de.dytanic.cloudnet.common.concurrent.ITaskListener;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.driver.service.ServiceTemplate;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.wrapper.Wrapper;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

class CloudNetV3CloudServiceManager extends DefaultCloudServiceManager {

  static final DefaultCloudServiceManager INSTANCE = new CloudNetV3CloudServiceManager();

  @Override
  public @NotNull CompletableFuture<CloudService> createCloudService(@NotNull CloudServiceCreateConfiguration configuration) {
    ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(configuration.group());
    if (serviceTask == null) {
      return CompletableFuture.completedFuture(null);
    }

    CompletableFuture<CloudService> future = new CompletableFuture<>();
    this.createCloudService(
      serviceTask,
      new ServiceTemplate("PrivateServers", configuration.template().templateName(), configuration.template().templateBackend()),
      configuration.privateServerConfiguration(),
      future
    );
    return future;
  }

  private void createCloudService(@NotNull ServiceTask task, @NotNull ServiceTemplate template,
    @NotNull CloudServiceConfiguration cloudServiceConfiguration, @NotNull CompletableFuture<CloudService> future) {
    CloudNetDriver.getInstance()
      .getCloudServiceFactory()
      .createCloudServiceAsync(ServiceConfiguration.builder(task)
        .autoDeleteOnStop()
        .staticService(false)
        .templates(template)
        .properties(JsonDocument.newDocument("cloudServiceConfiguration", cloudServiceConfiguration))
        .build()
      )
      .addListener(this.createListener(future));
  }

  @NotNull
  private ITaskListener<ServiceInfoSnapshot> createListener(@NotNull CompletableFuture<CloudService> future) {
    return new ITaskListener<ServiceInfoSnapshot>() {
      @Override
      public void onComplete(ITask<ServiceInfoSnapshot> task, ServiceInfoSnapshot serviceInfoSnapshot) {
        CloudNetDriver.getInstance().getCloudServiceProvider(serviceInfoSnapshot).start();
        future.complete(CloudNetV3CloudService.fromServiceInfoSnapshot(serviceInfoSnapshot).orElse(null));
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

  @Override
  public @NotNull Collection<CloudService> getAllCurrentlyRunningPrivateServersFromCloudSystem() {
    return CloudNetDriver.getInstance().getCloudServiceProvider()
      .getCloudServicesAsync()
      .get(20, TimeUnit.SECONDS, Collections.emptyList())
      .stream()
      .filter(e -> e.getProperty(BridgeServiceProperty.IS_ONLINE).orElse(false))
      .map(e -> CloudNetV3CloudService.fromServiceInfoSnapshot(e).orElse(null))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  @Override
  public @NotNull UUID getCurrentServiceUniqueID() {
    return Wrapper.getInstance().getServiceId().getUniqueId();
  }
}
