package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.util.StringIdentifiable;

public class WCArrowSlitBlock extends Block implements ModBlockLifecycle {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.applyCustomProperties();
            Block blk = new WCArrowSlitBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    private final ModBlock def;
    public static final EnumProperty<ArrowSlitType> TYPE = EnumProperty.of("type", ArrowSlitType.class);

    protected WCArrowSlitBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
        setDefaultState(getDefaultState().with(TYPE, ArrowSlitType.SINGLE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            boolean hasTop = world.getBlockState(pos.up()).isOf(this);
            boolean hasBottom = world.getBlockState(pos.down()).isOf(this);
            
            ArrowSlitType newType;
            if (hasTop && hasBottom) {
                newType = ArrowSlitType.MID;
            } else if (hasTop) {
                newType = ArrowSlitType.BOTTOM;
            } else if (hasBottom) {
                newType = ArrowSlitType.TOP;
            } else {
                newType = ArrowSlitType.SINGLE;
            }
            
            return state.with(TYPE, newType);
        }
        return state;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        // The getStateForNeighborUpdate method will handle state updates for neighboring blocks
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // The getStateForNeighborUpdate method will handle state updates for neighboring blocks
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    @Override
    public String[] getBlockTags() {
        return new String[0];
    }

    public enum ArrowSlitType implements StringIdentifiable {
        SINGLE("single"),
        TOP("top"),
        BOTTOM("bottom"),
        MID("mid");

        private final String name;

        ArrowSlitType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
} 