package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class FenceBlockModelHandler {
    private static final String GENERATED_PATH = "block/generated/";
    private static final String BLOCK_PATH = "block/";
    private static String getParentPath(boolean isTinted, boolean hasOverlay, String type) {
        StringBuilder path = new StringBuilder();
        path.append("block/");
        path.append(isTinted ? "tinted/" : "untinted/");
        path.append("fence_").append(type);
        if (hasOverlay) {
            path.append("_overlay");
        }
        return path.toString();
    }

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(currentBlock);

        for (WesterosBlockStateRecord state : blockDefinition.states) {
            String baseName = state.stateID == null ? "base" : state.stateID;
            boolean isTinted = state.isTinted();
            boolean hasOverlay = state.getOverlayTextureByIndex(0) != null;

            processStateVariants(blockStateModelGenerator, blockDefinition, state, baseName, stateSupplier, isTinted, hasOverlay);
        }

        blockStateModelGenerator.blockStateCollector.accept(stateSupplier);
    }

    private static void processStateVariants(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            WesterosBlockStateRecord state,
            String baseName,
            MultipartBlockStateSupplier stateSupplier,
            boolean isTinted,
            boolean hasOverlay
    ) {
        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);

            // Generate post model
            generatePostModel(blockStateModelGenerator, blockDefinition, baseName, setIdx, textureSet, state, stateSupplier, isTinted, hasOverlay);
            // Generate side models for each direction
//            generateSideModels(blockStateModelGenerator, blockDefinition, baseName, setIdx, textureSet, state, stateSupplier);
        }
    }

    private static void generatePostModel(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            String baseName,
            int setIdx,
            WesterosBlockDef.RandomTextureSet textureSet,
            WesterosBlockStateRecord state,
            MultipartBlockStateSupplier stateSupplier,
            boolean isTinted,
            boolean hasOverlay
    ) {
        Identifier postModelId = createModelIdentifier(blockDefinition.blockName, "post", baseName, setIdx);
        TextureMap textureMap = createTextureMap(textureSet, hasOverlay, blockDefinition);
        String parentPath = getParentPath(state.isTinted(), state.getOverlayTextureByIndex(0) != null, "post");

        Model postModel = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, parentPath)),
                Optional.empty(),
                TextureKey.TOP, TextureKey.SIDE
        );
        postModel.upload(postModelId, textureMap, blockStateModelGenerator.modelCollector);

        BlockStateVariant postVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, postModelId);
        if (textureSet.weight != null && textureSet.weight > 0) {
            postVariant.put(VariantSettings.WEIGHT, textureSet.weight);
        }

        stateSupplier.with(When.create(), postVariant);
    }

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet textureSet, boolean hasOverlay, WesterosBlockDef blockDefinition) {
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
                    .put(TextureKey.of("down_ov"), getOverlayTexture(blockDefinition, 0))
                    .put(TextureKey.of("up_ov"), getOverlayTexture(blockDefinition, 1))
                    .put(TextureKey.of("north_ov"), getOverlayTexture(blockDefinition, 2))
                    .put(TextureKey.of("south_ov"), getOverlayTexture(blockDefinition, 3))
                    .put(TextureKey.of("west_ov"), getOverlayTexture(blockDefinition, 4))
                    .put(TextureKey.of("east_ov"), getOverlayTexture(blockDefinition, 5));
        }

        return textureMap;
    }

    private static Identifier createModelIdentifier(String blockName, String type, String baseName, int setIdx) {
        StringBuilder path = new StringBuilder()
                .append(GENERATED_PATH)
                .append(blockName)
                .append('/');

        if (baseName != null && !baseName.equals("base")) {
            path.append(baseName).append('/');
        }

        path.append(type)
                .append("_v")
                .append(setIdx + 1);

        return Identifier.of(WesterosBlocks.MOD_ID, path.toString());
    }

    private static Identifier getOverlayTexture(WesterosBlockDef blockDefinition, int index) {
        return Identifier.of(WesterosBlocks.MOD_ID, BLOCK_PATH +
                blockDefinition.states.getFirst().getOverlayTextureByIndex(index));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        boolean isTinted = firstState.isTinted();

        String path = String.format("%s%s/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}
