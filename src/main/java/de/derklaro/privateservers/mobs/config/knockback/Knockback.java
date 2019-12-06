package de.derklaro.privateservers.mobs.config.knockback;

public final class Knockback {

    private boolean enabled;

    private String bypassPermission;

    private double distance;

    private double strength;

    public Knockback(boolean enabled, String bypassPermission, double distance, double strength) {
        this.enabled = enabled;
        this.bypassPermission = bypassPermission;
        this.distance = distance;
        this.strength = strength;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getBypassPermission() {
        return this.bypassPermission;
    }

    public double getDistance() {
        return this.distance;
    }

    public double getStrength() {
        return this.strength;
    }
}
