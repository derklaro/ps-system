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

package com.github.derklaro.privateservers.api.cloud.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a data class which holds all configuration options for a private server.
 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class CloudServiceConfiguration {

  private final boolean autoDeleteAfterOwnerLeave;
  private final boolean autoSaveBeforeStop;
  private final int maxIdleSeconds;

  private final Collection<String> whitelistedPlayers;
  private final UUID ownerUniqueId;
  private final String ownerName;

  private final String initialGroup;
  private final String initialTemplate;
  private final String initialTemplateBackend;

  private boolean publicService;
  private boolean whitelist;

  public int getMaxIdleSeconds() {
    return this.maxIdleSeconds > 0 ? this.maxIdleSeconds : 30;
  }
}
