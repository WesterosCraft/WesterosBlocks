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

public class LogBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public LogBlockExport(BlockStateModelGenerator generator, Block currentBlock, ModBlock customBlockDef) {
        super(generator, currentBlock, customBlockDef);
        this.generator = generator;
        this.block = currentBlock;
        this.def = customBlockDef;
    }

    private static final String[] states = {"axis=x", "axis=y", "axis=z"};
    private static final int[] xrot = {90, 0, 90};
    private static final int[] yrot = {90, 0, 0};
    private static final String[] models = {"x", "y", "z"};

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int i = 0; i < states.length; i++) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                Identifier symId = WesterosBlocks.id(GENERATED_PATH + getModelName(models[i], setIdx));
                BlockStateVariant variant = VariantBuilder.create(symId, set, yrot[i], xrot[i], null);
                blockStateBuilder.addVariant(states[i], variant, null, variants);
            }
        }

        generateBlockStateFiles(generator, block, variants);

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);
            // make models
            String[] types = {"cube_log_horizontal", "cube_log", "cube_log_horizontal"};
            for (int i = 0; i < models.length; i++) {
                generateLogModel(generator, types[i], set, def.isTinted(), setIdx, models[i]);
            }
        }
    }

    String getTextureWithFallback(ModBlock.RandomTextureSet set, int index) {
        int textureCount = set.getTextureCount();
        if (index < textureCount) {
            return set.getTextureByIndex(index);
        }
        // If requested index is beyond available textures, return last texture
        return set.getTextureByIndex(textureCount - 1);
    }

    public void generateLogModel(BlockStateModelGenerator generator, String type, ModBlock.RandomTextureSet set, boolean isTinted, int setIdx, String modelSuffix) {


        TextureMap textureMap = new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(getTextureWithFallback(set,0)))
                .put(TextureKey.UP, createBlockIdentifier(getTextureWithFallback(set,1)))
                .put(TextureKey.NORTH, createBlockIdentifier(getTextureWithFallback(set,2)))
                .put(TextureKey.SOUTH, createBlockIdentifier(getTextureWithFallback(set,2)))
                .put(TextureKey.WEST, createBlockIdentifier(getTextureWithFallback(set,2)))
                .put(TextureKey.EAST, createBlockIdentifier(getTextureWithFallback(set,2)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(getTextureWithFallback(set,2)));

        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%s", GENERATED_PATH, def.blockName, modelSuffix, setIdx + 1));

        String parentPath = getParentPath(isTinted, type);
        Model model = ModModels.ALL_SIDES(parentPath, null);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private static String getParentPath(boolean isTinted, String type) {
        String basePath;
        if (isTinted) {
            basePath = "tinted/";

        } else {
            basePath = "untinted/";
        }
        return basePath + type;
    }


    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, ModBlock blockDefinition) {
        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(WesterosBlocks.id(GENERATED_PATH + blockDefinition.blockName + "/" + models[1] + "_v1")), Optional.empty())
        );
    }
}
