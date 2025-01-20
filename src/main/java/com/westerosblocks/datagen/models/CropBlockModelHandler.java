package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class CropBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private final boolean layerSensitive;

    private static final String[] LAYER_CONDITIONS = {
            "layers=8", "layers=1", "layers=2", "layers=3",
            "layers=4", "layers=5", "layers=6", "layers=7"
    };

    public CropBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        this.layerSensitive = def.getType() != null &&
                def.getType().contains("layer_sensitive");
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();
        int rotationCount = def.rotateRandom ? 4 : 1;

        String[] conditions = layerSensitive ? LAYER_CONDITIONS : new String[]{""};

        // Generate variants for each layer condition
        for (int layer = 0; layer < conditions.length; layer++) {
            String layerCondition = conditions[layer];

            for (int stateIdx = 0; stateIdx < def.states.size(); stateIdx++) {
                WesterosBlockStateRecord rec = def.states.get(stateIdx);

                String id = rec.stateID == null ? "base" : rec.stateID;
                if (layer > 0) {
                    id = id + "_layer" + layer;
                }

                for (int setIdx = 0; setIdx < rec.getRandomTextureSetCount(); setIdx++) {
                    WesterosBlockDef.RandomTextureSet set = rec.getRandomTextureSet(setIdx);

                    for (int rot = 0; rot < rotationCount; rot++) {
                        BlockStateVariant variant = BlockStateVariant.create();
                        Identifier modelId = getModelId(id, setIdx, rec.isCustomModel());
                        variant.put(VariantSettings.MODEL, modelId);

                        if (set.weight != null) {
                            variant.put(VariantSettings.WEIGHT, set.weight);
                        }
                        if (rot > 0) {
                            variant.put(VariantSettings.Y, getRotation(90 * rot));
                        }

                        Set<String> stateIDs = rec.stateID == null ?
                                null : Collections.singleton(rec.stateID);
                        blockStateBuilder.addVariant(layerCondition, variant, stateIDs, variants);
                    }
                }

                if (!rec.isCustomModel()) {
                    generateCropModels(generator, rec, id, layer);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateCropModels(BlockStateModelGenerator generator, WesterosBlockStateRecord rec, String id, int layer) {
        boolean isTinted = rec.isTinted();

        for (int setIdx = 0; setIdx < rec.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = rec.getRandomTextureSet(setIdx);

            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.CROP, createBlockIdentifier(set.getTextureByIndex(0)));

            Identifier modelId = getModelId(id, setIdx, false);
            String parentPath = isTinted ? "tinted/crop" : "untinted/crop";
            if (layer > 0) {
                parentPath += "_layer" + layer;
            }

            Model model = new Model(
                    Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + parentPath)),
                    Optional.empty(),
                    TextureKey.CROP
            );
            model.upload(modelId, textureMap, generator.modelCollector);
        }
    }

    private Identifier getModelId(String id, int setIdx, boolean isCustom) {
        String path = String.format("%s%s/%s_v%d",
                isCustom ? CUSTOM_PATH : GENERATED_PATH,
                def.blockName,
                id,
                setIdx + 1
        );
        return Identifier.of(WesterosBlocks.MOD_ID, path);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.states.getFirst().getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(currentBlock.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}