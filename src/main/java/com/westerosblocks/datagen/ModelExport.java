package com.westerosblocks.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class ModelExport {
    public static final String GENERATED_PATH = "block/generated/";
    public static final String CUSTOM_PATH = "block/custom/";
    protected final Block block;
    protected static WesterosBlockDef def;
    protected static BlockStateModelGenerator generator;

    public ModelExport(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        this.block = block;
        this.def = def;
        this.generator = generator;
    }

    public static Identifier createBlockIdentifier(String texturePath) {
        // If the texture path includes a namespace
        if (texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            // If it's not a minecraft texture, prepend block/
            if (!namespace.equals("minecraft")) {
                path = "block/" + path;
            }
            return Identifier.of(namespace, path);
        }
        // No namespace, use mod ID and prepend block/
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

    public static void addVariant(String condition, BlockStateVariant variant, Set<String> stateIDs, Map<String, List<BlockStateVariant>> variants) {
        List<String> conditions = new ArrayList<>();

        if (stateIDs == null) {
            conditions.add(condition);
        } else {
            for (String stateVal : stateIDs) {
                String fullCondition = condition + ((!condition.isEmpty()) ? "," : "") + "state=" + stateVal;
                conditions.add(fullCondition);
            }
        }

        for (String conditionValue : conditions) {
            List<BlockStateVariant> existingVariants = variants.computeIfAbsent(conditionValue, k -> new ArrayList<>());
            existingVariants.add(variant);
        }
    }


    public static void generateBlockStateFiles(BlockStateModelGenerator generator, Block block, Map<String, List<BlockStateVariant>> variants) {
        if (variants.isEmpty()) {
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block));
            return;
        }

        if (variants.size() == 1 && variants.containsKey("")) {
            List<BlockStateVariant> variantList = variants.get("");
            if (variantList.size() == 1) {
                generator.blockStateCollector.accept(
                        VariantsBlockStateSupplier.create(block, variantList.getFirst())
                );
            } else {
                generator.blockStateCollector.accept(
                        VariantsBlockStateSupplier.create(block,
                                variantList.toArray(new BlockStateVariant[0]))
                );
            }
            return;
        }

        // Create custom BlockStateSupplier for multiple variants
        BlockStateSupplier supplier = new BlockStateSupplier() {
            @Override
            public JsonElement get() {
                JsonObject variantsJson = new JsonObject();

                for (Map.Entry<String, List<BlockStateVariant>> entry : variants.entrySet()) {
                    if (!entry.getValue().isEmpty()) {
                        variantsJson.add(entry.getKey(), BlockStateVariant.toJson(entry.getValue()));
                    }
                }

                JsonObject json = new JsonObject();
                json.add("variants", variantsJson);
                return json;
            }

            @Override
            public Block getBlock() {
                return block;
            }
        };

        generator.blockStateCollector.accept(supplier);
    }

    protected static String getModelName(String ext, int setidx) {
        return def.blockName + "/" + ext + ("_v" + (setidx+1));
    }

    protected static String getModelName(String ext, int setidx, String cond) {
        if (cond == null)
            return getModelName(ext, setidx);
        return def.blockName + "/" + cond + "/" + ext + ("_v" + (setidx+1));
    }
}
