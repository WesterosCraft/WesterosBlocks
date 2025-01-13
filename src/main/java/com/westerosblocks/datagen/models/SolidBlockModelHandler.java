package com.westerosblocks.datagen.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCSolidBlock;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class SolidBlockModelHandler extends ModelExport {
    protected static String getModelName(WesterosBlockDef def, String ext, int setidx) {
        return def.blockName + "/" + ext + ("_v" + (setidx + 1));
    }

    protected static String getModelName(WesterosBlockDef def, String ext, int setidx, Boolean symmetrical) {
        String dir = symmetrical ? "symmetrical" : "asymmetrical";
        return def.blockName + "/" + dir + "/" + ext + ("_v" + (setidx + 1));
    }

    public static Identifier modelFileName(WesterosBlockDef def, String ext, int setidx, Boolean isCustom) {
        Identifier id =  Identifier.of(WesterosBlocks.MOD_ID, getModelName(def, ext, setidx));
        return id.withPrefixedPath(GENERATED_PATH);
    }

    public static Identifier modelFileName(WesterosBlockDef def, String ext, int setidx, Boolean isCustom, Boolean symmetrical) {
        Identifier id = Identifier.of(WesterosBlocks.MOD_ID, getModelName(def, ext, setidx, symmetrical));
        return id.withPrefixedPath(GENERATED_PATH);
    }

    public static void generateBlockStateAndModels(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        WCSolidBlock solidBlock = (block instanceof WCSolidBlock) ? (WCSolidBlock) block : null;
        boolean isSymmertrical = solidBlock != null && solidBlock.symmetrical;
        final Map<String, List<BlockStateVariant>> variants = new HashMap<>();

        for (WesterosBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String fname = justBase ? "base" : sr.stateID;
            boolean isTinted = sr.isTinted();
            boolean hasOverlay = sr.getOverlayTextureByIndex(0) != null;

            for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
                int rotationCount = sr.rotateRandom ? 4 : 1;    // 4 for random, just 1 if not

                for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
                    if (isSymmertrical) {
                        BlockStateVariant varSymmetrical = BlockStateVariant.create();
                        Identifier symId = modelFileName(def, fname, setIdx, sr.isCustomModel(), true);
                        varSymmetrical.put(VariantSettings.MODEL, symId);
                        if (set.weight != null) { varSymmetrical.put(VariantSettings.WEIGHT, set.weight); }
                        if (rotIdx > 0) { varSymmetrical.put(VariantSettings.Y, getRotation(90 * rotIdx)); }
                        addVariant("symmetrical=true", varSymmetrical, stateIDs, variants);

                        BlockStateVariant varAsymmetrical = BlockStateVariant.create();
                        Identifier asymId = modelFileName(def, fname, setIdx, sr.isCustomModel(), false);
                        varAsymmetrical.put(VariantSettings.MODEL, modelFileName(def, fname, setIdx, sr.isCustomModel(), false));
                        if (set.weight != null) { varAsymmetrical.put(VariantSettings.WEIGHT, set.weight); }
                        if (rotIdx > 0) { varAsymmetrical.put(VariantSettings.Y, getRotation(90 * rotIdx)); }
                        addVariant("symmetrical=false", varAsymmetrical, stateIDs, variants);

                        generateSolidModel(generator, symId, true,
                                isTinted, hasOverlay, sr, setIdx);
                        generateSolidModel(generator, asymId, false,
                                isTinted, hasOverlay, sr, setIdx);
                    } else {
                        BlockStateVariant variant = BlockStateVariant.create();
                        Identifier id = modelFileName(def, fname, setIdx, sr.isCustomModel());
                        variant.put(VariantSettings.MODEL, modelFileName(def, fname, setIdx, sr.isCustomModel()));
                        if (set.weight != null) {
                            variant.put(VariantSettings.WEIGHT, set.weight);
                        }
                        if (rotIdx > 0) {
                            variant.put(VariantSettings.Y, getRotation(90 * rotIdx));
                        }
                        addVariant("", variant, stateIDs, variants);

                        generateSolidModel(generator, id, false, isTinted, hasOverlay, sr, setIdx);
                    }
                }
            }
        }

        if (variants.isEmpty()) {
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block));
            return;
        }

        if (variants.size() == 1 && variants.containsKey("")) {
            List<BlockStateVariant> variantList = variants.get("");
            if (variantList.size() == 1) {
                generator.blockStateCollector.accept(
                        VariantsBlockStateSupplier.create(block, variantList.get(0))
                );
            } else {
                generator.blockStateCollector.accept(
                        VariantsBlockStateSupplier.create(block,
                                variantList.toArray(new BlockStateVariant[0]))
                );
            }
            return;
        }

        // Create custom BlockStateSupplier for multiple variants
        BlockStateSupplier supplier = new BlockStateSupplier() {
            @Override
            public JsonElement get() {
                JsonObject variantsJson = new JsonObject();

                for (Map.Entry<String, List<BlockStateVariant>> entry : variants.entrySet()) {
                    if (!entry.getValue().isEmpty()) {
                        variantsJson.add(entry.getKey(), BlockStateVariant.toJson(entry.getValue()));
                    }
                }

                JsonObject json = new JsonObject();
                json.add("variants", variantsJson);
                return json;
            }

            @Override
            public Block getBlock() {
                return block;
            }
        };

        generator.blockStateCollector.accept(supplier);
    }

    //        // Create a multipart supplier for multiple variants
//        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);
//
//        for (Map.Entry<String, List<BlockStateVariant>> entry : variants.entrySet()) {
//            String condition = entry.getKey();
//            List<BlockStateVariant> variantList = entry.getValue();
//
//            When.PropertyCondition propertyCondition = When.create();
//            String[] properties = condition.split(",");
//            for (String property : properties) {
//                if (!property.isEmpty()) {
//                    String[] parts = property.split("=");
//                    if (parts.length == 2) {
//                        Property<?> prop = block.getStateManager().getProperty(parts[0]);
//                        if (prop != null) {
//                            // Get the matching value for this property's type
//                            Optional<?> value = prop.parse(parts[1]);
//                            if (value.isPresent()) {
//                                setPropertyValue(propertyCondition, prop, value.get());
//                            }
//                        }
//                    }
//                }
//            }
//
//            supplier.with(propertyCondition, variantList);
//        }

//    @SuppressWarnings("unchecked")
//    private static <T extends Comparable<T>> void setPropertyValue(When.PropertyCondition condition, Property<?> property, Object value) {
//        condition.set((Property<T>) property, (T) value);
//    }

    protected static void generateSolidModel(BlockStateModelGenerator generator,
                                             Identifier modelPath, boolean isSymmetrical,
                                             boolean isTinted, boolean hasOverlay, WesterosBlockStateRecord currentRec, int setIdx) {
        WesterosBlockDef.RandomTextureSet set = currentRec.getRandomTextureSet(setIdx);
        TextureMap textureMap = createTextureMap(set, isSymmetrical, hasOverlay, isTinted, currentRec);

        if (hasOverlay) {
            String parentPath = isTinted ? "tinted/cube_overlay" : "untinted/cube_overlay";
            ModModels.getAllSidesWithOverlay(parentPath)
                    .upload(modelPath, textureMap, generator.modelCollector);
        } else if (set.getTextureCount() > 1 || isTinted) {
            String parentPath = isTinted ? "tinted/cube" : "cube";
            String namespace = isTinted ? WesterosBlocks.MOD_ID : "minecraft";
            ModModels.getAllSides(parentPath, namespace)
                    .upload(modelPath, textureMap, generator.modelCollector);
        } else {
            TextureMap textureMapAll = TextureMap.all(Identifier.of("minecraft", "block/cube"));
            Models.CUBE_ALL.upload(modelPath, textureMapAll, generator.modelCollector);
        }
    }

    public static void addVariant(String condition, BlockStateVariant variant, Set<String> stateIDs, Map<String, List<BlockStateVariant>> variants) {
        List<String> conditions = new ArrayList<>();

        if (stateIDs == null) {
            conditions.add(condition);
        } else {
            for (String stateVal : stateIDs) {
                String fullCondition = condition + ((!condition.isEmpty()) ? "," : "") + "state=" + stateVal;
                conditions.add(fullCondition);
            }
        }

        for (String conditionValue : conditions) {
            List<BlockStateVariant> existingVariants = variants.computeIfAbsent(conditionValue, k -> new ArrayList<>());
            existingVariants.add(variant);
        }
    }

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical, boolean hasOverlay, boolean isTinted, WesterosBlockStateRecord currentRec) {
        if (hasOverlay) {
            return createOverlayTextureMap(ts, isSymmetrical, currentRec);
        } else if (currentRec.getTextureCount() > 1 || isTinted) {
            return createCustomTextureMap(ts, isSymmetrical);
        } else {
            return new TextureMap()
                    .put(TextureKey.ALL, createBlockIdentifier(ts.getTextureByIndex(0)));
        }
    }

    private static TextureMap createOverlayTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical, WesterosBlockStateRecord currentRec) {
        TextureMap map = createCustomTextureMap(ts, isSymmetrical);

        return map.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
                .put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
                .put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)))
                .put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(3)))
                .put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(4)))
                .put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(5)));
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical) {
        return new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(TextureKey.UP, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(TextureKey.NORTH, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(TextureKey.SOUTH, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(TextureKey.WEST, createBlockIdentifier(ts.getTextureByIndex(isSymmetrical ? 4 : 6)))
                .put(TextureKey.EAST, createBlockIdentifier(ts.getTextureByIndex(isSymmetrical ? 5 : 7)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        boolean isSymmetrical = currentBlock instanceof WCSolidBlock && ((WCSolidBlock) currentBlock).symmetrical;

        String path = isSymmetrical ?
                String.format("%s%s/symmetrical/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName) :
                String.format("%s%s/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName);

        // TODO
//        blockDefinition.getBlockColorMapResource();

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}