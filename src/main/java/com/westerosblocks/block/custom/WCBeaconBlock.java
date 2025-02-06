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
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

public class WCBeaconBlock extends WCCuboidBlock implements ModBlockLifecycle {
    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCBeaconBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    private static final ModBlock.Cuboid[] cuboids = {
            new ModBlock.Cuboid(0f, 0f, 0f, 0f, 1f, 1f, new int[]{0, 1, 2, 3, 4, 5}),
            new ModBlock.Cuboid(0f, 0f, 0f, 1f, 0f, 1f, new int[]{0, 1, 2, 3, 4, 5}),
            new ModBlock.Cuboid(0f, 0f, 0f, 1f, 1f, 0f, new int[]{0, 1, 2, 3, 4, 5}),
            new ModBlock.Cuboid(1f, 0f, 0f, 1f, 1f, 1f, new int[]{0, 1, 2, 3, 4, 5}),
            new ModBlock.Cuboid(0f, 1f, 0f, 1f, 1f, 1f, new int[]{0, 1, 2, 3, 4, 5}),
            new ModBlock.Cuboid(0f, 0f, 1f, 1f, 1f, 1f, new int[]{0, 1, 2, 3, 4, 5}),
            new ModBlock.Cuboid(0.125f, 0.00625f, 0.125f, 0.125f, 0.1875f, 0.875f, new int[]{6, 7, 8, 9, 10, 11}),
            new ModBlock.Cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.00625f, 0.875f, new int[]{6, 7, 8, 9, 10, 11}),
            new ModBlock.Cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.125f, new int[]{6, 7, 8, 9, 10, 11}),
            new ModBlock.Cuboid(0.875f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.875f, new int[]{6, 7, 8, 9, 10, 11}),
            new ModBlock.Cuboid(0.125f, 0.00625f, 0.1875f, 0.875f, 0.1875f, 0.875f, new int[]{6, 7, 8, 9, 10, 11}),
            new ModBlock.Cuboid(0.125f, 0.00625f, 0.875f, 0.875f, 0.1875f, 0.875f, new int[]{6, 7, 8, 9, 10, 11}),
            new ModBlock.Cuboid(0.1875f, 0.1875f, 0.1875f, 0.1875f, 0.875f, 0.8125f, new int[]{12, 13, 14, 15, 16, 17}),
            new ModBlock.Cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.1875f, 0.8125f, new int[]{12, 13, 14, 15, 16, 17}),
            new ModBlock.Cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.1875f, new int[]{12, 13, 14, 15, 16, 17}),
            new ModBlock.Cuboid(0.8125f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.8125f, new int[]{12, 13, 14, 15, 16, 17}),
            new ModBlock.Cuboid(0.1875f, 0.875f, 0.1875f, 0.8125f, 0.875f, 0.8125f, new int[]{12, 13, 14, 15, 16, 17}),
            new ModBlock.Cuboid(0.1875f, 0.1875f, 0.8125f, 0.8125f, 0.875f, 0.8125f, new int[]{12, 13, 14, 15, 16, 17})
    };
    private static final List<ModBlock.Cuboid> cuboidlist = Arrays.asList(cuboids);

    protected WCBeaconBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings, def, 1);
        def.cuboids = cuboidlist;
        this.cuboid_by_facing[0] = cuboidlist;
        SHAPE_BY_INDEX[0] = getBoundingBoxFromCuboidList(this.cuboid_by_facing[0]);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }
}
