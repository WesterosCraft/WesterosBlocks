package com.westerosblocks.block.custom;

import java.util.ArrayList;

import com.westerosblocks.utils.ModProperties;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WCSlabBlock extends SlabBlock {
    protected boolean toggleOnUse = false;

    protected boolean connectState;
    protected static IntProperty tempCONNECTSTATE;
    public static final IntProperty CONNECTSTATE = ModProperties.CONNECTSTATE;

    protected static ModProperties.StateProperty tempSTATE;
    public static ModProperties.StateProperty STATE;

    public static class Factory extends BlockFactory {
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            boolean doConnectState = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            boolean doToggleOnUse = params.length > 1 && params[1] instanceof Boolean ? (Boolean) params[1] : false;
            boolean doAddStates = params.length > 2 && params[2] instanceof Integer && (Integer) params[2] > 0;

            if (doConnectState) {
                tempCONNECTSTATE = CONNECTSTATE;
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
            return new WCSlabBlock(settings, doConnectState, doToggleOnUse, doAddStates);
        }
    }

    public WCSlabBlock(Settings settings) {
        super(settings);
    }

    public WCSlabBlock(Settings settings, boolean doConnectState, boolean doToggleOnUse, boolean doAddStates) {
        super(settings);
        if (doToggleOnUse) {
            toggleOnUse = true;
        }

        BlockState defbs = this.getDefaultState();
        this.connectState = doConnectState;
        if (this.connectState) {
            defbs = defbs.with(CONNECTSTATE, 0);
        }
        if (doAddStates && STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            state = state.cycle(STATE);
            world.setBlockState(pos, state, 10);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }

}
