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

package com.github.derklaro.privateservers.api.cloud.service;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a common listener for service state changes.
 */
public interface ServiceListener {
  /**
   * Called when a new service registers into the system.
   *
   * @param cloudService the cloud service registered.
   */
  void handleServiceRegister(@NotNull CloudService cloudService);

  /**
   * Called when any change happens to a cloud service.
   *
   * @param cloudService the changed service.
   */
  void handleServerUpdate(@NotNull CloudService cloudService);

  /**
   * Called when a service unregisters from the system.
   *
   * @param cloudService the unregistered service.
   */
  void handleServiceUnregister(@NotNull CloudService cloudService);
}
