package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

/**
 * Factory methods for {@link TextureMap}s keyed to this mod's block texture paths.
 * All methods resolve their string arguments as {@code westerosblocks:block/<path>}.
 */
public class ModTextureMap {

    private static Identifier block(String path) {
        return WesterosBlocks.id("block/" + path);
    }

    /** Stair model: bottom/top/side + particle (defaults to side). */
    public static TextureMap stairTextures(String bottom, String top, String side) {
        return new TextureMap()
                .put(TextureKey.BOTTOM, block(bottom))
                .put(TextureKey.TOP, block(top))
                .put(TextureKey.SIDE, block(side))
                .put(TextureKey.PARTICLE, block(side));
    }

    /** Stair overlay model: base + overlay textures. */
    public static TextureMap stairOverlayTextures(String bottom, String top, String side,
                                                   String bottomOv, String topOv, String sideOv) {
        return stairTextures(bottom, top, side)
                .put(ModTextureKey.BOTTOM_OVERLAY, block(bottomOv))
                .put(ModTextureKey.TOP_OVERLAY, block(topOv))
                .put(ModTextureKey.SIDE_OVERLAY, block(sideOv));
    }

    /** 6-face log model (down/up/north/south/west/east + particle defaulting to north). */
    public static TextureMap logTextures(String down, String up, String north, String south, String west, String east) {
        return new TextureMap()
                .put(TextureKey.DOWN, block(down))
                .put(TextureKey.UP, block(up))
                .put(TextureKey.NORTH, block(north))
                .put(TextureKey.SOUTH, block(south))
                .put(TextureKey.WEST, block(west))
                .put(TextureKey.EAST, block(east))
                .put(TextureKey.PARTICLE, block(north));
    }

    /** Crop model: single crop texture. */
    public static TextureMap cropTextures(String crop) {
        return new TextureMap()
                .put(ModTextureKey.CROP, block(crop))
                .put(TextureKey.PARTICLE, block(crop));
    }

    /** Fence/wall model: bottom/top/side + particle (defaults to side). */
    public static TextureMap fenceWallTextures(String bottom, String top, String side) {
        return new TextureMap()
                .put(TextureKey.BOTTOM, block(bottom))
                .put(TextureKey.TOP, block(top))
                .put(TextureKey.SIDE, block(side))
                .put(TextureKey.PARTICLE, block(side));
    }

    /** Fence/wall overlay model: base + overlay textures. */
    public static TextureMap fenceWallOverlayTextures(String bottom, String top, String side,
                                                       String bottomOv, String topOv, String sideOv) {
        return fenceWallTextures(bottom, top, side)
                .put(ModTextureKey.BOTTOM_OVERLAY, block(bottomOv))
                .put(ModTextureKey.TOP_OVERLAY, block(topOv))
                .put(ModTextureKey.SIDE_OVERLAY, block(sideOv));
    }

    public static TextureMap balconyTextures(String rail, String middle) {
        return new TextureMap()
                .put(TextureKey.RAIL, block(rail))
                .put(ModTextureKey.MIDDLE, block(middle))
                .put(TextureKey.PARTICLE, block(rail));
    }

    /** Standard leaves model: end/side + particle (defaults to side). */
    public static TextureMap leavesTextures(String end, String side) {
        return new TextureMap()
                .put(TextureKey.END, block(end))
                .put(TextureKey.SIDE, block(side))
                .put(TextureKey.PARTICLE, block(side));
    }

    /** Leaves overlay model: base + overlay textures. */
    public static TextureMap leavesOverlayTextures(String end, String side, String endOv, String sideOv) {
        return leavesTextures(end, side)
                .put(ModTextureKey.LEAVES_OVERLAY_END, block(endOv))
                .put(ModTextureKey.LEAVES_OVERLAY_SIDE, block(sideOv));
    }

    /** Better-foliage leaves: single ALL texture + particle. */
    public static TextureMap leavesBetterFoliageTextures(String all) {
        return new TextureMap()
                .put(TextureKey.ALL, block(all))
                .put(TextureKey.PARTICLE, block(all));
    }

    /** Better-foliage leaves overlay: base + overlay textures. */
    public static TextureMap leavesBetterFoliageOverlayTextures(String all, String endOv, String sideOv) {
        return leavesBetterFoliageTextures(all)
                .put(ModTextureKey.LEAVES_OVERLAY_END, block(endOv))
                .put(ModTextureKey.LEAVES_OVERLAY_SIDE, block(sideOv));
    }

    /**
     * Custom all-sides cube texture map. Accepts 1-6 textures in the order
     * {@code down, up, north, south, east, west}; any missing slots repeat the
     * last provided texture. Particle defaults to the {@code down} texture.
     */
    public static TextureMap customAllSides(String... textures) {
        if (textures.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        String[] filled = new String[6];
        for (int i = 0; i < 6; i++) {
            filled[i] = i < textures.length ? textures[i] : textures[textures.length - 1];
        }

        return new TextureMap()
                .put(TextureKey.DOWN, block(filled[0]))
                .put(TextureKey.UP, block(filled[1]))
                .put(TextureKey.NORTH, block(filled[2]))
                .put(TextureKey.SOUTH, block(filled[3]))
                .put(TextureKey.EAST, block(filled[4]))
                .put(TextureKey.WEST, block(filled[5]))
                .put(TextureKey.PARTICLE, block(filled[0]));
    }
}
