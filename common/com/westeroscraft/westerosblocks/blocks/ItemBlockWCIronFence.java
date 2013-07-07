package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemBlockWCIronFence extends ItemBlock {
    public ItemBlockWCIronFence(int blockid) {
        super(blockid);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        switch (meta) {
            case 1: // Crossbars
                return WesterosBlocks.blockIronFence.bars_iron_crossbars;
            case 2: // Oxidized
                return WesterosBlocks.blockIronFence.bars_iron_oxidized;
            case 3: // Oxidized crossbars
                return WesterosBlocks.blockIronFence.bars_iron_oxidized_crossbars;
            case 4: // Laundry
                return WesterosBlocks.blockIronFence.laundry_pants_towel;
            case 5: // Axe (east)
                return WesterosBlocks.blockIronFence.axe_left;
            case 6: // Cleaver (north)
                return WesterosBlocks.blockIronFence.cleaver_left;
            case 7: // Dagger (west)
                return WesterosBlocks.blockIronFence.dagger_left;
            case 8: // Pickaxe (south)
                return WesterosBlocks.blockIronFence.pickaxe_left;
            case 9: // Shovel (west)
                return WesterosBlocks.blockIronFence.shovel_left;
        }
        return WesterosBlocks.blockIronFence.getIcon(2, meta);
    }
    public String getUnlocalizedName(ItemStack item)
    {
        int meta = item.getItemDamage();
        switch (meta) {
            case 1:
                return "bars_iron_crossbars";
            case 2:
                return "bars_iron_oxidized";
            case 3:
                return "bars_iron_oxidized_crossbars";
            case 4:
                return "laundry_pants_towel";
            case 5:
                return "axe_east";
            case 6:
                return "cleaver_north";
            case 7:
                return "dagger_west";
            case 8:
                return "pickaxe_south";
            case 9:
                return "shovel_west";
            default:
                return super.getUnlocalizedName(item);
        }
    }
    public int getMetadata(int meta) {
        return meta;
    }
}