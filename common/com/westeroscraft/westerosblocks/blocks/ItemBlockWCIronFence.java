package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;

public class ItemBlockWCIronFence extends MultiBlockItem {
    public ItemBlockWCIronFence(int blockid) {
        super(blockid);
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
}