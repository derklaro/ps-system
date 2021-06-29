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

package com.github.derklaro.privateservers.api.task;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface TaskManager {

  @NotNull
  CompletableFuture<Void> scheduleSyncTask(@NotNull Runnable runnable);

  @NotNull
  CompletableFuture<Void> scheduleSyncTask(@NotNull Runnable runnable, long delay);

  @NotNull <T> CompletableFuture<T> callSync(@NotNull Callable<T> callable);

  void scheduleSyncRepeatingTask(@NotNull Runnable runnable, long repeatTime, @NotNull TimeUnit timeUnit);

  void scheduleSyncRepeatingTask(@NotNull Runnable runnable, long delay, long repeats);

  @NotNull
  CompletableFuture<Void> scheduleAsyncTask(@NotNull Runnable runnable);

  @NotNull
  CompletableFuture<Void> scheduleAsyncTask(@NotNull Runnable runnable, long delay);

  @NotNull <T> CompletableFuture<T> callAsync(@NotNull Callable<T> callable);

  void scheduleAsyncRepeatingTask(@NotNull Runnable runnable, long repeatTime, @NotNull TimeUnit timeUnit);

  void scheduleAsyncRepeatingTask(@NotNull Runnable runnable, long delay, long repeats);
}
