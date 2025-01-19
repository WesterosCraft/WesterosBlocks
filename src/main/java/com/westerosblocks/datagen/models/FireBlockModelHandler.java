package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

public class FireBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public FireBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        // Create multipart block state supplier
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // Generate models first if not custom
        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                generateFireModels(generator, set, setIdx);
            }
        }

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

            // Base case - no connections
            When baseCase = When.create()
                    .set(Properties.NORTH, false)
                    .set(Properties.SOUTH, false)
                    .set(Properties.EAST, false)
                    .set(Properties.WEST, false)
                    .set(Properties.UP, false);

            // Add floor models
            stateSupplier.with(baseCase,
                    BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("floor0", setIdx)),
                    BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("floor1", setIdx)));

            // Add side models for each direction
            addDirectionalFireVariants(stateSupplier, setIdx, "north", 0);
            addDirectionalFireVariants(stateSupplier, setIdx, "east", 90);
            addDirectionalFireVariants(stateSupplier, setIdx, "south", 180);
            addDirectionalFireVariants(stateSupplier, setIdx, "west", 270);

            // Add up models
            When upCase = When.create().set(Properties.UP, true);
            stateSupplier.with(upCase,
                    BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up0", setIdx)),
                    BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up1", setIdx)),
                    BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up_alt0", setIdx)),
                    BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up_alt1", setIdx)));
        }

        generator.blockStateCollector.accept(stateSupplier);
    }

    public Property<?> getDirection(String direction) {
        return switch (direction) {
            case "east" -> Properties.EAST;
            case "south" -> Properties.SOUTH;
            case "west" -> Properties.WEST;
            default -> Properties.NORTH;
        };
    }

    private void addDirectionalFireVariants(MultipartBlockStateSupplier stateSupplier, int setIdx, String direction, int rotation) {
        When dirCase = When.create().set((Property<Boolean>) getDirection(direction), true);
        BlockStateVariant rotationVariant = BlockStateVariant.create()
                .put(VariantSettings.Y, getRotation(rotation));

        stateSupplier.with(dirCase,
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side0", setIdx)),
                        rotationVariant),
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side1", setIdx)),
                        rotationVariant),
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side_alt0", setIdx)),
                        rotationVariant),
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side_alt1", setIdx)),
                        rotationVariant));
    }

    private void generateFireModels(BlockStateModelGenerator generator,
                                    WesterosBlockDef.RandomTextureSet set,
                                    int setIdx) {
        String texture0 = set.getTextureByIndex(0);
        String texture1 = set.getTextureByIndex(1);

        // Generate floor models
        generateFireModel(generator, "floor0", texture0, setIdx, Models.TEMPLATE_FIRE_FLOOR);
        generateFireModel(generator, "floor1", texture1, setIdx, Models.TEMPLATE_FIRE_FLOOR);

        // Generate side models
        generateFireModel(generator, "side0", texture0, setIdx, Models.TEMPLATE_FIRE_SIDE);
        generateFireModel(generator, "side1", texture1, setIdx, Models.TEMPLATE_FIRE_SIDE);
        generateFireModel(generator, "side_alt0", texture0, setIdx, Models.TEMPLATE_FIRE_SIDE_ALT);
        generateFireModel(generator, "side_alt1", texture1, setIdx, Models.TEMPLATE_FIRE_SIDE_ALT);

        // Generate up models
        generateFireModel(generator, "up0", texture0, setIdx, Models.TEMPLATE_FIRE_UP);
        generateFireModel(generator, "up1", texture1, setIdx, Models.TEMPLATE_FIRE_UP);
        generateFireModel(generator, "up_alt0", texture0, setIdx, Models.TEMPLATE_FIRE_UP_ALT);
        generateFireModel(generator, "up_alt1", texture1, setIdx, Models.TEMPLATE_FIRE_UP_ALT);
    }

    private void generateFireModel(BlockStateModelGenerator generator,
                                   String variant,
                                   String texture,
                                   int setIdx,
                                   Model model) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.FIRE, createBlockIdentifier(texture));

        model.upload(getModelId(variant, setIdx), textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String variant, int setIdx) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d",
                        GENERATED_PATH,
                        def.getBlockName(),
                        variant,
                        setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
                                          Block currentBlock,
                                          WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(currentBlock.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}