package com.westerosblocks.block.custom;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.List;

/**
 * Standalone torch block that doesn't depend on the def system.
 * Provides the same functionality as WCTorchBlock but with direct configuration.
 */
public class StandaloneTorchBlock extends TorchBlock {

    private final boolean allowUnsupported;
    private final boolean noParticle;
    private final Block wallBlock;
    private final String translationKey;
    private final List<String> tooltips;

    /**
     * Creates a new standalone torch block.
     * 
     * @param settings Block settings
     * @param wallBlock The corresponding wall torch block
     * @param allowUnsupported Whether this torch can be placed without support
     * @param noParticle Whether this torch should emit particles
     * @param translationKey The translation key for this block
     * @param tooltips Optional tooltips to display
     */
    public StandaloneTorchBlock(AbstractBlock.Settings settings, Block wallBlock, 
                               boolean allowUnsupported, boolean noParticle,
                               String translationKey, List<String> tooltips) {
        super(getParticle(noParticle), settings);
        this.wallBlock = wallBlock;
        this.allowUnsupported = allowUnsupported;
        this.noParticle = noParticle;
        this.translationKey = translationKey;
        this.tooltips = tooltips;
    }

    private static SimpleParticleType getParticle(boolean noParticle) {
        if (noParticle) {
            return FabricParticleTypes.simple(false);
        }
        return ParticleTypes.FLAME;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        // Check for wall placement first
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                BlockPos attachPos = pos.offset(opposite);
                if (world.getBlockState(attachPos).isSideSolidFullSquare(world, attachPos, direction)) {
                    return this.wallBlock.getDefaultState()
                            .with(StandaloneWallTorchBlock.FACING, direction);
                }
            }
        }

        // If not wall placement, return normal torch state
        return state;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!this.noParticle) {
            super.randomDisplayTick(state, world, pos, random);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowUnsupported) {
            return true;
        }
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        if (tooltips != null && !tooltips.isEmpty()) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.literal(tooltipText));
            }
        }
        super.appendTooltip(stack, context, tooltip, options);
    }
} 