package com.westerosblocks.block.custom;

import com.westerosblocks.utils.ModProperties;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WCSolidBlock extends Block {

    public static final IntProperty CONNECTSTATE = ModProperties.CONNECTSTATE;
    protected static IntProperty tempCONNECTSTATE;
    protected boolean connectstate;
    protected boolean toggleOnUse = false;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            boolean doConnectstate = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            boolean doToggleOnUse = params.length > 1 && params[1] instanceof Boolean ? (Boolean) params[1] : false;

            if (doConnectstate) {
                tempCONNECTSTATE = CONNECTSTATE;
            }

            Block blk = new WCSolidBlock(settings, doConnectstate, doToggleOnUse);
            return blk;
        }
    }

    public WCSolidBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.connectstate = false;
        this.toggleOnUse = false;
        this.setDefaultState(this.getDefaultState());
    }

    public WCSolidBlock(AbstractBlock.Settings settings, boolean connectstate, boolean toggleOnUse) {
        super(settings);
        this.connectstate = connectstate;
        this.toggleOnUse = toggleOnUse;
        BlockState defbs = this.getDefaultState();
        if (connectstate) {
            defbs = defbs.with(CONNECTSTATE, 0);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (tempCONNECTSTATE != null) {
            builder.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
        super.appendProperties(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState bs = super.getPlacementState(ctx);
        if (connectstate && bs != null && bs.contains(CONNECTSTATE)) {
            bs = bs.with(CONNECTSTATE, 0);
        }
        return bs;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && connectstate && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            if (state.contains(CONNECTSTATE)) {
                state = state.cycle(CONNECTSTATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }
}
