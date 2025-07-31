package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBranchBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * BranchBlockExport for builder-based branch blocks with cardinal direction
 * connections.
 * Handles different branch models based on connection states (no connections,
 * single connection, etc.).
 */
public class BranchBlockExport extends ModelExport2 {

        public BranchBlockExport() {
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
                // For backward compatibility, convert single texture to array
                generateBlockStateModels(generator, block, new String[] { texturePath });
        }

        public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String... texturePaths) {
                // Get the branch type from the block
                String branchType = "large_branch"; // Default branch type
                if (block instanceof WCBranchBlock) {
                        branchType = ((WCBranchBlock) block).getBranchType();
                }

                // Create models for different connection states
                Identifier baseModelId = createBranchModel(generator, block, branchType, texturePaths, "base");
                Identifier horizontalModelId = createBranchModel(generator, block, branchType, texturePaths,
                                "horizontal");
                Identifier connectedModelId = createBranchModel(generator, block, branchType, texturePaths,
                                "connected");
                Identifier horizontalConnectedModelId = createBranchModel(generator, block, branchType, texturePaths,
                                "horizontal_connected");
                Identifier horizontalConnectedTwoCornerModelId = createBranchModel(generator, block, branchType,
                                texturePaths,
                                "horizontal_connected_two_corner");
                Identifier horizontalConnectedThreeModelId = createBranchModel(generator, block, branchType,
                                texturePaths,
                                "horizontal_connected_three");
                Identifier horizontalConnectedFourModelId = createBranchModel(generator, block, branchType,
                                texturePaths,
                                "horizontal_connected_four");
                Identifier connectedTwoModelId = createBranchModel(generator, block, branchType, texturePaths,
                                "connected_two");
                Identifier connectedTwoCornerModelId = createBranchModel(generator, block, branchType, texturePaths,
                                "connected_two_corner");
                Identifier connectedThreeModelId = createBranchModel(generator, block, branchType, texturePaths,
                                "connected_three");
                Identifier connectedFourModelId = createBranchModel(generator, block, branchType, texturePaths,
                                "connected_four");

                // Use MultipartBlockStateSupplier for more efficient state generation
                MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

                // UP = false: Use horizontal models (no branch below)
                // Single branch (no connections)
                When.PropertyCondition singleCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(singleCondition, createVariant(horizontalModelId));

                // Single connection variants (horizontal) - use horizontal connected model
                // North connection only (horizontal)
                When.PropertyCondition northHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(northHorizontalCondition, createVariant(horizontalConnectedModelId, 0));

                // South connection only (horizontal)
                When.PropertyCondition southHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(southHorizontalCondition, createVariant(horizontalConnectedModelId, 180));

                // East connection only (horizontal)
                When.PropertyCondition eastHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(eastHorizontalCondition, createVariant(horizontalConnectedModelId, 90));

                // West connection only (horizontal)
                When.PropertyCondition westHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(westHorizontalCondition, createVariant(horizontalConnectedModelId, 270));

                // Two horizontal connections (corner variants) - use horizontal connected two
                // corner model
                // North-West corner (horizontal)
                When.PropertyCondition northWestHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(northWestHorizontalCondition,
                                createVariant(horizontalConnectedTwoCornerModelId, 270));

                // North-East corner (horizontal)
                When.PropertyCondition northEastHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(northEastHorizontalCondition, createVariant(horizontalConnectedTwoCornerModelId, 0));

                // South-West corner (horizontal)
                When.PropertyCondition southWestHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(southWestHorizontalCondition,
                                createVariant(horizontalConnectedTwoCornerModelId, 180));

                // South-East corner (horizontal)
                When.PropertyCondition southEastHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(southEastHorizontalCondition,
                                createVariant(horizontalConnectedTwoCornerModelId, 90));

                // Two opposite horizontal connections (North-South and East-West)
                // North-South opposite (horizontal)
                When.PropertyCondition northSouthHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(northSouthHorizontalCondition, createVariant(horizontalModelId, 0));

                // East-West opposite (horizontal)
                When.PropertyCondition eastWestHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(eastWestHorizontalCondition, createVariant(horizontalModelId, 90));

                // Three horizontal connections - use horizontal connected three model
                // North-East-West (horizontal)
                When.PropertyCondition northEastWestHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(northEastWestHorizontalCondition,
                                createVariant(horizontalConnectedThreeModelId, 270));

                // North-East-South (horizontal)
                When.PropertyCondition northEastSouthHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(northEastSouthHorizontalCondition,
                                createVariant(horizontalConnectedThreeModelId, 0));

                // North-South-West (horizontal)
                When.PropertyCondition northSouthWestHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(northSouthWestHorizontalCondition,
                                createVariant(horizontalConnectedThreeModelId, 180));

                // East-South-West (horizontal)
                When.PropertyCondition eastSouthWestHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(eastSouthWestHorizontalCondition,
                                createVariant(horizontalConnectedThreeModelId, 90));

                // All horizontal connections - use horizontal connected four model
                When.PropertyCondition allHorizontalCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, false);
                stateSupplier.with(allHorizontalCondition, createVariant(horizontalConnectedFourModelId, 0));

                // UP = true: Use vertical models (has branch below)
                // Single branch (no connections)
                When.PropertyCondition singleVerticalCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(singleVerticalCondition, createVariant(baseModelId));

                // Single connection variants (vertical)
                // North connection
                When.PropertyCondition northCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(northCondition, createVariant(connectedModelId));

                // South connection
                When.PropertyCondition southCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(southCondition, createVariant(connectedModelId, 180));

                // East connection
                When.PropertyCondition eastCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(eastCondition, createVariant(connectedModelId, 90));

                // West connection
                When.PropertyCondition westCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(westCondition, createVariant(connectedModelId, 270));

                // Two adjacent connections (corner variants) - use connected_two_corner model
                // North-West corner
                When.PropertyCondition northWestCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(northWestCondition, createVariant(connectedTwoCornerModelId, 270));

                // North-East corner
                When.PropertyCondition northEastCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(northEastCondition, createVariant(connectedTwoCornerModelId, 0)); // No rotation
                                                                                                     // needed

                // South-West corner
                When.PropertyCondition southWestCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(southWestCondition, createVariant(connectedTwoCornerModelId, 180));

                // South-East corner
                When.PropertyCondition southEastCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(southEastCondition, createVariant(connectedTwoCornerModelId, 90));

                // Opposite connections (center variants) - use connected_two model
                // North-South center
                When.PropertyCondition northSouthCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(northSouthCondition, createVariant(connectedTwoModelId, 0)); // No rotation needed

                // East-West center
                When.PropertyCondition eastWestCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(eastWestCondition, createVariant(connectedTwoModelId, 90));

                // Three connections - use connected_three model with appropriate rotation
                // North-East-West
                When.PropertyCondition northEastWestCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, false)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(northEastWestCondition, createVariant(connectedThreeModelId, 270)); // Rotate 270° to
                                                                                                       // align
                                                                                                       // extensions

                // North-East-South
                When.PropertyCondition northEastSouthCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, false)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(northEastSouthCondition, createVariant(connectedThreeModelId, 0)); // No rotation
                                                                                                      // needed

                // North-South-West
                When.PropertyCondition northSouthWestCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, false)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(northSouthWestCondition, createVariant(connectedThreeModelId, 180)); // Rotate 180°
                                                                                                        // to align
                                                                                                        // extensions

                // East-South-West
                When.PropertyCondition eastSouthWestCondition = When.create()
                                .set(WCBranchBlock.NORTH, false)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(eastSouthWestCondition, createVariant(connectedThreeModelId, 90));

                // All connections - use connected_four model
                When.PropertyCondition allConnectionsCondition = When.create()
                                .set(WCBranchBlock.NORTH, true)
                                .set(WCBranchBlock.EAST, true)
                                .set(WCBranchBlock.SOUTH, true)
                                .set(WCBranchBlock.WEST, true)
                                .set(WCBranchBlock.UP, true);
                stateSupplier.with(allConnectionsCondition, createVariant(connectedFourModelId, 0)); // No rotation
                                                                                                     // needed

                // Register the block state using the utility method from ModelExport2
                registerMultipartBlockState(generator, stateSupplier);
        }

        private Identifier createBranchModel(BlockStateModelGenerator generator, Block block, String branchType,
                        String[] texturePaths, String connectionType) {
                // Use the first texture for all faces (branches typically use single texture)
                String texturePath = texturePaths.length > 0 ? texturePaths[0] : "oak_branch";

                // Create texture map
                TextureMap textureMap = new TextureMap()
                                .put(TextureKey.ALL, createBlockIdentifier(texturePath))
                                .put(TextureKey.PARTICLE, createBlockIdentifier(texturePath));

                // Determine model template based on connection type
                Optional<Identifier> parentModel = Optional.empty();
                if ("large_branch".equals(branchType)) {
                        switch (connectionType) {
                                case "base":
                                        parentModel = Optional
                                                        .of(WesterosBlocks.id("block/custom/branches/large_branch"));
                                        break;
                                case "horizontal":
                                        parentModel = Optional
                                                        .of(WesterosBlocks.id(
                                                                        "block/custom/branches/large_branch_horizontal"));
                                        break;
                                case "connected":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_connected"));
                                        break;
                                case "horizontal_connected":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_horizontal_connected"));
                                        break;
                                case "horizontal_connected_two_corner":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_horizontal_connected_two_corner"));
                                        break;
                                case "horizontal_connected_three":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_horizontal_connected_three"));
                                        break;
                                case "horizontal_connected_four":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_horizontal_connected_four"));
                                        break;
                                case "connected_two":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_connected_two"));
                                        break;
                                case "connected_two_corner":
                                        parentModel = Optional
                                                        .of(WesterosBlocks.id(
                                                                        "block/custom/branches/large_branch_connected_two_corner"));
                                        break;
                                case "connected_three":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_connected_three"));
                                        break;
                                case "connected_four":
                                        parentModel = Optional.of(WesterosBlocks
                                                        .id("block/custom/branches/large_branch_connected_four"));
                                        break;
                                default:
                                        parentModel = Optional
                                                        .of(WesterosBlocks.id("block/custom/branches/large_branch"));
                        }
                } else {
                        // Default to base branch model for other branch types
                        parentModel = Optional.of(WesterosBlocks.id("block/custom/branches/large_branch"));
                }

                Identifier modelId = WesterosBlocks.id(
                                "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "")
                                                + "/"
                                                + connectionType);

                Model model = new Model(parentModel, Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
                model.upload(modelId, textureMap, generator.modelCollector);

                return modelId;
        }

        @Override
        public void generateItemModels(ItemModelGenerator generator, Block block) {
                // Use the base model for the item
                String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "")
                                + "/base";
                Model model = new Model(
                                Optional.of(Identifier.of("westerosblocks", modelPath)),
                                Optional.empty());
                generator.register(block.asItem(), model);
        }
}