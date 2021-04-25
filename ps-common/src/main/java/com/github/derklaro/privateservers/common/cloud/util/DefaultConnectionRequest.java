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
package com.github.derklaro.privateservers.common.cloud.util;

import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.api.cloud.util.ConnectionRequest;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@ToString
@EqualsAndHashCode
public abstract class DefaultConnectionRequest implements ConnectionRequest {

  private final CloudService targetService;
  private final UUID targetPlayer;

  public DefaultConnectionRequest(CloudService targetService, UUID targetPlayer) {
    this.targetService = targetService;
    this.targetPlayer = targetPlayer;
  }

  @Override
  public @NotNull CloudService getTargetService() {
    return this.targetService;
  }

  @Override
  public @NotNull UUID getTargetPlayer() {
    return this.targetPlayer;
  }
}
