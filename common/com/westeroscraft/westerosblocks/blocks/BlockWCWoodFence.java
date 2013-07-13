package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlocksCreativeTab;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockWCWoodFence extends BlockFence {
    private Icon icons[] = new Icon[16];
    
    public BlockWCWoodFence(int blockid) {
        super(blockid, "planks_oak", Material.wood);
        
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundWoodFootstep);
        this.setUnlocalizedName("fence");
        
        this.setCreativeTab(WesterosBlocksCreativeTab.tabWesterosDecorative);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        icons[0] = iconRegister.registerIcon("westerosblocks:fence_planks_oak/planks_oak");
        icons[1] = iconRegister.registerIcon("westerosblocks:fence_planks_spruce/planks_spruce");
        icons[2] = iconRegister.registerIcon("westerosblocks:fence_planks_birch/planks_birch");
        icons[3] = iconRegister.registerIcon("westerosblocks:fence_planks_jungle/planks_jungle");
        icons[4] = iconRegister.registerIcon("westerosblocks:fence_bark_oak/bark_oak");
        icons[5] = iconRegister.registerIcon("westerosblocks:fence_bark_spruce/bark_spruce");
        icons[6] = iconRegister.registerIcon("westerosblocks:fence_bark_birch/bark_birch");
        icons[7] = iconRegister.registerIcon("westerosblocks:fence_bark_jungle/bark_jungle");
        icons[8] = iconRegister.registerIcon("westerosblocks:fence_vines_oak/planks_oak_vines");
        icons[9] = iconRegister.registerIcon("westerosblocks:fence_vines_spruce/planks_spruce_vines");
        icons[10] = iconRegister.registerIcon("westerosblocks:fence_vines_birch/planks_birch_vines");
        icons[11] = iconRegister.registerIcon("westerosblocks:fence_vines_jungle/planks_jungle_vines");
        icons[12] = iconRegister.registerIcon("westerosblocks:fence_grapevines_oak/planks_oak_grapevines");
        icons[13] = iconRegister.registerIcon("westerosblocks:fence_grapevines_spruce/planks_spruce_grapevines");
        icons[14] = iconRegister.registerIcon("westerosblocks:fence_grapevines_birch/planks_birch_grapevines");
        icons[15] = iconRegister.registerIcon("westerosblocks:fence_grapevines_jungle/planks_jungle_grapevines");
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
    
    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }
    
    public void registerNames() {
        LanguageRegistry.addName(new ItemStack(this, 1, 0), "Oak Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 1), "Spruce Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 2), "Birch Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 3), "Jungle Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 4), "Oak Bark Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 5), "Spruce Bark Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 6), "Birch Bark Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 7), "Jungle Bark Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 8), "Oak Vines Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 9), "Spruce Vines Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 10), "Birch Vines Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 11), "Jungle Vines Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 12), "Oak Grapvines Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 13), "Spruce Grapevines Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 14), "Birch Grapevines Fence");
        LanguageRegistry.addName(new ItemStack(this, 1, 15), "Jungke Grapevines Fence");
    }
}