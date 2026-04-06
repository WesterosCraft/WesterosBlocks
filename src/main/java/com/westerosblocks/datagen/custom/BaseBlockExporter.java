package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.client.render.model.json.MultipartModelConditionBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.Direction;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.utils.ModProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Base class providing shared utilities for all block exporters.
 * Centralizes common functionality to eliminate code duplication and ensure consistency.
 */
public abstract class BaseBlockExporter {

    protected static String getBlockName(Block block) {
        String blockString = block.toString();
        if (blockString.contains(":")) {
            return blockString.split(":")[1].replace("}", "");
        }
        return blockString.toLowerCase().replace("block{", "").replace("}", "");
    }

    protected static Identifier createNestedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        return WesterosBlocks.id(modelPath);
    }

    protected static Identifier createNestedModelId(Block block) {
        return createNestedModelId(block, getBlockName(block));
    }

    protected static Identifier createBlockIdentifier(String texturePath) {
        if (texturePath == null || texturePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Texture path cannot be null or empty");
        }
        if (texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            return Identifier.of(namespace, path);
        }
        return WesterosBlocks.id("block/" + texturePath);
    }

    protected static Identifier createModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = variant != null && !variant.isEmpty()
            ? "block/" + blockName + "/" + variant
            : "block/" + blockName + "/" + blockName;
        return WesterosBlocks.id(modelPath);
    }

    protected static Identifier createModelId(Block block) {
        return createModelId(block, null);
    }

    protected static void validateTexturePaths(String[] texturePaths, int minRequired) {
        if (texturePaths == null || texturePaths.length < minRequired) {
            throw new IllegalArgumentException(
                "At least " + minRequired + " texture path(s) required, got " +
                (texturePaths == null ? 0 : texturePaths.length));
        }
    }

    protected static String[] fillTextureArray(String[] texturePaths, int targetSize) {
        validateTexturePaths(texturePaths, 1);
        String[] filledTextures = new String[targetSize];
        for (int i = 0; i < targetSize; i++) {
            if (i < texturePaths.length) {
                filledTextures[i] = texturePaths[i];
            } else {
                filledTextures[i] = texturePaths[texturePaths.length - 1];
            }
        }
        return filledTextures;
    }

    protected static String[] fillTextureArray(String[] texturePaths) {
        return fillTextureArray(texturePaths, 6);
    }

    // ========================================
    // WeightedVariant creation helpers
    // ========================================

    /**
     * Creates a WeightedVariant with just a model (no rotation).
     */
    protected static WeightedVariant createVariant(Identifier modelId) {
        return BlockStateModelGenerator.createWeightedVariant(modelId);
    }

    /**
     * Creates a WeightedVariant with model and Y rotation.
     */
    protected static WeightedVariant createVariant(Identifier modelId, int rotation) {
        if (rotation == 0) {
            return BlockStateModelGenerator.createWeightedVariant(modelId);
        }
        return BlockStateModelGenerator.createWeightedVariant(
            new ModelVariant(modelId).withRotationY(toYRotation(rotation)));
    }

    /**
     * Creates a simple blockstate with a single model variant.
     */
    protected static VariantsBlockModelDefinitionCreator createSimpleBlockState(Block block, Identifier modelId) {
        return VariantsBlockModelDefinitionCreator.of(block, createVariant(modelId));
    }

    protected static Model createTintedModel(boolean tinted, String modelPath, TextureKey... textureKeys) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String fullPath = tintPath + modelPath;
        return new Model(Optional.of(WesterosBlocks.id(fullPath)), Optional.empty(), textureKeys);
    }

    protected static Identifier uploadModel(Model model, Block block, String variant,
                                           TextureMap textureMap, BiConsumer<Identifier, ModelSupplier> modelCollector) {
        return model.upload(createNestedModelId(block, variant), textureMap, modelCollector);
    }

    /**
     * Creates a WeightedVariant with model, rotation, and optional weight.
     */
    protected static WeightedVariant createWeightedVariant(Identifier modelId, int rotation, int weight) {
        ModelVariant mv = new ModelVariant(modelId);
        if (rotation != 0) {
            mv = mv.withRotationY(toYRotation(rotation));
        }
        if (weight > 1) {
            return new WeightedVariant(Pool.builder().add(mv, weight).build());
        }
        return BlockStateModelGenerator.createWeightedVariant(mv);
    }

    /**
     * Creates a list of weighted variants from model IDs and weights, all with the same rotation.
     * Returns a single WeightedVariant with a Pool of all the variants.
     */
    protected static WeightedVariant createWeightedVariants(List<Identifier> modelIds,
                                                            List<Integer> weights, int rotation) {
        Pool.Builder<ModelVariant> poolBuilder = Pool.builder();
        for (int i = 0; i < modelIds.size(); i++) {
            ModelVariant mv = new ModelVariant(modelIds.get(i));
            if (rotation != 0) {
                mv = mv.withRotationY(toYRotation(rotation));
            }
            poolBuilder.add(mv, weights.get(i));
        }
        return new WeightedVariant(poolBuilder.build());
    }

    /**
     * Creates a WeightedVariant from a list of individual WeightedVariants by merging their pools.
     */
    protected static WeightedVariant mergeVariants(List<WeightedVariant> variants) {
        if (variants.size() == 1) {
            return variants.get(0);
        }
        Pool.Builder<ModelVariant> poolBuilder = Pool.builder();
        for (WeightedVariant wv : variants) {
            for (Weighted<ModelVariant> entry : wv.variants().getEntries()) {
                poolBuilder.add(entry.value(), entry.weight());
            }
        }
        return new WeightedVariant(poolBuilder.build());
    }

    /**
     * Converts degrees to AxisRotation enum.
     */
    protected static AxisRotation toYRotation(int degrees) {
        return switch (degrees) {
            case 0 -> AxisRotation.R0;
            case 90 -> AxisRotation.R90;
            case 180 -> AxisRotation.R180;
            case 270 -> AxisRotation.R270;
            default -> throw new IllegalArgumentException("Invalid rotation: " + degrees + ". Must be 0, 90, 180, or 270.");
        };
    }

    protected static int getRotationForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> throw new IllegalArgumentException("Direction must be horizontal: " + direction);
        };
    }

    protected static int getFacingSouthDefaultRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 90;
            case EAST -> 270;
            default -> 0;
        };
    }

    protected static ModProperties.StateProperty getStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property instanceof ModProperties.StateProperty sp && "state".equals(property.getName()))
                return sp;
        }
        return null;
    }

    protected static boolean hasStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property.getName().equals("state"))
                return true;
        }
        return false;
    }

    protected static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    protected static Identifier createGeneratedModelId(Block block, String variant) {
        return createNestedModelId(block, variant);
    }

    protected static String getModelName(String baseName, int variantIndex) {
        return baseName + "_v" + (variantIndex + 1);
    }

    protected static String getModelName(String baseName, int variantIndex, String suffix) {
        return baseName + "_v" + (variantIndex + 1) + "_" + suffix;
    }

    protected static String getStateIdOrBase(String stateID) {
        return stateID == null ? "base" : stateID;
    }

    /**
     * Builds a list of weighted, optionally-rotated block state variants as WeightedVariant entries
     * suitable for passing into a Pool or registering directly.
     */
    protected static WeightedVariant buildRotatedVariantList(
            List<Identifier> modelIds, List<Integer> weights, boolean rotateRandom) {
        Pool.Builder<ModelVariant> poolBuilder = Pool.builder();
        int rotationCount = rotateRandom ? 4 : 1;

        for (int i = 0; i < modelIds.size(); i++) {
            int weight = (weights != null) ? weights.get(i) : 1;
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                ModelVariant mv = new ModelVariant(modelIds.get(i));
                if (rotation > 0) {
                    mv = mv.withRotationY(toYRotation(90 * rotation));
                }
                poolBuilder.add(mv, weight);
            }
        }

        return new WeightedVariant(poolBuilder.build());
    }

    /**
     * Creates a VariantsBlockModelDefinitionCreator with optionally-rotated weighted variants.
     */
    protected static VariantsBlockModelDefinitionCreator createRotatedVariantsBlockState(
            Block block, List<Identifier> modelIds, List<Integer> weights, boolean rotateRandom) {
        WeightedVariant variant = buildRotatedVariantList(modelIds, weights, rotateRandom);
        return VariantsBlockModelDefinitionCreator.of(block, variant);
    }

    protected static class ModelRegistry {
        private final List<Identifier> modelIds = new ArrayList<>();
        private final List<Integer> weights = new ArrayList<>();

        public void add(Identifier modelId, int weight) {
            modelIds.add(modelId);
            weights.add(weight);
        }

        public List<Identifier> getModelIds() {
            return modelIds;
        }

        public List<Integer> getWeights() {
            return weights;
        }

        public int size() {
            return modelIds.size();
        }
    }

    /**
     * Helper class for building blockstates with multiple properties.
     * Collects variants with simple string conditions, then builds appropriate BlockStateVariantMap.
     */
    protected static class BlockStateBuilder {
        private final Block block;
        private final com.westerosblocks.utils.ModProperties.StateProperty stateProperty;

        // Maps condition string to list of (ModelVariant, weight) pairs
        private final java.util.Map<String, List<Weighted<ModelVariant>>> variants = new java.util.HashMap<>();

        public BlockStateBuilder(Block block, com.westerosblocks.utils.ModProperties.StateProperty stateProperty) {
            this.block = block;
            this.stateProperty = stateProperty;
        }

        /**
         * Adds a variant with condition string and optional stateID.
         */
        public void addVariant(String cond, WeightedVariant variant, String stateID) {
            String key;
            if (stateID == null) {
                key = cond;
            } else {
                key = cond + (cond.isEmpty() ? "" : ",") + "state=" + stateID;
            }

            List<Weighted<ModelVariant>> entries = variants.computeIfAbsent(key, k -> new ArrayList<>());
            for (Weighted<ModelVariant> entry : variant.variants().getEntries()) {
                entries.add(entry);
            }
        }

        public void register(BlockStateModelGenerator generator) {
            if (variants.isEmpty()) {
                return;
            }

            boolean hasSymmetrical = variants.keySet().stream().anyMatch(k -> k.contains("symmetrical="));
            boolean hasStates = variants.keySet().stream().anyMatch(k -> k.contains("state="));

            if (hasSymmetrical && hasStates) {
                registerDoubleProperty(generator);
            } else if (hasStates) {
                registerStateProperty(generator);
            } else if (hasSymmetrical) {
                registerSymmetricalProperty(generator);
            } else {
                registerSimple(generator);
            }
        }

        private WeightedVariant toWeightedVariant(List<Weighted<ModelVariant>> entries) {
            return new WeightedVariant(Pool.of(entries));
        }

        private void registerDoubleProperty(BlockStateModelGenerator generator) {
            BlockStateVariantMap.DoubleProperty<WeightedVariant, Boolean, String> variantMap =
                BlockStateVariantMap.models(com.westerosblocks.block.custom.WCSolidBlock.SYMMETRICAL, stateProperty);

            for (java.util.Map.Entry<String, List<Weighted<ModelVariant>>> entry : variants.entrySet()) {
                String key = entry.getKey();
                boolean symmetrical = key.contains("symmetrical=true");
                String stateID = extractStateID(key);
                variantMap.register(symmetrical, stateID, toWeightedVariant(entry.getValue()));
            }

            generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block).with(variantMap));
        }

        private void registerStateProperty(BlockStateModelGenerator generator) {
            BlockStateVariantMap.SingleProperty<WeightedVariant, String> variantMap =
                BlockStateVariantMap.models(stateProperty);

            for (java.util.Map.Entry<String, List<Weighted<ModelVariant>>> entry : variants.entrySet()) {
                String key = entry.getKey();
                String stateID = extractStateID(key);
                variantMap.register(stateID, toWeightedVariant(entry.getValue()));
            }

            generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block).with(variantMap));
        }

        private void registerSymmetricalProperty(BlockStateModelGenerator generator) {
            List<Weighted<ModelVariant>> symTrue = new ArrayList<>();
            List<Weighted<ModelVariant>> symFalse = new ArrayList<>();

            for (java.util.Map.Entry<String, List<Weighted<ModelVariant>>> entry : variants.entrySet()) {
                boolean isSymmetrical = entry.getKey().contains("symmetrical=true");
                if (isSymmetrical) {
                    symTrue.addAll(entry.getValue());
                } else {
                    symFalse.addAll(entry.getValue());
                }
            }

            BlockStateVariantMap.SingleProperty<WeightedVariant, Boolean> variantMap =
                BlockStateVariantMap.models(com.westerosblocks.block.custom.WCSolidBlock.SYMMETRICAL);
            variantMap.register(true, toWeightedVariant(symTrue));
            variantMap.register(false, toWeightedVariant(symFalse));

            generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block).with(variantMap));
        }

        private void registerSimple(BlockStateModelGenerator generator) {
            List<Weighted<ModelVariant>> allEntries = new ArrayList<>();
            for (List<Weighted<ModelVariant>> entryList : variants.values()) {
                allEntries.addAll(entryList);
            }

            generator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block, toWeightedVariant(allEntries)));
        }

        private String extractStateID(String key) {
            int stateIndex = key.indexOf("state=");
            if (stateIndex >= 0) {
                String stateValue = key.substring(stateIndex + 6);
                int commaIndex = stateValue.indexOf(',');
                if (commaIndex >= 0) {
                    stateValue = stateValue.substring(0, commaIndex);
                }
                return stateValue;
            }
            return null;
        }
    }

    protected static List<BlockDefinition.TextureVariantSet> extractTextureVariantSets(BlockDefinition definition) {
        List<BlockDefinition.TextureVariantSet> sets = new ArrayList<>();
        boolean overlay = definition.hasOverlayTextures();

        for (BlockDefinition.RandomTextureVariant rv : definition.getRandomTextures()) {
            List<String> textures = rv.getTextures();
            int weight = rv.getWeight();

            if (textures != null && !textures.isEmpty()) {
                String[] textureArray = textures.toArray(new String[0]);
                String[] overlayArray = null;
                if (overlay && definition.getOverlayTextures() != null
                        && definition.getOverlayTextures().size() >= textures.size()) {
                    overlayArray = definition.getOverlayTextures().subList(0, textures.size()).toArray(new String[0]);
                }
                sets.add(new BlockDefinition.TextureVariantSet(textureArray, weight, overlayArray));
            } else {
                sets.add(new BlockDefinition.TextureVariantSet(new String[]{"missingno"}, weight, null));
            }
        }
        return sets;
    }

    protected static String getOverlayTextureByIndex(List<String> overlayTextures, int index) {
        if (overlayTextures == null || overlayTextures.isEmpty()) {
            return null;
        }
        if (index >= overlayTextures.size()) {
            index = overlayTextures.size() - 1;
        }
        return overlayTextures.get(index);
    }

    protected static TextureMap createFenceWallTextureMap(String[] textures, String[] overlayTextures) {
        if (overlayTextures != null) {
            return ModTextureMap.fenceWallOverlayTextures(
                    textures[0], textures[1], textures[2],
                    overlayTextures[0], overlayTextures[1], overlayTextures[2]);
        }
        return ModTextureMap.fenceWallTextures(textures[0], textures[1], textures[2]);
    }

    protected static void registerSimpleItemModel(BlockStateModelGenerator generator, Block block, Identifier textureId) {
        TextureMap itemTextureMap = TextureMap.layer0(textureId);
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), itemTextureMap, generator.modelCollector);
    }

    protected static void registerParentedItemModel(BlockStateModelGenerator generator, Block block, Identifier modelId) {
        generator.registerParentedItemModel(block, modelId);
    }
}
