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

public class StairBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public StairBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

//    protected static String getModelName(String ext, int setidx, String name) {
//        return name + "/" + ext + ("_v" + (setidx + 1));
//    }

    protected static String getModelName(String ext, int setidx, String cond, String name) {
        if (cond == null)
            return getModelName(ext, setidx, name);
        return name + "/" + cond + "/" + ext + ("_v" + (setidx + 1));
    }

    public static String modelFileName(String ext, int setidx, String name) {
        return modelFileName(ext, setidx, name, null);
    }

    public static String modelFileName(String ext, int setidx, String name, Boolean isCustomModel) {
        if (isCustomModel != null && isCustomModel)
            return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx, name);
        else
            return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx, name);
    }

    public static String modelFileName(WesterosBlockStateRecord sr, String ext, int setidx, String cond, String blockName) {
        if (sr.isCustomModel())
            return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx, cond, blockName);
        else
            return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx, cond, blockName);
    }

    private static String getParentPath(boolean isOccluded, boolean isTinted, boolean hasOverlay, String modelType) {
        String basePath;
        if (isOccluded) {
            basePath = isTinted ? "tinted/" : "untinted/";
        } else {
            basePath = isTinted ? "tintednoocclusion/" : "noocclusion/";
        }

        if (modelType.equals("base")) {
            return basePath + (hasOverlay ? "stairs_overlay" : "stairs");
        }
        return basePath + (hasOverlay ? modelType + "_stairs_overlay" : modelType + "_stairs");
    }

    public void generateBlockStateModels() {
        WCStairBlock stairBlock = (block instanceof WCStairBlock) ? (WCStairBlock) block : null;
        boolean isUvLocked = !(block instanceof WCStairBlock) || !stairBlock.no_uvlock;
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (WesterosBlockStateRecord sr : def.states) {
            boolean isTinted = sr.isTinted();
            boolean hasOverlay = sr.getOverlayTextureByIndex(0) != null;
            boolean isOccluded = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;

            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=bottom,shape=straight", "base", 0, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=bottom,shape=straight", "base", 0, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=bottom,shape=straight", "base", 0, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=bottom,shape=straight", "base", 0, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=bottom,shape=outer_right", "outer", 0, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=bottom,shape=outer_right", "outer", 0, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=bottom,shape=outer_right", "outer", 0, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=bottom,shape=outer_right", "outer", 0, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=bottom,shape=outer_left", "outer", 0, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=bottom,shape=outer_left", "outer", 0, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=bottom,shape=outer_left", "outer", 0, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=bottom,shape=outer_left", "outer", 0, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=bottom,shape=inner_right", "inner", 0, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=bottom,shape=inner_right", "inner", 0, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=bottom,shape=inner_right", "inner", 0, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=bottom,shape=inner_right", "inner", 0, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=bottom,shape=inner_left", "inner", 0, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=bottom,shape=inner_left", "inner", 0, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=bottom,shape=inner_left", "inner", 0, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=bottom,shape=inner_left", "inner", 0, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=top,shape=straight", "base", 180, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=top,shape=straight", "base", 180, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=top,shape=straight", "base", 180, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=top,shape=straight", "base", 180, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=top,shape=outer_right", "outer", 180, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=top,shape=outer_right", "outer", 180, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=top,shape=outer_right", "outer", 180, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=top,shape=outer_right", "outer", 180, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=top,shape=outer_left", "outer", 180, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=top,shape=outer_left", "outer", 180, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=top,shape=outer_left", "outer", 180, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=top,shape=outer_left", "outer", 180, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=top,shape=inner_right", "inner", 180, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=top,shape=inner_right", "inner", 180, 270, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=top,shape=inner_right", "inner", 180, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=top,shape=inner_right", "inner", 180, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=east,half=top,shape=inner_left", "inner", 180, 0, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=west,half=top,shape=inner_left", "inner", 180, 180, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=south,half=top,shape=inner_left", "inner", 180, 90, isUvLocked, def);
            buildVariantList(blockStateBuilder, variants, sr, "facing=north,half=top,shape=inner_left", "inner", 180, 270, isUvLocked, def);

            doStairModels(generator, sr, def.getBlockName(), isOccluded, isTinted, hasOverlay);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private static void buildVariantList(BlockStateBuilder blockStateBuilder, Map<String, List<BlockStateVariant>> variants, WesterosBlockStateRecord sr, String cond, String ext, int xrot,
                                         int yrot, boolean isUvLocked, WesterosBlockDef def) {
        boolean justBase = sr.stateID == null;
        Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);

        // Loop over the random sets we've got
        for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

            // build variant
            BlockStateVariant var = BlockStateVariant.create();
            String varId = (justBase) ? modelFileName(ext, setIdx, def.blockName)
                    : modelFileName(sr, ext, setIdx, sr.stateID, def.blockName);

            var.put(VariantSettings.MODEL, Identifier.of(varId));
            if (set.weight != null) {
                var.put(VariantSettings.WEIGHT, set.weight);
            }

            if (xrot != 0) {
                var.put(VariantSettings.X, getRotation(xrot));
            }

            if (yrot != 0) {
                var.put(VariantSettings.Y, getRotation(yrot));
            }

            if (isUvLocked && ((xrot != 0) || (yrot != 0))) {
                var.put(VariantSettings.UVLOCK, true);
            }

            blockStateBuilder.addVariant(cond, var, stateIDs, variants);
        }
    }

    protected static void doStairModels(BlockStateModelGenerator generator, WesterosBlockStateRecord currentRec, String blockName, boolean isOccluded, boolean isTinted, boolean hasOverlay) {
        if (!currentRec.isCustomModel()) {
            String basePath = currentRec.stateID == null ? blockName :
                    blockName + "/" + currentRec.stateID;

            for (int setIdx = 0; setIdx < currentRec.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = currentRec.getRandomTextureSet(setIdx);
                String[] types = {"base", "outer", "inner"};

                for (String type : types) {
                    TextureMap textureMap = createTextureMap(set, currentRec, hasOverlay);
                    Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
                            GENERATED_PATH + basePath + "/" + type + "_v" + (setIdx + 1));
                    String parentPath = getParentPath(isOccluded, isTinted, hasOverlay, type);

                    Model model;
                    if (hasOverlay) {
                        model = ModModels.createBottomTopSideWithOverlay(parentPath);
                    } else {
                        model = ModModels.createBottomTopSide(parentPath);
                    }
                    model.upload(modelId, textureMap, generator.modelCollector);
                }
            }
        }
    }

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet ts, WesterosBlockStateRecord currentRec, boolean hasOverlay) {
        if (hasOverlay) {
            return createOverlayTextureMap(ts, currentRec);
        } else {
            return createCustomTextureMap(ts);
        }
    }

    private static TextureMap createOverlayTextureMap(WesterosBlockDef.RandomTextureSet ts, WesterosBlockStateRecord currentRec) {
        TextureMap map = createCustomTextureMap(ts);
        return map.put(ModTextureKey.BOTTOM_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
                .put(ModTextureKey.TOP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
                .put(ModTextureKey.SIDE_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)));
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef.RandomTextureSet ts) {
        return new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        boolean hasMultipleStates = blockDefinition.states.size() > 1;
        String basePath = hasMultipleStates ? "/base" : "";
        String path = String.format("%s%s%s/base_v1", GENERATED_PATH, blockDefinition.blockName, basePath);

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}
