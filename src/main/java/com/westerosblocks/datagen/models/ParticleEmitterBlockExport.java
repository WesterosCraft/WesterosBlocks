package com.westerosblocks.datagen.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                generateParticleBlockModel(generator, set, setIdx);
            }
        }

        // Add variant for the base model
        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);
            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = getModelId("base", setIdx);
            variant.put(VariantSettings.MODEL, modelId);

            if (set.weight != null) {
                variant.put(VariantSettings.WEIGHT, set.weight);
            }

            blockStateBuilder.addVariant("", variant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateParticleBlockModel(BlockStateModelGenerator generator, ModBlock.RandomTextureSet set, int setIdx) {
        // Create a texture map for the particle block
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(0)));

        // Get the model ID for the base variant
        Identifier modelId = getModelId("base", setIdx);
        String modelJson = """
            {
                "parent": "block/block",
                "textures": {
                    "particle": "#particle",
                    "all": "#all"
                },
                "elements": [
                    {   
                        "from": [ 4, 4, 4 ],
                        "to": [ 12, 12, 12 ],
                        "faces": {
                            "down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#all" },
                            "up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#all" },
                            "north": { "uv": [ 0, 0, 16, 16 ], "texture": "#all" },
                            "south": { "uv": [ 0, 0, 16, 16 ], "texture": "#all" },
                            "west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#all" },
                            "east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#all" }
                        }
                    }
                ]
            }
            """;


        // Create a new model with a smaller cube shape and translucent textures
        Model model = new Model(
                Optional.of(Identifier.ofVanilla("block/cube_all")),
                Optional.of(modelJson),
                TextureKey.ALL,
                TextureKey.PARTICLE
        );

        // Upload the model
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String variant, int setIdx) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d", 
            GENERATED_PATH,
            def.getBlockName(),
            variant,
            setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        // For the item model, we'll use the basic block model
        TextureMap textureMap = TextureMap.layer0(Identifier.of(WesterosBlocks.MOD_ID, "item/" + blockDefinition.particle));

        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            textureMap,
            itemModelGenerator.writer
        );
    }
}