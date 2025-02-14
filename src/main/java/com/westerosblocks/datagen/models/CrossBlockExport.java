package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class CrossBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;
    protected boolean layerSensitive = false;

    private static final String[] LAYER_CONDITIONS = {
            "layers=8", "layers=1", "layers=2", "layers=3",
            "layers=4", "layers=5", "layers=6", "layers=7"
    };

    public CrossBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        String t = def.getType();
        if ((t != null) && (t.contains(ModBlock.LAYER_SENSITIVE))) {
            layerSensitive = true;
        }
    }

    public void generateBlockStateModels() {
        String[] conditions = layerSensitive ? LAYER_CONDITIONS : new String[]{""};
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();
        int rotationCount = def.rotateRandom ? 4 : 1;

        for (int layer = 0; layer < conditions.length; layer++) {
            String layerCondition = conditions[layer];

            for (int idx = 0; idx < def.states.size(); idx++) {
                ModBlockStateRecord currentRec = def.states.get(idx);
                String id = currentRec.stateID == null ? "base" : currentRec.stateID;
                if (layer > 0) {
                    id = id + "_layer" + layer;
                }

                for (int setIdx = 0; setIdx < currentRec.getRandomTextureSetCount(); setIdx++) {
                    ModBlock.RandomTextureSet set = currentRec.getRandomTextureSet(setIdx);

                    for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
                        BlockStateVariant variant = BlockStateVariant.create();
                        Identifier modelId = getModelId(id, setIdx, currentRec.isCustomModel());
                        variant.put(VariantSettings.MODEL, modelId);
                        if (set.weight != null) {
                            variant.put(VariantSettings.WEIGHT, set.weight);
                        }
                        if (rotIdx > 0) {
                            variant.put(VariantSettings.Y, getRotation(90 * rotIdx));
                        }
                        Set<String> stateIDs = currentRec.stateID == null ? null : Collections.singleton(currentRec.stateID);
                        blockStateBuilder.addVariant(layerCondition, variant, stateIDs, variants);
                    }
                }

                if (!currentRec.isCustomModel()) {
                    generateCrossModel(generator, currentRec, id, layer);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateCrossModel(BlockStateModelGenerator generator, ModBlockStateRecord rec, String id, int layer) {
        boolean isTinted = rec.isTinted();

        for (int setIdx = 0; setIdx < rec.getRandomTextureSetCount(); setIdx++) {
            ModBlock.RandomTextureSet set = rec.getRandomTextureSet(setIdx);
            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.CROSS, createBlockIdentifier(set.getTextureByIndex(0)));
            Identifier modelId = getModelId(id, setIdx, false);
            String parentPath = isTinted ? "tinted/cross" : "untinted/cross";
            if (layer > 0) {
                parentPath += "_layer" + layer;
            }

            Model model = new Model(
                    Optional.of(WesterosBlocks.id("block/" + parentPath)),
                    Optional.empty(),
                    TextureKey.CROSS
            );
            model.upload(modelId, textureMap, generator.modelCollector);
        }
    }

    private Identifier getModelId(String id, int setIdx, boolean isCustom) {
        return getBaseModelId(id, setIdx, isCustom);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, ModBlock blockDefinition) {
        generateBasicItemModel(itemModelGenerator, currentBlock, blockDefinition);
    }
}