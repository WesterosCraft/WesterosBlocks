package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.custom.WCPaneBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class PaneBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private final boolean legacyModel;
    private final boolean barsModel;
    private final WCPaneBlock paneBlock;

    public PaneBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        this.paneBlock = (WCPaneBlock) block;
        this.legacyModel = paneBlock.isLegacyModel();
        this.barsModel = paneBlock.isBarsModel();
    }

    public void generateBlockStateModels() {
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // Generate base post model for non-bars type
        if (!barsModel) {
            BlockStateVariant postVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId("post", 0))
                    .put(VariantSettings.UVLOCK, true);
            stateSupplier.with(postVariant);
        }

        // Add directional variants
        addDirectionalVariants(stateSupplier, "north", 0);
        addDirectionalVariants(stateSupplier, "east", 90);
        addDirectionalVariants(stateSupplier, "south", 180);
        addDirectionalVariants(stateSupplier, "west", 270);

        // Add no-side variants for non-bars type
        if (!barsModel) {
            addNoSideVariants(stateSupplier, "north", 0);
            addNoSideVariants(stateSupplier, "east", 90);
            addNoSideVariants(stateSupplier, "south", 180);
            addNoSideVariants(stateSupplier, "west", 270);
        }

        // Generate models if not custom
        if (!def.isCustomModel()) {
            generatePaneModels(generator);
        }

        generator.blockStateCollector.accept(stateSupplier);
    }

    private void addDirectionalVariants(MultipartBlockStateSupplier stateSupplier, String direction, int rotation) {
        // Create the all-false condition
        When.PropertyCondition allFalseCondition = When.create()
                .set(Properties.NORTH, false)
                .set(Properties.SOUTH, false)
                .set(Properties.EAST, false)
                .set(Properties.WEST, false);

        // Create the direction-specific condition
        When.PropertyCondition directionCondition = When.create()
                .set(getProperty(direction), true);

        BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, getModelId("side", 0))
                .put(VariantSettings.UVLOCK, true);

        if (rotation > 0) {
            variant.put(VariantSettings.Y, getRotation(rotation));
        }

        if (legacyModel || barsModel) {
            // Add a single multipart entry with an OR condition
            stateSupplier.with(
                    When.anyOf(directionCondition, allFalseCondition),
                    variant
            );
        } else {
            // Just add the direction condition for non-legacy models
            stateSupplier.with(directionCondition, variant);
        }
    }

    private void addNoSideVariants(MultipartBlockStateSupplier stateSupplier, String direction, int rotation) {
        When.PropertyCondition condition = When.create().set(getProperty(direction), false);

        BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, getModelId("noside", 0))
                .put(VariantSettings.UVLOCK, true);

        if (rotation > 0) {
            variant.put(VariantSettings.Y, getRotation(rotation));
        }

        stateSupplier.with(condition, variant);
    }

    private void generatePaneModels(BlockStateModelGenerator generator) {
        WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(0);

        if (!barsModel) {
            // Generate post model
            generatePaneModel(generator, "post", set, "untinted/ctm_pane_post");
        }

        // Generate side model
        String sideParent = barsModel ? "untinted/bars_side" : "untinted/ctm_pane_side";
        generatePaneModel(generator, "side", set, sideParent);

        // Generate noside model for non-bars type
        if (!barsModel) {
            generatePaneModel(generator, "noside", set, "untinted/ctm_pane_noside");
        }
    }

    private void generatePaneModel(BlockStateModelGenerator generator, String variant, WesterosBlockDef.RandomTextureSet set, String parent) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.SIDE, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(ModTextureKey.CAP, createBlockIdentifier(set.getTextureByIndex(1)));

        Identifier modelId = getModelId(variant, 0);
        Model model = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + parent)),
                Optional.empty(),
                TextureKey.SIDE,
                ModTextureKey.CAP
        );

        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String variant, int setIdx) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d",
                        GENERATED_PATH,
                        def.getBlockName(),
                        variant,
                        setIdx + 1));
    }

    private Property<Boolean> getProperty(String direction) {
        return switch (direction) {
            case "east" -> Properties.EAST;
            case "south" -> Properties.SOUTH;
            case "west" -> Properties.WEST;
            default -> Properties.NORTH;
        };
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}