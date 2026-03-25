package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Exporter for furnace blocks following block-models.md patterns.
 * Generates models for directional furnace blocks with lit/unlit states.
 */
public class FurnaceBlockExporter extends BaseBlockExporter {

    private static Model createFurnaceModel(boolean tinted) {
        return createTintedModel(tinted, "orientable", TextureKey.TOP, TextureKey.FRONT, TextureKey.SIDE);
    }

    private static TextureMap createFurnaceTextureMap(String top, String side, String front) {
        return new TextureMap()
                .put(TextureKey.TOP, createBlockIdentifier(top))
                .put(TextureKey.SIDE, createBlockIdentifier(side))
                .put(TextureKey.FRONT, createBlockIdentifier(front));
    }

    private static VariantsBlockStateSupplier createFurnaceBlockstate(Block block, Identifier litModelId,
                                                                      Identifier unlitModelId) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.LIT)
                        // Unlit states
                        .register(Direction.NORTH, false, createVariant(unlitModelId, getRotationForDirection(Direction.NORTH)))
                        .register(Direction.SOUTH, false, createVariant(unlitModelId, getRotationForDirection(Direction.SOUTH)))
                        .register(Direction.WEST, false, createVariant(unlitModelId, getRotationForDirection(Direction.WEST)))
                        .register(Direction.EAST, false, createVariant(unlitModelId, getRotationForDirection(Direction.EAST)))
                        // Lit states
                        .register(Direction.NORTH, true, createVariant(litModelId, getRotationForDirection(Direction.NORTH)))
                        .register(Direction.SOUTH, true, createVariant(litModelId, getRotationForDirection(Direction.SOUTH)))
                        .register(Direction.WEST, true, createVariant(litModelId, getRotationForDirection(Direction.WEST)))
                        .register(Direction.EAST, true, createVariant(litModelId, getRotationForDirection(Direction.EAST)))
                );
    }


    public static void registerFurnaceBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                           String top, String side, String frontLit, String frontUnlit) {
        // Create texture maps for lit and unlit states
        TextureMap litTextureMap = createFurnaceTextureMap(top, side, frontLit);
        TextureMap unlitTextureMap = createFurnaceTextureMap(top, side, frontUnlit);

        // Upload lit and unlit models
        Identifier litModelId = uploadModel(createFurnaceModel(tinted), block, "lit", litTextureMap, generator.modelCollector);
        Identifier unlitModelId = uploadModel(createFurnaceModel(tinted), block, "base", unlitTextureMap, generator.modelCollector);

        // Create blockstate
        VariantsBlockStateSupplier blockstate = createFurnaceBlockstate(block, litModelId, unlitModelId);
        generator.blockStateCollector.accept(blockstate);

        // Register item model using unlit state
        registerParentedItemModel(generator, block, unlitModelId);
    }

    /**
     * Registers a furnace block using custom model references (no model generation).
     * For furnaces with pre-existing custom models.
     */
    public static void registerFurnaceBlockCustomModel(BlockStateModelGenerator generator, Block block, String blockName) {
        // Reference existing custom models
        Identifier litModelId = WesterosBlocks.id("block/custom/" + blockName + "/lit_v1");
        Identifier unlitModelId = WesterosBlocks.id("block/custom/" + blockName + "/base_v1");

        // Create blockstate
        VariantsBlockStateSupplier blockstate = createFurnaceBlockstate(block, litModelId, unlitModelId);
        generator.blockStateCollector.accept(blockstate);

        // Register item model using unlit state
        generator.registerParentedItemModel(block, unlitModelId);
    }


    public static void registerCustomFurnaceBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean isCustomModel = definition.hasCustomModel();

        // If custom model, just reference existing models without generating
        if (isCustomModel) {
            registerFurnaceBlockCustomModel(generator, block, definition.getBlockName());
            return;
        }

        // Furnace needs 4 textures: top, side, frontLit, frontUnlit
        if (definition.getTextures() != null && definition.getTextures().size() >= 4) {
            registerFurnaceBlock(generator, block, tinted,
                    definition.getTextures().get(0),
                    definition.getTextures().get(1),
                    definition.getTextures().get(2),
                    definition.getTextures().get(3));
        } else if (definition.getTextures() != null && definition.getTextures().size() >= 1) {
            // Fallback: use first texture for all sides
            String fallback = definition.getTextures().get(0);
            registerFurnaceBlock(generator, block, tinted, fallback, fallback, fallback, fallback);
            WesterosBlocks.LOGGER.warn("Furnace block '{}' has insufficient textures ({}), using fallback",
                    definition.getBlockName(), definition.getTextures().size());
        } else {
            registerFurnaceBlock(generator, block, tinted, "missingno", "missingno", "missingno", "missingno");
        }
    }
}
