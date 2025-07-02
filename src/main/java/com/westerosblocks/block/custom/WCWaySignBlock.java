package com.westerosblocks.block.custom;

import com.westerosblocks.util.ModUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WoodType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
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

public class WCWaySignBlock extends Block {
    // Simple fence post shape like FenceMimicBlock
    private static final VoxelShape SHAPE = Block.createCuboidShape(5, 0, 5, 11, 16, 11);
    private static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(5, 0, 5, 11, 24, 11);
    
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
        // No properties needed - simple like Supplementaries
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        BlockState stateAtPos = ctx.getWorld().getBlockState(pos);
        
        // If clicking on a fence post, we'll replace it entirely
        if (stateAtPos.getBlock() instanceof FenceBlock) {
            // Return our block state to replace the fence post
            BlockState newState = this.getDefaultState();
            
            // Force the replacement by setting the block state immediately
            if (!ctx.getWorld().isClient) {
                ctx.getWorld().setBlockState(pos, newState);
            }
            
            return newState;
        }
        
        // Check for wall placement
        Direction clickedFace = ctx.getSide();
        if (clickedFace.getAxis().isHorizontal()) {
            BlockPos behindPos = pos.offset(clickedFace.getOpposite());
            BlockState behindState = ctx.getWorld().getBlockState(behindPos);
            if (Block.isFaceFullSquare(behindState.getCollisionShape(ctx.getWorld(), behindPos), clickedFace)) {
                // This should redirect to the wall block, but for now we'll handle it here
                return this.getDefaultState();
            }
        }
        
        // Otherwise, place normally
        return this.getDefaultState();
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        // Check if there's a fence post at the current position (replace it)
        BlockState currentState = world.getBlockState(pos);
        if (currentState.getBlock() instanceof FenceBlock) {
            return true;
        }
        
        // Allow placement on solid ground as fallback
        BlockState groundState = world.getBlockState(pos.down());
        if (groundState.isSolidBlock(world, pos.down())) {
            return true;
        }
        
        return super.canPlaceAt(state, world, pos);
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
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
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, 
                          net.minecraft.block.entity.BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        
        // When breaking our way sign, drop the way sign item
        if (!world.isClient) {
            Block.dropStack(world, pos, new ItemStack(this));
        }
    }
} 