package com.westerosblocks;

import com.westerosblocks.block.blockentity.ModBlockEntities;
import com.westerosblocks.block.blockentity.client.WCBigDoorBlockEntityRenderer;
import com.westerosblocks.block.blockentity.custom.WCBigDoorBlockEntity;
import com.westerosblocks.client.ParticleEmitterHighlighter;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.entity.client.ChairRenderer;
import com.westerosblocks.entity.client.RopeRenderer;
import com.westerosblocks.item.ModItems;
import com.westerosblocks.item.client.ModShieldRenderer;
import com.westerosblocks.item.custom.ModShieldItem;
import mod.azure.azurelib.common.render.item.AzItemRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

import java.util.List;

public class WesterosBlocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        applyRenderLayersFromDefinitions();

        // Chair Blocks
        EntityRendererRegistry.register(ModEntities.CHAIR, ChairRenderer::new);
        EntityRendererRegistry.register(ModEntities.ROPE_ENTITY, RopeRenderer::new);

        // Highlight particle emitter blocks (invisible) when held in creative
        ParticleEmitterHighlighter.register();

        // Shields Azurelib
        for (ModShieldItem shield : List.of(
                ModItems.TARGARYEN_HEATER_SHIELD, ModItems.BLACKFYRE_HEATER_SHIELD,
                ModItems.BLACKWOOD_HEATER_SHIELD, ModItems.BRACKEN_HEATER_SHIELD,
                ModItems.TULLY_HEATER_SHIELD, ModItems.HEDGE_KNIGHT_HEATER_SHIELD,
                ModItems.LAUGHING_TREE_HEATER_SHIELD, ModItems.GREYJOY_ROUND_SHIELD)) {
            registerShieldRenderer(shield);
        }

        registerBigDoorRenderers();
    }

    private static void registerShieldRenderer(ModShieldItem shield) {
        AzItemRendererRegistry.register(shield,
                () -> new ModShieldRenderer(shield.getGeoPath(), shield.getTexPath()));
    }

    @SuppressWarnings("unchecked")
    private void registerBigDoorRenderers() {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();
        if (!registry.isInitialized()) {
            return;
        }

        for (BlockDefinition definition : registry.getByType("bigdoor")) {
            BlockEntityType<?> type = ModBlockEntities.getBlockEntityType(definition.getBlockName());
            if (type != null) {
                BlockEntityRendererFactories.register(
                        (BlockEntityType<WCBigDoorBlockEntity>) type,
                        WCBigDoorBlockEntityRenderer::new
                );
            }
        }
    }

    private void applyRenderLayersFromDefinitions() {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            return;
        }

        for (BlockDefinition definition : registry.getAllDefinitions()) {
            RenderLayer renderLayer = null;

            if (definition.hasRenderLayer()) {
                renderLayer = getRenderLayerFromString(definition.getRenderLayer());
            } else if (definition.hasOverlayTextures()) {
                renderLayer = RenderLayer.getCutout();
            } else if (definition.isAlphaRender()) {
                renderLayer = RenderLayer.getTranslucent();
            }


            if (renderLayer != null) {
                RenderLayer layer = renderLayer;
                Identifier blockId = WesterosBlocks.id(definition.getBlockName());
                Registries.BLOCK.getOrEmpty(blockId)
                        .ifPresent(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, layer));

                // For torch and fan blocks, also apply render layer to wall variant
                if ("torch".equals(definition.getBlockType()) || "fan".equals(definition.getBlockType())) {
                    Identifier wallId = WesterosBlocks.id("wall_" + definition.getBlockName());
                    Registries.BLOCK.getOrEmpty(wallId)
                            .ifPresent(wallBlock -> BlockRenderLayerMap.INSTANCE.putBlock(wallBlock, layer));
                }

                // For bunting blocks, also apply render layer to ceiling variant
                if ("bunting".equals(definition.getBlockType())) {
                    Identifier ceilingId = WesterosBlocks.id(definition.getBlockName() + "_ceiling");
                    Registries.BLOCK.getOrEmpty(ceilingId)
                            .ifPresent(ceilingBlock -> BlockRenderLayerMap.INSTANCE.putBlock(ceilingBlock, layer));
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
