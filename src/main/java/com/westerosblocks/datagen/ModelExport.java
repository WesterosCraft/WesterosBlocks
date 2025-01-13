package com.westerosblocks.datagen;

import com.google.common.collect.Maps;
import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import org.spongepowered.include.com.google.common.collect.Lists;
import java.util.*;

public class ModelExport {
    public static final String GENERATED_PATH = "block/generated/";

    public static Identifier createBlockIdentifier(String texturePath) {
        return Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath);
    }

    public static VariantSettings.Rotation getRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }

    /**
     * Represents a block state definition that can contain either variants or multipart states.
     */
    public static class StateObject {
        private final Map<String, List<BlockStateVariant>> variants;
        private final List<States> multipart;
        private static Block block = null;

        public StateObject(Block block) {
            this.variants = Maps.newHashMap();
            this.multipart = Lists.newArrayList();
            this.block = block;
        }

        /**
         * Adds a variant to the state object with an optional condition.
         */
        public void addVariant(String condition, BlockStateVariant variant, Set<String> stateIDs) {
            if (stateIDs == null) {
                addSingleVariant(condition, variant);
            } else {
                for (String stateVal : stateIDs) {
                    String fullCondition = condition + ((!condition.isEmpty()) ? "," : "") + "state=" + stateVal;
                    addSingleVariant(fullCondition, variant);
                }
            }
        }

        private void addSingleVariant(String condition, BlockStateVariant variant) {
            List<BlockStateVariant> variantList = variants.computeIfAbsent(condition, k -> Lists.newArrayList());
            variantList.add(variant);
        }

        /**
         * Adds multipart states to the state object.
         */
        public void addStates(States states, Set<String> stateIDs) {
            if (stateIDs == null) {
                multipart.add(states);
            } else {
                for (String stateVal : stateIDs) {
                    States newStates = new States(states, stateVal);
                    multipart.add(newStates);
                }
            }
        }

        /**
         * States class for multipart state definitions
         */
        public static class States {
            public final List<Apply> apply;
            public final When when;

            public States() {
                this.apply = Lists.newArrayList();
                this.when = null;
            }

            public States(States original, String condition) {
                this.apply = original.apply;
                if (original.when != null) {
                    this.when = parseCondition(condition);
                } else {
                    this.when = parseCondition(condition);
                }
            }
        }

        /**
         * Apply class for defining model applications in multipart states
         */
        public static class Apply {
            public final BlockStateVariant variant;

            public Apply(BlockStateVariant variant) {
                this.variant = variant;
            }
        }

        /**
         * Helper class for parsing property conditions
         */
        private static When.PropertyCondition parseCondition(String condition) {
            When.PropertyCondition propertyCondition = When.create();
            if (!condition.isEmpty()) {
                String[] parts = condition.split(",");
                for (String part : parts) {
                    String[] keyValue = part.split("=");
                    if (keyValue.length == 2) {
                        String propertyName = keyValue[0].trim();
                        String value = keyValue[1].trim();

                        // Handle common property types
                        switch (propertyName) {
//                            case "facing":
//                                propertyCondition.set(Properties.HORIZONTAL_FACING, value);
//                                break;
                            case "symmetrical":
                                BooleanProperty SYMMETRICAL = (BooleanProperty) block.getStateManager().getProperty("symmetrical");
                                propertyCondition.set(SYMMETRICAL, value.equals("true"));
                                break;
                            case "state":
                                // Handle custom state properties if needed
                                break;
                        }
                    }
                }
            }
            return propertyCondition;
        }

        /**
         * Converts the state object to a BlockStateSupplier
         */
        public BlockStateSupplier toBlockStateSupplier(Block block) {
            if (!multipart.isEmpty()) {
                MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);
                for (States state : multipart) {
                    if (state.when != null) {
                        supplier.with(state.when, state.apply.stream()
                                .map(apply -> apply.variant)
                                .toList());
                    } else {
                        supplier.with(state.apply.stream()
                                .map(apply -> apply.variant)
                                .toList());
                    }
                }
                return supplier;
            } else {
                if (variants.size() == 1 && variants.containsKey("")) {
                    // Single variant case
                    List<BlockStateVariant> variantList = variants.get("");
                    if (variantList.size() == 1) {
                        return VariantsBlockStateSupplier.create(block, variantList.get(0));
                    } else {
                        return VariantsBlockStateSupplier.create(block, variantList.toArray(new BlockStateVariant[0]));
                    }
                } else {
                    // Multiple variants case
                    VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block);
                    // We need to create appropriate BlockStateVariantMaps for each property
                    // This is a simplified example - you'll need to expand based on your needs
                    for (Map.Entry<String, List<BlockStateVariant>> entry : variants.entrySet()) {
                        if (entry.getValue().size() == 1) {
                            supplier.coordinate(createVariantMap(parseCondition(entry.getKey()), entry.getValue().get(0)));
                        } else {
                            supplier.coordinate(createVariantMap(parseCondition(entry.getKey()), entry.getValue()));
                        }
                    }
                    return supplier;
                }
            }
        }

        private BlockStateVariantMap createVariantMap(When.PropertyCondition condition, BlockStateVariant variant) {
            // This is a simplified implementation - you'll need to expand based on your needs
            return BlockStateVariantMap.create(Properties.FACING)
                    .register(direction -> variant);
        }

        private BlockStateVariantMap createVariantMap(When.PropertyCondition condition, List<BlockStateVariant> variants) {
            // This is a simplified implementation - you'll need to expand based on your needs
            return BlockStateVariantMap.create(Properties.FACING)
                    .register(direction -> variants.getFirst());
        }
    }
}
