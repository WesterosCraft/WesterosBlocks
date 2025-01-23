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
    private static WCBedBlock bedBlock;

    public BedBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        bedBlock = (WCBedBlock) block;
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
        Model model = ModModels.BED_PART(parentPath);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private String getBedParentPath(boolean isHead) {
        String bedType = switch (bedBlock.bedType) {
            case RAISED -> "bed_raised";
            case HAMMOCK -> "bed_hammock";
            default -> "bed";
        };
        return "block/untinted/" + bedType + (isHead ? "_head" : "_foot");
    }

    private static Identifier modelFileName(String blockName, ModelRec rec) {
        return WesterosBlocks.id(String.format("%s%s/%s", GENERATED_PATH, blockName, rec.ext));
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef def, boolean head) {
        return ModTextureMap.bed(def, head);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        TextureMap textureMap = ModTextureMap.bed2(blockDefinition);
        Model bedItemModel = ModModels.BED_ITEM("untinted/bed_item");

        bedItemModel.upload(
                ModelIds.getItemModelId(currentBlock.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}