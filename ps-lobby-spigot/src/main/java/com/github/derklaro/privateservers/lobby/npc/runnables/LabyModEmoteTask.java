/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 - 2021 Pasqual Koschmieder and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.derklaro.privateservers.lobby.npc.runnables;

import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.NpcConfiguration;
import com.github.derklaro.privateservers.lobby.npc.DefaultNpcManager;
import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.modifier.LabyModModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LabyModEmoteTask {

  private static final Random RANDOM = new Random();

  private final DefaultNpcManager npcManager;

  public LabyModEmoteTask(@NotNull Configuration configuration, @NotNull Plugin plugin, @NotNull DefaultNpcManager npcManager) {
    this.npcManager = npcManager;

    NpcConfiguration.LabyModConfiguration labyModConfiguration = configuration.getNpcConfiguration().getLabyModConfiguration();
    if (labyModConfiguration.getEmoteDelayTicks() > 0 && labyModConfiguration.getEmoteIds().length > 0) {
      this.schedule(labyModConfiguration, plugin);
    }
  }

  protected void schedule(@NotNull NpcConfiguration.LabyModConfiguration configuration, @NotNull Plugin plugin) {
    int delayTicks = configuration.getEmoteDelayTicks();
    int emoteId = configuration.getEmoteIds()[RANDOM.nextInt(configuration.getEmoteIds().length)];

    plugin.getTaskManager().scheduleAsyncTask(() -> {
      for (NPC npc : this.npcManager.getNpcPool().getNPCs()) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          if (npc.isShownFor(onlinePlayer)) {
            npc.labymod()
              .queue(LabyModModifier.LabyModAction.EMOTE, emoteId)
              .send(onlinePlayer);
          }
        }
      }

      this.schedule(configuration, plugin);
    }, delayTicks);
  }
}
