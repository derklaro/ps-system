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
package com.github.derklaro.privateservers.api.cloud.configuration;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@ToString
@EqualsAndHashCode
public class CloudServiceConfiguration {

  private final boolean autoDeleteAfterOwnerLeave;
  private final boolean autoSaveBeforeStop;
  private final Collection<String> whitelistedPlayers;
  private final UUID ownerUniqueID;
  private final String ownerName;
  private boolean publicService;
  private boolean hasWhitelist;

  public CloudServiceConfiguration(boolean publicService, boolean hasWhitelist, boolean autoDeleteAfterOwnerLeave, boolean autoSaveBeforeStop, UUID ownerUniqueID, String ownerName) {
    this.publicService = publicService;
    this.hasWhitelist = hasWhitelist;
    this.autoDeleteAfterOwnerLeave = autoDeleteAfterOwnerLeave;
    this.autoSaveBeforeStop = autoSaveBeforeStop;
    this.whitelistedPlayers = new ArrayList<>();
    this.ownerUniqueID = ownerUniqueID;
    this.ownerName = ownerName;
  }

  public boolean isPublicService() {
    return this.publicService;
  }

  public void setPublicService(boolean publicService) {
    this.publicService = publicService;
  }

  public boolean isHasWhitelist() {
    return this.hasWhitelist;
  }

  public void setHasWhitelist(boolean hasWhitelist) {
    this.hasWhitelist = hasWhitelist;
  }

  public boolean isAutoDeleteAfterOwnerLeave() {
    return this.autoDeleteAfterOwnerLeave;
  }

  public boolean isAutoSaveBeforeStop() {
    return this.autoSaveBeforeStop;
  }

  @NotNull
  public Collection<String> getWhitelistedPlayers() {
    return this.whitelistedPlayers;
  }

  public UUID getOwnerUniqueId() {
    return this.ownerUniqueID;
  }

  public String getOwnerName() {
    return this.ownerName;
  }
}
