package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCWallTorchBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TorchBlockExport extends ModelExport {

    public TorchBlockExport(BlockStateModelGenerator generator, Block block) {
        super(generator, block, null);
    }

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block standingTorch, Block wallTorch, String texturePath) {
        // Generate standing torch block state
        generateStandingTorchBlockState(generator, standingTorch, texturePath);
        
        // Generate wall torch block state
        generateWallTorchBlockState(generator, wallTorch, texturePath);
    }

    private static void generateStandingTorchBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the standing torch model
        Identifier standingModelId = createStandingTorchModel(generator, block, texturePath);

        // Create block state variant (standing torch has no properties)
        BlockStateVariant standingVariant = BlockStateVariant.create()
            .put(VariantSettings.MODEL, standingModelId);

        // Use BlockStateBuilder like the original TorchBlockExport
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();
        blockStateBuilder.addVariant("", standingVariant, null, variants);

        // Generate block state files
        generateBlockStateFiles(generator, block, variants);
    }

    private static void generateWallTorchBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the wall torch model
        Identifier wallModelId = createWallTorchModel(generator, block, texturePath);

        // Create variants for each facing direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCWallTorchBlock.FACING)
            .register(net.minecraft.util.math.Direction.NORTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, wallModelId))
            .register(net.minecraft.util.math.Direction.EAST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, wallModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(net.minecraft.util.math.Direction.SOUTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, wallModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(net.minecraft.util.math.Direction.WEST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, wallModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state using the generator's collector
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    private static Identifier createStandingTorchModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for standing torch
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TORCH, Identifier.of("westerosblocks", "block/" + texturePath));

        // Create model identifier
        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID, 
            "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", ""));

        // Create and upload the model using the template
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/untinted/template_torch")),
            Optional.empty(),
            TextureKey.TORCH
        );
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    private static Identifier createWallTorchModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for wall torch
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TORCH, Identifier.of("westerosblocks", "block/" + texturePath));

        // Create model identifier
        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID, 
            "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", ""));

        // Create and upload the model using the template
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/untinted/template_torch_wall")),
            Optional.empty(),
            TextureKey.TORCH
        );
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block standingTorch, Block wallTorch) {
        // Generate item models for both standing and wall torch blocks
        generateSingleItemModel(generator, standingTorch);
        generateSingleItemModel(generator, wallTorch);
    }

    private static void generateSingleItemModel(ItemModelGenerator generator, Block block) {
        // Create texture map for item model
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.LAYER0, Identifier.of("westerosblocks", "block/" + getTexturePath(block)));

        // Create and upload the generated item model
        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            textureMap,
            generator.writer
        );
    }

    private static String getTexturePath(Block block) {
        // Extract texture path from block name
        String blockName = block.getTranslationKey().replace("block.westerosblocks.", "");
        
        // Map block names to texture paths
        return switch (blockName) {
            case "torch" -> "lighting/torch";
            case "torch_unlit" -> "lighting/torch_unlit";
            case "candle" -> "lighting/candle";
            case "candle_unlit" -> "lighting/candle_unlit";
            case "wall_torch" -> "lighting/torch";
            case "wall_torch_unlit" -> "lighting/torch_unlit";
            case "wall_candle" -> "lighting/candle";
            case "wall_candle_unlit" -> "lighting/candle_unlit";
            default -> "lighting/torch"; // fallback
        };
    }
} 