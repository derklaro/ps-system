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
package com.github.derklaro.privateservers.lobby.npc.database;

import com.github.juliarn.npc.NPC;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@ToString
@EqualsAndHashCode
public final class DatabaseNPCObject {

  private final double x;
  private final double y;
  private final double z;
  private final float yaw;
  private final float pitch;
  private final String world;
  private final String texturesProfileName;

  private transient Location location;

  private DatabaseNPCObject(double x, double y, double z, float yaw, float pitch, String world, String texturesProfileName) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
    this.world = world;
    this.texturesProfileName = texturesProfileName;
  }

  @NotNull
  public static DatabaseNPCObject fromLocation(@NotNull Location location, @NotNull String texturesProfileName) {
    return new DatabaseNPCObject(location.getX(), location.getY(), location.getZ(), location.getYaw(),
      location.getPitch(), location.getWorld().getName(), texturesProfileName);
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getZ() {
    return this.z;
  }

  public float getYaw() {
    return this.yaw;
  }

  public float getPitch() {
    return this.pitch;
  }

  public String getWorld() {
    return this.world;
  }

  public String getTexturesProfileName() {
    return this.texturesProfileName;
  }

  public Location getLocation() {
    if (this.location == null) {
      this.location = new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    return this.location;
  }

  public boolean matches(@NotNull NPC npc) {
    Location location = npc.getLocation();
    return this.getLocation().hashCode() == location.hashCode();
  }
}
