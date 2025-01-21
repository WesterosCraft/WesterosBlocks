package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.FurnaceBlock;

public class WCFurnaceBlock extends FurnaceBlock implements WesterosBlockLifecycle {

    private static String[] TAGS = {};

    public WCFurnaceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return null;
    }

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
