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
        Identifier connectedModelId = createBranchModel(generator, block, branchType, texturePaths, "connected");

        // Use MultipartBlockStateSupplier for more efficient state generation
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // Single branch (no connections)
        When.PropertyCondition singleCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(singleCondition, createVariant(baseModelId));

        // Single connection variants
        // North connection
        When.PropertyCondition northCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(northCondition, createVariant(connectedModelId));

        // South connection
        When.PropertyCondition southCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(southCondition, createVariant(connectedModelId, 180));

        // East connection
        When.PropertyCondition eastCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(eastCondition, createVariant(connectedModelId, 90));

        // West connection
        When.PropertyCondition westCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(westCondition, createVariant(connectedModelId, 270));

        // Two adjacent connections (corner variants)
        // North-West corner
        When.PropertyCondition northWestCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(northWestCondition, createVariant(connectedModelId, 270));

        // North-East corner
        When.PropertyCondition northEastCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(northEastCondition, createVariant(connectedModelId, 90));

        // South-West corner
        When.PropertyCondition southWestCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(southWestCondition, createVariant(connectedModelId, 180));

        // South-East corner
        When.PropertyCondition southEastCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(southEastCondition, createVariant(connectedModelId, 180));

        // Opposite connections (center variants)
        // North-South center
        When.PropertyCondition northSouthCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(northSouthCondition, createVariant(connectedModelId, 180));

        // East-West center
        When.PropertyCondition eastWestCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(eastWestCondition, createVariant(connectedModelId, 90));

        // Three connections
        // North-East-West
        When.PropertyCondition northEastWestCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(northEastWestCondition, createVariant(connectedModelId, 90));

        // North-East-South
        When.PropertyCondition northEastSouthCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false);
        stateSupplier.with(northEastSouthCondition, createVariant(connectedModelId, 180));

        // North-South-West
        When.PropertyCondition northSouthWestCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(northSouthWestCondition, createVariant(connectedModelId, 180));

        // East-South-West
        When.PropertyCondition eastSouthWestCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(eastSouthWestCondition, createVariant(connectedModelId, 90));

        // All connections
        When.PropertyCondition allConnectionsCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true);
        stateSupplier.with(allConnectionsCondition, createVariant(connectedModelId, 180));

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
                    parentModel = Optional.of(WesterosBlocks.id("block/custom/branches/large_branch"));
                    break;
                case "connected":
                    parentModel = Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_connected"));
                    break;
                default:
                    parentModel = Optional.of(WesterosBlocks.id("block/custom/branches/large_branch"));
            }
        } else {
            // Default to base branch model for other branch types
            parentModel = Optional.of(WesterosBlocks.id("block/custom/branches/large_branch"));
        }

        Identifier modelId = WesterosBlocks.id(
                "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "/"
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