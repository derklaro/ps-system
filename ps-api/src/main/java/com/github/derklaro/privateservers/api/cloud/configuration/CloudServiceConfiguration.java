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

  private final String initialGroup;
  private final String initialTemplate;
  private final String initialTemplateBackend;

  private boolean publicService;
  private boolean hasWhitelist;

  public CloudServiceConfiguration(boolean autoDeleteAfterOwnerLeave, boolean autoSaveBeforeStop,
                                   Collection<String> whitelistedPlayers, UUID ownerUniqueID, String ownerName,
                                   String initialGroup, String initialTemplate, String initialTemplateBackend,
                                   boolean publicService, boolean hasWhitelist) {
    this.autoDeleteAfterOwnerLeave = autoDeleteAfterOwnerLeave;
    this.autoSaveBeforeStop = autoSaveBeforeStop;
    this.whitelistedPlayers = whitelistedPlayers;
    this.ownerUniqueID = ownerUniqueID;
    this.ownerName = ownerName;
    this.initialGroup = initialGroup;
    this.initialTemplate = initialTemplate;
    this.initialTemplateBackend = initialTemplateBackend;
    this.publicService = publicService;
    this.hasWhitelist = hasWhitelist;
  }

  public boolean isAutoDeleteAfterOwnerLeave() {
    return autoDeleteAfterOwnerLeave;
  }

  public boolean isAutoSaveBeforeStop() {
    return autoSaveBeforeStop;
  }

  public Collection<String> getWhitelistedPlayers() {
    return whitelistedPlayers;
  }

  public UUID getOwnerUniqueID() {
    return ownerUniqueID;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public String getInitialGroup() {
    return initialGroup;
  }

  public String getInitialTemplate() {
    return initialTemplate;
  }

  public String getInitialTemplateBackend() {
    return initialTemplateBackend;
  }

  public boolean isPublicService() {
    return publicService;
  }

  public void setPublicService(boolean publicService) {
    this.publicService = publicService;
  }

  public boolean isHasWhitelist() {
    return hasWhitelist;
  }

  public void setHasWhitelist(boolean hasWhitelist) {
    this.hasWhitelist = hasWhitelist;
  }
}
