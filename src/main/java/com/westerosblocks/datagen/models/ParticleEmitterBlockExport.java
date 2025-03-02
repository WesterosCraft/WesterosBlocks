package com.westerosblocks.datagen.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.datagen.ModelExport;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.util.Identifier;

public class ParticleEmitterBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public ParticleEmitterBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    @Override
    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                // Generate both on and off models with appropriate texture indices
                generateParticleBlockModel(generator, set, setIdx, false); // Off state - index 0
                generateParticleBlockModel(generator, set, setIdx, true);  // On state - index 1
            }
        }

        // Define the state variants for powered=false and powered=true
        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);

            // Off state variant
            BlockStateVariant offVariant = BlockStateVariant.create();
            Identifier offModelId = getModelId("off", setIdx);
            offVariant.put(VariantSettings.MODEL, offModelId);

            if (set.weight != null) {
                offVariant.put(VariantSettings.WEIGHT, set.weight);
            }

            // On state variant
            BlockStateVariant onVariant = BlockStateVariant.create();
            Identifier onModelId = getModelId("on", setIdx);
            onVariant.put(VariantSettings.MODEL, onModelId);

            if (set.weight != null) {
                onVariant.put(VariantSettings.WEIGHT, set.weight);
            }

            // Add variants with their conditions
            blockStateBuilder.addVariant("powered=false", offVariant, null, variants);
            blockStateBuilder.addVariant("powered=true", onVariant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateParticleBlockModel(BlockStateModelGenerator generator, ModBlock.RandomTextureSet set, int setIdx, boolean powered) {
        // Get the texture ID for the current state - index 0 for off, index 1 for on
        int textureIndex = powered ? 1 : 0;

        // Make sure we have enough textures in the set
        String textureId;
        if (textureIndex < set.getTextureCount()) {
            textureId = set.getTextureByIndex(textureIndex);
        } else {
            // Fallback to the first texture if the requested index doesn't exist
            textureId = set.getTextureByIndex(0);
        }

        Identifier textureIdentifier = createBlockIdentifier(textureId);

        // Create a custom model with our custom textures mapping
        Model model = new CustomParticleEmitterModel(powered, textureIdentifier.toString());

        // Get the model ID for the variant
        String variant = powered ? "on" : "off";
        Identifier modelId = getModelId(variant, setIdx);

        // Use a dummy TextureMap since our custom model handles textures
        TextureMap emptyMap = new TextureMap().put(TextureKey.PARTICLE, textureIdentifier);

        // Upload the model
        model.upload(modelId, emptyMap, generator.modelCollector);
    }

    private static class CustomParticleEmitterModel extends Model {
        private final boolean powered;
        private final String texture;

        public CustomParticleEmitterModel(boolean powered, String texture) {
            super(Optional.empty(), Optional.empty(), TextureKey.PARTICLE);
            this.powered = powered;
            this.texture = texture;
        }

        @Override
        public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
            // Get the base elements from the PARTICLE_EMITTER model
            JsonObject baseJson = ModModels.PARTICLE_EMITTER(powered)
                    .createJson(id, textures);

            // Add our custom textures section
            JsonObject texturesJson = new JsonObject();
            texturesJson.addProperty("0", texture);
            texturesJson.addProperty("particle", texture);
            baseJson.add("textures", texturesJson);

            return baseJson;
        }
    }

    private Identifier getModelId(String variant, int setIdx) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                GENERATED_PATH,
                def.getBlockName(),
                variant,
                setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        // For the item model, always use the "off" state texture (index 0)
        String texture;
        if (blockDefinition.getRandomTextureSetCount() > 0) {
            texture = blockDefinition.getRandomTextureSet(0).getTextureByIndex(0);
        } else {
            // Fallback to particle texture if no texture sets
            texture = blockDefinition.particle != null ? blockDefinition.particle : "transparent";
        }

        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texture));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}