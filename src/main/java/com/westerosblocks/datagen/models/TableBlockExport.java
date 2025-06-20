package com.westerosblocks.datagen.models;

import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCTableBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class TableBlockExport {
    private static final String PARTICLE_KEY = "particle";
    private static final String TEXTURE_KEY = "1";

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
        Identifier singleModelId = createModel(generator, block, TableType.SINGLE, texturePath);
        Identifier doubleModelId = createModel(generator, block, TableType.DOUBLE, texturePath);
        Identifier centerModelId = createModel(generator, block, TableType.CENTER, texturePath);
        Identifier cornerModelId = createModel(generator, block, TableType.CORNER, texturePath);

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

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    private static Identifier createModel(BlockStateModelGenerator generator, Block block, TableType type, String texturePath) {
        JsonObject modelJson = new JsonObject();
        
        // Use the existing models as parent
        String parentModelPath = "westerosblocks:block/custom/table/table_" + type.asString();
        modelJson.addProperty("parent", parentModelPath);
        modelJson.addProperty("credit", "Generated by WesterosBlocks");

        // Override the texture to use the dynamic texture path
        JsonObject textures = new JsonObject();
        textures.addProperty(TEXTURE_KEY, texturePath);
        textures.addProperty(PARTICLE_KEY, texturePath);
        modelJson.add("textures", textures);

        // Create a unique model ID for this block and type
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + type.asString();
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Register the model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_single";
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 