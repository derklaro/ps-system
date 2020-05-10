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
import com.github.derklaro.privateservers.api.cloud.util.ConnectionRequest;
import com.github.derklaro.privateservers.cloudnet.v3.connection.CloudNetV3ConnectionRequest;
import com.github.derklaro.privateservers.cloudnet.v3.util.CloudNetV3Constants;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudService;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class CloudNetV3CloudService extends DefaultCloudService {

    @NotNull
    public static Optional<CloudService> fromServiceInfoSnapshot(@NotNull ServiceInfoSnapshot serviceInfoSnapshot) {
        return serviceInfoSnapshot.getProperty(CloudNetV3Constants.CLOUD_SERVICE_CONFIG_PROPERTY).map(serviceConfiguration -> new CloudNetV3CloudService(
                serviceInfoSnapshot,
                serviceConfiguration
        ));
    }

    private CloudNetV3CloudService(@NotNull ServiceInfoSnapshot serviceInfoSnapshot, @NotNull CloudServiceConfiguration cloudServiceConfiguration) {
        super(serviceInfoSnapshot.getServiceId().getName(), serviceInfoSnapshot.getServiceId().getUniqueId(), cloudServiceConfiguration);
        this.serviceInfoSnapshot = serviceInfoSnapshot;
    }

    private final ServiceInfoSnapshot serviceInfoSnapshot;

    @Override
    public @NotNull ConnectionRequest createConnectionRequest(@NotNull UUID targetPlayerUniqueID) {
        return new CloudNetV3ConnectionRequest(this, targetPlayerUniqueID);
    }

    @Override
    public void publishCloudServiceInfoUpdate() {
        CloudNetV3Constants.CLOUD_SERVICE_CONFIG_PROPERTY.set(this.serviceInfoSnapshot, super.cloudServiceConfiguration);
        Wrapper.getInstance().publishServiceInfoUpdate(this.serviceInfoSnapshot);
    }

    @Override
    public void copyCloudService() {
        try {
            this.serviceInfoSnapshot.provider().deployResourcesAsync(false).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
        }
    }

    @Override
    public void shutdown() {
        if (super.cloudServiceConfiguration.isAutoSaveBeforeStop()) {
            this.copyCloudService();
        }

        CloudNetDriver.getInstance().getCloudServiceProvider(this.serviceInfoSnapshot).stop();
    }
}
