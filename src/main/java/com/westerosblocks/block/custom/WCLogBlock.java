package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;

public class WCLogBlock extends PillarBlock implements ModBlockLifecycle {
    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();

            Block blk = new WCLogBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    protected ModBlock def;

    protected WCLogBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    private static final String[] TAGS = {"logs"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
