package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WCTileEntitySound extends TileEntity
{
    // Index of sound in blocks sound list
    public byte soundIndex;

    public boolean previousRedstoneState;

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setByte("idx", this.soundIndex);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.soundIndex = nbt.getByte("idx");
    }

    /**
     * change sound selection
     */
    public void changeSoundSelection(WCSoundBlock sb, int meta)
    {
        List<String> sndids = sb.getWBDefinition().getSoundIDList(meta);
        if ((sndids == null) || sndids.isEmpty()) {
            this.soundIndex = (byte)-1;
        }
        else {
            this.soundIndex = (byte)((this.soundIndex + 1) % sndids.size());
        }
        this.onInventoryChanged();
    }

    /**
     * plays the stored note
     */
    public void triggerSound(WCSoundBlock sb, World world, int x, int y, int z)
    {
        world.addBlockEvent(x, y, z, sb.blockID, 0, this.soundIndex);
    }
}
