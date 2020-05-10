/*
 * MIT License
 *
 * Copyright (c) 2020 Pasqual K. and contributors
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
package com.github.derklaro.privateservers.lobby.entity;

import com.github.derklaro.privateservers.PrivateServersSpigot;
import com.github.derklaro.privateservers.lobby.database.JsonNPCDatabase;
import com.github.derklaro.privateservers.lobby.profile.PlayerProfile;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NPCMap {

    private NPCMap() {
        throw new UnsupportedOperationException();
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(NPCMap::destroyAll));
    }

    private static final NPCLib NPC_LIB = new NPCLib(PrivateServersSpigot.getInstance());

    private static final Collection<NPC> NPCS = new CopyOnWriteArrayList<>();

    public static void createNpc(@NotNull Location location, @NotNull String texturesProfileName) {
        JsonNPCDatabase.addNpc(location, texturesProfileName);
        spawnNpc(location, texturesProfileName);
    }

    public static void spawnNpc(@NotNull Location location, @NotNull String texturesProfileName) {
        PlayerProfile playerProfile = new PlayerProfile(texturesProfileName);
        playerProfile.complete(true);

        NPC npc = NPC_LIB.createNPC(Collections.singletonList("§6§lPrivateServers"));
        npc.setLocation(location);
        playerProfile.getSkin().ifPresent(npc::setSkin);
        NPCS.add(npc.create());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            npc.show(onlinePlayer);
        }
    }

    public static void removeNpc(@NotNull NPC npc) {
        NPCS.remove(npc);
        JsonNPCDatabase.removeNPC(npc);

        npc.destroy();
    }

    public static void showNpcToPlayer(@NotNull Player player) {
        for (NPC npc : NPCS) {
            npc.show(player);
        }
    }

    public static void destroyAll() {
        for (NPC npc : NPCS) {
            npc.destroy();
        }

        NPCS.clear();
    }

    public static boolean isInternalNpc(@NotNull NPC npc) {
        for (NPC npc1 : NPCS) {
            if (npc1.getId().equals(npc.getId())) {
                return true;
            }
        }

        return false;
    }
}
