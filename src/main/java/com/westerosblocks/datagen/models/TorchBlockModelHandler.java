package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TorchBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static final String[] FACING_DIRECTIONS = {
            "facing=east", "facing=south", "facing=west", "facing=north"
    };
    private static final int[] ROTATIONS = {0, 90, 180, 270};

    public TorchBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        // Only proceed with blockstate generation if this is not a wall torch block
        if (!def.blockName.startsWith("wall_")) {
            // Generate models for both standing and wall variants
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                if (!def.isCustomModel()) {
                    generateTorchModels(generator, set, setIdx);
                }
            }

            // Generate standing torch blockstate file
            final Map<String, List<BlockStateVariant>> variants = new HashMap<>();
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

                BlockStateVariant standingVariant = BlockStateVariant.create();
                Identifier standingModelId = getModelId("base", setIdx);
                standingVariant.put(VariantSettings.MODEL, standingModelId);
                if (set.weight != null) {
                    standingVariant.put(VariantSettings.WEIGHT, set.weight);
                }
                addVariant("", standingVariant, null, variants);
            }
            generateBlockStateFiles(generator, block, variants);

            // Generate wall torch blockstate file
            final Map<String, List<BlockStateVariant>> wallVariants = new HashMap<>();
            for (int i = 0; i < FACING_DIRECTIONS.length; i++) {
                for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                    WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = getModelId("wall", setIdx);
                    variant.put(VariantSettings.MODEL, modelId);
                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }
                    variant.put(VariantSettings.Y, getRotation(ROTATIONS[i]));
                    addVariant(FACING_DIRECTIONS[i], variant, null, wallVariants);
                }
            }

            // Write wall torch blockstate file
            String wallBlockName = "wall_" + def.blockName;
            WesterosBlockDef wallDef = new WesterosBlockDef();
            wallDef.blockName = wallBlockName;
            Block wallBlock = Block.getBlockFromItem(block.asItem());
            generateBlockStateFiles(generator, wallBlock, wallVariants);
        }
    }

    private void generateTorchModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        // Generate the torch model (base_v1.json)
        TextureMap torchTextureMap = new TextureMap()
                .put(TextureKey.TORCH, createBlockIdentifier(set.getTextureByIndex(0)));

        Identifier torchModelId = getModelId("base", setIdx);
        Model torchModel = new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/untinted/template_torch")),
                Optional.empty(),
                TextureKey.TORCH);
        torchModel.upload(torchModelId, torchTextureMap, generator.modelCollector);

        // Generate the wall torch model (wall_v1.json)
        Identifier wallModelId = getModelId("wall", setIdx);
        Model wallTorchModel = new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/untinted/template_torch_wall")),
                Optional.empty(),
                TextureKey.TORCH);
        wallTorchModel.upload(wallModelId, torchTextureMap, generator.modelCollector);
    }

    private Identifier getModelId(String type, int setIdx) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d", GENERATED_PATH, def.getBlockName(), type, setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        // Only generate item model for non-wall torch variants
        if (!blockDefinition.blockName.startsWith("wall_")) {
            WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
            String texturePath = String.format("block/%s", firstSet.getTextureByIndex(0));

            Models.GENERATED.upload(
                    ModelIds.getItemModelId(currentBlock.asItem()),
                    TextureMap.layer0(Identifier.of(WesterosBlocks.MOD_ID, texturePath)),
                    itemModelGenerator.writer
            );
        }
    }
}