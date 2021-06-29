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

package com.github.derklaro.privateservers.api.cloud;

import com.github.derklaro.privateservers.api.cloud.exception.CloudSystemAlreadyDetectedException;
import com.github.derklaro.privateservers.api.cloud.exception.CloudSystemAlreadyRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents a provider and detector for cloud systems.
 */
public interface CloudDetector {
  /**
   * Registers a new cloud system for detection.
   *
   * @param requiredClass the class which is required to run the cloud system.
   * @param factory the factory to create a new instance of the cloud system.
   * @throws CloudSystemAlreadyRegisteredException thrown if the cloud system is already registered.
   */
  void registerCloudSystem(@NotNull String requiredClass, @NotNull Supplier<CloudSystem> factory) throws CloudSystemAlreadyRegisteredException;

  /**
   * Detects the best cloud system which can currently be ran.
   *
   * @throws CloudSystemAlreadyDetectedException thrown if a cloud system is already detected.
   */
  void detectCloudSystem() throws CloudSystemAlreadyDetectedException;

  /**
   * Get if a cloud system is already detected.
   *
   * @return if a cloud system is already detected.
   */
  boolean isCloudSystemDetected();

  /**
   * Get the detected cloud system if one got detected.
   *
   * @return the detected cloud system if one got detected.
   */
  @NotNull Optional<CloudSystem> getDetectedCloudSystem();
}
