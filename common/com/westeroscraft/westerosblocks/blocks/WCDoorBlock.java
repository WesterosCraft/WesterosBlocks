package com.westeroscraft.westerosblocks.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCDoorBlock extends BlockDoor implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            // Validate meta : we require meta 0, and only allow it
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCDoorBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    private Icon[] upper;
    private Icon[] lower;
    
    protected WCDoorBlock(WesterosBlockDef def) {
        super(def.blockID, def.getMaterial());
        this.def = def;
        def.doStandardContructorSettings(this);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, null, new WCDoorItem(this, this.def), 0);
        
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        def.doStandardRegisterIcons(iconRegister);
        upper = new Icon[2];
        lower = new Icon[2];
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
    public Icon getIcon(int side, int meta)
    {
        return this.lower[0];
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess blkAccess, int x, int y, int z, int side) {
        if (side != 1 && side != 0) {
            int meta = this.getFullMetadata(blkAccess, x, y, z);
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
    public int idDropped(int meta, Random par2Random, int par3)
    {
        return (meta & 8) != 0 ? 0 : def.blockID;
    }
    
    @SideOnly(Side.CLIENT)
    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return def.blockID;
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
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFireSpreadSpeed(world, x, y, z, 0, face);
    }
    
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFlammability(world, x, y, z, 0, face);
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return def.getLightValue(world, x, y, z, 0);
    }
    
    @Override
    public int getLightOpacity(World world, int x, int y, int z) {
        return def.getLightOpacity(world, x, y, z, 0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor() {
        return def.getBlockColor();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int meta) {
        return def.getRenderColor(0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        return def.colorMultiplier(access, x, y, z, 0x0);
    }
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(def.blockID, 1, 0);
    }
    @Override
    public void getSubBlocks (int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    }
}
