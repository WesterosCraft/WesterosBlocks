package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class DoorBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static class ModelRec {
        String cond;
        String ext;
        int y;
        ModelRec(String c, String e, int y) {
            cond = c; ext = e; this.y = y;
        }
    };

//    bottom → door_bottom
//    bottom_rh → door_bottom_right
//    bottom_open → door_bottom_open
//    bottom_right_open → door_bottom_right_open
//    top → door_top
//    top_rh → door_top_right
//    top_open → door_top_open
//    top_right_open → door_top_right_open

    private static final ModelRec[] MODELS = {
            // EAST facing
            new ModelRec("facing=east,half=lower,hinge=left,open=false", "bottom_left", 0),
            new ModelRec("facing=east,half=lower,hinge=left,open=true", "bottom_left_open", 90),
            new ModelRec("facing=east,half=lower,hinge=right,open=false", "bottom_right", 0),
            new ModelRec("facing=east,half=lower,hinge=right,open=true", "bottom_right_open", 270),
            new ModelRec("facing=east,half=upper,hinge=left,open=false", "top_left", 0),
            new ModelRec("facing=east,half=upper,hinge=left,open=true", "top_left_open", 90),
            new ModelRec("facing=east,half=upper,hinge=right,open=false", "top_right", 0),
            new ModelRec("facing=east,half=upper,hinge=right,open=true", "top_right_open", 270),

            // SOUTH facing
            new ModelRec("facing=south,half=lower,hinge=left,open=false", "bottom_left", 90),
            new ModelRec("facing=south,half=lower,hinge=left,open=true", "bottom_left_open", 180),
            new ModelRec("facing=south,half=lower,hinge=right,open=false", "bottom_right", 90),
            new ModelRec("facing=south,half=lower,hinge=right,open=true", "bottom_right_open", 0),
            new ModelRec("facing=south,half=upper,hinge=left,open=false", "top_left", 90),
            new ModelRec("facing=south,half=upper,hinge=left,open=true", "top_left_open", 180),
            new ModelRec("facing=south,half=upper,hinge=right,open=false", "top_right", 90),
            new ModelRec("facing=south,half=upper,hinge=right,open=true", "top_right_open", 0),

            // WEST facing
            new ModelRec("facing=west,half=lower,hinge=left,open=false", "bottom_left", 180),
            new ModelRec("facing=west,half=lower,hinge=left,open=true", "bottom_left_open", 270),
            new ModelRec("facing=west,half=lower,hinge=right,open=false", "bottom_right", 180),
            new ModelRec("facing=west,half=lower,hinge=right,open=true", "bottom_right_open", 90),
            new ModelRec("facing=west,half=upper,hinge=left,open=false", "top_left", 180),
            new ModelRec("facing=west,half=upper,hinge=left,open=true", "top_left_open", 270),
            new ModelRec("facing=west,half=upper,hinge=right,open=false", "top_right", 180),
            new ModelRec("facing=west,half=upper,hinge=right,open=true", "top_right_open", 90),

            // NORTH facing
            new ModelRec("facing=north,half=lower,hinge=left,open=false", "bottom_left", 270),
            new ModelRec("facing=north,half=lower,hinge=left,open=true", "bottom_left_open", 0),
            new ModelRec("facing=north,half=lower,hinge=right,open=false", "bottom_right", 270),
            new ModelRec("facing=north,half=lower,hinge=right,open=true", "bottom_right_open", 180),
            new ModelRec("facing=north,half=upper,hinge=left,open=false", "top_left", 270),
            new ModelRec("facing=north,half=upper,hinge=left,open=true", "top_left_open", 0),
            new ModelRec("facing=north,half=upper,hinge=right,open=false", "top_right", 270),
            new ModelRec("facing=north,half=upper,hinge=right,open=true", "top_right_open", 180)
    };

    public DoorBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                generateDoorModels(generator, set);
            }
        }

        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModelRec rec : MODELS) {
            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = getModelId(rec.ext, def.isCustomModel());
            variant.put(VariantSettings.MODEL, modelId);

            if (rec.y != 0) {
                variant.put(VariantSettings.Y, getRotation(rec.y));
            }

            blockStateBuilder.addVariant(rec.cond, variant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateDoorModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.BOTTOM, createBlockIdentifier(set.getTextureByIndex(1)));

        // Each entry is {model extension, parent model} pairs
        String[][] variants = {
                {"bottom_left", "door_bottom_left"},
                {"bottom_right", "door_bottom_right"},
                {"bottom_left_open", "door_bottom_left_open"},
                {"bottom_right_open", "door_bottom_right_open"},
                {"top_left", "door_top_left"},
                {"top_right", "door_top_right"},
                {"top_left_open", "door_top_left_open"},
                {"top_right_open", "door_top_right_open"}
        };

        // Generate each door model variant
        for (String[] variant : variants) {
            String modelName = variant[0];
            String parentPath = "block/" + variant[1];

            Identifier modelId = getModelId(modelName, false);
            Model doorModel = new Model(
                    Optional.of(Identifier.ofVanilla(parentPath)),
                    Optional.empty(),
                    TextureKey.TOP,
                    TextureKey.BOTTOM
            );
            doorModel.upload(modelId, textureMap, generator.modelCollector);
        }
    }

    private Identifier getModelId(String variant, boolean isCustom) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v1",
                        isCustom ? CUSTOM_PATH : GENERATED_PATH,
                        def.getBlockName(),
                        variant));
    }

    // TODO the door model is ugly af i think we need item textures
    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
                                          Block block,
                                          WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}