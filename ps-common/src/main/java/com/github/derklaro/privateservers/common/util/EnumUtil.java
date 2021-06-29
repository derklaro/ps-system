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

package com.github.derklaro.privateservers.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class EnumUtil {

  private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> CACHE = new ConcurrentHashMap<>();

  private EnumUtil() {
    throw new UnsupportedOperationException();
  }

  public static <T extends Enum<T>> T findEnumField(@NotNull Class<T> enumClass, @NotNull String field, @Nullable T defaultField) {
    return findEnumField(enumClass, field).orElse(defaultField);
  }

  private static <T extends Enum<T>> Optional<T> findEnumField(Class<T> enumClass, String field) {
    Map<String, WeakReference<? extends Enum<?>>> cached = CACHE.computeIfAbsent(enumClass, aClass -> cache(enumClass));
    WeakReference<? extends Enum<?>> reference = cached.get(field);
    return reference == null ? Optional.empty() : Optional.ofNullable(enumClass.cast(reference.get()));
  }

  @NotNull
  private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> cache(Class<T> enumClass) {
    Map<String, WeakReference<? extends Enum<?>>> out = new HashMap<>();
    try {
      Collection<T> instance = EnumSet.allOf(enumClass);
      instance.forEach(t -> out.put(t.name(), new WeakReference<>(t)));
    } catch (Throwable ignored) {
    }

    return out;
  }
}
