package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CakeBlock;

public class WCCakeBlock extends CakeBlock implements ModBlockLifecycle {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
        	def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCCakeBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    private ModBlock def;

    protected WCCakeBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
    }
    @Override
    public ModBlock getWBDefinition() {
        return def;
    }
    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
