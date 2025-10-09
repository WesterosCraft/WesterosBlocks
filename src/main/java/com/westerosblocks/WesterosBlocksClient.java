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
        // Chair Blocks
        EntityRendererRegistry.register(ModEntities.CHAIR, ChairRenderer::new);

        // flowerbed blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CLOVER, RenderLayer.getCutout());

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
            RenderLayer renderLayer = null;

            // First check for explicit renderLayer property
            if (definition.hasRenderLayer()) {
                renderLayer = getRenderLayerFromString(definition.getRenderLayer());
            }
            // If no renderLayer but alphaRender is true, use translucent
            else if (definition.isAlphaRender()) {
                renderLayer = RenderLayer.getTranslucent();
            }

            // Apply render layer if determined
            if (renderLayer != null) {
                Block block = Registries.BLOCK.get(WesterosBlocks.id(definition.getBlockName()));
                if (block != null) {
                    BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);

                    // For torch blocks, also apply render layer to wall variant
                    if ("torch".equals(definition.getBlockType())) {
                        Block wallBlock = Registries.BLOCK.get(WesterosBlocks.id("wall_" + definition.getBlockName()));
                        if (wallBlock != null) {
                            BlockRenderLayerMap.INSTANCE.putBlock(wallBlock, renderLayer);
                        }
                    }

                    // For fan blocks, also apply render layer to wall variant
                    if ("fan".equals(definition.getBlockType())) {
                        Block wallBlock = Registries.BLOCK.get(WesterosBlocks.id("wall_" + definition.getBlockName()));
                        if (wallBlock != null) {
                            BlockRenderLayerMap.INSTANCE.putBlock(wallBlock, renderLayer);
                        }
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
