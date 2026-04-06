package com.westerosblocks.datagen.custom;

import com.westerosblocks.block.custom.WCTableBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

/**
 * Exporter for WCTableBlock2 - generates blockstate and model JSON files
 * for table blocks with connection-based model variants.
 */
public class TableBlockExporter extends BaseBlockExporter {

    public static void registerTableBlock2(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        String texturePath = (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "missingno";

        registerTableBlock2(generator, block, texturePath, texturePath);
    }

    private static void registerTableBlock2(BlockStateModelGenerator generator, Block block, String texturePath, String particleTexture) {
        String blockName = getBlockName(block);

        // Use ModTextureKey.TABLE and TextureKey.PARTICLE
        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.TABLE, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

        // Upload model variants with texture mappings
        Identifier singleModelId = ModModels.TABLE_SINGLE.upload(createNestedModelId(block, "single"), textureMap, generator.modelCollector);
        Identifier leftModelId = ModModels.TABLE_LEFT.upload(createNestedModelId(block, "left"), textureMap, generator.modelCollector);
        Identifier rightModelId = ModModels.TABLE_RIGHT.upload(createNestedModelId(block, "right"), textureMap, generator.modelCollector);
        Identifier middleModelId = ModModels.TABLE_MIDDLE.upload(createNestedModelId(block, "middle"), textureMap, generator.modelCollector);

        // Create blockstate variant map (FACING first, then CONNECTION)
        BlockStateVariantMap.DoubleProperty<Direction, WCTableBlock.ConnectionType> variantMap =
            BlockStateVariantMap.models(WCTableBlock.FACING, WCTableBlock.CONNECTION);

        // Register all combinations of facing and connection
        for (Direction facing : Direction.Type.HORIZONTAL) {
            AxisRotation rotation = toYRotation(getFacingSouthDefaultRotation(facing));

            // SINGLE connection
            variantMap.register(facing, WCTableBlock.ConnectionType.SINGLE,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(singleModelId).withRotationY(rotation)));

            // LEFT connection
            variantMap.register(facing, WCTableBlock.ConnectionType.LEFT,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(leftModelId).withRotationY(rotation)));

            // RIGHT connection
            variantMap.register(facing, WCTableBlock.ConnectionType.RIGHT,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(rightModelId).withRotationY(rotation)));

            // MIDDLE connection
            variantMap.register(facing, WCTableBlock.ConnectionType.MIDDLE,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(middleModelId).withRotationY(rotation)));
        }

        // Register the blockstate with all variants
        generator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block).with(variantMap));

        // Register item model (uses single variant)
        registerParentedItemModel(generator, block, singleModelId);
    }

}
