package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;

public class WCPaneBlock extends IronBarsBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties().noOcclusion();
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
            Block blk = new WCPaneBlock(settings, def, doUnconnect);
            return def.registerRenderType(blk, false, true);
        }
    }
    
    public static final BooleanProperty UNCONNECT = BooleanProperty.create("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    private WesterosBlockDef def;
    private boolean legacy_model;
    private boolean bars_model;
    public final boolean unconnect;

    protected WCPaneBlock(AbstractBlock.Settings settings, WesterosBlockDef def, boolean doUnconnect) {
        super(settings);
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
