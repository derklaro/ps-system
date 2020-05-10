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
package com.github.derklaro.privateservers.reformcloud.v2.cloud;

import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.api.cloud.util.ConnectionRequest;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudService;
import com.github.derklaro.privateservers.reformcloud.v2.connection.ReformCloudV2ConnectionRequest;
import org.jetbrains.annotations.NotNull;
import systems.reformcloud.reformcloud2.executor.api.common.ExecutorAPI;
import systems.reformcloud.reformcloud2.executor.api.common.process.ProcessInformation;

import java.util.Optional;
import java.util.UUID;

public final class ReformCloudV2CloudService extends DefaultCloudService {

    @NotNull
    public static Optional<CloudService> fromProcessInformation(@NotNull ProcessInformation processInformation) {
        CloudServiceConfiguration configuration = processInformation.getExtra().get("cloudServiceConfiguration", CloudServiceConfiguration.class);
        if (configuration == null) {
            return Optional.empty();
        }

        return Optional.of(new ReformCloudV2CloudService(processInformation, configuration));
    }

    private ReformCloudV2CloudService(@NotNull ProcessInformation processInformation, @NotNull CloudServiceConfiguration cloudServiceConfiguration) {
        super(processInformation.getProcessDetail().getName(), processInformation.getProcessDetail().getProcessUniqueID(), cloudServiceConfiguration);
        this.processInformation = processInformation;
    }

    private final ProcessInformation processInformation;

    @Override
    public @NotNull ConnectionRequest createConnectionRequest(@NotNull UUID targetPlayerUniqueID) {
        return new ReformCloudV2ConnectionRequest(this, targetPlayerUniqueID);
    }

    @Override
    public void publishCloudServiceInfoUpdate() {
        this.processInformation.getExtra().add("cloudServiceConfiguration", super.cloudServiceConfiguration);
        ExecutorAPI.getInstance().getSyncAPI().getProcessSyncAPI().update(this.processInformation);
    }

    @Override
    public void copyCloudService() {
        this.processInformation.toWrapped().copy();
    }

    @Override
    public void shutdown() {
        if (super.cloudServiceConfiguration.isAutoSaveBeforeStop()) {
            this.copyCloudService();
        }

        this.processInformation.toWrapped().stop();
    }
}
