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
package com.github.derklaro.privateservers.lobby.profile;

import com.github.derklaro.requestbuilder.RequestBuilder;
import com.github.derklaro.requestbuilder.method.RequestMethod;
import com.github.derklaro.requestbuilder.result.RequestResult;
import com.github.derklaro.requestbuilder.result.http.StatusCode;
import com.github.derklaro.requestbuilder.types.MimeTypes;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.jitse.npclib.api.skin.Skin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class PlayerProfile {

    private static final ThreadLocal<Gson> GSON = ThreadLocal.withInitial(
            () -> new GsonBuilder().serializeNulls().create()
    );

    private static final String UUID_REQUEST_URL = "https://api.mojang.com/users/profiles/minecraft/%s";

    private static final String TEXTURES_REQUEST_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=%b";

    private static final Pattern UNIQUE_ID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    private static final Type PROPERTY_LIST_TYPE = new TypeToken<Collection<Property>>() {
    }.getType();

    private UUID uniqueId;

    private String name;

    private Collection<Property> properties;

    public PlayerProfile(@NotNull UUID uniqueId) {
        this(uniqueId, null);
    }

    public PlayerProfile(@NotNull UUID uniqueId, Collection<Property> properties) {
        this(uniqueId, null, properties);
    }

    public PlayerProfile(@NotNull String name) {
        this(name, null);
    }

    public PlayerProfile(@NotNull String name, Collection<Property> properties) {
        this(null, name, properties);
    }

    public PlayerProfile(UUID uniqueId, String name, Collection<Property> properties) {
        if (name == null && uniqueId == null) {
            throw new IllegalArgumentException("Either name or uniqueId has to be given!");
        }

        this.uniqueId = uniqueId;
        this.name = name;
        this.properties = properties;
    }

    /**
     * @return if this profile is complete (has UUID and name)
     */
    public boolean isComplete() {
        return this.uniqueId != null && this.name != null;
    }

    /**
     * @return if this profile has properties
     */
    public boolean hasProperties() {
        return this.properties != null;
    }

    /**
     * Fills this profiles with all missing attributes
     *
     * @return if the profile was successfully completed
     */
    public boolean complete() {
        return this.complete(true);
    }

    /**
     * Fills this profiles with all missing attributes
     *
     * @param propertiesAndName if properties and name should be filled for this profile
     * @return if the profile was successfully completed
     */
    public boolean complete(boolean propertiesAndName) {
        if (this.isComplete() && this.hasProperties()) {
            return true;
        }

        if (this.uniqueId == null) {
            RequestBuilder builder = RequestBuilder
                    .newBuilder(String.format(UUID_REQUEST_URL, this.name))
                    .setConnectTimeout(10, TimeUnit.SECONDS)
                    .setRequestMethod(RequestMethod.GET)
                    .enableRedirectFollow()
                    .accepts(MimeTypes.getMimeType("json"));

            try (RequestResult requestResult = builder.fireAndForget()) {
                if (requestResult.getStatus() != StatusCode.OK) {
                    return false;
                }

                JsonElement jsonElement = JsonParser.parseString(requestResult.getResultAsString());

                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (jsonObject.has("id")) {
                        this.uniqueId = UUID.fromString(UNIQUE_ID_PATTERN.matcher(jsonObject.get("id").getAsString()).replaceAll("$1-$2-$3-$4-$5"));
                    } else {
                        return false;
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }

        if ((this.name == null || this.properties == null) && propertiesAndName) {
            RequestBuilder builder = RequestBuilder
                    .newBuilder(String.format(TEXTURES_REQUEST_URL, this.uniqueId.toString().replace("-", ""), false))
                    .setConnectTimeout(10, TimeUnit.SECONDS)
                    .setRequestMethod(RequestMethod.GET)
                    .enableRedirectFollow()
                    .accepts(MimeTypes.getMimeType("json"));

            try (RequestResult requestResult = builder.fireAndForget()) {
                if (requestResult.getStatus() != StatusCode.OK) {
                    return false;
                }

                JsonElement jsonElement = JsonParser.parseString(requestResult.getResultAsString());

                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (jsonObject.has("name") && jsonObject.has("properties")) {
                        this.name = this.name == null ? jsonObject.get("name").getAsString() : this.name;
                        this.properties = this.properties == null ? GSON.get().fromJson(jsonObject.get("properties"), PROPERTY_LIST_TYPE) : this.properties;
                    } else {
                        return false;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public Collection<Property> getProperties() {
        return this.properties == null ? new HashSet<>() : this.properties;
    }

    public Optional<Skin> getSkin() {
        if (this.properties == null) {
            return Optional.empty();
        }

        for (Property property : this.properties) {
            if (property.getName().equals("textures")) {
                return Optional.of(new Skin(property.getValue(), property.getSignature()));
            }
        }

        return Optional.empty();
    }

    public void setProperties(Collection<Property> properties) {
        this.properties = properties;
    }

    public static class Property {

        public Property(@NotNull String name, @NotNull String value, @Nullable String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        private final String name;

        private final String value;

        private final String signature;

        @NotNull
        public String getName() {
            return name;
        }

        @NotNull
        public String getValue() {
            return value;
        }

        @Nullable
        public String getSignature() {
            return signature;
        }

        public boolean isSigned() {
            return signature != null;
        }
    }
}
