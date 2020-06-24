package com.westeroscraft.westerosblocks.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.properties.IProperty;
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

import com.google.common.collect.Lists;
import com.westeroscraft.westerosblocks.WesterosBlockDef.BoundingBox;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WCSolidBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCSolidBlock(def) };
        }
    }
    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    protected static PropertyMeta new_variant = null;
    
    protected WesterosBlockDef def;
    private boolean isSolidOpaque = true;
    
    private PropertyMeta variant;

    protected WCSolidBlock(WesterosBlockDef def) {
        super(def.getMaterial());
        this.isSolidOpaque = !def.nonOpaque;
        this.def = def;
        def.doStandardContructorSettings(this);
        setSoundType(def.getSoundType());
    }
    @Override
    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    @Override
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
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:(isSolidOpaque?BlockRenderLayer.SOLID:BlockRenderLayer.CUTOUT));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return isSolidOpaque;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return !def.hasCollisionBoxes();
    }    

    @Override
    public boolean isFullBlock(IBlockState state) {
        return !def.hasCollisionBoxes();
    }    

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, 0, (def.alphaRender ? TransparencyMode.TRANSPARENT : TransparencyMode.OPAQUE));
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
    
    // map from state to meta and vice versa
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(variant, meta);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(variant).intValue();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
    	return def.colorMultiplier();
    }
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, state.getValue(variant).intValue());
    }
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(variant).intValue();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        if (def.hasCollisionBoxes()) {
            List<BoundingBox> boxes = def.getCollisionBoxList(this.getMetaFromState(state));
            for (BoundingBox bb : boxes) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, bb.getAABB());
            }
        }
        else {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {  
        if (def.hasCollisionBoxes()) {
            BoundingBox bb = def.getBoundingBox(state, source, pos);
            if (bb != null) {
                return bb.getAABB();
            }
        }
        return FULL_BLOCK_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {   
        if (def.hasCollisionBoxes()) {
            BoundingBox bb = def.getBoundingBox(state, source, pos);
            if (bb != null) {
                return bb.getAABB();
            }
        }
        return FULL_BLOCK_AABB;
    }

    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        if (def.hasCollisionBoxes() == false) {
            return super.collisionRayTrace(blockState, worldIn, pos, start, end);
        }
        List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for (BoundingBox bb : def.getCollisionBoxList(this.getMetaFromState(blockState))) {
            list.add(this.rayTrace(pos, start, end, bb.getAABB()));
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
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        Block connector = world.getBlockState(pos.offset(facing)).getBlock();
        return connector instanceof BlockWall || connector instanceof BlockFence || connector instanceof BlockPane;
    }

    @Override
    public SoundType getSoundType(IBlockState blockState, World world, BlockPos blockPos, @Nullable Entity entity) {
        return def.getSoundType(blockState.getBlock().getMetaFromState(blockState));
    }

    @SuppressWarnings("deprecation")
    @Override
    public Material getMaterial(IBlockState blockState) {
        return def.getMaterial(blockState.getBlock().getMetaFromState(blockState));
    }

}
