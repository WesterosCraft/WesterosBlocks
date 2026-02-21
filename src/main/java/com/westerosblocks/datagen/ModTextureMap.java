package com.westerosblocks.datagen;

import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

public class ModTextureMap {

    /**
     * Creates a texture map for stair models with bottom, top, side, and particle keys.
     * Particle defaults to the side texture.
     */
    public static TextureMap stairTextures(String bottom, String top, String side) {
        return new TextureMap()
                .put(TextureKey.BOTTOM, Identifier.of("westerosblocks", "block/" + bottom))
                .put(TextureKey.TOP, Identifier.of("westerosblocks", "block/" + top))
                .put(TextureKey.SIDE, Identifier.of("westerosblocks", "block/" + side))
                .put(TextureKey.PARTICLE, Identifier.of("westerosblocks", "block/" + side));
    }

    /**
     * Creates a texture map for stair overlay models with base + overlay keys.
     */
    public static TextureMap stairOverlayTextures(String bottom, String top, String side,
                                                   String bottomOv, String topOv, String sideOv) {
        return stairTextures(bottom, top, side)
                .put(ModTextureKey.BOTTOM_OVERLAY, Identifier.of("westerosblocks", "block/" + bottomOv))
                .put(ModTextureKey.TOP_OVERLAY, Identifier.of("westerosblocks", "block/" + topOv))
                .put(ModTextureKey.SIDE_OVERLAY, Identifier.of("westerosblocks", "block/" + sideOv));
    }

    /**
     * Creates a texture map for 6-face log models (down, up, north, south, west, east + particle).
     * Particle defaults to the north texture.
     */
    public static TextureMap logTextures(String down, String up, String north, String south, String west, String east) {
        return new TextureMap()
                .put(TextureKey.DOWN, Identifier.of("westerosblocks", "block/" + down))
                .put(TextureKey.UP, Identifier.of("westerosblocks", "block/" + up))
                .put(TextureKey.NORTH, Identifier.of("westerosblocks", "block/" + north))
                .put(TextureKey.SOUTH, Identifier.of("westerosblocks", "block/" + south))
                .put(TextureKey.WEST, Identifier.of("westerosblocks", "block/" + west))
                .put(TextureKey.EAST, Identifier.of("westerosblocks", "block/" + east))
                .put(TextureKey.PARTICLE, Identifier.of("westerosblocks", "block/" + north));
    }

    /**
     * Creates a texture map for crop models with a single crop key.
     */
    public static TextureMap cropTextures(String crop) {
        return new TextureMap()
                .put(ModTextureKey.CROP, Identifier.of("westerosblocks", "block/" + crop));
    }

    /**
     * Creates a texture map for all six sides of a block with custom textures
     *
     * @param textures Array of texture paths in order: down, up, north, south,
     *                 east, west
     * @return TextureMap with all sides configured
     */
    public static TextureMap customAllSides(String... textures) {
        if (textures.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        // Fill remaining slots with the last texture if less than 6 provided
        String[] filledTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            if (i < textures.length) {
                filledTextures[i] = textures[i];
            } else {
                filledTextures[i] = textures[textures.length - 1];
            }
        }

        return new TextureMap()
                .put(TextureKey.DOWN, Identifier.of("westerosblocks", "block/" + filledTextures[0]))
                .put(TextureKey.UP, Identifier.of("westerosblocks", "block/" + filledTextures[1]))
                .put(TextureKey.NORTH, Identifier.of("westerosblocks", "block/" + filledTextures[2]))
                .put(TextureKey.SOUTH, Identifier.of("westerosblocks", "block/" + filledTextures[3]))
                .put(TextureKey.EAST, Identifier.of("westerosblocks", "block/" + filledTextures[4]))
                .put(TextureKey.WEST, Identifier.of("westerosblocks", "block/" + filledTextures[5]))
                .put(TextureKey.PARTICLE, Identifier.of("westerosblocks", "block/" + filledTextures[0]));
    }
}
