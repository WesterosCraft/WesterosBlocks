package com.westerosblocks.block.custom;

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
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            boolean betterFoliage = false;
            boolean overlay = false;
            boolean noDecay = false;
            
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                betterFoliage = (Boolean) paramMap.getOrDefault("betterFoliage", false);
                overlay = (Boolean) paramMap.getOrDefault("overlay", false);
                noDecay = (Boolean) paramMap.getOrDefault("noDecay", false);
            }
            
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
