package com.westerosblocks.block.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.westerosblocks.data.BlockDefinition;
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
    protected BlockDefinition def;
    protected boolean toggleOnUse = false;

    protected boolean connectState;
    protected static IntProperty tempCONNECTSTATE;
    public static final IntProperty CONNECTSTATE = ModProperties.CONNECTSTATE;

    protected static ModProperties.StateProperty tempSTATE;
    public static ModProperties.StateProperty STATE;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            // Build settings from definition (1.18.2 pattern: def.makeProperties())
            AbstractBlock.Settings settings = definition.makeSettings();

            // Build state property from definition (1.18.2 pattern: def.buildStateProperty())
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            if (stateProperty != null) {
                tempSTATE = stateProperty;
            }

            // Extract block-specific properties from definition
            boolean doConnectState = definition.isConnectState();
            boolean doToggleOnUse = definition.toggleOnUse();
            boolean doAddStates = (stateProperty != null);

            if (doConnectState) {
                tempCONNECTSTATE = CONNECTSTATE;
            }

            return new WCSlabBlock(settings, definition, doConnectState, doToggleOnUse, doAddStates);
        }
    }

    public WCSlabBlock(Settings settings) {
        super(settings);
        this.def = null;
    }

    public WCSlabBlock(Settings settings, BlockDefinition def, boolean doConnectState, boolean doToggleOnUse, boolean doAddStates) {
        super(settings);
        this.def = def;

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
        if (tempCONNECTSTATE != null) {
            builder.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
        if (tempSTATE != null) {
            builder.add(tempSTATE);
            tempSTATE = null;
        }
    }

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
