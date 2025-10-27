package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import com.westerosblocks.data.BlockDefinition;

public class WCTorchBlock extends TorchBlock {
    protected BlockDefinition def;
    private final boolean allowUnsupported;
    private final boolean noParticle;
    private final Block wallBlock;

    public WCTorchBlock(AbstractBlock.Settings settings, BlockDefinition def, Block wallBlock,
            boolean allowUnsupported, boolean noParticle) {
        super(getParticle(noParticle), settings);
        this.def = def;
        this.wallBlock = wallBlock;
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                BlockPos attachPos = pos.offset(opposite);
                if (world.getBlockState(attachPos).isSideSolidFullSquare(world, attachPos, direction)) {
                    return this.wallBlock.getDefaultState()
                            .with(WCWallTorchBlock.FACING, direction);
                }
            }
        }

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

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();

            // wallBlock will be set later by ModBlocks after wall torch is registered
            Block wallBlock = null;
            boolean allowUnsupported = definition.isAllowUnsupported();
            boolean noParticle = definition.isNoParticle();

            return new WCTorchBlock(settings, definition, wallBlock, allowUnsupported, noParticle);
        }
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
