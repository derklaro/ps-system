/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2022 Pasqual K. and contributors
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

package com.github.derklaro.privateservers.cloudnet.v2;

import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.module.annotation.Module;
import com.github.derklaro.privateservers.cloudnet.v2.cloud.CloudNetV2CloudSystem;
import org.jetbrains.annotations.NotNull;

@Module(
  id = "com.github.derklaro.privateservers.cloudnet.v2",
  displayName = "CloudNetV2PrivateServerModule",
  version = "1.1.0",
  description = "Module for private servers cloudnet v2.2 integration",
  authors = "derklaro"
)
public class CloudNetV2Module {

  private static final String IDENTITY_CLASS = "de.dytanic.cloudnet.api.builders.ApiServerProcessBuilder";

  public CloudNetV2Module(@NotNull Plugin plugin) {
    plugin.getCloudSystemDetector().registerCloudSystem(IDENTITY_CLASS, CloudNetV2CloudSystem::new);
  }
}
