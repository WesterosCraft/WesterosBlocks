package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CakeBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public CakeBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        // Generate models for all 7 bite states (0-6)
        if (!def.isCustomModel()) {
            generateCakeModels(generator);
        }

        // Generate variants for each bite state
        for (int bites = 0; bites < 7; bites++) {
            BlockStateVariant variant = BlockStateVariant.create();
            String modelName = bites == 0 ? "uneaten" : "slice" + bites;
            Identifier modelId = getModelId(modelName, 0, def.isCustomModel());
            variant.put(VariantSettings.MODEL, modelId);

            blockStateBuilder.addVariant("bites=" + bites, variant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateCakeModels(BlockStateModelGenerator generator) {
        TextureMap baseTextureMap = ModTextureMap.cake(def);

        // Generate models for each bite state
        for (int bites = 0; bites < 7; bites++) {
            String modelName = bites == 0 ? "uneaten" : "slice" + bites;
            Identifier modelId = getModelId(modelName, 0, false);
            Model cakeModel = ModModels.CAKE(bites);
            cakeModel.upload(modelId, baseTextureMap, generator.modelCollector);
        }
    }

    private Identifier getModelId(String variant, int setIdx, boolean isCustom) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d", isCustom ? CUSTOM_PATH : GENERATED_PATH, def.getBlockName(), variant, setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        String path = String.format("%s%s/uneaten_v1", GENERATED_PATH, blockDefinition.getBlockName());

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty())
        );
    }
}
