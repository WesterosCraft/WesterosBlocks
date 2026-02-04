package com.westerosblocks.data;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for TypeProperties.
 * Handles object format: {"unconnect": false, "noUvlock": true}
 */
public class TypePropertiesDeserializer implements JsonDeserializer<TypeProperties> {

    @Override
    public TypeProperties deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (json.isJsonObject()) {
            Gson defaultGson = new Gson();
            return defaultGson.fromJson(json, TypeProperties.class);
        }

        return null;
    }
}
