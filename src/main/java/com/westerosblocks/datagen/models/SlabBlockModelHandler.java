package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCSlabBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class SlabBlockModelHandler extends ModelExport {
    private static String getParentPath(boolean isOccluded, boolean isTinted, boolean hasOverlay, String type) {
        String basePath;
        if (isOccluded) {
            if (isTinted) {
                basePath = "tinted/";
            } else {
                basePath = "untinted/";
            }
        } else {
            if (isTinted) {
                basePath = "tintednoocclusion/";
            } else {
                basePath = "noocclusion/";
            }
        }

        // Return the complete path based on type and overlay
        return switch (type) {
            case "bottom" -> basePath + (hasOverlay ? "half_slab_overlay" : "half_slab");
            case "top" -> basePath + (hasOverlay ? "upper_slab_overlay" : "upper_slab");
            case "double" -> (isTinted ? basePath : "") + (hasOverlay ? "cube_overlay" : "cube");
            default -> "block/cube_all";
        };
    }

    protected static Identifier getModelName(String blockName, String ext, int setidx) {
        Identifier id = Identifier.of(WesterosBlocks.MOD_ID, blockName + "/" + ext + ("_v" + (setidx + 1)));
        return id.withPrefixedPath(GENERATED_PATH);
    }

    protected static Identifier getModelName(String blockName, String ext, int setidx, String cond) {
        if (cond == null) {
            return getModelName(blockName, ext, setidx);
        }
        Identifier id = Identifier.of(WesterosBlocks.MOD_ID, blockName + "/" + cond + "/" + ext + ("_v" + (setidx + 1)));

        return id.withPrefixedPath(GENERATED_PATH);
    }

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        WCSlabBlock slabBlock = (block instanceof WCSlabBlock) ? (WCSlabBlock) block : null;
        final Map<String, List<BlockStateVariant>> variants = new HashMap<>();

        for (WesterosBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String baseName = def.states.size() > 1 ? (sr.stateID == null ? "base" : sr.stateID) : "";
            boolean isTinted = sr.isTinted();
            boolean hasOverlay = sr.getOverlayTextureByIndex(0) != null;
            boolean isOccluded = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;


            for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                // Do state for top half block
                BlockStateVariant topVariant = BlockStateVariant.create();
                Identifier topId = getModelName(def.blockName, "top", setIdx, sr.stateID);
                topVariant.put(VariantSettings.MODEL, topId);
                if (set.weight != null) {
                    topVariant.put(VariantSettings.WEIGHT, set.weight);
                }
                addVariant("type=top", topVariant, stateIDs, variants);

                // Do bottom half slab
                BlockStateVariant bottomVariant = BlockStateVariant.create();
                Identifier bottomId = getModelName(def.blockName, "bottom", setIdx, sr.stateID);
                bottomVariant.put(VariantSettings.MODEL, bottomId);
                if (set.weight != null) {
                    bottomVariant.put(VariantSettings.WEIGHT, set.weight);
                }
                addVariant("type=bottom", bottomVariant, stateIDs, variants);

                // Do full slab
                BlockStateVariant doubleVariant = BlockStateVariant.create();
                Identifier doubleId = getModelName(def.blockName, "double", setIdx, sr.stateID);
                doubleVariant.put(VariantSettings.MODEL, doubleId);
                if (set.weight != null) {
                    doubleVariant.put(VariantSettings.WEIGHT, set.weight);
                }
                addVariant("type=double", doubleVariant, stateIDs, variants);

                // Make models
                String[] types = {"bottom", "top", "double"};
                String baseModelPath = stateIDs != null && stateIDs.isEmpty() ?
                        String.format("%s%s", GENERATED_PATH, def.blockName) :
                        String.format("%s%s/%s", GENERATED_PATH, def.blockName, baseName);

                for (String type : types) {
                    String namespace = type.equals("double") && !isTinted && !hasOverlay && isOccluded ? "minecraft" : WesterosBlocks.MOD_ID;
                    Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
                            baseModelPath + "/" + type + "_v" + (setIdx + 1));
                    TextureMap textureMap = createTextureMap(set, sr, hasOverlay);
                    String parentPath = getParentPath(isOccluded, isTinted, hasOverlay, type);

                    Model model;
                    if (hasOverlay) {
                        model = ModModels.getAllSidesWithOverlay(parentPath);
                    } else {
                        model = ModModels.getAllSides(parentPath, namespace);
                    }
                    model.upload(modelId, textureMap, generator.modelCollector);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
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

        return map.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
                .put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
                .put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)))
                .put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(3)))
                .put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(4)))
                .put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(5)));
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef.RandomTextureSet ts) {
        return new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(TextureKey.UP, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(TextureKey.NORTH, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(TextureKey.SOUTH, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(TextureKey.WEST, createBlockIdentifier(ts.getTextureByIndex(4)))
                .put(TextureKey.EAST, createBlockIdentifier(ts.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        boolean hasMultipleStates = blockDefinition.states.size() > 1;
        String basePath = hasMultipleStates ? "/base" : "";
        String path = String.format("%s%s%s/bottom_v1", GENERATED_PATH, blockDefinition.blockName, basePath);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}
