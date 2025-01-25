package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlowerPotExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public FlowerPotExport(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
            int rotationCount = def.rotateRandom ? 4 : 1;

            for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
                BlockStateVariant variant = BlockStateVariant.create();
                Identifier modelId = getModelId("base", setIdx);
                variant.put(VariantSettings.MODEL, modelId);
                if (set.weight != null) {
                    variant.put(VariantSettings.WEIGHT, set.weight);
                }
                if (rotIdx > 0) {
                    variant.put(VariantSettings.Y, getRotation(90 * rotIdx));
                }
                blockStateBuilder.addVariant("", variant, null, variants);
            }
        }

        generateBlockStateFiles(generator, block, variants);

        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                generateFlowerPotModel(generator, set, setIdx);
            }
        }
    }

    private void generateFlowerPotModel(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.DIRT, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(ModTextureKey.FLOWER_POT, createBlockIdentifier(set.getTextureByIndex(1)))
                .put(TextureKey.PLANT, createBlockIdentifier(set.getTextureByIndex(2)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(1)));

        String parentPath = def.isTinted() ? "tinted/flower_pot_cross" : "untinted/flower_pot_cross";

        Identifier modelId = getModelId("base", setIdx);
        Model model = new Model(Optional.of(WesterosBlocks.id("block/" + parentPath)),
                Optional.empty(),
                TextureKey.DIRT,
                ModTextureKey.FLOWER_POT,
                TextureKey.PLANT,
                TextureKey.PARTICLE);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String type, int setIdx) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d", GENERATED_PATH, def.getBlockName(), type, setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        String path = String.format("%s%s/base_v1", GENERATED_PATH, blockDefinition.blockName);
        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty())
        );
    }
}