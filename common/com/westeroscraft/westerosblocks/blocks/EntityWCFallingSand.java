package com.westeroscraft.westerosblocks.blocks;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityWCFallingSand extends Entity implements IEntityAdditionalSpawnData
{
    public int blockID;
    public int metadata;

    /** How long the block has been falling for. */
    public int fallTime;
    public boolean shouldDropItem;

    public EntityWCFallingSand(World par1World)
    {
        super(par1World);
        this.shouldDropItem = true;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityWCFallingSand(World par1World, double par2, double par4, double par6, int par8)
    {
        this(par1World, par2, par4, par6, par8, 0);
    }

    public EntityWCFallingSand(World world, double x, double y, double z, int blkid, int meta)
    {
        super(world);
        this.shouldDropItem = true;
        this.blockID = blkid;
        this.metadata = meta;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void entityInit() {}

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        if (this.blockID == 0)
        {
            this.setDead();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.fallTime;
            this.motionY -= 0.04;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.98;
            this.motionY *= 0.98;
            this.motionZ *= 0.98;

            if (!this.worldObj.isRemote)
            {
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.posY);
                int k = MathHelper.floor_double(this.posZ);

                if (this.fallTime == 1)
                {
                    if (this.worldObj.getBlockId(i, j, k) != this.blockID)
                    {
                        this.setDead();
                        return;
                    }

                    this.worldObj.setBlockToAir(i, j, k);
                }

                if (this.onGround)
                {
                    this.motionX *= 0.7;
                    this.motionZ *= 0.7;
                    this.motionY *= -0.5D;

                    if (this.worldObj.getBlockId(i, j, k) != Block.pistonMoving.blockID)
                    {
                        this.setDead();


                        if (this.worldObj.canPlaceEntityOnSide(this.blockID, i, j, k, true, 1, (Entity)null, (ItemStack)null) && !BlockSand.canFallBelow(this.worldObj, i, j - 1, k) && this.worldObj.setBlock(i, j, k, this.blockID, this.metadata, 3))
                        {
                            ((WCSandBlock)Block.blocksList[this.blockID]).onFinishFalling(this.worldObj, i, j, k, this.metadata);
                        }
                        else if (this.shouldDropItem)
                        {
                            this.entityDropItem(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
                        }
                    }
                }
                else if (this.fallTime > 100 && !this.worldObj.isRemote && (j < 1 || j > 256) || this.fallTime > 600)
                {
                    if (this.shouldDropItem)
                    {
                        this.entityDropItem(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
                    }

                    this.setDead();
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("TileID", this.blockID);
        nbt.setByte("Data", (byte)this.metadata);
        nbt.setByte("Time", (byte)this.fallTime);
        nbt.setBoolean("DropItem", this.shouldDropItem);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.blockID = nbt.getInteger("TileID");
        this.metadata = nbt.getByte("Data") & 255;
        this.fallTime = nbt.getByte("Time") & 255;
        this.shouldDropItem = nbt.getBoolean("DropItem");
    }
    
    @Override
    public void func_85029_a(CrashReportCategory par1CrashReportCategory)
    {
        super.func_85029_a(par1CrashReportCategory);
        par1CrashReportCategory.addCrashSection("Immitating block ID", Integer.valueOf(this.blockID));
        par1CrashReportCategory.addCrashSection("Immitating block data", Integer.valueOf(this.metadata));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getShadowSize()
    {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World getWorld()
    {
        return this.worldObj;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @Override
    public boolean canRenderOnFire()
    {
        return false;
    }

    public void writeSpawnData(ByteArrayDataOutput data) {
        data.writeInt(this.blockID);
        data.writeInt(this.metadata);
    }

    public void readSpawnData(ByteArrayDataInput data) {
        this.blockID = data.readInt();
        this.metadata = data.readInt();
    }

}
