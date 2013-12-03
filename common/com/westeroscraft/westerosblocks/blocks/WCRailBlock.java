package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCRailBlock extends BlockRail implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCRailBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCRailBlock(WesterosBlockDef def) {
        super(def.blockID);
        this.def = def;
        def.doStandardContructorSettings(this);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        if (meta >= 6)
            return def.doStandardIconGet(1, 0);
        else
            return def.doStandardIconGet(0, 0);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, id, tab, list);
    }
    @SuppressWarnings("rawtypes")
    @Override
    public void addCreativeItems(ArrayList itemList) {
        def.getStandardCreativeItems(this, itemList);
    }
    @Override
    public int damageDropped(int meta) {
        return 0;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFireSpreadSpeed(world, x, y, z, metadata, face);
    }
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFlammability(world, x, y, z, metadata, face);
    }
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return def.getLightValue(world, x, y, z);
    }
    @Override
    public int getLightOpacity(World world, int x, int y, int z) {
        return def.getLightOpacity(world, x, y, z);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor() {
        return def.getBlockColor();
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int meta)
    {
        return def.getRenderColor(meta);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        return def.colorMultiplier(access, x, y, z);
    }
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     * Modified from BlockRailBase: don't break when support blocks break
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            int meta = par1World.getBlockMetadata(par2, par3, par4);

            this.func_94358_a(par1World, par2, par3, par4, meta, meta, par5);
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {
        def.doRandomDisplayTick(world, x, y, z, rnd);
        super.randomDisplayTick(world, x, y, z, rnd);
    }
}
