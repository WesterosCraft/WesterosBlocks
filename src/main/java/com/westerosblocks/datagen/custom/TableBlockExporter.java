package com.westerosblocks.datagen.custom;

import com.westerosblocks.block.custom.WCTableBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
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
            BlockStateVariantMap.create(WCTableBlock.FACING, WCTableBlock.CONNECTION);

        // Register all combinations of facing and connection
        for (Direction facing : Direction.Type.HORIZONTAL) {
            VariantSettings.Rotation rotation = toYRotation(getFacingSouthDefaultRotation(facing));

            // SINGLE connection
            variantMap.register(facing, WCTableBlock.ConnectionType.SINGLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, singleModelId)
                            .put(VariantSettings.Y, rotation));

            // LEFT connection
            variantMap.register(facing, WCTableBlock.ConnectionType.LEFT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, leftModelId)
                            .put(VariantSettings.Y, rotation));

            // RIGHT connection
            variantMap.register(facing, WCTableBlock.ConnectionType.RIGHT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, rightModelId)
                            .put(VariantSettings.Y, rotation));

            // MIDDLE connection
            variantMap.register(facing, WCTableBlock.ConnectionType.MIDDLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, middleModelId)
                            .put(VariantSettings.Y, rotation));
        }

        // Register the blockstate with all variants
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(variantMap));

        // Register item model (uses single variant)
        registerParentedItemModel(generator, block, singleModelId);
    }

}
