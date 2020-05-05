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
package com.github.derklaro.privateservers.cloudnet.v3.listeners;

import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.cloudnet.v3.cloud.CloudNETV3CloudService;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceInfoUpdateEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStartEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStopEvent;
import org.jetbrains.annotations.NotNull;

public class CloudServiceListener {

    public CloudServiceListener(CloudSystem cloudSystem) {
        this.cloudSystem = cloudSystem;
    }

    private final CloudSystem cloudSystem;

    @EventListener
    public void handleStart(@NotNull CloudServiceStartEvent event) {
        CloudNETV3CloudService.fromServiceInfoSnapshot(event.getServiceInfo()).ifPresent(
                cloudService -> this.cloudSystem.getCloudServiceManager().handleCloudServiceStart(cloudService)
        );
    }

    @EventListener
    public void handleUpdate(@NotNull CloudServiceInfoUpdateEvent event) {
        CloudNETV3CloudService.fromServiceInfoSnapshot(event.getServiceInfo()).ifPresent(
                cloudService -> this.cloudSystem.getCloudServiceManager().handleCloudServiceUpdate(cloudService)
        );
    }

    @EventListener
    public void handleStop(@NotNull CloudServiceStopEvent event) {
        CloudNETV3CloudService.fromServiceInfoSnapshot(event.getServiceInfo()).ifPresent(
                cloudService -> this.cloudSystem.getCloudServiceManager().handleCloudServiceStop(cloudService)
        );
    }
}
