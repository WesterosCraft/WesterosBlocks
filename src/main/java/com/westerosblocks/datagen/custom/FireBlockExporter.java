package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Exporter for fire blocks following block-models.md patterns.
 * Generates models for animated fire blocks with directional multipart states.
 */
public class FireBlockExporter extends BaseBlockExporter {

    // ========================================
    // Public Registration Methods (block-models.md 5.5)
    // ========================================

    /**
     * Registers a fire block with two animation textures.
     * Fire blocks use vanilla models with custom textures.
     */
    public static void registerFireBlock(BlockStateModelGenerator generator, Block block, String texture0, String texture1) {
        // Fire blocks use vanilla template models
        TextureMap textureMap0 = new TextureMap().put(TextureKey.FIRE, createBlockIdentifier(texture0));
        TextureMap textureMap1 = new TextureMap().put(TextureKey.FIRE, createBlockIdentifier(texture1));

        // Generate all fire model variants
        Identifier floor0 = Models.TEMPLATE_FIRE_FLOOR.upload(createNestedModelId(block, "floor0"), textureMap0, generator.modelCollector);
        Identifier floor1 = Models.TEMPLATE_FIRE_FLOOR.upload(createNestedModelId(block, "floor1"), textureMap1, generator.modelCollector);
        Identifier side0 = Models.TEMPLATE_FIRE_SIDE.upload(createNestedModelId(block, "side0"), textureMap0, generator.modelCollector);
        Identifier side1 = Models.TEMPLATE_FIRE_SIDE.upload(createNestedModelId(block, "side1"), textureMap1, generator.modelCollector);
        Identifier sideAlt0 = Models.TEMPLATE_FIRE_SIDE_ALT.upload(createNestedModelId(block, "side_alt0"), textureMap0, generator.modelCollector);
        Identifier sideAlt1 = Models.TEMPLATE_FIRE_SIDE_ALT.upload(createNestedModelId(block, "side_alt1"), textureMap1, generator.modelCollector);
        Identifier up0 = Models.TEMPLATE_FIRE_UP.upload(createNestedModelId(block, "up0"), textureMap0, generator.modelCollector);
        Identifier up1 = Models.TEMPLATE_FIRE_UP.upload(createNestedModelId(block, "up1"), textureMap1, generator.modelCollector);
        Identifier upAlt0 = Models.TEMPLATE_FIRE_UP_ALT.upload(createNestedModelId(block, "up_alt0"), textureMap0, generator.modelCollector);
        Identifier upAlt1 = Models.TEMPLATE_FIRE_UP_ALT.upload(createNestedModelId(block, "up_alt1"), textureMap1, generator.modelCollector);

        // Create multipart blockstate
        MultipartBlockStateSupplier blockstate = MultipartBlockStateSupplier.create(block);

        // Base case: floor fire when no sides are connected
        blockstate.with(When.create()
                        .set(Properties.NORTH, false)
                        .set(Properties.SOUTH, false)
                        .set(Properties.EAST, false)
                        .set(Properties.WEST, false)
                        .set(Properties.UP, false),
                BlockStateVariant.create().put(VariantSettings.MODEL, floor0),
                BlockStateVariant.create().put(VariantSettings.MODEL, floor1));

        // Directional fire variants
        addDirectionalFireVariants(blockstate, Properties.NORTH, 0, side0, side1, sideAlt0, sideAlt1);
        addDirectionalFireVariants(blockstate, Properties.EAST, 90, side0, side1, sideAlt0, sideAlt1);
        addDirectionalFireVariants(blockstate, Properties.SOUTH, 180, side0, side1, sideAlt0, sideAlt1);
        addDirectionalFireVariants(blockstate, Properties.WEST, 270, side0, side1, sideAlt0, sideAlt1);

        // Up case: ceiling fire
        blockstate.with(When.create().set(Properties.UP, true),
                BlockStateVariant.create().put(VariantSettings.MODEL, up0),
                BlockStateVariant.create().put(VariantSettings.MODEL, up1),
                BlockStateVariant.create().put(VariantSettings.MODEL, upAlt0),
                BlockStateVariant.create().put(VariantSettings.MODEL, upAlt1));

        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, floor0);
    }

    private static void addDirectionalFireVariants(MultipartBlockStateSupplier blockstate, Property<Boolean> direction,
                                                   int rotation, Identifier side0, Identifier side1,
                                                   Identifier sideAlt0, Identifier sideAlt1) {
        BlockStateVariant rotationVariant = BlockStateVariant.create()
                .put(VariantSettings.Y, getRotation(rotation));

        blockstate.with(When.create().set(direction, true),
                BlockStateVariant.union(BlockStateVariant.create().put(VariantSettings.MODEL, side0), rotationVariant),
                BlockStateVariant.union(BlockStateVariant.create().put(VariantSettings.MODEL, side1), rotationVariant),
                BlockStateVariant.union(BlockStateVariant.create().put(VariantSettings.MODEL, sideAlt0), rotationVariant),
                BlockStateVariant.union(BlockStateVariant.create().put(VariantSettings.MODEL, sideAlt1), rotationVariant));
    }

    private static VariantSettings.Rotation getRotation(int rotation) {
        return switch (rotation) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }

    // ========================================
    // BlockDefinition Integration (block-models.md 5.6)
    // ========================================

    public static void registerCustomFireBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (definition.getTextures() != null && definition.getTextures().size() >= 2) {
            String texture0 = definition.getTextures().get(0);
            String texture1 = definition.getTextures().get(1);
            registerFireBlock(generator, block, texture0, texture1);
        } else {
            // Fallback for fire blocks with insufficient textures
            registerFireBlock(generator, block, "missingno", "missingno");
        }
    }
}
