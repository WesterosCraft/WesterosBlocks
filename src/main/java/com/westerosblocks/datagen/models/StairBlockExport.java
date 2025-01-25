package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCStairBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class StairBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private final WCStairBlock stairBlock;
    private final boolean isOccluded;

    public StairBlockExport(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        this.stairBlock = (WCStairBlock) block;
        this.isOccluded = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        if (!def.isCustomModel()) {
            for (WesterosBlockStateRecord sr : def.states) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                    generateStairModels(generator, sr, set, setIdx);
                }
            }
        }

        // Generate all the variants for each state
        for (WesterosBlockStateRecord sr : def.states) {
            generateDirectionalVariants(blockStateBuilder, sr, "bottom", variants);
            generateDirectionalVariants(blockStateBuilder, sr, "top", variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateDirectionalVariants(BlockStateBuilder builder, WesterosBlockStateRecord sr, String half, Map<String, List<BlockStateVariant>> variants) {
        int xRot = half.equals("top") ? 180 : 0;

        // East facing variants
        addVariant(builder, sr, String.format("facing=east,half=%s,shape=straight", half), "base", xRot, 0, variants);
        addVariant(builder, sr, String.format("facing=east,half=%s,shape=outer_right", half), "outer", xRot, half.equals("top") ? 90 : 0, variants);
        addVariant(builder, sr, String.format("facing=east,half=%s,shape=outer_left", half), "outer", xRot, half.equals("top") ? 0 : 270, variants);
        addVariant(builder, sr, String.format("facing=east,half=%s,shape=inner_right", half), "inner", xRot, half.equals("top") ? 90 : 0, variants);
        addVariant(builder, sr, String.format("facing=east,half=%s,shape=inner_left", half), "inner", xRot, half.equals("top") ? 0 : 270, variants);

        // West facing variants
        addVariant(builder, sr, String.format("facing=west,half=%s,shape=straight", half), "base", xRot, 180, variants);
        addVariant(builder, sr, String.format("facing=west,half=%s,shape=outer_right", half), "outer", xRot, half.equals("top") ? 270 : 180, variants);
        addVariant(builder, sr, String.format("facing=west,half=%s,shape=outer_left", half), "outer", xRot, half.equals("top") ? 180 : 90, variants);
        addVariant(builder, sr, String.format("facing=west,half=%s,shape=inner_right", half), "inner", xRot, half.equals("top") ? 270 : 180, variants);
        addVariant(builder, sr, String.format("facing=west,half=%s,shape=inner_left", half), "inner", xRot, half.equals("top") ? 180 : 90, variants);

        // South facing variants
        addVariant(builder, sr, String.format("facing=south,half=%s,shape=straight", half), "base", xRot, 90, variants);
        addVariant(builder, sr, String.format("facing=south,half=%s,shape=outer_right", half), "outer", xRot, half.equals("top") ? 180 : 90, variants);
        addVariant(builder, sr, String.format("facing=south,half=%s,shape=outer_left", half), "outer", xRot, half.equals("top") ? 90 : 0, variants);
        addVariant(builder, sr, String.format("facing=south,half=%s,shape=inner_right", half), "inner", xRot, half.equals("top") ? 180 : 90, variants);
        addVariant(builder, sr, String.format("facing=south,half=%s,shape=inner_left", half), "inner", xRot, half.equals("top") ? 90 : 0, variants);

        // North facing variants
        addVariant(builder, sr, String.format("facing=north,half=%s,shape=straight", half), "base", xRot, 270, variants);
        addVariant(builder, sr, String.format("facing=north,half=%s,shape=outer_right", half), "outer", xRot, half.equals("top") ? 0 : 270, variants);
        addVariant(builder, sr, String.format("facing=north,half=%s,shape=outer_left", half), "outer", xRot, half.equals("top") ? 270 : 180, variants);
        addVariant(builder, sr, String.format("facing=north,half=%s,shape=inner_right", half), "inner", xRot, half.equals("top") ? 0 : 270, variants);
        addVariant(builder, sr, String.format("facing=north,half=%s,shape=inner_left", half), "inner", xRot, half.equals("top") ? 270 : 180, variants);
    }

    private void addVariant(BlockStateBuilder builder, WesterosBlockStateRecord sr, String condition, String modelType, int xRot, int yRot, Map<String, List<BlockStateVariant>> variants) {
        Set<String> stateIDs = sr.stateID == null ? null : Collections.singleton(sr.stateID);

        for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = getModelId(modelType, setIdx, sr.stateID);
            variant.put(VariantSettings.MODEL, modelId);

            if (set.weight != null) {
                variant.put(VariantSettings.WEIGHT, set.weight);
            }
            if (xRot != 0) {
                variant.put(VariantSettings.X, getRotation(xRot));
            }
            if (yRot != 0) {
                variant.put(VariantSettings.Y, getRotation(yRot));
            }
            if (!stairBlock.no_uvlock && (xRot != 0 || yRot != 0)) {
                variant.put(VariantSettings.UVLOCK, true);
            }

            builder.addVariant(condition, variant, stateIDs, variants);
        }
    }

    private void generateStairModels(BlockStateModelGenerator generator, WesterosBlockStateRecord sr, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        boolean isTinted = sr.isTinted();
        boolean hasOverlay = sr.getOverlayTextureByIndex(0) != null;
        TextureMap textureMap = ModTextureMap.bottomTopSide(set, sr, hasOverlay);

        generateModelVariant(generator, "base", sr, setIdx, textureMap, isTinted, hasOverlay);
        generateModelVariant(generator, "outer", sr, setIdx, textureMap, isTinted, hasOverlay);
        generateModelVariant(generator, "inner", sr, setIdx, textureMap, isTinted, hasOverlay);
    }

    private void generateModelVariant(BlockStateModelGenerator generator, String variant, WesterosBlockStateRecord sr, int setIdx, TextureMap textureMap, boolean isTinted, boolean hasOverlay) {
        String baseParent = String.format("block/%s/%sstairs%s",
                isOccluded ? (isTinted ? "tinted" : "untinted") : (isTinted ? "tintednoocclusion" : "noocclusion"),
                variant.equals("base") ? "" : variant + "_",
                hasOverlay ? "_overlay" : "");

        TextureKey[] textureKeys;
        if (hasOverlay) {
            textureKeys = new TextureKey[]{
                    TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                    ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY
            };
        } else {
            textureKeys = new TextureKey[]{
                    TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE
            };
        }

        Model model = new Model(
                Optional.of(WesterosBlocks.id(baseParent)),
                Optional.empty(),
                textureKeys
        );

        Identifier modelId = getModelId(variant, setIdx, sr.stateID);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String variant, int setIdx, String stateId) {
        String path = stateId == null ?
                String.format("%s/%s_v%d", def.blockName, variant, setIdx + 1) :
                String.format("%s/%s/%s_v%d", def.blockName, stateId, variant, setIdx + 1);

        return WesterosBlocks.id((def.isCustomModel() ? "block/custom/" : "block/generated/") + path);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        boolean hasMultipleStates = blockDefinition.states.size() > 1;
        String basePath = hasMultipleStates ? "/base" : "";
        String path = String.format("%s%s%s/base_v1", GENERATED_PATH, blockDefinition.blockName, basePath);

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty())
        );


        if (blockDefinition.isTinted()) {
            String tintResource = blockDefinition.getBlockColorMapResource();
            if (tintResource != null) {
                // TODO: Handle tinting registration
            }
        }
    }
}