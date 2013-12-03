package com.westeroscraft.westerosblocks.blocks;


import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCStairBlock extends BlockStairs implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if ((def.modelBlockName == null) || (def.modelBlockMeta < 0)) {
                WesterosBlocks.log.severe("Type 'stair' requires modelBlockName and modelBlockMeta settings");
                return null;
            }
            // Try to find model block
            Block blk = WesterosBlocks.findBlockByName(def.modelBlockName);
            if (blk == null) {
                WesterosBlocks.log.severe(String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
                return null;
            }
            // Validate meta : we require meta 0, and only allow it
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }

            return new Block[] { new WCStairBlock(def, blk) };
        }
    }
    
    private WesterosBlockDef def;
    private Icon offsetIconXN = null;
    private Icon offsetIconXP = null;
    private Icon offsetIconZN = null;
    private Icon offsetIconZP = null;
    
    protected WCStairBlock(WesterosBlockDef def, Block blk) {
        super(def.blockID, blk, def.modelBlockMeta);
        this.def = def;
        this.setCreativeTab(def.getCreativeTab());
        this.setUnlocalizedName(def.blockName);
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
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        Icon ico = super.getIcon(side, meta);
        if (side == 2) {
            if(this.getBlockBoundsMaxX() == 0.5) {
                if (offsetIconXP == null) {
                    offsetIconXP = new ShiftedIcon(ico, true);
                }
                ico = offsetIconXP;
            }
            else if(this.getBlockBoundsMinX() == 0.5) {
                if (offsetIconXN == null) {
                    offsetIconXN = new ShiftedIcon(ico, false);
                }
                ico = offsetIconXN;
            }
        }
        else if (side == 5) {
            if (this.getBlockBoundsMaxZ() == 0.5) {
                if (offsetIconZP == null) {
                    offsetIconZP = new ShiftedIcon(ico, true);
                }
                ico = offsetIconZP;
            }
            else if (this.getBlockBoundsMinZ() == 0.5) {
                if (offsetIconZN == null) {
                    offsetIconZN = new ShiftedIcon(ico, false);
                }
                ico = offsetIconZN;
            }
        }
        return ico;
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

}
