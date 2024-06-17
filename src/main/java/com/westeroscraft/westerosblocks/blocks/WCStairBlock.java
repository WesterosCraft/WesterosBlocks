package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;

public class WCStairBlock extends StairBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            String t = def.getType();
            boolean doUnconnect = false;
            boolean doConnectstate = false;
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                	String[] parts = tok.split(":");
                    // See if we have unconnect
                    if (parts[0].equals("unconnect")) {
                    	doUnconnect = true;
                    	tempUNCONNECT = UNCONNECT;
                    }
                    // See if we have connectstate
					if (parts[0].equals("connectstate")) {
						doConnectstate = true;
						tempCONNECTSTATE = CONNECTSTATE;
					}
                }
            }
            if (def.modelBlockName == null) {
                WesterosBlocks.log.error("Type 'stair' requires modelBlockName settings");
                return null;
            }
            // Try to find model block
            Block blk = WesterosBlocks.findBlockByName(def.modelBlockName);
            if (blk == null) {
                WesterosBlocks.log.error(String.format("modelBlockName '%s' not found for block '%s'",
                        def.modelBlockName, def.blockName));
                return null;
            }
            if (blk.defaultBlockState().isAir()) {
                WesterosBlocks.log.error(String.format(
                        "modelBlockName '%s' not yet defined for block '%s' - must be defined before block",
                        def.modelBlockName, def.blockName));
                return null;
            }
            BlockBehaviour.Properties props = def.makeAndCopyProperties(blk);
            return def.registerRenderType(def.registerBlock(new WCStairBlock(blk.defaultBlockState(), props, def, doUnconnect, doConnectstate)),
                    false, false);
        }
    }

    private WesterosBlockDef def;
    public static final BooleanProperty UNCONNECT = BooleanProperty.create("unconnect");
    protected static BooleanProperty tempUNCONNECT;
    public final boolean unconnect;

    public static final IntegerProperty CONNECTSTATE = IntegerProperty.create("connectstate", 0, 3);
	protected static IntegerProperty tempCONNECTSTATE;
    public final boolean connectstate;

    public final boolean no_uvlock;

    protected WCStairBlock(BlockState modelstate, BlockBehaviour.Properties props, WesterosBlockDef def, boolean doUnconnect, boolean doConnectstate) {
        super(() -> modelstate, props);
        this.def = def;
        String t = def.getType();
        boolean no_uvlock = false;
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("no-uvlock")) {
                	no_uvlock = true;
                }
            }
        }
        this.no_uvlock = no_uvlock;
        this.unconnect = doUnconnect;
        this.connectstate = doConnectstate;

		BlockState defbs = this.stateDefinition.any()
                            .setValue(FACING, Direction.NORTH)
                            .setValue(HALF, Half.BOTTOM)
				            .setValue(SHAPE, StairsShape.STRAIGHT)
                            .setValue(WATERLOGGED, Boolean.valueOf(false));
		if (unconnect) {
			defbs = defbs.setValue(UNCONNECT, Boolean.valueOf(false));
		}
		if (connectstate) {
			defbs = defbs.setValue(CONNECTSTATE, 0);
		}
		this.registerDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
    	if (tempUNCONNECT != null) {
    		StateDefinition.add(tempUNCONNECT);
    		tempUNCONNECT = null;
    	}
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

    @Override
    public BlockState updateShape(BlockState state, Direction p_56926_, BlockState p_56927_, LevelAccessor world, BlockPos pos, BlockPos p_56930_) {
    	if (unconnect && state.getValue(UNCONNECT)) {
            if (state.getValue(WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
            return state;
    	}
    	return super.updateShape(state, p_56926_, p_56927_, world, pos, p_56930_);
     }

    private static String[] TAGS = { "stairs" };

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
