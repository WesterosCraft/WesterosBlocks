package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelReader;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCLadderVertBlock extends LadderBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties().noOcclusion();
        	return def.registerRenderType(def.registerBlock(new WCLadderVertBlock(props, def)), false, false);
        }
    }

    private WesterosBlockDef def;
    private boolean allow_unsupported;
    private boolean no_climb;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty UP = BlockStateProperties.UP;

    protected WCLadderVertBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                if (tok.equals("no-climb")) {
                	no_climb = true;
                }
            }
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    // See if connected - avoid connection if ladders aren't on same facing
    private BlockState updateStateVertical(BlockState bs, BlockGetter reader, BlockPos pos) {
    	BlockState bsneighbor = reader.getBlockState(pos.above());
    	// If we connect to above, and are not the same block type OR have the same facing (and are the same)
    	Boolean up = def.isConnectMatch(bs, bsneighbor) && ((bs.getBlock() != bsneighbor.getBlock()) ||
    			Boolean.valueOf((bs.getValue(FACING) == bsneighbor.getValue(FACING))));
    	bsneighbor = reader.getBlockState(pos.below());
    	// If we connect to below, and are not the same block type OR have the same facing (and are the same)
    	Boolean down = def.isConnectMatch(bs, bsneighbor) && ((bs.getBlock() != bsneighbor.getBlock()) ||
    			Boolean.valueOf((bs.getValue(FACING) == bsneighbor.getValue(FACING))));
    	return bs.setValue(UP, up).setValue(DOWN, down);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
    	state = super.updateShape(state, dir, state2, world, pos, pos2);
    	if (state != null) {
    		state = updateStateVertical(state, world, pos);
    	}
    	return state;
    }

    @Nullable 
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs != null) {
    		bs = updateStateVertical(this.defaultBlockState(), ctx.getLevel(), ctx.getClickedPos());
    	}
    	return bs;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, WATERLOGGED, UP, DOWN);
     }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return allow_unsupported || super.canSurvive(state, world, pos);
     }
    private static String[] TAGS = { "climbable" };
    private static String[] TAGS_NOCLIMB = {  };
    @Override
    public String[] getBlockTags() {
    	if (no_climb) return TAGS_NOCLIMB;
    	return TAGS;
    }    

}
