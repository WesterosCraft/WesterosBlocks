package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.custom.WCLayerBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LayerBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private final WCLayerBlock layerBlock;

    public LayerBlockExport(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        this.layerBlock = (WCLayerBlock) block;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int layer = 0; layer < layerBlock.layerCount; layer++) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

                BlockStateVariant variant = BlockStateVariant.create();
                Identifier modelId = getModelId(layer + 1, setIdx);
                variant.put(VariantSettings.MODEL, modelId);
                if (set.weight != null) {
                    variant.put(VariantSettings.WEIGHT, set.weight);
                }
                blockStateBuilder.addVariant("layers=" + (layer + 1), variant, null, variants);

                if (!def.isCustomModel()) {
                    generateLayerModel(generator, layer + 1, set, setIdx);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateLayerModel(BlockStateModelGenerator generator, int layer, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        boolean isTinted = def.isTinted();
        float yMax = (16.0f / layerBlock.layerCount) * layer;  // Calculate height based on layer number
        Model layerModel = ModModels.WC_LAYER(yMax, isTinted);
        TextureMap textureMap = ModTextureMap.txtN(set);

        layerModel.upload(getModelId(layer, setIdx), textureMap, generator.modelCollector);
    }

    private Identifier getModelId(int layer, int setIdx) {
        return WesterosBlocks.id(
                String.format("%s%s/layer%d_v%d",
                GENERATED_PATH,
                def.getBlockName(),
                layer,
                setIdx + 1));
    }

    // TODO need to figure out a diff model for in-game GUI
    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        String path = String.format("%s%s/layer1_v1", GENERATED_PATH, blockDefinition.blockName);
        itemModelGenerator.register(currentBlock.asItem(), new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty()));

        if (blockDefinition.isTinted()) {
            String tintResource = blockDefinition.getBlockColorMapResource();
            if (tintResource != null) {
                // TODO: Handle tinting registration for items
            }
        }
    }
}
