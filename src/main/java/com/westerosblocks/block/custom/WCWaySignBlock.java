package com.westerosblocks.block.custom;

import com.westerosblocks.block.blockentity.WCWaySignBlockEntity;
import com.westerosblocks.util.ModUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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

public class WCWaySignBlock extends FenceMimicBlock {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    
    private final String blockName;
    private final String creativeTab;
    private final WoodType woodType;
    
    public WCWaySignBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, WoodType woodType) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
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
        super.appendProperties(builder);
        builder.add(FACING);
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        BlockState stateAtPos = ctx.getWorld().getBlockState(pos);
        
        // Get the player's facing direction and make the sign face towards the player
        Direction playerFacing = ctx.getHorizontalPlayerFacing();
        Direction signFacing = playerFacing.getOpposite();
        
        // If clicking on a fence post, replace it with our way sign
        if (stateAtPos.getBlock() instanceof FenceBlock && !(stateAtPos.getBlock() instanceof WCWaySignBlock)) {
            // Get the fence connections from the existing fence
            boolean north = stateAtPos.get(FenceBlock.NORTH);
            boolean east = stateAtPos.get(FenceBlock.EAST);
            boolean south = stateAtPos.get(FenceBlock.SOUTH);
            boolean west = stateAtPos.get(FenceBlock.WEST);
            boolean waterlogged = stateAtPos.get(FenceBlock.WATERLOGGED);
            
            // Create our block state with the same fence connections and sign facing towards player
            BlockState newState = this.getDefaultState()
                .with(NORTH, north)
                .with(EAST, east)
                .with(SOUTH, south)
                .with(WEST, west)
                .with(WATERLOGGED, waterlogged)
                .with(FACING, signFacing);
            
            return newState;
        }
        
        // Check for wall placement
        Direction clickedFace = ctx.getSide();
        if (clickedFace.getAxis().isHorizontal()) {
            BlockPos behindPos = pos.offset(clickedFace.getOpposite());
            BlockState behindState = ctx.getWorld().getBlockState(behindPos);
            if (Block.isFaceFullSquare(behindState.getCollisionShape(ctx.getWorld(), behindPos), clickedFace)) {
                // This should redirect to the wall block, but for now we'll handle it here
                return this.getDefaultState().with(FACING, signFacing);
            }
        }
        
        // Otherwise, use normal fence placement logic with sign facing towards player
        BlockState defaultState = super.getPlacementState(ctx);
        if (defaultState != null) {
            return defaultState.with(FACING, signFacing);
        }
        
        return this.getDefaultState().with(FACING, signFacing);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        // Always allow placement - we'll handle fence replacement in getPlacementState
        return true;
    }
    
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, 
                        net.minecraft.entity.LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        
        // Ensure our block is properly placed
        if (!world.isClient) {
            world.setBlockState(pos, state);
        }
    }
    

    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // If support is gone, break the sign
        if (!canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        
        // Add the way sign item to drops
        drops.add(new ItemStack(this));
        
        return drops;
    }
    
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // Handle sign editing interaction
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        
        // For now, just return success - we'll implement sign editing later
        return ActionResult.SUCCESS;
    }
    
    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, 
                          net.minecraft.block.entity.BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        
        // When breaking our way sign, drop the way sign item
        if (!world.isClient) {
            Block.dropStack(world, pos, new ItemStack(this));
        }
    }
    
    // 5x5x16 centered collision box to match the fence post in the model
    private static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(5.5, 0, 5.5, 10.5, 16, 10.5);
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }
} 