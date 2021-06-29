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

package com.github.derklaro.privateservers.api.cloud.connection;

import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a re-usable request of a player to join a specific cloud service.
 *
 * @see CloudService#createConnectionRequest(UUID)
 */
public interface ConnectionRequest {
  /**
   * Get the service the player will be connected to when fired.
   *
   * @return the service the player will be connected to.
   */
  @NotNull CloudService getTargetService();

  /**
   * Get the unique id of the player which should be sent when fired.
   *
   * @return the unique id of the player which should be sent.
   */
  @NotNull UUID getTargetPlayer();

  /**
   * Fires the request connecting the player to the target service if it still exists.
   */
  void fire();
}
