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
package com.github.derklaro.privateservers.cloudnet.v3;

import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.module.annotation.Module;
import com.github.derklaro.privateservers.cloudnet.v3.cloud.CloudNetV3CloudSystem;
import com.github.derklaro.privateservers.cloudnet.v3.listeners.CloudServiceListener;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import org.jetbrains.annotations.NotNull;

@Module(
        id = "com.github.derklaro.privateservers.cloudnet.v3",
        displayName = "CloudNetV3PrivateServerModule",
        version = "1.1.0",
        description = "Module for private servers cloudnet v3 integration",
        authors = "derklaro"
)
public class CloudNetV3Module {

    public CloudNetV3Module(@NotNull Plugin plugin) {
        CloudSystem cloudSystem = new CloudNetV3CloudSystem();

        plugin.getCloudSystemDetector().registerCloudSystem(cloudSystem);
        CloudNetDriver.getInstance().getEventManager().registerListener(new CloudServiceListener(cloudSystem));
    }
}