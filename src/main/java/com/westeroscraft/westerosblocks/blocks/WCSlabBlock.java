package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
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
import com.westeroscraft.westerosblocks.items.WCSlabItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

public class WCSlabBlock extends BlockSlab implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            // Limit to 0-7
            def.setMetaMask(0x7);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, null)) {
                return null;
            }
            PropertyMeta new_var = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            def.setBlockIDCount(2); // 2 block IDs - half, full
            glob_is_double = false;
            new_variant = new_var;
            WCSlabBlock half = new WCSlabBlock(def, false);
            glob_is_double = true;
            new_variant = new_var;
            WCSlabBlock full = new WCSlabBlock(def, true);
            glob_is_double = false;
            half.otherBlock = full;
            full.otherBlock = half;
            return new Block[] { half, full };
        }
    }
    private static PropertyMeta new_variant;
    private static boolean glob_is_double;

    private static final int HALF_IDX = 0;
    private static final int FULL_IDX = 1;
    
    private WesterosBlockDef def;
    private WCSlabBlock otherBlock;
    private boolean is_double;
    private PropertyMeta variant;
    
    protected WCSlabBlock(WesterosBlockDef def, boolean is_double) {
        super(def.getMaterial());
        this.is_double = is_double;
        this.def = def;
        if (def.lightOpacity == WesterosBlockDef.DEF_INT) {
            def.lightOpacity = 255;
        }
        def.doStandardContructorSettings(this, is_double?FULL_IDX:HALF_IDX);

        IBlockState iblockstate = this.blockState.getBaseState();
        if (is_double) {
            this.setCreativeTab(null);
        }
        else {
            useNeighborBrightness = true;
            iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(iblockstate.withProperty(variant, variant.fromMeta(0)));
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        if (this.is_double) { // isDoubleSlab
            WCSlabItem.setSlabs(otherBlock, this);
            def.doStandardRegisterActions(this, WCSlabItem.class, FULL_IDX);
        }
        else {
            WCSlabItem.setSlabs(this, otherBlock);
            def.doStandardRegisterActions(this, WCSlabItem.class, HALF_IDX);
        }
        
        return true;
    }

    @Override
    public IProperty<?> getVariantProperty()
    {
        return this.variant;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        if (isDouble())
        	return this.getDefaultState().withProperty(this.variant, (meta & 7));
        else
        	return this.getDefaultState().withProperty(this.variant, (meta & 7)).withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        Integer var = state.getValue(this.variant);
        if (isDouble())
        	return var;
        return (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP ? 8 : 0) + var;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
    	return state.getValue(variant).intValue();
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (itemIn != Item.getItemFromBlock(this.is_double?this:this.otherBlock)) {
    		def.getStandardCreativeItems(this, itemIn, tab, list);
        }
    }
    
    @Override
    public String getUnlocalizedName(int meta)
    {
        return "tile." + def.getBlockName(HALF_IDX) + "_" + (meta & 7); // Get name for full slab
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
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
    	return def.colorMultiplier();
    }
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        
        /* Add models for half slabs */
        if (!this.is_double) {
            String blkname = def.getBlockName(HALF_IDX);
            def.defaultRegisterTextureBlock(mtd, HALF_IDX, TransparencyMode.SEMITRANSPARENT);
            BoxBlockModel bottom = md.addBoxModel(blkname);
            bottom.setYRange(0.0, 0.5);
            BoxBlockModel top = md.addBoxModel(blkname);
            top.setYRange(0.5, 1.0);
            for (WesterosBlockDef.Subblock sb : def.subBlocks) {
                bottom.setMetaValue(sb.meta);
                top.setMetaValue(sb.meta | 0x8);
            }
        }
        else {
            def.defaultRegisterTextureBlock(mtd, FULL_IDX, null);
        }
    }
    
    @Override
    public boolean isDouble() {
    	return is_double | glob_is_double;
    }

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return variant.fromMeta(stack.getMetadata() & 7);
	}
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	if (new_variant != null) {
    		this.variant = new_variant;
    		new_variant = null;
    	}
    	if (isDouble()) {
            return new BlockStateContainer(this, new IProperty[] { this.variant });
    	}
    	else {
    		return new BlockStateContainer(this, new IProperty[] { HALF, this.variant });
    	}
    }
    
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (net.minecraftforge.common.ForgeModContainer.disableStairSlabCulling)
            return super.doesSideBlockRendering(state, world, pos, face);

        return state.isOpaqueCube();
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    	if (this.isDouble())
            return Item.getItemFromBlock(this.otherBlock);
    	else
            return Item.getItemFromBlock(this);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack((this.isDouble()?this.otherBlock:this), 1, state.getValue(variant).intValue());
    }
}
