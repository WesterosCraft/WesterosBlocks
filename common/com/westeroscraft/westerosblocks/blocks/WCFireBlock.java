package com.westeroscraft.westerosblocks.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;

import org.dynmap.modsupport.ModTextureDefinition;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.*;

public class WCFireBlock extends BlockFire implements WesterosBlockLifecycle, WesterosBlockDynmapSupport
{
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCFireBlock(def) };
        }
    }
    private WesterosBlockDef def;

    protected WCFireBlock(WesterosBlockDef def) {
        super(def.blockID);
        
        this.def = def;
        def.doStandardContructorSettings(this);
        this.setTickRandomly(false);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }


    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4))
        {
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    }

    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @SideOnly(Side.CLIENT)
    public Icon getFireIcon(int side)
    {
        return def.doStandardIconGet(side, 0);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return def.doStandardIconGet(0, 0);
    }
    
    public boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return false;
    }

    public int getChanceToEncourageFire(World world, int x, int y, int z, int oldChance, ForgeDirection face)
    {
        return 0;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
}
