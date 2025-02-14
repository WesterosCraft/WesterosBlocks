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

public class FurnaceBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    private static class ModelRec {
        String cond;
        String ext;
        int y;
        ModelRec(String c, String e, int y) {
            cond = c; ext = e; this.y = y;
        }
    }

    private static final ModelRec[] MODELS = {
            new ModelRec("facing=north,lit=true", "lit", 0),
            new ModelRec("facing=south,lit=true", "lit", 180),
            new ModelRec("facing=west,lit=true", "lit", 270),
            new ModelRec("facing=east,lit=true", "lit", 90),
            new ModelRec("facing=north,lit=false", "base", 0),
            new ModelRec("facing=south,lit=false", "base", 180),
            new ModelRec("facing=west,lit=false", "base", 270),
            new ModelRec("facing=east,lit=false", "base", 90)
    };

    public FurnaceBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModelRec rec : MODELS) {
            Identifier modelId = getModelId(rec.ext, def.isCustomModel());
            BlockStateVariant variant = VariantBuilder.createWithRotation(modelId, null, rec.y);
            blockStateBuilder.addVariant(rec.cond, variant, null, variants);
        }

        if (!def.isCustomModel()) {
            generateFurnaceModels(generator);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateFurnaceModels(BlockStateModelGenerator generator) {
        ModBlock.RandomTextureSet set = def.getRandomTextureSet(0);
        boolean isTinted = def.isTinted();
        String parentPath = "block/" + (isTinted ? "tinted/orientable" : "orientable");

        TextureMap litTextureMap = ModTextureMap.directional(set);
        TextureMap baseTextureMap = ModTextureMap.directional(set);

        Model litModel = new Model(
                Optional.of(Identifier.of(isTinted ? WesterosBlocks.MOD_ID : "minecraft", parentPath)),
                Optional.empty(),
                TextureKey.TOP, TextureKey.SIDE, TextureKey.FRONT
        );
        litModel.upload(getModelId("lit", false), litTextureMap, generator.modelCollector);

        Model baseModel = new Model(
                Optional.of(Identifier.of(isTinted ? WesterosBlocks.MOD_ID : "minecraft", parentPath)),
                Optional.empty(),
                TextureKey.TOP, TextureKey.SIDE, TextureKey.FRONT
        );
        baseModel.upload(getModelId("base", false), baseTextureMap, generator.modelCollector);
    }

    private Identifier getModelId(String variant, boolean isCustom) {
        return getBaseModelId(variant, 0, isCustom);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        String path = String.format("%s%s/base_v1", blockDefinition.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH, blockDefinition.getBlockName());

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)),
                        Optional.empty())
        );
    }
}
