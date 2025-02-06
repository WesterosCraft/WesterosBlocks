package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

public class WCCropBlock extends WCPlantBlock implements ModBlockLifecycle {
    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            // See if we have a state property
            ModBlock.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            Map<String, String> params = ModBlocks.parseBlockParameters(def.getType());

            if (params.containsKey(ModBlock.LAYER_SENSITIVE)) {
                tempLAYERS = Properties.LAYERS;
            }

            AbstractBlock.Settings settings = def.makeBlockSettings().noCollision().breakInstantly();
            Block blk = new WCCropBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    protected WCCropBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings, def);
    }

    private static String[] TAGS = {"crops"};

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
