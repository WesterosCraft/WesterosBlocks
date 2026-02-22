package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;

public class WCLeavesBlock extends LeavesBlock implements WCBlockDef {
    protected BlockDefinition def;
    public final boolean betterFoliage;
    public final boolean overlay;
    public final boolean noDecay;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();

            boolean betterFoliage = definition.hasBetterFoliage();
            boolean overlay = definition.hasOverlay();
            boolean noDecay = definition.isNoDecay();

            return new WCLeavesBlock(settings, definition, betterFoliage, overlay, noDecay);
        }
    }

    protected WCLeavesBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean betterFoliage, boolean overlay, boolean noDecay) {
        super(settings.nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, reader, pos) -> false));
        this.def = def;
        this.betterFoliage = betterFoliage;
        this.overlay = overlay;
        this.noDecay = noDecay;
        setDefaultState(this.getDefaultState().with(DISTANCE, 7).with(PERSISTENT, !noDecay));
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
