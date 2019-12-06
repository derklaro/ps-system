package de.derklaro.privateservers.utility.configuration;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.derklaro.privateservers.utility.Constants;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Configuration {
    /**
     * The json object of the configuration
     */
    protected JsonObject jsonObject;

    /**
     * Creates a new configuration
     */
    public Configuration() {
        this.jsonObject = new JsonObject();
    }

    /**
     * Creates a new configuration
     *
     * @param jsonObject The json object of an existing configuration
     */
    public Configuration(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Adds an object property to the config
     *
     * @param key   The key of the item
     * @param value The string as value
     * @return The config class instance
     */
    public Configuration addValue(String key, Object value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.add(key, Constants.GSON.toJsonTree(value));
        return this;
    }

    /**
     * Gets a object value out of the config
     *
     * @param key  The key of the config value
     * @param type The defining type of the containing object
     * @return The config class instance
     */
    public <T> T getValue(String key, Type type) {
        return jsonObject.has(key) ? Constants.GSON.fromJson(jsonObject.get(key), type) : null;
    }

    /**
     * Gets a object value out of the config
     *
     * @param key       The key of the config value
     * @param typeToken The defining type token of the containing object
     * @return The config class instance
     */
    public <T> T getValue(String key, TypeToken<T> typeToken) {
        return getValue(key, typeToken.getType());
    }

    /**
     * Saves the file to a specific path
     *
     * @param path The path as file where the file should be saved to
     */
    private void write(File path) {
        if (path.exists()) {
            Preconditions.checkArgument(path.delete());
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
            Constants.GSON.toJson(jsonObject, (writer));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves the file to a specific path
     *
     * @param path The path where the file should be saved to
     */
    public void write(Path path) {
        this.write(path.toFile());
    }

    /**
     * Saves the file to a specific path
     *
     * @param path The path as string where the file should be saved to
     */
    public void write(String path) {
        this.write(Paths.get(path));
    }

    /**
     * Reads a config from the given path as file
     *
     * @param path The path where the file is saved
     * @return The configuration which was loaded or an empty
     * configuration if the file wasn't found or an error occurred
     */
    public static Configuration parse(File path) {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(path.toPath()), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return new Configuration(Constants.PARSER.parse(bufferedReader).getAsJsonObject());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        return new Configuration();
    }

    /**
     * Reads a config from the given path
     *
     * @param path The path where the file is saved
     * @return The configuration which was loaded or an empty
     * configuration if the file wasn't found or an error occurred
     */
    public static Configuration parse(Path path) {
        return parse(path.toFile());
    }

    /**
     * Reads a config from the given path as string
     *
     * @param path The path where the file is saved
     * @return The configuration which was loaded or an empty
     * configuration if the file wasn't found or an error occurred
     */
    public static Configuration parse(String path) {
        return parse(Paths.get(path));
    }
}
