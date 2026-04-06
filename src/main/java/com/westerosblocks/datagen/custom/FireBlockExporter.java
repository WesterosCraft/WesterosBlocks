package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.client.render.model.json.MultipartModelConditionBuilder;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Optional;


public class FireBlockExporter extends BaseBlockExporter {

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
        MultipartBlockModelDefinitionCreator blockstate = MultipartBlockModelDefinitionCreator.create(block);

        // Base case: floor fire when no sides are connected
        blockstate.with(new MultipartModelConditionBuilder()
                        .put(Properties.NORTH, false)
                        .put(Properties.SOUTH, false)
                        .put(Properties.EAST, false)
                        .put(Properties.WEST, false)
                        .put(Properties.UP, false),
                BlockStateModelGenerator.createWeightedVariant(
                        new ModelVariant(floor0), new ModelVariant(floor1)));

        // Directional fire variants
        addDirectionalFireVariants(blockstate, Properties.NORTH, 0, side0, side1, sideAlt0, sideAlt1);
        addDirectionalFireVariants(blockstate, Properties.EAST, 90, side0, side1, sideAlt0, sideAlt1);
        addDirectionalFireVariants(blockstate, Properties.SOUTH, 180, side0, side1, sideAlt0, sideAlt1);
        addDirectionalFireVariants(blockstate, Properties.WEST, 270, side0, side1, sideAlt0, sideAlt1);

        // Up case: ceiling fire
        blockstate.with(new MultipartModelConditionBuilder().put(Properties.UP, true),
                BlockStateModelGenerator.createWeightedVariant(
                        new ModelVariant(up0), new ModelVariant(up1),
                        new ModelVariant(upAlt0), new ModelVariant(upAlt1)));

        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, floor0);
    }

    private static void addDirectionalFireVariants(MultipartBlockModelDefinitionCreator blockstate, Property<Boolean> direction,
                                                   int rotation, Identifier side0, Identifier side1,
                                                   Identifier sideAlt0, Identifier sideAlt1) {
        AxisRotation rot = getRotation(rotation);

        blockstate.with(new MultipartModelConditionBuilder().put(direction, true),
                BlockStateModelGenerator.createWeightedVariant(
                        new ModelVariant(side0).withRotationY(rot),
                        new ModelVariant(side1).withRotationY(rot),
                        new ModelVariant(sideAlt0).withRotationY(rot),
                        new ModelVariant(sideAlt1).withRotationY(rot)));
    }

    private static AxisRotation getRotation(int rotation) {
        return switch (rotation) {
            case 90 -> AxisRotation.R90;
            case 180 -> AxisRotation.R180;
            case 270 -> AxisRotation.R270;
            default -> AxisRotation.R0;
        };
    }

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
