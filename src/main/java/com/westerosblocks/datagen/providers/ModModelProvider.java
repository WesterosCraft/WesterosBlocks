package com.westerosblocks.datagen.providers;

import com.westerosblocks.block.ModBlocks;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.datagen.custom.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;


import static com.westerosblocks.datagen.ModBlockStateModelGenerator.*;

public class ModModelProvider extends FabricModelProvider {

    private final FabricDataOutput output;

    public ModModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator bsmg) {
        generateModelsFromDefinitions(bsmg);

        // Table Blocks
        registerCustomTableBlock(bsmg, ModBlocks.OAK_TABLE)
                .texture("wood/oak/all").build();

        // Branch Blocks
        registerCustomBranchBlock(bsmg, ModBlocks.OAK_BRANCH)
                .texture("bark/oak/side")
                .build();
        registerCustomBranchBlock(bsmg, ModBlocks.BIRCH_BRANCH)
                .texture("bark/birch/side")
                .build();

        // Pane Blocks
        registerCustomPaneBlock(bsmg, ModBlocks.DORNE_CARVED_STONE_WINDOW)
                .texture("pane_block/moorish_stone_window_pane").build();
        registerCustomPaneBlock(bsmg, ModBlocks.DORNE_CARVED_WOODEN_WINDOW)
                .texture("pane_block/moorish_wood_window_pane").build();
        registerCustomPaneBlock(bsmg, ModBlocks.IRON_BARS)
                .texture("bars_iron_block/iron_bars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.IRON_CROSSBAR)
                .texture("bars_iron_block/bars_iron_crossbars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.OXIDIZED_IRON_BARS)
                .texture("bars_iron_block/bars_iron_oxidized").build();
        registerCustomPaneBlock(bsmg, ModBlocks.OXIDIZED_IRON_CROSSBAR)
                .texture("bars_iron_block/bars_iron_oxidized_crossbars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.VERTICAL_NET)
                .randomTexture("vertical_net/vertical_net1")
                .randomTexture("vertical_net/vertical_net2")
                .randomTexture("vertical_net/vertical_net3")
                .randomTexture("vertical_net/vertical_net4")
                .randomTexture("vertical_net/vertical_net5")
                .build();

        // Torch Blocks
        registerCustomTorchBlock(bsmg, ModBlocks.TORCH).texture("lighting/torch").build();
        registerCustomTorchBlock(bsmg, ModBlocks.TORCH_UNLIT).texture("lighting/torch_unlit").build();
        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE).texture("lighting/candle").build();
        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE_UNLIT).texture("lighting/candle_unlit").build();

        // Chair Blocks
        registerCustomChairBlock(bsmg, ModBlocks.OAK_CHAIR).texture("bark/oak/side").build();

        // Arrow Slit Blocks
        registerCustomArrowSlitBlock(bsmg, ModBlocks.ARBOR_BRICK_ARROW_SLIT).texture("ashlar_third/arbor/all").build();

        // Fan Blocks
        registerCustomFanBlock(bsmg, ModBlocks.CORAL_TUBE_FAN)
                .randomTexture("coral/tube/fan1")
                .randomTexture("coral/tube/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_BRAIN_FAN)
                .randomTexture("coral/brain/fan1")
                .randomTexture("coral/brain/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_BUBBLE_FAN)
                .randomTexture("coral/bubble/fan1")
                .randomTexture("coral/bubble/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_FIRE_FAN)
                .randomTexture("coral/fire/fan1")
                .randomTexture("coral/fire/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_HORN_FAN)
                .randomTexture("coral/horn/fan1")
                .randomTexture("coral/horn/fan2")
                .build();

        // Rail Blocks
        registerCustomRailBlock(bsmg, ModBlocks.FANCY_BLUE_CARPET).texture("carpet/fancy_blue_carpet").build();
        registerCustomRailBlock(bsmg, ModBlocks.FANCY_RED_CARPET).texture("carpet/fancy_red_carpet").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_CHAIN).textures("rail_block/chain", "rail_block/chain_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_NET).textures("rail_block/net_large", "rail_block/net_large_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_ROPE).textures("rail_block/rope", "rail_block/rope_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.PACKED_SNOW).textures("rail_block/packed_snow", "rail_block/packed_snow_turned").build();

        // Flowerbed Blocks
        registerCustomFlowerbedBlock(bsmg, ModBlocks.CLOVER)
                .stemTexture("flowerbed/clover_stem")
                .flowerTexture("flowerbed/clover")
                .build();


        // Crop Blocks
        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_CARROTS, "crop_carrots")
                .addState("age0", "carrots/carrots_stage_0")
                .addState("age1", "carrots/carrots_stage_1")
                .addState("age2", "carrots/carrots_stage_2")
                .addState("age3", "carrots/carrots_stage_3")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_TURNIPS, "crop_turnips")
                .addState("age0", "turnips/turnips_stage_0")
                .addState("age1", "turnips/turnips_stage_1")
                .addState("age2", "turnips/turnips_stage_2")
                .addState("age3", "turnips/turnips_stage_3")
                .isLayerSensitive()
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_PEAS, "crop_peas")
                .addState("age0", "peas/peas_stage_0")
                .addState("age1", "peas/peas_stage_1")
                .addState("age2", "peas/peas_stage_2")
                .isLayerSensitive()
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CANDLE_ALTAR, "candle_altar")
                .addStateRandomTextures("lit", "lighting/candle_altar/lit1", "lighting/candle_altar/lit2", "lighting/candle_altar/lit3")
                .addStateRandomTextures("unlit", "lighting/candle_altar/unlit1", "lighting/candle_altar/unlit2", "lighting/candle_altar/unlit3")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_WHEAT, "crop_wheat")
                .isLayerSensitive()
                .addState("age0", "wheat/wheat_stage_0")
                .addState("age1", "wheat/wheat_stage_1")
                .addState("age2", "wheat/wheat_stage_2")
                .addState("age3", "wheat/wheat_stage_3")
                .addStateRandomTextures("age4", "wheat/stage4_1", "wheat/stage4_2")
                .addStateRandomTextures("age5", "wheat/stage5_1", "wheat/stage5_2")
                .addStateRandomTextures("age6", "wheat/stage6_1", "wheat/stage6_2")
                .addStateRandomTextures("age7", "wheat/stage7_1", "wheat/stage7_2", "wheat/stage7_3", "wheat/stage7_4")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.SEAGRASS, "seagrass")
                .addRandomTexture("seagrass/side1")
                .addRandomTexture("seagrass/side2")
                .addRandomTexture("seagrass/side3")
                .addRandomTexture("seagrass/side4")
                .isLayerSensitive()
                .build();

        // Bed Blocks
        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.ITCHY_STRAW_BED, "itchy_straw_bed")
                .bedType("normal")
                .texture("bed_block/bed_straw_itchy_0")
                .texture("bed_block/bed_straw_itchy_1")
                .texture("bed_block/bed_straw_itchy_2")
                .texture("bed_block/bed_straw_itchy_3")
                .texture("bed_block/bed_straw_itchy_4")
                .texture("bed_block/bed_straw_itchy_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.HAMMOCK, "hammock")
                .bedType("hammock")
                .texture("bed_block/bed_hammock_0")
                .texture("bed_block/bed_hammock_1")
                .texture("bed_block/bed_hammock_2")
                .texture("bed_block/bed_hammock_3")
                .texture("bed_block/bed_hammock_4")
                .texture("bed_block/bed_hammock_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NIGHTS_WATCH_BED, "nights_watch_bed")
                .bedType("normal")
                .texture("bed_block/bed_night_watch_0")
                .texture("bed_block/bed_night_watch_1")
                .texture("bed_block/bed_night_watch_2")
                .texture("bed_block/bed_night_watch_3")
                .texture("bed_block/bed_night_watch_4")
                .texture("bed_block/bed_night_watch_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_BLUE_BED, "noble_blue_bed")
                .bedType("raised")
                .texture("bed_block/bed_noble_blue_0")
                .texture("bed_block/bed_noble_blue_1")
                .texture("bed_block/bed_noble_blue_2")
                .texture("bed_block/bed_noble_blue_3")
                .texture("bed_block/bed_noble_blue_4")
                .texture("bed_block/bed_noble_blue_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_RED_BED, "noble_red_bed")
                .bedType("raised")
                .texture("bed_block/bed_noble_red_0")
                .texture("bed_block/bed_noble_red_1")
                .texture("bed_block/bed_noble_red_2")
                .texture("bed_block/bed_noble_red_3")
                .texture("bed_block/bed_noble_red_4")
                .texture("bed_block/bed_noble_red_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NORTHERN_BED, "northern_bed")
                .bedType("normal")
                .texture("bed_block/bed_north_0")
                .texture("bed_block/bed_north_1")
                .texture("bed_block/bed_north_2")
                .texture("bed_block/bed_north_3")
                .texture("bed_block/bed_north_4")
                .texture("bed_block/bed_north_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_GREEN_BED, "pale_green_bed")
                .bedType("normal")
                .texture("bed_block/bed_patchy_green_0")
                .texture("bed_block/bed_patchy_green_1")
                .texture("bed_block/bed_patchy_green_2")
                .texture("bed_block/bed_patchy_green_3")
                .texture("bed_block/bed_patchy_green_4")
                .texture("bed_block/bed_patchy_green_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_RED_BED, "pale_red_bed")
                .bedType("normal")
                .texture("bed_block/bed_patchy_red_0")
                .texture("bed_block/bed_patchy_red_1")
                .texture("bed_block/bed_patchy_red_2")
                .texture("bed_block/bed_patchy_red_3")
                .texture("bed_block/bed_patchy_red_4")
                .texture("bed_block/bed_patchy_red_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.STRAW_BED, "straw_bed")
                .bedType("normal")
                .texture("bed_block/bed_straw_0")
                .texture("bed_block/bed_straw_1")
                .texture("bed_block/bed_straw_2")
                .texture("bed_block/bed_straw_3")
                .texture("bed_block/bed_straw_4")
                .texture("bed_block/bed_straw_5")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.APPLE_FRUIT_LEAVES, "apple_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/apple0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/apple1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/apple2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/apple3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.APRICOT_FRUIT_LEAVES, "apricot_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/apricot0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/apricot1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/apricot2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/apricot3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.BLACKBERRY_BUSH, "blackberry_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/blackberry0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/blackberry1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/blackberry2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/blackberry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.BLUEBERRY_BUSH, "blueberry_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/blueberry0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/blueberry1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/blueberry2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/blueberry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.CHERRY_FRUIT_LEAVES, "cherry_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/cherry0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/cherry1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/cherry2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/cherry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.HOP_FRUIT_LEAVES, "hop_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/hop0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/hop1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/hop2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/hop3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.JUNIPER_BUSH, "juniper_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/spruce/all", "transparent", "leaves/overlay/juniper0")
                .addRandomTextureSet(10, "leaves/spruce/all", "transparent", "leaves/overlay/juniper1")
                .addRandomTextureSet(10, "leaves/spruce/all", "transparent", "leaves/overlay/juniper2")
                .addRandomTextureSet(2, "leaves/spruce/all", "transparent", "leaves/overlay/juniper3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.LEMON_FRUIT_LEAVES, "lemon_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/lemon0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/lemon1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/lemon2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/lemon3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.LIME_FRUIT_LEAVES, "lime_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/lime0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/lime1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/lime2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/lime3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.OLIVE_FRUIT_LEAVES, "olive_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/olive0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/olive1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/olive2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/olive3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.ORANGE_FRUIT_LEAVES, "orange_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/orange0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/orange1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/orange2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/orange3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PALM_LEAVES, "palm_leaves")
                .betterFoliage()
                .rotateRandom()
                .isTinted()
                .textures("leaves/palm/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PEACH_FRUIT_LEAVES, "peach_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/peach0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/peach1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/peach2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/peach3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PINK_ROSE_BUSH, "pink_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PLUM_FRUIT_LEAVES, "plum_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/plum0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/plum1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/plum2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/plum3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.POMEGRANATE_FRUIT_LEAVES, "pomegranate_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PURPLE_GRAPE_FRUIT_LEAVES, "purple_grape_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.RASPBERRY_BUSH, "raspberry_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.RED_ROSE_BUSH, "red_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/red_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/red_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/red_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/red_rose3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.SNOWY_SPRUCE_LEAVES, "snowy_spruce_leaves")
                .betterFoliage()
                .rotateRandom()
                .textures("leaves/spruce/snowy/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.SNOWY_WEIRWOOD_LEAVES, "snowy_weirwood_leaves")
                .betterFoliage()
                .rotateRandom()
                .textures("leaves/weirwood/snowy/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.WEIRWOOD_LEAVES, "weirwood_leaves")
                .betterFoliage()
                .rotateRandom()
                .textures("leaves/weirwood/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.WHITE_GRAPE_FRUIT_LEAVES, "white_grape_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.WHITE_ROSE_BUSH, "white_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/white_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/white_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/white_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/white_rose3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.YELLOW_ROSE_BUSH, "yellow_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose3")
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.DAPPLED_MOSS, "dappled_moss")
                .isTinted()
                .textures("dappled_moss/dappled", "dappled_moss/dappled")
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_ONE, "falling_water_block_one")
                .textures("alyssas_tears_mist/mist1", "alyssas_tears_mist/mist1")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_TWO, "falling_water_block_two")
                .textures("alyssas_tears_mist/mist2", "alyssas_tears_mist/mist2")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_THREE, "falling_water_block_three")
                .textures("alyssas_tears_mist/mist3", "alyssas_tears_mist/mist3")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_FOUR, "falling_water_block_four")
                .textures("alyssas_tears_mist/mist4", "alyssas_tears_mist/mist4")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.JASMINE_VINES, "jasmine_vines")
                .addRandomTextureSet(1, "jasmine_vines/side1", "jasmine_vines/side1")
                .addRandomTextureSet(1, "jasmine_vines/side2", "jasmine_vines/side2")
                .addRandomTextureSet(1, "jasmine_vines/side3", "jasmine_vines/side3")
                .addRandomTextureSet(1, "jasmine_vines/side4", "jasmine_vines/side4")
                .addRandomTextureSet(1, "jasmine_vines/side5", "jasmine_vines/side5")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.VINES, "vines")
                .addRandomTextureSet(1, "vines/side1", "vines/side1")
                .addRandomTextureSet(1, "vines/side2", "vines/side2")
                .addRandomTextureSet(1, "vines/side3", "vines/side3")
                .addRandomTextureSet(1, "vines/side4", "vines/side4")
                .addRandomTextureSet(1, "vines/side5", "vines/side5")
                .isTinted()
                .build();

        // ladder blocks
//        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.IRON_RUNGS, "iron_rungs")
//                .isCustom()
//                .texture("iron_rungs/ladder")
//                .build();
//
//        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.IRON_RUNGS_BROKEN, "iron_rungs_broken")
//                .isCustom()
//                .addRandomTextureSet("")
//                .addRandomTextureSet("")
//                .addRandomTextureSet("")
//                .addRandomTextureSet("")
//                .addRandomTextureSet("")
//                .addRandomTextureSet("")
//                .build();
//
//        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.ROPE_LADDER, "rope_ladder")
//                .texture("rope_ladder/side")
//                .build();
//
//
//        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.VINE_JASMINE, "vine_jasmine")
//                .addRandomTextureSet("jasmine_vines/side1")
//                .addRandomTextureSet("jasmine_vines/side2")
//                .addRandomTextureSet("jasmine_vines/side3")
//                .addRandomTextureSet("jasmine_vines/side4")
//                .addRandomTextureSet("jasmine_vines/side5")
//                .build();
//
//        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.WINTERFELL_STONE_LADDER, "winterfell_stone_ladder")
//                .addRandomTextureSet("winterfell_stone_ladder/side1")
//                .addRandomTextureSet("winterfell_stone_ladder/side2")
//                .build();
//
//        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.WOOD_LADDER, "wood_ladder")
//                .isCustom()
//                .texture("wood_ladder/side")
//                .build();

        // Fence Blocks
        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.BIRCH_BARK_FENCE, "birch_bark_fence")
                .texture("bark/birch/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.BIRCH_FENCE_WITH_GRAPES, "birch_fence_with_grapes")
                .texture("wood/birch/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.BIRCH_FENCE_WITH_VINES, "birch_fence_with_vines")
                .texture("wood/birch/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.JUNGLE_BARK_FENCE, "jungle_bark_fence")
                .texture("bark/jungle/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.JUNGLE_FENCE_WITH_GRAPES, "jungle_fence_with_grapes")
                .texture("wood/jungle/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.JUNGLE_FENCE_WITH_VINES, "jungle_fence_with_vines")
                .texture("wood/jungle/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.MARBLE_COLUMN_FENCE, "marble_column_fence")
                .texture("marble/quartz/column_side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.OAK_BARK_FENCE, "oak_bark_fence")
                .texture("bark/oak/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.OAK_FENCE_WITH_GRAPES, "oak_fence_with_grapes")
                .texture("wood/oak/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.OAK_FENCE_WITH_VINES, "oak_fence_with_vines")
                .texture("wood/oak/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.PALM_FENCE, "palm_fence")
                .texture("bark/palm/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.REINFORCED_OAK_FENCE, "reinforced_oak_fence")
                .texture("wood/oak/reinforced")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SEPT_CRYSTAL_SMALL, "sept_crystal_small")
                .textures("crystal/fence_top", "crystal/fence_top", "crystal/fence")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SPRUCE_BARK_FENCE, "spruce_bark_fence")
                .texture("bark/spruce/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SPRUCE_FENCE_WITH_GRAPES, "spruce_fence_with_grapes")
                .texture("wood/spruce/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SPRUCE_FENCE_WITH_VINES, "spruce_fence_with_vines")
                .texture("wood/spruce/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.STACKED_BONES_FENCE, "stacked_bones_fence")
                .texture("stacked_bones/bone_stacked_side")
                .build();

        // Fence Gate Blocks
        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_BIRCH_BARK_FENCE_GATE, "locked_birch_bark_fence_gate")
                .texture("bark/birch/side")
                .build();

        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_JUNGLE_BARK_FENCE_GATE, "locked_jungle_bark_fence_gate")
                .texture("bark/jungle/side")
                .build();

        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_OAK_BARK_FENCE_GATE, "locked_oak_bark_fence_gate")
                .texture("bark/oak/side")
                .build();

        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_SPRUCE_BARK_FENCE_GATE, "locked_spruce_bark_fence_gate")
                .texture("bark/spruce/side")
                .build();

        // Particle Emitter Blocks
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.CASCADE_PARTICLE_EMITTER, "cascade_particle_emitter");
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.COSY_SMOKE_PARTICLE_EMITTER, "cosy_smoke_particle_emitter");
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.SIGNAL_SMOKE_PARTICLE_EMITTER, "signal_smoke_particle_emitter");
    }

    private void generateModelsFromDefinitions(BlockStateModelGenerator bsmg) {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            return;
        }

        // Generate solid block models
        for (BlockDefinition definition : registry.getByType("solid")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                SolidBlockExporter.registerCustomSolidBlock(bsmg, block, definition);
            }
        }

        // Generate door block models
        for (BlockDefinition definition : registry.getByType("door")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                DoorBlockExporter.registerCustomDoorBlock(bsmg, block, definition);
            }
        }

        // Generate log block models
        for (BlockDefinition definition : registry.getByType("log")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                LogBlockExporter.registerCustomLogBlock(bsmg, block, definition);
            }
        }

        // Generate plant block models
        for (BlockDefinition definition : registry.getByType("plant")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                PlantBlockExporter.registerCustomPlantBlock(bsmg, block, definition);
            }
        }

        // Generate flowerpot block models
        for (BlockDefinition definition : registry.getByType("flowerpot")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                FlowerPotBlockExporter.registerCustomFlowerPotBlock(bsmg, block, definition);
            }
        }

        // Generate web block models
        for (BlockDefinition definition : registry.getByType("web")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                CrossBlockExporter.registerCrossBlockFromDefinition(bsmg, block, definition);
            }
        }

        // Generate slab block models
        for (BlockDefinition definition : registry.getByType("slab")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                SlabBlockExporter.registerCustomSlabBlock(bsmg, block, definition);
            }
        }

        // Generate halfdoor block models
        for (BlockDefinition definition : registry.getByType("halfdoor")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                HalfDoorBlockExporter.registerCustomHalfDoorBlock(bsmg, block, definition);
            }
        }

        // Generate fire block models
        for (BlockDefinition definition : registry.getByType("fire")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                FireBlockDatagen.registerCustomFireBlock(bsmg, block, definition);
            }
        }

        // Generate ladder block models
        for (BlockDefinition definition : registry.getByType("ladder")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                LadderBlockDatagen.registerCustomLadderBlock(bsmg, block, definition);
            }
        }

        // TODO: Add other block types as needed
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models are now handled automatically by the block exporters
    }
}
