package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.WallFenceBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

public class WCFenceBlock extends BlockFence implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCFenceBlock(def) };
        }
    }
    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    protected static PropertyMeta new_variant = null;
    
    private WesterosBlockDef def;
    private PropertyMeta variant;

    protected WCFenceBlock(WesterosBlockDef def) {
        super(def.getMaterial(), def.getMaterial().getMaterialMapColor());
        this.def = def;
        def.doStandardContructorSettings(this);
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
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:BlockRenderLayer.CUTOUT);
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
        return new BlockStateContainer(this, new IProperty[] { BlockFence.NORTH, BlockFence.EAST,
                BlockFence.WEST, BlockFence.SOUTH, variant });
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
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 3);
        // Get plant model, and set for all defined meta
        WallFenceBlockModel pbm = md.addWallFenceModel(blkname, WallFenceBlockModel.FenceType.FENCE);
        for (WesterosBlockDef.Subblock sb : def.subBlocks) {
            pbm.setMetaValue(sb.meta);
        }
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

}
