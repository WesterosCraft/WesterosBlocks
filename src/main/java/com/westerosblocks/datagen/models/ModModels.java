package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModels {
    private static final String GENERATED_PATH = "block/";

    public static Model getAllSides(String parent) {
        return null;
    }

    public static Model getAllSides(String parent, String namespace) {
        return block(parent,
                namespace,
                TextureKey.DOWN,
                TextureKey.UP,
                TextureKey.NORTH,
                TextureKey.SOUTH,
                TextureKey.WEST,
                TextureKey.EAST,
                TextureKey.PARTICLE);
    }

    public static Model getAllSidesWithOverlay(String parent) {
        return block(parent,
                TextureKey.DOWN,
                TextureKey.UP,
                TextureKey.NORTH,
                TextureKey.SOUTH,
                TextureKey.WEST,
                TextureKey.EAST,
                TextureKey.PARTICLE,
                ModTextureKey.DOWN_OVERLAY,
                ModTextureKey.UP_OVERLAY,
                ModTextureKey.NORTH_OVERLAY,
                ModTextureKey.SOUTH_OVERLAY,
                ModTextureKey.WEST_OVERLAY,
                ModTextureKey.EAST_OVERLAY);
    }

    public static final Model ALL_SIDES = getAllSides("cube_all");
    public static final Model ALL_SIDES_WITH_OVERLAY = getAllSidesWithOverlay("untinted/cube_overlay");

    private static Model block(String parent, String namespace, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(namespace != null ? namespace : WesterosBlocks.MOD_ID, GENERATED_PATH + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static Model block(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, GENERATED_PATH + parent)), Optional.empty(), requiredTextureKeys);
    }
}