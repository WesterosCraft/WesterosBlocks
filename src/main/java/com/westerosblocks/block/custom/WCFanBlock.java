package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class WCFanBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties floorprops = def.makeProperties().noCollision().breakInstantly();
        	Block floorblock = new WCFanBlock(floorprops, def);
        	@SuppressWarnings("deprecation")
			    BlockBehaviour.Properties wallprops = def.makeProperties().noCollision().breakInstantly().dropsLike(floorblock);
        	Block wallblock = new WCWallFanBlock(wallprops, def);
//        	def.registerWallOrFloorBlock(floorblock, wallblock, helper);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), floorblock);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, "wall_" + def.blockName), wallblock);
            def.registerBlockItem(def.blockName, floorblock);
            def.registerBlockItem("wall_" + def.blockName, wallblock);

            def.registerRenderType(floorblock, false, false);
        	def.registerRenderType(wallblock, false, false);
        	return floorblock;
        }
    }

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private WesterosBlockDef def;
    private boolean allow_unsupported = false;

    private static final VoxelShape SHAPE = VoxelShapes.cuboid(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    protected WCFanBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
            }
        }
        BlockState defbs = this.stateDefinition.any().with(WATERLOGGED, Boolean.valueOf(false));
        setDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
    	BlockState bs = super.getPlacementState(ctx);
    	if (bs == null) return null;
      FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
      bs = bs.with(WATERLOGGED, Boolean.valueOf(fluidstate.isIn(FluidTags.WATER)));
    	return bs;
    }

    @SuppressWarnings("deprecation")
	  @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }


    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        switch(type) {
            case LAND:
                return false;
            case WATER:
                return state.getFluidState().isIn(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    	builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {        if (direction == Direction.DOWN && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    	if (this.allow_unsupported) return true;
      BlockPos blockPos2 = pos.below();
      return world.getBlockState(blockPos2).isFaceSturdy(world, blockPos2, Direction.UP);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return SHAPE;
    }

    private static String[] TAGS = { "fans" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
}
