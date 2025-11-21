package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a single color map entry from color_maps.json.
 * Maps a list of block names to a specific colormap texture.
 */
public class ColorMapEntry {
    @SerializedName("blockNames")
    private List<String> blockNames;

    @SerializedName("colorMult")
    private String colorMult;

    /**
     * Gets the list of block names that should use this colormap.
     * @return List of block identifiers (e.g., "minecraft:stone")
     */
    public List<String> getBlockNames() {
        return blockNames;
    }

    /**
     * Gets the colormap texture path or hex color.
     * @return Colormap path (e.g., "textures/colormap/stone") or hex color
     */
    public String getColorMult() {
        return colorMult;
    }

    /**
     * Extracts the colormap identifier from the colorMult value.
     * E.g., "textures/colormap/sand" -> "sand", "#FFFFFF" -> "#FFFFFF"
     * @return The colormap identifier
     */
    public String getColormapId() {
        if (colorMult == null) {
            return null;
        }
        if (colorMult.contains("/")) {
            String[] parts = colorMult.split("/");
            return parts[parts.length - 1];
        }
        return colorMult;
    }

    /**
     * Checks if this entry has a valid colorMult value.
     * @return true if colorMult is not null and not empty
     */
    public boolean hasColorMult() {
        return colorMult != null && !colorMult.isEmpty();
    }

    /**
     * Checks if this entry has block names.
     * @return true if blockNames is not null and not empty
     */
    public boolean hasBlockNames() {
        return blockNames != null && !blockNames.isEmpty();
    }
}
