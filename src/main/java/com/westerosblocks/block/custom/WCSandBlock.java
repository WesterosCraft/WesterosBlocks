package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ColorCode;

import java.util.List;

public class WCSandBlock extends ColoredFallingBlock implements ModBlockLifecycle {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.applyCustomProperties();
            Block blk = new WCSandBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), true, false);
        }
    }
    
    private final ModBlock def;
    
    protected WCSandBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(new ColorCode(0xDBCAA0), settings);	// TODO: configurable dust color
        this.def = def;
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }
    
    private static final String[] TAGS = { "sand" };

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
