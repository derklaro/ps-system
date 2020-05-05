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
package com.github.derklaro.privateservers.cloudnet.v2.legacy.listeners;

import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.cloudnet.v2.legacy.cloud.CloudNetV2CloudService;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitServerAddEvent;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitServerInfoUpdateEvent;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitServerRemoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class CloudServiceListener implements Listener {

    public CloudServiceListener(CloudSystem cloudSystem) {
        this.cloudSystem = cloudSystem;
    }

    private final CloudSystem cloudSystem;

    @EventHandler
    public void handleStart(@NotNull BukkitServerAddEvent event) {
        CloudNetV2CloudService.fromServerInfo(event.getServerInfo()).ifPresent(
                cloudService -> this.cloudSystem.getCloudServiceManager().handleCloudServiceStart(cloudService)
        );
    }

    @EventHandler
    public void handleUpdate(@NotNull BukkitServerInfoUpdateEvent event) {
        CloudNetV2CloudService.fromServerInfo(event.getServerInfo()).ifPresent(
                cloudService -> this.cloudSystem.getCloudServiceManager().handleCloudServiceUpdate(cloudService)
        );
    }

    @EventHandler
    public void handleStop(@NotNull BukkitServerRemoveEvent event) {
        CloudNetV2CloudService.fromServerInfo(event.getServerInfo()).ifPresent(
                cloudService -> this.cloudSystem.getCloudServiceManager().handleCloudServiceStop(cloudService)
        );
    }
}
