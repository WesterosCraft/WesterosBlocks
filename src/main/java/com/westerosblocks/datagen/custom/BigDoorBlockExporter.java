package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBigDoorBlock;
import com.westerosblocks.block.custom.WCBigDoorBlock.BigDoorPart;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Exporter for generating blockstate and model files for big door (3x3 multiblock) blocks.
 * Generates 72 variants: 9 parts x 2 open states x 4 facings
 */
public class BigDoorBlockExporter extends BaseBlockExporter {

    public static void registerCustomBigDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] textures = definition.getTexturesAsArray();
        validateTexturePaths(textures, 1);

        String texture = textures[0];
        boolean isCustomModel = definition.hasCustomModel();

        if (isCustomModel) {
            registerCustomModelBigDoor(generator, block, definition);
        } else {
            registerGeneratedBigDoor(generator, block, texture);
        }
    }

    /**
     * Registers a big door with custom pre-made models
     */
    private static void registerCustomModelBigDoor(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String blockName = getBlockName(block);

        BlockStateVariantMap.TripleProperty<WeightedVariant, Direction, Boolean, BigDoorPart> variants =
                BlockStateVariantMap.models(WCBigDoorBlock.FACING, WCBigDoorBlock.OPEN, WCBigDoorBlock.PART);

        for (Direction facing : Direction.Type.HORIZONTAL) {
            for (boolean open : new boolean[]{false, true}) {
                for (BigDoorPart part : BigDoorPart.values()) {
                    String openState = open ? "open" : "closed";
                    String modelPath = "block/custom/bigdoor/" + blockName + "/" + part.asString() + "_" + openState;
                    Identifier modelId = WesterosBlocks.id(modelPath);
                    int rotation = getRotationForDirection(facing);

                    variants.register(facing, open, part, createVariant(modelId, rotation));
                }
            }
        }

        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block).with(variants));

        // Register item model
        Identifier itemModelId = WesterosBlocks.id("block/custom/bigdoor/" + blockName + "/bottom_center_closed");
        registerParentedItemModel(generator, block, itemModelId);
    }

    /**
     * Registers a big door with auto-generated models
     */
    private static void registerGeneratedBigDoor(BlockStateModelGenerator generator, Block block, String texture) {
        String blockName = getBlockName(block);
        Identifier textureId = createBlockIdentifier(texture);

        // Generate models for each part and state
        for (BigDoorPart part : BigDoorPart.values()) {
            for (boolean open : new boolean[]{false, true}) {
                String openState = open ? "open" : "closed";
                String variant = part.asString() + "_" + openState;

                // Select the appropriate parent model based on part and state
                Model model = getModelForPart(part, open);

                TextureMap textureMap = new TextureMap()
                        .put(TextureKey.ALL, textureId)
                        .put(TextureKey.TEXTURE, textureId)
                        .put(TextureKey.PARTICLE, textureId);

                uploadModel(model, block, variant, textureMap, generator.modelCollector);
            }
        }

        // Create blockstate variants
        BlockStateVariantMap.TripleProperty<WeightedVariant, Direction, Boolean, BigDoorPart> variants =
                BlockStateVariantMap.models(WCBigDoorBlock.FACING, WCBigDoorBlock.OPEN, WCBigDoorBlock.PART);

        for (Direction facing : Direction.Type.HORIZONTAL) {
            for (boolean open : new boolean[]{false, true}) {
                for (BigDoorPart part : BigDoorPart.values()) {
                    String openState = open ? "open" : "closed";
                    String variant = part.asString() + "_" + openState;
                    Identifier modelId = createNestedModelId(block, variant);
                    int rotation = getRotationForDirection(facing);

                    variants.register(facing, open, part, createVariant(modelId, rotation));
                }
            }
        }

        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block).with(variants));

        // Register item model using bottom_center_closed
        Identifier itemModelId = createNestedModelId(block, "bottom_center_closed");
        registerParentedItemModel(generator, block, itemModelId);
    }

    /**
     * Returns the appropriate model for a door part and open state.
     * These are simple cube section models that will be combined in the blockstate.
     */
    private static Model getModelForPart(BigDoorPart part, boolean open) {
        // All parts use the same base model type but with different textures
        // The actual geometry differences are handled by the parent model

        String parentPath;
        if (!open) {
            // Closed: all parts use a thin wall slice
            parentPath = "block/bigdoor/closed_" + getColumnType(part);
        } else {
            // Open: center is empty, sides have swung panels
            // Left column gets open_right (door swings to outer edge)
            // Right column gets open_left (door swings to outer edge)
            if (part.isCenterColumn()) {
                parentPath = "block/bigdoor/open_center";
            } else if (part.isLeftColumn()) {
                parentPath = "block/bigdoor/open_right";
            } else {
                parentPath = "block/bigdoor/open_left";
            }
        }

        return new Model(
                Optional.of(WesterosBlocks.id(parentPath)),
                Optional.empty(),
                TextureKey.ALL,
                TextureKey.TEXTURE,
                TextureKey.PARTICLE
        );
    }

    private static String getColumnType(BigDoorPart part) {
        if (part.isLeftColumn()) return "left";
        if (part.isRightColumn()) return "right";
        return "center";
    }
}
