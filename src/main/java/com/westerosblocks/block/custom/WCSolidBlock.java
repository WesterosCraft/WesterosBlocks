package com.westerosblocks.block.custom;

import com.westerosblocks.utils.ModProperties;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;

public class WCSolidBlock extends Block {
    protected boolean toggleOnUse = false;

    protected boolean connectState;
    protected static IntProperty tempCONNECTSTATE;
    public static final IntProperty CONNECTSTATE = ModProperties.CONNECTSTATE;

    protected static ModProperties.StateProperty tempSTATE;
    public static ModProperties.StateProperty STATE;

    public boolean symmetrical;
    protected static BooleanProperty tempSYMMETRICAL;
    public static BooleanProperty SYMMETRICAL = ModProperties.SYMMETRICAL;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            boolean doConnectState = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            boolean doToggleOnUse = params.length > 1 && params[1] instanceof Boolean ? (Boolean) params[1] : false;
            boolean doAddStates = params.length > 2 && params[2] instanceof Integer && (Integer) params[2] > 0;
            boolean doSymmetrical = params.length > 3 && params[3] instanceof Boolean ? (Boolean) params[3] : false;

            if (doConnectState) {
                tempCONNECTSTATE = CONNECTSTATE;
            }

            if (doSymmetrical) {
                tempSYMMETRICAL = SYMMETRICAL;
            }

            if (doAddStates) {
                int numStates = (Integer) params[2];
                ArrayList<String> stateIds = new ArrayList<>();
                for (int i = 0; i < numStates; i++) {
                    stateIds.add("state" + i);
                }
                STATE = new ModProperties.StateProperty(stateIds);
                tempSTATE = STATE;
            }

            return new WCSolidBlock(settings, doConnectState, doToggleOnUse, doAddStates, doSymmetrical);
        }
    }

    public WCSolidBlock(AbstractBlock.Settings settings) {
        super(settings);

    }

    public WCSolidBlock(AbstractBlock.Settings settings, boolean connectedState, boolean doToggleOnUse,
            boolean addStates, boolean doSymmetrical) {
        super(settings);

        if (doToggleOnUse) {
            toggleOnUse = true;
        }

        BlockState defbs = this.getDefaultState();
        this.connectState = connectedState;
        this.symmetrical = doSymmetrical;
        if (this.connectState) {
            defbs = defbs.with(CONNECTSTATE, 0);
        }
        if (this.symmetrical) {
            defbs = defbs.with(SYMMETRICAL, symmetrical);
        }
        if (addStates && STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (tempCONNECTSTATE != null) {
            builder.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
        if (tempSYMMETRICAL != null) {
            builder.add(tempSYMMETRICAL);
            tempSYMMETRICAL = null;
        }
        if (tempSTATE != null) {
            builder.add(tempSTATE);
            tempSTATE = null;
        }
        super.appendProperties(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState bs = super.getPlacementState(ctx);
        if (connectState && bs != null && bs.contains(CONNECTSTATE)) {
            bs = bs.with(CONNECTSTATE, 0);
        }
        return bs;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            if (state.contains(STATE)) {
                state = state.cycle(STATE);
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
