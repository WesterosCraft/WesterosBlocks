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

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCTableBlock.NORTH, WCTableBlock.EAST, WCTableBlock.SOUTH, WCTableBlock.WEST, WCTableBlock.WATERLOGGED)
            .register(false, false, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            .register(false, false, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            
            .register(false, false, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, false, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, false, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, false, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, true, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(false, true, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, false, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(true, false, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))

            // Corner variants - 2 adjacent sides connected
            // North-West corner (north=true, west=true, east=false, south=false)
            .register(false, false, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, false, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            
            // North-East corner (north=true, east=true, west=false, south=false)
            .register(true, true, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, true, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            
            // South-West corner (south=true, west=true, north=false, east=false)
            .register(false, true, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R0))
            .register(false, true, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R0))
            
            // South-East corner (south=true, east=true, north=false, west=false)
            .register(true, false, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(true, false, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            
            // North-South center (north=true, south=true, east=false, west=false)
            .register(true, false, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, false, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            
            // East-West center (east=true, west=true, north=false, south=false)
            .register(false, true, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(false, true, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))

            .register(false, true, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, true, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(true, false, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, false, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, true, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, true, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId));

        ModelExportUtils.registerBlockState(generator, block, variants);
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        ModelExportUtils.generateItemModel(generator, block, "single");
    }
} 