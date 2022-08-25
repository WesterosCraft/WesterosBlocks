package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCLogVertBlock extends RotatedPillarBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCLogVertBlock(props, def)), false, false);
        }
    }
    protected WesterosBlockDef def;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    
    protected WCLogVertBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));
    }
    
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AXIS, UP, DOWN);
    }

    // See if connected - avoid connection if ladders aren't on same facing
    private BlockState updateStateVertical(BlockState bs, BlockGetter reader, BlockPos pos) {
    	Axis val = bs.getValue(AXIS);
    	BlockState above, below;
    	if (val == Axis.Y) {
    		above = reader.getBlockState(pos.above());
    		below = reader.getBlockState(pos.below());
    	}
    	else if (val == Axis.X) {
    		above = reader.getBlockState(pos.east());
    		below = reader.getBlockState(pos.west());    		
    	}
    	else {
    		above = reader.getBlockState(pos.north());
    		below = reader.getBlockState(pos.south());    		    		
    	}
    	// If we connect to above
    	Boolean up = def.isConnectMatch(bs, above);
    	// If we connect to below
    	Boolean down = def.isConnectMatch(bs, below);
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
    		bs = updateStateVertical(bs, ctx.getLevel(), ctx.getClickedPos());
    	}
    	return bs;
    }

    private static String[] TAGS = { "logs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
