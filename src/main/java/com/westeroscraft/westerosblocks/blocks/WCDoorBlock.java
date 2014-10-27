package com.westeroscraft.westerosblocks.blocks;

import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.items.WCDoorItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCDoorBlock extends BlockDoor implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            // Validate meta : we require meta 0, and only allow it
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCDoorBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    private IIcon[] upper;
    private IIcon[] lower;
    
    protected WCDoorBlock(WesterosBlockDef def) {
        super(def.getMaterial());
        this.def = def;
        def.doStandardContructorSettings(this);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, WCDoorItem.class);
        
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        def.doStandardRegisterIcons(iconRegister);
        upper = new IIcon[2];
        lower = new IIcon[2];
        upper[0] = def.doStandardIconGet(0,  0);
        lower[0] = def.doStandardIconGet(1,  0);
        upper[1] = new IconFlipped(upper[0], true, false);
        lower[1] = new IconFlipped(lower[0], true, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public IIcon getIcon(int side, int meta)
    {
        return this.lower[0];
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public IIcon getIcon(IBlockAccess blkAccess, int x, int y, int z, int side) {
        if (side != 1 && side != 0) {
            int meta = this.func_150012_g(blkAccess, x, y, z); // getFullMetadata
            int direction = meta & 3;
            boolean flag = (meta & 4) != 0;
            boolean flag1 = false;
            boolean flag2 = (meta & 8) != 0;

            if (flag) {
                if (direction == 0 && side == 2) {
                    flag1 = !flag1;
                } else if (direction == 1 && side == 5) {
                    flag1 = !flag1;
                } else if (direction == 2 && side == 3) {
                    flag1 = !flag1;
                } else if (direction == 3 && side == 4) {
                    flag1 = !flag1;
                }
            } else {
                if (direction == 0 && side == 5) {
                    flag1 = !flag1;
                } else if (direction == 1 && side == 3) {
                    flag1 = !flag1;
                } else if (direction == 2 && side == 4) {
                    flag1 = !flag1;
                } else if (direction == 3 && side == 2) {
                    flag1 = !flag1;
                }

                if ((meta & 16) != 0) {
                    flag1 = !flag1;
                }
            }

            return flag2 ? this.upper[flag1 ? 1 : 0]
                    : this.lower[flag1 ? 1 : 0];
        } else {
            return this.lower[0];
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItemDropped(int meta, Random par2Random, int par3)
    {
        return (meta & 8) != 0 ? null : Item.getItemFromBlock(this);
    }
    
    @Override
    public int damageDropped(int meta) {
        return 0;
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
    public int getRenderColor(int meta) {
        return def.getRenderColor(meta);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        return def.colorMultiplier(access, x, y, z);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item itm, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, Item.getIdFromItem(itm), tab, list);
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
        WesterosBlockDef def = this.getWBDefinition();
        int blkid = Block.getIdFromBlock(this);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 2);
        // Get door model, and set for all defined meta
        md.addDoorModel(blkid);
   }

}
