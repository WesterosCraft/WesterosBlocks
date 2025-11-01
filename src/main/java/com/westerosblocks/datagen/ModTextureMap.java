package com.westerosblocks.datagen;

import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

public class ModTextureMap {

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
