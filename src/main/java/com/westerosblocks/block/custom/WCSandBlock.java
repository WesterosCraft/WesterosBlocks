package com.westerosblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class WCSandBlock extends FallingBlock implements WCBlockDef {
    protected BlockDefinition def;
    private final int dustColor;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            // TODO: Add getDustColor() getter to BlockDefinition
            int dustColor = 14406560; // Default sand dust color
            return new WCSandBlock(settings, definition, dustColor);
        }
    }

    public WCSandBlock(AbstractBlock.Settings settings, BlockDefinition def, int dustColor) {
        super(settings);
        this.def = def;
        this.dustColor = dustColor;
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return null;
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return this.dustColor;
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
