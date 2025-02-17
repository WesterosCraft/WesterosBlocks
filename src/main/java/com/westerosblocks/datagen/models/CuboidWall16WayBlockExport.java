package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CuboidWall16WayBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    private static final String[] FACING_DIRECTIONS = {
            "facing=north", "facing=east", "facing=south", "facing=west"
    };

    private static final int[] BASE_ROTATIONS = {0, 90, 180, 270};

    public CuboidWall16WayBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    @Override
    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModBlockStateRecord sr : def.states) {
            if (!sr.isCustomModel()) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateWallModels(generator, sr, setIdx);
                }
            }

            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);

            // Generate variants for each facing direction and rotation
            for (int faceIdx = 0; faceIdx < FACING_DIRECTIONS.length; faceIdx++) {
                // For each rotation (0-15)
                for (int rotation = 0; rotation < 16; rotation++) {
                    for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                        ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                        // Calculate total rotation based on facing direction and rotation state
                        // Convert 22.5 degree increments to integer degrees
                        int totalRotation = (BASE_ROTATIONS[faceIdx] + (int)(rotation * 22.5)) % 360;

                        Identifier modelId = getModelId(setIdx, sr.isCustomModel());
                        BlockStateVariant variant = VariantBuilder.create(
                                modelId,
                                set,
                                totalRotation,
                                0,
                                true  // UV lock
                        );

                        String condition = FACING_DIRECTIONS[faceIdx] + ",rotation=" + rotation;
                        blockStateBuilder.addVariant(condition, variant, stateIDs, variants);
                    }
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateWallModels(BlockStateModelGenerator generator, ModBlockStateRecord sr, int setIdx) {
        ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
        TextureMap textureMap = createCuboidTextureMap(set);

        // Generate the wall model
        Identifier modelId = getModelId(setIdx, sr.isCustomModel());
        Model model = ModModels.WALL_16WAY_CUBOID();
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private TextureMap createCuboidTextureMap(ModBlock.RandomTextureSet set) {
        return new TextureMap()
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(ModTextureKey.TEXTURE_0, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(ModTextureKey.TEXTURE_1, createBlockIdentifier(set.getTextureByIndex(1)))
                .put(ModTextureKey.TEXTURE_2, createBlockIdentifier(set.getTextureByIndex(2)))
                .put(ModTextureKey.TEXTURE_3, createBlockIdentifier(set.getTextureByIndex(3)))
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(set.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(set.getTextureByIndex(5)));
    }

    private Identifier getModelId(int setIdx, boolean isCustom) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                isCustom ? CUSTOM_PATH : GENERATED_PATH,
                def.getBlockName(),
                "wall",
                setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        generateBlockBasedItemModel(itemModelGenerator, block, blockDefinition);
    }
}