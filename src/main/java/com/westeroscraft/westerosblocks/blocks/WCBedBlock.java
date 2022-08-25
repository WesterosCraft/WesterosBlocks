package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.item.DyeColor;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;


public class WCBedBlock extends BedBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties().noOcclusion();
        	return def.registerRenderType(def.registerBlock(new WCBedBlock(props, def)), false, false);
        }        
    }
    
    private WesterosBlockDef def;
    public static enum BedType {
    	NORMAL, RAISED, HAMMOCK
    };
    public final BedType bedType;
    
    protected WCBedBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(DyeColor.RED, props);
        this.def = def;
        String type = def.getTypeValue("shape", "normal");
        if (type.equals("raised"))
        	bedType = BedType.RAISED;
        else if (type.equals("hammock"))
        	bedType = BedType.HAMMOCK;
        else
        	bedType = BedType.NORMAL;        
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    // Force to model for now
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_152175_, BlockState p_152176_) {
        return null;
     }

    private static String[] TAGS = { "beds" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
