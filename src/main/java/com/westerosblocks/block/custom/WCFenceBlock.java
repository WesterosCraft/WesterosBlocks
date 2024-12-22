package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WCFenceBlock extends FenceBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties();
			// See if we have a state property
			WesterosBlockDef.StateProperty state = def.buildStateProperty();
			if (state != null) {
				tempSTATE = state;
			}
            // Process types
            String t = def.getType();
            Boolean doUnconnect = null;
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                	String[] parts = tok.split(":");
                    // See if we have unconnect
                    if (parts[0].equals("unconnect")) {
                    	doUnconnect = "true".equals(parts[1]);
                    	tempUNCONNECT = UNCONNECT;
                    }
                }
            }
            Block blk = new WCFenceBlock(settings, def, doUnconnect);
            return def.registerRenderType(blk, false, false);
        }
    };
    
    public static final BooleanProperty UNCONNECT = BooleanProperty.create("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    public final boolean unconnect;
    public final Boolean unconnectDef;

	protected static WesterosBlockDef.StateProperty tempSTATE;
	protected WesterosBlockDef.StateProperty STATE;

	protected boolean toggleOnUse = false;

    private WesterosBlockDef def;

    protected WCFenceBlock(AbstractBlock.Settings settings, WesterosBlockDef def, Boolean doUnconnect) {
        super(settings);
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

        unconnect = (doUnconnect != null);
        unconnectDef = doUnconnect;
        BlockState defbs = this.stateDefinition.any()
                            .with(NORTH, Boolean.valueOf(false))
                            .with(EAST, Boolean.valueOf(false))
                            .with(SOUTH, Boolean.valueOf(false))
                            .with(WEST, Boolean.valueOf(false))
                            .with(WATERLOGGED, Boolean.valueOf(false));
        if (unconnect) {
            defbs = defbs.with(UNCONNECT, unconnectDef);
        }
		if (STATE != null) {
			defbs = defbs.with(STATE, STATE.defValue);
		}
        setDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    	if (tempUNCONNECT != null) {
    		builder.add(tempUNCONNECT);
    		tempUNCONNECT = null;
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
    	if (unconnect && unconnectDef) {
    		return getDefaultState();
    	}
    	return super.getPlacementState(ctx);
    }
    

    @Override  
    public BlockState updateShape(BlockState state, Direction dir, BlockState nstate, LevelAccessor world, BlockPos pos, BlockPos pos2) {
    	if (unconnect && state.get(UNCONNECT)) {
            if (state.get(WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
            return state;
    	}
    	return super.updateShape(state, dir, nstate, world, pos, pos);
    }

    @Override  
    public boolean connectsTo(BlockState p_53330_, boolean p_53331_, Direction p_53332_) {
        Block block = p_53330_.getBlock();
        boolean flag = this.isSameFence(p_53330_) && ((!p_53330_.hasProperty(UNCONNECT)) || (!p_53330_.get(UNCONNECT)));
        boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(p_53330_, p_53332_);
        return !isExceptionForConnection(p_53330_) && p_53331_ || flag || flag1;
    }

    private boolean isSameFence(BlockState state) {
        return state.isIn(BlockTags.FENCES) && state.isIn(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
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


    private static String[] TAGS = { "fences" };
    private static String[] TAGS2 = { "fences", "wooden_fences" };
    @Override
    public String[] getBlockTags() {
        return def.getMaterial() == AuxMaterial.WOOD ? TAGS2 : TAGS;
    }    
}
