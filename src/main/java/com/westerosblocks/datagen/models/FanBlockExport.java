package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FanBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public FanBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        // Add floor variants with waterlogged states but no facing
        for (boolean waterlogged : new boolean[]{true, false}) {
            BlockStateVariant floorVariant = VariantBuilder.create(getModelId(def.blockName, "base", 1), null);
            blockStateBuilder.addVariant("waterlogged=" + waterlogged, floorVariant, null, variants);
        }

        // For wall variants, handle both facing and waterlogged states
        Map<String, Integer> directionRotations = Map.of(
                "east", 90,
                "south", 180,
                "west", 270,
                "north", 0
        );

        // Generate model variants if not using custom models
        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                generateFanModels(generator, def.getRandomTextureSet(setIdx), setIdx);
            }
        }

        // For wall variants, we need a different block instance
        String wallBlockName = def.blockName + "_wall";
        Block wallBlock = ModBlocks.getCustomBlocksByName().get(wallBlockName);
        if (wallBlock != null) {
            BlockStateBuilder wallStateBuilder = new BlockStateBuilder(wallBlock);
            final Map<String, List<BlockStateVariant>> wallVariants = wallStateBuilder.getVariants();

            directionRotations.forEach((direction, rotation) -> {
                // Generate variants for both waterlogged states
                for (boolean waterlogged : new boolean[]{true, false}) {
                    BlockStateVariant variant = VariantBuilder.createWithRotation(getModelId(def.blockName, "wall", 0), null, rotation);
                    String condition = String.format("facing=%s,waterlogged=%s", direction, waterlogged);
                    wallStateBuilder.addVariant(condition, variant, null, wallVariants);
                }
            });

            generateBlockStateFiles(generator, wallBlock, wallVariants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateFanModels(BlockStateModelGenerator generator, ModBlock.RandomTextureSet set, int setIdx) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.FAN, createBlockIdentifier(set.getTextureByIndex(0)));
        String basePath = def.isTinted() ? "tinted" : "untinted";

        // floor model
        Model floorModel = new Model(
                Optional.of(WesterosBlocks.id("block/" + basePath + "/fan")),
                Optional.empty(),
                TextureKey.FAN
        );
        floorModel.upload(getModelId(def.blockName, "base", setIdx), textureMap, generator.modelCollector);

        // wall model
        Model wallModel = new Model(
                Optional.of(WesterosBlocks.id("block/" + basePath + "/wall_fan")),
                Optional.empty(),
                TextureKey.FAN
        );
        wallModel.upload(getModelId(def.blockName, "wall", setIdx), textureMap, generator.modelCollector);
    }


    private Identifier getModelId(String blockName, String variant, int setIdx) {
        return getBaseModelId(variant, setIdx, def.isCustomModel());
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        ModBlock.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        String basePath = blockDefinition.isTinted() ? "tinted" : "untinted";

        // main fan item
        generateSingleItemModel(itemModelGenerator, block, firstSet, basePath);

        // wall fan item
        String wallBlockName = blockDefinition.blockName + "_wall";
        Block wallBlock = ModBlocks.getCustomBlocksByName().get(wallBlockName);
        if (wallBlock != null) {
            generateSingleItemModel(itemModelGenerator, wallBlock, firstSet, basePath);
        }
    }

    private static void generateSingleItemModel(ItemModelGenerator itemModelGenerator, Block block, ModBlock.RandomTextureSet set, String basePath) {
        Model itemModel = new Model(
                Optional.of(WesterosBlocks.id("block/" + basePath + "/fan")),
                Optional.empty(),
                TextureKey.FAN
        );

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.FAN, createBlockIdentifier(set.getTextureByIndex(0)));

        itemModel.upload(ModelIds.getItemModelId(block.asItem()), textureMap, itemModelGenerator.writer);
    }
}
