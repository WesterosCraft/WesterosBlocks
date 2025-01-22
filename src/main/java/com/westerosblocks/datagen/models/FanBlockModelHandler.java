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

public class FanBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static final String[] FACING_DIRECTIONS = {
            "facing=east", "facing=south", "facing=west", "facing=north"
    };
    private static final int[] ROTATIONS = {90, 180, 270, 0};

    public FanBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

            // Add floor variant (default case)
            BlockStateVariant floorVariant = BlockStateVariant.create();
            Identifier floorModelId = getModelId("base", setIdx);
            floorVariant.put(VariantSettings.MODEL, floorModelId);
            if (set.weight != null) {
                floorVariant.put(VariantSettings.WEIGHT, set.weight);
            }
            blockStateBuilder.addVariant("mounted=false", floorVariant, null, variants);

            // Add wall variants for each direction when mounted=true
            for (int i = 0; i < FACING_DIRECTIONS.length; i++) {
                BlockStateVariant wallVariant = BlockStateVariant.create();
                Identifier wallModelId = getModelId("wall", setIdx);
                wallVariant.put(VariantSettings.MODEL, wallModelId);
                if (set.weight != null) {
                    wallVariant.put(VariantSettings.WEIGHT, set.weight);
                }
                wallVariant.put(VariantSettings.Y, getRotation(ROTATIONS[i]));
                blockStateBuilder.addVariant("mounted=true," + FACING_DIRECTIONS[i], wallVariant, null, variants);
            }

            if (!def.isCustomModel()) {
                generateFanModels(generator, set, setIdx);
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateFanModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set, int setIdx) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.FAN, createBlockIdentifier(set.getTextureByIndex(0)));

        // floor fan model
        Identifier floorModelId = getModelId("base", setIdx);
        String floorParentPath = def.isTinted() ? "tinted/fan" : "untinted/fan";
        Model floorModel = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + floorParentPath)),
                Optional.empty(),
                TextureKey.FAN
        );
        floorModel.upload(floorModelId, textureMap, generator.modelCollector);

        // wall fan model
        Identifier wallModelId = getModelId("wall", setIdx);
        String wallParentPath = def.isTinted() ? "tinted/wall_fan" : "untinted/wall_fan";
        Model wallModel = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + wallParentPath)),
                Optional.empty(),
                TextureKey.FAN
        );

        wallModel.upload(wallModelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String type, int setIdx) {
        String baseName = type.equals("wall") ? "wall_" + def.blockName : def.blockName;
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d",
                        GENERATED_PATH,
                        baseName,
                        type,
                        setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(currentBlock.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}