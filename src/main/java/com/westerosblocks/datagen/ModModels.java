package com.westerosblocks.datagen;

import java.util.Optional;

import com.westerosblocks.WesterosBlocks;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

public class ModModels {

    // Custom model with cube_all parent and all six face texture keys
    public static final Model CUSTOM_CUBE_ALL = ModModels.block("cube_all",
            TextureKey.DOWN,
            TextureKey.UP,
            TextureKey.NORTH,
            TextureKey.SOUTH,
            TextureKey.EAST,
            TextureKey.WEST);

    // helper method for creating Models
    private static Model block(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + parent)), Optional.empty(),
                requiredTextureKeys);
    }

    // helper method for creating Models with variants
    private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + parent)), Optional.of(variant),
                requiredTextureKeys);
    }
}
