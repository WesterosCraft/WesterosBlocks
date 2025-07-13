package com.westerosblocks.datagen.models;

/**
 * Utility class for texture array operations used by block model exports.
 */
public class TextureUtils {
    
    /**
     * Expands a texture array to exactly 6 items by filling remaining slots with the last texture.
     * If the input array has fewer than 6 items, the last item is used to fill the remaining slots.
     * 
     * @param texturePaths The input texture array
     * @return An array with exactly 6 texture paths
     */
    public static String[] expandTextureArray(String... texturePaths) {
        if (texturePaths == null || texturePaths.length == 0) {
            // Return default textures if none provided
            return new String[]{"stone", "stone", "stone", "stone", "stone", "stone"};
        }
        
        if (texturePaths.length >= 6) {
            // Already has 6 or more textures, return first 6
            String[] result = new String[6];
            System.arraycopy(texturePaths, 0, result, 0, 6);
            return result;
        }
        
        // Expand to 6 textures by filling remaining slots with the last texture
        String[] result = new String[6];
        System.arraycopy(texturePaths, 0, result, 0, texturePaths.length);
        
        String lastTexture = texturePaths[texturePaths.length - 1];
        for (int i = texturePaths.length; i < 6; i++) {
            result[i] = lastTexture;
        }
        
        return result;
    }
} 