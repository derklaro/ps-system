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
package com.github.derklaro.privateservers.common.cloud;

import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class DefaultCloudService implements CloudService {

    public DefaultCloudService(String name, UUID uniqueID, CloudServiceConfiguration cloudServiceConfiguration) {
        this.name = name;
        this.uniqueID = uniqueID;
        this.cloudServiceConfiguration = cloudServiceConfiguration;
    }

    protected final String name;

    protected final UUID uniqueID;

    protected CloudServiceConfiguration cloudServiceConfiguration;

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull UUID getUniqueID() {
        return this.uniqueID;
    }

    @Override
    public @NotNull UUID getOwnerUniqueID() {
        return this.cloudServiceConfiguration.getOwnerUniqueID();
    }

    @Override
    public @NotNull String getOwnerName() {
        return this.cloudServiceConfiguration.getOwnerName();
    }

    @Override
    public @NotNull CloudServiceConfiguration getCloudServiceConfiguration() {
        return this.cloudServiceConfiguration;
    }

    @Override
    public void setCloudServiceConfiguration(@NotNull CloudServiceConfiguration cloudServiceConfiguration) {
        this.cloudServiceConfiguration = cloudServiceConfiguration;
    }

    @Override
    public int hashCode() {
        return this.getUniqueID().hashCode();
    }

    @Override
    public boolean equals(@NotNull Object obj) {
        if (!(obj instanceof CloudService)) {
            return false;
        }

        return ((CloudService) obj).getUniqueID().equals(this.getUniqueID());
    }

}
