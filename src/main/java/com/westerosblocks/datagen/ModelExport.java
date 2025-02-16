package com.westerosblocks.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class ModelExport {
    public static final String GENERATED_PATH = "block/generated/";
    public static final String CUSTOM_PATH = "block/custom/";
    protected final Block block;
    public static ModBlock def;
    protected static BlockStateModelGenerator generator;
    protected static ModBlock.DisplayProperties displayProperties;

    public ModelExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        this.block = block;
        ModelExport.def = def;
        ModelExport.displayProperties = def.display;
        ModelExport.generator = generator;
    }

    public static Identifier createBlockIdentifier(String texturePath) {
        // If the texture path includes a namespace
        if (texturePath != null && texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            // If it's not a minecraft texture, prepend block/
            if (!namespace.equals("minecraft")) {
                path = "block/" + path;
            }
            return Identifier.of(namespace, path);
        }
        // No namespace, use mod ID and prepend block/
        return WesterosBlocks.id("block/" + texturePath);
    }

    public static VariantSettings.Rotation getRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }

    public static class BlockStateBuilder {
        private final MultipartBlockStateSupplier multipartSupplier;
        private final Map<String, List<BlockStateVariant>> variants;

        public BlockStateBuilder(Block block) {
            this.variants = new HashMap<>();
            this.multipartSupplier = MultipartBlockStateSupplier.create(block);
        }

        public void addVariant(String condition, BlockStateVariant variant, Set<String> stateIDs, Map<String, List<BlockStateVariant>> variants) {
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

        public MultipartBlockStateSupplier getMultipartSupplier() {
            return multipartSupplier;
        }

        public Map<String, List<BlockStateVariant>> getVariants() {
            return variants;
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

    protected static class VariantBuilder {
        public static BlockStateVariant create(Identifier modelId, ModBlock.RandomTextureSet set, Integer yRotation, Integer xRotation, Boolean uvLock) {
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, modelId);

            if (set != null && set.weight != null) {
                variant.put(VariantSettings.WEIGHT, set.weight);
            }

            if (yRotation != null && yRotation != 0) {
                variant.put(VariantSettings.Y, getRotation(yRotation));
            }

            if (xRotation != null && xRotation != 0) {
                variant.put(VariantSettings.X, getRotation(xRotation));
            }

            if (uvLock != null) {
                variant.put(VariantSettings.UVLOCK, uvLock);
            }

            return variant;
        }

        public static BlockStateVariant create(Identifier modelId, ModBlock.RandomTextureSet set) {
            return create(modelId, set, null, null, null);
        }

        public static BlockStateVariant createWithRotation(Identifier modelId, ModBlock.RandomTextureSet set, int yRotation) {
            return create(modelId, set, yRotation, null, null);
        }
    }

    protected static String getModelName(String ext, int setidx) {
        return def.blockName + "/" + ext + ("_v" + (setidx + 1));
    }

    public void generateBlockStateModels() {
    }

    protected static Identifier getBaseModelId(String variant, int setIdx, boolean isCustom, String blockName) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                isCustom ? CUSTOM_PATH : GENERATED_PATH,
                blockName,
                variant,
                setIdx + 1));
    }

    // Overload for when blockName = def.blockName
    protected Identifier getBaseModelId(String variant, int setIdx, boolean isCustom) {
        return getBaseModelId(variant, setIdx, isCustom, def.getBlockName());
    }

    protected static void generateBasicItemModel(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        if (blockDefinition == null || blockDefinition.states.isEmpty()) {
            return;
        }

        ModBlock.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        if (firstSet == null) {
            // Try getting from first state if direct access fails
            ModBlockStateRecord firstState = blockDefinition.states.getFirst();
            if (firstState != null) {
                firstSet = firstState.getRandomTextureSet(0);
            }
        }

        if (firstSet == null) {
            throw new IllegalStateException("No texture set found for block: " + block);
        }

        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }

    // For blocks that use their block model as item model
    protected static void generateBlockBasedItemModel(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition, String variant) {
        if (blockDefinition == null || blockDefinition.states.isEmpty()) {
            throw new IllegalStateException("No block definition or states found for block: " + block);
        }
        ModBlockStateRecord firstState = blockDefinition.states.getFirst();

        if (firstState == null) {
            throw new IllegalStateException("First state is null for block: " + block);
        }

        // For blocks with states, use the first state's ID instead of the provided variant
        String variantToUse = firstState.stateID != null ? firstState.stateID : variant;

        // Only add /base path for multi-state blocks WITHOUT explicit stateIDs
        String basePath = blockDefinition.states.size() > 1 && firstState.stateID == null ? "/base" : "";

        String path = String.format("%s%s%s/%s_v1",
                firstState.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH,
                blockDefinition.getBlockName(),
                basePath,
                variantToUse);

        itemModelGenerator.register(block.asItem(), new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty())
        );
    }

    // Overload for default "base" variant
    protected static void generateBlockBasedItemModel(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        generateBlockBasedItemModel(itemModelGenerator, block, blockDefinition, "base");
    }
}
