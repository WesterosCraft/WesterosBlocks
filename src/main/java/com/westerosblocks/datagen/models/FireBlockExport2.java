package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import static com.westerosblocks.datagen.ModelExport.createBlockIdentifier;

/**
 * Fire block model export for the builder system.
 * Generates block state models and item models for fire blocks.
 */
public class FireBlockExport2 {
    
    /**
     * Generates block state models for a fire block.
     * 
     * @param generator The block state model generator
     * @param block The fire block
     * @param texture0 The primary texture path
     * @param texture1 The secondary texture path (optional, can be null)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texture0, String texture1) {
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);
        
        // Generate fire models
        generateFireModels(generator, block, texture0, texture1);
        
        // Base case - no directions
        When baseCase = When.create()
                .set(Properties.NORTH, false)
                .set(Properties.SOUTH, false)
                .set(Properties.EAST, false)
                .set(Properties.WEST, false)
                .set(Properties.UP, false);

        stateSupplier.with(baseCase,
                BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "floor0")),
                BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "floor1")));

        // Add directional fire variants
        addDirectionalFireVariants(stateSupplier, block, "north", 0);
        addDirectionalFireVariants(stateSupplier, block, "east", 90);
        addDirectionalFireVariants(stateSupplier, block, "south", 180);
        addDirectionalFireVariants(stateSupplier, block, "west", 270);

        // Up case
        When upCase = When.create().set(Properties.UP, true);
        stateSupplier.with(upCase,
                BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "up0")),
                BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "up1")),
                BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "up_alt0")),
                BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "up_alt1")));

        generator.blockStateCollector.accept(stateSupplier);
    }

    /**
     * Generates block state models for a fire block with a single texture.
     * 
     * @param generator The block state model generator
     * @param block The fire block
     * @param texture The texture path
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texture) {
        generateBlockStateModels(generator, block, texture, texture);
    }

    /**
     * Generates item models for a fire block.
     * 
     * @param itemModelGenerator The item model generator
     * @param block The fire block
     * @param texture The texture path
     */
    public void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, String texture) {
        // Use the same approach as the old FireBlockExport - generateBasicItemModel
        // Create a texture map using the first fire layer texture
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texture));
        
        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            textureMap,
            itemModelGenerator.writer
        );
    }

    private Property<?> getDirection(String direction) {
        return switch (direction) {
            case "east" -> Properties.EAST;
            case "south" -> Properties.SOUTH;
            case "west" -> Properties.WEST;
            default -> Properties.NORTH;
        };
    }

    private void addDirectionalFireVariants(MultipartBlockStateSupplier stateSupplier, Block block, String direction, int rotation) {
        When dirCase = When.create().set((Property<Boolean>) getDirection(direction), true);
        BlockStateVariant rotationVariant = BlockStateVariant.create()
                .put(VariantSettings.Y, getRotation(rotation));

        stateSupplier.with(dirCase,
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "side0")),
                        rotationVariant),
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "side1")),
                        rotationVariant),
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "side_alt0")),
                        rotationVariant),
                BlockStateVariant.union(
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId(block, "side_alt1")),
                        rotationVariant));
    }

    private void generateFireModels(BlockStateModelGenerator generator, Block block, String texture0, String texture1) {
        // Generate floor models
        generateFireModel(generator, block, "floor0", texture0, Models.TEMPLATE_FIRE_FLOOR);
        generateFireModel(generator, block, "floor1", texture1, Models.TEMPLATE_FIRE_FLOOR);

        // Generate side models
        generateFireModel(generator, block, "side0", texture0, Models.TEMPLATE_FIRE_SIDE);
        generateFireModel(generator, block, "side1", texture1, Models.TEMPLATE_FIRE_SIDE);
        generateFireModel(generator, block, "side_alt0", texture0, Models.TEMPLATE_FIRE_SIDE_ALT);
        generateFireModel(generator, block, "side_alt1", texture1, Models.TEMPLATE_FIRE_SIDE_ALT);

        // Generate up models
        generateFireModel(generator, block, "up0", texture0, Models.TEMPLATE_FIRE_UP);
        generateFireModel(generator, block, "up1", texture1, Models.TEMPLATE_FIRE_UP);
        generateFireModel(generator, block, "up_alt0", texture0, Models.TEMPLATE_FIRE_UP_ALT);
        generateFireModel(generator, block, "up_alt1", texture1, Models.TEMPLATE_FIRE_UP_ALT);
    }

    private void generateFireModel(BlockStateModelGenerator generator,
                                   Block block,
                                   String variant,
                                   String texture,
                                   Model model) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.FIRE, createBlockIdentifier(texture));

        model.upload(getModelId(block, variant), textureMap, generator.modelCollector);
    }

    private Identifier getModelId(Block block, String variant) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("block/%s_%s",
                        Registries.BLOCK.getId(block).getPath(),
                        variant));
    }

    private Identifier createBlockIdentifier(String texture) {
        return Identifier.of(WesterosBlocks.MOD_ID, "block/" + texture);
    }

    private VariantSettings.Rotation getRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }
} 