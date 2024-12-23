package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WCSlabBlock extends SlabBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
			AbstractBlock.Settings settings = def.makeBlockSettings();
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
                        break;
                    }
				}
			}
			Block blk = new WCSlabBlock(settings, def, doConnectstate);
			return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }
    protected WesterosBlockDef def;

	public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
	protected static IntProperty tempCONNECTSTATE;
    public final boolean connectstate;

	protected static WesterosBlockDef.StateProperty tempSTATE;
	protected WesterosBlockDef.StateProperty STATE;

	protected boolean toggleOnUse = false;

    protected WCSlabBlock(AbstractBlock.Settings settings, WesterosBlockDef def, boolean doConnectstate) {
        super(settings);
        this.def = def;

		String t = def.getType();
		if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                    break;
                }
            }
		}
    
        connectstate = doConnectstate;
		BlockState defbs = getDefaultState();
		if (connectstate) {
			defbs = defbs.with(CONNECTSTATE, 0);
		}
        if (STATE != null) {
			defbs = defbs.with(STATE, STATE.defValue);
		}
		this.setDefaultState(defbs);
    }

    protected WCSlabBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
		this(settings, def, false);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (tempCONNECTSTATE != null) {
			builder.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
		if (tempSTATE != null) {
			STATE = tempSTATE;
			tempSTATE = null;
		}
		if (STATE != null) {
			builder.add(STATE);
		}
    	super.appendProperties(builder);
    }

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
								 BlockHitResult hit) {
		Hand hand = player.getActiveHand();
		if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
			state = state.cycle(this.STATE);
			world.setBlockState(pos, state, 10);
			world.syncWorldEvent(player, 1006, pos, 0);
			return ActionResult.success(world.isClient);
		}
		else {
			return ActionResult.PASS;
		}
	}

    private static final String[] TAGS = { "slabs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
