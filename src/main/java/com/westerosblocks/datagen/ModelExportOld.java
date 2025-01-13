//package com.westerosblocks.datagen;
//
//import com.westerosblocks.WesterosBlocks;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.data.client.*;
//import net.minecraft.state.StateManager;
//import net.minecraft.state.property.EnumProperty;
//import net.minecraft.state.property.Property;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.StringIdentifiable;
//import net.minecraft.util.math.Direction;
//import org.jetbrains.annotations.NotNull;
//import net.minecraft.state.property.Properties;
//
//import java.util.*;
//import java.util.function.Function;
//
//public class ModelExportOld {
//    public static final String GENERATED_PATH = "block/generated/";
//    // Common state lists
//    public static final String[] FACING = {"north", "south", "east", "west"};
//    public static final String[] BOOLEAN = {"true", "false"};
//    public static final String[] TOPBOTTOM = {"top", "bottom"};
//    public static final String[] UPPERLOWER = {"upper", "lower"};
//    public static final String[] LEFTRIGHT = {"left", "right"};
//    public static final String[] ALLFACING = {"north", "south", "east", "west", "up", "down"};
//    public static final String[] UPFACING = {"north", "south", "east", "west", "up"};
//    public static final String[] HEADFOOT = {"head", "foot"};
//    public static final String[] AGE15 = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
//    public static final String[] DISTANCE7 = {"0", "1", "2", "3", "4", "5", "6", "7"};
//    public static final String[] BITES7 = {"0", "1", "2", "3", "4", "5", "6", "7"};
//    public static final String[] SHAPE5 = {"straight", "inner_right", "inner_left", "outer_right", "outer_left"};
//    public static final String[] AXIS = {"x", "y", "z"};
//    public static final String[] FACINGNE = {"north", "east"};
//    public static final String[] RAILSHAPE = {"north_south", "east_west", "ascending_east", "ascending_west", "ascending_north", "ascending_south", "south_east", "south_west", "north_west", "north_east"};
//
//
//    public static Identifier createBlockIdentifier(String texturePath) {
//        return Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath);
//    }
//
//    /**
//     * Handles variant registration for blocks with multiple states
//     */
//    protected static <T extends Comparable<T>> void registerVariants(
//            BlockStateModelGenerator generator,
//            Block block,
//            Property<T> property,
//            Function<T, List<BlockStateVariant>> variantProvider) {
//
//        BlockStateVariantMap.SingleProperty<T> variantMap = BlockStateVariantMap.create(property);
//
//        // Register variants for each state value
//        for (T value : property.getValues()) {
//            List<BlockStateVariant> variants = variantProvider.apply(value);
//            if (!variants.isEmpty()) {
//                if (variants.size() == 1) {
//                    variantMap.register(value, variants.getFirst());
//                } else {
//                    variantMap.register(value, variants);
//                }
//            }
//        }
//
//        // Create block state with variant map
//        generator.blockStateCollector.accept(
//                VariantsBlockStateSupplier.create(block).coordinate(variantMap)
//        );
//    }
//
//
//    /**
//     * Helper method to create a block state variant with optional weight and rotation
//     */
//    protected static BlockStateVariant createVariant(Identifier modelPath, Integer weight, Integer rotationIndex) {
//        BlockStateVariant variant = BlockStateVariant.create()
//                .put(VariantSettings.MODEL, modelPath);
//
//        if (weight != null && weight > 0) {
//            variant.put(VariantSettings.WEIGHT, weight);
//        }
//
//        if (rotationIndex != null && rotationIndex > 0) {
//            variant.put(VariantSettings.Y, VariantSettings.Rotation.values()[rotationIndex]);
//        }
//
//        return variant;
//    }
//
//
//    /**
//     * used in model generation to skip transparent textures when mapping faces, ensuring rendering of partially transparent blocks
//     */
//    protected static boolean isTransparentTexture(String texturePath) {
//        if (texturePath == null) return false;
//        return texturePath.contains("transparent");
//    }
//
//    /**
//     * Creates a BlockStateSupplier for a block using all its properties.
//     *
//     * @param block   The block to create the state supplier for
//     * @param modelId The base model identifier to use
//     * @return The created BlockStateSupplier
//     */
//
//    public static VariantsBlockStateSupplier createVariantBlockStateSupplier(@NotNull Block block, Identifier modelId, List<BlockStateVariant> variants) {
//        StateManager<Block, BlockState> stateManager = block.getStateManager();
//        Collection<Property<?>> properties = stateManager.getProperties();
//
//        if (properties.isEmpty()) {
//            return VariantsBlockStateSupplier.create(block,
//                    BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
//        }
//
//        if (variants != null) {
//            for (BlockState state : block.getStateManager().getStates()) {
//                variants.add(createVariantForState(state, modelId));
//            }
//
//        }
//
//        return VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]));
//    }
//
//    /**
//     * Handles variant registration for blocks with multiple states
//     */
//    public static BlockStateVariantMap createBlockStateVariantMap(Collection<Property<?>> properties) {
//        int size = properties.size();
//        if (size == 0) {
//            throw new IllegalArgumentException("Must provide at least one property");
//        }
//
//        Property<?>[] props = properties.toArray(new Property<?>[0]);
//        return switch (size) {
//            case 1 -> BlockStateVariantMap.SingleProperty.create(props[0]);
//            case 2 -> BlockStateVariantMap.DoubleProperty.create(props[0], props[1]);
//            case 3 -> BlockStateVariantMap.TripleProperty.create(props[0], props[1], props[2]);
//            case 4 -> BlockStateVariantMap.QuadrupleProperty.create(props[0], props[1], props[2], props[3]);
//            case 5 -> BlockStateVariantMap.QuintupleProperty.create(props[0], props[1], props[2], props[3], props[4]);
//            default -> throw new IllegalArgumentException("Too many properties for block: " + size);
//        };
//    }
//
//    private static BlockStateVariant createVariantForState(BlockState state, Identifier baseModelId) {
//        BlockStateVariant variant = BlockStateVariant.create()
//                .put(VariantSettings.MODEL, baseModelId);
//
//        // Handle each property
//        state.getProperties().forEach(property -> {
//            Comparable<?> value = state.get(property);
//            WesterosBlocks.LOGGER.info("PROPERTY: {} : {}", property, value);
//
//            // Handle direction properties
//            if (value instanceof Direction direction) {
//                if (direction.getAxis().isHorizontal()) {
//                    variant.put(VariantSettings.Y, getRotationForDirection(direction));
//                } else {
//                    variant.put(VariantSettings.X, direction == Direction.DOWN ?
//                            VariantSettings.Rotation.R180 : VariantSettings.Rotation.R0);
//                }
//            }
//            // Handle axis properties
////            else if (value instanceof Direction.Axis axis) {
////                if (axis != Direction.Axis.Y) {
////                    variant.put(VariantSettings.X, VariantSettings.Rotation.R90);
////                    if (axis == Direction.Axis.X) {
////                        variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
////                    }
////                }
////            }
//        });
//
//        return variant;
//    }
//
//    private static VariantSettings.Rotation getRotationForDirection(Direction dir) {
//        return switch (dir) {
//            case NORTH -> VariantSettings.Rotation.R0;
//            case EAST -> VariantSettings.Rotation.R90;
//            case SOUTH -> VariantSettings.Rotation.R180;
//            case WEST -> VariantSettings.Rotation.R270;
//            default -> VariantSettings.Rotation.R0;
//        };
//    }
//
//
//    public static class StateObject {
//        private final BlockStateModelGenerator generator;
//        private final Block block;
//        private VariantsBlockStateSupplier variantSupplier;
//        private MultipartBlockStateSupplier multipartSupplier;
//
//        public StateObject(BlockStateModelGenerator generator, Block block) {
//            this.generator = generator;
//            this.block = block;
//        }
//
//        public void addVariant(String condition, BlockStateVariant variant, Set<String> stateIDs) {
//            if (variantSupplier == null) {
//                variantSupplier = VariantsBlockStateSupplier.create(block);
//            }
//
//            BlockStateVariant customVariant = BlockStateVariant.create()
//                    .put(VariantSettings.MODEL, Identifier.of(variant.model)); // Use direct Identifier instead of creating with minecraft namespace
//
//            if (variant.x != null)
//                customVariant.put(VariantSettings.X, VariantSettings.Rotation.values()[variant.x / 90]);
//            if (variant.y != null)
//                customVariant.put(VariantSettings.Y, VariantSettings.Rotation.values()[variant.y / 90]);
//            if (variant.weight != null)
//                customVariant.put(VariantSettings.WEIGHT, variant.weight);
//            if (variant.uvlock != null)
//                customVariant.put(VariantSettings.UVLOCK, variant.uvlock);
//
//            if (stateIDs == null) {
//                if (condition.isEmpty()) {
//                    variantSupplier = VariantsBlockStateSupplier.create(block, customVariant);
//                } else {
//                    String[] parts = condition.split(",");
//                    BlockStateVariantMap variantMap = createVariantMap(parts, customVariant);
//                    variantSupplier.coordinate(variantMap);
//                }
//            } else {
//                for (String stateId : stateIDs) {
//                    String fullCondition = condition + (condition.isEmpty() ? "" : ",") + "state=" + stateId;
//                    String[] parts = fullCondition.split(",");
//                    BlockStateVariantMap variantMap = createVariantMap(parts, customVariant);
//                    variantSupplier.coordinate(variantMap);
//                }
//            }
//        }
//
//        public void addMultipartEntry(MultipartEntry entry, Set<String> stateIDs) {
//            if (multipartSupplier == null) {
//                multipartSupplier = MultipartBlockStateSupplier.create(block);
//            }
//
//            if (stateIDs == null) {
//                addMultipartVariants(entry, null);
//            } else {
//                for (String stateId : stateIDs) {
//                    addMultipartVariants(entry, stateId);
//                }
//            }
//        }
//
//        private void addMultipartVariants(MultipartEntry entry, String stateId) {
//            When when = null;
//            if (entry.getCondition() != null) {
//                when = entry.getCondition().createWhen();
//                if (stateId != null) {
//                    // Find the corresponding BlockState enum value
//                    try {
//                        Condition.BlockState blockState = Condition.BlockState.valueOf(stateId.toUpperCase());
//                        when = When.create().set(Condition.STATE, blockState);
//                    } catch (IllegalArgumentException e) {
//                        WesterosBlocks.LOGGER.error("Invalid state ID: {}", stateId);
//                    }
//                }
//            }
//
//            for (Variant variant : entry.getVariants()) {
//                BlockStateVariant fabricVariant = BlockStateVariant.create()
//                        .put(VariantSettings.MODEL, Identifier.of("minecraft", variant.model));
//
//                if (variant.x != null)
//                    fabricVariant.put(VariantSettings.X, VariantSettings.Rotation.values()[variant.x / 90]);
//                if (variant.y != null)
//                    fabricVariant.put(VariantSettings.Y, VariantSettings.Rotation.values()[variant.y / 90]);
//                if (variant.uvlock != null) fabricVariant.put(VariantSettings.UVLOCK, variant.uvlock);
//                if (variant.weight != null) fabricVariant.put(VariantSettings.WEIGHT, variant.weight);
//
//                if (when != null) {
//                    multipartSupplier.with(when, fabricVariant);
//                } else {
//                    multipartSupplier.with(fabricVariant);
//                }
//            }
//        }
//
//        private BlockStateVariantMap createVariantMap(String[] conditions, BlockStateVariant variant) {
//            List<Property<?>> properties = new ArrayList<>();
//            List<String> values = new ArrayList<>();
//
//            // Collect the properties and values
//            for (String condition : conditions) {
//                String[] keyValue = condition.split("=");
//                if (keyValue.length == 2) {
//                    Property<?> property = findProperty(keyValue[0]);
//                    if (property != null) {
//                        properties.add(property);
//                        values.add(keyValue[1]);
//                    }
//                }
//            }
//
//            // Create appropriate BlockStateVariantMap based on number of properties
//            int size = properties.size();
//            if (size == 0) {
//                throw new IllegalArgumentException("Must provide at least one property");
//            }
//
//            Property<?>[] props = properties.toArray(new Property<?>[0]);
//            return switch (size) {
//                case 1 -> BlockStateVariantMap.create(props[0])
//                        .register(value -> variant);
//                case 2 -> BlockStateVariantMap.create(props[0], props[1])
//                        .register((v1, v2) -> variant);
//                case 3 -> BlockStateVariantMap.create(props[0], props[1], props[2])
//                        .register((v1, v2, v3) -> variant);
//                case 4 -> BlockStateVariantMap.create(props[0], props[1], props[2], props[3])
//                        .register((v1, v2, v3, v4) -> variant);
//                case 5 -> BlockStateVariantMap.create(props[0], props[1], props[2], props[3], props[4])
//                        .register((v1, v2, v3, v4, v5) -> variant);
//                default -> throw new IllegalArgumentException("Too many properties for block state: " + size);
//            };
//        }
//
//        private Property<?> findProperty(String name) {
//            return block.getStateManager().getProperties().stream()
//                    .filter(p -> p.getName().equals(name))
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        public void generate() {
//            if (variantSupplier != null) {
//                generator.blockStateCollector.accept(variantSupplier);
//            }
//            if (multipartSupplier != null) {
//                generator.blockStateCollector.accept(multipartSupplier);
//            }
//        }
//    }
//
//    /**
//     * Represents a multipart block state definition with conditions and variants
//     */
//    public static class MultipartEntry {
//        private final List<Variant> variants;
//        private final Condition condition;
//
//        private MultipartEntry(Builder builder) {
//            this.variants = new ArrayList<>(builder.variants);
//            this.condition = builder.condition;
//        }
//
//        public List<Variant> getVariants() {
//            return variants;
//        }
//
//        public Condition getCondition() {
//            return condition;
//        }
//
//        public static Builder builder() {
//            return new Builder();
//        }
//
//        public static class Builder {
//            private final List<Variant> variants = new ArrayList<>();
//            private Condition condition;
//
//            public Builder variant(Variant variant) {
//                this.variants.add(variant);
//                return this;
//            }
//
//            public Builder variants(List<Variant> variants) {
//                this.variants.addAll(variants);
//                return this;
//            }
//
//            public Builder condition(Condition condition) {
//                this.condition = condition;
//                return this;
//            }
//
//            public MultipartEntry build() {
//                return new MultipartEntry(this);
//            }
//        }
//    }
//
//    /**
//     * Represents conditions for when a multipart variant should be applied
//     */
//    public static class Condition {
//        public enum BlockState implements StringIdentifiable {
//            BASE("base"),
//            STATE0("state0"),
//            STATE1("state1"),
//            STATE2("state2");
//
//            private final String name;
//
//            BlockState(String name) {
//                this.name = name;
//            }
//
//            @Override
//            public String asString() {
//                return this.name;
//            }
//        }
//
//        public static final EnumProperty<BlockState> STATE = EnumProperty.of("state", BlockState.class);
//
//        private final Boolean north;
//        private final Boolean south;
//        private final Boolean east;
//        private final Boolean west;
//        private final Boolean up;
//        private final Boolean down;
//        private final BlockState stateValue;
//
//        private Condition(Builder builder) {
//            this.north = builder.north;
//            this.south = builder.south;
//            this.east = builder.east;
//            this.west = builder.west;
//            this.up = builder.up;
//            this.down = builder.down;
//            this.stateValue = builder.stateValue;
//        }
//
//        public When createWhen() {
//            When.PropertyCondition when = When.create();
//
//            if (north != null) when.set(Properties.NORTH, north);
//            if (south != null) when.set(Properties.SOUTH, south);
//            if (east != null) when.set(Properties.EAST, east);
//            if (west != null) when.set(Properties.WEST, west);
//            if (up != null) when.set(Properties.UP, up);
//            if (down != null) when.set(Properties.DOWN, down);
//            if (stateValue != null) {
//                when.set(STATE, stateValue);
//            }
//
//            return when;
//        }
//
//        public static Builder builder() {
//            return new Builder();
//        }
//
//        public static class Builder {
//            private Boolean north;
//            private Boolean south;
//            private Boolean east;
//            private Boolean west;
//            private Boolean up;
//            private Boolean down;
//            private BlockState stateValue;
//
//            public Builder north(boolean north) {
//                this.north = north;
//                return this;
//            }
//
//            public Builder south(boolean south) {
//                this.south = south;
//                return this;
//            }
//
//            public Builder east(boolean east) {
//                this.east = east;
//                return this;
//            }
//
//            public Builder west(boolean west) {
//                this.west = west;
//                return this;
//            }
//
//            public Builder up(boolean up) {
//                this.up = up;
//                return this;
//            }
//
//            public Builder down(boolean down) {
//                this.down = down;
//                return this;
//            }
//
//            public Builder state(String value) {
//                this.stateValue = BlockState.valueOf(value.toUpperCase());
//                return this;
//            }
//
//            public Condition build() {
//                return new Condition(this);
//            }
//        }
//    }
//
//    /**
//     * Represents a block state variant with model and transformation properties
//     */
//    public static class Variant {
//        public final String model;
//        public final Integer x;
//        public final Integer y;
//        public final Integer z;
//        public final Integer weight;
//        public final Boolean uvlock;
//
//        private Variant(Builder builder) {
//            this.model = builder.model;
//            this.x = builder.x;
//            this.y = builder.y;
//            this.z = builder.z;
//            this.weight = builder.weight;
//            this.uvlock = builder.uvlock;
//        }
//
//        public static Builder builder() {
//            return new Builder();
//        }
//
//        public static class Builder {
//            private String model;
//            private Integer x;
//            private Integer y;
//            private Integer z;
//            private Integer weight;
//            private Boolean uvlock;
//
//            public Builder model(String model) {
//                this.model = model;
//                return this;
//            }
//
//            public Builder x(Integer x) {
//                this.x = x;
//                return this;
//            }
//
//            public Builder y(Integer y) {
//                this.y = y;
//                return this;
//            }
//
//            public Builder z(Integer z) {
//                this.z = z;
//                return this;
//            }
//
//            public Builder weight(Integer weight) {
//                if (weight != null) {
//                    this.weight = weight;
//                }
//                return this;
//            }
//
//            public Builder uvlock(Boolean uvlock) {
//                if (uvlock != null) {
//                    this.uvlock = uvlock;
//                }
//                return this;
//            }
//
//            public Variant build() {
//                if (model == null) {
//                    throw new IllegalStateException("Model must be specified");
//                }
//                return new Variant(this);
//            }
//        }
//
//        // Example usage:
//        public static Variant createRotated(String model, int y) {
//            return builder()
//                    .model(model)
//                    .y(y)
//                    .build();
//        }
//
//        public static Variant createWeighted(String model, int weight) {
//            return builder()
//                    .model(model)
//                    .weight(weight)
//                    .build();
//        }
//
//        public static Variant create(String model) {
//            return builder()
//                    .model(model)
//                    .build();
//        }
//    }
//
//
//}