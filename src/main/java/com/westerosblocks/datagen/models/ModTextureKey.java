package com.westerosblocks.datagen.models;
import net.minecraft.data.client.TextureKey;

public class ModTextureKey {
    public static final TextureKey DOWN_OVERLAY = TextureKey.of("down_ov");
    public static final TextureKey UP_OVERLAY = TextureKey.of("up_ov");
    public static final TextureKey NORTH_OVERLAY = TextureKey.of("north_ov");
    public static final TextureKey SOUTH_OVERLAY = TextureKey.of("south_ov");
    public static final TextureKey WEST_OVERLAY = TextureKey.of("west_ov");
    public static final TextureKey EAST_OVERLAY = TextureKey.of("east_ov");
    public static final TextureKey SIDE_OVERLAY = TextureKey.of("side_ov");
    public static final TextureKey BOTTOM_OVERLAY = TextureKey.of("bottom_ov");
    public static final TextureKey TOP_OVERLAY = TextureKey.of("top_ov");
    public static final TextureKey WALL_OVERLAY = TextureKey.of("wall_ov");
    public static final TextureKey TEXTURE_0 = TextureKey.of("txt0");
    public static final TextureKey TEXTURE_1 = TextureKey.of("txt1");
    public static final TextureKey TEXTURE_2 = TextureKey.of("txt2");
    public static final TextureKey TEXTURE_3 = TextureKey.of("txt3");
    public static final TextureKey TEXTURE_4 = TextureKey.of("txt4");
    public static final TextureKey TEXTURE_5 = TextureKey.of("txt5");
    public static final TextureKey TEXTURE_6 = TextureKey.of("txt6");
    public static final TextureKey TEXTURE_7 = TextureKey.of("txt7");
    public static final TextureKey TEXTURE_8 = TextureKey.of("txt8");
    public static final TextureKey TEXTURE_9 = TextureKey.of("txt9");
    public static final TextureKey TEXTURE_10 = TextureKey.of("txt10");
    public static final TextureKey TEXTURE_11 = TextureKey.of("txt11");
    public static final TextureKey TEXTURE_12 = TextureKey.of("txt12");
    public static final TextureKey TEXTURE_13 = TextureKey.of("txt13");
    public static final TextureKey TEXTURE_14 = TextureKey.of("txt14");
    public static final TextureKey TEXTURE_15 = TextureKey.of("txt15");
    public static final TextureKey TEXTURE_16 = TextureKey.of("txt16");
    public static final TextureKey TEXTURE_17 = TextureKey.of("txt17");
    public static final TextureKey LADDER = TextureKey.of("ladder");
    public static final TextureKey FLOWER_POT = TextureKey.of("flowerpot");
    public static final TextureKey LEAVES_OVERLAY_END = TextureKey.of("overlayend");
    public static final TextureKey LEAVES_OVERLAY_SIDE = TextureKey.of("overlayside");
    public static final TextureKey VINES = TextureKey.of("vines");
    public static final TextureKey BED_TOP = TextureKey.of("bedtop");
    public static final TextureKey BED_END = TextureKey.of("bedend");
    public static final TextureKey BED_SIDE = TextureKey.of("bedside");
    public static final TextureKey BED_TOP2 = TextureKey.of("bedtop2");
    public static final TextureKey BED_END2 = TextureKey.of("bedend2");
    public static final TextureKey BED_SIDE2 = TextureKey.of("bedside2");
    public static final TextureKey CAP = TextureKey.of("cap");
    public static final TextureKey INSIDE = TextureKey.of("inside");

    private static final TextureKey[] TXT_N_KEYS = new TextureKey[18];
    static {
        for (int i = 0; i < TXT_N_KEYS.length; i++) {
            TXT_N_KEYS[i] = TextureKey.of("txt" + i);
        }
    }

    /**
     * Returns a TextureKey for the given numeric index
     */
    public static TextureKey getTextureNKey(int index) {
        if (index < 0 || index >= TXT_N_KEYS.length) {
            throw new IllegalArgumentException("Texture key index out of bounds: " + index);
        }
        return TXT_N_KEYS[index];
    }
}
