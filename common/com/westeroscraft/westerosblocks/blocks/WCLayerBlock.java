package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCLayerBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCLayerBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    int layerCount;
    
    protected WCLayerBlock(WesterosBlockDef def) {
        super(def.blockID, def.getMaterial());
        this.def = def;
        def.doStandardContructorSettings(this);
        layerCount = 8;
        int off = def.type.indexOf("cnt:");
        if (off >= 0) {
            try {
                layerCount = Integer.parseInt(def.type.substring(off+4));
            } catch (NumberFormatException nfx) {
                WesterosBlocks.log.info("Error parsing 'cnt:' in " + def.blockName);
            }
        }
        if (layerCount < 2) layerCount = 2;
        if (layerCount > 16) layerCount = 16;
        
        setBlockBoundsForLayerDepth(0);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        WCLayerItem.block = this;
        def.doStandardRegisterActions(this, WCLayerItem.class);
        
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
        return def.doStandardIconGet(side, meta);
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
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int l = 1 + (par1World.getBlockMetadata(par2, par3, par4) % layerCount);
        float f = 1F / (float) layerCount;
        return AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)((float)par3 + (float)l * f), (double)par4 + this.maxZ);
    }
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockaccess, int x, int y, int z) {
        setBlockBoundsForLayerDepth(blockaccess.getBlockMetadata(x,  y, z));
    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        return side == 1 ? true : super.shouldSideBeRendered(access, x, y, z, side);
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
    private void setBlockBoundsForLayerDepth(int meta) {
        int j = meta % layerCount;
        float f = (float)(1 + j) / (float)layerCount;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int l = world.getBlockId(x, y - 1, z);        
        Block block = Block.blocksList[l];
        if (block == null) { // Invalid/empty block below
            return false;
        }
        if (block == this) { // Full block of our type below
            return true;
        }
        if (!block.isLeaves(world, x, y - 1, z) && !Block.blocksList[l].isOpaqueCube()) {   // If no leaves and not opaque blow, cannot place
            return false;
        }
        boolean rslt = world.getBlockMaterial(x, y - 1, z).blocksMovement(); // If blocks movement, can place
        return rslt;
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        this.canLayerStay(par1World, par2, par3, par4);
    }

    private boolean canLayerStay(World par1World, int par2, int par3, int par4) {
        if (!this.canPlaceBlockAt(par1World, par2, par3, par4)) {
            par1World.setBlockToAir(par2, par3, par4);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
        par1World.setBlockToAir(par3, par4, par5);
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return (meta % layerCount) + 1;
    }
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {
        def.doRandomDisplayTick(world, x, y, z, rnd);
        super.randomDisplayTick(world, x, y, z, rnd);
    }
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd);
        /* Make models for each layer thickness */
        for (int i = 0; i < layerCount; i++) {
            BoxBlockModel mod = md.addBoxModel(this.blockID);
            mod.setYRange(0.0, (double)(i+1) / (double) layerCount);
            mod.setMetaValue(i);
        }
    }
}
