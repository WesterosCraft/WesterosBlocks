package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CuboidNSEWStackBlockModelHandler extends CuboidBlockModelHandler {
    private static final String[] FACING = {"north", "east", "south", "west"};
    private static final int[] ROTATIONS = {270, 0, 90, 180}; // Match original rotation values
    protected final Block block;
    protected static WesterosBlockDef def;
    protected static BlockStateModelGenerator generator;

    public CuboidNSEWStackBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.block = block;
        this.def = def;
        this.generator = generator;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        generateBlockStateFiles(generator, block, variants);
    }


    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        String path = String.format("%s%s/base_v1", GENERATED_PATH, blockDefinition.getBlockName());
        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)),
                        Optional.empty())
        );

        if (blockDefinition.isTinted()) {
            String tintResource = blockDefinition.getBlockColorMapResource();
            if (tintResource != null) {
                // TODO: Handle tinting registration
            }
        }
    }
}