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

/**
 * Represents a platform specific manager to run tasks.
 */
public interface TaskManager {
  /**
   * Schedules a task on the main minecraft thread. If the server software does not support these operations, the
   * runnable will be called on the current thread.
   *
   * @param runnable the runnable to run.
   * @return a future completed after the task was called.
   */
  @NotNull
  CompletableFuture<Void> scheduleSyncTask(@NotNull Runnable runnable);

  /**
   * Schedules a task on the main minecraft thread. If the server software does not support these operations, the
   * runnable will be called on the current thread.
   *
   * @param runnable the runnable to run.
   * @param delay the delay before the run.
   * @return a future completed after the task was called.
   */
  @NotNull
  CompletableFuture<Void> scheduleSyncTask(@NotNull Runnable runnable, long delay);

  /**
   * Calls the callable on the main minecraft thread. If the server software does not support these operations, the
   * runnable will be called on the current thread.
   *
   * @param callable the callable to call.
   * @param <T> the type of the return value of the callable.
   * @return a future completed with the result of the callable.
   */
  @NotNull <T> CompletableFuture<T> callSync(@NotNull Callable<T> callable);

  /**
   * Schedules a repeating task on the main minecraft thread. If the server software does not support these operations, the
   * runnable will be called on an async thread.
   *
   * @param runnable the runnable to run.
   * @param repeatTime the delay between the repeats.
   * @param timeUnit the time unit of the delay.
   */
  void scheduleSyncRepeatingTask(@NotNull Runnable runnable, long repeatTime, @NotNull TimeUnit timeUnit);

  /**
   * Schedules a repeating task on the main minecraft thread. If the server software does not support these operations, the
   * runnable will be called on an async thread.
   *
   * @param runnable the runnable to run.
   * @param delay the delay between the repeats.
   * @param repeats the amount of repeats.
   */
  void scheduleSyncRepeatingTask(@NotNull Runnable runnable, long delay, long repeats);

  /**
   * Schedules a task on an extra async thread.
   *
   * @param runnable the runnable to run.
   * @return a future completed when the task was called.
   */
  @NotNull
  CompletableFuture<Void> scheduleAsyncTask(@NotNull Runnable runnable);

  /**
   * Schedules a task on an extra async thread.
   *
   * @param runnable the runnable to run.
   * @param delay the delay before the execution.
   * @return a future completed when the task was called.
   */
  @NotNull
  CompletableFuture<Void> scheduleAsyncTask(@NotNull Runnable runnable, long delay);

  /**
   * Calls a callable on an extra async thread.
   *
   * @param callable the callable to call.
   * @param <T> the type of the return value of the callable.
   * @return a future completed with the result of the callable.
   */
  @NotNull <T> CompletableFuture<T> callAsync(@NotNull Callable<T> callable);

  /**
   * Schedules an async repeating task on an extra async thread.
   *
   * @param runnable the runnable to run.
   * @param repeatTime the delay between the repeats.
   * @param timeUnit the time unit of the delay.
   */
  void scheduleAsyncRepeatingTask(@NotNull Runnable runnable, long repeatTime, @NotNull TimeUnit timeUnit);

  /**
   * Schedules an async repeating task on an extra async thread.
   *
   * @param runnable the runnable to run.
   * @param delay the delay between the repeats.
   * @param repeats the amount of repeats.
   */
  void scheduleAsyncRepeatingTask(@NotNull Runnable runnable, long delay, long repeats);
}
