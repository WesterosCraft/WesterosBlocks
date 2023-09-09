package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCPaneBlock extends IronBarsBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties().noOcclusion();
            String t = def.getType();
            boolean doUnconnect = false;
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                	String[] parts = tok.split(":");
                    if (parts[0].equals("unconnect")) {
                    	doUnconnect = true;
                    	tempUNCONNECT = UNCONNECT;
                    }
                }
            }        	
        	return def.registerRenderType(def.registerBlock(new WCPaneBlock(props, def, doUnconnect)), false, true);
        }
    }
    
    public static final BooleanProperty UNCONNECT = BooleanProperty.create("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    private WesterosBlockDef def;
    private boolean legacy_model;
    private boolean bars_model;
    public final boolean unconnect;

    protected WCPaneBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doUnconnect) {
        super(props);
        this.def = def;
        this.unconnect = doUnconnect;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("legacy-model")) {
                    legacy_model = true;
                }
                if (tok.equals("bars-model")) {
                    bars_model = true;
                }
            }
        }
        if (doUnconnect) {
            this.registerDefaultState(this.stateDefinition.any().
            		setValue(NORTH, Boolean.valueOf(false)).
            		setValue(EAST, Boolean.valueOf(false)).
            		setValue(SOUTH, Boolean.valueOf(false)).
            		setValue(WEST, Boolean.valueOf(false)).
            		setValue(WATERLOGGED, Boolean.valueOf(false)).
            		setValue(UNCONNECT, Boolean.valueOf(false)));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> sd) {
        if (tempUNCONNECT != null) {
            sd.add(tempUNCONNECT);
    		tempUNCONNECT = null;
        }
        super.createBlockStateDefinition(sd);
    }

    @Override  
    public BlockState updateShape(BlockState state, Direction dir, BlockState nstate, LevelAccessor world, BlockPos pos, BlockPos pos2) {
    	if (unconnect && state.getValue(UNCONNECT)) {
            if (state.getValue(WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
            return state;
    	}
    	return super.updateShape(state, dir, nstate, world, pos, pos);
    }

    public boolean isLegacyModel() {
    	return legacy_model;
    }

    public boolean isBarsModel() {
        return bars_model;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    private static String[] TAGS = {  };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
