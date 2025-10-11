package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import com.westerosblocks.data.BlockDefinition;

public class WCWallTorchBlock extends WallTorchBlock {

    public static final net.minecraft.state.property.DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private final boolean allowUnsupported;
    private final boolean noParticle;

    public WCWallTorchBlock(AbstractBlock.Settings settings, 
                           boolean allowUnsupported, boolean noParticle) {
        super(getParticle(noParticle), settings);
        this.allowUnsupported = allowUnsupported;
        this.noParticle = noParticle;
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
        if (this.allowUnsupported) {
            return true;
        }
        return super.canPlaceAt(state, world, pos);
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            boolean allowUnsupported = definition != null && definition.isAllowUnsupported();
            boolean noParticle = definition != null && definition.isNoParticle();

            return new WCWallTorchBlock(settings, allowUnsupported, noParticle);
        }
    }
}
