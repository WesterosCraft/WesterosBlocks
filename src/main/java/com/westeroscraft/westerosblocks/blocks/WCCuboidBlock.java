package com.westeroscraft.westerosblocks.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.dynmap.modsupport.CuboidBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;
import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDef.BoundingBox;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCCuboidBlock(def) };
        }
    }
    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    protected static PropertyMeta new_variant = null;
    
    protected WesterosBlockDef def;
    public PropertyMeta variant;
    
    protected WCCuboidBlock(WesterosBlockDef def) {
        super(def.getMaterial());
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
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        def.getStandardCreativeItems(this, tab, list);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return def.getFireSpreadSpeed(world, pos, face);
    }
    
    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return def.getFlammability(world, pos, face);
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return def.getLightValue(state, world, pos);
    }
    
    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return def.getLightOpacity(state, world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:BlockRenderLayer.CUTOUT);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }    

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }    

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        WesterosBlockDef def = this.getWBDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 6);
        for (int meta = 0; meta < 16; meta++) {
            List<Cuboid> cl = def.getCuboidList(meta);   
            if (cl == null) continue;
            CuboidBlockModel mod = md.addCuboidModel(blkname);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rnd) {
        def.randomDisplayTick(stateIn, worldIn, pos, rnd);
        super.randomDisplayTick(stateIn, worldIn, pos, rnd);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if (new_variant != null) {
            variant = new_variant;
            new_variant = null;
        }
        return new BlockStateContainer(this, new IProperty[] { variant });
    }
    
    // map from state to meta and vice verca - use highest bit for polished boolean, use low 2 bits for variant
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(variant, meta);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer) state.getValue(variant);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {   
        BoundingBox bb = def.getBoundingBox(state, source, pos);
        return bb.getAABB();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {   
        BoundingBox bb = def.getBoundingBox(state, source, pos);
        return bb.getAABB();
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
        List<WesterosBlockDef.Cuboid> cl = this.getCuboidList(meta);
        if (cl == null) cl = Collections.emptyList();
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
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        List<WesterosBlockDef.Cuboid> cl = this.getCuboidList(this.getMetaFromState(state));
        if (cl == null) cl = Collections.emptyList();
        for(WesterosBlockDef.Cuboid bb : cl) {
            AxisAlignedBB aabb = new AxisAlignedBB(bb.xMin, bb.yMin, bb.zMin, bb.xMax, bb.yMax, bb.zMax);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb);
        }
    }
    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        List<WesterosBlockDef.Cuboid> cl = this.getCuboidList(this.getMetaFromState(blockState));
        if (cl == null) cl = Collections.emptyList();

        List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for(WesterosBlockDef.Cuboid bb : cl) {
            AxisAlignedBB aabb = new AxisAlignedBB(bb.xMin, bb.yMin, bb.zMin, bb.xMax, bb.yMax, bb.zMax);
            list.add(this.rayTrace(pos, start, end, aabb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list)
        {
            if (raytraceresult != null)
            {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1)
                {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

}
