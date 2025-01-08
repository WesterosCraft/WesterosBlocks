package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCCuboidBlock;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import javax.lang.model.element.Element;
import java.util.*;

public class CuboidBlockModelHandler extends ModelExport {
    private static final String GENERATED_PATH = "block/generated/";
    private static final int[] DEFAULT_SIDE_TEXTURES = {0, 1, 2, 3, 4, 5};
    private static final boolean[] DEFAULT_NO_TINT = new boolean[6];

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator,
                                                Block block, WesterosBlockDef blockDefinition) {
        WCCuboidBlock cuboidBlock = (WCCuboidBlock) block;
        boolean hasMultipleStates = blockDefinition.states.size() > 1;

        for (int stateIdx = 0; stateIdx < blockDefinition.states.size(); stateIdx++) {
            WesterosBlockStateRecord state = blockDefinition.states.get(stateIdx);
            String baseName = hasMultipleStates ? (state.stateID == null ? "base" : state.stateID) : "";
            boolean isTinted = state.isTinted();
            boolean isOccluded = (blockDefinition.ambientOcclusion != null) ?
                    blockDefinition.ambientOcclusion : true;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                generateCuboidModel(blockStateModelGenerator, blockDefinition, state,
                        baseName, setIdx, cuboidBlock.getModelCuboids(stateIdx),
                        isOccluded, isTinted);
            }
        }

        // Generate blockstate with variants
        generateBlockState(blockStateModelGenerator, block, blockDefinition);
    }

    private static void generateBlockState(BlockStateModelGenerator generator,
                                           Block block, WesterosBlockDef blockDefinition) {
        List<BlockStateVariant> variants = new ArrayList<>();

        for (WesterosBlockStateRecord state : blockDefinition.states) {
            String baseName = state.stateID == null ? "base" : state.stateID;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
                String modelPath = GENERATED_PATH + blockDefinition.blockName +
                        (baseName.isEmpty() ? "" : "/" + baseName) + "_v" + (setIdx + 1);

                // Handle rotations if specified
                if (state.rotateRandom) {
                    for (int rot = 0; rot < 4; rot++) {
                        BlockStateVariant variant = BlockStateVariant.create()
                                .put(VariantSettings.MODEL,
                                        Identifier.of(WesterosBlocks.MOD_ID, modelPath));

                        if (rot > 0) {
                            variant.put(VariantSettings.Y,
                                    VariantSettings.Rotation.valueOf("R" + (rot * 90)));
                        }

                        if (textureSet.weight != null && textureSet.weight > 0) {
                            variant.put(VariantSettings.WEIGHT, textureSet.weight);
                        }

                        variants.add(variant);
                    }
                } else {
                    BlockStateVariant variant = BlockStateVariant.create()
                            .put(VariantSettings.MODEL,
                                    Identifier.of(WesterosBlocks.MOD_ID, modelPath));

                    if (textureSet.weight != null && textureSet.weight > 0) {
                        variant.put(VariantSettings.WEIGHT, textureSet.weight);
                    }

                    if (state.rotYOffset != 0) {
                        variant.put(VariantSettings.Y,
                                VariantSettings.Rotation.valueOf("R" + state.rotYOffset));
                    }

                    variants.add(variant);
                }
            }
        }

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
        );
    }

    private static void generateCuboidModel(BlockStateModelGenerator generator,
                                            WesterosBlockDef blockDef, WesterosBlockStateRecord state,
                                            String baseName, int setIdx, List<WesterosBlockDef.Cuboid> cuboids,
                                            boolean isOccluded, boolean isTinted) {

        WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
        String modelPath = blockDef.states.size() > 1 ?
                String.format("%s%s/%s/v%d", GENERATED_PATH, blockDef.blockName, baseName.isEmpty() ? "base" : baseName, setIdx + 1) :
                String.format("%s%s/%s_v%d", GENERATED_PATH, blockDef.blockName, baseName.isEmpty() ? "base" : baseName, setIdx + 1);


        // Create model
        Model model;
        if (WesterosBlockDef.SHAPE_CROSSED.equals(cuboids.getFirst().shape)) {
            model = Models.CROSS;
        } else {
            String basePath = isOccluded ?
                    (isTinted ? "tinted/cuboid" : "untinted/cuboid") :
                    (isTinted ? "tintednoocclusion/cuboid" : "noocclusion/cuboid");
            model = ModModels.createCuboidModel(basePath);
        }

        // Create texture map
        TextureMap textureMap = createCustomTextureMap(textureSet);

        // Add textures
        for (int i = 0; i < Math.max(6, textureSet.getTextureCount()); i++) {
            String texture = textureSet.getTextureByIndex(i);
            if (!isTransparentTexture(texture)) {
                TextureKey texKey = TextureKey.of("txt" + i);
                textureMap.put(texKey, createBlockIdentifier(texture));
            }
        }

        // Generate models for each cuboid
        for (WesterosBlockDef.Cuboid cuboid : cuboids) {
            addCuboidToModel(cuboid, textureMap, isTinted);
        }

        // Upload model
        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID, modelPath);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private static void addCuboidToModel(WesterosBlockDef.Cuboid cuboid, TextureMap textureMap, boolean isTinted) {
        int[] sideTextures = cuboid.sideTextures != null ? cuboid.sideTextures : DEFAULT_SIDE_TEXTURES;
        boolean[] noTint = cuboid.noTint != null ? cuboid.noTint : DEFAULT_NO_TINT;

        Direction[] directions = {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

//        for (int i = 0; i < directions.length; i++) {
//            TextureKey sourceKey = TextureKey.of("txt" + sideTextures[i]);
//            if (textureMap.getTexture(sourceKey) != null) {
//                Direction dir = directions[i];
//                TextureKey targetKey = TextureKey.of("txt" + sideTextures[dir.getId()]);
//                textureMap.put(targetKey, textureMap.getTexture(sourceKey));
//
//                if (isTinted && !noTint[i]) {
//                    textureMap.put(TextureKey.of("tintindex"), Identifier.of("0"));
//                }
//            }
//        }
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef.RandomTextureSet ts) {
        return new TextureMap()
                .put(ModTextureKey.TEXTURE_0, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(ModTextureKey.TEXTURE_1, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(ModTextureKey.TEXTURE_2, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(ModTextureKey.TEXTURE_3, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(ts.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(ts.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
                                          Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        String path = GENERATED_PATH + blockDefinition.blockName + "/" +
                baseName + "_v1";

        itemModelGenerator.register(block.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)),
                        Optional.empty()));
    }
}