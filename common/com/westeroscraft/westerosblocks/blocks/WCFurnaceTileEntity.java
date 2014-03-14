package com.westeroscraft.westerosblocks.blocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.tileentity.TileEntityFurnace;

public class WCFurnaceTileEntity extends TileEntityFurnace {
    private static Method canSmeltMethod;
    
    public WCFurnaceTileEntity() {
        if (canSmeltMethod == null) {
            try {
                canSmeltMethod = TileEntityFurnace.class.getMethod("func_70402_r");
                canSmeltMethod.setAccessible(true);
            } catch (NoSuchMethodException nsmx) {
            }
        }
    }
    
    private boolean canSmelt2() {
        try {
            return (Boolean) canSmeltMethod.invoke(this);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return false;
    }
    
    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;

        if (this.furnaceBurnTime > 0)
        {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.furnaceBurnTime == 0 && this.canSmelt2())
            {
                this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.getStackInSlot(1));

                if (this.furnaceBurnTime > 0)
                {
                    flag1 = true;

                    if (this.getStackInSlot(1) != null)
                    {
                        --this.getStackInSlot(1).stackSize;

                        if (this.getStackInSlot(1).stackSize == 0)
                        {
                            this.setInventorySlotContents(1, this.getStackInSlot(1).getItem().getContainerItemStack(getStackInSlot(1)));
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt2())
            {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime == 200)
                {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    flag1 = true;
                }
            }
            else
            {
                this.furnaceCookTime = 0;
            }

            if (flag != this.furnaceBurnTime > 0)
            {
                flag1 = true;
                WCFurnaceBlock.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1)
        {
            this.onInventoryChanged();
        }
    }
}
