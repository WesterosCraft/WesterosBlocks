package com.westerosblocks.data;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for OptionsProperties.
 * Handles object format: {"unconnect": false, "noUvlock": true}
 */
public class OptionsPropertiesDeserializer implements JsonDeserializer<OptionsProperties> {
    private static final Gson DEFAULT_GSON = new Gson();

    @Override
    public OptionsProperties deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (json.isJsonObject()) {
            // Use a plain Gson instance to avoid infinite recursion through this custom deserializer
            return DEFAULT_GSON.fromJson(json, OptionsProperties.class);
        }

        return null;
    }
}
