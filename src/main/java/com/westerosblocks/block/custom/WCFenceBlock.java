package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import com.westerosblocks.utils.ModProperties;

import java.util.List;

public class WCFenceBlock extends FenceBlock implements WCBlockDef {
    protected BlockDefinition def;

    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    protected static BooleanProperty tempUNCONNECT;
    protected static ModProperties.StateProperty tempSTATE;

    private final boolean unconnect;
    private final boolean unconnectDefault;
    private final boolean toggleOnUse;
    protected ModProperties.StateProperty STATE;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();

            boolean unconnect = definition.isUnconnect();
            boolean toggleOnUse = definition.toggleOnUse();

            if (unconnect) {
                tempUNCONNECT = UNCONNECT;
            }

            if (stateProperty != null) {
                tempSTATE = stateProperty;
            }

            return new WCFenceBlock(settings, definition, unconnect, toggleOnUse);
        }
    }

    protected WCFenceBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean unconnect, boolean toggleOnUse) {
        super(settings);
        this.def = def;
        this.unconnect = unconnect;
        this.unconnectDefault = unconnect;
        this.toggleOnUse = toggleOnUse;

        BlockState defaultState = this.getStateManager().getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(WATERLOGGED, false);

        if (this.unconnect) {
            defaultState = defaultState.with(UNCONNECT, this.unconnectDefault);
        }

        if (STATE != null) {
            defaultState = defaultState.with(STATE, STATE.defValue);
        }

        setDefaultState(defaultState);
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
        if (unconnect && unconnectDefault) {
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

    private boolean isSameFence(BlockState state) {
        return state.isIn(BlockTags.FENCES) && state.isIn(BlockTags.WOODEN_FENCES) == this.getDefaultState().isIn(BlockTags.WOODEN_FENCES);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandStack().isEmpty()) {
            if (state.contains(this.STATE)) {
                state = state.cycle(this.STATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
