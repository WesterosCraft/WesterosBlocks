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

    private Block getWallBlock() {
        String wallBlockName = def.blockName + "_wall";
        return ModBlocks.getCustomBlocksByName().get(wallBlockName);
    }

    public void generateBlockStateModels() {
        // Floor fan blockstates
        BlockStateBuilder floorBuilder = new BlockStateBuilder(block);
        Map<String, List<BlockStateVariant>> floorVariants = floorBuilder.getVariants();

        // Wall fan blockstates
        BlockStateBuilder wallBuilder = new BlockStateBuilder(getWallBlock());
        Map<String, List<BlockStateVariant>> wallVariants = wallBuilder.getVariants();

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

            // Floor variant
            BlockStateVariant floorVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId(def.blockName, "base", setIdx));
            if (set.weight != null) {
                floorVariant.put(VariantSettings.WEIGHT, set.weight);
            }
            floorBuilder.addVariant("", floorVariant, null, floorVariants);

            // Wall variants
            addWallVariants(wallBuilder, wallVariants, set, setIdx);

            if (!def.isCustomModel()) {
                generateFanModels(generator, set, setIdx);
            }
        }

        generateBlockStateFiles(generator, block, floorVariants);
        generateBlockStateFiles(generator, getWallBlock(), wallVariants);
    }

    private void addWallVariants(BlockStateBuilder builder, Map<String, List<BlockStateVariant>> variants,
                                 WesterosBlockDef.RandomTextureSet set, int setIdx) {
        Map<String, Integer> directionRotations = Map.of(
                "east", 90,
                "south", 180,
                "west", 270,
                "north", 0
        );

        directionRotations.forEach((direction, rotation) -> {
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId(def.blockName, "wall", setIdx))
                    .put(VariantSettings.Y, getRotation(rotation));
            if (set.weight != null) {
                variant.put(VariantSettings.WEIGHT, set.weight);
            }
            builder.addVariant("facing=" + direction, variant, null, variants);
        });
    }

    private void generateFanModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.FAN, createBlockIdentifier(set.getTextureByIndex(0)));
        String basePath = def.isTinted() ? "tinted" : "untinted";

        // Generate floor model
        Model floorModel = new Model(
                Optional.of(WesterosBlocks.id("block/" + basePath + "/fan")),
                Optional.empty(),
                TextureKey.FAN
        );
        floorModel.upload(getModelId(def.blockName, "base", setIdx), textureMap, generator.modelCollector);

        // Generate wall model
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

        // Generate model for main fan item
        generateSingleItemModel(itemModelGenerator, block, firstSet, basePath);

        // Generate model for wall fan item
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
