package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.BoundingBox;
import com.westeroscraft.westerosblocks.items.WCLayerItem;

public class WCLayerBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            new_layers = PropertyInteger.create("layers", 1, getLayerCount(def));

            return new Block[] { new WCLayerBlock(def) };
        }
    }
    
    private static int getLayerCount(WesterosBlockDef def) {
        int layerCount = 8;
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
        return layerCount;
    }
    
    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    protected static PropertyInteger new_layers = null;

    private WesterosBlockDef def;
    public int layerCount;
    public PropertyInteger layers;
    
    protected WCLayerBlock(WesterosBlockDef def) {
        super(def.getMaterial());
        this.def = def;
        this.layerCount = getLayerCount(def);
        def.doStandardContructorSettings(this);
        for (int i = 0; i < layerCount; i++) {
            setBlockBoundsForMeta(i);
        }
    }
    
    private void setBlockBoundsForMeta(int meta) {
        int j = meta % layerCount;
        float f = (float)(1 + j) / (float)layerCount;
        def.setBoundingBox(meta, 0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, WCLayerItem.class);
        
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        def.getStandardCreativeItems(this, itemIn, tab, list);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override 
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return ((Integer)state.getValue(layers)) + 1;
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
    public boolean blocksMovement(IBlockAccess worldIn, BlockPos pos)
    {
        return ((Integer)worldIn.getBlockState(pos).getValue(layers)).intValue() <= (layerCount/2);
    }

    @Override
    public boolean isFullyOpaque(IBlockState state)
    {
        return ((Integer)state.getValue(layers)).intValue() == layerCount;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        int i = ((Integer)blockState.getValue(layers)).intValue() - 1;
        float f = 1.0F / layerCount;
        AxisAlignedBB axisalignedbb = blockState.getBoundingBox(worldIn, pos);
        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, (double)((float)i * f), axisalignedbb.maxZ);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:BlockRenderLayer.SOLID);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {   
        BoundingBox bb = def.getBoundingBox(state, source, pos);
        return new AxisAlignedBB(bb.xMin, bb.yMin, bb.zMin, bb.xMax, bb.yMax, bb.zMax);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rnd) {
        def.randomDisplayTick(stateIn, worldIn, pos, rnd);
        super.randomDisplayTick(stateIn, worldIn, pos, rnd);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        if (new_layers != null) {
            layers = new_layers;
            new_layers = null;
        }
        return new BlockStateContainer(this, new IProperty[] { layers });
    }
    
    // map from state to meta and vice verca - use highest bit for polished boolean, use low 2 bits for variant
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(layers, meta + 1);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer) state.getValue(layers) - 1;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, 0, TransparencyMode.TRANSPARENT);
        /* Make models for each layer thickness */
        for (int i = 0; i < layerCount; i++) {
            BoxBlockModel mod = md.addBoxModel(blkname);
            mod.setYRange(0.0, (double)(i+1) / (double) layerCount);
            mod.setMetaValue(i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }
}
