package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList; 
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboidNEBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboidNEBlock(props, def)), false, false);
        }
    }
    
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.EAST, Direction.NORTH);

    protected List<WesterosBlockDef.Cuboid> cuboid_by_facing[] = new List[2];
    
    @SuppressWarnings("unchecked")
    protected WCCuboidNEBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props, def);
        
        cuboid_by_facing[0] = def.getCuboidList();	// East facing
        cuboid_by_facing[1] = new ArrayList<WesterosBlockDef.Cuboid>();
        for (WesterosBlockDef.Cuboid c : cuboid_by_facing[0]) {
            cuboid_by_facing[1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
        }
        this.SHAPE_BY_INDEX = new VoxelShape[cuboid_by_facing.length];
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.EAST));
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
       stateContainer.add(FACING, WATERLOGGED);
    }
    
    @Override
    protected int getIndexFromState(BlockState state) {
    	return (state.getValue(FACING) == Direction.EAST) ? 0 : 1;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       Direction[] adirection = ctx.getNearestLookingDirections();
       Direction dir = Direction.EAST;	// Default
       for (Direction d : adirection) {
    	   if (d == Direction.EAST || d == Direction.WEST) {
    		   dir = Direction.EAST;
    		   break;
    	   }
    	   if (d == Direction.NORTH || d == Direction.SOUTH) {
    		   dir = Direction.NORTH;
    		   break;
    	   }
       }
       return this.defaultBlockState().setValue(FACING, dir).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    }
}
