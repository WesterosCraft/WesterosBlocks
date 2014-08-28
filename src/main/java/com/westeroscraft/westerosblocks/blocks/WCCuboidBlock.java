package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.CuboidBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

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
    protected WesterosBlockDef.Cuboid currentCuboid = null; // Current rendering cuboid
    protected int cuboidIndex = -1;
    protected Icon sideIcons[][] = new Icon[16][];
    protected WesterosBlockDef.CuboidRotation[] metaRotations = null;
    
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
        boolean tmpset = false;
        if (cuboidIndex < 0) {
            List<WesterosBlockDef.Cuboid> clist = def.getCuboidList(meta);
            if ((clist != null) && (clist.size() > 0)) {    
                currentCuboid = clist.get(0);
            }
            if (currentCuboid == null) {
                return null;
            }
            cuboidIndex = 0;
            tmpset = true;
        }
        Icon ico = getIconInternal(side, meta);

        if (tmpset) {
            currentCuboid = null;
            cuboidIndex = -1;
        }
        return ico;
    }
    @SideOnly(Side.CLIENT)
    protected Icon getIconInternal(int side, int meta) {
        if ((side == 2) || (side == 5)) { // North or East
            if (this.sideIcons[meta] == null) {
                List<WesterosBlockDef.Cuboid> lst = def.getCuboidList(meta);
                if (lst != null) {
                    this.sideIcons[meta] = new Icon[lst.size() * 6];
                }
            }
            if (this.sideIcons[meta][6*cuboidIndex+side] != null) { // North needs shift
                return this.sideIcons[meta][6*cuboidIndex+side];
            }
        }
        int[] sidemap = null;
        if (this.currentCuboid != null) {
            sidemap = this.currentCuboid.sideTextures;
        }
        int nside = side;
        if (sidemap != null) {
            if (nside >= sidemap.length) {
                nside = sidemap.length - 1;
            }
            nside = sidemap[nside];
        }
        Icon ico = def.doStandardIconGet(nside, meta);
        if (side == 2) { // North
            float shft = (1.0F - currentCuboid.xMax) - currentCuboid.xMin;
            if (shft != 0.0F) {
                ico = new ShiftedIcon(ico, shft);
            }
            this.sideIcons[meta][6*cuboidIndex + side] = ico;
        }
        else if (side == 5) { // East
            float shft = (1.0F - currentCuboid.zMax) - currentCuboid.zMin;
            if (shft != 0.0F) {
                ico = new ShiftedIcon(ico, shft);
            }
            this.sideIcons[meta][6*cuboidIndex + side] = ico;
        }
        return ico;
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
//    @Override
//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
//        AxisAlignedBB bb = def.getCollisionBoundingBoxFromPool(world, x, y, z);
//        if (bb == null)
//            bb = super.getCollisionBoundingBoxFromPool(world, x, y, z);
//        return bb;
//    }
//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess blockaccess, int x, int y, int z) {
//        def.setBlockBoundsBasedOnState(this, blockaccess, x, y, z);
//    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        if (currentCuboid != null) {
            switch (side) {
                case 0: // Bottom
                    if (currentCuboid.yMin > 0.0F)
                        return true;
                    break;
                case 1: // Top
                    if (currentCuboid.yMax < 1.0F)
                        return true;
                    break;
                case 2: // Zmin
                    if (currentCuboid.zMin > 0.0F)
                        return true;
                    break;
                case 3: // Zmax
                    if (currentCuboid.zMax < 1.0F)
                        return true;
                    break;
                case 4: // Xmin
                    if (currentCuboid.xMin > 0.0F)
                        return true;
                    break;
                case 5: // Xmax
                    if (currentCuboid.xMax < 1.0F)
                        return true;
                    break;
            }
        }
        return !access.isBlockOpaqueCube(x, y, z);
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
    public void setActiveRenderCuboid(WesterosBlockDef.Cuboid c, RenderBlocks renderer, int meta, int index) {
        if (c != null) {
            this.currentCuboid = c;
        }
        else {
            this.currentCuboid = null;
        }
        cuboidIndex = index;
    }
    /**
     *  Get cuboid list at given meta
     *  @param meta
     */
    public List<WesterosBlockDef.Cuboid> getCuboidList(int meta) {
        List<WesterosBlockDef.Cuboid> rslt = def.getCuboidList(meta);
        if (rslt == null) {
            rslt = Collections.emptyList();
        }
        return rslt;
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
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {
        def.doRandomDisplayTick(world, x, y, z, rnd, metaRotations);
        super.randomDisplayTick(world, x, y, z, rnd);
    }
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        WesterosBlockDef def = this.getWBDefinition();
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 6);
        for (int meta = 0; meta < 16; meta++) {
            List<Cuboid> cl = this.getCuboidList(meta);   
            if (cl == null) continue;
            CuboidBlockModel mod = md.addCuboidModel(this.blockID);
            for (Cuboid c : cl) {
                if (WesterosBlockDef.SHAPE_CROSSED.equals(c.shape)) {   // Crosed
                    mod.addCrossedPatches(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax, c.sideTextures[0]);
                }
                else {
                    mod.addCuboid(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax, c.sideTextures);
                }
            }
            mod.setMetaValue(meta);
        }
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, @SuppressWarnings("rawtypes") List list, Entity entity)
    {
        int meta = world.getBlockMetadata(x,  y,  z);
        List<Cuboid> cl = this.getCuboidList(meta); 
        for (Cuboid c : cl) {
            this.setBlockBounds(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
        }
        def.setBlockBoundsBasedOnState(this, world, x, y, z);
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        int meta = world.getBlockMetadata(x,  y,  z);
        List<Cuboid> cl = this.getCuboidList(meta); 
        MovingObjectPosition bestpos = null;
        double bestdist = 0.0;
        for (Cuboid c : cl) {
            this.setBlockBounds(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax);
            MovingObjectPosition pos = super.collisionRayTrace(world, x, y, z, start, end);
            if (pos != null) {
                if (bestpos == null) {
                    bestpos = pos;
                    bestdist = bestpos.hitVec.squareDistanceTo(end);
                }
                else {
                    double dist = pos.hitVec.squareDistanceTo(end);
                    if (dist > bestdist) {
                        bestpos = pos;
                        bestdist = dist;
                    }
                }
            }
        }
        def.setBlockBoundsBasedOnState(this, world, x, y, z);
        return bestpos;
    }
}
