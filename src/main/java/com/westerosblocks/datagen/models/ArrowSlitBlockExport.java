package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class ArrowSlitBlockExport extends CuboidBlockExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public ArrowSlitBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
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
            // Generate models for unstacked state
            ModBlockStateRecord unstackedElement = def.getStackElementByIndex(0);
            for (int setIdx = 0; setIdx < unstackedElement.getRandomTextureSetCount(); setIdx++) {
                generateArrowSlitModel(generator, unstackedElement, setIdx, "unstacked");
            }

            // Generate models for stacked state
            ModBlockStateRecord stackedElement = def.getStackElementByIndex(1);
            for (int setIdx = 0; setIdx < stackedElement.getRandomTextureSetCount(); setIdx++) {
                generateArrowSlitModel(generator, stackedElement, setIdx, "stacked");
            }
        }

        // Add variants for all combinations of facing and stacked states
        addVariants(blockStateBuilder, def.getStackElementByIndex(0), "unstacked", false, variants);
        addVariants(blockStateBuilder, def.getStackElementByIndex(1), "stacked", true, variants);

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateArrowSlitModel(BlockStateModelGenerator generator, ModBlockStateRecord element, int setIdx, String modelType) {
        ModBlock.RandomTextureSet set = element.getRandomTextureSet(setIdx);

        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(set.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(set.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(4)));

        Model arrowSlitModel = ModModels.ARROW_SLIT();
        arrowSlitModel.upload(getModelId(modelType, setIdx, element.isCustomModel()), textureMap, generator.modelCollector);
    }

    private void addVariants(BlockStateBuilder builder, ModBlockStateRecord element, String modelType, boolean stacked, 
                           Map<String, List<BlockStateVariant>> variants) {
        String[] directions = {"north", "east", "south", "west"};
        int[] rotations = {270, 0, 90, 180};

        for (int i = 0; i < directions.length; i++) {
            for (int setIdx = 0; setIdx < element.getRandomTextureSetCount(); setIdx++) {
                ModBlock.RandomTextureSet set = element.getRandomTextureSet(setIdx);
                Identifier modelId = getModelId(modelType, setIdx, element.isCustomModel());
                int rotation = rotations[i];
                BlockStateVariant variant = VariantBuilder.createWithRotation(modelId, set, rotation);

                builder.addVariant(String.format("facing=%s,stacked=%s", directions[i], stacked), variant, null, variants);
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
        Identifier itemModelId = WesterosBlocks.id("item/" + blockDefinition.getBlockName());

        ModBlockStateRecord unstackedElement = blockDefinition.getStackElementByIndex(0);
        ModBlock.RandomTextureSet textureSet = unstackedElement.getRandomTextureSet(0);

        TextureMap itemTextureMap = new TextureMap()
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(textureSet.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(textureSet.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(textureSet.getTextureByIndex(4)));

        Model itemModel = ModModels.ARROW_SLIT_ITEM();
        itemModel.upload(itemModelId, itemTextureMap, itemModelGenerator.writer);
    }
} 