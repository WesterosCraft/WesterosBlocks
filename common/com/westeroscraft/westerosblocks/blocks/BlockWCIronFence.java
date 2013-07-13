package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlocksCreativeTab;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockWCIronFence extends BlockPane {
    public Icon axe_left;
    public Icon axe_right;
    public Icon cleaver_left;
    public Icon cleaver_right;
    public Icon dagger_left;
    public Icon dagger_right;
    public Icon shovel_left;
    public Icon shovel_right;
    public Icon pickaxe_left;
    public Icon pickaxe_right;
    public Icon transparent;
    public Icon bars_iron_crossbars;
    public Icon bars_iron_oxidized;
    public Icon bars_iron_oxidized_crossbars;
    public Icon laundry_pants_towel;
    
    public BlockWCIronFence(int blkid, String sidetexture, String blocktexture,
            Material material, boolean canDropItself) {
        super(blkid, sidetexture, blocktexture, material, canDropItself);

        this.setCreativeTab(WesterosBlocksCreativeTab.tabWesterosDecorative);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        
        axe_left = iconRegister.registerIcon("westerosblocks:axe_east/axe_left");
        axe_right = iconRegister.registerIcon("westerosblocks:axe_east/axe_right");
        cleaver_left = iconRegister.registerIcon("westerosblocks:cleaver_north/cleaver_left");
        cleaver_right = iconRegister.registerIcon("westerosblocks:cleaver_north/cleaver_right");
        dagger_left = iconRegister.registerIcon("westerosblocks:dagger_west/dagger_left");
        dagger_right = iconRegister.registerIcon("westerosblocks:dagger_west/dagger_right");
        pickaxe_left = iconRegister.registerIcon("westerosblocks:pickaxe_south/pickaxe_left");
        pickaxe_right = iconRegister.registerIcon("westerosblocks:pickaxe_south/pickaxe_right");
        shovel_left = iconRegister.registerIcon("westerosblocks:shovel_west/shovel_left");
        shovel_right = iconRegister.registerIcon("westerosblocks:shovel_west/shovel_right");
        transparent = iconRegister.registerIcon("westerosblocks:transparent");
        bars_iron_crossbars = iconRegister.registerIcon("westerosblocks:bars_iron_crossbars/bars_iron_crossbars");
        bars_iron_oxidized = iconRegister.registerIcon("westerosblocks:bars_iron_oxidized/bars_iron_oxidized");
        bars_iron_oxidized_crossbars = iconRegister.registerIcon("westerosblocks:bars_iron_oxidized_crossbars/bars_iron_oxidized_crossbars");
        laundry_pants_towel = iconRegister.registerIcon("westerosblocks:laundry/laundry_pants_towel");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        switch (meta) {
            case 1: // Crossbars
                return bars_iron_crossbars;
            case 2: // Oxidized
                return bars_iron_oxidized;
            case 3: // Oxidized crossbars
                return bars_iron_oxidized_crossbars;
            case 4: // Laundry
                return laundry_pants_towel;
            case 5: // Axe (east)
                return axe_left;
            case 6: // Cleaver (north)
                return cleaver_left;
            case 7: // Dagger (west)
                return dagger_left;
            case 8: // Pickaxe (south)
                return pickaxe_left;
            case 9: // Shovel (west)
                return shovel_left;
            default:
                return super.getIcon(side, meta);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, 0));
        list.add(new ItemStack(id, 1, 1));
        list.add(new ItemStack(id, 1, 2));
        list.add(new ItemStack(id, 1, 3));
        list.add(new ItemStack(id, 1, 4));
        list.add(new ItemStack(id, 1, 5));
        list.add(new ItemStack(id, 1, 6));
        list.add(new ItemStack(id, 1, 7));
        list.add(new ItemStack(id, 1, 8));
        list.add(new ItemStack(id, 1, 9));
    }
    public void registerNames() {
        LanguageRegistry.addName(new ItemStack(this, 1, 0), "Iron Bars");
        LanguageRegistry.addName(new ItemStack(this, 1, 1), "Iron Crossbars");
        LanguageRegistry.addName(new ItemStack(this, 1, 2), "Oxidized Iron Bars");
        LanguageRegistry.addName(new ItemStack(this, 1, 3), "Oxidized Iron Crossbars");
        LanguageRegistry.addName(new ItemStack(this, 1, 4), "Launndry");
        LanguageRegistry.addName(new ItemStack(this, 1, 5), "Axe");
        LanguageRegistry.addName(new ItemStack(this, 1, 6), "Cleaver");
        LanguageRegistry.addName(new ItemStack(this, 1, 7), "Dagger");
        LanguageRegistry.addName(new ItemStack(this, 1, 8), "Pickaxe");
    }
}
