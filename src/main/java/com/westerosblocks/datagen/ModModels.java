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
    public static final Model DOOR_BOTTOM_LEFT = block("untinted/door_bottom_left", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_BOTTOM_RIGHT = block("untinted/door_bottom_right", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_BOTTOM_LEFT_OPEN = block("untinted/door_bottom_left_open", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_BOTTOM_RIGHT_OPEN = block("untinted/door_bottom_right_open", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_LEFT = vanillaBlock("door_top_left", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_RIGHT = vanillaBlock("door_top_right", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_LEFT_OPEN = vanillaBlock("door_top_left_open", TextureKey.TOP, TextureKey.BOTTOM);
    public static final Model DOOR_TOP_RIGHT_OPEN = vanillaBlock("door_top_right_open", TextureKey.TOP, TextureKey.BOTTOM);

    // Half door (shutter) models
    public static final Model HALF_DOOR_LEFT = block("untinted/half_door_left", TextureKey.TEXTURE);
    public static final Model HALF_DOOR_RIGHT = block("untinted/half_door_right", TextureKey.TEXTURE);
    public static final Model HALF_DOOR_LEFT_OPEN = block("untinted/half_door_left_open", TextureKey.TEXTURE);
    public static final Model HALF_DOOR_RIGHT_OPEN = block("untinted/half_door_right_open", TextureKey.TEXTURE);

    // Pane models
    public static final Model PANE_POST = block("untinted/ctm_pane_post", TextureKey.SIDE, ModTextureKey.CAP);
    public static final Model PANE_SIDE = block("untinted/ctm_pane_side", TextureKey.SIDE, ModTextureKey.CAP);
    public static final Model PANE_NOSIDE = block("untinted/ctm_pane_noside", TextureKey.SIDE, ModTextureKey.CAP);

    // Torch models
    public static final Model TORCH = block("untinted/template_torch", TextureKey.TORCH);
    public static final Model TORCH_WALL = block("untinted/template_torch_wall", TextureKey.TORCH);

    // Chair models
    public static final Model CHAIR = block("chair/simple_chair", TextureKey.ALL);
    public static final Model CHAIR_45 = block("chair/simple_chair_45", TextureKey.ALL);

    // Branch models
    public static final Model BRANCH_HORIZONTAL_CONNECTED = block("branches/large_branch_horizontal_connected", TextureKey.TEXTURE);

    // Log models (using vanilla parents)
    public static final Model LOG = vanillaBlock("cube_column", TextureKey.END, TextureKey.SIDE);
    public static final Model LOG_HORIZONTAL = vanillaBlock("cube_column_horizontal", TextureKey.END, TextureKey.SIDE);

    // Table models
    public static final Model TABLE = block("tables/table", TextureKey.TEXTURE);

    // Rail models
    public static final Model RAIL_FLAT = vanillaBlock("rail_flat", TextureKey.RAIL);
    public static final Model RAIL_CURVED = vanillaBlock("rail_curved", TextureKey.RAIL);
    public static final Model TEMPLATE_RAIL_RAISED_NE = vanillaBlock("template_rail_raised_ne", TextureKey.RAIL);
    public static final Model TEMPLATE_RAIL_RAISED_SW = vanillaBlock("template_rail_raised_sw", TextureKey.RAIL);

    // Fan models
    public static final Model FAN = block("untinted/fan", ModTextureKey.FAN);
    public static final Model WALL_FAN = block("untinted/wall_fan", ModTextureKey.FAN);

    // Flowerbed models (using vanilla parents)
    public static final Model FLOWERBED_1 = vanillaBlock("flowerbed_1", TextureKey.FLOWERBED, TextureKey.STEM);
    public static final Model FLOWERBED_2 = vanillaBlock("flowerbed_2", TextureKey.FLOWERBED, TextureKey.STEM);
    public static final Model FLOWERBED_3 = vanillaBlock("flowerbed_3", TextureKey.FLOWERBED, TextureKey.STEM);
    public static final Model FLOWERBED_4 = vanillaBlock("flowerbed_4", TextureKey.FLOWERBED, TextureKey.STEM);

    // Particle emitter models
    public static final Model PARTICLE_EMITTER_OFF = block("custom/particle_emitter/particle_emitter_off", TextureKey.TEXTURE, TextureKey.PARTICLE);
    public static final Model PARTICLE_EMITTER_ON = block("custom/particle_emitter/particle_emitter_on", TextureKey.TEXTURE, TextureKey.PARTICLE);

    public static Model CUBOID_NSEW_STACK_ITEM() {
        return new Model(Optional.of(Identifier.of("westerosblocks", "item/cuboid_nsew_stack_item")),
                Optional.empty(),
                TextureKey.PARTICLE,
                ModTextureKey.TEXTURE_1,
                ModTextureKey.TEXTURE_2,
                ModTextureKey.TEXTURE_5);
    }


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
