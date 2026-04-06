package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import com.westerosblocks.data.BlockDefinition;

public class WCRailBlock extends RailBlock implements WCBlockDef {
    protected BlockDefinition def;
    private final boolean allowUnsupported;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings().nonOpaque();
            boolean allowUnsupported = definition.isAllowUnsupported();
            return new WCRailBlock(settings, definition, allowUnsupported);
        }
    }

    public WCRailBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean allowUnsupported) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowUnsupported) return true;
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, net.minecraft.world.block.WireOrientation wireOrientation,
                                  boolean notify) {
        if (!this.allowUnsupported) {
            super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
        } else if (!world.isClient() && world.getBlockState(pos).isOf(this)) {
            this.updateBlockState(state, world, pos, sourceBlock);
        }
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}