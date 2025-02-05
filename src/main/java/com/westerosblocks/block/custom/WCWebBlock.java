package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WCWebBlock extends Block implements ModBlockLifecycle {
    private ModBlock def;
    protected static ModBlock.StateProperty tempSTATE;
    protected static IntProperty tempLAYERS;
    protected ModBlock.StateProperty STATE;
    protected IntProperty LAYERS;
    protected boolean toggleOnUse = false;
    private boolean noInWeb = false;
    public boolean layerSensitive = false;
    public static final BooleanProperty WATERLOGGED = BooleanProperty.of("waterlogged");

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings().noCollision();

        	ModBlock.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}
            String t = def.getType();
            if ((t != null) && (t.contains(ModBlock.LAYER_SENSITIVE))) {
            	tempLAYERS = Properties.LAYERS;
            }

            Block blk = new WCWebBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    protected WCWebBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("no-in-web")) {
                    noInWeb = true;
                }
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                }
            }
        }
        BlockState bsdef = this.getDefaultState().with(WATERLOGGED, Boolean.FALSE);
    	if (LAYERS != null) {
    		bsdef = bsdef.with(LAYERS, 8);
    	}
        if (STATE != null) {
        	bsdef = bsdef.with(STATE, STATE.defValue);
        }
    	setDefaultState(bsdef);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!noInWeb)
    		super.onEntityCollision(state, world, pos, entity);
        else
            // Here you can implement cobweb-like behavior
            entity.slowMovement(state, new Vec3d(0.25D, 0.05F, 0.25D));
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    	if (tempSTATE != null) {
    		STATE = tempSTATE;
    		tempSTATE = null;
    	}
    	if (tempLAYERS != null) {
    		LAYERS = tempLAYERS;
    		tempLAYERS = null;
    	}
    	if (STATE != null) {
            builder.add(STATE);
    	}
    	if (LAYERS != null) {
            builder.add(LAYERS);
    	}
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
    	BlockState bs = super.getPlacementState(ctx);
    	if (bs == null) return null;
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        bs = bs.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
        if (STATE != null) {
     	   bs = bs.with(STATE, STATE.defValue);
        }
        if (LAYERS != null) {
        	BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
        	if ((below != null) && (below.contains(Properties.LAYERS))) {
        		Block blk = below.getBlock();
        		Integer layer = below.get(Properties.LAYERS);
        		// See if soft layer
        		if ((blk == Blocks.SNOW) || ((blk instanceof WCLayerBlock) && ((WCLayerBlock)blk).softLayer)) {
        			layer = (layer > 2) ? Integer.valueOf(layer - 2) : Integer.valueOf(1);
        		}
        		bs = bs.with(LAYERS, layer);
        	}
        	else if ((below != null) && (below.getBlock() instanceof SlabBlock)) {
        		SlabType slabtype = below.get(Properties.SLAB_TYPE);
        		if (slabtype == SlabType.BOTTOM) bs = bs.with(LAYERS, 4);
        	}
        }
    	return bs;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(blockState);
    }


    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return switch (type) {
            case LAND -> false;
            case WATER -> state.getFluidState().isIn(FluidTags.WATER);
            case AIR -> false;
            default -> false;
        };
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
        } else {
            return ActionResult.PASS;
        }
    }

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
