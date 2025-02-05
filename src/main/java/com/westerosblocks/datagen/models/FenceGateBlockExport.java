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

public class FenceGateBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    private static final String[] FACINGS = {"east", "north", "south", "west"};
    private static final int[] FACING_ROTATIONS = {270, 180, 0, 90};

    public FenceGateBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int f = 0; f < FACINGS.length; f++) {
            generateVariantsForState(blockStateBuilder, variants, "facing=" + FACINGS[f] + ",in_wall=false,open=false", "gate", FACING_ROTATIONS[f]);
            generateVariantsForState(blockStateBuilder, variants, "facing=" + FACINGS[f] + ",in_wall=false,open=true", "gate_open", FACING_ROTATIONS[f]);
            generateVariantsForState(blockStateBuilder, variants, "facing=" + FACINGS[f] + ",in_wall=true,open=false", "gate_wall", FACING_ROTATIONS[f]);
            generateVariantsForState(blockStateBuilder, variants, "facing=" + FACINGS[f] + ",in_wall=true,open=true", "gate_wall_open", FACING_ROTATIONS[f]);
        }

        generateBlockStateFiles(generator, block, variants);

        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                generateFenceGateModels(generator, set, setIdx);
            }
        }
    }

    private void generateVariantsForState(BlockStateBuilder blockStateBuilder, Map<String, List<BlockStateVariant>> variants, String condition, String modelType, int yRotation) {
        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);

            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = getModelId(modelType, setIdx);
            variant.put(VariantSettings.MODEL, modelId);

            if (set.weight != null) {
                variant.put(VariantSettings.WEIGHT, set.weight);
            }

            if (yRotation != 0) {
                variant.put(VariantSettings.Y, getRotation(yRotation));
            }

            variant.put(VariantSettings.UVLOCK, true);

            blockStateBuilder.addVariant(condition, variant, null, variants);
        }
    }

    private void generateFenceGateModels(BlockStateModelGenerator generator, ModBlock.RandomTextureSet set, int setIdx) {
        boolean isTinted = def.isTinted();
        String basePath = isTinted ? "tinted" : "untinted";

        // Generate models for different states
        String[][] modelTypes = {
                {"gate", "template_fence_gate"},
                {"gate_open", "template_fence_gate_open"},
                {"gate_wall", "template_fence_gate_wall"},
                {"gate_wall_open", "template_fence_gate_wall_open"}
        };

        for (String[] type : modelTypes) {
            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.TEXTURE, createBlockIdentifier(set.getTextureByIndex(0)));

            Identifier modelId = getModelId(type[0], setIdx);
            String parentPath = basePath + "/" + type[1];

            Model model = new Model(
                    Optional.of(WesterosBlocks.id("block/" + parentPath)),
                    Optional.empty(),
                    TextureKey.TEXTURE
            );
            model.upload(modelId, textureMap, generator.modelCollector);
        }
    }

    private Identifier getModelId(String type, int setIdx) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                        GENERATED_PATH,
                        def.getBlockName(),
                        type,
                        setIdx + 1
                ));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, ModBlock blockDefinition) {
        ModBlock.RandomTextureSet firstSet = blockDefinition.states.getFirst().getRandomTextureSet(0);
        boolean isTinted = blockDefinition.isTinted();
        String basePath = isTinted ? "tinted" : "untinted";

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TEXTURE, createBlockIdentifier(firstSet.getTextureByIndex(0)));

        String parentPath = String.format("block/%s/template_fence_gate", basePath);

        Identifier modelId = ModelIds.getItemModelId(currentBlock.asItem());
        Identifier parentId = WesterosBlocks.id(parentPath);

        Model model = new Model(Optional.of(parentId), Optional.empty(), TextureKey.TEXTURE);
        model.upload(modelId, textureMap, itemModelGenerator.writer);
    }

}