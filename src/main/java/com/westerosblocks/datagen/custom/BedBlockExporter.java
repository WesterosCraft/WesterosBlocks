package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.enums.BedPart;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class BedBlockExporter extends BaseBlockExporter {
    private static Model createBedPartModel(String bedType, boolean isHead, boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String partSuffix = isHead ? "_head" : "_foot";

        String bedTypeName = switch (bedType) {
            case "raised" -> "bed_raised";
            case "hammock" -> "bed_hammock";
            default -> "bed";
        };

        String path = tintPath + bedTypeName + partSuffix;
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
            TextureKey.PARTICLE, ModTextureKey.BED_TOP, ModTextureKey.BED_SIDE, ModTextureKey.BED_END);
    }

    private static Model getBedItemModel(boolean tinted) {
        return tinted ? ModModels.BED_ITEM_TINTED : ModModels.BED_ITEM_UNTINTED;
    }

    private static TextureMap createBedPartTextureMap(String[] textures, boolean isHead) {
        if (isHead) {
            // Head uses: texture[0]=bedtop+particle, texture[2]=bedside, texture[4]=bedend
            return new TextureMap()
                    .put(TextureKey.PARTICLE, createBlockIdentifier(textures[0]))
                    .put(ModTextureKey.BED_TOP, createBlockIdentifier(textures[0]))
                    .put(ModTextureKey.BED_SIDE, createBlockIdentifier(textures[2]))
                    .put(ModTextureKey.BED_END, createBlockIdentifier(textures[4]));
        } else {
            // Foot uses: texture[1]=bedtop+particle, texture[3]=bedside, texture[5]=bedend
            return new TextureMap()
                    .put(TextureKey.PARTICLE, createBlockIdentifier(textures[1]))
                    .put(ModTextureKey.BED_TOP, createBlockIdentifier(textures[1]))
                    .put(ModTextureKey.BED_SIDE, createBlockIdentifier(textures[3]))
                    .put(ModTextureKey.BED_END, createBlockIdentifier(textures[5]));
        }
    }

    private static TextureMap createBedItemTextureMap(String[] textures) {
        return new TextureMap()
                .put(ModTextureKey.BED_TOP, createBlockIdentifier(textures[0]))
                .put(ModTextureKey.BED_TOP2, createBlockIdentifier(textures[1]))
                .put(ModTextureKey.BED_SIDE, createBlockIdentifier(textures[2]))
                .put(ModTextureKey.BED_SIDE2, createBlockIdentifier(textures[3]))
                .put(ModTextureKey.BED_END, createBlockIdentifier(textures[4]))
                .put(ModTextureKey.BED_END2, createBlockIdentifier(textures[5]));
    }

    private static VariantsBlockStateSupplier createBedBlockstate(Block block, Identifier headModelId, Identifier footModelId) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(
                        BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BED_PART)
                                .register(Direction.NORTH, BedPart.FOOT, createVariant(footModelId, 180))
                                .register(Direction.EAST, BedPart.FOOT, createVariant(footModelId, 270))
                                .register(Direction.SOUTH, BedPart.FOOT, createVariant(footModelId, 0))
                                .register(Direction.WEST, BedPart.FOOT, createVariant(footModelId, 90))
                                .register(Direction.NORTH, BedPart.HEAD, createVariant(headModelId, 180))
                                .register(Direction.EAST, BedPart.HEAD, createVariant(headModelId, 270))
                                .register(Direction.SOUTH, BedPart.HEAD, createVariant(headModelId, 0))
                                .register(Direction.WEST, BedPart.HEAD, createVariant(headModelId, 90))
                );
    }

    public static void registerBedBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                       String bedType, String[] textures) {
        if (textures.length != 6) {
            throw new IllegalArgumentException("Bed blocks require exactly 6 textures, got " + textures.length);
        }

        // Create texture maps for head and foot
        TextureMap headTextureMap = createBedPartTextureMap(textures, true);
        TextureMap footTextureMap = createBedPartTextureMap(textures, false);

        // Upload head and foot models
        Identifier headModelId = createBedPartModel(bedType, true, tinted)
                .upload(createNestedModelId(block, "head"), headTextureMap, generator.modelCollector);
        Identifier footModelId = createBedPartModel(bedType, false, tinted)
                .upload(createNestedModelId(block, "foot"), footTextureMap, generator.modelCollector);

        // Create blockstate
        VariantsBlockStateSupplier blockstate = createBedBlockstate(block, headModelId, footModelId);
        generator.blockStateCollector.accept(blockstate);

        // Register item model
        TextureMap itemTextureMap = createBedItemTextureMap(textures);
        Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
        getBedItemModel(tinted).upload(itemModelId, itemTextureMap, generator.modelCollector);
    }


    public static void registerCustomBedBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        String bedType = definition.hasBedType() ? definition.getBedType() : "normal";

        if (definition.getTextures() == null || definition.getTextures().size() != 6) {
            WesterosBlocks.LOGGER.warn("Bed block '{}' requires exactly 6 textures, got {}",
                    definition.getBlockName(), definition.getTextures() != null ? definition.getTextures().size() : 0);
            return;
        }

        String[] textures = definition.getTextures().toArray(new String[0]);
        registerBedBlock(generator, block, tinted, bedType, textures);
    }
}
