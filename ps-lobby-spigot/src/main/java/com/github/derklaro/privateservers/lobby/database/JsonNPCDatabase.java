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
package com.github.derklaro.privateservers.lobby.database;

import com.github.derklaro.privateservers.lobby.entity.NPCMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.jitse.npclib.api.NPC;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public final class JsonNPCDatabase {

    private JsonNPCDatabase() {
        throw new UnsupportedOperationException();
    }

    private static final ThreadLocal<Gson> GSON = ThreadLocal.withInitial(
            () -> new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create()
    );

    private static final Path DB_PATH = Paths.get("plugins/ps/db/db_npc.json");

    private static final Collection<DatabaseNPCObject> DATABASE_NPC_OBJECTS = new CopyOnWriteArrayList<>();

    public static void loadAndSpawn() {
        if (Files.notExists(DB_PATH)) {
            try {
                if (DB_PATH.getParent() != null) {
                    Files.createDirectories(DB_PATH.getParent());
                }
            } catch (final IOException exception) {
                exception.printStackTrace();
            }

            store();
            return;
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(DB_PATH), StandardCharsets.UTF_8)) {
            JsonElement element = JsonParser.parseReader(inputStreamReader);
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                DATABASE_NPC_OBJECTS.addAll(GSON.get().fromJson(jsonObject.get("npcs"), TypeToken.getParameterized(Collection.class, DatabaseNPCObject.class).getType()));
            } else {
                store();
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        for (DatabaseNPCObject databaseNpcObject : DATABASE_NPC_OBJECTS) {
            NPCMap.createNpc(databaseNpcObject.getLocation(), databaseNpcObject.getTexturesProfileName());
        }
    }

    public static void addNpc(@NotNull Location location, @NotNull String texturesProfileName) {
        DATABASE_NPC_OBJECTS.add(DatabaseNPCObject.fromLocation(location, texturesProfileName));
        store();
    }

    public static void removeNPC(@NotNull NPC npc) {
        DATABASE_NPC_OBJECTS.removeIf(object -> object.matches(npc));
        store();
    }

    private static void store() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("npcs", GSON.get().toJsonTree(DATABASE_NPC_OBJECTS));

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(DB_PATH), StandardCharsets.UTF_8)) {
            Files.deleteIfExists(DB_PATH);
            Files.createFile(DB_PATH);

            GSON.get().toJson(jsonObject, outputStreamWriter);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
