package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class WCFireBlock extends FireBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeBlockSettings().noCollision().breakInstantly(); //.randomTicks();
            Block blk = new WCFireBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }
    private WesterosBlockDef def;

    protected WCFireBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(AGE, 0)
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false));
    }

    // prevents fire from being replaced by other fire blocks
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    // prevents vanilla fire spread mechanics. might wanna control via type prop at some point
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        //noop to prevent spread
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BASE_SHAPE;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(this)) {
            super.onBlockAdded(state, world, pos, oldState, notify);
        }
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    private static final String[] TAGS = { "fire" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
