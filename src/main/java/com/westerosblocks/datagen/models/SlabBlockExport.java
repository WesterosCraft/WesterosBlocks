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

public class SlabBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public SlabBlockExport(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

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
        Identifier id = WesterosBlocks.id(blockName + "/" + ext + ("_v" + (setidx + 1)));
        return id.withPrefixedPath(GENERATED_PATH);
    }

    protected static Identifier getModelName(String blockName, String ext, int setidx, String cond) {
        if (cond == null) {
            return getModelName(blockName, ext, setidx);
        }
        Identifier id = WesterosBlocks.id(blockName + "/" + cond + "/" + ext + ("_v" + (setidx + 1)));

        return id.withPrefixedPath(GENERATED_PATH);
    }

    public void generateBlockStateModels() {
        WCSlabBlock slabBlock = (block instanceof WCSlabBlock) ? (WCSlabBlock) block : null;
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

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
                blockStateBuilder.addVariant("type=top", topVariant, stateIDs, variants);

                // Do bottom half slab
                BlockStateVariant bottomVariant = BlockStateVariant.create();
                Identifier bottomId = getModelName(def.blockName, "bottom", setIdx, sr.stateID);
                bottomVariant.put(VariantSettings.MODEL, bottomId);
                if (set.weight != null) {
                    bottomVariant.put(VariantSettings.WEIGHT, set.weight);
                }
                blockStateBuilder.addVariant("type=bottom", bottomVariant, stateIDs, variants);

                // Do full slab
                BlockStateVariant doubleVariant = BlockStateVariant.create();
                Identifier doubleId = getModelName(def.blockName, "double", setIdx, sr.stateID);
                doubleVariant.put(VariantSettings.MODEL, doubleId);
                if (set.weight != null) {
                    doubleVariant.put(VariantSettings.WEIGHT, set.weight);
                }
                blockStateBuilder.addVariant("type=double", doubleVariant, stateIDs, variants);

                // Make models
                String[] types = {"bottom", "top", "double"};
                String baseModelPath = stateIDs != null && stateIDs.isEmpty() ?
                        String.format("%s%s", GENERATED_PATH, def.blockName) :
                        String.format("%s%s/%s", GENERATED_PATH, def.blockName, baseName);

                for (String type : types) {
                    String namespace = type.equals("double") && !isTinted && !hasOverlay && isOccluded ? "minecraft" : WesterosBlocks.MOD_ID;
                    Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
                            baseModelPath + "/" + type + "_v" + (setIdx + 1));
                    TextureMap textureMap = ModTextureMap.frontTopSides(set, sr, hasOverlay, null);
                    String parentPath = getParentPath(isOccluded, isTinted, hasOverlay, type);

                    Model model;
                    if (hasOverlay) {
                        model = ModModels.ALL_SIDES_OVERLAY(parentPath);
                    } else {
                        model = ModModels.ALL_SIDES(parentPath, namespace);
                    }
                    model.upload(modelId, textureMap, generator.modelCollector);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        boolean hasMultipleStates = blockDefinition.states.size() > 1;
        String basePath = hasMultipleStates ? "/base" : "";
        String path = String.format("%s%s%s/bottom_v1", GENERATED_PATH, blockDefinition.blockName, basePath);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty())
        );
    }
}
