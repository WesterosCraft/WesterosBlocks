package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;


public class BuntingBlockExporter extends BaseBlockExporter {

    public static void registerBuntingBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String blockName = getBlockName(block);

        // Build texture map from definition
        String texturePath = definition.getTextures().get(0);
        Identifier textureId = createBlockIdentifier(texturePath);
        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.ZERO, textureId)
                .put(TextureKey.PARTICLE, textureId);

        // Upload wall model
        Identifier wallModelId = uploadModel(ModModels.BUNTING_WALL, block, "wall_v1", textureMap, generator.modelCollector);

        // Wall variant blockstate
        BlockStateVariantMap wallVariants = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
                .register(Direction.NORTH, createVariant(wallModelId))
                .register(Direction.EAST, createVariant(wallModelId, 90))
                .register(Direction.SOUTH, createVariant(wallModelId, 180))
                .register(Direction.WEST, createVariant(wallModelId, 270));

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(wallVariants)
        );

        // Item model parented to wall model
        registerParentedItemModel(generator, block, wallModelId);

        // Ceiling variant blockstate
        String ceilingName = definition.getBlockName() + "_ceiling";
        Block ceilingBlock = Registries.BLOCK.get(WesterosBlocks.id(ceilingName));

        if (ceilingBlock == null || !Registries.BLOCK.containsId(WesterosBlocks.id(ceilingName))) {
            WesterosBlocks.LOGGER.warn("Could not find ceiling block for bunting: {}", ceilingName);
            return;
        }

        // Upload ceiling model
        Identifier ceilingModelId = uploadModel(ModModels.BUNTING_CEILING, block, "ceiling_v1", textureMap, generator.modelCollector);

        BlockStateVariantMap ceilingVariants = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
                .register(Direction.NORTH, createVariant(ceilingModelId))
                .register(Direction.EAST, createVariant(ceilingModelId, 90))
                .register(Direction.SOUTH, createVariant(ceilingModelId, 180))
                .register(Direction.WEST, createVariant(ceilingModelId, 270));

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(ceilingBlock).coordinate(ceilingVariants)
        );
    }
}
