package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.custom.WCBedBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BedBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private static WCBedBlock bblk;

    public BedBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        bblk = (WCBedBlock) block;
    }

    private record ModelRec(String cond, String ext, int y) {
    }

    private static final ModelRec[] MODELS = {
            new ModelRec("facing=north,part=foot", "foot", 180),
            new ModelRec("facing=east,part=foot", "foot", 270),
            new ModelRec("facing=south,part=foot", "foot", 0),
            new ModelRec("facing=west,part=foot", "foot", 90),
            new ModelRec("facing=north,part=head", "head", 180),
            new ModelRec("facing=east,part=head", "head", 270),
            new ModelRec("facing=south,part=head", "head", 0),
            new ModelRec("facing=west,part=head", "head", 90)
    };

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        if (!def.isCustomModel()) {
            generateBedModels(generator);
        }

        for (ModelRec rec : MODELS) {
            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = modelFileName(def.blockName, rec);
            variant.put(VariantSettings.MODEL, modelId);
            if (rec.y != 0) {
                variant.put(VariantSettings.Y, getRotation(rec.y));
            }
            blockStateBuilder.addVariant(rec.cond, variant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateBedModels(BlockStateModelGenerator generator) {
        // Generate head model
        TextureMap headTextureMap = createCustomTextureMap(def, true);
        Identifier headModelId = modelFileName(def.blockName, new ModelRec("", "head", 0));
        generateBedPartModel(generator, headModelId, headTextureMap, true);

        // Generate foot model
        TextureMap footTextureMap = createCustomTextureMap(def, false);
        Identifier footModelId = modelFileName(def.blockName, new ModelRec("", "foot", 0));
        generateBedPartModel(generator, footModelId, footTextureMap, false);
    }

    private void generateBedPartModel(BlockStateModelGenerator generator, Identifier modelId, TextureMap textureMap, boolean isHead) {
        String parentPath = getBedParentPath(isHead);
        Model model = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, parentPath)),
                Optional.empty(),
                ModTextureKey.BED_TOP,
                ModTextureKey.BED_END,
                ModTextureKey.BED_SIDE,
                TextureKey.PARTICLE
        );
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private String getBedParentPath(boolean isHead) {
        String bedType = switch (bblk.bedType) {
            case RAISED -> "bed_raised";
            case HAMMOCK -> "bed_hammock";
            default -> "bed";
        };
        return "block/untinted/" + bedType + (isHead ? "_head" : "_foot");
    }

    private static Identifier modelFileName(String blockName, ModelRec rec) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s", GENERATED_PATH, blockName, rec.ext));
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef def, boolean head) {
        return new TextureMap()
                .put(ModTextureKey.BED_TOP, createBlockIdentifier(def.getTextureByIndex(head ? 0 : 1)))
                .put(ModTextureKey.BED_END, createBlockIdentifier(def.getTextureByIndex(head ? 4 : 5)))
                .put(ModTextureKey.BED_SIDE, createBlockIdentifier(def.getTextureByIndex(head ? 2 : 3)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(def.getTextureByIndex(0)));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.BED_TOP, createBlockIdentifier(blockDefinition.getTextureByIndex(0)))
                .put(ModTextureKey.BED_END, createBlockIdentifier(blockDefinition.getTextureByIndex(4)))
                .put(ModTextureKey.BED_SIDE, createBlockIdentifier(blockDefinition.getTextureByIndex(2)))
                .put(ModTextureKey.BED_TOP2, createBlockIdentifier(blockDefinition.getTextureByIndex(1)))
                .put(ModTextureKey.BED_END2, createBlockIdentifier(blockDefinition.getTextureByIndex(5)))
                .put(ModTextureKey.BED_SIDE2, createBlockIdentifier(blockDefinition.getTextureByIndex(3)));

        // Create a custom model with the proper parent and textures
        Model bedItemModel = new Model(
                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "item/untinted/bed_item")),
                Optional.empty(),
                ModTextureKey.BED_TOP,
                ModTextureKey.BED_END,
                ModTextureKey.BED_SIDE,
                ModTextureKey.BED_TOP2,
                ModTextureKey.BED_END2,
                ModTextureKey.BED_SIDE2
        );

        bedItemModel.upload(
                ModelIds.getItemModelId(currentBlock.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}