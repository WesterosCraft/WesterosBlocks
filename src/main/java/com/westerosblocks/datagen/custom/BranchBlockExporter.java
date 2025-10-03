package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBranchBlock;

import java.util.Optional;

/**
 * Exporter for branch blocks following block-models.md patterns.
 * Generates models for horizontally and vertically connectable branch blocks with multiple connection states.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (dynamically created for each connection type)</li>
 *   <li>TextureMap builders (ALL and PARTICLE texture keys)</li>
 *   <li>BlockStateSupplier methods (MultipartBlockStateSupplier for complex connections)</li>
 *   <li>Clean datagen methods (registerBranchBlock)</li>
 * </ul>
 *
 * <p><b>Branch Connection Types:</b>
 * <ul>
 *   <li><b>Horizontal (UP=false):</b> 11 variants including single, straight connections, corners, T-junctions, and cross</li>
 *   <li><b>Vertical (UP=true):</b> 11 variants with vertical support pillar</li>
 *   <li><b>Total:</b> 22 multipart variants for all horizontal connection combinations</li>
 * </ul>
 *
 * <p><b>Model Hierarchy:</b>
 * <ul>
 *   <li>base - Single branch with no connections</li>
 *   <li>horizontal - Straight horizontal branch (North-South or East-West)</li>
 *   <li>connected - Vertical single directional connection</li>
 *   <li>horizontal_connected - Horizontal single directional connection</li>
 *   <li>horizontal_connected_two_corner - Horizontal L-shaped corner</li>
 *   <li>horizontal_connected_three - Horizontal T-junction</li>
 *   <li>horizontal_connected_four - Horizontal cross junction</li>
 *   <li>connected_two - Vertical opposite connections (center)</li>
 *   <li>connected_two_corner - Vertical L-shaped corner</li>
 *   <li>connected_three - Vertical T-junction</li>
 *   <li>connected_four - Vertical cross junction with all connections</li>
 * </ul>
 *
 * <p><b>Texture Order:</b> Single texture applied to all faces with {@code TextureKey.ALL}
 *
 * @see WCBranchBlock
 */
public class BranchBlockExporter extends BaseBlockExporter {
    public static void registerBranchBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        registerBranchBlock(generator, block, new String[] { texturePath });
    }
    public static void registerBranchBlock(BlockStateModelGenerator generator, Block block, String... texturePaths) {
        if (texturePaths.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        // Get the branch type from the block
        String branchType = "large_branch"; // Default branch type
        if (block instanceof WCBranchBlock) {
            branchType = ((WCBranchBlock) block).getBranchType();
        }

        // Create models for different connection states
        Identifier baseModelId = createBranchModel(generator, block, branchType, texturePaths, "base");
        Identifier horizontalModelId = createBranchModel(generator, block, branchType, texturePaths, "horizontal");
        Identifier connectedModelId = createBranchModel(generator, block, branchType, texturePaths, "connected");
        Identifier horizontalConnectedModelId = createBranchModel(generator, block, branchType, texturePaths, "horizontal_connected");
        Identifier horizontalConnectedTwoCornerModelId = createBranchModel(generator, block, branchType, texturePaths, "horizontal_connected_two_corner");
        Identifier horizontalConnectedThreeModelId = createBranchModel(generator, block, branchType, texturePaths, "horizontal_connected_three");
        Identifier horizontalConnectedFourModelId = createBranchModel(generator, block, branchType, texturePaths, "horizontal_connected_four");
        Identifier horizontalConnectedUpCornerModelId = createBranchModel(generator, block, branchType, texturePaths, "horizontal_connected_up_corner");
        Identifier connectedTwoModelId = createBranchModel(generator, block, branchType, texturePaths, "connected_two");
        Identifier connectedTwoCornerModelId = createBranchModel(generator, block, branchType, texturePaths, "connected_two_corner");
        Identifier connectedThreeModelId = createBranchModel(generator, block, branchType, texturePaths, "connected_three");
        Identifier connectedFourModelId = createBranchModel(generator, block, branchType, texturePaths, "connected_four");

        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // UP = false: Use horizontal models (no branch below)
        // Single branch (no connections) - use large_branch model when all neighbors
        // are false
        When.PropertyCondition singleCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(singleCondition, createVariant(baseModelId));

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
        stateSupplier.with(southHorizontalCondition, createVariant(horizontalConnectedUpCornerModelId, 180));

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
        stateSupplier.with(northWestHorizontalCondition, createVariant(horizontalConnectedTwoCornerModelId, 270));

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
        stateSupplier.with(southWestHorizontalCondition, createVariant(horizontalConnectedTwoCornerModelId, 180));

        // South-East corner (horizontal)
        When.PropertyCondition southEastHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(southEastHorizontalCondition, createVariant(horizontalConnectedTwoCornerModelId, 90));

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
        stateSupplier.with(northEastWestHorizontalCondition, createVariant(horizontalConnectedThreeModelId, 270));

        // North-East-South (horizontal)
        When.PropertyCondition northEastSouthHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northEastSouthHorizontalCondition, createVariant(horizontalConnectedThreeModelId, 0));

        // North-South-West (horizontal)
        When.PropertyCondition northSouthWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northSouthWestHorizontalCondition, createVariant(horizontalConnectedThreeModelId, 180));

        // East-South-West (horizontal)
        When.PropertyCondition eastSouthWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(eastSouthWestHorizontalCondition, createVariant(horizontalConnectedThreeModelId, 90));

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
        stateSupplier.with(northEastCondition, createVariant(connectedTwoCornerModelId, 0));

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
        stateSupplier.with(northSouthCondition, createVariant(connectedTwoModelId, 0));

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
        stateSupplier.with(northEastWestCondition, createVariant(connectedThreeModelId, 270));

        // North-East-South
        When.PropertyCondition northEastSouthCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northEastSouthCondition, createVariant(connectedThreeModelId, 0));

        // North-South-West
        When.PropertyCondition northSouthWestCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northSouthWestCondition, createVariant(connectedThreeModelId, 180));

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
        stateSupplier.with(allConnectionsCondition, createVariant(connectedFourModelId, 0));


        generator.blockStateCollector.accept(stateSupplier);
        generator.registerParentedItemModel(block, baseModelId);
    }

    /**
     * Creates a branch model with the specified connection type
     */
    private static Identifier createBranchModel(BlockStateModelGenerator generator, Block block, String branchType,
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
                    parentModel = Optional.of(WesterosBlocks.id("block/branches/large_branch"));
                    break;
                case "horizontal":
                    parentModel = Optional.of(WesterosBlocks.id("block/branches/large_branch_horizontal"));
                    break;
                case "connected":
                    parentModel = Optional.of(WesterosBlocks.id("block/branches/large_branch_connected"));
                    break;
                case "horizontal_connected":
                    parentModel = Optional
                            .of(WesterosBlocks.id("block/branches/large_branch_horizontal_connected"));
                    break;
                case "horizontal_connected_two_corner":
                    parentModel = Optional.of(
                            WesterosBlocks.id("block/branches/large_branch_horizontal_connected_two_corner"));
                    break;
                case "horizontal_connected_three":
                    parentModel = Optional
                            .of(WesterosBlocks.id("block/branches/large_branch_horizontal_connected_three"));
                    break;
                case "horizontal_connected_four":
                    parentModel = Optional
                            .of(WesterosBlocks.id("block/branches/large_branch_horizontal_connected_four"));
                    break;
                case "horizontal_connected_up_corner":
                    parentModel = Optional
                            .of(WesterosBlocks.id("block/branches/large_branch_horizontal_connected_up_corner"));
                    break;
                case "connected_two":
                    parentModel = Optional.of(WesterosBlocks.id("block/branches/large_branch_connected_two"));
                    break;
                case "connected_two_corner":
                    parentModel = Optional
                            .of(WesterosBlocks.id("block/branches/large_branch_connected_two_corner"));
                    break;
                case "connected_three":
                    parentModel = Optional.of(WesterosBlocks.id("block/branches/large_branch_connected_three"));
                    break;
                case "connected_four":
                    parentModel = Optional.of(WesterosBlocks.id("block/branches/large_branch_connected_four"));
                    break;
                default:
                    parentModel = Optional.of(WesterosBlocks.id("block/branches/large_branch"));
            }
        } else {
            // Default to base branch model for other branch types
            parentModel = Optional.of(WesterosBlocks.id("block/custom/branches/large_branch"));
        }

        String blockName = getBlockName(block);
        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/" + connectionType);

        // Create and upload the model
        Model model = new Model(parentModel, Optional.empty(),
                TextureKey.ALL, TextureKey.PARTICLE);
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }
}
