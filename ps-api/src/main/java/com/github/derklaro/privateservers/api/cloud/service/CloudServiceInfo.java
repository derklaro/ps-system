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

package com.github.derklaro.privateservers.api.cloud.service;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * Holds server specific information about a cloud service.
 */
public interface CloudServiceInfo {

  /**
   * Get the address the service is bound to.
   *
   * @return the bound address of the service.
   */
  @NotNull InetSocketAddress getAddress();

  /**
   * Get the motd (message of the day) of the service.
   *
   * @return the motd of the service.
   */
  @NotNull String getMessageOfTheDay();

  /**
   * Get the state of the service.
   *
   * @return the state of the service.
   */
  @NotNull String getState();

  /**
   * Get the amount of player currently connected to the service.
   *
   * @return the amount of players connected to the service.
   */
  int getOnlineCount();

  /**
   * Get the maximum amount of players that can connect to the service.
   *
   * @return the maximum player count of the service.
   */
  int getMaximumPlayerCount();

  /**
   * Get the maximum heap memory size the service can use.
   *
   * @return the maximum heap memory of the service.
   */
  int getHeapMemory();
}
