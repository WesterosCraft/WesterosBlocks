package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Exporter for {@code mounted_mirror} blocks. Mirrors
 * {@link MountedSlabBlockExporter#registerMultiStateMountedSlab} but with the
 * {@code HALF} (top/bottom) dimension removed: FACING supplies the Y-rotation and the
 * STATE property ({@code left}/{@code right}) selects between two authored models.
 */
public class MountedMirrorBlockExporter extends BaseBlockExporter {

    private static final Direction[] DIRECTIONS = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static final int[] ROTATIONS = {0, 90, 180, 270};

    public static void registerMountedMirrorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!hasStateProperty(block)) {
            WesterosBlocks.LOGGER.warn("mounted_mirror block '{}' has no STATE property; expected left/right states", getBlockName(block));
            return;
        }
        ModProperties.StateProperty stateProp = getStateProperty(block);

        // FACING -> Y rotation only (the model is supplied by the STATE coordinate).
        BlockStateVariantMap.SingleProperty<Direction> facingMap =
                BlockStateVariantMap.create(Properties.HORIZONTAL_FACING);
        for (int d = 0; d < DIRECTIONS.length; d++) {
            facingMap.register(DIRECTIONS[d], rotationOnlyVariant(ROTATIONS[d]));
        }

        // STATE (left/right) -> model
        BlockStateVariantMap.SingleProperty<String> modelMap =
                BlockStateVariantMap.create(stateProp);

        Identifier firstModel = null;
        for (BlockDefinition.StateVariant state : definition.getStates()) {
            String id = state.getStateID();
            boolean isCustom = definition.hasCustomModel() || state.isCustomModel();

            Identifier modelId = isCustom
                    ? createCustomModelId(block, id)
                    : generateStateModel(generator, block, id, state, definition);

            modelMap.register(id, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
            if (firstModel == null) {
                firstModel = modelId;
            }
        }

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(facingMap).coordinate(modelMap));

        registerParentedItemModel(generator, block, firstModel);
    }

    /**
     * Non-custom fallback: builds a CUBE_ALL model from the state's (or definition's)
     * first texture. Blocks needing real geometry should set {@code isCustomModel} and
     * author left/right model files.
     */
    private static Identifier generateStateModel(BlockStateModelGenerator generator, Block block,
                                                 String stateId, BlockDefinition.StateVariant state,
                                                 BlockDefinition definition) {
        String texture;
        if (state.getTextures() != null && !state.getTextures().isEmpty()) {
            texture = state.getTextures().get(0);
        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            texture = definition.getTextures().get(0);
        } else {
            texture = "missing";
        }
        TextureMap textureMap = TextureMap.all(createBlockIdentifier(texture));
        return Models.CUBE_ALL.upload(createNestedModelId(block, stateId), textureMap, generator.modelCollector);
    }
}
