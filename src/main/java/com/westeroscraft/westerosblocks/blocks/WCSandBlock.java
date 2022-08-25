package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SandBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCSandBlock extends SandBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSandBlock(props, def)), true, false);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCSandBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(14406560, props);	// TODO: configurable dust color
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
