package com.westerosblocks.block.custom;

import java.util.List;

import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.particle.ModParticles;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WCParticleBlock extends Block implements ModBlockLifecycle {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);
    private final ModBlock def;

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.applyCustomProperties();
            Block blk = new WCParticleBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk),  false,  true);
        }
    }

    public WCParticleBlock(Settings settings, ModBlock def) {
            super(settings);
            this.def = def;
        }

        @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (def.particle != null) {
            ParticleEffect particle = ModParticles.get(def.particle);

            if (particle != null && random.nextFloat() < 0.7f) {
                // Center the particle spawn in the block
                double x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
                double y = pos.getY() + 0.5 + random.nextFloat() * 0.2;
                double z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.2;

                double velocityX = (random.nextFloat() - 0.5) * 0.02;
                double velocityY = 0.05 + random.nextFloat() * 0.02;
                double velocityZ = (random.nextFloat() - 0.5) * 0.02;

                world.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);
            }
        }
    }

        @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }

        @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    private static final String[] TAGS = {};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
