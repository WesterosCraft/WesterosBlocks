package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import com.westerosblocks.block.custom.WCSolidBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class SolidBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public SolidBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    protected static String getModelName(ModBlock def, String ext, int setIdx, Boolean symmetrical) {
        String dir = symmetrical ? "symmetrical" : "asymmetrical";
        return def.blockName + "/" + dir + "/" + ext + ("_v" + (setIdx + 1));
    }

    public static Identifier modelFileName(ModBlock def, String ext, int setIdx, Boolean symmetrical) {
        Identifier id = WesterosBlocks.id(getModelName(def, ext, setIdx, symmetrical));
        return id.withPrefixedPath(GENERATED_PATH);
    }

    // Add a differently named method for regular (non-symmetrical) paths
    protected static String getStandardModelName(ModBlock def, String ext, int setIdx) {
        return def.blockName + "/" + ext + ("_v" + (setIdx + 1));
    }

    public static Identifier standardModelFileName(ModBlock def, String ext, int setIdx, Boolean isCustom) {
        String path = String.format("%s%s/%s_v%d",
                isCustom ? CUSTOM_PATH : GENERATED_PATH,
                def.blockName, ext, setIdx + 1);
        return WesterosBlocks.id(path);
    }

    public void generateBlockStateModels() {
        WCSolidBlock solidBlock = (block instanceof WCSolidBlock) ? (WCSolidBlock) block : null;
        boolean isSymmetrical = solidBlock != null && solidBlock.symmetrical;
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String fname = justBase ? "base" : sr.stateID;
            boolean isTinted = sr.isTinted();
            boolean hasOverlay = sr.getOverlayTextureByIndex(0) != null;

            for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                ModBlock.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
                int rotationCount = sr.rotateRandom ? 4 : 1;    // 4 for random, just 1 if not

                for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
                    if (isSymmetrical) {
                        BlockStateVariant varSymmetrical = BlockStateVariant.create();
                        Identifier symId = modelFileName(def, fname, setIdx, true);
                        varSymmetrical.put(VariantSettings.MODEL, symId);
                        if (set.weight != null) {
                            varSymmetrical.put(VariantSettings.WEIGHT, set.weight);
                        }
                        if (rotIdx > 0) {
                            varSymmetrical.put(VariantSettings.Y, getRotation(90 * rotIdx));
                        }
                        blockStateBuilder.addVariant("symmetrical=true", varSymmetrical, stateIDs, variants);

                        BlockStateVariant varAsymmetrical = BlockStateVariant.create();
                        Identifier asymId = modelFileName(def, fname, setIdx,  false);
                        varAsymmetrical.put(VariantSettings.MODEL, modelFileName(def, fname, setIdx, false));
                        if (set.weight != null) {
                            varAsymmetrical.put(VariantSettings.WEIGHT, set.weight);
                        }
                        if (rotIdx > 0) {
                            varAsymmetrical.put(VariantSettings.Y, getRotation(90 * rotIdx));
                        }
                        blockStateBuilder.addVariant("symmetrical=false", varAsymmetrical, stateIDs, variants);

                        generateSolidModel(generator, symId, true,
                                isTinted, hasOverlay, sr, setIdx);
                        generateSolidModel(generator, asymId, false,
                                isTinted, hasOverlay, sr, setIdx);
                    } else {
                        BlockStateVariant variant = BlockStateVariant.create();
                        Identifier id = standardModelFileName(def, fname, setIdx, sr.isCustomModel());
                        variant.put(VariantSettings.MODEL, id);
                        if (set.weight != null) {
                            variant.put(VariantSettings.WEIGHT, set.weight);
                        }
                        if (rotIdx > 0) {
                            variant.put(VariantSettings.Y, getRotation(90 * rotIdx));
                        }
                        blockStateBuilder.addVariant("", variant, stateIDs, variants);

                        generateSolidModel(generator, id, false, isTinted, hasOverlay, sr, setIdx);
                    }
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    protected void generateSolidModel(BlockStateModelGenerator generator, Identifier modelPath,
                                      boolean isSymmetrical, boolean isTinted, boolean hasOverlay,
                                      ModBlockStateRecord currentRec, int setIdx) {
        ModBlock.RandomTextureSet set = currentRec.getRandomTextureSet(setIdx);
        TextureMap textureMap = createTextureMap(set, isSymmetrical, hasOverlay, isTinted, currentRec);

        // For blocks with multiple textures, use CUBE model which requires all sides
        if (set.getTextureCount() > 1 && !hasOverlay) {
            Models.CUBE.upload(modelPath, textureMap, generator.modelCollector);
        } else if (hasOverlay) {
            String parentPath = isTinted ? "tinted/cube_overlay" : "untinted/cube_overlay";
            ModModels.ALL_SIDES_OVERLAY(parentPath, this.def)
                    .upload(modelPath, textureMap, generator.modelCollector);
        } else if (set.getTextureCount() > 1 || isTinted) {
            String parentPath = isTinted ? "tinted/cube" : "cube";
            String namespace = isTinted ? WesterosBlocks.MOD_ID : "minecraft";
            ModModels.ALL_SIDES(namespace, parentPath, this.def)
                    .upload(modelPath, textureMap, generator.modelCollector);
        } else {
            TextureMap textureMapAll = TextureMap.all(createBlockIdentifier(set.getTextureByIndex(0)));
            Models.CUBE_ALL.upload(modelPath, textureMapAll, generator.modelCollector);
        }
    }

    private static TextureMap createTextureMap(ModBlock.RandomTextureSet ts, boolean isSymmetrical, boolean hasOverlay, boolean isTinted, ModBlockStateRecord currentRec) {
        // For special blocks that need specific texture handling
        if (ts.getTextureCount() > 1 && !hasOverlay) {
            // Create a texture map that ensures all sides have a texture
            TextureMap fullMap = new TextureMap();

            // Add particle texture
            fullMap.put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(0)));

            // Texture keys in order they'll be used
            TextureKey[] sides = {
                    TextureKey.DOWN, TextureKey.UP,
                    TextureKey.NORTH, TextureKey.SOUTH,
                    TextureKey.EAST, TextureKey.WEST
            };

            for (int i = 0; i < sides.length; i++) {
                // For textures beyond what's available in the set, use the last available texture
                int textureIndex = Math.min(i, ts.getTextureCount() - 1);
                fullMap.put(sides[i], createBlockIdentifier(ts.getTextureByIndex(textureIndex)));
            }

            return fullMap;
        } else if (hasOverlay) {
            // Maintain original overlay logic with symmetrical parameter
            return ModTextureMap.frontTopSides(ts, currentRec, true, isSymmetrical);
        } else if (currentRec.getTextureCount() > 1 || isTinted) {
            // Maintain original multi-texture or tinted logic with symmetrical parameter
            return ModTextureMap.frontTopSides(ts, null, false, isSymmetrical);
        } else {
            // Simple single texture case
            return new TextureMap().put(TextureKey.ALL, createBlockIdentifier(ts.getTextureByIndex(0)));
        }
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, ModBlock blockDefinition) {
        ModBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        boolean isSymmetrical = currentBlock instanceof WCSolidBlock && ((WCSolidBlock) currentBlock).symmetrical;

        String path = isSymmetrical ?
                String.format("%s%s/symmetrical/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName) :
                String.format("%s%s/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty())
        );
    }
}