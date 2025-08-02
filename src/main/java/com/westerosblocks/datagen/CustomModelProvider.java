package com.westerosblocks.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom model provider that can generate model files with custom textures and
 * parent models.
 */
public class CustomModelProvider extends FabricModelProvider {

    private final Map<Block, CustomBlockConfig> customBlocks = new HashMap<>();
    private final FabricDataOutput output;

    public CustomModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    /**
     * Register a custom block with custom texture
     * 
     * @param block The block to register
     * @return CustomBlockBuilder for configuration
     */
    public CustomBlockBuilder registerCustomBlock(Block block) {
        return new CustomBlockBuilder(block);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Register custom blocks
        for (Map.Entry<Block, CustomBlockConfig> entry : customBlocks.entrySet()) {
            Block block = entry.getKey();
            CustomBlockConfig config = entry.getValue();

            if (config.texturePath != null || config.textures != null) {
                // Generate custom model file when texture path or textures are specified
                generateCustomModelFile(block, config);
                // Register with standard method (will use our custom model file)
                blockStateModelGenerator.registerSimpleCubeAll(block);
            } else {
                // For blocks without custom texture, let the standard method handle it
                // This will generate the default model with the block name as texture
                blockStateModelGenerator.registerSimpleCubeAll(block);
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models can be handled here if needed
    }

    private void generateCustomModelFile(Block block, CustomBlockConfig config) {
        String blockId = getBlockId(block);
        Path modelPath = output.getPath().resolve("assets/westerosblocks/models/block/" + blockId + ".json");

        JsonObject modelJson = new JsonObject();
        modelJson.add("parent", new JsonPrimitive(config.parentModel));

        JsonObject textures = new JsonObject();

        if (config.textures != null) {
            // Handle multiple textures for different sides
            String[] sideTextures = expandTextures(config.textures);
            textures.add("down", new JsonPrimitive("westerosblocks:block/" + sideTextures[0]));
            textures.add("up", new JsonPrimitive("westerosblocks:block/" + sideTextures[1]));
            textures.add("north", new JsonPrimitive("westerosblocks:block/" + sideTextures[2]));
            textures.add("south", new JsonPrimitive("westerosblocks:block/" + sideTextures[3]));
            textures.add("east", new JsonPrimitive("westerosblocks:block/" + sideTextures[4]));
            textures.add("west", new JsonPrimitive("westerosblocks:block/" + sideTextures[5]));
        } else if (config.texturePath != null) {
            // Handle single texture for all sides
            textures.add(config.textureKey, new JsonPrimitive("westerosblocks:block/" + config.texturePath));
        }

        modelJson.add("textures", textures);

        // Create a pretty-printing Gson instance
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(modelJson);

        // Write the model file with pretty formatting
        try {
            modelPath.getParent().toFile().mkdirs();
            java.nio.file.Files.write(modelPath, prettyJson.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to write model file: " + modelPath, e);
        }
    }

    private String getBlockId(Block block) {
        // Extract block ID from the block's registry key
        String blockString = block.toString();
        // Remove the namespace and extract just the block name
        if (blockString.contains(":")) {
            return blockString.split(":")[1].replace("}", "");
        }
        return blockString.toLowerCase().replace("block{", "").replace("}", "");
    }

    /**
     * Expand textures array to exactly 6 textures
     * If less than 6 textures provided, the last texture fills remaining sides
     * 
     * @param textures Input textures array
     * @return Array of exactly 6 textures
     */
    private String[] expandTextures(String[] textures) {
        if (textures == null || textures.length == 0) {
            return new String[6];
        }

        String[] expanded = new String[6];
        for (int i = 0; i < 6; i++) {
            if (i < textures.length) {
                expanded[i] = textures[i];
            } else {
                // Use the last texture for remaining sides
                expanded[i] = textures[textures.length - 1];
            }
        }
        return expanded;
    }

    /**
     * Builder class for configuring custom block properties
     */
    public class CustomBlockBuilder {
        private final Block block;
        private String texturePath;
        private String[] textures;
        private String parentModel = "minecraft:block/cube_all";
        private String textureKey = "all";

        public CustomBlockBuilder(Block block) {
            this.block = block;
        }

        /**
         * Set the texture path for the block (single texture for all sides)
         * 
         * @param texturePath The texture path (e.g., "bark/birch/side")
         * @return this builder for chaining
         */
        public CustomBlockBuilder texture(String texturePath) {
            if (this.textures != null) {
                throw new IllegalStateException("Cannot use .texture() when .textures() has already been called");
            }
            this.texturePath = texturePath;
            return this;
        }

        /**
         * Set multiple textures for different sides of the block
         * Order: down, up, north, south, east, west
         * If less than 6 textures provided, the last texture fills remaining sides
         * 
         * @param textures Array of texture paths
         * @return this builder for chaining
         */
        public CustomBlockBuilder textures(String... textures) {
            if (this.texturePath != null) {
                throw new IllegalStateException("Cannot use .textures() when .texture() has already been called");
            }
            this.textures = textures;
            return this;
        }

        /**
         * Set a custom parent model
         * 
         * @param parentModel The parent model identifier (e.g.,
         *                    "minecraft:block/cube_all")
         * @return this builder for chaining
         */
        public CustomBlockBuilder parent(String parentModel) {
            this.parentModel = parentModel;
            return this;
        }

        /**
         * Set a custom texture key (default is "all")
         * 
         * @param textureKey The texture key to use
         * @return this builder for chaining
         */
        public CustomBlockBuilder textureKey(String textureKey) {
            this.textureKey = textureKey;
            return this;
        }

        /**
         * Build and register the custom block configuration
         * 
         * @return this builder for chaining
         */
        public CustomBlockBuilder build() {
            if (texturePath == null && textures == null) {
                throw new IllegalStateException("Must call either .texture() or .textures() before .build()");
            }
            CustomBlockConfig config = new CustomBlockConfig(texturePath, textures, parentModel, textureKey);
            customBlocks.put(block, config);
            return this;
        }
    }

    /**
     * Configuration class for custom block properties
     */
    private static class CustomBlockConfig {
        private final String texturePath;
        private final String[] textures;
        private final String parentModel;
        private final String textureKey;

        public CustomBlockConfig(String texturePath, String[] textures, String parentModel, String textureKey) {
            this.texturePath = texturePath;
            this.textures = textures;
            this.parentModel = parentModel;
            this.textureKey = textureKey;
        }
    }
}