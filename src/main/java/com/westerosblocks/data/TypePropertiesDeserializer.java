package com.westerosblocks.data;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for TypeProperties that handles both:
 * - Legacy string format: "unconnect:false,no-uvlock"
 * - New object format: {"unconnect": false, "noUvlock": true}
 */
public class TypePropertiesDeserializer implements JsonDeserializer<TypeProperties> {

    @Override
    public TypeProperties deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            // Legacy string format: "unconnect:false,no-uvlock"
            return parseFromString(json.getAsString());
        } else if (json.isJsonObject()) {
            // New object format - use default deserialization
            Gson defaultGson = new Gson();
            return defaultGson.fromJson(json, TypeProperties.class);
        }

        return null;
    }

    /**
     * Parses legacy string format into TypeProperties.
     *
     * Supported formats:
     * - Boolean flags: "unconnect", "no-uvlock", "bars-model"
     * - Key:value pairs: "unconnect:false", "plant-id:blue_bells"
     * - Comma-separated: "unconnect:false,no-uvlock"
     */
    private TypeProperties parseFromString(String typeStr) {
        if (typeStr == null || typeStr.isEmpty()) {
            return null;
        }

        TypeProperties props = new TypeProperties();

        for (String tok : typeStr.split(",")) {
            String trimmed = tok.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            // Handle key:value pairs
            if (trimmed.contains(":")) {
                String[] parts = trimmed.split(":", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "unconnect" -> props.setUnconnect(Boolean.parseBoolean(value));
                    case "plant-id" -> props.setPlantId(value);
                    case "symmetrical" -> props.setSymmetrical(Boolean.parseBoolean(value));
                    case "locked" -> props.setLocked(Boolean.parseBoolean(value));
                    case "always-on" -> props.setAlwaysOn(Boolean.parseBoolean(value));
                    case "connectstate" -> props.setConnectstate(Boolean.parseBoolean(value));
                }
            } else {
                // Handle boolean flags (presence means true)
                switch (trimmed) {
                    case "unconnect" -> props.setUnconnect(true);
                    case "connectstate" -> props.setConnectstate(true);
                    case "no-uvlock" -> props.setNoUvlock(true);
                    case "bars-model", "bars" -> props.setBarsModel(true);
                    case "legacy-model" -> props.setLegacyModel(true);
                    case "no-decay" -> props.setNoDecay(true);
                    case "better-foliage" -> props.setBetterFoliage(true);
                    case "overlay" -> props.setOverlay(true);
                    case "allow-unsupported" -> props.setAllowUnsupported(true);
                    case "no-particle" -> props.setNoParticle(true);
                    case "no-in-web" -> props.setNoInWeb(true);
                    case "no-climb" -> props.setNoClimb(true);
                    case "toggleOnUse" -> props.setToggleOnUse(true);
                    case "layerSensitive" -> props.setLayerSensitive(true);
                }
            }
        }

        return props;
    }
}
