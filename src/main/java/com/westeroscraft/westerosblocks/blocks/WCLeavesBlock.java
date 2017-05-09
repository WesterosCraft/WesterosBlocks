package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import org.dynmap.modsupport.BlockSide;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WCLeavesBlock extends BlockLeaves implements IShearable, WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x7);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, null)) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCLeavesBlock(def) };
        }
    }
    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    private static PropertyMeta new_variant = null;
    
    private WesterosBlockDef def;
    private boolean[] nodecay;
    private PropertyMeta variant;
    

    public WCLeavesBlock(WesterosBlockDef def) {
        this.def = def;
        if (def.lightOpacity == WesterosBlockDef.DEF_INT) {
            def.lightOpacity = 1;
        }
        def.doStandardContructorSettings(this);
        setSoundType(def.getStepSound());
        nodecay = new boolean[8];
        for (int i = 0; i < 8; i++) {
            String typ = def.getType(i);
            if ((typ != null) && (typ.contains("no-decay"))) {
                nodecay[i] = true;
            }
        }
        int defmeta = def.getDefinedBaseMeta().get(0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(variant, defmeta).withProperty(CHECK_DECAY, !nodecay[0]).withProperty(DECAYABLE, !nodecay[0]));
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        def.getStandardCreativeItems(this, itemIn, tab, list);
    }

    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
    }

    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(variant));
    }

    // map from state to meta and vice verca - use highest bit for polished boolean, use low 2 bits for variant
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	int off = meta & 0x7;
        return this.getDefaultState().withProperty(variant, off).withProperty(DECAYABLE, !nodecay[off]).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = state.getValue(variant);

        if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        if (new_variant != null) {
            variant = new_variant;
            new_variant = null;
        }
        return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE, variant});
    }

    public int damageDropped(IBlockState state)
    {
        return state.getValue(variant);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(this, 1, world.getBlockState(pos).getValue(variant)));
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
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        String blkname = def.getBlockName(0);
        for (int meta = 0; meta < 8; meta++) {
            Subblock sb = def.getByMeta(meta);
            if (sb == null) continue;
            String topbot = sb.getTextureByIndex(2);
            String sides = sb.getTextureByIndex(3);
            BlockTextureRecord btr = mtd.addBlockTextureRecord(blkname);
            btr.setTransparencyMode(TransparencyMode.TRANSPARENT);
            btr.setMetaValue(meta);
            btr.setMetaValue(meta | 8);
            btr.setSideTexture(topbot, BlockSide.TOP);
            btr.setSideTexture(topbot, BlockSide.BOTTOM);
            btr.setSideTexture(sides, BlockSide.ALLSIDES);
            def.setBlockColorMap(btr, sb);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return Blocks.LEAVES.getBlockLayer();
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return Blocks.LEAVES.isOpaqueCube(state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
    	return null;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
    	return def.colorMultiplier();
    }
    
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

}
