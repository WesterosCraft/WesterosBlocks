package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModelExport {
    public static final String GENERATED_PATH = "block/generated/";
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
    protected static BlockStateVariant createVariant(Identifier modelPath, Integer weight, Integer rotationIndex) {
        BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelPath);

        if (weight != null && weight > 0) {
            variant.put(VariantSettings.WEIGHT, weight);
        }

        if (rotationIndex != null && rotationIndex > 0) {
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

    /**
     * used in model generation to skip transparent textures when mapping faces, ensuring rendering of partially transparent blocks
     */
    protected static boolean isTransparentTexture(String texturePath) {
        if (texturePath == null) return false;
        return texturePath.contains("transparent");
    }

    /**
     * Creates a block state supplier with variants based on block type and configuration
     */
    protected static BlockStateSupplier createBlockStateWithVariants(
            Block block,
            List<Property<?>> properties,
            VariantListProvider variantProvider,
            boolean useMultipart) {

        if (useMultipart) {
            return createMultipartBlockState(block, properties, variantProvider);
        }
        return createVariantsBlockState(block, properties, variantProvider);
    }

    /**
     * Functional interface to provide variant lists for different property combinations
     */
    @FunctionalInterface
    protected interface VariantListProvider {
        List<BlockStateVariant> getVariants(Map<Property<?>, Comparable<?>> propertyValues);
    }

    private static BlockStateSupplier createVariantsBlockState(
            Block block,
            List<Property<?>> properties,
            VariantListProvider variantProvider) {

        VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block);

        if (properties.size() == 1) {
            supplier.coordinate(createSinglePropertyVariantMap(properties.getFirst(), variantProvider));
        } else if (properties.size() == 2) {
            supplier.coordinate(createDoublePropertyVariantMap(properties.get(0), properties.get(1), variantProvider));
        } else if (properties.size() == 3) {
            supplier.coordinate(createTriplePropertyVariantMap(
                    properties.get(0), properties.get(1), properties.get(2), variantProvider));
        }

        return supplier;
    }

    private static BlockStateSupplier createMultipartBlockState(
            Block block,
            List<Property<?>> properties,
            VariantListProvider variantProvider) {

        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

        // Add base variants without conditions if needed
        List<BlockStateVariant> baseVariants = variantProvider.getVariants(new HashMap<>());
        if (!baseVariants.isEmpty()) {
            supplier.with(baseVariants);
        }

        // Add conditional variants for each property
        for (Property<?> property : properties) {
            for (Comparable<?> value : property.getValues()) {
                Map<Property<?>, Comparable<?>> propertyMap = new HashMap<>();
                propertyMap.put(property, value);

                List<BlockStateVariant> variants = variantProvider.getVariants(propertyMap);
                if (!variants.isEmpty()) {
                    // Create condition using proper generic type handling
                    supplier.with(createCondition(property, value), variants);
                }
            }
        }

        return supplier;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> When createCondition(Property<?> property, Comparable<?> value) {
        return When.create().set((Property<T>) property, (T) value);
    }

    private static <T extends Comparable<T>> BlockStateVariantMap createSinglePropertyVariantMap(
            Property<T> property,
            VariantListProvider variantProvider) {

        BlockStateVariantMap.SingleProperty<T> map = BlockStateVariantMap.create(property);

        for (T value : property.getValues()) {
            Map<Property<?>, Comparable<?>> propertyMap = new HashMap<>();
            propertyMap.put(property, value);

            List<BlockStateVariant> variants = variantProvider.getVariants(propertyMap);
            if (!variants.isEmpty()) {
                map.register(value, variants);
            }
        }

        return map;
    }

    private static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> BlockStateVariantMap createDoublePropertyVariantMap(
            Property<T1> property1,
            Property<T2> property2,
            VariantListProvider variantProvider) {

        BlockStateVariantMap.DoubleProperty<T1, T2> map = BlockStateVariantMap.create(property1, property2);

        for (T1 value1 : property1.getValues()) {
            for (T2 value2 : property2.getValues()) {
                Map<Property<?>, Comparable<?>> propertyMap = new HashMap<>();
                propertyMap.put(property1, value1);
                propertyMap.put(property2, value2);

                List<BlockStateVariant> variants = variantProvider.getVariants(propertyMap);
                if (!variants.isEmpty()) {
                    map.register(value1, value2, variants);
                }
            }
        }

        return map;
    }

    private static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>>
    BlockStateVariantMap createTriplePropertyVariantMap(
            Property<T1> property1,
            Property<T2> property2,
            Property<T3> property3,
            VariantListProvider variantProvider) {

        BlockStateVariantMap.TripleProperty<T1, T2, T3> map =
                BlockStateVariantMap.create(property1, property2, property3);

        for (T1 value1 : property1.getValues()) {
            for (T2 value2 : property2.getValues()) {
                for (T3 value3 : property3.getValues()) {
                    Map<Property<?>, Comparable<?>> propertyMap = new HashMap<>();
                    propertyMap.put(property1, value1);
                    propertyMap.put(property2, value2);
                    propertyMap.put(property3, value3);

                    List<BlockStateVariant> variants = variantProvider.getVariants(propertyMap);
                    if (!variants.isEmpty()) {
                        map.register(value1, value2, value3, variants);
                    }
                }
            }
        }

        return map;
    }

    /**
     * Creates a BlockStateSupplier for a block using all its properties.
     * @param block The block to create the state supplier for
     * @param modelId The base model identifier to use
     * @param blockStateCollector The collector to accept the final state
     * @return The created BlockStateSupplier
     */
    public static BlockStateSupplier createBlockStateSupplier(Block block, Identifier modelId, Consumer<BlockStateSupplier> blockStateCollector) {
        StateManager<Block, BlockState> stateManager = block.getStateManager();
        Collection<Property<?>> properties = stateManager.getProperties();

        if (properties.isEmpty()) {
            // If no properties, create a simple singleton state
            return VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
        }

        // Create variant map based on number of properties
        BlockStateVariantMap variantMap = createVariantMap(properties, modelId);

        return VariantsBlockStateSupplier.create(block).coordinate(variantMap);
    }
}
