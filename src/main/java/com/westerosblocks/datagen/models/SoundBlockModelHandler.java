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

public class SoundBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public SoundBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        // If not custom model, generate model first
        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                generateSoundBlockModel(generator, set, setIdx);
            }
        }

        // Generate single variant with empty condition
        BlockStateVariant variant = BlockStateVariant.create();
        Identifier modelId = getModelId("base", 0, def.isCustomModel());
        variant.put(VariantSettings.MODEL, modelId);

        blockStateBuilder.addVariant("", variant, null, variants);

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateSoundBlockModel(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(set.getTextureByIndex(0)));

        Identifier modelId = getModelId("base", setIdx, false);
        Model model = new Model(
                Optional.of(Identifier.ofVanilla("block/cube_all")), // Use vanilla cube_all parent
                Optional.empty(),
                TextureKey.ALL
        );
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String variant, int setIdx, boolean isCustom) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d",
                        isCustom ? CUSTOM_PATH : GENERATED_PATH,
                        def.getBlockName(),
                        variant,
                        setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
                                          Block block,
                                          WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}