package com.westerosblocks.datagen.models;

import com.westerosblocks.block.custom.WCTableBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

/**
 * TableBlockExport that extends ModelExport2 for consistent model generation patterns.
 * Handles table blocks with connection-based state generation using multipart block states.
 */
public class TableBlockExport extends ModelExport2 {

    public enum TableType {
        SINGLE("single"),
        DOUBLE("double"),
        CENTER("middle"),
        CORNER("corner");

        private final String name;

        TableType(String name) {
            this.name = name;
        }

        public String asString() {
            return this.name;
        }
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each type using the utility methods from ModelExport2
        Identifier singleModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/table/table_single", 
            texturePath, "single"
        );
        Identifier doubleModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/table/table_double", 
            texturePath, "double"
        );
        Identifier centerModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/table/table_middle", 
            texturePath, "middle"
        );
        Identifier cornerModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/table/table_corner", 
            texturePath, "corner"
        );

        // Use MultipartBlockStateSupplier for more efficient state generation
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // Single table (no connections)
        When.PropertyCondition singleCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(singleCondition, createVariant(singleModelId));

        // Double table variants (one connection)
        // North connection
        When.PropertyCondition northDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(northDoubleCondition, createVariant(doubleModelId, 180));

        // South connection
        When.PropertyCondition southDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(southDoubleCondition, createVariant(doubleModelId));

        // East connection - needs horizontal flip
        When.PropertyCondition eastDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(eastDoubleCondition, createVariant(doubleModelId, 270));

        // West connection - needs horizontal flip
        When.PropertyCondition westDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(westDoubleCondition, createVariant(doubleModelId, 90));

        // Corner variants (two adjacent connections)
        // North-West corner (north=true, west=true, east=false, south=false)
        When.PropertyCondition northWestCornerCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(northWestCornerCondition, createVariant(cornerModelId, 180));

        // North-East corner (north=true, east=true, west=false, south=false)
        When.PropertyCondition northEastCornerCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(northEastCornerCondition, createVariant(cornerModelId, 270));

        // South-West corner (south=true, west=true, north=false, east=false)
        When.PropertyCondition southWestCornerCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(southWestCornerCondition, createVariant(cornerModelId, 90));

        // South-East corner (south=true, east=true, north=false, west=false)
        When.PropertyCondition southEastCornerCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(southEastCornerCondition, createVariant(cornerModelId));

        // Center variants (opposite connections)
        // North-South center
        When.PropertyCondition northSouthCenterCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(northSouthCenterCondition, createVariant(centerModelId));

        // East-West center
        When.PropertyCondition eastWestCenterCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(eastWestCenterCondition, createVariant(centerModelId, 270));

        // Three connections
        // North-East-West
        When.PropertyCondition threeConnectionsCondition1 = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(threeConnectionsCondition1, createVariant(centerModelId, 90));

        // North-East-South
        When.PropertyCondition threeConnectionsCondition2 = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(threeConnectionsCondition2, createVariant(centerModelId, 180));

        // North-South-West
        When.PropertyCondition threeConnectionsCondition3 = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(threeConnectionsCondition3, createVariant(centerModelId, 180));

        // East-South-West
        When.PropertyCondition threeConnectionsCondition4 = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(threeConnectionsCondition4, createVariant(centerModelId, 90));

        // All connections
        When.PropertyCondition allConnectionsCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(allConnectionsCondition, createVariant(centerModelId));

        // Register the block state using the utility method from ModelExport2
        registerMultipartBlockState(generator, stateSupplier);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        generateItemModel(generator, block, "single");
    }
} 