package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;

import java.util.Map;

public class WCLeavesBlock extends LeavesBlock {
    public final boolean betterFoliage;
    public final boolean overlay;
    public final boolean noDecay;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            boolean betterFoliage = definition != null && definition.hasBetterFoliage();
            boolean overlay = definition != null && definition.hasOverlay();
            boolean noDecay = definition != null && definition.isNoDecay();

            return new WCLeavesBlock(settings, betterFoliage, overlay, noDecay);
        }
    }

    protected WCLeavesBlock(AbstractBlock.Settings settings, boolean betterFoliage, boolean overlay, boolean noDecay) {
        super(settings.nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, reader, pos) -> false));
        this.betterFoliage = betterFoliage;
        this.overlay = overlay;
        this.noDecay = noDecay;
        setDefaultState(this.getDefaultState().with(DISTANCE, 7).with(PERSISTENT, !noDecay));
    }
}
