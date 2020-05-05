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

import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.cloudnet.v2.legacy.cloud.CloudNetV2CloudService;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitServerAddEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CloudServiceStartAwaitListener implements Listener {

    private static final Map<String, CompletableFuture<CloudService>> FUTURE_MAP = new ConcurrentHashMap<>();

    @NotNull
    public static CompletableFuture<CloudService> awaitStart(@NotNull UUID waitUniqueID) {
        CompletableFuture<CloudService> future = new CompletableFuture<>();
        FUTURE_MAP.put(waitUniqueID.toString(), future);
        return future;
    }

    @EventHandler
    public void handleStart(@NotNull BukkitServerAddEvent event) {
        String waitUniqueID = event.getServerInfo().getServerConfig().getProperties().getString("ps::wait");
        if (waitUniqueID == null) {
            return;
        }

        CompletableFuture<CloudService> future = FUTURE_MAP.remove(waitUniqueID);
        if (future == null) {
            return;
        }

        future.complete(CloudNetV2CloudService.fromServerInfo(event.getServerInfo()).orElse(null));
    }
}
