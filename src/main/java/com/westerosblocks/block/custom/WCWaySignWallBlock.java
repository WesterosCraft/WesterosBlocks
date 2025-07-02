package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WoodType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import java.util.List;

public class WCWaySignWallBlock extends Block {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    
    // Wall sign shapes (flat against walls)
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 8, 0, 16, 13, 1);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0, 8, 15, 16, 13, 16);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(15, 8, 0, 16, 13, 16);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0, 8, 0, 1, 13, 16);
    
    private final String blockName;
    private final String creativeTab;
    private final WoodType woodType;
    
    public WCWaySignWallBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, WoodType woodType) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
        setDefaultState(getDefaultState()
            .with(FACING, Direction.NORTH));
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
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        if (direction.getAxis() == Direction.Axis.Y) {
            return null; // Can't place on top/bottom faces
        }
        return this.getDefaultState()
            .with(FACING, direction);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction facing = state.get(FACING);
        BlockPos behindPos = pos.offset(facing.getOpposite());
        BlockState behindState = world.getBlockState(behindPos);
        return Block.isFaceFullSquare(behindState.getCollisionShape(world, behindPos), facing);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForDirection(state.get(FACING));
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForDirection(state.get(FACING));
    }
    
    private VoxelShape getShapeForDirection(Direction facing) {
        switch (facing) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return NORTH_SHAPE;
        }
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }
    
    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, 
                          net.minecraft.block.entity.BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (!world.isClient) {
            Block.dropStack(world, pos, new ItemStack(this));
        }
    }
} 