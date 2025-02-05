package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoulSandBlock;

public class WCSoulSandBlock extends SoulSandBlock implements ModBlockLifecycle {
    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCSoulSandBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), true, false);
        }
    }

    private ModBlock def;

    protected WCSoulSandBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
    }
    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    private static String[] TAGS = { "soul_speed_blocks" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
