package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.*;

public class CuboidVertical8WayBlockExport extends CuboidBlockExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    // Using only the allowed rotation angles
    private static final String[] ROTATION_MODIFIERS = {
            "",         // 0 degrees
            "_rot22",   // 22.5 degrees
            "_rot45",   // 45 degrees
            "_rotn22",  // -22.5 degrees
            "_rotn45"   // -45 degrees
    };

    private static final Direction[] HORIZONTAL_FACINGS = {
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST
    };

    public CuboidVertical8WayBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String baseName = justBase ? "base" : sr.stateID;

            // For each wall facing direction
            for (Direction facing : HORIZONTAL_FACINGS) {
                // For each rotation on wall (8 positions)
                for (int rotation = 0; rotation < 8; rotation++) {
                    for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                        ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                        BlockStateVariant variant = BlockStateVariant.create();

                        // Map the 8-way rotation to our available models
                        String modelRotation = switch(rotation) {
                            case 0 -> "";         // 0°
                            case 1 -> "_rot22";   // 45°
                            case 2 -> "_rot45";   // 90°
                            case 3 -> "_rot22";   // 135°
                            case 4 -> "";         // 180°
                            case 5 -> "_rotn22";  // 225°
                            case 6 -> "_rotn45";  // 270°
                            case 7 -> "_rotn22";  // 315°
                            default -> "";
                        };

                        Identifier modelId = getModelId(baseName + modelRotation, setIdx, sr.isCustomModel());
                        variant.put(VariantSettings.MODEL, modelId);

                        if (set.weight != null) {
                            variant.put(VariantSettings.WEIGHT, set.weight);
                        }

                        // Base rotation for wall facing
                        int facingRotation = switch (facing) {
                            case EAST -> 90;
                            case SOUTH -> 180;
                            case WEST -> 270;
                            default -> 0;
                        };

                        // Additional Y rotation based on 8-way position
                        int additionalRotation = (rotation * 45) % 360;

                        // Apply Y rotation to align with wall
                        variant.put(VariantSettings.Y, getRotation((facingRotation + additionalRotation) % 360));

                        // Apply X rotation to make vertical
                        variant.put(VariantSettings.X, getRotation(90));

                        String variantKey = String.format("facing=%s,rotation=%d", facing.asString(), rotation);
                        blockStateBuilder.addVariant(variantKey, variant, stateIDs, variants);
                    }
                }
            }

            if (!sr.isCustomModel()) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateCuboidModels(sr, setIdx, baseName);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateCuboidModels(ModBlockStateRecord sr, int setIdx, String baseName) {
        ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
        TextureMap textureMap = createCuboidTextureMap(set);

        // Generate base models with allowed rotations
        float[] rotations = {0f, 22.5f, 45f, -22.5f, -45f};
        for (int i = 0; i < ROTATION_MODIFIERS.length; i++) {
            generateRotatedModel(baseName + ROTATION_MODIFIERS[i], textureMap, sr, setIdx, rotations[i]);
        }
    }

    private void generateRotatedModel(String modelName, TextureMap textureMap,
                                      ModBlockStateRecord sr, int setIdx, float rotation) {
        Identifier modelId = getModelId(modelName, setIdx, false);

        // Generate with wall mounting
        ModModels.WALL_MOUNTED_CUBOID("cuboid_vertical_8way", rotation)
                .upload(modelId, textureMap, generator.modelCollector);
    }

    public Identifier getModelId(String variant, int setIdx, boolean isCustom) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                isCustom ? CUSTOM_PATH : GENERATED_PATH,
                def.getBlockName(),
                variant,
                setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        ModBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;

        String path = String.format("%s%s/%s_v1",
                firstState.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH,
                blockDefinition.getBlockName(),
                baseName);

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)),
                        Optional.empty())
        );
    }
}
