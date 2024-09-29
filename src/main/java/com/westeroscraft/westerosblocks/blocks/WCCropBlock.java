package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Block;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCCropBlock extends WCPlantBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}        	
            String t = def.getType();
            if ((t != null) && (t.indexOf(WesterosBlockDef.LAYER_SENSITIVE) >= 0)) {
            	tempLAYERS = BlockStateProperties.LAYERS;
            }
        	BlockBehaviour.Properties props = def.makeProperties().noCollission().instabreak();
            Block blk = new WCCropBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, false, false);
        }
    }

    protected WCCropBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def);
    }
    private static String[] TAGS = { "crops" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
