package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class FenceBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private final boolean isTinted;
    private final boolean hasOverlay;

    private static final ModelRec[] PARTS = {
            // Post
            new ModelRec("post", null, null, null, null, null, null),
            // North fence
            new ModelRec("side", "true", null, null, null, true, null),
            // East fence
            new ModelRec("side", null, null, "true", null, true, 90),
            // South fence
            new ModelRec("side", null, "true", null, null, true, 180),
            // West fence
            new ModelRec("side", null, null, null, "true", true, 270)
    };

    private record ModelRec(String modelExt, String north, String south, String east, String west, Boolean uvLock,
                            Integer yRot) {
    }

    public FenceBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;

        WesterosBlockStateRecord firstState = def.states.getFirst();
        this.isTinted = firstState.isTinted();
        this.hasOverlay = firstState.getOverlayTextureByIndex(0) != null;
    }

    public void generateBlockStateModels() {
        // Create multipart block state supplier
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // Generate models first if not custom - do this ONCE before generating blockstates
        if (!def.isCustomModel()) {
            for (WesterosBlockStateRecord sr : def.states) {
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    generateFenceModels(generator, sr, setIdx);
                }
            }
        }

        // Generate variants for each state record
        for (WesterosBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);

            // Process each component of the fence
            for (ModelRec part : PARTS) {
                // Generate variants for each texture set
                for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                    WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

                    BlockStateVariant variant = BlockStateVariant.create();
                    variant.put(VariantSettings.MODEL, getModelId(part.modelExt(), setIdx, sr.stateID));
                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }
                    if (part.uvLock() != null) {
                        variant.put(VariantSettings.UVLOCK, part.uvLock());
                    }
                    if (part.yRot() != null) {
                        variant.put(VariantSettings.Y, getRotation(part.yRot()));
                    }

                    // Create when condition if needed
                    When when = null;
                    if (part.north() != null || part.south() != null ||
                            part.east() != null || part.west() != null) {
                        When.PropertyCondition condition = When.create();
                        if (part.north() != null) condition.set(Properties.NORTH, Boolean.parseBoolean(part.north()));
                        if (part.south() != null) condition.set(Properties.SOUTH, Boolean.parseBoolean(part.south()));
                        if (part.east() != null) condition.set(Properties.EAST, Boolean.parseBoolean(part.east()));
                        if (part.west() != null) condition.set(Properties.WEST, Boolean.parseBoolean(part.west()));
                        when = condition;
                    }

                    // Add to state supplier
                    if (when != null) {
                        stateSupplier.with(when, variant);
                    } else {
                        stateSupplier.with(variant);
                    }
                }
            }
        }

        generator.blockStateCollector.accept(stateSupplier);
    }

    private void generateFenceModels(BlockStateModelGenerator generator, WesterosBlockStateRecord sr, int setIdx) {
        WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);

        generateFenceModel(generator, "post", set, sr, setIdx, "untinted/fence_post");
        generateFenceModel(generator, "side", set, sr, setIdx, "untinted/fence_side");
    }

    private void generateFenceModel(BlockStateModelGenerator generator,
                                    String variant,
                                    WesterosBlockDef.RandomTextureSet set,
                                    WesterosBlockStateRecord sr,
                                    int setIdx,
                                    String baseParent) {
        String parent = baseParent;
        if (isTinted) {
            parent = parent.replace("untinted", "tinted");
        }
        if (hasOverlay) {
            parent += "_overlay";
        }

        TextureMap textures = new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(set.getTextureByIndex(2)));

        if (hasOverlay) {
            textures.put(ModTextureKey.BOTTOM_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(0)))
                    .put(ModTextureKey.TOP_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(1)))
                    .put(ModTextureKey.SIDE_OVERLAY, createBlockIdentifier(sr.getOverlayTextureByIndex(2)));
        }

        Identifier modelId = getModelId(variant, setIdx, sr.stateID);
        Model model = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + parent)),
                Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE
        );

        Model overlayModel = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + parent)),
                Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY
        );

        if (hasOverlay) {
            overlayModel.upload(modelId, textures, generator.modelCollector);
        } else {
            model.upload(modelId, textures, generator.modelCollector);
        }
    }

    private Identifier getModelId(String variant, int setIdx, String stateId) {
        String path = stateId == null ?
                String.format("%s/%s_v%d", def.blockName, variant, setIdx + 1) :
                String.format("%s/%s/%s_v%d", def.blockName, stateId, variant, setIdx + 1);

        return Identifier.of(WesterosBlocks.MOD_ID, GENERATED_PATH + path);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
                                          Block block,
                                          WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        WesterosBlockDef.RandomTextureSet firstSet = firstState.getRandomTextureSet(0);
        boolean isTinted = firstState.isTinted();

        String parent = "block/" + (isTinted ? "tinted" : "untinted") + "/fence_inventory";

        TextureMap textures = new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(firstSet.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(firstSet.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(firstSet.getTextureByIndex(2)));

        Model model = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, parent)),
                Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE
        );

        model.upload(ModelIds.getItemModelId(block.asItem()), textures, itemModelGenerator.writer);

        if (isTinted) {
            String tintResource = blockDefinition.getBlockColorMapResource();
            if (tintResource != null) {
                // TODO: Handle tinting registration
            }
        }
    }
}