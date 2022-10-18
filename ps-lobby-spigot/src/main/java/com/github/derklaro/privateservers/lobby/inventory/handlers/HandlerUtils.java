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

package com.github.derklaro.privateservers.lobby.inventory.handlers;

import com.github.derklaro.privateservers.api.configuration.InventoryConfiguration;
import com.github.derklaro.privateservers.common.translation.Message;
import com.github.derklaro.privateservers.translation.BukkitComponentRenderer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class HandlerUtils {

  private HandlerUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean canUse(@NotNull Player player, @NotNull InventoryConfiguration.ItemLayout layout) {
    return layout.getUsePermission() == null || player.hasPermission(layout.getUsePermission());
  }

  public static void notifyNotAllowed(@NotNull Player player) {
    player.closeInventory();
    BukkitComponentRenderer.renderAndSend(player, Message.ITEM_USE_NO_PERMISSION.build());
  }
}
