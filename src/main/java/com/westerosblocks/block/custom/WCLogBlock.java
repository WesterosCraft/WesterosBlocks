package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;

public class WCLogBlock extends PillarBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties();

            Block blk = new WCLogBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }

    protected WesterosBlockDef def;

    protected WCLogBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static String[] TAGS = {"logs"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
