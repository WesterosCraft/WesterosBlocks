package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;

import net.neoforged.neoforge.registries.RegisterEvent;

public class WCFenceBlock extends FenceBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	BlockBehaviour.Properties props = def.makeProperties();
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
            Block blk = new WCFenceBlock(props, def, doUnconnect);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
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

    protected WCFenceBlock(BlockBehaviour.Properties props, WesterosBlockDef def, Boolean doUnconnect) {
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

        unconnect = (doUnconnect != null);
        unconnectDef = doUnconnect;
        BlockState defbs = this.stateDefinition.any()
                            .setValue(NORTH, Boolean.valueOf(false))
                            .setValue(EAST, Boolean.valueOf(false))
                            .setValue(SOUTH, Boolean.valueOf(false))
                            .setValue(WEST, Boolean.valueOf(false))
                            .setValue(WATERLOGGED, Boolean.valueOf(false));
        if (unconnect) {
            defbs = defbs.setValue(UNCONNECT, unconnectDef);
        }
		if (STATE != null) {
			defbs = defbs.setValue(STATE, STATE.defValue);
		}
        this.registerDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }    
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
    	if (tempUNCONNECT != null) {
    		stateDefinition.add(tempUNCONNECT);
    		tempUNCONNECT = null;
    	}
		if (tempSTATE != null) {
			STATE = tempSTATE;
			tempSTATE = null;
		}
		if (STATE != null) {
			stateDefinition.add(STATE);
		}
    	super.createBlockStateDefinition(stateDefinition);
    }

    @Override  
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	if (unconnect && unconnectDef) {
    		return this.defaultBlockState();
    	}
    	return super.getStateForPlacement(ctx);
    }
    

    @Override  
    public BlockState updateShape(BlockState state, Direction dir, BlockState nstate, LevelAccessor world, BlockPos pos, BlockPos pos2) {
    	if (unconnect && state.getValue(UNCONNECT)) {
            if (state.getValue(WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
            return state;
    	}
    	return super.updateShape(state, dir, nstate, world, pos, pos);
    }

    @Override  
    public boolean connectsTo(BlockState p_53330_, boolean p_53331_, Direction p_53332_) {
        Block block = p_53330_.getBlock();
        boolean flag = this.isSameFence(p_53330_) && ((!p_53330_.hasProperty(UNCONNECT)) || (!p_53330_.getValue(UNCONNECT)));
        boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(p_53330_, p_53332_);
        return !isExceptionForConnection(p_53330_) && p_53331_ || flag || flag1;
    }

    private boolean isSameFence(BlockState p_153255_) {
        return p_153255_.is(BlockTags.FENCES) && p_153255_.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
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


    private static String[] TAGS = { "fences" };
    private static String[] TAGS2 = { "fences", "wooden_fences" };
    @Override
    public String[] getBlockTags() {
        return def.getMaterial() == AuxMaterial.WOOD ? TAGS2 : TAGS;
    }    
}
