package com.westeroscraft.westerosblocks.blocks;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.WallBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCWallBlock extends WallBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            BlockBehaviour.Properties props = def.makeProperties();
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
            return def.registerRenderType(def.registerBlock(new WCWallBlock(props, def, doUnconnect)), false, false);
        }
    }

    private WesterosBlockDef def;
    public static final BooleanProperty UNCONNECT = BooleanProperty.create("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    public final boolean unconnect;

    public final Map<BlockState, VoxelShape> ourShapeByIndex;
    public final Map<BlockState, VoxelShape> ourCollisionShapeByIndex;

    public static enum WallSize {
        NORMAL, // 16/16 high wall
        SHORT // 13/16 high wall
    };

    public final WallSize wallSize; // "normal", or "short"

    protected WCWallBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doUnconnect) {
        super(props);
        this.def = def;
        String height = def.getTypeValue("size", "normal");
        float wheight;
        if (height.equals("short")) {
            wallSize = WallSize.SHORT;
            wheight = 13;
        } else {
            wallSize = WallSize.NORMAL;
            wheight = 16;
        }
        unconnect = doUnconnect;
        if (unconnect) {
            this.registerDefaultState(this.stateDefinition.any().
            		setValue(UP, Boolean.valueOf(true)).
            		setValue(NORTH_WALL, WallSide.NONE).
            		setValue(EAST_WALL, WallSide.NONE).
            		setValue(SOUTH_WALL, WallSide.NONE).
            		setValue(WEST_WALL, WallSide.NONE).
            		setValue(WATERLOGGED, Boolean.valueOf(false)).
            		setValue(UNCONNECT, Boolean.valueOf(false)));
        }
        this.ourCollisionShapeByIndex = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
        this.ourShapeByIndex = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, wheight, 16.0F);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_58051_, BlockPos p_58052_, CollisionContext p_58053_) {
    	if (unconnect) {
    		state = state.setValue(UNCONNECT, Boolean.valueOf(false));
    	}
        return this.ourShapeByIndex.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_58056_, BlockPos p_58057_, CollisionContext p_58058_) {
    	if (unconnect) {
    		state = state.setValue(UNCONNECT, Boolean.valueOf(false));
    	}
        return this.ourCollisionShapeByIndex.get(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> sd) {
        if (tempUNCONNECT != null) {
            sd.add(tempUNCONNECT);
    		tempUNCONNECT = null;
        }
        sd.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState bs = super.getStateForPlacement(ctx);
        return bs;
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

    private static String[] TAGS = { "walls" };

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
