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

import net.jitse.npclib.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class DatabaseNPCObject {

    @NotNull
    public static DatabaseNPCObject fromLocation(@NotNull Location location, @NotNull String texturesProfileName) {
        return new DatabaseNPCObject(location.getX(), location.getY(), location.getZ(), location.getYaw(),
                location.getPitch(), location.getWorld().getName(), texturesProfileName);
    }

    private DatabaseNPCObject(double x, double y, double z, float yaw, float pitch, String world, String texturesProfileName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
        this.texturesProfileName = texturesProfileName;
    }

    private final double x;

    private final double y;

    private final double z;

    private final float yaw;

    private final float pitch;

    private final String world;

    private final String texturesProfileName;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getWorld() {
        return world;
    }

    public String getTexturesProfileName() {
        return texturesProfileName;
    }

    private transient volatile Location location;

    public Location getLocation() {
        if (location == null) {
            this.location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatabaseNPCObject)) return false;
        DatabaseNPCObject that = (DatabaseNPCObject) o;
        return Double.compare(that.getX(), getX()) == 0 &&
                Double.compare(that.getY(), getY()) == 0 &&
                Double.compare(that.getZ(), getZ()) == 0 &&
                Float.compare(that.getYaw(), getYaw()) == 0 &&
                Float.compare(that.getPitch(), getPitch()) == 0 &&
                Objects.equals(getWorld(), that.getWorld());
    }

    public boolean matches(@NotNull NPC npc) {
        Location location = npc.getLocation();
        return this.getLocation().hashCode() == location.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ(), getYaw(), getPitch(), getWorld());
    }
}
