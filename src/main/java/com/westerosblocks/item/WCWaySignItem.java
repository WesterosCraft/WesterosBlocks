package com.westerosblocks.item;

import com.westerosblocks.block.custom.WCWaySignBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class WCWaySignItem extends BlockItem {
    
    private final String woodType;
    
    public WCWaySignItem(Block block, Settings settings, String woodType) {
        super(block, settings);
        this.woodType = woodType;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;
        
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        ItemStack stack = context.getStack();
        BlockState state = world.getBlockState(pos);
        Block targetBlock = state.getBlock();
        
        // Check if we're clicking on a fence post
        if (targetBlock instanceof FenceBlock && !(targetBlock instanceof WCWaySignBlock)) {
            // Get the fence connections
            boolean north = state.get(FenceBlock.NORTH);
            boolean east = state.get(FenceBlock.EAST);
            boolean south = state.get(FenceBlock.SOUTH);
            boolean west = state.get(FenceBlock.WEST);
            boolean waterlogged = state.get(FenceBlock.WATERLOGGED);
            
            // Get the player's facing direction and make the sign face towards the player
            Direction playerFacing = context.getHorizontalPlayerFacing();
            Direction signFacing = playerFacing.getOpposite();
            
            // Create our way sign block state with the same connections and sign facing towards player
            BlockState newState = this.getBlock().getDefaultState()
                .with(WCWaySignBlock.NORTH, north)
                .with(WCWaySignBlock.EAST, east)
                .with(WCWaySignBlock.SOUTH, south)
                .with(WCWaySignBlock.WEST, west)
                .with(WCWaySignBlock.WATERLOGGED, waterlogged)
                .with(WCWaySignBlock.FACING, signFacing);
            
            // Replace the fence with our way sign
            if (!world.isClient) {
                world.setBlockState(pos, newState, 3);
                
                // Play placement sound
                world.playSound(player, pos, net.minecraft.sound.SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 
                    1.0F, 1.0F);
                
                // Trigger game event
                world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);
                
                // Consume the item
                if (!player.isCreative()) {
                    stack.decrement(1);
                }
            }
            
            return ActionResult.success(world.isClient);
        }
        
        // Check for wall placement
        Direction clickedFace = context.getSide();
        if (clickedFace.getAxis().isHorizontal()) {
            BlockPos behindPos = pos.offset(clickedFace.getOpposite());
            BlockState behindState = world.getBlockState(behindPos);
            if (Block.isFaceFullSquare(behindState.getCollisionShape(world, behindPos), clickedFace)) {
                // This should redirect to the wall block
                return ActionResult.PASS;
            }
        }
        
        // Otherwise, use normal placement
        return super.useOnBlock(context);
    }
    
    public String getWoodType() {
        return woodType;
    }
} 