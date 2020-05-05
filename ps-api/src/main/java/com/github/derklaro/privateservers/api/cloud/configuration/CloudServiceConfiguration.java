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
package com.github.derklaro.privateservers.api.cloud.configuration;

import java.util.UUID;

public class CloudServiceConfiguration {

    public CloudServiceConfiguration(boolean publicService, boolean hasWhitelist, boolean autoDeleteAfterOwnerLeave, boolean autoSaveBeforeStop, UUID ownerUniqueID, String ownerName) {
        this.publicService = publicService;
        this.hasWhitelist = hasWhitelist;
        this.autoDeleteAfterOwnerLeave = autoDeleteAfterOwnerLeave;
        this.autoSaveBeforeStop = autoSaveBeforeStop;
        this.ownerUniqueID = ownerUniqueID;
        this.ownerName = ownerName;
    }

    private boolean publicService;

    private boolean hasWhitelist;

    private final boolean autoDeleteAfterOwnerLeave;

    private final boolean autoSaveBeforeStop;

    private final UUID ownerUniqueID;

    private final String ownerName;

    public boolean isPublicService() {
        return publicService;
    }

    public boolean isHasWhitelist() {
        return hasWhitelist;
    }

    public boolean isAutoDeleteAfterOwnerLeave() {
        return autoDeleteAfterOwnerLeave;
    }

    public boolean isAutoSaveBeforeStop() {
        return autoSaveBeforeStop;
    }

    public UUID getOwnerUniqueID() {
        return ownerUniqueID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setPublicService(boolean publicService) {
        this.publicService = publicService;
    }

    public void setHasWhitelist(boolean hasWhitelist) {
        this.hasWhitelist = hasWhitelist;
    }
}
