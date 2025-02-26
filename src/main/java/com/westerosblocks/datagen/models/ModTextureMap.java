package com.westerosblocks.datagen.models;

import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;

import static com.westerosblocks.datagen.ModelExport.createBlockIdentifier;

public class ModTextureMap extends TextureMap {

    public static TextureMap singleTexture(ModBlock.RandomTextureSet set) {
        return new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(0)));
    }

    public static TextureMap directional(ModBlock.RandomTextureSet set) {
        return new TextureMap()
                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.SIDE, createBlockIdentifier(set.getTextureByIndex(1)))
                .put(TextureKey.FRONT, createBlockIdentifier(set.getTextureByIndex(2)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(1)));
    }

    public static TextureMap frontTopSides(ModBlock.RandomTextureSet ts, ModBlockStateRecord sr, Boolean hasOverlay, Boolean isSymmetrical) {
        int textureCount = ts.getTextureCount();

        // Find the last non-transparent texture to use as default fallback
        String defaultTexture = "";
        for (int i = textureCount - 1; i >= 0; i--) {
            String texture = ts.getTextureByIndex(i);
            if (texture != null && !texture.equals("transparent")) {
                defaultTexture = texture;
                break;
            }
        }

        // If no non-transparent texture found, use the last texture anyway
        if (defaultTexture.isEmpty() && textureCount > 0) {
            defaultTexture = ts.getTextureByIndex(textureCount - 1);
        }

        TextureMap tMap = new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(textureCount > 0 ? ts.getTextureByIndex(0) : defaultTexture))
                .put(TextureKey.UP, createBlockIdentifier(textureCount > 1 ? ts.getTextureByIndex(1) : defaultTexture))
                .put(TextureKey.NORTH, createBlockIdentifier(textureCount > 2 ? ts.getTextureByIndex(2) : defaultTexture))
                .put(TextureKey.SOUTH, createBlockIdentifier(textureCount > 3 ? ts.getTextureByIndex(3) : defaultTexture))
                .put(TextureKey.WEST, createBlockIdentifier(textureCount > (isSymmetrical != null && isSymmetrical ? 4 : 6) ?
                        ts.getTextureByIndex(isSymmetrical != null && isSymmetrical ? 4 : 6) : defaultTexture))
                .put(TextureKey.EAST, createBlockIdentifier(textureCount > (isSymmetrical != null && isSymmetrical ? 5 : 7) ?
                        ts.getTextureByIndex(isSymmetrical != null && isSymmetrical ? 5 : 7) : defaultTexture))
                .put(TextureKey.PARTICLE, createBlockIdentifier(textureCount > 2 ? ts.getTextureByIndex(2) : defaultTexture));

        if (hasOverlay != null && hasOverlay && sr != null) {
            tMap.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(0)))
                    .put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(1)))
                    .put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(2)))
                    .put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(3)))
                    .put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(4)))
                    .put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(5)));
        }
        return tMap;
    }

    public static TextureMap txtN(ModBlock.RandomTextureSet ts) {
        return new TextureMap()
                .put(ModTextureKey.TEXTURE_0, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(ModTextureKey.TEXTURE_1, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(ModTextureKey.TEXTURE_2, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(ModTextureKey.TEXTURE_3, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(ts.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(ts.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(0)));
    }

    public static TextureMap bed(ModBlock def, boolean head) {
        return new TextureMap()
                .put(ModTextureKey.BED_TOP, createBlockIdentifier(def.getTextureByIndex(head ? 0 : 1)))
                .put(ModTextureKey.BED_END, createBlockIdentifier(def.getTextureByIndex(head ? 4 : 5)))
                .put(ModTextureKey.BED_SIDE, createBlockIdentifier(def.getTextureByIndex(head ? 2 : 3)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(def.getTextureByIndex(0)));
    }

    public static TextureMap bed2(ModBlock def) {
        return new TextureMap()
                .put(ModTextureKey.BED_TOP, createBlockIdentifier(def.getTextureByIndex(0)))
                .put(ModTextureKey.BED_TOP2, createBlockIdentifier(def.getTextureByIndex(1)))
                .put(ModTextureKey.BED_SIDE, createBlockIdentifier(def.getTextureByIndex(2)))
                .put(ModTextureKey.BED_SIDE2, createBlockIdentifier(def.getTextureByIndex(3)))
                .put(ModTextureKey.BED_END, createBlockIdentifier(def.getTextureByIndex(4)))
                .put(ModTextureKey.BED_END2, createBlockIdentifier(def.getTextureByIndex(5)));
    }

    public static TextureMap cake(ModBlock def) {
        return new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(def.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(def.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(def.getTextureByIndex(2)))
                .put(ModTextureKey.INSIDE, createBlockIdentifier(def.getTextureByIndex(3)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(def.getTextureByIndex(2))); // Side texture for particle
    }

    public static TextureMap leaves(ModBlock.RandomTextureSet set, Boolean hasOverlay) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.END, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.SIDE, createBlockIdentifier(set.getTextureByIndex(1)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(0)));

        if (hasOverlay != null && hasOverlay) {
            textureMap
                    .put(ModTextureKey.LEAVES_OVERLAY_END, createBlockIdentifier(set.getTextureByIndex(1)))
                    .put(ModTextureKey.LEAVES_OVERLAY_SIDE, createBlockIdentifier(set.getTextureByIndex(2)))
                    .put(TextureKey.ALL, createBlockIdentifier(set.getTextureByIndex(0)));
        }

        return textureMap;
    }

    public static TextureMap bottomTopSide(ModBlock.RandomTextureSet set, ModBlockStateRecord sr, Boolean hasOverlay) {
        TextureMap tMap = new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(set.getTextureByIndex(2)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(2)));

        if (hasOverlay != null && hasOverlay && sr != null) {
            tMap.put(ModTextureKey.BOTTOM_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(0)))
                    .put(ModTextureKey.TOP_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(1)))
                    .put(ModTextureKey.SIDE_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(2)));
        }
        return tMap;
    }
}
