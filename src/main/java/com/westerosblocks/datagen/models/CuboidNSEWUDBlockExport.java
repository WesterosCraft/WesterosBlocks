package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class CuboidNSEWUDBlockExport extends CuboidBlockExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static final String[] FACING_DIRECTIONS = {
            "north", "east", "south", "west", "up", "down"
    };

    private record RotationData(int x, int y) {
    }

    private static final Map<String, RotationData> ROTATIONS = Map.of(
            "north", new RotationData(0, 0),
            "east", new RotationData(0, 90),
            "south", new RotationData(0, 180),
            "west", new RotationData(0, 270),
            "up", new RotationData(270, 0),
            "down", new RotationData(90, 0)
    );

    public CuboidNSEWUDBlockExport(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    @Override
    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (WesterosBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String baseName = justBase ? "base" : sr.stateID;

            if (!sr.isCustomModel()) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateCuboidModels(generator, sr, setIdx);
                }
            }

            for (String facing : FACING_DIRECTIONS) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = getModelId(baseName, setIdx, sr.isCustomModel());
                    variant.put(VariantSettings.MODEL, modelId);
                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }
                    RotationData rotation = ROTATIONS.get(facing);
                    if (rotation.x != 0) {
                        variant.put(VariantSettings.X, getRotation(rotation.x));
                    }
                    int yRot = (rotation.y + sr.rotYOffset) % 360;
                    if (yRot > 0) {
                        variant.put(VariantSettings.Y, getRotation(yRot));
                    }

                    blockStateBuilder.addVariant("facing=" + facing, variant, stateIDs, variants);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        String pathPrefix = firstState.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH;
        String path = String.format("%s%s/%s_v1", pathPrefix, blockDefinition.getBlockName(), baseName);

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)),
                        Optional.empty())
        );

        if (blockDefinition.isTinted()) {
            String tintResource = blockDefinition.getBlockColorMapResource();
            if (tintResource != null) {
                // TODO: Handle tinting registration if needed
            }
        }
    }
}