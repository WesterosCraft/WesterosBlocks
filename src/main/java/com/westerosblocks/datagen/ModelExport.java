package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModelExport {

    public static Identifier createBlockIdentifier(String texturePath) {
        return Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath);
    }

    /**
     * Handles variant registration for blocks with multiple states
     */
    protected static <T extends Comparable<T>> void registerVariants(
            BlockStateModelGenerator generator,
            Block block,
            Property<T> property,
            Function<T, List<BlockStateVariant>> variantProvider) {

        BlockStateVariantMap.SingleProperty<T> variantMap = BlockStateVariantMap.create(property);

        // Register variants for each state value
        for (T value : property.getValues()) {
            List<BlockStateVariant> variants = variantProvider.apply(value);
            if (!variants.isEmpty()) {
                if (variants.size() == 1) {
                    variantMap.register(value, variants.getFirst());
                } else {
                    variantMap.register(value, variants);
                }
            }
        }

        // Create block state with variant map
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    /**
     * Handles variant registration for blocks with a single state
     */
    protected static void registerSingleState(
            BlockStateModelGenerator generator,
            Block block,
            List<BlockStateVariant> variants) {

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
        );
    }

    /**
     * Helper method to create a block state variant with optional weight and rotation
     */
    protected static BlockStateVariant createVariant(
            Identifier modelPath,
            Integer weight,
            int rotationIndex) {

        BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelPath);

        if (weight != null && weight > 0) {
            variant.put(VariantSettings.WEIGHT, weight);
        }

        if (rotationIndex > 0) {
            variant.put(VariantSettings.Y, VariantSettings.Rotation.values()[rotationIndex]);
        }

        return variant;
    }

    /**
     * Helper method to generate models with given texture mappings
     */
    protected static void generateModel(
            BlockStateModelGenerator generator,
            Identifier modelId,
            Model model,
            TextureMap textures,
            BiConsumer<Identifier, TextureMap> customModelHandler) {

        if (customModelHandler != null) {
            customModelHandler.accept(modelId, textures);
        } else {
            model.upload(modelId, textures, generator.modelCollector);
        }
    }

}
