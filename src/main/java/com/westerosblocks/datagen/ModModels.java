package com.westerosblocks.datagen;

import java.util.Optional;

import com.westerosblocks.WesterosBlocks;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;
import com.westerosblocks.datagen.ModTextureKey;

public class ModModels {
    // Custom model with cube_all parent and all six face texture keys
    public static final Model CUSTOM_CUBE_ALL = sixFace("cube_all");

    // Custom slab models with all six face texture keys
    public static final Model SLAB_BOTTOM = sixFace("untinted/slab");
    public static final Model SLAB_TOP = sixFace("untinted/slab_top");
    public static final Model SLAB_BOTTOM_TINTED = sixFace("tinted/half_slab");
    public static final Model SLAB_TOP_TINTED = sixFace("tinted/upper_slab");

    // Slab overlay models
    public static final Model SLAB_BOTTOM_OVERLAY_UNTINTED = sixFaceOverlay("untinted/half_slab_overlay");
    public static final Model SLAB_BOTTOM_OVERLAY_TINTED = sixFaceOverlay("tinted/half_slab_overlay");
    public static final Model SLAB_TOP_OVERLAY_UNTINTED = sixFaceOverlay("untinted/upper_slab_overlay");
    public static final Model SLAB_TOP_OVERLAY_TINTED = sixFaceOverlay("tinted/upper_slab_overlay");

    // Door models - using custom untinted models
    public static final Model DOOR_BOTTOM_LEFT = block("untinted/door_bottom_left", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model DOOR_BOTTOM_RIGHT = block("untinted/door_bottom_right", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model DOOR_BOTTOM_LEFT_OPEN = block("untinted/door_bottom_left_open", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model DOOR_BOTTOM_RIGHT_OPEN = block("untinted/door_bottom_right_open", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model DOOR_TOP_LEFT = vanillaBlock("door_top_left", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model DOOR_TOP_RIGHT = vanillaBlock("door_top_right", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model DOOR_TOP_LEFT_OPEN = vanillaBlock("door_top_left_open", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model DOOR_TOP_RIGHT_OPEN = vanillaBlock("door_top_right_open", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.PARTICLE);

    // Half door (shutter) models — parent templates use #bottom
    public static final Model HALF_DOOR_LEFT = block("untinted/half_door_left", TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model HALF_DOOR_RIGHT = block("untinted/half_door_right", TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model HALF_DOOR_LEFT_OPEN = block("untinted/half_door_left_open", TextureKey.BOTTOM, TextureKey.PARTICLE);
    public static final Model HALF_DOOR_RIGHT_OPEN = block("untinted/half_door_right_open", TextureKey.BOTTOM, TextureKey.PARTICLE);

    // Pane models
    public static final Model PANE_POST = block("untinted/ctm_pane_post", TextureKey.SIDE, ModTextureKey.CAP, TextureKey.PARTICLE);
    public static final Model PANE_SIDE = block("untinted/ctm_pane_side", TextureKey.SIDE, ModTextureKey.CAP, TextureKey.PARTICLE);
    public static final Model PANE_NOSIDE = block("untinted/ctm_pane_noside", TextureKey.SIDE, ModTextureKey.CAP, TextureKey.PARTICLE);

    // Torch models
    public static final Model TORCH = block("untinted/template_torch", TextureKey.TORCH, TextureKey.PARTICLE);
    public static final Model TORCH_WALL = block("untinted/template_torch_wall", TextureKey.TORCH, TextureKey.PARTICLE);

    // Chair models
    public static final Model CHAIR = block("chair/simple_chair", TextureKey.ALL, TextureKey.PARTICLE);
    public static final Model CHAIR_45 = block("chair/simple_chair_45", TextureKey.ALL, TextureKey.PARTICLE);

    // Branch models
    public static final Model BRANCH_HORIZONTAL_CONNECTED = block("branches/large_branch_horizontal_connected", TextureKey.TEXTURE, TextureKey.PARTICLE);

    // Log models (using vanilla parents — 2-texture END+SIDE)
    public static final Model LOG = vanillaBlock("cube_column", TextureKey.END, TextureKey.SIDE);
    public static final Model LOG_HORIZONTAL = vanillaBlock("cube_column_horizontal", TextureKey.END, TextureKey.SIDE);

    // Log models (using custom parents — 6-texture per face + particle)
    public static final Model LOG_6FACE = block("untinted/cube_log",
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST, TextureKey.PARTICLE);
    public static final Model LOG_6FACE_HORIZONTAL = block("untinted/cube_log_horizontal",
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST, TextureKey.PARTICLE);
    public static final Model LOG_6FACE_TINTED = block("tinted/cube_log",
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST, TextureKey.PARTICLE);
    public static final Model LOG_6FACE_HORIZONTAL_TINTED = block("tinted/cube_log_horizontal",
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST, TextureKey.PARTICLE);

    // Bench models
    public static final Model BENCH_SINGLE = block("bench/wood_bench_1x1", ModTextureKey.BENCH, TextureKey.PARTICLE);
    public static final Model BENCH_LEFT = block("bench/wood_bench_edge", ModTextureKey.BENCH, TextureKey.PARTICLE);
    public static final Model BENCH_RIGHT = block("bench/wood_bench_right", ModTextureKey.BENCH, TextureKey.PARTICLE);
    public static final Model BENCH_MIDDLE = block("bench/wood_bench_middle", ModTextureKey.BENCH, TextureKey.PARTICLE);

    // Table models
    public static final Model TABLE_SINGLE = block("table/wood_table_1x1", ModTextureKey.TABLE, TextureKey.PARTICLE);
    public static final Model TABLE_LEFT = block("table/wood_table_left", ModTextureKey.TABLE, TextureKey.PARTICLE);
    public static final Model TABLE_RIGHT = block("table/wood_table_right", ModTextureKey.TABLE, TextureKey.PARTICLE);
    public static final Model TABLE_MIDDLE = block("table/wood_table_middle", ModTextureKey.TABLE, TextureKey.PARTICLE);

    // Fence models — untinted/tinted × with/without overlay
    public static final Model FENCE_POST_UNTINTED = connectorShape("fence_post", false, false);
    public static final Model FENCE_POST_TINTED = connectorShape("fence_post", true, false);
    public static final Model FENCE_POST_OVERLAY_UNTINTED = connectorShape("fence_post", false, true);
    public static final Model FENCE_POST_OVERLAY_TINTED = connectorShape("fence_post", true, true);

    public static final Model FENCE_SIDE_UNTINTED = connectorShape("fence_side", false, false);
    public static final Model FENCE_SIDE_TINTED = connectorShape("fence_side", true, false);
    public static final Model FENCE_SIDE_OVERLAY_UNTINTED = connectorShape("fence_side", false, true);
    public static final Model FENCE_SIDE_OVERLAY_TINTED = connectorShape("fence_side", true, true);

    public static final Model FENCE_INVENTORY_UNTINTED = connectorShape("fence_inventory", false, false);
    public static final Model FENCE_INVENTORY_TINTED = connectorShape("fence_inventory", true, false);
    public static final Model FENCE_INVENTORY_OVERLAY_UNTINTED = connectorShape("fence_inventory", false, true);
    public static final Model FENCE_INVENTORY_OVERLAY_TINTED = connectorShape("fence_inventory", true, true);

    // Wall models — untinted/tinted × with/without overlay
    public static final Model WALL_POST_UNTINTED = connectorShape("template_wall_post", false, false);
    public static final Model WALL_POST_TINTED = connectorShape("template_wall_post", true, false);
    public static final Model WALL_POST_OVERLAY_UNTINTED = connectorShape("template_wall_post", false, true);
    public static final Model WALL_POST_OVERLAY_TINTED = connectorShape("template_wall_post", true, true);

    public static final Model WALL_SIDE_UNTINTED = connectorShape("template_wall_side", false, false);
    public static final Model WALL_SIDE_TINTED = connectorShape("template_wall_side", true, false);
    public static final Model WALL_SIDE_OVERLAY_UNTINTED = connectorShape("template_wall_side", false, true);
    public static final Model WALL_SIDE_OVERLAY_TINTED = connectorShape("template_wall_side", true, true);

    public static final Model WALL_SIDE_SHORT_UNTINTED = connectorShape("template_wall_side_2", false, false);
    public static final Model WALL_SIDE_SHORT_TINTED = connectorShape("template_wall_side_2", true, false);
    public static final Model WALL_SIDE_SHORT_OVERLAY_UNTINTED = connectorShape("template_wall_side_2", false, true);
    public static final Model WALL_SIDE_SHORT_OVERLAY_TINTED = connectorShape("template_wall_side_2", true, true);

    public static final Model WALL_SIDE_TALL_UNTINTED = connectorShape("template_wall_side_tall", false, false);
    public static final Model WALL_SIDE_TALL_TINTED = connectorShape("template_wall_side_tall", true, false);
    public static final Model WALL_SIDE_TALL_OVERLAY_UNTINTED = connectorShape("template_wall_side_tall", false, true);
    public static final Model WALL_SIDE_TALL_OVERLAY_TINTED = connectorShape("template_wall_side_tall", true, true);

    public static final Model WALL_INVENTORY_UNTINTED = connectorShape("wall_inventory", false, false);
    public static final Model WALL_INVENTORY_TINTED = connectorShape("wall_inventory", true, false);

    // Hedge wall models (always tinted — hedges always have colorMult)
    public static final Model HEDGE_POST_TINTED = connectorShape("template_hedge_wall_post", true, false);
    public static final Model HEDGE_SIDE_TINTED = connectorShape("template_hedge_wall_side", true, false);
    public static final Model HEDGE_SIDE_TALL_TINTED = connectorShape("template_hedge_wall_side_tall", true, false);
    public static final Model HEDGE_INVENTORY_TINTED = connectorShape("hedge_wall_inventory", true, false);

    // Leaves models — standard (END+SIDE) and better foliage (ALL)
    public static final Model LEAVES_UNTINTED = block("untinted/leaves",
            TextureKey.END, TextureKey.SIDE, TextureKey.PARTICLE);
    public static final Model LEAVES_TINTED = block("tinted/leaves",
            TextureKey.END, TextureKey.SIDE, TextureKey.PARTICLE);
    public static final Model LEAVES_OVERLAY_UNTINTED = block("untinted/leaves_overlay",
            TextureKey.END, TextureKey.SIDE, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
    public static final Model LEAVES_OVERLAY_TINTED = block("tinted/leaves_overlay",
            TextureKey.END, TextureKey.SIDE, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);

    public static final Model LEAVES_BF1_UNTINTED = block("untinted/leaves_bf1",
            TextureKey.ALL, TextureKey.PARTICLE);
    public static final Model LEAVES_BF1_TINTED = block("tinted/leaves_bf1",
            TextureKey.ALL, TextureKey.PARTICLE);
    public static final Model LEAVES_BF1_OVERLAY_UNTINTED = block("untinted/leaves_overlay_bf1",
            TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
    public static final Model LEAVES_BF1_OVERLAY_TINTED = block("tinted/leaves_overlay_bf1",
            TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);

    public static final Model LEAVES_BF2_UNTINTED = block("untinted/leaves_bf2",
            TextureKey.ALL, TextureKey.PARTICLE);
    public static final Model LEAVES_BF2_TINTED = block("tinted/leaves_bf2",
            TextureKey.ALL, TextureKey.PARTICLE);
    public static final Model LEAVES_BF2_OVERLAY_UNTINTED = block("untinted/leaves_overlay_bf2",
            TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
    public static final Model LEAVES_BF2_OVERLAY_TINTED = block("tinted/leaves_overlay_bf2",
            TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);

    public static final Model LEAVES_BF3_UNTINTED = block("untinted/leaves_bf3",
            TextureKey.ALL, TextureKey.PARTICLE);
    public static final Model LEAVES_BF3_TINTED = block("tinted/leaves_bf3",
            TextureKey.ALL, TextureKey.PARTICLE);
    public static final Model LEAVES_BF3_OVERLAY_UNTINTED = block("untinted/leaves_overlay_bf3",
            TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
    public static final Model LEAVES_BF3_OVERLAY_TINTED = block("tinted/leaves_overlay_bf3",
            TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);

    // Solid cube models (tinted and overlay variants)
    public static final Model CUBE_TINTED = sixFace("tinted/cube");
    public static final Model CUBE_OVERLAY_UNTINTED = sixFaceOverlay("untinted/cube_overlay");
    public static final Model CUBE_OVERLAY_TINTED = sixFaceOverlay("tinted/cube_overlay");

    // Bed item models
    public static final Model BED_ITEM_UNTINTED = new Model(
            Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "item/untinted/bed_item")), Optional.empty(),
            ModTextureKey.BED_TOP, ModTextureKey.BED_TOP2, ModTextureKey.BED_SIDE,
            ModTextureKey.BED_SIDE2, ModTextureKey.BED_END, ModTextureKey.BED_END2);
    public static final Model BED_ITEM_TINTED = new Model(
            Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "item/tinted/bed_item")), Optional.empty(),
            ModTextureKey.BED_TOP, ModTextureKey.BED_TOP2, ModTextureKey.BED_SIDE,
            ModTextureKey.BED_SIDE2, ModTextureKey.BED_END, ModTextureKey.BED_END2);

    // Flower pot models
    public static final Model FLOWERPOT_EMPTY_UNTINTED = block("untinted/flower_pot",
            ModTextureKey.DIRT, ModTextureKey.FLOWERPOT, TextureKey.PARTICLE);
    public static final Model FLOWERPOT_EMPTY_TINTED = block("tinted/flower_pot",
            ModTextureKey.DIRT, ModTextureKey.FLOWERPOT, TextureKey.PARTICLE);
    public static final Model FLOWERPOT_FILLED_UNTINTED = block("untinted/flower_pot_cross",
            ModTextureKey.DIRT, ModTextureKey.FLOWERPOT, ModTextureKey.PLANT, TextureKey.PARTICLE);
    public static final Model FLOWERPOT_FILLED_TINTED = block("tinted/flower_pot_cross",
            ModTextureKey.DIRT, ModTextureKey.FLOWERPOT, ModTextureKey.PLANT, TextureKey.PARTICLE);

    // Cross models (base only — layer variants are parameterized)
    public static final Model CROSS_UNTINTED = block("untinted/cross", TextureKey.CROSS, TextureKey.PARTICLE);
    public static final Model CROSS_TINTED = block("tinted/cross", TextureKey.CROSS, TextureKey.PARTICLE);

    // Vine models
    public static final Model VINE_SIDE_UNTINTED = block("untinted/vine_1", ModTextureKey.VINES, TextureKey.PARTICLE);
    public static final Model VINE_SIDE_TINTED = block("tinted/vine_1", ModTextureKey.VINES, TextureKey.PARTICLE);
    public static final Model VINE_TOP_UNTINTED = block("untinted/vine_u", ModTextureKey.VINES, TextureKey.PARTICLE);
    public static final Model VINE_TOP_TINTED = block("tinted/vine_u", ModTextureKey.VINES, TextureKey.PARTICLE);

    // Pane side model (bars variant — regular pane side already defined as PANE_SIDE)
    public static final Model PANE_SIDE_BARS = block("untinted/bars_side", TextureKey.SIDE, ModTextureKey.CAP, TextureKey.PARTICLE);

    // Rail models
    public static final Model RAIL_FLAT = vanillaBlock("rail_flat", TextureKey.RAIL, TextureKey.PARTICLE);
    public static final Model RAIL_CURVED = vanillaBlock("rail_curved", TextureKey.RAIL, TextureKey.PARTICLE);
    public static final Model TEMPLATE_RAIL_RAISED_NE = vanillaBlock("template_rail_raised_ne", TextureKey.RAIL, TextureKey.PARTICLE);
    public static final Model TEMPLATE_RAIL_RAISED_SW = vanillaBlock("template_rail_raised_sw", TextureKey.RAIL, TextureKey.PARTICLE);

    // Fan models
    public static final Model FAN = block("untinted/fan", ModTextureKey.FAN, TextureKey.PARTICLE);
    public static final Model WALL_FAN = block("untinted/wall_fan", ModTextureKey.FAN, TextureKey.PARTICLE);

    // Flowerbed models (using vanilla parents)
    public static final Model FLOWERBED_1 = vanillaBlock("flowerbed_1", TextureKey.FLOWERBED, TextureKey.STEM, TextureKey.PARTICLE);
    public static final Model FLOWERBED_2 = vanillaBlock("flowerbed_2", TextureKey.FLOWERBED, TextureKey.STEM, TextureKey.PARTICLE);
    public static final Model FLOWERBED_3 = vanillaBlock("flowerbed_3", TextureKey.FLOWERBED, TextureKey.STEM, TextureKey.PARTICLE);
    public static final Model FLOWERBED_4 = vanillaBlock("flowerbed_4", TextureKey.FLOWERBED, TextureKey.STEM, TextureKey.PARTICLE);

    // Bunting models
    public static final Model BUNTING_WALL = block("buntings/bunting_wall", ModTextureKey.ZERO, TextureKey.PARTICLE);
    public static final Model BUNTING_CEILING = block("buntings/bunting_ceiling", ModTextureKey.ZERO, TextureKey.PARTICLE);

    // Balcony models — untinted/tinted
    public static final Model BALCONY_SIDE_UNTINTED = block("untinted/balcony_side",
            TextureKey.RAIL, ModTextureKey.MIDDLE, TextureKey.PARTICLE);
    public static final Model BALCONY_SIDE_TINTED = block("tinted/balcony_side",
            TextureKey.RAIL, ModTextureKey.MIDDLE, TextureKey.PARTICLE);
    public static final Model BALCONY_SIDE_WALL_UNTINTED = block("untinted/balcony_side_wall",
            TextureKey.RAIL, ModTextureKey.MIDDLE, TextureKey.PARTICLE);
    public static final Model BALCONY_SIDE_WALL_TINTED = block("tinted/balcony_side_wall",
            TextureKey.RAIL, ModTextureKey.MIDDLE, TextureKey.PARTICLE);

    // Arrow slit models
    public static final Model ARROW_SLIT_SINGLE = block("arrow_slits/arrow_slit_single", TextureKey.TEXTURE, TextureKey.PARTICLE);
    public static final Model ARROW_SLIT_BOTTOM = block("arrow_slits/arrow_slit_bottom", TextureKey.TEXTURE, TextureKey.PARTICLE);
    public static final Model ARROW_SLIT_TOP = block("arrow_slits/arrow_slit_top", TextureKey.TEXTURE, TextureKey.PARTICLE);
    public static final Model ARROW_SLIT_MIDDLE = block("arrow_slits/arrow_slit_middle", TextureKey.TEXTURE, TextureKey.PARTICLE);

    // Particle emitter models
    public static final Model PARTICLE_EMITTER_OFF = block("custom/particle_emitter/particle_emitter_off", TextureKey.TEXTURE, TextureKey.PARTICLE);
    public static final Model PARTICLE_EMITTER_ON = block("custom/particle_emitter/particle_emitter_on", TextureKey.TEXTURE, TextureKey.PARTICLE);

    public static Model CUBOID_NSEW_STACK_ITEM() {
        return new Model(Optional.of(WesterosBlocks.id("item/cuboid_nsew_stack_item")),
                Optional.empty(),
                TextureKey.PARTICLE,
                ModTextureKey.TEXTURE_1,
                ModTextureKey.TEXTURE_2,
                ModTextureKey.TEXTURE_5,
                ModTextureKey.TEXTURE_6);
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

    /**
     * Multipart connector shape (fence/wall/hedge): bottom/top/side, optional overlays, particle.
     * Path format: {@code (tinted|untinted)/<kind>[_overlay]}.
     */
    private static Model connectorShape(String kind, boolean tinted, boolean overlay) {
        String path = (tinted ? "tinted/" : "untinted/") + kind + (overlay ? "_overlay" : "");
        return overlay
                ? block(path, TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                        ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY, TextureKey.PARTICLE)
                : block(path, TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE);
    }

    /** 6-face cube shape: down/up/north/south/east/west + particle. Full path (no prefix added). */
    private static Model sixFace(String path) {
        return block(path,
                TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST, TextureKey.PARTICLE);
    }

    /** 6-face cube shape with per-face overlays + particle. Full path (no prefix added). */
    private static Model sixFaceOverlay(String path) {
        return block(path,
                TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST,
                ModTextureKey.DOWN_OVERLAY, ModTextureKey.UP_OVERLAY, ModTextureKey.NORTH_OVERLAY,
                ModTextureKey.SOUTH_OVERLAY, ModTextureKey.EAST_OVERLAY, ModTextureKey.WEST_OVERLAY, TextureKey.PARTICLE);
    }
}
