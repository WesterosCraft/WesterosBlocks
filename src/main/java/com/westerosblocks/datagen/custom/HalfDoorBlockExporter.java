package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.block.custom.WCHalfDoorBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.data.client.VariantSettings.Rotation;

import java.util.List;
import java.util.Optional;

/**
 * Exporter for half door (shutter) blocks following block-models.md patterns.
 * Generates models for half-height door blocks with hinge and open/closed states.
 *
 * @see ModModels#HALF_DOOR_LEFT
 * @see ModModels#HALF_DOOR_RIGHT
 * @see ModModels#HALF_DOOR_LEFT_OPEN
 * @see ModModels#HALF_DOOR_RIGHT_OPEN
 */
public class HalfDoorBlockExporter extends BaseBlockExporter {

    /**
     * Registers a half door (shutter) block with all hinge, open, and direction variants.
     * Follows block-models.md pattern for multi-variant blocks.
     *
     * <p>Generates 16 blockstate variants (2 hinges × 2 open states × 4 directions)
     *
     * @param generator The BlockStateModelGenerator to register models with
     * @param block The half door block to generate models for
     * @param texturePath Texture path for the half door (used for all variants)
     */
    public static void registerHalfDoorBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Upload models for each variant - block-models.md section 5.2: Parent Block Model
        Identifier leftModelId = createHalfDoorModel(generator, block, texturePath, "left", ModModels.HALF_DOOR_LEFT);
        Identifier rightModelId = createHalfDoorModel(generator, block, texturePath, "right", ModModels.HALF_DOOR_RIGHT);
        Identifier leftOpenModelId = createHalfDoorModel(generator, block, texturePath, "left_open", ModModels.HALF_DOOR_LEFT_OPEN);
        Identifier rightOpenModelId = createHalfDoorModel(generator, block, texturePath, "right_open", ModModels.HALF_DOOR_RIGHT_OPEN);

        // Create blockstate variants - block-models.md section 5.4: Custom BlockStateSupplier Method
        BlockStateVariantMap variants = createHalfDoorVariants(leftModelId, rightModelId, leftOpenModelId, rightOpenModelId);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        // Register item model - block-models.md section 5.5: Custom Datagen Method
        registerSimpleItemModel(generator, block, createBlockIdentifier(texturePath));
    }

    /**
     * Registers a half door block from a BlockDefinition.
     * Automatically extracts texture from the definition and registers the half door block.
     *
     * @param generator The BlockStateModelGenerator to register models with
     * @param block The half door block to generate models for
     * @param definition The block definition containing texture information
     */
    public static void registerCustomHalfDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        if (textureList != null && !textureList.isEmpty()) {
            String texturePath = textureList.get(0);
            registerHalfDoorBlock(generator, block, texturePath);
        } else {
            // Fallback if no textures defined
            registerHalfDoorBlock(generator, block, "missingno");
        }
    }

    private static TextureMap createHalfDoorTextureMap(String texturePath) {
        // Note: Uses TextureKey.BOTTOM for compatibility with existing models
        return new TextureMap().put(TextureKey.BOTTOM, createBlockIdentifier(texturePath));
    }

    private static BlockStateVariantMap createHalfDoorVariants(Identifier leftModelId, Identifier rightModelId,
                                                                Identifier leftOpenModelId, Identifier rightOpenModelId) {
        return BlockStateVariantMap.create(WCHalfDoorBlock.FACING, WCHalfDoorBlock.HINGE, WCHalfDoorBlock.OPEN)
            // EAST facing
            .register(Direction.EAST, DoorHinge.LEFT, false, createVariant(leftModelId))
            .register(Direction.EAST, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 90))
            .register(Direction.EAST, DoorHinge.RIGHT, false, createVariant(rightModelId))
            .register(Direction.EAST, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 270))
            // SOUTH facing
            .register(Direction.SOUTH, DoorHinge.LEFT, false, createVariant(leftModelId, 90))
            .register(Direction.SOUTH, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 180))
            .register(Direction.SOUTH, DoorHinge.RIGHT, false, createVariant(rightModelId, 90))
            .register(Direction.SOUTH, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 0))
            // WEST facing
            .register(Direction.WEST, DoorHinge.LEFT, false, createVariant(leftModelId, 180))
            .register(Direction.WEST, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 270))
            .register(Direction.WEST, DoorHinge.RIGHT, false, createVariant(rightModelId, 180))
            .register(Direction.WEST, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 90))
            // NORTH facing
            .register(Direction.NORTH, DoorHinge.LEFT, false, createVariant(leftModelId, 270))
            .register(Direction.NORTH, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 0))
            .register(Direction.NORTH, DoorHinge.RIGHT, false, createVariant(rightModelId, 270))
            .register(Direction.NORTH, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 180));
    }

    private static Identifier createHalfDoorModel(BlockStateModelGenerator generator, Block block, String texturePath,
                                                   String variant, Model model) {
        // Create model ID using nested path structure
        String blockName = getBlockName(block);
        Identifier modelId = WesterosBlocks.id("block/" + blockName + "/" + blockName + "_" + variant);

        // Create texture map
        TextureMap textureMap = createHalfDoorTextureMap(texturePath);

        // Create custom model with parent model path for legacy compatibility
        String parentModelPath = "block/untinted/" + getParentModelName(variant);
        Model doorModel = new Model(
            Optional.of(WesterosBlocks.id(parentModelPath)),
            Optional.empty(),
            TextureKey.BOTTOM
        );
        doorModel.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    private static String getParentModelName(String variant) {
        return switch (variant) {
            case "left" -> "half_door_left";
            case "right" -> "half_door_right";
            case "left_open" -> "half_door_left_open";
            case "right_open" -> "half_door_right_open";
            default -> throw new IllegalArgumentException("Unknown half door variant: " + variant);
        };
    }
}
