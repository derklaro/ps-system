package de.derklaro.privateservers.plugins;

import com.google.common.base.Preconditions;
import de.derklaro.privateservers.PrivateServers;
import de.derklaro.privateservers.mobs.config.plugins.Plugin;
import de.derklaro.privateservers.utility.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

final class PluginInstaller {

    private PluginInstaller() {
        throw new UnsupportedOperationException();
    }

    private static void install(Plugin plugin) {
        try {
            if (!Files.exists(Paths.get("plugins/" + plugin.getName().toLowerCase() + ".jar")))
                download(plugin, "plugins/" + plugin.getName().toLowerCase() + ".jar");

            File file = new File("plugins/" + plugin.getName().toLowerCase() + ".jar");
            org.bukkit.plugin.Plugin toEnable = Bukkit.getServer().getPluginManager().loadPlugin(file);
            Bukkit.getServer().getPluginManager().enablePlugin(toEnable);
        } catch (final InvalidPluginException | InvalidDescriptionException ex) {
            ex.printStackTrace();
        }
    }

    static boolean install(String plugin) {
        Plugin plugin1 = PrivateServers.getInstance().getCloudSystem().getPlugin(plugin);
        if (plugin1 == null)
            return false;

        org.bukkit.plugin.Plugin plugin2 = Bukkit.getServer().getPluginManager().getPlugin(plugin1.getName());
        if (Bukkit.getServer().getPluginManager().isPluginEnabled(plugin2))
            return false;

        install(plugin1);
        return true;
    }

    static boolean uninstall(String plugin) {
        Plugin plugin1 = PrivateServers.getInstance().getCloudSystem().getPlugin(plugin);
        if (plugin1 == null)
            return false;

        org.bukkit.plugin.Plugin plugin2 = CollectionUtils.filter(Bukkit.getServer().getPluginManager().getPlugins(), e -> e.getName().equalsIgnoreCase(plugin1.getName()));
        if (plugin2 == null || !Bukkit.getPluginManager().isPluginEnabled(plugin2))
            return false;

        Bukkit.getServer().getPluginManager().disablePlugin(plugin2);
        Preconditions.checkArgument(new File("plugins/" + plugin1.getName().toLowerCase() + ".jar").delete());
        return true;
    }

    private static void download(Plugin plugin, String to) {
        try {
            URLConnection urlConnection = new URL(plugin.getDownloadUrl()).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setConnectTimeout(1000);
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            try (InputStream inputStream = urlConnection.getInputStream()) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }

            ((HttpURLConnection) urlConnection).disconnect();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
