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
package com.github.derklaro.privateservers.runner.listeners;

import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

  private final CloudSystem cloudSystem;

  public PlayerQuitListener(CloudSystem cloudSystem) {
    this.cloudSystem = cloudSystem;
  }

  @EventHandler
  public void handle(final @NotNull PlayerQuitEvent event) {
    this.cloudSystem.getCloudServiceManager().getCurrentCloudService().map(CloudService::getCloudServiceConfiguration).ifPresent(cloudServiceConfiguration -> {
      if (cloudServiceConfiguration.isAutoDeleteAfterOwnerLeave() && event.getPlayer().getUniqueId().equals(cloudServiceConfiguration.getOwnerUniqueId())) {
        this.cloudSystem.getCloudServiceManager().getCurrentCloudService().ifPresent(CloudService::shutdown);
      }
    });
  }
}
