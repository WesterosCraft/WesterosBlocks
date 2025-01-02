package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModels {
    private static final String GENERATED_PATH = "block/generated/";

    public static final Model ALL_SIDES = block("cube_all",
            TextureKey.DOWN,
            TextureKey.UP,
            TextureKey.NORTH,
            TextureKey.SOUTH,
            TextureKey.WEST,
            TextureKey.EAST,
            TextureKey.PARTICLE);

    public static final Model ALL_SIDES_WITH_OVERLAY = block("untinted/cube_overlay",
            TextureKey.DOWN,
            TextureKey.UP,
            TextureKey.NORTH,
            TextureKey.SOUTH,
            TextureKey.WEST,
            TextureKey.EAST,
            TextureKey.PARTICLE,
            TextureKey.of("down_ov"),
            TextureKey.of("up_ov"),
            TextureKey.of("north_ov"),
            TextureKey.of("south_ov"),
            TextureKey.of("west_ov"),
            TextureKey.of("east_ov"));

    private static Model block(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, GENERATED_PATH + parent )), Optional.empty(), requiredTextureKeys);
    }
}
