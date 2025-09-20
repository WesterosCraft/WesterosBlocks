package com.westerosblocks.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class WCSandBlock extends FallingBlock {
    private final int dustColor;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            int dustColor = 14406560; // Default sand dust color
            if (params.length > 0 && params[0] instanceof Integer) {
                dustColor = (Integer) params[0];
            }
            return new WCSandBlock(settings, dustColor);
        }
    }

    public WCSandBlock(AbstractBlock.Settings settings, int dustColor) {
        super(settings);
        this.dustColor = dustColor;
    }

    public WCSandBlock(AbstractBlock.Settings settings) {
        this(settings, 14406560); // Default sand dust color
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return null;
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return this.dustColor;
    }
}
