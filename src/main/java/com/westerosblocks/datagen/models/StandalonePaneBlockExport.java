package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCStandalonePaneBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class StandalonePaneBlockExport {
    
    public StandalonePaneBlockExport() {
    }
    
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texture) {
        WCStandalonePaneBlock paneBlock = (WCStandalonePaneBlock) block;
        
        // Create multipart block state supplier like the original
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // Generate base post model (always include for standalone panes)
        BlockStateVariant postVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, getModelId(block, "post"))
                .put(VariantSettings.UVLOCK, true);
        stateSupplier.with(postVariant);

        // Add directional variants
        addDirectionalVariants(stateSupplier, block, "north", 0);
        addDirectionalVariants(stateSupplier, block, "east", 90);
        addDirectionalVariants(stateSupplier, block, "south", 180);
        addDirectionalVariants(stateSupplier, block, "west", 270);

        // Add no-side variants
        addNoSideVariants(stateSupplier, block, "north", 0);
        addNoSideVariants(stateSupplier, block, "east", 90);
        addNoSideVariants(stateSupplier, block, "south", 180);
        addNoSideVariants(stateSupplier, block, "west", 270);

        // Generate the pane models using the texture
        generatePaneModels(generator, block, texture);

        generator.blockStateCollector.accept(stateSupplier);
    }
    
    private void addDirectionalVariants(MultipartBlockStateSupplier stateSupplier, Block block, String direction, int rotation) {
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
                .put(VariantSettings.MODEL, getModelId(block, "side"))
                .put(VariantSettings.Y, getRotation(rotation))
                .put(VariantSettings.UVLOCK, true);

        // Add a single multipart entry with an OR condition (like legacy model behavior)
        stateSupplier.with(
                When.anyOf(directionCondition, allFalseCondition),
                variant
        );
    }

    private void addNoSideVariants(MultipartBlockStateSupplier stateSupplier, Block block, String direction, int rotation) {
        When.PropertyCondition condition = When.create().set(getProperty(direction), false);

        BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, getModelId(block, "noside"))
                .put(VariantSettings.UVLOCK, true);

        if (rotation > 0) {
            variant.put(VariantSettings.Y, getRotation(rotation));
        }

        stateSupplier.with(condition, variant);
    }
    
    private void generatePaneModels(BlockStateModelGenerator generator, Block block, String texture) {
        // Generate post model
        generatePaneModel(generator, block, "post", texture, "untinted/ctm_pane_post");

        // Generate side model
        generatePaneModel(generator, block, "side", texture, "untinted/ctm_pane_side");

        // Generate noside model
        generatePaneModel(generator, block, "noside", texture, "untinted/ctm_pane_noside");
    }

    private void generatePaneModel(BlockStateModelGenerator generator, Block block, String variant, String texture, String parent) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.SIDE, ModelExport2.createBlockIdentifier(texture))
                .put(ModTextureKey.CAP, ModelExport2.createBlockIdentifier(texture));

        Identifier modelId = getModelId(block, variant);
        Model model = new Model(
                Optional.of(WesterosBlocks.id("block/" + parent)),
                Optional.empty(),
                TextureKey.SIDE,
                ModTextureKey.CAP
        );

        model.upload(modelId, textureMap, generator.modelCollector);
    }
    
    private Identifier getModelId(Block block, String variant) {
        return ModelIds.getBlockModelId(block).withSuffixedPath("_" + variant);
    }

    private Property<Boolean> getProperty(String direction) {
        return switch (direction) {
            case "east" -> Properties.EAST;
            case "south" -> Properties.SOUTH;
            case "west" -> Properties.WEST;
            default -> Properties.NORTH;
        };
    }
    
    private VariantSettings.Rotation getRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }
    
    public void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, String texture) {
        TextureMap textureMap = TextureMap.layer0(ModelExport2.createBlockIdentifier(texture));
        
        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
} 