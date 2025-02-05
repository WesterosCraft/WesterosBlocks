package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class CuboidNSEWBlockExport extends CuboidBlockExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    private static final String[] FACING_DIRECTIONS = {
            "north", "east", "south", "west"
    };

    private static final int[] ROTATIONS = {
            270, 0, 90, 180
    };

    public CuboidNSEWBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModBlockStateRecord sr : def.states) {
            if (!sr.isCustomModel()) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateCuboidModels(generator, sr, setIdx);
                }
            }
        }

        for (ModBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String baseName = justBase ? "base" : sr.stateID;

            for (int dirIndex = 0; dirIndex < FACING_DIRECTIONS.length; dirIndex++) {
                // For each texture set
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = getModelId(baseName, setIdx, sr.isCustomModel());
                    variant.put(VariantSettings.MODEL, modelId);

                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }

                    // Calculate rotation based on direction and offset
                    int rotation = (ROTATIONS[dirIndex] + sr.rotYOffset) % 360;
                    if (rotation > 0) {
                        variant.put(VariantSettings.Y, getRotation(rotation));
                    }

                    blockStateBuilder.addVariant(
                            "facing=" + FACING_DIRECTIONS[dirIndex],
                            variant,
                            stateIDs,
                            variants
                    );
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        ModBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        String pathPrefix = firstState.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH;
        String path = String.format("%s%s/%s_v1", pathPrefix, blockDefinition.getBlockName(), baseName);

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)),
                        Optional.empty())
        );
    }
}