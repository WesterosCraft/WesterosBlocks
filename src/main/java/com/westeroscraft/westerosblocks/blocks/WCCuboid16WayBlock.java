package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList; 
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboid16WayBlock extends WCCuboidBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboid16WayBlock(props, def)), false, false);
        }
    }

    private static final int ROTATIONS = 16;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    protected List<WesterosBlockDef.Cuboid> cuboid_by_facing[] = new List[ROTATIONS];
    
    private static final WesterosBlockDef.CuboidRotation[] shape_rotations = { 
		WesterosBlockDef.CuboidRotation.NONE,
		WesterosBlockDef.CuboidRotation.ROTY90,
		WesterosBlockDef.CuboidRotation.ROTY180,
		WesterosBlockDef.CuboidRotation.ROTY270 };
    @SuppressWarnings("unchecked")
    protected WCCuboid16WayBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def);
        
        cuboid_by_facing[0] = def.getCuboidList();	// East facing
        for (int i = 1; i < ROTATIONS; i++) {
        	cuboid_by_facing[i] = new ArrayList<WesterosBlockDef.Cuboid>();
	        for (WesterosBlockDef.Cuboid c : cuboid_by_facing[0]) {
	        	cuboid_by_facing[i].add(c.rotateCuboid(shape_rotations[i / 4]));
	        }
        }
        this.SHAPE_BY_INDEX = new VoxelShape[cuboid_by_facing.length];
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
       StateDefinition.add(ROTATION, WATERLOGGED);
    }
    
    @Override
    protected int getIndexFromState(BlockState state) {
    	return state.getValue(ROTATION);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(ROTATION, Integer.valueOf(rot.rotate(state.getValue(ROTATION), 16)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, Integer.valueOf(mirror.mirror(state.getValue(ROTATION), 16)));
     }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       Integer dir = Integer.valueOf(Mth.floor((double)(ctx.getRotation() * 16.0F / 360.0F) + 0.5D) & 15);
       return this.defaultBlockState().setValue(ROTATION, dir).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    }
}
