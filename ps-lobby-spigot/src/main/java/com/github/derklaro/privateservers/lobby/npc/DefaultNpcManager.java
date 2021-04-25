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
package com.github.derklaro.privateservers.lobby.npc;

import com.github.derklaro.privateservers.PrivateServersSpigot;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.profile.Profile;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class DefaultNpcManager implements NpcManager {

  private final Configuration configuration;
  private final NPCPool npcPool;

  public DefaultNpcManager(Configuration configuration) {
    this.configuration = configuration;
    this.npcPool = NPCPool.builder(PrivateServersSpigot.getInstance())
      .actionDistance(configuration.getNpcConfiguration().getActionDistance())
      .spawnDistance(configuration.getNpcConfiguration().getSpawnDistance())
      .tabListRemoveTicks(configuration.getNpcConfiguration().getTablistRemovalTicks())
      .build();
  }

  @Override
  public void createAndSpawnNpc(@NotNull Location location, @NotNull String texturePlayerName) {
    NPC.builder()
      .location(location)
      .lookAtPlayer(this.configuration.getNpcConfiguration().isLookAtPlayer())
      .imitatePlayer(this.configuration.getNpcConfiguration().isImitatePlayer())
      .profile(new Profile(texturePlayerName))
      .build(this.npcPool);
  }

  @Override
  public void removeNpc(@NotNull Location location) {
    for (NPC npc : this.npcPool.getNPCs()) {
      if (npc.getLocation().equals(location)) {
        this.npcPool.removeNPC(npc.getEntityId());
      }
    }
  }

  @Override
  public void removeAllNPCs() {
    for (NPC npc : this.npcPool.getNPCs()) {
      this.npcPool.removeNPC(npc.getEntityId());
    }
  }

  @Override
  public boolean isManagedNpc(int entityId) {
    return this.npcPool.getNpc(entityId).isPresent();
  }
}
