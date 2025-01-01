package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCSolidBlock;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SolidBlockModelHandler {
    private static final String GENERATED_PATH = "block/generated/";
    private static final String BLOCK_PATH = "block/";

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WCSolidBlock solidBlock = (currentBlock instanceof WCSolidBlock) ? (WCSolidBlock) currentBlock : null;
        boolean isSymmetrical = currentBlock instanceof WCSolidBlock && ((WCSolidBlock) currentBlock).symmetrical;
        List<BlockStateVariant> variants = new ArrayList<>();

//        VariantsBlockStateSupplier stateSupplier = VariantsBlockStateSupplier.create(currentBlock);


        // Process each state record
        for (WesterosBlockStateRecord stateRecord : blockDefinition.states) {
            String baseName = stateRecord.stateID == null ? "base" : stateRecord.stateID;
            boolean isTinted = stateRecord.isTinted();
            boolean hasOverlay = stateRecord.getOverlayTextureByIndex(0) != null;

            // Handle each random texture set
            for (int setIdx = 0; setIdx < stateRecord.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet textureSet = stateRecord.getRandomTextureSet(setIdx);
                int rotationCount = stateRecord.rotateRandom ? 4 : 1; // 4 for random, just 1 if not

                for (int i = 0; i < rotationCount; i++) {
                    // if its symmetrical
                    if (solidBlock != null && solidBlock.symmetrical) {
                        String symmetricalModelPath = String.format("%s:block/generated/%s/symmetrical/%s_v%d",
                                WesterosBlocks.MOD_ID, blockDefinition.blockName, baseName, setIdx + 1);

                        BlockStateVariant variant = BlockStateVariant.create()
                                .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID, symmetricalModelPath));

                        if (textureSet.weight != null && textureSet.weight > 0) {
                            variant.put(VariantSettings.WEIGHT, textureSet.weight);
                        }

                        if (i > 0) {
                            variant.put(VariantSettings.Y, VariantSettings.Rotation.values()[i]);
                        }

                        variants.add(variant);

                    } else {
                    // do asymmetrical stuff
                    }

                }
            }
        }

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(currentBlock, variants.toArray(new BlockStateVariant[0]))
        );

        // Generate models for each state
//        generateModels(blockStateModelGenerator, currentBlock, blockDefinition);
    }

    private static TextureMap createTextureMap(String variant, WesterosBlockDef.RandomTextureSet textureSet, boolean hasOverlay, WesterosBlockDef blockDefinition) {
        TextureMap textureMap;

            textureMap = new TextureMap()
                    .put(TextureKey.DOWN, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(0)))
                    .put(TextureKey.UP, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(1)))
                    .put(TextureKey.NORTH, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(2)))
                    .put(TextureKey.SOUTH, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(3)))
                    .put(TextureKey.WEST, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(4)))
                    .put(TextureKey.EAST, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(5)))
                    .put(TextureKey.PARTICLE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(2)));


        if (hasOverlay) {
            textureMap
                    .put(TextureKey.of("down_ov"), blockDefinition.getOverlayTexture(0))
                    .put(TextureKey.of("up_ov"), blockDefinition.getOverlayTexture( 1))
                    .put(TextureKey.of("north_ov"), blockDefinition.getOverlayTexture(2))
                    .put(TextureKey.of("south_ov"), blockDefinition.getOverlayTexture(3))
                    .put(TextureKey.of("west_ov"), blockDefinition.getOverlayTexture(4))
                    .put(TextureKey.of("east_ov"), blockDefinition.getOverlayTexture(5));
        }

        return textureMap;
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