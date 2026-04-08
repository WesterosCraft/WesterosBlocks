package com.westerosblocks.datagen.custom;

import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ArrowSlitBlockExporter extends BaseBlockExporter {

    public static void registerCustomArrowSlitBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        java.util.List<String> textureList = definition.getTextures();
        String texturePath = (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "missingno";

        Identifier textureId = createBlockIdentifier(texturePath);

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TEXTURE, textureId)
                .put(TextureKey.PARTICLE, textureId);

        // Upload 4 child models that parent to the arrow slit Blockbench models
        Identifier singleModelId = ModModels.ARROW_SLIT_SINGLE.upload(
                createNestedModelId(block, "single"), textureMap, generator.modelCollector);
        Identifier bottomModelId = ModModels.ARROW_SLIT_BOTTOM.upload(
                createNestedModelId(block, "bottom"), textureMap, generator.modelCollector);
        Identifier topModelId = ModModels.ARROW_SLIT_TOP.upload(
                createNestedModelId(block, "top"), textureMap, generator.modelCollector);
        Identifier middleModelId = ModModels.ARROW_SLIT_MIDDLE.upload(
                createNestedModelId(block, "middle"), textureMap, generator.modelCollector);

        // Build blockstate variants for FACING x UP x DOWN
        BlockStateVariantMap.TripleProperty<Direction, Boolean, Boolean> variantMap =
                BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.UP, WCArrowSlitBlock.DOWN);

        for (Direction facing : Direction.Type.HORIZONTAL) {
            int rotation = getRotationForDirection(facing);
            variantMap.register(facing, false, false, createVariant(singleModelId, rotation));
            variantMap.register(facing, true, false, createVariant(bottomModelId, rotation));
            variantMap.register(facing, false, true, createVariant(topModelId, rotation));
            variantMap.register(facing, true, true, createVariant(middleModelId, rotation));
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));
        generator.registerParentedItemModel(block, singleModelId);
    }
}
