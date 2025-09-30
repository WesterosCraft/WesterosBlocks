package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.List;

public class WCBeaconBlock extends Block {
    private static final VoxelShape SHAPE = createBeaconShape();

    private static VoxelShape createBeaconShape() {
        VoxelShape shape = VoxelShapes.empty();
        
        // Outer frame
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0f, 0f, 1f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1f, 0f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(1f, 0f, 0f, 1f, 1f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 1f, 0f, 1f, 1f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 1f, 1f, 1f, 1f));
        
        // Bottom tier
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.125f, 0.125f, 0.1875f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.00625f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.1875f, 0.875f, 0.1875f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.875f, 0.875f, 0.1875f, 0.875f));
        
        // Top tier
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.1875f, 0.1875f, 0.875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.1875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.1875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.8125f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.875f, 0.1875f, 0.8125f, 0.875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.8125f, 0.8125f, 0.875f, 0.8125f));
        
        return shape;
    }

    public WCBeaconBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
    }

    public static class Factory {
        public static Block buildBlockClass(AbstractBlock.Settings settings) {
            return new WCBeaconBlock(settings);
        }
    }
}