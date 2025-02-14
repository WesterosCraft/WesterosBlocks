package com.westerosblocks.datagen;

import com.westerosblocks.block.ModBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;

public interface BlockExportFactory {
    ModelExport create(BlockStateModelGenerator generator, Block block, ModBlock def);
}
