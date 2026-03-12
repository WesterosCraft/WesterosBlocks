package com.westerosblocks.data;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for OptionsProperties.
 * Handles object format: {"unconnect": false, "noUvlock": true}
 */
public class OptionsPropertiesDeserializer implements JsonDeserializer<OptionsProperties> {

    @Override
    public OptionsProperties deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (json.isJsonObject()) {
            return context.deserialize(json, OptionsProperties.class);
        }

        return null;
    }
}
