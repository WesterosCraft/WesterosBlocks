package com.westerosblocks.datagen;

import java.util.Optional;

import com.westerosblocks.WesterosBlocks;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;
import com.westerosblocks.datagen.ModTextureKey;

public class ModModels {
    // Custom model with cube_all parent and all six face texture keys
    public static final Model CUSTOM_CUBE_ALL = ModModels.block("cube_all",
            TextureKey.DOWN,
            TextureKey.UP,
            TextureKey.NORTH,
            TextureKey.SOUTH,
            TextureKey.EAST,
            TextureKey.WEST);

    // Custom slab models with all six face texture keys
    public static final Model SLAB_BOTTOM = ModModels.block("untinted/slab",
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST, TextureKey.PARTICLE);
    public static final Model SLAB_TOP = ModModels.block("untinted/slab_top",
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST, TextureKey.PARTICLE);

    // Door models - using custom untinted models
    public static final Model DOOR_BOTTOM_LEFT = ModModels.block("untinted/door_bottom_left", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_BOTTOM_RIGHT = ModModels.block("untinted/door_bottom_right", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_BOTTOM_LEFT_OPEN = ModModels.block("untinted/door_bottom_left_open", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_BOTTOM_RIGHT_OPEN = ModModels.block("untinted/door_bottom_right_open", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_LEFT = vanillaBlock("door_top_left", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_RIGHT = vanillaBlock("door_top_right", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_LEFT_OPEN = vanillaBlock("door_top_left_open", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_RIGHT_OPEN = vanillaBlock("door_top_right_open", TextureKey.TOP, TextureKey.BOTTOM);

    // Half door (shutter) models
    public static final Model HALF_DOOR_LEFT = ModModels.block("untinted/half_door_left", TextureKey.TEXTURE);
    public static final Model HALF_DOOR_RIGHT = ModModels.block("untinted/half_door_right", TextureKey.TEXTURE);
    public static final Model HALF_DOOR_LEFT_OPEN = ModModels.block("untinted/half_door_left_open", TextureKey.TEXTURE);
    public static final Model HALF_DOOR_RIGHT_OPEN = ModModels.block("untinted/half_door_right_open", TextureKey.TEXTURE);

    // Pane models
    public static final Model PANE_POST = ModModels.block("untinted/ctm_pane_post", TextureKey.SIDE, ModTextureKey.CAP);
    public static final Model PANE_SIDE = ModModels.block("untinted/ctm_pane_side", TextureKey.SIDE, ModTextureKey.CAP);
    public static final Model PANE_NOSIDE = ModModels.block("untinted/ctm_pane_noside", TextureKey.SIDE, ModTextureKey.CAP);

    // Torch models
    public static final Model TORCH = ModModels.block("untinted/template_torch", TextureKey.TORCH);
    public static final Model TORCH_WALL = ModModels.block("untinted/template_torch_wall", TextureKey.TORCH);

    // Chair models
    public static final Model CHAIR = ModModels.block("chair/simple_chair", TextureKey.ALL);
    public static final Model CHAIR_45 = ModModels.block("chair/simple_chair_45", TextureKey.ALL);

    // Branch models
    public static final Model BRANCH_HORIZONTAL_CONNECTED = ModModels.block("branches/large_branch_horizontal_connected", TextureKey.TEXTURE);

    // Log models (using vanilla parents)
    public static final Model LOG = vanillaBlock("cube_column", TextureKey.END, TextureKey.SIDE);
    public static final Model LOG_HORIZONTAL = vanillaBlock("cube_column_horizontal", TextureKey.END, TextureKey.SIDE);

    // Table models
    public static final Model TABLE = ModModels.block("tables/table", TextureKey.TEXTURE);

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

    // helper method for vanilla block models
    private static Model vanillaBlock(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.ofVanilla("block/" + parent)), Optional.empty(),
                requiredTextureKeys);
    }
}
