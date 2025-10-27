package com.westerosblocks;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.entity.client.ChairRenderer;
import com.westerosblocks.item.ModItems;
import com.westerosblocks.item.client.ModShieldRenderer;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class WesterosBlocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        applyRenderLayersFromDefinitions();

        // Chair Blocks
        EntityRendererRegistry.register(ModEntities.CHAIR, ChairRenderer::new);

        // flowerbed blocks
//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CLOVER, RenderLayer.getCutout());


        // Shields Azurelib
        AzItemRendererRegistry.register(ModItems.TARGARYEN_HEATER_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.TARGARYEN_HEATER_SHIELD).getGeoPath(),
                        (ModItems.TARGARYEN_HEATER_SHIELD).getTexPath()
                ));

        AzItemRendererRegistry.register(ModItems.BLACKFYRE_HEATER_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.BLACKFYRE_HEATER_SHIELD).getGeoPath(),
                        (ModItems.BLACKFYRE_HEATER_SHIELD).getTexPath()
                ));

        AzItemRendererRegistry.register(ModItems.BLACKWOOD_HEATER_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.BLACKWOOD_HEATER_SHIELD).getGeoPath(),
                        (ModItems.BLACKWOOD_HEATER_SHIELD).getTexPath()
                ));

        AzItemRendererRegistry.register(ModItems.BRACKEN_HEATER_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.BRACKEN_HEATER_SHIELD).getGeoPath(),
                        (ModItems.BRACKEN_HEATER_SHIELD).getTexPath()
                ));

        AzItemRendererRegistry.register(ModItems.TULLY_HEATER_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.TULLY_HEATER_SHIELD).getGeoPath(),
                        (ModItems.TULLY_HEATER_SHIELD).getTexPath()
                ));

        AzItemRendererRegistry.register(ModItems.HEDGE_KNIGHT_HEATER_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.HEDGE_KNIGHT_HEATER_SHIELD).getGeoPath(),
                        (ModItems.HEDGE_KNIGHT_HEATER_SHIELD).getTexPath()
                ));

        AzItemRendererRegistry.register(ModItems.LAUGHING_TREE_HEATER_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.LAUGHING_TREE_HEATER_SHIELD).getGeoPath(),
                        (ModItems.LAUGHING_TREE_HEATER_SHIELD).getTexPath()
                ));

        AzItemRendererRegistry.register(ModItems.GREYJOY_ROUND_SHIELD,
                () -> new ModShieldRenderer(
                        (ModItems.GREYJOY_ROUND_SHIELD).getGeoPath(),
                        (ModItems.GREYJOY_ROUND_SHIELD).getTexPath()
                ));
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
            } else if (definition.isAlphaRender()) {
                renderLayer = RenderLayer.getTranslucent();
            }


            if (renderLayer != null) {
                Block block = Registries.BLOCK.get(WesterosBlocks.id(definition.getBlockName()));
                BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);

                // For torch blocks, also apply render layer to wall variant
                if ("torch".equals(definition.getBlockType())) {
                    Block wallBlock = Registries.BLOCK.get(WesterosBlocks.id("wall_" + definition.getBlockName()));
                    BlockRenderLayerMap.INSTANCE.putBlock(wallBlock, renderLayer);
                }

                // For fan blocks, also apply render layer to wall variant
                if ("fan".equals(definition.getBlockType())) {
                    Block wallBlock = Registries.BLOCK.get(WesterosBlocks.id("wall_" + definition.getBlockName()));
                    BlockRenderLayerMap.INSTANCE.putBlock(wallBlock, renderLayer);
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
