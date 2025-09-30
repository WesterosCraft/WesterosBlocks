package com.westerosblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.westerosblocks.data.BlockDefinition;
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
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            // TODO: Add getDustColor() getter to BlockDefinition
            int dustColor = 14406560; // Default sand dust color
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
