package com.westerosblocks.block.custom;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.List;

/**
 * Standalone wall torch block that doesn't depend on the def system.
 * Provides the same functionality as WCWallTorchBlock but with direct configuration.
 */
public class StandaloneWallTorchBlock extends WallTorchBlock {

    public static final net.minecraft.state.property.DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private final boolean allowUnsupported;
    private final boolean noParticle;
    private final String translationKey;
    private final List<String> tooltips;

    /**
     * Creates a new standalone wall torch block.
     * 
     * @param settings Block settings
     * @param allowUnsupported Whether this torch can be placed without support
     * @param noParticle Whether this torch should emit particles
     * @param translationKey The translation key for this block
     * @param tooltips Optional tooltips to display
     */
    public StandaloneWallTorchBlock(AbstractBlock.Settings settings, 
                                   boolean allowUnsupported, boolean noParticle,
                                   String translationKey, List<String> tooltips) {
        super(getParticle(noParticle), settings);
        this.allowUnsupported = allowUnsupported;
        this.noParticle = noParticle;
        this.translationKey = translationKey;
        this.tooltips = tooltips;
    }

    private static SimpleParticleType getParticle(boolean noParticle) {
        if (noParticle) {
            return null; 
        }
        return ParticleTypes.FLAME;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!this.noParticle) {
            super.randomDisplayTick(state, world, pos, random);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowUnsupported) return true;
        return super.canPlaceAt(state, world, pos);
    }

    private static final String[] TAGS = {"wall_post_override"};

    public String[] getBlockTags() {
        return TAGS;
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