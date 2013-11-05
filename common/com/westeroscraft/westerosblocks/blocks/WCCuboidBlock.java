package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            return new Block[] { new WCCuboidBlock(def) };
        }
    }
    
    protected WesterosBlockDef def;
    protected WesterosBlockDef.Cuboid currentCuboid; // Current rendering cuboid
    
    protected WCCuboidBlock(WesterosBlockDef def) {
        super(def.blockID, def.getMaterial());
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
        int[] sidemap = null;
        if (this.currentCuboid != null) {
            sidemap = this.currentCuboid.sideTextures;
        }
        if (sidemap != null) {
            if (side >= sidemap.length) {
                side = sidemap.length - 1;
            }
            side = sidemap[side];
        }
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
        return meta;
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        AxisAlignedBB bb = def.getCollisionBoundingBoxFromPool(world, x, y, z);
        if (bb == null)
            bb = super.getCollisionBoundingBoxFromPool(world, x, y, z);
        return bb;
    }
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockaccess, int x, int y, int z) {
        def.setBlockBoundsBasedOnState(this, blockaccess, x, y, z);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        if (currentCuboid != null) {
            switch (side) {
                case 0: // Bottom
                    return (currentCuboid.yMin > 0.0F);
                case 1: // Top
                    return (currentCuboid.yMax < 1.0F);
                case 2: // Zmin
                    return (currentCuboid.zMin > 0.0F);
                case 3: // Zmax
                    return (currentCuboid.zMax < 1.0F);
                case 4: // Xmin
                    return (currentCuboid.xMin > 0.0F);
                case 5: // Xmax
                    return (currentCuboid.xMax < 1.0F);
                default:
                    return true;
            }
       }
        return true;
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
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return WesterosBlocks.cuboidRenderID;
    }
    
    /**
     * Set active cuboid during render
     */
    public void setActiveRenderCuboid(WesterosBlockDef.Cuboid c, RenderBlocks renderer, int meta) {
        if (c != null) {
            this.currentCuboid = c;
        }
        else {
            this.currentCuboid = null;
        }
    }
    /**
     *  Get cuboid list at given meta
     *  @param meta
     */
    public List<WesterosBlockDef.Cuboid> getCuboidList(int meta) {
        return def.getCuboidList(meta);
    }
    
    public void setBoundingBoxFromCuboidList(int meta) {
        List<WesterosBlockDef.Cuboid> cl = getCuboidList(meta);
        float xmin = 100.0F, ymin = 100.0F, zmin = 100.0F;
        float xmax = -100.0F, ymax = -100.0F, zmax = -100.0F;
        for(WesterosBlockDef.Cuboid c : cl) {
            if (c.xMin < xmin) xmin = c.xMin;
            if (c.yMin < ymin) ymin = c.yMin;
            if (c.zMin < zmin) zmin = c.zMin;
            if (c.xMax > xmax) xmax = c.xMax;
            if (c.yMax > ymax) ymax = c.yMax;
            if (c.zMax > zmax) zmax = c.zMax;
        }
        def.setBoundingBox(meta, xmin, ymin, zmin, xmax, ymax, zmax);
    }
}
