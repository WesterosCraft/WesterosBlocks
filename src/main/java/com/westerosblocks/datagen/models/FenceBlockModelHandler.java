//package com.westerosblocks.datagen.models;
//
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockStateRecord;
//import com.westerosblocks.datagen.ModelExport;
//import net.minecraft.block.Block;
//import net.minecraft.client.model.ModelPart;
//import net.minecraft.data.client.*;
//import net.minecraft.registry.Registries;
//import net.minecraft.state.property.BooleanProperty;
//import net.minecraft.state.property.Properties;
//import net.minecraft.util.Identifier;
//
//public class FenceBlockModelHandler extends ModelExport {
//    private final BlockStateModelGenerator generator;
//    private final Block block;
//    private final WesterosBlockDef def;
//
//    private static final BooleanProperty NORTH = Properties.NORTH;
//    private static final BooleanProperty EAST = Properties.EAST;
//    private static final BooleanProperty SOUTH = Properties.SOUTH;
//    private static final BooleanProperty WEST = Properties.WEST;
//
//    public FenceBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
//        super(block, def);
//        this.generator = generator;
//        this.block = block;
//        this.def = def;
//    }
//
//    // Modern way to create model ID using Registries
//    private Identifier createModelId(Block block, String suffix, int setIdx, String stateId) {
//        String blockPath = Registries.BLOCK.getId(block).getPath();
//        String basePath = GENERATED_PATH + blockPath;
//        String statePath = stateId != null ? "/" + stateId : "";
//        String variantPath = suffix + "_v" + (setIdx + 1);
//
//        return Identifier.of(
//                WesterosBlocks.MOD_ID,
//                basePath + statePath + "/" + variantPath
//        );
//    }
//
//    private static ModelPart[] PARTS = {
//            // Post
//            new ModelPart("post", null, null, null, null, null, null),
//            // North low
//            new ModelPart("side", "true", null, null, null, true, null),
//            // East low
//            new ModelPart("side", null, null, "true", null, true, 90),
//            // South low
//            new ModelPart("side", null, "true", null, null, true, 180),
//            // East low
//            new ModelPart("side", null, null, null, "true", true, 270),
//    };
//
//    public void generateBlockStateModels() {
//
//        for (WesterosBlockStateRecord sr : def.states) {
//            boolean isTinted = sr.isTinted();
//            boolean hasOverlay = sr.getOverlayTextureByIndex(0) != null;
//
//            for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
//                WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
//
//                // Create the base model identifiers
//                Identifier postModelId = createModelId(block, "_post", setIdx, sr.stateID);
//                Identifier sideModelId = createModelId(block, "_side", setIdx, sr.stateID);
//
//                // Create texture mappings
//                TextureMap textureMap = createTextureMap(set, sr, hasOverlay);
//
//                // Generate and register the models
//                posModelId = Models.CUSTOM_FENCE_POST.upload(block, postModelId.getPath(), textureMap, generator.modelCollector);
//                Identifier northSideId = Models.CUSTOM_FENCE_SIDE_NORTH.upload(block, sideModelId.getPath() + "_north", textureMap, generator.modelCollector);
//                Identifier eastSideId = Models.CUSTOM_FENCE_SIDE_EAST.upload(block, sideModelId.getPath() + "_east", textureMap, generator.modelCollector);
//                Identifier southSideId = Models.CUSTOM_FENCE_SIDE_SOUTH.upload(block, sideModelId.getPath() + "_south", textureMap, generator.modelCollector);
//                Identifier westSideId = Models.CUSTOM_FENCE_SIDE_WEST.upload(block, sideModelId.getPath() + "_west", textureMap, generator.modelCollector);
//
//
//
//                // Add side connections
//                if (set.weight != null && set.weight > 0) {
//                    addWeightedConnections(stateSupplier, northSideId, eastSideId, southSideId, westSideId, set.weight);
//                } else {
//                    addConnections(stateSupplier, northSideId, eastSideId, southSideId, westSideId);
//                }
//
//
//            }
//        }
//        generateBlockStateFiles(generator, block, variants);
//    }
//
//    private void addConnections(MultipartBlockStateSupplier stateSupplier,
//                                Identifier northId, Identifier eastId, Identifier southId, Identifier westId) {
//        stateSupplier
//                .with(When.create().set(NORTH, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, northId)
//                                .put(VariantSettings.UVLOCK, true))
//                .with(When.create().set(EAST, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, eastId)
//                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
//                                .put(VariantSettings.UVLOCK, true))
//                .with(When.create().set(SOUTH, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, southId)
//                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
//                                .put(VariantSettings.UVLOCK, true))
//                .with(When.create().set(WEST, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, westId)
//                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
//                                .put(VariantSettings.UVLOCK, true));
//    }
//
//    private void addWeightedConnections(MultipartBlockStateSupplier stateSupplier,
//                                        Identifier northId, Identifier eastId, Identifier southId, Identifier westId, int weight) {
//        stateSupplier
//                .with(When.create().set(NORTH, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, northId)
//                                .put(VariantSettings.UVLOCK, true)
//                                .put(VariantSettings.WEIGHT, weight))
//                .with(When.create().set(EAST, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, eastId)
//                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
//                                .put(VariantSettings.UVLOCK, true)
//                                .put(VariantSettings.WEIGHT, weight))
//                .with(When.create().set(SOUTH, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, southId)
//                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
//                                .put(VariantSettings.UVLOCK, true)
//                                .put(VariantSettings.WEIGHT, weight))
//                .with(When.create().set(WEST, true),
//                        BlockStateVariant.create()
//                                .put(VariantSettings.MODEL, westId)
//                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
//                                .put(VariantSettings.UVLOCK, true)
//                                .put(VariantSettings.WEIGHT, weight));
//    }
//
//    private TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet set, WesterosBlockStateRecord sr, boolean hasOverlay) {
//        TextureMap map = new TextureMap()
//                .put(TextureKey.TEXTURE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + set.getTextureByIndex(0)))
//                .put(TextureKey.PARTICLE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + set.getTextureByIndex(0)));
//
////        if (hasOverlay) {
////            // Add overlay textures if present
////            map.put(TextureKey.OVERLAY, Identifier.of(WesterosBlocks.MOD_ID, "block/" + sr.getOverlayTextureByIndex(0)));
////        }
//
//        return map;
//    }
//
//    private void generateInventoryModel() {
//        WesterosBlockStateRecord sr0 = def.states.get(0);
//        WesterosBlockDef.RandomTextureSet set = sr0.getRandomTextureSet(0);
//
//        TextureMap textureMap = new TextureMap()
//                .put(TextureKey.TEXTURE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + set.getTextureByIndex(0)));
//
//        Identifier inventoryModelId = Models.CUSTOM_FENCE_INVENTORY.upload(
//                block,
//                "inventory",
//                textureMap,
//                generator.modelCollector
//        );
//
//        generator.registerParentedItemModel(block, inventoryModelId);
//    }
//}