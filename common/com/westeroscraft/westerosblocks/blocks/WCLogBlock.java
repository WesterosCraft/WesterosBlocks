package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraftforge.event.ForgeSubscribe;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCLogBlock extends BlockLog implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(int index, WesterosBlockDef def) {
            // Make sure meta values only for 0-3 (other bits are orientation
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3 }, null)) {
                return null;
            }
            return new WCLogBlock(index, def);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCLogBlock(int def_index, WesterosBlockDef def) {
        super(def.blockID);
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
    public void registerIcons(IconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        int orient = meta & 12;
        int type = meta & 3;
        // Map side to texture index (0=topbottom, 1=side)
        switch (orient) {
            case 0: // Up/down
                if (side < 2)
                    side = 0;
                else
                    side = 1;
                break;
            case 4: // East/west
                if ((side == 4) || (side == 5))
                    side = 0;
                else
                    side = 1;
                break;
            case 8: // North/south
                if ((side == 2) || (side == 3))
                    side = 0;
                else
                    side = 1;
                break;
            case 12: // all sides
                side = 1;
                break;
        }
        return def.doStandardIconGet(side, type);
    }

    @Override
    public int idDropped(int metadata, Random rand, int unused) {
        return blockID;
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
    public int damageDropped(int meta) {
        return meta & 0x3;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
}
