package de.derklaro.privateservers.mobs.config;

import java.util.UUID;

public final class Mob {

    private UUID uniqueID;

    private String entityClassName;

    private String name;

    private String displayName;

    private MobPosition mobPosition;

    public Mob(UUID uniqueID, String entityClassName, String name, String displayName, MobPosition mobPosition) {
        this.uniqueID = uniqueID;
        this.entityClassName = entityClassName;
        this.name = name;
        this.displayName = displayName;
        this.mobPosition = mobPosition;
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public String getEntityClassName() {
        return this.entityClassName;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public MobPosition getMobPosition() {
        return this.mobPosition;
    }

    public static class MobPosition {
        private String world;
        private double x, y, z, yaw, pitch;

        public MobPosition(String world, double x, double y, double z, double yaw, double pitch) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        @Override
        public String toString() {
            return "world: " + world + " x: " + x + " y: " + y + " z: " + z;
        }

        public String getWorld() {
            return this.world;
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

        public double getYaw() {
            return this.yaw;
        }

        public double getPitch() {
            return this.pitch;
        }
    }
}
