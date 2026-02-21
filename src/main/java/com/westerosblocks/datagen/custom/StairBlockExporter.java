package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.block.custom.WCStairBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StairBlockExporter extends BaseBlockExporter {

    // Stair rotation table: 40 entries for all facing × half × shape combinations
    private record StairRotation(Direction facing, BlockHalf half, StairShape shape,
                                 String modelType, int x, int y) {}

    private static final StairRotation[] STAIR_ROTATIONS = {
        // Bottom half - straight
        new StairRotation(Direction.EAST,  BlockHalf.BOTTOM, StairShape.STRAIGHT,    "base",  0, 0),
        new StairRotation(Direction.WEST,  BlockHalf.BOTTOM, StairShape.STRAIGHT,    "base",  0, 180),
        new StairRotation(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.STRAIGHT,    "base",  0, 90),
        new StairRotation(Direction.NORTH, BlockHalf.BOTTOM, StairShape.STRAIGHT,    "base",  0, 270),
        // Bottom half - outer_right
        new StairRotation(Direction.EAST,  BlockHalf.BOTTOM, StairShape.OUTER_RIGHT, "outer", 0, 0),
        new StairRotation(Direction.WEST,  BlockHalf.BOTTOM, StairShape.OUTER_RIGHT, "outer", 0, 180),
        new StairRotation(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT, "outer", 0, 90),
        new StairRotation(Direction.NORTH, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT, "outer", 0, 270),
        // Bottom half - outer_left
        new StairRotation(Direction.EAST,  BlockHalf.BOTTOM, StairShape.OUTER_LEFT,  "outer", 0, 270),
        new StairRotation(Direction.WEST,  BlockHalf.BOTTOM, StairShape.OUTER_LEFT,  "outer", 0, 90),
        new StairRotation(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,  "outer", 0, 0),
        new StairRotation(Direction.NORTH, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,  "outer", 0, 180),
        // Bottom half - inner_right
        new StairRotation(Direction.EAST,  BlockHalf.BOTTOM, StairShape.INNER_RIGHT, "inner", 0, 0),
        new StairRotation(Direction.WEST,  BlockHalf.BOTTOM, StairShape.INNER_RIGHT, "inner", 0, 180),
        new StairRotation(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.INNER_RIGHT, "inner", 0, 90),
        new StairRotation(Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_RIGHT, "inner", 0, 270),
        // Bottom half - inner_left
        new StairRotation(Direction.EAST,  BlockHalf.BOTTOM, StairShape.INNER_LEFT,  "inner", 0, 270),
        new StairRotation(Direction.WEST,  BlockHalf.BOTTOM, StairShape.INNER_LEFT,  "inner", 0, 90),
        new StairRotation(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.INNER_LEFT,  "inner", 0, 0),
        new StairRotation(Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_LEFT,  "inner", 0, 180),
        // Top half - straight
        new StairRotation(Direction.EAST,  BlockHalf.TOP,    StairShape.STRAIGHT,    "base",  180, 0),
        new StairRotation(Direction.WEST,  BlockHalf.TOP,    StairShape.STRAIGHT,    "base",  180, 180),
        new StairRotation(Direction.SOUTH, BlockHalf.TOP,    StairShape.STRAIGHT,    "base",  180, 90),
        new StairRotation(Direction.NORTH, BlockHalf.TOP,    StairShape.STRAIGHT,    "base",  180, 270),
        // Top half - outer_right
        new StairRotation(Direction.EAST,  BlockHalf.TOP,    StairShape.OUTER_RIGHT, "outer", 180, 90),
        new StairRotation(Direction.WEST,  BlockHalf.TOP,    StairShape.OUTER_RIGHT, "outer", 180, 270),
        new StairRotation(Direction.SOUTH, BlockHalf.TOP,    StairShape.OUTER_RIGHT, "outer", 180, 180),
        new StairRotation(Direction.NORTH, BlockHalf.TOP,    StairShape.OUTER_RIGHT, "outer", 180, 0),
        // Top half - outer_left
        new StairRotation(Direction.EAST,  BlockHalf.TOP,    StairShape.OUTER_LEFT,  "outer", 180, 0),
        new StairRotation(Direction.WEST,  BlockHalf.TOP,    StairShape.OUTER_LEFT,  "outer", 180, 180),
        new StairRotation(Direction.SOUTH, BlockHalf.TOP,    StairShape.OUTER_LEFT,  "outer", 180, 90),
        new StairRotation(Direction.NORTH, BlockHalf.TOP,    StairShape.OUTER_LEFT,  "outer", 180, 270),
        // Top half - inner_right
        new StairRotation(Direction.EAST,  BlockHalf.TOP,    StairShape.INNER_RIGHT, "inner", 180, 90),
        new StairRotation(Direction.WEST,  BlockHalf.TOP,    StairShape.INNER_RIGHT, "inner", 180, 270),
        new StairRotation(Direction.SOUTH, BlockHalf.TOP,    StairShape.INNER_RIGHT, "inner", 180, 180),
        new StairRotation(Direction.NORTH, BlockHalf.TOP,    StairShape.INNER_RIGHT, "inner", 180, 0),
        // Top half - inner_left
        new StairRotation(Direction.EAST,  BlockHalf.TOP,    StairShape.INNER_LEFT,  "inner", 180, 0),
        new StairRotation(Direction.WEST,  BlockHalf.TOP,    StairShape.INNER_LEFT,  "inner", 180, 180),
        new StairRotation(Direction.SOUTH, BlockHalf.TOP,    StairShape.INNER_LEFT,  "inner", 180, 90),
        new StairRotation(Direction.NORTH, BlockHalf.TOP,    StairShape.INNER_LEFT,  "inner", 180, 270),
    };

    private record StairModelSet(Identifier base, Identifier inner, Identifier outer, int weight) {}

    public static void registerCustomStairBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCStairBlock stairBlock)) {
            throw new IllegalArgumentException("Block must be a WCStairBlock instance");
        }

        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        boolean hasMultipleStates = definition.getStateCount() > 1;

        if (definition.hasCustomModel()) {
            registerCustomModelStairBlock(generator, block, stairBlock);
            return;
        }

        // Collect model sets per state (preserving state order)
        Map<String, List<StairModelSet>> stateModelMap = new LinkedHashMap<>();
        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = getStateIdOrBase(state.getStateID());
            List<StairModelSet> modelSets = new ArrayList<>();

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null || set.getTextureCount() == 0) continue;

                String[] textures = new String[set.getTextureCount()];
                for (int i = 0; i < set.getTextureCount(); i++) {
                    textures[i] = set.getTextureByIndex(i);
                }

                String prefix = hasMultipleStates ? stateId + "_" : "";
                Identifier baseModel = generateStairModel(generator, block, definition, textures, prefix + "base", setIdx);
                Identifier innerModel = generateStairModel(generator, block, definition, textures, prefix + "inner", setIdx);
                Identifier outerModel = generateStairModel(generator, block, definition, textures, prefix + "outer", setIdx);

                modelSets.add(new StairModelSet(baseModel, innerModel, outerModel, set.getWeight()));
                if (firstModel == null) firstModel = baseModel;
            }

            if (!modelSets.isEmpty()) {
                stateModelMap.put(stateId, modelSets);
            }
        }

        if (stateModelMap.isEmpty()) {
            registerFallbackStairBlock(generator, block, definition, stairBlock);
            return;
        }

        // Generate blockstate
        ModProperties.StateProperty stateProperty = getStateProperty(block);
        if (hasMultipleStates && stateProperty != null) {
            generateBlockStateWithStates(generator, block, stateModelMap, stateProperty, stairBlock.no_uvlock);
        } else {
            // Single state or no state property — combine all model sets as random variants
            List<StairModelSet> allModelSets = new ArrayList<>();
            stateModelMap.values().forEach(allModelSets::addAll);
            generateBlockState(generator, block, allModelSets, stairBlock.no_uvlock);
        }

        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    private static void registerCustomModelStairBlock(BlockStateModelGenerator generator, Block block, WCStairBlock stairBlock) {
        Identifier baseModel = createCustomModelId(block, "base_v1");
        Identifier innerModel = createCustomModelId(block, "inner_v1");
        Identifier outerModel = createCustomModelId(block, "outer_v1");

        List<StairModelSet> modelSets = List.of(new StairModelSet(baseModel, innerModel, outerModel, 1));
        generateBlockState(generator, block, modelSets, stairBlock.no_uvlock);
        registerParentedItemModel(generator, block, baseModel);
    }

    private static void registerFallbackStairBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCStairBlock stairBlock) {
        String[] fallbackTextures = {"missing", "missing", "missing"};
        Identifier baseModel = generateStairModel(generator, block, definition, fallbackTextures, "base", 0);
        Identifier innerModel = generateStairModel(generator, block, definition, fallbackTextures, "inner", 0);
        Identifier outerModel = generateStairModel(generator, block, definition, fallbackTextures, "outer", 0);

        List<StairModelSet> modelSets = List.of(new StairModelSet(baseModel, innerModel, outerModel, 1));
        generateBlockState(generator, block, modelSets, stairBlock.no_uvlock);
        registerParentedItemModel(generator, block, baseModel);
    }

    private static Identifier generateStairModel(BlockStateModelGenerator generator, Block block, BlockDefinition definition,
                                                  String[] textures, String type, int variantIndex) {
        String variantName = type + "_v" + (variantIndex + 1);
        Identifier modelId = createGeneratedModelId(block, variantName);

        boolean isTinted = definition.isTinted();
        boolean hasOverlay = definition.hasOverlay();

        // Extract the pure model type (strip state prefix like "base_" from "base_inner")
        String modelType;
        if (type.contains("inner")) modelType = "inner_stairs";
        else if (type.contains("outer")) modelType = "outer_stairs";
        else modelType = "stairs";
        String parentPath = modelType + (hasOverlay ? "_overlay" : "");

        TextureKey[] textureKeys;
        if (hasOverlay) {
            textureKeys = new TextureKey[]{TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE,
                ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY};
        } else {
            textureKeys = new TextureKey[]{TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE};
        }

        Model model = createTintedModel(isTinted, parentPath, textureKeys);

        String bottomTex = textures.length > 0 ? textures[0] : "missing";
        String topTex = textures.length > 1 ? textures[1] : bottomTex;
        String sideTex = textures.length > 2 ? textures[2] : topTex;

        TextureMap textureMap;
        if (hasOverlay && definition.getOverlayTextures() != null) {
            List<String> overlayTextures = definition.getOverlayTextures();
            String bottomOv = !overlayTextures.isEmpty() ? overlayTextures.get(0) : bottomTex;
            String topOv = overlayTextures.size() > 1 ? overlayTextures.get(1) : bottomOv;
            String sideOv = overlayTextures.size() > 2 ? overlayTextures.get(2) : topOv;
            textureMap = ModTextureMap.stairOverlayTextures(bottomTex, topTex, sideTex, bottomOv, topOv, sideOv);
        } else {
            textureMap = ModTextureMap.stairTextures(bottomTex, topTex, sideTex);
        }

        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    // Single-state blockstate: TripleProperty<Direction, BlockHalf, StairShape>
    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          List<StairModelSet> modelSets, boolean noUvlock) {
        BlockStateVariantMap.TripleProperty<Direction, BlockHalf, StairShape> variantMap =
            BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF, Properties.STAIR_SHAPE);

        for (StairRotation rot : STAIR_ROTATIONS) {
            List<BlockStateVariant> variants = buildVariantsForRotation(rot, modelSets, noUvlock);

            if (variants.size() == 1) {
                variantMap.register(rot.facing(), rot.half(), rot.shape(), variants.get(0));
            } else {
                variantMap.register(rot.facing(), rot.half(), rot.shape(), variants);
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    // Multi-state blockstate: QuadrupleProperty<Direction, BlockHalf, StairShape, String>
    private static void generateBlockStateWithStates(BlockStateModelGenerator generator, Block block,
                                                     Map<String, List<StairModelSet>> stateModelMap,
                                                     ModProperties.StateProperty stateProperty,
                                                     boolean noUvlock) {
        BlockStateVariantMap.QuadrupleProperty<Direction, BlockHalf, StairShape, String> variantMap =
            BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF, Properties.STAIR_SHAPE, stateProperty);

        for (Map.Entry<String, List<StairModelSet>> entry : stateModelMap.entrySet()) {
            String stateId = entry.getKey();
            List<StairModelSet> modelSets = entry.getValue();

            for (StairRotation rot : STAIR_ROTATIONS) {
                List<BlockStateVariant> variants = buildVariantsForRotation(rot, modelSets, noUvlock);

                if (variants.size() == 1) {
                    variantMap.register(rot.facing(), rot.half(), rot.shape(), stateId, variants.get(0));
                } else {
                    variantMap.register(rot.facing(), rot.half(), rot.shape(), stateId, variants);
                }
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    private static List<BlockStateVariant> buildVariantsForRotation(StairRotation rot, List<StairModelSet> modelSets, boolean noUvlock) {
        List<BlockStateVariant> variants = new ArrayList<>();

        for (StairModelSet modelSet : modelSets) {
            Identifier model = switch (rot.modelType()) {
                case "inner" -> modelSet.inner();
                case "outer" -> modelSet.outer();
                default -> modelSet.base();
            };

            BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, model);

            if (rot.x() != 0) {
                variant.put(VariantSettings.X, toYRotation(rot.x()));
            }
            if (rot.y() != 0) {
                variant.put(VariantSettings.Y, toYRotation(rot.y()));
            }
            if (!noUvlock && (rot.x() != 0 || rot.y() != 0)) {
                variant.put(VariantSettings.UVLOCK, true);
            }
            if (modelSet.weight() > 1) {
                variant.put(VariantSettings.WEIGHT, modelSet.weight());
            }

            variants.add(variant);
        }

        return variants;
    }

}
