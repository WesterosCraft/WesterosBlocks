package com.westerosblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.function.BiFunction;

public class WCFlowerbedBlock extends PlantBlock implements Fertilizable {
    protected BlockDefinition def;
    public static final MapCodec<WCFlowerbedBlock> CODEC = createCodec(WCFlowerbedBlock::new);
    public static final int MIN_FLOWERS = 1;
    public static final int MAX_FLOWERS = 4;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty FLOWER_AMOUNT = Properties.FLOWER_AMOUNT;
    private static final BiFunction<Direction, Integer, VoxelShape> FACING_AND_AMOUNT_TO_SHAPE = Util.memoize((facing, flowerAmount) -> {
        VoxelShape[] quarterShapes = new VoxelShape[]{
                Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),  // SE
                Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 3.0, 8.0),   // NE
                Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 3.0, 8.0),    // NW
                Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 3.0, 16.0)    // SW
        };

        VoxelShape combinedShape = VoxelShapes.empty();

        for (int i = 0; i < flowerAmount; i++) {
            int shapeIndex = Math.floorMod(i - facing.getHorizontal(), 4);
            combinedShape = VoxelShapes.union(combinedShape, quarterShapes[shapeIndex]);
        }

        return combinedShape.asCuboid();
    });

    public MapCodec<WCFlowerbedBlock> getCodec() {
        return CODEC;
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            // Handle null definition for manual block creation
            AbstractBlock.Settings settings = definition != null
                    ? definition.makeSettings()
                    : AbstractBlock.Settings.create();
            return new WCFlowerbedBlock(settings, definition);
        }
    }

    public WCFlowerbedBlock(AbstractBlock.Settings settings) {
        this(settings, null);
    }

    public WCFlowerbedBlock(AbstractBlock.Settings settings, BlockDefinition def) {
        super(settings);
        this.def = def;
        this.setDefaultState(this.stateManager.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(FLOWER_AMOUNT, 1));
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return !context.shouldCancelInteraction() 
            && context.getStack().isOf(this.asItem()) 
            && state.get(FLOWER_AMOUNT) < MAX_FLOWERS 
            || super.canReplace(state, context);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FACING_AND_AMOUNT_TO_SHAPE.apply(state.get(FACING), state.get(FLOWER_AMOUNT));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState existingState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (existingState.isOf(this)) {
            return existingState.with(FLOWER_AMOUNT, Math.min(MAX_FLOWERS, existingState.get(FLOWER_AMOUNT) + 1));
        }
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLOWER_AMOUNT);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int currentAmount = state.get(FLOWER_AMOUNT);
        if (currentAmount < MAX_FLOWERS) {
            world.setBlockState(pos, state.with(FLOWER_AMOUNT, currentAmount + 1), Block.NOTIFY_LISTENERS);
        } else {
            dropStack(world, pos, new ItemStack(this));
        }
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
