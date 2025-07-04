package com.westerosblocks.block.custom;

import com.westerosblocks.util.ModBlockSetType;
import com.westerosblocks.util.ModWoodType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.block.WoodType;

import java.util.List;

public class WCStandaloneTrapDoorBlock extends TrapdoorBlock {
    private final String blockName;
    private final String creativeTab;
    private final WoodType woodType;
    private final boolean locked;

    public WCStandaloneTrapDoorBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, ModWoodType.getWoodType("oak"), false);
    }

    public WCStandaloneTrapDoorBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType) {
        this(settings, blockName, creativeTab, ModWoodType.getWoodType(woodType), false);
    }

    public WCStandaloneTrapDoorBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType, boolean locked) {
        this(settings, blockName, creativeTab, ModWoodType.getWoodType(woodType), locked);
    }

    public WCStandaloneTrapDoorBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, WoodType woodType) {
        this(settings, blockName, creativeTab, woodType, false);
    }

    public WCStandaloneTrapDoorBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, WoodType woodType, boolean locked) {
        super(ModBlockSetType.getBlockSetType(woodType.toString().toLowerCase()), settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
        this.locked = locked;
        
        this.setDefaultState(this.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, false)
                .with(HALF, BlockHalf.BOTTOM)
                .with(POWERED, false)
                .with(WATERLOGGED, false));
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, HALF, POWERED, WATERLOGGED);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction direction = ctx.getSide();

        if (!ctx.canReplaceExisting() && direction.getAxis().isHorizontal()) {
            blockState = blockState.with(FACING, direction)
                    .with(HALF, ctx.getHitPos().y - (double)ctx.getBlockPos().getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM);
        } else {
            blockState = blockState.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(HALF, direction == Direction.UP ? BlockHalf.BOTTOM : BlockHalf.TOP);
        }

        if (ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())) {
            blockState = blockState.with(OPEN, true).with(POWERED, true);
        }

        return blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        if (this.locked) {
            tooltip.add(Text.translatable("tooltip.westerosblocks.locked_trapdoor").formatted(net.minecraft.util.Formatting.RED));
        }
        super.appendTooltip(stack, context, tooltip, options);
    }
} 