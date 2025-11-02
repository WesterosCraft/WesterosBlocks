package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBranchBlock;
import com.westerosblocks.data.BlockDefinition;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.MultipartBlockStateSupplier;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.When;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;


public class BranchBlockExporter extends BaseBlockExporter {


    /**
     * Registers a custom branch block with connection-based state generation
     */
    public static void registerCustomBranchBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        String texturePath = (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "missingno";

        registerCustomBranchBlock(generator, block, texturePath, texturePath);
    }

    /**
     * Internal implementation for registering branch blocks
     */
    private static void registerCustomBranchBlock(BlockStateModelGenerator generator, Block block, String texturePath, String particleTexture) {
        String blockName = getBlockName(block);

        TextureMap textureMap = new TextureMap()
            .put(TextureKey.ALL, createBlockIdentifier(texturePath))
            .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

        // Create parent model references for all branch connection states
        Model baseParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model horizontalParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_horizontal")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model connectedParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_connected")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model horizontalConnectedParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_horizontal_connected")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model horizontalConnectedTwoCornerParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_horizontal_connected_two_corner")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model horizontalConnectedThreeParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_horizontal_connected_three")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model horizontalConnectedFourParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_horizontal_connected_four")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model connectedTwoParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_connected_two")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model connectedTwoCornerParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_connected_two_corner")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model connectedThreeParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_connected_three")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
        Model connectedFourParent = new Model(Optional.of(WesterosBlocks.id("block/custom/branches/large_branch_connected_four")), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);

        // Upload all models with nested IDs
        Identifier baseModelId = baseParent.upload(createNestedModelId(block, "large_branch"), textureMap, generator.modelCollector);
        Identifier horizontalModelId = horizontalParent.upload(createNestedModelId(block, "large_branch_horizontal"), textureMap, generator.modelCollector);
        Identifier connectedModelId = connectedParent.upload(createNestedModelId(block, "large_branch_connected"), textureMap, generator.modelCollector);
        Identifier horizontalConnectedModelId = horizontalConnectedParent.upload(createNestedModelId(block, "large_branch_horizontal_connected"), textureMap, generator.modelCollector);
        Identifier horizontalConnectedTwoCornerModelId = horizontalConnectedTwoCornerParent.upload(createNestedModelId(block, "large_branch_horizontal_connected_two_corner"), textureMap, generator.modelCollector);
        Identifier horizontalConnectedThreeModelId = horizontalConnectedThreeParent.upload(createNestedModelId(block, "large_branch_horizontal_connected_three"), textureMap, generator.modelCollector);
        Identifier horizontalConnectedFourModelId = horizontalConnectedFourParent.upload(createNestedModelId(block, "large_branch_horizontal_connected_four"), textureMap, generator.modelCollector);
        Identifier connectedTwoModelId = connectedTwoParent.upload(createNestedModelId(block, "large_branch_connected_two"), textureMap, generator.modelCollector);
        Identifier connectedTwoCornerModelId = connectedTwoCornerParent.upload(createNestedModelId(block, "large_branch_connected_two_corner"), textureMap, generator.modelCollector);
        Identifier connectedThreeModelId = connectedThreeParent.upload(createNestedModelId(block, "large_branch_connected_three"), textureMap, generator.modelCollector);
        Identifier connectedFourModelId = connectedFourParent.upload(createNestedModelId(block, "large_branch_connected_four"), textureMap, generator.modelCollector);

        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // UP = false: Use horizontal models (no branch below)
        // Single branch (no connections)
        When.PropertyCondition singleCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(singleCondition, BlockStateVariant.create().put(VariantSettings.MODEL, baseModelId));

        // Single connection variants (horizontal)
        // North connection only
        When.PropertyCondition northHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedModelId));

        // South connection only
        When.PropertyCondition southHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(southHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedModelId).put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // East connection only
        When.PropertyCondition eastHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(eastHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // West connection only
        When.PropertyCondition westHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(westHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedModelId).put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Two horizontal connections (corner variants)
        // North-West corner
        When.PropertyCondition northWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northWestHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedTwoCornerModelId).put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // North-East corner
        When.PropertyCondition northEastHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northEastHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedTwoCornerModelId));

        // South-West corner
        When.PropertyCondition southWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(southWestHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedTwoCornerModelId).put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // South-East corner
        When.PropertyCondition southEastHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(southEastHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedTwoCornerModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // Two opposite horizontal connections (North-South and East-West)
        // North-South opposite
        When.PropertyCondition northSouthHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northSouthHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalModelId));

        // East-West opposite
        When.PropertyCondition eastWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(eastWestHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // Three horizontal connections
        // North-East-West
        When.PropertyCondition northEastWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northEastWestHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedThreeModelId).put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // North-East-South
        When.PropertyCondition northEastSouthHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northEastSouthHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedThreeModelId));

        // North-South-West
        When.PropertyCondition northSouthWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(northSouthWestHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedThreeModelId).put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // East-South-West
        When.PropertyCondition eastSouthWestHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(eastSouthWestHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedThreeModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // All horizontal connections
        When.PropertyCondition allHorizontalCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, false);
        stateSupplier.with(allHorizontalCondition, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalConnectedFourModelId));

        // UP = true: Use vertical models (has branch below)
        // Single branch (no connections)
        When.PropertyCondition singleVerticalCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(singleVerticalCondition, BlockStateVariant.create()
                .put(VariantSettings.MODEL, baseModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // Single connection variants (vertical)
        // North connection
        When.PropertyCondition northCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedModelId));

        // South connection
        When.PropertyCondition southCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(southCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedModelId).put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // East connection
        When.PropertyCondition eastCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(eastCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // West connection
        When.PropertyCondition westCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(westCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedModelId).put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Two adjacent connections (corner variants)
        // North-West corner
        When.PropertyCondition northWestCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northWestCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedTwoCornerModelId).put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // North-East corner
        When.PropertyCondition northEastCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northEastCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedTwoCornerModelId));

        // South-West corner
        When.PropertyCondition southWestCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(southWestCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedTwoCornerModelId).put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // South-East corner
        When.PropertyCondition southEastCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(southEastCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedTwoCornerModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // Opposite connections (center variants)
        // North-South center
        When.PropertyCondition northSouthCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northSouthCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedTwoModelId));

        // East-West center
        When.PropertyCondition eastWestCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(eastWestCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedTwoModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // Three connections
        // North-East-West
        When.PropertyCondition northEastWestCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, false)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northEastWestCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedThreeModelId).put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // North-East-South
        When.PropertyCondition northEastSouthCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, false)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northEastSouthCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedThreeModelId));

        // North-South-West
        When.PropertyCondition northSouthWestCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, false)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(northSouthWestCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedThreeModelId).put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // East-South-West
        When.PropertyCondition eastSouthWestCondition = When.create()
                .set(WCBranchBlock.NORTH, false)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(eastSouthWestCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedThreeModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // All connections
        When.PropertyCondition allConnectionsCondition = When.create()
                .set(WCBranchBlock.NORTH, true)
                .set(WCBranchBlock.EAST, true)
                .set(WCBranchBlock.SOUTH, true)
                .set(WCBranchBlock.WEST, true)
                .set(WCBranchBlock.UP, true);
        stateSupplier.with(allConnectionsCondition, BlockStateVariant.create().put(VariantSettings.MODEL, connectedFourModelId));

        // Register the block state
        generator.blockStateCollector.accept(stateSupplier);

        // Register item model using the base variant
        registerSimpleItemModel(generator, block, createBlockIdentifier(texturePath));
    }
}
