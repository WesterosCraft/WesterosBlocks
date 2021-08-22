package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.StateContainer;

import org.dynmap.modsupport.CopyBlockTextureRecord;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;

public class WCStairBlock extends StairsBlock implements WesterosBlockDynmapSupport, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            if (def.modelBlockName == null) {
                WesterosBlocks.log.error("Type 'stair' requires modelBlockName settings");
                return null;
            }
            // Try to find model block
            Block blk = WesterosBlocks.findBlockByName(def.modelBlockName);
            if (blk == null) {
                WesterosBlocks.log.error(String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
                return null;
            }
            WesterosBlockDef mbdef = null;
            // If a WB, look up def
            if (blk instanceof WesterosBlockLifecycle) {
            	mbdef = ((WesterosBlockLifecycle) blk). getWBDefinition();
            	// See if we have a cond property
            	WesterosBlockDef.CondProperty prop = mbdef.buildCondProperty();
            	if (prop != null) {
            		tempCOND = prop;
            	}        	
            }
        	AbstractBlock.Properties props = def.makeAndCopyProperties(blk);
        	return def.registerRenderType(def.registerBlock(new WCStairBlock(blk.defaultBlockState(), props, def, mbdef)), false, false);
        }
    }
    
    private WesterosBlockDef def;
    private WesterosBlockDef moddef;
    private static WesterosBlockDef.CondProperty tempCOND;
    private WesterosBlockDef.CondProperty COND;
    
    protected WCStairBlock(BlockState modelstate, AbstractBlock.Properties props, WesterosBlockDef def, WesterosBlockDef moddef) {
        super(() -> modelstate, props);
        this.def = def;
        this.moddef = moddef;
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));
        }
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
    	super.createBlockStateDefinition(stateContainer);
    	if (tempCOND != null) {
    		COND = tempCOND;
    		tempCOND = null;
    	}
    	if (COND != null) {
	       stateContainer.add(COND);
    	}
    }
 
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if ((bs != null) && (COND != null)) {
    		bs = bs.setValue(COND, def.getMatchingCondition(ctx.getLevel(), ctx.getClickedPos())); 
    	}
    	return bs;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName();
        String modblkname = def.modelBlockName;
        // Make copy of model block textu def
        CopyBlockTextureRecord btr = mtd.addCopyBlockTextureRecord(blkname, modblkname, 0);
        btr.setTransparencyMode(TransparencyMode.SEMITRANSPARENT);
        // Get stair model
        md.addStairModel(blkname);
    } 
    private static String[] TAGS = { "stairs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
