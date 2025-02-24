package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class Cuboid8WayBlockExport extends CuboidBlockExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    private static final String[] ROTATION_MODIFIERS = {
            "",
            "_rotn45",
    };

    public Cuboid8WayBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String baseName = justBase ? "base" : sr.stateID;

            // For each direction (8-way rotation)
            for (int rotation = 0; rotation < 8; rotation++) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = getModelId(baseName + ROTATION_MODIFIERS[rotation % 2], setIdx, sr.isCustomModel());
                    variant.put(VariantSettings.MODEL, modelId);
                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }
                    int rot = (90 * (((rotation + 1) % 8) / 2) + sr.rotYOffset) % 360;
                    if (rot > 0) {
                        variant.put(VariantSettings.Y, getRotation(rot));
                    }
                    blockStateBuilder.addVariant("rotation=" + rotation, variant, stateIDs, variants);
                }
            }

            if (!sr.isCustomModel()) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateCuboidModels(sr, setIdx, baseName);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateCuboidModels(ModBlockStateRecord sr, int setIdx, String baseName) {
        ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
        TextureMap textureMap = createCuboidTextureMap(set);

        generateRotatedModel(baseName + ROTATION_MODIFIERS[0], textureMap, sr, setIdx, 0f);
        generateRotatedModel(baseName + ROTATION_MODIFIERS[1], textureMap, sr, setIdx, -45f);
    }

    private void generateRotatedModel(String modelName, TextureMap textureMap,
                                      ModBlockStateRecord sr, int setIdx, float rotation) {
        Identifier modelId = getModelId(modelName, setIdx, false);
        ModModels.ROTATED_CUBOID("cuboid_8way", rotation)
                .upload(modelId, textureMap, generator.modelCollector);
    }

    public Identifier getModelId(String variant, int setIdx, boolean isCustom) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d", isCustom ? CUSTOM_PATH : GENERATED_PATH, def.getBlockName(), variant, setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        ModBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;

        String path = String.format("%s%s/%s_v1",
                firstState.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH,
                blockDefinition.getBlockName(),
                baseName);

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)),
                        Optional.empty())
        );
    }
}