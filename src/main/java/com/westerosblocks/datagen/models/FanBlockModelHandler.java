package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FanBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public FanBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        // Add single floor variant
        BlockStateVariant floorVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, getModelId(def.blockName, "base", 1));
        blockStateBuilder.addVariant("", floorVariant, null, variants);

        // Add single wall variant for each direction
        Map<String, Integer> directionRotations = Map.of(
                "east", 90,
                "south", 180,
                "west", 270,
                "north", 0
        );

        directionRotations.forEach((direction, rotation) -> {
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId(def.blockName, "wall", 0))
                    .put(VariantSettings.Y, getRotation(rotation));
            blockStateBuilder.addVariant("facing=" + direction, variant, null, variants);
        });

        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                generateFanModels(generator, def.getRandomTextureSet(setIdx), setIdx);
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateFanModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, int setIdx) {
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
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                GENERATED_PATH,
                blockName,
                variant,
                setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
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

    private static void generateSingleItemModel(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef.RandomTextureSet set, String basePath) {
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
