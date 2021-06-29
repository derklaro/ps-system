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

package com.github.derklaro.privateservers.translation;

import com.github.derklaro.privateservers.api.translation.ComponentRenderer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.Function;

public final class BukkitComponentRenderer {

  private static final Function<Player, String> GET_LOCALE;

  static {
    Function<Player, String> localeGetter;
    try {
      // legacy bukkit
      Player.Spigot.class.getMethod("getLocale");
      localeGetter = player -> player.spigot().getLocale();
    } catch (ReflectiveOperationException exception) {
      // modern bukkit
      try {
        Method getLocale = Player.class.getMethod("getLocale");
        MethodHandle getterHandle = MethodHandles.publicLookup().unreflect(getLocale);

        localeGetter = player -> {
          try {
            return (String) getterHandle.invoke(player);
          } catch (Throwable throwable) {
            return null;
          }
        };
      } catch (ReflectiveOperationException e) {
        // no other method is working, use fallback locale
        localeGetter = player -> null;
      }
    }

    GET_LOCALE = localeGetter;
  }

  private BukkitComponentRenderer() {
    throw new UnsupportedOperationException();
  }

  public static void renderAndSend(@NotNull Player player, @NotNull Component component) {
    player.sendMessage(ComponentRenderer.renderToString(component, GET_LOCALE.apply(player)));
  }
}
