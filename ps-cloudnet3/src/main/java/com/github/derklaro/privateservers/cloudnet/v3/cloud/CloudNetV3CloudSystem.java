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

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.cloudnet.v3.listeners.CloudNetV3ServiceListener;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import org.jetbrains.annotations.NotNull;

public class CloudNetV3CloudSystem implements CloudSystem {

  public CloudNetV3CloudSystem() {
    CloudNetDriver.getInstance().getEventManager().registerListener(new CloudNetV3ServiceListener(this));
  }

  @Override
  public @NotNull String getIdentifierClass() {
    return "de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty";
  }

  @Override
  public @NotNull String getName() {
    return "CloudNETV3";
  }

  @Override
  public @NotNull CloudServiceManager getCloudServiceManager() {
    return CloudNetV3CloudServiceManager.INSTANCE;
  }
}
