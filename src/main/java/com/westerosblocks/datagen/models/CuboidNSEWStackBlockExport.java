package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CuboidNSEWStackBlockExport extends CuboidBlockExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public CuboidNSEWStackBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    @Override
    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        if (!def.isCustomModel()) {
            // Bottom element models
            ModBlockStateRecord bottomElement = def.getStackElementByIndex(0);
            for (int setIdx = 0; setIdx < bottomElement.getRandomTextureSetCount(); setIdx++) {
                generateStackElementModel(generator, bottomElement, setIdx, "base");
            }

            // Top element models
            ModBlockStateRecord topElement = def.getStackElementByIndex(1);
            for (int setIdx = 0; setIdx < topElement.getRandomTextureSetCount(); setIdx++) {
                generateStackElementModel(generator, topElement, setIdx, "top");
            }
        }

        addHalfVariants(blockStateBuilder, def.getStackElementByIndex(0), "base", "lower", variants);
        addHalfVariants(blockStateBuilder, def.getStackElementByIndex(1), "top", "upper", variants);

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateStackElementModel(BlockStateModelGenerator generator, ModBlockStateRecord element, int setIdx, String modelType) {
        ModBlock.RandomTextureSet set = element.getRandomTextureSet(setIdx);

        // TODO i think this needs to do all textures but filter out transparent ones?
        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(set.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(set.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(4)));

        Model cuboidNSEWStackModel = ModModels.CUBOID_NSEW_STACK();
        cuboidNSEWStackModel.upload(getModelId(modelType, setIdx, element.isCustomModel()), textureMap, generator.modelCollector);
    }

    private void addHalfVariants(BlockStateBuilder builder, ModBlockStateRecord element, String modelType, String half, Map<String, List<BlockStateVariant>> variants) {
        String[] directions = {"north", "east", "south", "west"};
        int[] rotations = {270, 0, 90, 180};

        for (int i = 0; i < directions.length; i++) {
            for (int setIdx = 0; setIdx < element.getRandomTextureSetCount(); setIdx++) {
                BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, getModelId(modelType, setIdx, element.isCustomModel()));
                int rotation = half.equals("upper") ? (rotations[i] + element.rotYOffset) % 360 : rotations[i];
                if (rotation != 0) {
                    variant.put(VariantSettings.Y, getRotation(rotation));
                }
                if (element.getRandomTextureSet(setIdx).weight != null) {
                    variant.put(VariantSettings.WEIGHT, element.getRandomTextureSet(setIdx).weight);
                }
                builder.addVariant(String.format("facing=%s,half=%s", directions[i], half), variant, null, variants);
            }
        }
    }

    @Override
    public Identifier getModelId(String variant, int setIdx, boolean isCustom) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                        isCustom ? CUSTOM_PATH : GENERATED_PATH,
                        def.getBlockName(),
                        variant,
                        setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        generateBlockBasedItemModel(itemModelGenerator, block, blockDefinition);
    }
}