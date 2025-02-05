package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.util.ColorCode;

public class WCSandBlock extends ColoredFallingBlock implements ModBlockLifecycle {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCSandBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), true, false);
        }
    }
    
    private ModBlock def;
    
    protected WCSandBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(new ColorCode(0xDBCAA0), settings);	// TODO: configurable dust color
        this.def = def;
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }
    
    private static String[] TAGS = { "sand" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
