package com.westerosblocks;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.entity.client.ChairRenderer;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class WesterosBlocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Pane Blocks
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DORNE_CARVED_STONE_WINDOW, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DORNE_CARVED_WOODEN_WINDOW, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_BARS, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_CROSSBAR, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OXIDIZED_IRON_BARS, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OXIDIZED_IRON_CROSSBAR, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VERTICAL_NET, RenderLayer.getCutout());
        
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
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BEES, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_ONE, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_TWO, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_THREE, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALYSSAS_TEARS_MIST_FOUR, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLACK_BRICICLE, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUSHEL_OF_HERBS, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUSHEL_OF_SOURLEAF, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_BLUE, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_ORANGE, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_RED, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_WHITE, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUTTERFLY_YELLOW, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHAIN_BLOCK_HARNESS, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHILI_RISTRA, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COBWEB, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_FISH, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_FOWL, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_FROG, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_HARE, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_JUNGLE_TALL_GRASS, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_RAT, RenderLayer.getCutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_SAVANNA_TALL_GRASS, RenderLayer.getCutout());

        // Fire Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SAFE_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILDFIRE, RenderLayer.getCutout());



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

        // Particle Emitter Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CASCADE_PARTICLE_EMITTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COSY_SMOKE_PARTICLE_EMITTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SIGNAL_SMOKE_PARTICLE_EMITTER, RenderLayer.getCutout());

        // Apply render layers from block definitions
        applyRenderLayersFromDefinitions();
    }

    private void applyRenderLayersFromDefinitions() {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            return;
        }

        for (BlockDefinition definition : registry.getAllDefinitions()) {
            if (definition.hasRenderLayer()) {
                Block block = Registries.BLOCK.get(WesterosBlocks.id(definition.getBlockName()));
                if (block != null) {
                    RenderLayer renderLayer = getRenderLayerFromString(definition.getRenderLayer());
                    if (renderLayer != null) {
                        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
                    }
                }
            }
        }
    }

    private RenderLayer getRenderLayerFromString(String renderLayerName) {
        return switch (renderLayerName.toLowerCase()) {
            case "cutout" -> RenderLayer.getCutout();
            case "translucent" -> RenderLayer.getTranslucent();
            case "cutout_mipped" -> RenderLayer.getCutoutMipped();
            default -> null;
        };
    }
}
