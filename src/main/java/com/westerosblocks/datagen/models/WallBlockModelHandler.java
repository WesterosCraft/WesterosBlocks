package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.block.enums.WallShape;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.*;

public class WallBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static final ModelPart[] PARTS = {
            // Post
            ModelPart.of("post", "true", null, null, null, null, null, null),
            // North low
            ModelPart.of("side", null, "low", null, null, null, true, null),
            // North tall
            ModelPart.of("side_tall", null, "tall", null, null, null, true, null),
            // East low
            ModelPart.of("side", null, null, null, "low", null, true, 90),
            // East tall
            ModelPart.of("side_tall", null, null, null, "tall", null, true, 90),
            // South low
            ModelPart.of("side", null, null, "low", null, null, true, 180),
            // South tall
            ModelPart.of("side_tall", null, null, "tall", null, null, true, 180),
            // West low
            ModelPart.of("side", null, null, null, null, "low", true, 270),
            // West tall
            ModelPart.of("side_tall", null, null, null, null, "tall", true, 270)
    };

    public WallBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }


    public void generateBlockStateModels() {
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        for (WesterosBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);

            // Generate models if not a custom model
            if (!sr.isCustomModel()) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateWallModels(sr, setIdx);
                }
            }

            for (ModelPart part : PARTS) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = getModelId(part.modExt(), setIdx, sr);
                    variant.put(VariantSettings.MODEL, modelId);

                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }
                    if (part.uvlock() != null) {
                        variant.put(VariantSettings.UVLOCK, part.uvlock());
                    }
                    if (part.y() != null) {
                        variant.put(VariantSettings.Y, getRotation(part.y()));
                    }

                    When.PropertyCondition baseCondition = part.condition();

                    if (stateIDs != null) {
                        for (String stateID : stateIDs) {
                            When.PropertyCondition stateCondition = When.create();
                            if (block.getStateManager().getProperty("state") != null) {
                                stateCondition.set(
                                        (WesterosBlockDef.StateProperty) block.getStateManager().getProperty("state"),
                                        stateID
                                );
                            }

                            stateSupplier.with(
                                    When.allOf(baseCondition, stateCondition),
                                    variant
                            );
                        }
                    } else {
                        stateSupplier.with(baseCondition, variant);
                    }
                }
            }
        }

        generator.blockStateCollector.accept(stateSupplier);
    }

    private void generateWallModels(WesterosBlockStateRecord sr, int setIdx) {
        boolean isTinted = sr.isTinted();
        boolean hasOverlay = sr.getOverlayTextureByIndex(0) != null;
        WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

        generateWallModelVariant("post", set, sr, setIdx, isTinted, hasOverlay);
        generateWallModelVariant("side", set, sr, setIdx, isTinted, hasOverlay);
        generateWallModelVariant("side_tall", set, sr, setIdx, isTinted, hasOverlay);
    }

    private void generateWallModelVariant(String variant, WesterosBlockDef.RandomTextureSet set,
                                          WesterosBlockStateRecord sr, int setIdx, boolean isTinted, boolean hasOverlay) {
        TextureMap textureMap = ModTextureMap.bottomTopSide(set, sr, null);
        String parentPath = getParentPath(variant, isTinted, hasOverlay);
        Identifier modelId = getModelId(variant, setIdx, sr);

        Model model = new Model(
                Optional.of(WesterosBlocks.id(parentPath)),
                Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE
        );

        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private String getParentPath(String variant, boolean isTinted, boolean hasOverlay) {
        String basePath = isTinted ? "tinted/" : "untinted/";
        String modelName = "template_wall_" + variant;
        if (hasOverlay) modelName += "_overlay";
        return "block/" + basePath + modelName;
    }

    private Identifier getModelId(String variant, int setIdx, WesterosBlockStateRecord sr) {
        String path = String.format("%s%s/%s-v%d", GENERATED_PATH, def.getBlockName(), variant, setIdx + 1);
        if (sr.stateID != null) {
            path = path + "/" + sr.stateID;
        }
        return WesterosBlocks.id(path);
    }

    private record ModelPart(
            String modExt,
            When.PropertyCondition condition,
            Boolean uvlock,
            Integer y
    ) {
        public static ModelPart of(String modExt, String up, String north, String south, String east, String west, Boolean uvlock, Integer y) {
            When.PropertyCondition condition = When.create();
            if (up != null) condition.set(Properties.UP, Boolean.parseBoolean(up));
            if (north != null) condition.set(Properties.NORTH_WALL_SHAPE, WallShape.valueOf(north.toUpperCase()));
            if (south != null) condition.set(Properties.SOUTH_WALL_SHAPE, WallShape.valueOf(south.toUpperCase()));
            if (east != null) condition.set(Properties.EAST_WALL_SHAPE, WallShape.valueOf(east.toUpperCase()));
            if (west != null) condition.set(Properties.WEST_WALL_SHAPE, WallShape.valueOf(west.toUpperCase()));
            return new ModelPart(modExt, condition, uvlock, y);
        }
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        WesterosBlockDef.RandomTextureSet firstSet = firstState.getRandomTextureSet(0);

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.WALL, createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.WALL_INVENTORY.upload(ModelIds.getItemModelId(block.asItem()), textureMap, itemModelGenerator.writer
        );
    }
}