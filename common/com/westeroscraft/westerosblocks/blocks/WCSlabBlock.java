package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCSlabBlock extends BlockHalfSlab implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            // Limit to 0-7
            def.setMetaMask(0x7);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, null)) {
                return null;
            }
            if (def.blockIDs.length != 2) {
                WesterosBlocks.log.severe(String.format("Block '%s' does not have a valid number of blockIDs: 2 are required", def.blockName));
                return null;
            }
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
    
    protected WCSlabBlock(WesterosBlockDef def, boolean is_double) {
        super(def.blockIDs[is_double?FULL_IDX:HALF_IDX], is_double, def.getMaterial());
        this.def = def;
        def.doStandardContructorSettings(this, is_double?FULL_IDX:HALF_IDX);
        if (def.lightOpacity < 0) {
            this.setLightOpacity(0);    // Workaround MC's f*cked up lighting exceptions
        }
        if (is_double) {
            this.setCreativeTab(null);
        }
        useNeighborBrightness[blockID] = true;
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        if (this.isDoubleSlab) {
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
    public void registerIcons(IconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
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
    public String getFullSlabName(int meta) {
        return def.getUnlocalizedName(FULL_IDX) + "." + meta; // Get name for full slab
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
    @Override
    public int damageDropped(int meta) {
        return meta;
    }
    @Override
    public int idDropped(int meta, Random rnd, int par3) {
        if (this.isDoubleSlab) {
            return this.otherBlock.blockID;
        }
        else
            return blockID;
    }
    @Override
    public int idPicked(World world, int x, int y, int z) {
        if (!isDoubleSlab)
            return blockID;
        else
            return otherBlock.blockID;
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(blockID, 2, meta);
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
    
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd);
        /* Add models for half slabs */
        if (!this.isDoubleSlab) {
            BoxBlockModel bottom = md.addBoxModel(this.blockID);
            bottom.setYRange(0.0, 0.5);
            BoxBlockModel top = md.addBoxModel(this.blockID);
            top.setYRange(0.5, 1.0);
            for (WesterosBlockDef.Subblock sb : def.subBlocks) {
                bottom.setMetaValue(sb.meta);
                top.setMetaValue(sb.meta | 0x8);
            }
        }
    }

}
