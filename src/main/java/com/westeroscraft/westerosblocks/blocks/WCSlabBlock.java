package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class WCSlabBlock extends SlabBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
            // See if we have connectstate
            String t = def.getType();
			boolean doConnectstate = false;
			if (t != null) {
				String[] toks = t.split(",");
				for (String tok : toks) {
					String[] parts = tok.split(":");
					if (parts[0].equals("connectstate")) {
						doConnectstate = true;
						tempCONNECTSTATE = CONNECTSTATE;
					}
				}
			}
        	return def.registerRenderType(def.registerBlock(new WCSlabBlock(props, def, doConnectstate)), false, false);
        }
    }
    protected WesterosBlockDef def;

	public static final IntegerProperty CONNECTSTATE = IntegerProperty.create("connectstate", 0, 3);
	protected static IntegerProperty tempCONNECTSTATE;
    public final boolean connectstate;

    protected WCSlabBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doConnectstate) {
        super(props);
        this.def = def;
    
        connectstate = doConnectstate;
		BlockState defbs = this.stateDefinition.any();
		if (connectstate) {
			defbs = defbs.setValue(CONNECTSTATE, 0);
		}
        this.registerDefaultState(defbs);
    }

    protected WCSlabBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        this(props, def, false);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        if (tempCONNECTSTATE != null) {
            StateDefinition.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
    	super.createBlockStateDefinition(StateDefinition);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	return bs;
    }

    private static String[] TAGS = { "slabs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
