package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class RailBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    private static final String[] SHAPES = {
            "shape=north_south", "shape=east_west",
            "shape=ascending_east", "shape=ascending_west",
            "shape=ascending_north", "shape=ascending_south",
            "shape=south_east", "shape=south_west",
            "shape=north_west", "shape=north_east"
    };

    private static final String[] MODEL_TYPES = {
            "flat", "flat",
            "raised_ne", "raised_sw",
            "raised_ne", "raised_sw",
            "curved", "curved",
            "curved", "curved"
    };

    private static final int[] ROTATIONS = {
            0, 90,
            90, 90,
            0, 0,
            0, 90,
            180, 270
    };

    public RailBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int i = 0; i < SHAPES.length; i++) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);

                BlockStateVariant variant = BlockStateVariant.create();
                Identifier modelId = getModelId(MODEL_TYPES[i], setIdx);
                variant.put(VariantSettings.MODEL, modelId);

                if (set.weight != null) {
                    variant.put(VariantSettings.WEIGHT, set.weight);
                }

                if (ROTATIONS[i] != 0) {
                    variant.put(VariantSettings.Y, getRotation(ROTATIONS[i]));
                }

                blockStateBuilder.addVariant(SHAPES[i], variant, null, variants);
            }
        }

        generateBlockStateFiles(generator, block, variants);

        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);

                Set<String> generatedModels = new HashSet<>();

                for (String modelType : MODEL_TYPES) {
                    if (generatedModels.add(modelType)) { // Only generate if we haven't seen this type before
                        generateRailModel(generator, modelType, set, setIdx);
                    }
                }
            }
        }
    }

    private void generateRailModel(BlockStateModelGenerator generator, String type, ModBlock.RandomTextureSet set, int setIdx) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.RAIL, createBlockIdentifier(type.equals("curved") ? set.getTextureByIndex(1) : set.getTextureByIndex(0)));

        Model model = switch(type) {
            case "flat" -> Models.RAIL_FLAT;
            case "curved" -> Models.RAIL_CURVED;
            case "raised_ne" -> Models.TEMPLATE_RAIL_RAISED_NE;
            case "raised_sw" -> Models.TEMPLATE_RAIL_RAISED_SW;
            default -> Models.RAIL_FLAT;
        };

        Identifier modelId = getModelId(type, setIdx);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String type, int setIdx) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d", GENERATED_PATH, def.getBlockName(), type, setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, ModBlock blockDefinition) {
        ModBlock.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        String texturePath = String.format("block/%s",  firstSet.getTextureByIndex(0));
        Identifier textureId = WesterosBlocks.id(texturePath);

        Models.GENERATED.upload(
                ModelIds.getItemModelId(currentBlock.asItem()),
                TextureMap.layer0(textureId),
                itemModelGenerator.writer
        );
    }
}