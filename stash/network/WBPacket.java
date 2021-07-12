package com.westeroscraft.westerosblocks.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

public abstract class WBPacket {
    public abstract void readData(ByteBuf data);

    public abstract void writeData(ByteBuf data);
    
    public abstract void processPacket(INetHandler netHandler, EntityPlayer player);
}
