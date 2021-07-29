package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import org.dynmap.modsupport.CuboidBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.state.StateContainer;

import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;

import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.state.DirectionProperty;

public class WCCuboidNSEWStackBlock extends WCCuboidBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboidNSEWStackBlock(props, def)), false, false);
        }
    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    
    protected WesterosBlockDef def;
    
    // Index = FACING + 4*TOP
    protected List<WesterosBlockDef.Cuboid> cuboid_by_facing[] = new List[8];

    protected WCCuboidNSEWStackBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props, def);
        SHAPE_BY_INDEX = new VoxelShape[8];
        cuboid_by_facing = new List[8];
    	for (int j = 0; j < cuboid_by_facing.length; j++) {
    		cuboid_by_facing[j] = new ArrayList<WesterosBlockDef.Cuboid>();
    	}
        for (int i = 0; i < 2; i++) {
        	WesterosBlockDef.StackElement se = def.getStackElementByIndex(i);
            for (WesterosBlockDef.Cuboid c : se.cuboids) {
                cuboid_by_facing[4*i].add(c);
                cuboid_by_facing[4*i + 1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
                cuboid_by_facing[4*i + 2].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
                cuboid_by_facing[4*i + 3].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
            }        	
        }
        for (int j = 0; j < cuboid_by_facing.length; j++) {
            SHAPE_BY_INDEX[j] = getBoundingBoxFromCuboidList(cuboid_by_facing[j]);
        }
        this.registerDefaultState(this.stateDefinition.any()
        		.setValue(TOP, Boolean.valueOf(false))
        		.setValue(FACING, Direction.EAST)
        		.setValue(WATERLOGGED, Boolean.valueOf(false)));
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
    	stateContainer.add(FACING, TOP, WATERLOGGED);
    }
    @Override
    protected int getIndexFromState(BlockState state) {
    	int topoff = state.getValue(TOP) ? 4 : 0;
    	switch (state.getValue(FACING)) {
    	case EAST:
    	default:
    		return topoff;
    	case SOUTH:
    		return topoff+1;
    	case WEST:
    		return topoff+2;
    	case NORTH:
    		return topoff+3;
    	}
    }    
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        if (blockpos.getY() < 255 && ctx.getLevel().getBlockState(blockpos.above()).canBeReplaced(ctx)) {
            FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
            Direction[] adirection = ctx.getNearestLookingDirections();
            Direction dir = Direction.EAST;	// Default
            for (Direction d : adirection) {
            	if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
        			dir = d;
        			break;
            	}
            }
            return this.defaultBlockState()
        		.setValue(FACING, dir)
        		.setValue(TOP, Boolean.valueOf(false))
        		.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
        }
        else {
        	return null;
        }
    }
    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack item) {
        world.setBlock(pos.above(), state.setValue(TOP, Boolean.valueOf(true)), 3);
    }
    @Override
    public boolean canSurvive(BlockState state, IWorldReader reader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        return state.getValue(TOP) == Boolean.valueOf(false) ? blockstate.isFaceSturdy(reader, blockpos, Direction.UP) : blockstate.is(this);
    }
}
