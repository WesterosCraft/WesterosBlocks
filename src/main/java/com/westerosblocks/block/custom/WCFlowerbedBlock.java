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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WCFlowerbedBlock extends PlantBlock implements Fertilizable, WCBlockDef {
    protected BlockDefinition def;
    public static final MapCodec<WCFlowerbedBlock> CODEC = createCodec(WCFlowerbedBlock::new);
    public static final int MIN_FLOWERS = 1;
    public static final int MAX_FLOWERS = 4;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty FLOWER_AMOUNT = Properties.FLOWER_AMOUNT;

    // Precomputed shapes: [facingHorizontalIndex][flowerAmount - 1]
    private static final VoxelShape[][] SHAPES = makeAllShapes();

    private static VoxelShape[][] makeAllShapes() {
        VoxelShape[] quarterShapes = new VoxelShape[]{
                Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),  // SE
                Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 3.0, 8.0),   // NE
                Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 3.0, 8.0),    // NW
                Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 3.0, 16.0)    // SW
        };

        VoxelShape[][] shapes = new VoxelShape[4][4];
        for (Direction facing : Direction.Type.HORIZONTAL) {
            for (int amount = 1; amount <= 4; amount++) {
                VoxelShape combinedShape = VoxelShapes.empty();
                for (int i = 0; i < amount; i++) {
                    int shapeIndex = Math.floorMod(i - facing.getHorizontal(), 4);
                    combinedShape = VoxelShapes.union(combinedShape, quarterShapes[shapeIndex]);
                }
                shapes[facing.getHorizontal()][amount - 1] = combinedShape.asCuboid();
            }
        }
        return shapes;
    }

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
        return SHAPES[state.get(FACING).getHorizontal()][state.get(FLOWER_AMOUNT) - 1];
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
