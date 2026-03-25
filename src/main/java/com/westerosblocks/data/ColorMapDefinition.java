package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Root POJO for color_maps.json file.
 * Contains a list of color map entries that map blocks to colormaps.
 */
public class ColorMapDefinition {
    @SerializedName("colorMaps")
    private List<ColorMapEntry> colorMaps;

    /**
     * Gets the list of color map entries.
     * @return List of ColorMapEntry objects
     */
    public List<ColorMapEntry> getColorMaps() {
        return colorMaps != null ? colorMaps : new ArrayList<>();
    }

    /**
     * Checks if there are any color map entries.
     * @return true if colorMaps is not null and not empty
     */
    public boolean hasColorMaps() {
        return colorMaps != null && !colorMaps.isEmpty();
    }

    /**
     * Gets the count of color map entries.
     * @return Number of entries
     */
    public int getColorMapCount() {
        return colorMaps != null ? colorMaps.size() : 0;
    }

    /**
     * Gets the total count of blocks across all color map entries.
     * @return Total number of blocks
     */
    public int getTotalBlockCount() {
        if (colorMaps == null) {
            return 0;
        }
        return colorMaps.stream()
                .mapToInt(entry -> entry.hasBlockNames() ? entry.getBlockNames().size() : 0)
                .sum();
    }
}
