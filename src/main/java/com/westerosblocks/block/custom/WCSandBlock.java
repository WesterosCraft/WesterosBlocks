package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.util.ColorCode;

public class WCSandBlock extends ColoredFallingBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCSandBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), true, false);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCSandBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(new ColorCode(0xDBCAA0), settings);	// TODO: configurable dust color
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    private static String[] TAGS = { "sand" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
