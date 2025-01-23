package com.westerosblocks.datagen.models;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;

import static com.westerosblocks.datagen.ModelExport.createBlockIdentifier;

public class ModTextureMap extends TextureMap {

    public static TextureMap frontTopSides(WesterosBlockDef.RandomTextureSet ts, WesterosBlockStateRecord currentRec, Boolean hasOverlay, Boolean isSymmetrical) {
        TextureMap tMap = new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(TextureKey.UP, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(TextureKey.NORTH, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(TextureKey.SOUTH, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(TextureKey.WEST, createBlockIdentifier(ts.getTextureByIndex(isSymmetrical != null && isSymmetrical ? 4 : 6)))
                .put(TextureKey.EAST, createBlockIdentifier(ts.getTextureByIndex(isSymmetrical != null && isSymmetrical ? 5 : 7)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));

        if (hasOverlay != null && hasOverlay && currentRec != null) {
            tMap.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
                    .put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
                    .put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)))
                    .put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(3)))
                    .put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(4)))
                    .put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(5)));
        }
        return tMap;
    }

    public static TextureMap txtN(WesterosBlockDef.RandomTextureSet ts) {
        return new TextureMap()
                .put(ModTextureKey.TEXTURE_0, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(ModTextureKey.TEXTURE_1, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(ModTextureKey.TEXTURE_2, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(ModTextureKey.TEXTURE_3, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(ts.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(ts.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(0)));
    }

    public static TextureMap bed(WesterosBlockDef def, boolean head) {
        return new TextureMap()
                .put(ModTextureKey.BED_TOP, createBlockIdentifier(def.getTextureByIndex(head ? 0 : 1)))
                .put(ModTextureKey.BED_END, createBlockIdentifier(def.getTextureByIndex(head ? 4 : 5)))
                .put(ModTextureKey.BED_SIDE, createBlockIdentifier(def.getTextureByIndex(head ? 2 : 3)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(def.getTextureByIndex(0)));
    }

    public static TextureMap bed2(WesterosBlockDef def) {
        return new TextureMap()
                .put(ModTextureKey.BED_TOP, createBlockIdentifier(def.getTextureByIndex(0)))
                .put(ModTextureKey.BED_TOP2, createBlockIdentifier(def.getTextureByIndex(1)))
                .put(ModTextureKey.BED_SIDE, createBlockIdentifier(def.getTextureByIndex(2)))
                .put(ModTextureKey.BED_SIDE2, createBlockIdentifier(def.getTextureByIndex(3)))
                .put(ModTextureKey.BED_END, createBlockIdentifier(def.getTextureByIndex(4)))
                .put(ModTextureKey.BED_END2, createBlockIdentifier(def.getTextureByIndex(5)));
    }

    public static TextureMap cake(WesterosBlockDef def) {
        return new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(def.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(def.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(def.getTextureByIndex(2)))
                .put(ModTextureKey.INSIDE, createBlockIdentifier(def.getTextureByIndex(3)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(def.getTextureByIndex(2))); // Side texture for particle
    }

    public static TextureMap leaves(WesterosBlockDef.RandomTextureSet set, Boolean hasOverlay) {
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

    public static TextureMap bottomTopSide(WesterosBlockDef.RandomTextureSet set, WesterosBlockStateRecord sr, Boolean hasOverlay) {
        TextureMap tMap = new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(set.getTextureByIndex(2)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(2)));

        if (hasOverlay != null && hasOverlay) {
            tMap
                    .put(ModTextureKey.BOTTOM_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(0)))
                    .put(ModTextureKey.TOP_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(1)))
                    .put(ModTextureKey.SIDE_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(2)));
        }
        return tMap;
    }
}
