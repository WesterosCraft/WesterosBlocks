package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlocksCreativeTab;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockWCCobblestone extends Block {
    private Icon icons[] = new Icon[15];
    
    public BlockWCCobblestone(int blockid) {
        super(blockid, Material.rock);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundStoneFootstep);
        this.setUnlocalizedName("stonebrick");
        this.setCreativeTab(WesterosBlocksCreativeTab.tabWesterosBlocks);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        icons[0] = this.blockIcon;
        icons[1] = iconRegister.registerIcon("WesterosBlocks:stone_bisque/stone_large_bisque");
        icons[2] = iconRegister.registerIcon("WesterosBlocks:stone_darkkhaki/stone_large_darkkhaki");
        icons[3] = iconRegister.registerIcon("WesterosBlocks:stone_brown/stone_large_brown");
        icons[4] = iconRegister.registerIcon("WesterosBlocks:stone_beige/stone_large_beige");
        icons[5] = iconRegister.registerIcon("WesterosBlocks:stone_floralwhite/stone_large_floralwhite");
        icons[6] = iconRegister.registerIcon("WesterosBlocks:stone_ghostwhite/stone_large_ghostwhite");
        icons[7] = iconRegister.registerIcon("WesterosBlocks:stone_dimgrey/stone_large_dimgrey");
        icons[8] = iconRegister.registerIcon("WesterosBlocks:stone_papayawhip/stone_large_papayawhip");
        icons[9] = iconRegister.registerIcon("WesterosBlocks:stone_silver/stone_small_silver");
        icons[10] = iconRegister.registerIcon("WesterosBlocks:stone_burlywood/stone_small_burlywood");
        icons[11] = iconRegister.registerIcon("WesterosBlocks:stone_honeydew/stone_small_honeydew");
        icons[12] = iconRegister.registerIcon("WesterosBlocks:stone_peru/stone_large_peru");
        icons[13] = iconRegister.registerIcon("WesterosBlocks:stone_goldenrod/stone_large_goldenrod");
        icons[14] = iconRegister.registerIcon("WesterosBlocks:stone_antiquewhite/stone_small_antiquewhite");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        if (meta >= icons.length) meta = 0;
        return icons[meta];
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
        for (int i = 0; i < icons.length; i++) {
            list.add(new ItemStack(id, 1, i));
        }
    }
    public void registerNames() {
        LanguageRegistry.addName(new ItemStack(this, 1, 0), "Cobblestone");
        LanguageRegistry.addName(new ItemStack(this, 1, 1), "Bisque Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 2), "Dark Khaki Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 3), "Brown Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 4), "Beige Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 5), "Floral White Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 6), "Ghost White Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 7), "Dim Grey Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 8), "Papayawhip Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 9), "Silver Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 10), "Burlywood Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 11), "Honeydew Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 12), "Peru Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 13), "Goldenrod Stone");
        LanguageRegistry.addName(new ItemStack(this, 1, 14), "Antique White Stone");
    }
    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }
}