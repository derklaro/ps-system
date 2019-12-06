package de.derklaro.privateservers.mobs.config.plugins;

public final class Plugin {

    private String name;

    private String displayName;

    private String downloadUrl;

    public Plugin(String name, String displayName, String downloadUrl) {
        this.name = name;
        this.displayName = displayName;
        this.downloadUrl = downloadUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }
}
