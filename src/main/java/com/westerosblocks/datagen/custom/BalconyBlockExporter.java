package com.westerosblocks.datagen.custom;

import com.westerosblocks.block.custom.WCBalconyBlock;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class BalconyBlockExporter extends BaseBlockExporter {

    private static Model getBalconySideModel(boolean tinted) {
        return tinted ? ModModels.BALCONY_SIDE_TINTED : ModModels.BALCONY_SIDE_UNTINTED;
    }

    private static Model getBalconySideWallModel(boolean tinted) {
        return tinted ? ModModels.BALCONY_SIDE_WALL_TINTED : ModModels.BALCONY_SIDE_WALL_UNTINTED;
    }

    private static MultipartBlockStateSupplier createBalconyVariants(Block block,
            List<Identifier> sideModelIds, List<Identifier> wallModelIds, List<Integer> weights) {
        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

        for (int i = 0; i < sideModelIds.size(); i++) {
            // Normal variants (wall=false)
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.NORTH, false);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.EAST, false);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.SOUTH, false);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.WEST, false);

            // Wall variants (wall=true)
            addSideVariant(supplier, wallModelIds.get(i), weights.get(i), Direction.NORTH, true);
            addSideVariant(supplier, wallModelIds.get(i), weights.get(i), Direction.EAST, true);
            addSideVariant(supplier, wallModelIds.get(i), weights.get(i), Direction.SOUTH, true);
            addSideVariant(supplier, wallModelIds.get(i), weights.get(i), Direction.WEST, true);
        }

        return supplier;
    }

    private static void addSideVariant(MultipartBlockStateSupplier supplier, Identifier sideModelId,
                                       int weight, Direction direction, boolean isWall) {
        BlockStateVariant sideVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, sideModelId)
                .put(VariantSettings.UVLOCK, true);

        int yRotation = getRotationForDirection(direction);
        if (yRotation != 0) {
            sideVariant = sideVariant.put(VariantSettings.Y, toYRotation(yRotation));
        }

        if (weight > 1) {
            sideVariant = sideVariant.put(VariantSettings.WEIGHT, weight);
        }

        When.PropertyCondition condition = When.create();
        switch (direction) {
            case NORTH -> condition.set(Properties.NORTH, true);
            case EAST -> condition.set(Properties.EAST, true);
            case SOUTH -> condition.set(Properties.SOUTH, true);
            case WEST -> condition.set(Properties.WEST, true);
        }
        condition.set(WCBalconyBlock.WALL, isWall);

        supplier.with(condition, sideVariant);
    }

    public static void registerBalconyBlock(BlockStateModelGenerator generator, Block block, boolean tinted, String[] textures) {
        String[] expandedTextures = fillTextureArray(textures, 2);
        TextureMap textureMap = ModTextureMap.balconyTextures(expandedTextures[0], expandedTextures[1]);

        Identifier sideModelId = getBalconySideModel(tinted)
                .upload(createNestedModelId(block, "side"), textureMap, generator.modelCollector);

        Identifier wallModelId = getBalconySideWallModel(tinted)
                .upload(createNestedModelId(block, "side_wall"), textureMap, generator.modelCollector);

        MultipartBlockStateSupplier blockstate = createBalconyVariants(block,
                List.of(sideModelId), List.of(wallModelId), List.of(1));
        generator.blockStateCollector.accept(blockstate);

        registerParentedItemModel(generator, block, sideModelId);
    }

    public static void registerBalconyBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, boolean tinted,
                                                              List<BlockDefinition.TextureVariantSet> textureSets) {
        List<Identifier> sideModelIds = new ArrayList<>();
        List<Identifier> wallModelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 0; i < textureSets.size(); i++) {
            BlockDefinition.TextureVariantSet set = textureSets.get(i);
            String[] expandedTextures = fillTextureArray(set.getTexturesAsArray(), 2);
            TextureMap textureMap = ModTextureMap.balconyTextures(expandedTextures[0], expandedTextures[1]);

            Identifier sideModelId = getBalconySideModel(tinted)
                    .upload(createNestedModelId(block, "side_v" + (i + 1)), textureMap, generator.modelCollector);
            Identifier wallModelId = getBalconySideWallModel(tinted)
                    .upload(createNestedModelId(block, "side_wall_v" + (i + 1)), textureMap, generator.modelCollector);

            sideModelIds.add(sideModelId);
            wallModelIds.add(wallModelId);
            weights.add(set.weight);
        }

        MultipartBlockStateSupplier blockstate = createBalconyVariants(block, sideModelIds, wallModelIds, weights);
        generator.blockStateCollector.accept(blockstate);

        registerParentedItemModel(generator, block, sideModelIds.get(0));
    }

    public static void registerCustomBalconyBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        List<String> textureList = definition.getTextures();

        if (definition.hasRandomTextures()) {
            registerBalconyBlockWithRandomTextures(generator, block, tinted, extractTextureVariantSets(definition));
        } else if (textureList != null && !textureList.isEmpty()) {
            String[] textures = textureList.toArray(new String[0]);
            registerBalconyBlock(generator, block, tinted, textures);
        } else {
            registerBalconyBlock(generator, block, tinted, new String[]{"missingno"});
        }
    }
}
