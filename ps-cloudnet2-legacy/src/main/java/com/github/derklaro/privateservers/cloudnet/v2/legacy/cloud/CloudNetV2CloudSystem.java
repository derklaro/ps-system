/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2021 Pasqual K. and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.derklaro.privateservers.cloudnet.v2.legacy.cloud;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.cloudnet.v2.legacy.listeners.CloudNetV2CloudServiceListener;
import com.github.derklaro.privateservers.cloudnet.v2.legacy.listeners.CloudServiceStartAwaitListener;
import de.dytanic.cloudnet.bridge.CloudServer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class CloudNetV2CloudSystem implements CloudSystem {

  public CloudNetV2CloudSystem() {
    Bukkit.getPluginManager().registerEvents(new CloudNetV2CloudServiceListener(this), CloudServer.getInstance().getPlugin());
    Bukkit.getPluginManager().registerEvents(new CloudServiceStartAwaitListener(), CloudServer.getInstance().getPlugin());
  }

  @Override
  public @NotNull String getIdentifierClass() {
    return "de.dytanic.cloudnet.lib.utility.CollectionWrapper";
  }

  @Override
  public @NotNull String getName() {
    return "CloudNetV2-Legacy";
  }

  @Override
  public @NotNull CloudServiceManager getCloudServiceManager() {
    return CloudNetV2CloudServiceManager.INSTANCE;
  }
}
