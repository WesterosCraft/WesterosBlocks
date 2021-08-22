package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;

import javax.annotation.Nullable;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.SlabType;

public class WCSlabBlock extends SlabBlock implements WesterosBlockDynmapSupport, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	        	
        	return def.registerRenderType(def.registerBlock(new WCSlabBlock(props, def)), false, false);
        }
    }
    protected WesterosBlockDef def;
    private static WesterosBlockDef.CondProperty tempCOND;
    private WesterosBlockDef.CondProperty COND;

    protected WCSlabBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        if (COND != null) {
            this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));
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
        def.defaultRegisterTextures(mtd);
        
        /* Add models for half slabs */
        String blkname = def.getBlockName();
        def.defaultRegisterTextureBlock(mtd, TransparencyMode.SEMITRANSPARENT, 0, 2);
        def.defaultRegisterTextureBlock(mtd, TransparencyMode.OPAQUE, 2, 1);
        BoxBlockModel bottom = md.addBoxModel(blkname);
        bottom.setYRange(0.0, 0.5);
        BoxBlockModel top = md.addBoxModel(blkname);
        top.setYRange(0.5, 1.0);
        bottom.setMetaValue(0);
        top.setMetaValue(1);
    }
    private static String[] TAGS = { "slabs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
