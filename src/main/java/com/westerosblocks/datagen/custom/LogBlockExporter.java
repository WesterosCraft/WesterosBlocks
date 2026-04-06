package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class LogBlockExporter extends BaseBlockExporter {

    public static void registerCustomLogBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Log block definition states should never be null/empty for block: " + getBlockName(block));
        }

        BlockDefinition.StateVariant state = states.get(0);

        // Generate models for all texture sets and axes
        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
            generateLogModels(generator, block, state, setIdx, tinted);
        }

        // Generate blockstate using Fabric API
        generateBlockState(generator, block, state);

        Identifier itemModelId = createGeneratedModelId(block, getModelName("y", 0));
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          BlockDefinition.StateVariant state) {
        BlockStateVariantMap.SingleProperty<WeightedVariant, Direction.Axis> variantMap =
            BlockStateVariantMap.models(Properties.AXIS);

        for (Direction.Axis axis : Direction.Axis.values()) {
            String axisName = axis.asString(); // "x", "y", "z"
            int xRot = axis == Direction.Axis.Y ? 0 : 90;
            int yRot = axis == Direction.Axis.X ? 90 : 0;

            net.minecraft.util.collection.Pool.Builder<ModelVariant> poolBuilder = net.minecraft.util.collection.Pool.builder();

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null || set.getTextureCount() == 0) continue;

                Identifier modelId = createGeneratedModelId(block, getModelName(axisName, setIdx));

                ModelVariant mv = new ModelVariant(modelId);
                if (xRot > 0) {
                    mv = mv.withRotationX(AxisRotation.R90);
                }
                if (yRot > 0) {
                    mv = mv.withRotationY(AxisRotation.R90);
                }

                poolBuilder.add(mv, set.getWeight());
            }

            variantMap.register(axis, new WeightedVariant(poolBuilder.build()));
        }

        generator.blockStateCollector.accept(
            VariantsBlockModelDefinitionCreator.of(block).with(variantMap)
        );
    }

    private static void generateLogModels(BlockStateModelGenerator generator, Block block,
                                         BlockDefinition.StateVariant state, int setIdx, boolean tinted) {
        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null || set.getTextureCount() == 0) return;

        String down = set.getTextureByIndex(0);
        String up = set.getTextureByIndex(1);
        String north = set.getTextureByIndex(2);
        String south = set.getTextureByIndex(3);
        String west = set.getTextureByIndex(4);
        String east = set.getTextureByIndex(5);

        TextureMap textureMap = ModTextureMap.logTextures(down, up, north, south, west, east);

        // Vertical (Y-axis) model
        Model verticalModel = tinted ? ModModels.LOG_6FACE_TINTED : ModModels.LOG_6FACE;
        Identifier yModelId = createGeneratedModelId(block, getModelName("y", setIdx));
        verticalModel.upload(yModelId, textureMap, generator.modelCollector);

        // Horizontal (X-axis and Z-axis) models
        Model horizontalModel = tinted ? ModModels.LOG_6FACE_HORIZONTAL_TINTED : ModModels.LOG_6FACE_HORIZONTAL;
        Identifier xModelId = createGeneratedModelId(block, getModelName("x", setIdx));
        horizontalModel.upload(xModelId, textureMap, generator.modelCollector);

        Identifier zModelId = createGeneratedModelId(block, getModelName("z", setIdx));
        horizontalModel.upload(zModelId, textureMap, generator.modelCollector);
    }
}
