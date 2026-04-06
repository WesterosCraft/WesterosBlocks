package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.MultipartModelConditionBuilder;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.block.custom.WCPaneBlock;
import com.westerosblocks.data.BlockDefinition;

import java.util.ArrayList;
import java.util.List;

public class PaneBlockExporter extends BaseBlockExporter {

    private static Model getPaneSideModel(boolean isBars) {
        return isBars ? ModModels.PANE_SIDE_BARS : ModModels.PANE_SIDE;
    }

    public static void registerPaneBlockWithVariants(BlockStateModelGenerator generator, Block block,
                                                     List<String[]> textureVariants, List<Integer> weights) {
        WCPaneBlock paneBlock = (WCPaneBlock) block;
        boolean isBars = paneBlock.isBarsModel();

        List<Identifier> postModelIds = new ArrayList<>();
        List<Identifier> sideModelIds = new ArrayList<>();
        List<Identifier> nosideModelIds = new ArrayList<>();

        // Generate models for each texture variant
        for (int i = 0; i < textureVariants.size(); i++) {
            String[] textures = textureVariants.get(i);
            String sideTexture = textures[0];
            String capTexture = textures.length > 1 ? textures[1] : textures[0];

            TextureMap paneTextureMap = new TextureMap()
                    .put(TextureKey.SIDE, createBlockIdentifier(sideTexture))
                    .put(ModTextureKey.CAP, createBlockIdentifier(capTexture))
                    .put(TextureKey.PARTICLE, createBlockIdentifier(sideTexture));

            // Upload models with _v1, _v2, etc. suffix
            String suffix = "_v" + (i + 1);

            if (!isBars) {
                Identifier postModelId = ModModels.PANE_POST.upload(
                        createNestedModelId(block, "post" + suffix), paneTextureMap, generator.modelCollector);
                postModelIds.add(postModelId);

                Identifier nosideModelId = ModModels.PANE_NOSIDE.upload(
                        createNestedModelId(block, "noside" + suffix), paneTextureMap, generator.modelCollector);
                nosideModelIds.add(nosideModelId);
            }

            Identifier sideModelId = getPaneSideModel(isBars).upload(
                    createNestedModelId(block, "side" + suffix), paneTextureMap, generator.modelCollector);
            sideModelIds.add(sideModelId);
        }

        // Create multipart blockstate
        MultipartBlockModelDefinitionCreator supplier = createPaneBlockState(block, paneBlock,
                postModelIds, sideModelIds, nosideModelIds, weights);
        generator.blockStateCollector.accept(supplier);

        // Register item model (using first texture variant)
        String[] firstTextures = textureVariants.get(0);
        registerSimpleItemModel(generator, block, createBlockIdentifier(firstTextures[0]));
    }

    private static MultipartBlockModelDefinitionCreator createPaneBlockState(Block block, WCPaneBlock paneBlock,
            List<Identifier> postModelIds, List<Identifier> sideModelIds,
            List<Identifier> nosideModelIds, List<Integer> weights) {

        MultipartBlockModelDefinitionCreator supplier = MultipartBlockModelDefinitionCreator.create(block);
        boolean isBars = paneBlock.isBarsModel();

        // Add all variants (for random textures)
        for (int i = 0; i < sideModelIds.size(); i++) {
            int weight = weights.get(i);

            // Post model (always present for non-bars models)
            if (!isBars) {
                WeightedVariant postVariant = BlockStateModelGenerator.createWeightedVariant(postModelIds.get(i));
                supplier = supplier.with(postVariant);
            }

            // Add side connections for all 4 directions with proper rotation and uvlock
            addPaneSideVariant(supplier, sideModelIds.get(i), weight, Direction.NORTH, isBars);
            addPaneSideVariant(supplier, sideModelIds.get(i), weight, Direction.EAST, isBars);
            addPaneSideVariant(supplier, sideModelIds.get(i), weight, Direction.SOUTH, isBars);
            addPaneSideVariant(supplier, sideModelIds.get(i), weight, Direction.WEST, isBars);

            // No-side connections for non-bars models (all 4 directions)
            if (!isBars) {
                addPaneNosideVariant(supplier, nosideModelIds.get(i), weight, Direction.NORTH);
                addPaneNosideVariant(supplier, nosideModelIds.get(i), weight, Direction.EAST);
                addPaneNosideVariant(supplier, nosideModelIds.get(i), weight, Direction.SOUTH);
                addPaneNosideVariant(supplier, nosideModelIds.get(i), weight, Direction.WEST);
            }
        }

        return supplier;
    }

    /**
     * Adds a pane side variant for a specific direction.
     * For bars models, uses OR logic: direction=true OR all_directions=false
     */
    private static void addPaneSideVariant(MultipartBlockModelDefinitionCreator supplier, Identifier sideModelId,
                                           int weight, Direction direction, boolean isBars) {
        ModelVariant mv = new ModelVariant(sideModelId).withUVLock(true);

        int yRotation = getRotationForDirection(direction);
        if (yRotation != 0) {
            mv = mv.withRotationY(toYRotation(yRotation));
        }

        WeightedVariant sideVariant = BlockStateModelGenerator.createWeightedVariant(mv);

        // Create condition
        if (isBars) {
            // For bars: direction=true OR all_directions=false
            MultipartModelConditionBuilder directionTrue = switch (direction) {
                case NORTH -> new MultipartModelConditionBuilder().put(Properties.NORTH, true);
                case EAST -> new MultipartModelConditionBuilder().put(Properties.EAST, true);
                case SOUTH -> new MultipartModelConditionBuilder().put(Properties.SOUTH, true);
                case WEST -> new MultipartModelConditionBuilder().put(Properties.WEST, true);
                default -> throw new IllegalArgumentException("Invalid direction: " + direction);
            };

            MultipartModelConditionBuilder allFalse = new MultipartModelConditionBuilder()
                    .put(Properties.NORTH, false)
                    .put(Properties.EAST, false)
                    .put(Properties.SOUTH, false)
                    .put(Properties.WEST, false);

            supplier.with(BlockStateModelGenerator.or(directionTrue, allFalse), sideVariant);
        } else {
            // For regular panes: just direction=true
            MultipartModelConditionBuilder condition = switch (direction) {
                case NORTH -> new MultipartModelConditionBuilder().put(Properties.NORTH, true);
                case EAST -> new MultipartModelConditionBuilder().put(Properties.EAST, true);
                case SOUTH -> new MultipartModelConditionBuilder().put(Properties.SOUTH, true);
                case WEST -> new MultipartModelConditionBuilder().put(Properties.WEST, true);
                default -> throw new IllegalArgumentException("Invalid direction: " + direction);
            };

            supplier.with(condition, sideVariant);
        }
    }

    /**
     * Adds a pane noside variant for a specific direction (non-bars models only).
     */
    private static void addPaneNosideVariant(MultipartBlockModelDefinitionCreator supplier, Identifier nosideModelId,
                                             int weight, Direction direction) {
        ModelVariant mv = new ModelVariant(nosideModelId).withUVLock(true);

        int yRotation = getRotationForDirection(direction);
        if (yRotation != 0) {
            mv = mv.withRotationY(toYRotation(yRotation));
        }

        WeightedVariant nosideVariant = BlockStateModelGenerator.createWeightedVariant(mv);

        // Condition: direction=false
        MultipartModelConditionBuilder condition = switch (direction) {
            case NORTH -> new MultipartModelConditionBuilder().put(Properties.NORTH, false);
            case EAST -> new MultipartModelConditionBuilder().put(Properties.EAST, false);
            case SOUTH -> new MultipartModelConditionBuilder().put(Properties.SOUTH, false);
            case WEST -> new MultipartModelConditionBuilder().put(Properties.WEST, false);
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };

        supplier.with(condition, nosideVariant);
    }

    public static void registerCustomPaneBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // After doInit(), states is ALWAYS non-empty (at least synthetic base state exists)
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        // Extract all random texture variants from the first state
        BlockDefinition.StateVariant state = states.get(0);
        List<String[]> textureVariants = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        int textureSetCount = state.getRandomTextureSetCount();
        if (textureSetCount > 0) {
            // Multiple texture variants (random textures)
            for (int i = 0; i < textureSetCount; i++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(i);
                if (set != null && set.getTextureCount() > 0) {
                    // Pane blocks need side texture and cap texture
                    // texture[0] = side, texture[1] = cap (if present, otherwise use side for both)
                    String[] textures = new String[2];
                    textures[0] = set.getTextureByIndex(0);  // side texture
                    textures[1] = set.getTextureCount() > 1 ? set.getTextureByIndex(1) : textures[0];  // cap texture

                    textureVariants.add(textures);
                    weights.add(set.getWeight());
                } else {
                    // Fallback for empty texture set
                    textureVariants.add(new String[]{"missingno", "missingno"});
                    weights.add(1);
                }
            }
        } else {
            // No random textures, use single texture
            textureVariants.add(new String[]{"missingno", "missingno"});
            weights.add(1);
        }

        registerPaneBlockWithVariants(generator, block, textureVariants, weights);
    }
}