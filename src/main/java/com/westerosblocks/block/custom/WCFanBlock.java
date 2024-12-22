package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

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

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    
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
        BlockState defbs = this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false));
        this.registerDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs == null) return null;
      FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
      bs = bs.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    	return bs;
    }

    @SuppressWarnings("deprecation")
	  @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch(pathComputationType) {
            case LAND:
                return false;
            case WATER:
                return state.getFluidState().is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
    	stateDefinition.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (direction == Direction.DOWN && !this.canSurvive(blockState, levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos blockPos) {
    	if (this.allow_unsupported) return true;
      BlockPos blockPos2 = blockPos.below();
      return levelReader.getBlockState(blockPos2).isFaceSturdy(levelReader, blockPos2, Direction.UP);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
    }

    private static String[] TAGS = { "fans" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
