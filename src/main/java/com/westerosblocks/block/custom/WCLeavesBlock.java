package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class WCLeavesBlock extends LeavesBlock implements ModBlockLifecycle {
    protected ModBlock def;
    public final boolean betterFoliage;
    public final boolean overlay;

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            if (def.lightOpacity == ModBlock.DEF_INT) {
                def.lightOpacity = 1;
            }
            AbstractBlock.Settings settings = def.applyCustomProperties().nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, reader, pos) -> false);
            Block blk = new WCLeavesBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), true, true);
        }
    }

    protected WCLeavesBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
        String typ = def.getType();
        betterFoliage = (typ != null) && typ.contains("better-foliage");
        overlay = (typ != null) && typ.contains("overlay");
        boolean nodecay = (typ != null) && typ.contains("no-decay");
        setDefaultState(this.getDefaultState().with(DISTANCE, 7).with(PERSISTENT, !nodecay));
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    private static final String[] TAGS = {"leaves"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }
}
