package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class VinesBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public VinesBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

            if (!def.isCustomModel()) {
                generateVineModels(generator, set, setIdx);
            }

            int rotationCount = def.rotateRandom ? 4 : 1;
            for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
                // SOUTH
                When.PropertyCondition southCondition = When.create().set(Properties.SOUTH, true);
                BlockStateVariant southVariant = createVariant(set, setIdx, 0, false, 0);
                stateSupplier.with(southCondition, southVariant);

                // WEST
                When.PropertyCondition westCondition = When.create().set(Properties.WEST, true);
                BlockStateVariant westVariant = createVariant(set, setIdx, 90, false, 0);
                stateSupplier.with(westCondition, westVariant);

                // NORTH
                When.PropertyCondition northCondition = When.create().set(Properties.NORTH, true);
                BlockStateVariant northVariant = createVariant(set, setIdx, 180, false, 0);
                stateSupplier.with(northCondition, northVariant);

                // EAST
                When.PropertyCondition eastCondition = When.create().set(Properties.EAST, true);
                BlockStateVariant eastVariant = createVariant(set, setIdx, 270, false, 0);
                stateSupplier.with(eastCondition, eastVariant);

                // UP
                When.PropertyCondition upCondition = When.create().set(Properties.UP, true);
                BlockStateVariant upVariant = createVariant(set, setIdx, 0, true, 0);
                stateSupplier.with(upCondition, upVariant);

                // DOWN
                When.PropertyCondition downCondition = When.create().set(Properties.DOWN, true);
                BlockStateVariant downVariant = createVariant(set, setIdx, 0, true, 180);
                stateSupplier.with(downCondition, downVariant);
            }
        }

        generator.blockStateCollector.accept(stateSupplier);
    }

    private BlockStateVariant createVariant(WesterosBlockDef.RandomTextureSet set, int setIdx, int yRotation, boolean isVertical, int xRotation) {
        BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, getModelId(isVertical ? "top" : "base", setIdx));

        if (set.weight != null) {
            variant.put(VariantSettings.WEIGHT, set.weight);
        }

        if (yRotation != 0) {
            variant.put(VariantSettings.Y, getRotation(yRotation));
        }

        if (xRotation != 0) {
            variant.put(VariantSettings.X, getRotation(xRotation));
        }

        return variant;
    }

    private void generateVineModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        // base vine model
        TextureMap baseTextureMap = new TextureMap().put(ModTextureKey.VINES, createBlockIdentifier(set.getTextureByIndex(0)));
        Identifier baseModelId = getModelId("base", setIdx);
        Model baseModel = new Model(
                Optional.of(WesterosBlocks.id("block/vines/vine_1")),
                Optional.empty(),
                ModTextureKey.VINES
        );
        baseModel.upload(baseModelId, baseTextureMap, generator.modelCollector);

        // top vine model
        TextureMap topTextureMap = new TextureMap().put(ModTextureKey.VINES, createBlockIdentifier(set.getTextureByIndex(1)));
        Identifier topModelId = getModelId("top", setIdx);
        Model topModel = new Model(
                Optional.of(WesterosBlocks.id("block/vines/vine_u")),
                Optional.empty(),
                ModTextureKey.VINES
        );
        topModel.upload(topModelId, topTextureMap, generator.modelCollector);
    }

    private Identifier getModelId(String type, int setIdx) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d", def.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH, def.getBlockName(), type, setIdx + 1)
        );
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.states.getFirst().getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), textureMap, itemModelGenerator.writer);
    }
}