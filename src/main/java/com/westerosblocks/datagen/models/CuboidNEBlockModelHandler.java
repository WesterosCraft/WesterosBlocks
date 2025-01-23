package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class CuboidNEBlockModelHandler extends CuboidBlockModelHandler {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static final String[] DIRECTIONS = {"east", "north"};

    public CuboidNEBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
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
            if (!sr.isCustomModel()) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateCuboidModels(generator, sr, setIdx);
                }
            }
        }

        for (WesterosBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String baseName = justBase ? "base" : sr.stateID;

            for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                // Generate variants for east and north directions
                for (int dirIndex = 0; dirIndex < DIRECTIONS.length; dirIndex++) {
                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = getModelId(baseName, setIdx, sr.isCustomModel());
                    variant.put(VariantSettings.MODEL, modelId);

                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }

                    // Calculate rotation based on direction
                    int rotation = (90 * dirIndex + sr.rotYOffset) % 360;
                    if (rotation > 0) {
                        variant.put(VariantSettings.Y, getRotation(rotation));
                    }

                    // Add variant to builder
                    blockStateBuilder.addVariant(
                            "facing=" + DIRECTIONS[dirIndex],
                            variant,
                            stateIDs,
                            variants
                    );
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    // TODO item model could use some work
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
                // TODO: Handle tinting registration
            }
        }
    }
}
