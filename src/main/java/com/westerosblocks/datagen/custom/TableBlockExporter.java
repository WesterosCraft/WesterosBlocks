package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCTableBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;

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

/**
 * Exporter for table blocks following block-models.md patterns.
 * Generates models for connectable table blocks with multiple connection states.
 *
 * @see WCTableBlock
 */
public class TableBlockExporter extends BaseBlockExporter {

        /**
         * Registers a custom table block with connection-based state generation (legacy method for CustomBlockBuilder)
         */
        public static void registerCustomTableBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
                registerCustomTableBlock(generator, block, texturePath, texturePath);
        }

        /**
         * Registers a custom table block with connection-based state generation
         */
        public static void registerCustomTableBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
                // Extract texture from definition
                definition.validateTextureData();
                List<String> textureList = definition.getTextures();
                String texturePath = (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "missingno";

                registerCustomTableBlock(generator, block, texturePath, texturePath);
        }

        /**
         * Internal implementation for registering table blocks
         */
        private static void registerCustomTableBlock(BlockStateModelGenerator generator, Block block, String texturePath, String particleTexture) {
                String blockName = getBlockName(block);

                TextureMap textureMap = new TextureMap()
                        .put(ModTextureKey.TABLE, createBlockIdentifier(texturePath))
                        .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

                Model singleParent = new Model(Optional.of(WesterosBlocks.id("block/table/table_single")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);
                Model doubleParent = new Model(Optional.of(WesterosBlocks.id("block/table/table_double")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);
                Model centerParent = new Model(Optional.of(WesterosBlocks.id("block/table/table_middle")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);
                Model cornerParent = new Model(Optional.of(WesterosBlocks.id("block/table/table_corner")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);

                Identifier singleModelId = singleParent.upload(createNestedModelId(block, "table_single"), textureMap, generator.modelCollector);
                Identifier doubleModelId = doubleParent.upload(createNestedModelId(block, "table_double"), textureMap, generator.modelCollector);
                Identifier centerModelId = centerParent.upload(createNestedModelId(block, "table_middle"), textureMap, generator.modelCollector);
                Identifier cornerModelId = cornerParent.upload(createNestedModelId(block, "table_corner"), textureMap, generator.modelCollector);

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
                stateSupplier.with(northDoubleCondition, BlockStateVariant.create().put(VariantSettings.MODEL, doubleModelId).put(VariantSettings.Y, VariantSettings.Rotation.R180));

                // South connection
                When.PropertyCondition southDoubleCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(southDoubleCondition, BlockStateVariant.create().put(VariantSettings.MODEL, doubleModelId));

                // East connection
                When.PropertyCondition eastDoubleCondition = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, false)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(eastDoubleCondition, BlockStateVariant.create().put(VariantSettings.MODEL, doubleModelId).put(VariantSettings.Y, VariantSettings.Rotation.R270));

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
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R0));

                // North-East-South
                When.PropertyCondition threeConnectionsCondition2 = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, false);
                stateSupplier.with(threeConnectionsCondition2,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, centerModelId)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                        .put(VariantSettings.UVLOCK, true));

                // North-South-West
                When.PropertyCondition threeConnectionsCondition3 = When.create()
                                .set(WCTableBlock.NORTH, true)
                                .set(WCTableBlock.EAST, false)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(threeConnectionsCondition3,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, centerModelId)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                                        .put(VariantSettings.UVLOCK, true));

                // East-South-West
                When.PropertyCondition threeConnectionsCondition4 = When.create()
                                .set(WCTableBlock.NORTH, false)
                                .set(WCTableBlock.EAST, true)
                                .set(WCTableBlock.SOUTH, true)
                                .set(WCTableBlock.WEST, true);
                stateSupplier.with(threeConnectionsCondition4,
                                BlockStateVariant.create().put(VariantSettings.MODEL, centerModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R180));

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
                registerSimpleItemModel(generator, block, createBlockIdentifier(texturePath));
        }
}
