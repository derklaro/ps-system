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

package com.github.derklaro.privateservers.lobby.npc.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public final class JsonNpcDatabase implements NpcDatabase {

  private static final Path DB_PATH = Paths.get("plugins/ps/db/db_npc.json");
  private static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();
  private static final Type COLLECTION_NPC_TYPE = TypeToken.getParameterized(Collection.class, DatabaseNPCObject.class).getType();

  private final Collection<DatabaseNPCObject> npcObjects = new CopyOnWriteArrayList<>();

  @Override
  public void initialize() {
    if (Files.notExists(DB_PATH)) {
      try {
        if (DB_PATH.getParent() != null) {
          Files.createDirectories(DB_PATH.getParent());
        }
      } catch (IOException exception) {
        exception.printStackTrace();
      }

      this.store();
      return;
    }

    try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(DB_PATH), StandardCharsets.UTF_8)) {
      JsonElement element = JsonParser.parseReader(inputStreamReader);
      if (element.isJsonObject()) {
        JsonObject jsonObject = element.getAsJsonObject();
        this.npcObjects.addAll(GSON.fromJson(jsonObject.get("npcs"), COLLECTION_NPC_TYPE));
      } else {
        this.store();
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void addNpc(@NotNull DatabaseNPCObject object) {
    this.npcObjects.add(object);
    this.store();
  }

  @Override
  public void removeNpc(@NotNull DatabaseNPCObject object) {
    this.npcObjects.remove(object);
    this.store();
  }

  @Override
  public void removeNpc(@NotNull Location location) {
    for (DatabaseNPCObject npcObject : this.npcObjects) {
      if (npcObject.getLocation().equals(location)) {
        this.removeNpc(npcObject);
        break;
      }
    }
  }

  @Override
  public void removeAllNPCs() {
    this.npcObjects.clear();
    this.store();
  }

  @Override
  public @NotNull @UnmodifiableView Collection<DatabaseNPCObject> getAllNPCs() {
    return Collections.unmodifiableCollection(this.npcObjects);
  }

  protected void store() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("npcs", GSON.toJsonTree(this.npcObjects));

    try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(DB_PATH), StandardCharsets.UTF_8)) {
      Files.deleteIfExists(DB_PATH);
      Files.createFile(DB_PATH);

      GSON.toJson(jsonObject, outputStreamWriter);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
