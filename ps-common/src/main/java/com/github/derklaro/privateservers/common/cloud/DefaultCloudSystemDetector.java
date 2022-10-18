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

package com.github.derklaro.privateservers.common.cloud;

import com.github.derklaro.privateservers.api.cloud.CloudDetector;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.cloud.exception.CloudSystemAlreadyDetectedException;
import com.github.derklaro.privateservers.api.cloud.exception.CloudSystemAlreadyRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class DefaultCloudSystemDetector implements CloudDetector {

  public static final CloudDetector DEFAULT_INSTANCE = new DefaultCloudSystemDetector();

  private final AtomicReference<CloudSystem> detectedCloudSystem = new AtomicReference<>();
  private final Map<String, Supplier<CloudSystem>> cloudSystemFactories = new ConcurrentHashMap<>();

  private DefaultCloudSystemDetector() {
  }

  @Override
  public void registerCloudSystem(@NotNull String requiredClass, @NotNull Supplier<CloudSystem> factory) throws CloudSystemAlreadyRegisteredException {
    if (this.cloudSystemFactories.putIfAbsent(requiredClass, factory) != null) {
      throw new CloudSystemAlreadyRegisteredException(requiredClass, factory);
    }
  }

  @Override
  public void detectCloudSystem() throws CloudSystemAlreadyDetectedException {
    if (this.isCloudSystemDetected()) {
      throw new CloudSystemAlreadyDetectedException(this.detectedCloudSystem.get());
    }

    for (Map.Entry<String, Supplier<CloudSystem>> entry : this.cloudSystemFactories.entrySet()) {
      try {
        Class.forName(entry.getKey());
        // class is present, we can use this as the running system
        this.detectedCloudSystem.set(entry.getValue().get());
        break;
      } catch (ClassNotFoundException ignored) {
      }
    }
  }

  @Override
  public boolean isCloudSystemDetected() {
    return this.detectedCloudSystem.get() != null;
  }

  @Override
  public @NotNull Optional<CloudSystem> getDetectedCloudSystem() {
    return Optional.ofNullable(this.detectedCloudSystem.get());
  }
}
