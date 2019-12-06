package de.derklaro.privateservers.mobs.config.plugins;

import java.util.Collection;

public final class InstallablePlugins {

    private Collection<Plugin> plugins;

    public InstallablePlugins(Collection<Plugin> plugins) {
        this.plugins = plugins;
    }

    public Collection<Plugin> getPlugins() {
        return this.plugins;
    }
}
