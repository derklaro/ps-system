package de.derklaro.privateservers.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.Serializable;

public final class Constants implements Serializable {

    public static final JsonParser PARSER = new JsonParser();

    public static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
}
