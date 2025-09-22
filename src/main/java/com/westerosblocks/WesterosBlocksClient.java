package com.westerosblocks;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.PlantBlocks;
import com.westerosblocks.block.SolidBlocks;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.entity.client.ChairRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class WesterosBlocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(SolidBlocks.CAGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SolidBlocks.FISH_TRAP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SolidBlocks.SEPT_CRYSTAL_LARGE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SolidBlocks.COLOURED_SEPT_WINDOW, RenderLayer.getTranslucent());
        
        // Pane Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DORNE_CARVED_STONE_WINDOW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DORNE_CARVED_WOODEN_WINDOW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_BARS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_CROSSBAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OXIDIZED_IRON_BARS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OXIDIZED_IRON_CROSSBAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VERTICAL_NET, RenderLayer.getCutout());
        
        // Torch Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TORCH_UNLIT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_TORCH_UNLIT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CANDLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_CANDLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CANDLE_UNLIT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_CANDLE_UNLIT, RenderLayer.getCutout());
        
        // Rail Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FANCY_BLUE_CARPET, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FANCY_RED_CARPET, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HORIZONTAL_CHAIN, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HORIZONTAL_NET, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HORIZONTAL_ROPE, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PACKED_SNOW, RenderLayer.getCutoutMipped());

        // Chair Blocks
        EntityRendererRegistry.register(ModEntities.CHAIR, ChairRenderer::new);

        // Bed Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOBLE_BLUE_BED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOBLE_RED_BED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HAMMOCK, RenderLayer.getCutout());

        // Fan Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORAL_TUBE_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_CORAL_TUBE_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORAL_BRAIN_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_CORAL_BRAIN_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORAL_BUBBLE_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_CORAL_BUBBLE_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORAL_FIRE_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_CORAL_FIRE_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORAL_HORN_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_CORAL_HORN_FAN, RenderLayer.getCutout());

        // Web Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BEES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_ONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_TWO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_THREE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_FOUR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLACK_BRICICLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUSHEL_OF_HERBS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUSHEL_OF_SOURLEAF, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_BLUE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_ORANGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_RED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_WHITE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_YELLOW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHAIN_BLOCK_HARNESS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHILI_RISTRA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COBWEB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_FISH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_FOWL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_FROG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_HARE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_JUNGLE_TALL_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_RAT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_SAVANNA_TALL_GRASS, RenderLayer.getCutout());

        // Fire Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SAFE_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILDFIRE, RenderLayer.getCutout());

        // Plant Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.CATTAILS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BLUE_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BLUE_CHICORY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BLUE_FORGETMENOTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BLUE_FLAX, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BLUE_HYACINTH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BLUE_ORCHID, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BLUE_SWAMP_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BRACKEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_1, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_2, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_3, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_4, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_5, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_6, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_7, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_8, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_9, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_10, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_11, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_12, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.BROWN_MUSHROOM_13, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.CORAL_BRAIN_WEB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.CORAL_BUBBLE_WEB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.CORAL_FIRE_WEB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.CORAL_HORN_WEB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.CORAL_TUBE_WEB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.COW_PARSELY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.CRANBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.DEAD_BRACKEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.DEAD_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.DEAD_SCRUB_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.DOCK_LEAF, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.FIREWEED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.GREEN_LEAFY_HERB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.GREEN_SCRUB_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.GREEN_SPINY_HERB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.HEATHER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.KELP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.LADY_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.MAGENTA_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.MEADOW_FESCUE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.NETTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.ORANGE_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.ORANGE_BOG_ASPHODEL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.ORANGE_MARIGOLDS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.ORANGE_SUN_STAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.ORANGE_TROLLIUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.PINK_ALLIUM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.PINK_PRIMROSE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.PINK_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.PINK_SWEET_PEAS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.PINK_THISTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.PINK_TULIPS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.PINK_WILDFLOWERS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_ASTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_CARNATIONS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_CHRYSANTHEMUM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_DARK_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_FLOWERING_SPINY_HERB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_1, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_2, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_3, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_4, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_5, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_6, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_7, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_8, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_MUSHROOM_9, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_POPPIES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_SORREL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_SOURLEAF_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.RED_TULIPS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.STRAWBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.THICK_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.UNSHADED_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.WHITE_CHAMOMILE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.WHITE_DAISIES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.WHITE_LILYOFTHEVALLEY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.WHITE_PEONY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.WHITE_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_BEDSTRAW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_BUTTERCUPS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_DAFFODILS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_DAISIES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_DANDELIONS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_HELLEBORE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_LUPINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_RUDBECKIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_SUNFLOWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_TANSY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.YELLOW_WILDFLOWERS, RenderLayer.getCutout());

        // flowerbed blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CLOVER, RenderLayer.getCutout());

        // crop blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CROP_CARROTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SEAGRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CANDLE_ALTAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CROP_PEAS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CROP_TURNIPS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CROP_WHEAT, RenderLayer.getCutout());

        // vines blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DAPPLED_MOSS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JASMINE_VINES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VINES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FALLING_WATER_BLOCK_ONE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FALLING_WATER_BLOCK_TWO, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FALLING_WATER_BLOCK_THREE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FALLING_WATER_BLOCK_FOUR, RenderLayer.getTranslucent());

        // ladder blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_RUNGS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_RUNGS_BROKEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROPE_LADDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VINE_JASMINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WINTERFELL_STONE_LADDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOOD_LADDER, RenderLayer.getCutout());

        // potted blocks
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BLUE_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BLUE_CHICORY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BLUE_FLAX, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BLUE_FORGETMENOTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BLUE_HYACINTH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BLUE_ORCHID, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BLUE_SWAMP_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BRACKEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BROWN_MUSHROOM_1, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BROWN_MUSHROOM_3, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BROWN_MUSHROOM_6, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_BROWN_MUSHROOM_13, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_CATTAILS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_COW_PARSELY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_DEAD_BRACKEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_DEAD_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_DEAD_SCRUB_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_DOCK_LEAF, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_FIREWEED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_GREEN_LEAFY_HERB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_GREEN_SCRUB_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_GREEN_SPINY_HERB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_HEATHER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_LADY_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_MAGENTA_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_MEADOW_FESCUE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_NETTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_ORANGE_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_ORANGE_BOG_ASPHODEL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_ORANGE_MARIGOLDS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_ORANGE_SUN_STAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_ORANGE_TROLLIUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PINK_ALLIUM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PINK_PRIMROSE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PINK_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PINK_SWEET_PEAS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PINK_THISTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PINK_TULIPS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PINK_WILDFLOWERS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PURPLE_ALPINE_SOWTHISTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PURPLE_FOXGLOVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PURPLE_LAVENDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PURPLE_PANSIES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PURPLE_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_PURPLE_VIOLETS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_ASTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_CARNATIONS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_CHRYSANTHEMUM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_DARK_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_FLOWERING_SPINY_HERB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_MUSHROOM_1, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_MUSHROOM_2, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_MUSHROOM_3, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_MUSHROOM_7, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_MUSHROOM_8, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_MUSHROOM_9, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_POPPIES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_SORREL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_SOURLEAF_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_RED_TULIPS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_WHITE_CHAMOMILE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_WHITE_DAISIES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_WHITE_LILYOFTHEVALLEY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_WHITE_PEONY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_WHITE_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_BEDSTRAW, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_BELLS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_BUTTERCUPS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_DAFFODILS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_DAISIES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_DANDELIONS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_HELLEBORE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_LUPINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_ROSES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_RUDBECKIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_SUNFLOWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_TANSY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PlantBlocks.POTTED_YELLOW_WILDFLOWERS, RenderLayer.getCutout());

        // Particle Emitter Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CASCADE_PARTICLE_EMITTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COSY_SMOKE_PARTICLE_EMITTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SIGNAL_SMOKE_PARTICLE_EMITTER, RenderLayer.getCutout());
    }
}
