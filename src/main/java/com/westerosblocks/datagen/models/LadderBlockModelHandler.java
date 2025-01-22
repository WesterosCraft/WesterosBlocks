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

import static com.westerosblocks.datagen.models.ModModels.WC_LADDER;

public class LadderBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    static final String[] FACES = {"north", "south", "east", "west"};
    static final int[] Y_ROTATIONS = {0, 180, 90, 270};

    public LadderBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public static Identifier modelFileName(int setidx, boolean isCustomModel) {
        return Identifier.of(WesterosBlocks.MOD_ID, getModelName("base", setidx)).withPrefixedPath(isCustomModel ? CUSTOM_PATH : GENERATED_PATH);
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                Identifier modelId =  modelFileName(setIdx, false);
                generateLadderModel(generator, set, modelId);
            }
        }

        for (int faceIdx = 0; faceIdx < 4; faceIdx++) {

            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                BlockStateVariant variant = BlockStateVariant.create();
                Identifier modId = modelFileName(setIdx, def.isCustomModel());
                variant.put(VariantSettings.MODEL, modId);
                if (set.weight != null) {
                    variant.put(VariantSettings.WEIGHT, set.weight);
                }
                if (Y_ROTATIONS[faceIdx] > 0) {
                    variant.put(VariantSettings.Y, getRotation(Y_ROTATIONS[faceIdx]));
                }
                blockStateBuilder.addVariant("facing=" + FACES[faceIdx], variant, null, variants);
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    protected static void generateLadderModel(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, Identifier modelId) {
        TextureMap textureMap = new TextureMap().put(ModTextureKey.LADDER, createBlockIdentifier(set.getTextureByIndex(0)));
        Identifier parentId = Identifier.of(WesterosBlocks.MOD_ID, "untinted/ladder");
        Model model = WC_LADDER(parentId.getPath());
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        String path = String.format("%s%s/%s", blockDefinition.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH, blockDefinition.getBlockName(), "base_v1");

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}
