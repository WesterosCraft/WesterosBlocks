package com.westerosblocks.block.custom;

import com.westerosblocks.block.*;
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
import net.minecraft.world.WorldAccess;

public class WCFenceBlock extends FenceBlock implements WesterosBlockLifecycle {

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
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    public final boolean unconnect;
    public final Boolean unconnectDef;

    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected WesterosBlockDef.StateProperty STATE;

    protected boolean toggleOnUse = false;

    private final WesterosBlockDef def;

    protected WCFenceBlock(AbstractBlock.Settings settings, WesterosBlockDef def, Boolean doUnconnect) {
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

        unconnect = (doUnconnect != null);
        unconnectDef = doUnconnect;
        BlockState defbs = this.getStateManager().getDefaultState()
                .with(NORTH, Boolean.FALSE)
                .with(EAST, Boolean.FALSE)
                .with(SOUTH, Boolean.FALSE)
                .with(WEST, Boolean.FALSE)
                .with(WATERLOGGED, Boolean.FALSE);
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
            return this.getDefaultState();
        }
        return super.getPlacementState(ctx);
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (unconnect && state.get(UNCONNECT)) {
            if (state.get(WATERLOGGED)) {
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return state;
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        Block block = state.getBlock();
        boolean bl = this.isSameFence(state) && (!state.contains(UNCONNECT) || !state.get(UNCONNECT));
        boolean bl2 = block instanceof FenceGateBlock && FenceGateBlock.canWallConnect(state, dir);
        return !Block.cannotConnect(state) && neighborIsFullSquare || bl || bl2;
    }

    // Helper method to check if a fence gate can connect in a direction
    private boolean canFenceGateConnect(BlockState state, Direction dir) {
        Direction facing = state.get(FenceGateBlock.FACING);
        return facing.getAxis() == dir.getAxis();
    }

    private boolean isSameFence(BlockState state) {
        return state.isIn(BlockTags.FENCES) && state.isIn(BlockTags.WOODEN_FENCES) == this.getDefaultState().isIn(BlockTags.WOODEN_FENCES);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
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

    private static final String[] TAGS = {"fences"};
    private static final String[] TAGS2 = {"fences", "wooden_fences"};

    @Override
    public String[] getBlockTags() {
        return WesterosBlockSettings.get(def.material) == WesterosBlockSettings.get("wood") ? TAGS2 : TAGS;
    }
}
