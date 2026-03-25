package com.westerosblocks.item.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class WCBlockItem extends BlockItem {

    public WCBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        // Get block name from registry
        String blockName = Registries.BLOCK.getId(getBlock()).getPath();

        // Look up definition and add tooltips if present
        BlockDefinition def = BlockDefinitionRegistry.getInstance().getDefinition(blockName);
        if (def != null && def.hasTooltips()) {
            int index = 0;
            for (BlockDefinition.TooltipEntry entry : def.getTooltips()) {
                String key = "tooltip.westerosblocks." + blockName + "." + index;
                Formatting format = Formatting.byName(entry.getFormat());
                if (format == null) {
                    format = Formatting.GRAY;
                }
                tooltip.add(Text.translatable(key).formatted(format));
                index++;
            }
        }

        // Add "Cyclable" tooltip for toggleOnUse blocks
        if (def != null && def.toggleOnUse()) {
            int count = def.getStateCount();
            tooltip.add(Text.literal(count + " cyclable variants").formatted(Formatting.GOLD));
        }
    }
}
