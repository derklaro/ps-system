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

package com.github.derklaro.privateservers;

import com.github.derklaro.privateservers.api.task.TaskManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class SpigotTaskManager implements TaskManager {

  public static final SpigotTaskManager INSTANCE = new SpigotTaskManager();

  private SpigotTaskManager() {
  }

  @Override
  public @NotNull CompletableFuture<Void> scheduleSyncTask(@NotNull Runnable runnable) {
    return this.scheduleSyncTask(runnable, 0);
  }

  @Override
  public @NotNull CompletableFuture<Void> scheduleSyncTask(@NotNull Runnable runnable, long delay) {
    CompletableFuture<Void> result = new CompletableFuture<>();

    if (Bukkit.isPrimaryThread()) {
      runnable.run();
      result.complete(null);
    } else {
      Bukkit.getScheduler().runTaskLater(PrivateServersSpigot.getInstance(), () -> {
        runnable.run();
        result.complete(null);
      }, delay);
    }

    return result;
  }

  @Override
  public @NotNull <T> CompletableFuture<T> callSync(@NotNull Callable<T> callable) {
    CompletableFuture<T> future = new CompletableFuture<>();

    if (Bukkit.isPrimaryThread()) {
      try {
        future.complete(callable.call());
      } catch (Exception exception) {
        future.completeExceptionally(exception);
      }
    } else {
      Bukkit.getScheduler().runTask(PrivateServersSpigot.getInstance(), () -> {
        try {
          future.complete(callable.call());
        } catch (Exception exception) {
          future.completeExceptionally(exception);
        }
      });
    }

    return future;
  }

  @Override
  public void scheduleSyncRepeatingTask(@NotNull Runnable runnable, long repeatTime, @NotNull TimeUnit timeUnit) {
    Bukkit.getScheduler().runTaskTimer(PrivateServersSpigot.getInstance(), runnable, 0, timeUnit.toSeconds(repeatTime) / 20);
  }

  @Override
  public void scheduleSyncRepeatingTask(@NotNull Runnable runnable, long delay, long repeats) {
    Bukkit.getScheduler().runTaskTimer(PrivateServersSpigot.getInstance(), runnable, delay, repeats);
  }

  @Override
  public @NotNull CompletableFuture<Void> scheduleAsyncTask(@NotNull Runnable runnable) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    Bukkit.getScheduler().runTaskAsynchronously(PrivateServersSpigot.getInstance(), () -> {
      runnable.run();
      future.complete(null);
    });
    return future;
  }

  @Override
  public @NotNull CompletableFuture<Void> scheduleAsyncTask(@NotNull Runnable runnable, long delay) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    Bukkit.getScheduler().runTaskLaterAsynchronously(PrivateServersSpigot.getInstance(), () -> {
      runnable.run();
      future.complete(null);
    }, delay);
    return future;
  }

  @Override
  public @NotNull <T> CompletableFuture<T> callAsync(@NotNull Callable<T> callable) {
    CompletableFuture<T> future = new CompletableFuture<>();
    Bukkit.getScheduler().runTaskAsynchronously(PrivateServersSpigot.getInstance(), () -> {
      try {
        future.complete(callable.call());
      } catch (Exception exception) {
        future.completeExceptionally(exception);
      }
    });
    return future;
  }

  @Override
  public void scheduleAsyncRepeatingTask(@NotNull Runnable runnable, long repeatTime, @NotNull TimeUnit timeUnit) {
    Bukkit.getScheduler().runTaskTimerAsynchronously(PrivateServersSpigot.getInstance(), runnable, 0, timeUnit.toSeconds(repeatTime) / 20);
  }

  @Override
  public void scheduleAsyncRepeatingTask(@NotNull Runnable runnable, long delay, long repeats) {
    Bukkit.getScheduler().runTaskTimerAsynchronously(PrivateServersSpigot.getInstance(), runnable, delay, repeats);
  }
}
