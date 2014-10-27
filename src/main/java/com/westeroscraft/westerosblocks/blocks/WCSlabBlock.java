package com.westeroscraft.westerosblocks.blocks;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.items.WCSlabItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCSlabBlock extends BlockSlab implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            // Limit to 0-7
            def.setMetaMask(0x7);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, null)) {
                return null;
            }
            def.setBlockIDCount(2); // 2 block IDs - half, full
            WCSlabBlock half = new WCSlabBlock(def, false);
            WCSlabBlock full = new WCSlabBlock(def, true);
            half.otherBlock = full;
            full.otherBlock = half;
            return new Block[] { half, full };
        }
    }

    private static final int HALF_IDX = 0;
    private static final int FULL_IDX = 1;
    
    private WesterosBlockDef def;
    private WCSlabBlock otherBlock;
    private static BitSet halfSlabs = new BitSet();
    
    protected WCSlabBlock(WesterosBlockDef def, boolean is_double) {
        super(is_double, def.getMaterial());
        this.def = def;
        if (def.lightOpacity == WesterosBlockDef.DEF_INT) {
            def.lightOpacity = 255;
        }
        def.doStandardContructorSettings(this, is_double?FULL_IDX:HALF_IDX);
        if (is_double) {
            this.setCreativeTab(null);
        }
        else {
            //WesterosBlocks.slabStyleLightingBlocks.set(def.blockID);
            //halfSlabs.set(def.blockID);
            //NOTYET useNeighborBrightness[def.blockID] = true;
        }
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        if (this.field_150004_a) { // isDoubleSlab
            WCSlabItem.setSlabs(otherBlock, this);
            def.doStandardRegisterActions(this, WCSlabItem.class, null, FULL_IDX);
        }
        else {
            WCSlabItem.setSlabs(this, otherBlock);
            def.doStandardRegisterActions(this, WCSlabItem.class, null, HALF_IDX);
        }
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return def.doStandardIconGet(side, meta);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itm, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, Item.getIdFromItem(itm), tab, list);
    }
    
    @Override
    public String func_150002_b(int meta) { // getFullSlabName
        return def.getBlockName(FULL_IDX) + "." + meta; // Get name for full slab
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return def.getFireSpreadSpeed(world, x, y, z, face);
    }
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return def.getFlammability(world, x, y, z, face);
    }
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return def.getLightValue(world, x, y, z);
    }
    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return def.getLightOpacity(this, world, x, y, z);
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
    @Override
    public int damageDropped(int meta) {
        return meta;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        if (!field_150004_a)
            return Item.getItemFromBlock(this);
        else
            return Item.getItemFromBlock(otherBlock);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(Item.getItemFromBlock(this), 2, meta & 7);
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
    
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (this.field_150004_a)
        {
            return super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
        }
        else if (par5 != 1 && par5 != 0 && !super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5))
        {
            return false;
        }
        else
        {
            int i1 = par2 + Facing.offsetsXForSide[Facing.oppositeSide[par5]];
            int j1 = par3 + Facing.offsetsYForSide[Facing.oppositeSide[par5]];
            int k1 = par4 + Facing.offsetsZForSide[Facing.oppositeSide[par5]];
            boolean flag = (par1IBlockAccess.getBlockMetadata(i1, j1, k1) & 8) != 0;
            return flag ? (par5 == 0 ? true : (par5 == 1 && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) ? true : !isBlockSingleSlab(par1IBlockAccess.getBlock(par2, par3, par4)) || (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) == 0)) : (par5 == 1 ? true : (par5 == 0 && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) ? true : !isBlockSingleSlab(par1IBlockAccess.getBlock(par2, par3, par4)) || (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) != 0));
        }
    }

    @SideOnly(Side.CLIENT)

    private static boolean isBlockSingleSlab(Block id)
    {
        return (id == Blocks.stone_slab) || (id == Blocks.wooden_slab) || halfSlabs.get(Block.getIdFromBlock(id));
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        
        /* Add models for half slabs */
        if (!this.field_150004_a) {
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

}
