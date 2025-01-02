package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolidBlockModelHandlerOld {
    private static final String GENERATED_PATH = "block/generated/";
    private static final String BLOCK_PATH = "block/";

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        boolean isSymmetrical = currentBlock instanceof WCSolidBlock && ((WCSolidBlock) currentBlock).symmetrical;
        List<BlockStateVariant> variants = new ArrayList<>();

        for (WesterosBlockStateRecord state : blockDefinition.states) {
            String baseName = state.stateID == null ? "base" : state.stateID;
            processStateVariants(blockStateModelGenerator, blockDefinition, state, baseName, isSymmetrical, variants);
        }

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(currentBlock, variants.toArray(new BlockStateVariant[0]))
        );
    }

    private static void processStateVariants(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            WesterosBlockStateRecord state,
            String baseName,
            boolean isSymmetrical,
            List<BlockStateVariant> variants
    ) {

        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
            int rotationCount = state.rotateRandom ? 4 : 1;
            WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);

            for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {

                if (isSymmetrical) {
                    generateSymmetricalVariants(blockStateModelGenerator, blockDefinition, baseName,
                            setIdx, textureSet, variants);
                } else {
                    generateAsymmetricalVariants(blockStateModelGenerator, blockDefinition, baseName, rotationCount,
                            setIdx, textureSet, variants);
                }

            }
        }
    }

    private static void generateSymmetricalVariants(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            String baseName,
            int setIdx,
            WesterosBlockDef.RandomTextureSet textureSet,
            List<BlockStateVariant> variants
    ) {
        String[] types = {"symmetrical", "asymmetrical"};
        for (String type : types) {
            Identifier modelId = createModelIdentifier(blockDefinition.blockName, type, baseName, setIdx);
            generateVariantsForType(modelId, textureSet, variants);
            TextureMap textureMap = createTextureMap(textureSet);

            Models.CUBE_ALL.upload(modelId, textureMap, blockStateModelGenerator.modelCollector);
        }
    }

    private static void generateAsymmetricalVariants(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            String baseName,
            int rotationCount,
            int setIdx,
            WesterosBlockDef.RandomTextureSet textureSet,
            List<BlockStateVariant> variants
    ) {
        Identifier modelId = createModelIdentifier(blockDefinition.blockName, null, baseName, setIdx);
        generateVariantsForType(modelId, textureSet, variants);

        // Generate the model
        TextureMap textureMap = createTextureMap(textureSet);
        Models.CUBE_ALL.upload(modelId, textureMap, blockStateModelGenerator.modelCollector);
    }

    private static void generateVariantsForType(
            Identifier modelId,
//            int rotationCount,
            WesterosBlockDef.RandomTextureSet textureSet,
            List<BlockStateVariant> variants
    ) {
//        for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, modelId);

//            if (rotIdx > 0) {
//                variant.put(VariantSettings.Y, VariantSettings.Rotation.values()[rotIdx]);
//            }

            if (textureSet.weight != null && textureSet.weight > 0) {
                variant.put(VariantSettings.WEIGHT, textureSet.weight);
            }

            variants.add(variant);
//        }
    }

    private static Identifier createModelIdentifier(String blockName, String type, String baseName, int setIdx) {
        StringBuilder path = new StringBuilder()
                .append(GENERATED_PATH)
                .append(blockName)
                .append('/');

        if (type != null) {
            path.append(type).append('/');
        }

        path.append(baseName)
                .append("_v")
                .append(setIdx + 1);

        return Identifier.of(WesterosBlocks.MOD_ID, path.toString());
    }

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet textureSet) {
        return new TextureMap()
                .put(TextureKey.ALL, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(0)));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        boolean isSymmetrical = currentBlock instanceof WCSolidBlock && ((WCSolidBlock) currentBlock).symmetrical;

        String path = isSymmetrical ?
                String.format("%s%s/symmetrical/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName) :
                String.format("%s%s/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}
