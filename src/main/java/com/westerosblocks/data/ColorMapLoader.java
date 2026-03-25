package com.westerosblocks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.westerosblocks.WesterosBlocks;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loader for color_maps.json file.
 * This file maps vanilla and mod blocks to colormap textures for Polytone integration.
 */
public class ColorMapLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String colorMapsFilePath;

    /**
     * Creates a new ColorMapLoader.
     * @param colorMapsFilePath Path to the color_maps.json file (e.g., "definitions/color_maps.json")
     */
    public ColorMapLoader(String colorMapsFilePath) {
        this.colorMapsFilePath = colorMapsFilePath;
    }

    /**
     * Loads the color map definition from the JSON file.
     * @return ColorMapDefinition object, or null if loading fails
     */
    public ColorMapDefinition loadDefinition() {
        WesterosBlocks.LOGGER.info("Loading color maps from: {}", colorMapsFilePath);

        try {
            ColorMapDefinition definition = loadFromResources();
            if (definition != null && definition.hasColorMaps()) {
                WesterosBlocks.LOGGER.info("Successfully loaded {} color maps with {} total blocks",
                        definition.getColorMapCount(), definition.getTotalBlockCount());
                return definition;
            } else {
                WesterosBlocks.LOGGER.warn("Color maps file found but contains no entries");
                return null;
            }
        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to read color maps file: {}", colorMapsFilePath, e);
        } catch (JsonSyntaxException e) {
            WesterosBlocks.LOGGER.error("Invalid JSON syntax in color maps file: {}", colorMapsFilePath, e);
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Unexpected error loading color maps", e);
        }

        return null;
    }

    /**
     * Loads the color map definition from mod resources.
     * @return ColorMapDefinition object
     * @throws IOException if file cannot be read
     */
    private ColorMapDefinition loadFromResources() throws IOException {
        ModContainer container = FabricLoader.getInstance().getModContainer(WesterosBlocks.MOD_ID).orElse(null);
        if (container == null) {
            WesterosBlocks.LOGGER.error("Could not find mod container for {}", WesterosBlocks.MOD_ID);
            return null;
        }

        // Try to find the file in the mod's root paths
        for (Path rootPath : container.getRootPaths()) {
            String pathWithoutSlash = colorMapsFilePath.startsWith("/")
                    ? colorMapsFilePath.substring(1)
                    : colorMapsFilePath;
            Path filePath = rootPath.resolve(pathWithoutSlash);

            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                WesterosBlocks.LOGGER.debug("Found color maps file at: {}", filePath);
                String content = Files.readString(filePath);
                return GSON.fromJson(content, ColorMapDefinition.class);
            }
        }

        WesterosBlocks.LOGGER.warn("Color maps file not found at: {}", colorMapsFilePath);
        return null;
    }

    /**
     * Validates the loaded color map definition.
     * Checks for empty entries, invalid colormap paths, etc.
     * @param definition The definition to validate
     */
    public void validateDefinition(ColorMapDefinition definition) {
        if (definition == null || !definition.hasColorMaps()) {
            return;
        }

        for (ColorMapEntry entry : definition.getColorMaps()) {
            if (!entry.hasColorMult()) {
                WesterosBlocks.LOGGER.warn("Color map entry has no colorMult value");
            }
            if (!entry.hasBlockNames()) {
                WesterosBlocks.LOGGER.warn("Color map entry for '{}' has no block names", entry.getColorMult());
            }
        }
    }
}
