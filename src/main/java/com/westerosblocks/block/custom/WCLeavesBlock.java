package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;

import java.util.Map;

public class WCLeavesBlock extends LeavesBlock {
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

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            boolean betterFoliage = (Boolean) parameters.getOrDefault("betterFoliage", false);
            boolean overlay = (Boolean) parameters.getOrDefault("overlay", false);
            boolean noDecay = (Boolean) parameters.getOrDefault("noDecay", false);

            return new WCLeavesBlock(settings, null, betterFoliage, overlay, noDecay);
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

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
