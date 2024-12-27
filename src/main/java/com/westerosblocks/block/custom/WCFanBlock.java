package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
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
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class WCFanBlock extends Block implements WesterosBlockLifecycle {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private final WesterosBlockDef def;
    private boolean allow_unsupported = false;
    private static final VoxelShape SHAPE = VoxelShapes.cuboid(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings floorSettings = def.makeBlockSettings().noCollision().breakInstantly();
        	Block floorBlock = new WCFanBlock(floorSettings, def);

            AbstractBlock.Settings wallSettings = def.makeBlockSettings().noCollision().breakInstantly().dropsLike(floorBlock);
        	Block wallBlock = new WCWallFanBlock(wallSettings, def);

            def.registerRenderType(ModBlocks.registerBlock(def.blockName, floorBlock), false, false);
        	def.registerRenderType(ModBlocks.registerBlock("wall_" + def.blockName, wallBlock), false, false);
        	return floorBlock;
        }
    }

    protected WCFanBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                    break;
                }
            }
        }
        BlockState defbs = this.getDefaultState().with(WATERLOGGED, Boolean.FALSE);
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
        bs = bs.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
    	return bs;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return switch (type) {
            case LAND -> false;
            case WATER -> state.getFluidState().isIn(FluidTags.WATER);
            case AIR -> false;
            default -> false;
        };
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
      BlockPos blockPos2 = pos.down();
      return world.getBlockState(blockPos2).isSideSolidFullSquare(world, blockPos2, Direction.UP);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
      return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return SHAPE;
    }

    private static final String[] TAGS = { "fans" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
}
