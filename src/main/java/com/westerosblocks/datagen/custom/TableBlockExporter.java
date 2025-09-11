package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCTableBlock;

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

public class TableBlockExporter extends BaseBlockExporter {

        /**
         * Registers a custom table block with connection-based state generation
         */
        public static void registerCustomTableBlock(BlockStateModelGenerator generator, Block block,
                        String texturePath) {
                String blockName = getBlockName(block);

                TextureKey KEY1 = TextureKey.of("1");

                TextureMap textureMap = new TextureMap()
                                .put(KEY1, Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath));

                Model singleParent = new Model(
                                java.util.Optional.of(Identifier.of(WesterosBlocks.MOD_ID,
                                                "block/table/table_single")),
                                java.util.Optional.empty(), KEY1);
                Model doubleParent = new Model(
                                java.util.Optional.of(Identifier.of(WesterosBlocks.MOD_ID,
                                                "block/table/table_double")),
                                java.util.Optional.empty(), KEY1);
                Model centerParent = new Model(
                                java.util.Optional.of(Identifier.of(WesterosBlocks.MOD_ID,
                                                "block/table/table_middle")),
                                java.util.Optional.empty(), KEY1);
                Model cornerParent = new Model(
                                java.util.Optional.of(Identifier.of(WesterosBlocks.MOD_ID,
                                                "block/table/table_corner")),
                                java.util.Optional.empty(), KEY1);

                Identifier singleModelId = singleParent.upload(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/table_single"),
                                textureMap, generator.modelCollector);

                Identifier doubleModelId = doubleParent.upload(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/table_double"),
                                textureMap, generator.modelCollector);

                Identifier centerModelId = centerParent.upload(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/table_middle"),
                                textureMap, generator.modelCollector);

                Identifier cornerModelId = cornerParent.upload(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/table_corner"),
                                textureMap, generator.modelCollector);

                MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

                // Single table (no connections)
                When.PropertyCondition singleCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(singleCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, singleModelId));

                // Double table variants (one connection)
                // North connection
                When.PropertyCondition northDoubleCondition = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(northDoubleCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, doubleModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R180));

                // South connection
                When.PropertyCondition southDoubleCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(southDoubleCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, doubleModelId));

                // East connection
                When.PropertyCondition eastDoubleCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(eastDoubleCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, doubleModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

                // West connection
                When.PropertyCondition westDoubleCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(westDoubleCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, doubleModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90));

                // Corner variants (two adjacent connections)
                // North-West corner
                When.PropertyCondition northWestCornerCondition = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(northWestCornerCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, cornerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R180));

                // North-East corner
                When.PropertyCondition northEastCornerCondition = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(northEastCornerCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, cornerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

                // South-West corner
                When.PropertyCondition southWestCornerCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(southWestCornerCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, cornerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90));

                // South-East corner
                When.PropertyCondition southEastCornerCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(southEastCornerCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, cornerModelId));

                // Center variants (opposite connections)
                // North-South center
                When.PropertyCondition northSouthCenterCondition = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(northSouthCenterCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId));

                // East-West center
                When.PropertyCondition eastWestCenterCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(eastWestCenterCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

                // Three connections
                // North-East-West
                When.PropertyCondition threeConnectionsCondition1 = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(threeConnectionsCondition1,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90));

                // North-East-South
                When.PropertyCondition threeConnectionsCondition2 = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(threeConnectionsCondition2,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R180));

                // North-South-West
                When.PropertyCondition threeConnectionsCondition3 = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(threeConnectionsCondition3,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R180));

                // East-South-West
                When.PropertyCondition threeConnectionsCondition4 = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(threeConnectionsCondition4,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90));

                // All connections
                When.PropertyCondition allConnectionsCondition = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(allConnectionsCondition,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId));

                // Register the block state
                generator.blockStateCollector.accept(stateSupplier);

                // Register item model using the single variant
                generator.registerParentedItemModel(block, singleModelId);
        }
}
