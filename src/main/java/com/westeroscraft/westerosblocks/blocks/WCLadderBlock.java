package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
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

public class WCLadderBlock extends BlockLadder implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x3);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3 }, new int[] { 0 })) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCLadderBlock(def) };
        }
    }
    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    protected static PropertyMeta new_variant = null;

    private WesterosBlockDef def;
    private boolean allow_unsupported[] = new boolean[4];
    private boolean no_climb[] = new boolean[4];
    private PropertyMeta variant;
    private static EnumFacing[] orientation = { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH };

    protected WCLadderBlock(WesterosBlockDef def) {
        this.def = def;
        def.doStandardContructorSettings(this);
        for (int i = 0; i < 4; i++) {
            String t = def.getType(i);
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                    if (tok.equals("allow-unsupported")) {
                        allow_unsupported[i] = true;
                    }
                    if (tok.equals("no-climb")) {
                    	no_climb[i] = true;
                    }
                }
            }
        }
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
        return new BlockStateContainer(this, new IProperty[] { FACING, variant });
    }
    
    // map from state to meta and vice verca - use highest bit for polished boolean, use low 2 bits for variant
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = orientation[meta >> 2];

        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(variant, meta & 3);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	int idx = (Integer) state.getValue(variant);
    	EnumFacing f = state.getValue(FACING);
    	for (int i = 0; i < orientation.length; i++) {
    		if (orientation[i] == f) {
    			idx += (4 * i);
    			break;
    		}
    	}
    	return idx;
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
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 1);
        /* Make base model */
        PatchBlockModel mod = md.addPatchModel(blkname);
        String patch0 = mod.addPatch(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, SideVisible.BOTH);
        /* Make rotated models */
        PatchBlockModel mod90 = md.addPatchModel(blkname);
        mod90.addRotatedPatch(patch0, 0, 90, 0);
        PatchBlockModel mod180 = md.addPatchModel(blkname);
        mod180.addRotatedPatch(patch0, 0, 180, 0);
        PatchBlockModel mod270 = md.addPatchModel(blkname);
        mod270.addRotatedPatch(patch0, 0, 270, 0);
        
        for (WesterosBlockDef.Subblock sb : def.subBlocks) {
            mod.setMetaValue(sb.meta);
            mod90.setMetaValue(sb.meta + 8);
            mod180.setMetaValue(sb.meta + 4);
            mod270.setMetaValue(sb.meta + 12);
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	IBlockState state = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    	if (state != null) {
    		state = state.withProperty(variant, meta & 0x3);
    	}
    	return state;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing facing)
    {
        IBlockState bs = worldIn.getBlockState(pos);
        if ((bs.getBlock() == this) && (allow_unsupported[bs.getValue(variant).intValue()])) {
            return true;
        }
        return super.canPlaceBlockOnSide(worldIn, pos, facing);
    }
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        IBlockState bs = worldIn.getBlockState(pos);
        if ((bs.getBlock() == this) && (allow_unsupported[bs.getValue(variant).intValue()])) {
            return;
        }
    	super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

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
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
    	return !no_climb[state.getValue(variant).intValue()];
	}
}
