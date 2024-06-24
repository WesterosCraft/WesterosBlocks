package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class WCSlabBlock extends SlabBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
			// See if we have a state property
			WesterosBlockDef.StateProperty state = def.buildStateProperty();
			if (state != null) {
				tempSTATE = state;
			}
            // Process types
            String t = def.getType();
			boolean doConnectstate = false;
			if (t != null) {
				String[] toks = t.split(",");
				for (String tok : toks) {
					String[] parts = tok.split(":");
                    // See if we have connectstate
					if (parts[0].equals("connectstate")) {
						doConnectstate = true;
						tempCONNECTSTATE = CONNECTSTATE;
					}
				}
			}
        	return def.registerRenderType(def.registerBlock(new WCSlabBlock(props, def, doConnectstate)), false, false);
        }
    }
    protected WesterosBlockDef def;

	public static final IntegerProperty CONNECTSTATE = IntegerProperty.create("connectstate", 0, 3);
	protected static IntegerProperty tempCONNECTSTATE;
    public final boolean connectstate;

	protected static WesterosBlockDef.StateProperty tempSTATE;
	protected WesterosBlockDef.StateProperty STATE;

	protected boolean toggleOnUse = false;

    protected WCSlabBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doConnectstate) {
        super(props);
        this.def = def;

		String t = def.getType();
		if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                }
            }
		}
    
        connectstate = doConnectstate;
		BlockState defbs = this.stateDefinition.any();
		if (connectstate) {
			defbs = defbs.setValue(CONNECTSTATE, 0);
		}
        if (STATE != null) {
			defbs = defbs.setValue(STATE, STATE.defValue);
		}
        this.registerDefaultState(defbs);
    }

    protected WCSlabBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        this(props, def, false);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        if (tempCONNECTSTATE != null) {
            StateDefinition.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
		if (tempSTATE != null) {
			STATE = tempSTATE;
			tempSTATE = null;
		}
		if (STATE != null) {
			StateDefinition.add(STATE);
		}
    	super.createBlockStateDefinition(StateDefinition);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	return bs;
    }

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitrslt) {
		if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandItem().isEmpty()) {
            state = state.cycle(this.STATE);
            level.setBlock(pos, state, 10);
            level.levelEvent(player, 1006, pos, 0);
            return InteractionResult.sidedSuccess(level.isClientSide);
		}
		else {
			return InteractionResult.PASS;
		}
	}

    private static String[] TAGS = { "slabs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
