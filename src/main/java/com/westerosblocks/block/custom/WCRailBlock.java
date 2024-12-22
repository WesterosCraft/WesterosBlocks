package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.util.math.BlockPos;

public class WCRailBlock extends RailBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties().noOcclusion().noCollision();

            Block blk = new WCRailBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean allow_unsupported = false;
    
    protected WCRailBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
            }
        }

    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }    

    @Override
    public boolean canSurvive(BlockState p_49395_, LevelReader p_49396_, BlockPos p_49397_) {
    	if (this.allow_unsupported) return true;
        return super.canSurvive(p_49395_, p_49396_, p_49397_);
     }

    @Override
    public void neighborChanged(BlockState p_49377_, Level p_49378_, BlockPos p_49379_, Block p_49380_, BlockPos p_49381_, boolean p_49382_) {
    	if (!this.allow_unsupported) { 
    		super.neighborChanged(p_49377_, p_49378_, p_49379_, p_49380_, p_49381_, p_49382_);
    	}
    	else if (!p_49378_.isClientSide && p_49378_.getBlockState(p_49379_).is(this)) {
        	this.updateState(p_49377_, p_49378_, p_49379_, p_49380_);
        }
    }

    private static String[] TAGS = { "rails" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
