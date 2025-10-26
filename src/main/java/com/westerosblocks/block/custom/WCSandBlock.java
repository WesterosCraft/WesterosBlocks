package com.westerosblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Map;

public class WCSandBlock extends FallingBlock {
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

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            int dustColor = (Integer) parameters.getOrDefault("dustColor", 14406560);
            return new WCSandBlock(settings, null, dustColor);
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

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
