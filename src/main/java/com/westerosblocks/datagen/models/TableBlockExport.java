package com.westerosblocks.datagen.models;

import com.westerosblocks.block.custom.WCTableBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class TableBlockExport {

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

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each type
        Identifier singleModelId = ModelExportUtils.createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/table/table_single", 
            texturePath, "single"
        );
        Identifier doubleModelId = ModelExportUtils.createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/table/table_double", 
            texturePath, "double"
        );
        Identifier centerModelId = ModelExportUtils.createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/table/table_middle", 
            texturePath, "middle"
        );
        Identifier cornerModelId = ModelExportUtils.createModelWithKey1(
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
        stateSupplier.with(singleCondition, BlockStateVariant.create().put(VariantSettings.MODEL, singleModelId));

        // Double table variants (one connection)
        // North connection
        When.PropertyCondition northDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(northDoubleCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, doubleModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // South connection
        When.PropertyCondition southDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(southDoubleCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, doubleModelId));

        // East connection - needs horizontal flip
        When.PropertyCondition eastDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(eastDoubleCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, doubleModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // West connection - needs horizontal flip
        When.PropertyCondition westDoubleCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(westDoubleCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, doubleModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // Corner variants (two adjacent connections)
        // North-West corner (north=true, west=true, east=false, south=false)
        When.PropertyCondition northWestCornerCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(northWestCornerCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, cornerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // North-East corner (north=true, east=true, west=false, south=false)
        When.PropertyCondition northEastCornerCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(northEastCornerCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, cornerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // South-West corner (south=true, west=true, north=false, east=false)
        When.PropertyCondition southWestCornerCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(southWestCornerCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, cornerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // South-East corner (south=true, east=true, north=false, west=false)
        When.PropertyCondition southEastCornerCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(southEastCornerCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, cornerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R0));

        // Center variants (opposite connections)
        // North-South center
        When.PropertyCondition northSouthCenterCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(northSouthCenterCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, centerModelId));

        // East-West center
        When.PropertyCondition eastWestCenterCondition = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(eastWestCenterCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, centerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Three connections
        // North-East-West
        When.PropertyCondition threeConnectionsCondition1 = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, false)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(threeConnectionsCondition1, BlockStateVariant.create()
            .put(VariantSettings.MODEL, centerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // North-East-South
        When.PropertyCondition threeConnectionsCondition2 = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, false);
        stateSupplier.with(threeConnectionsCondition2, BlockStateVariant.create()
            .put(VariantSettings.MODEL, centerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // North-South-West
        When.PropertyCondition threeConnectionsCondition3 = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, false)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(threeConnectionsCondition3, BlockStateVariant.create()
            .put(VariantSettings.MODEL, centerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R180));

        // East-South-West
        When.PropertyCondition threeConnectionsCondition4 = When.create()
            .set(WCTableBlock.NORTH, false)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(threeConnectionsCondition4, BlockStateVariant.create()
            .put(VariantSettings.MODEL, centerModelId)
            .put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // All connections
        When.PropertyCondition allConnectionsCondition = When.create()
            .set(WCTableBlock.NORTH, true)
            .set(WCTableBlock.EAST, true)
            .set(WCTableBlock.SOUTH, true)
            .set(WCTableBlock.WEST, true);
        stateSupplier.with(allConnectionsCondition, BlockStateVariant.create()
            .put(VariantSettings.MODEL, centerModelId));

        // Register the block state using the generator's collector
        generator.blockStateCollector.accept(stateSupplier);
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        ModelExportUtils.generateItemModel(generator, block, "single");
    }
} 