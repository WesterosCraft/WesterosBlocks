package com.westeroscraft.westerosblocks.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

public interface WesterosBlocksMessageDest {
    /**
     * Handle message received
     * 
     * @param handler - network manager
     * @param player - player sending the message (if from client)
     * @param msg - message buffer
     * @param msgoff - offset of start of message in buffer
     */
    public void deliverMessage(INetHandler handler, EntityPlayer player, byte[] msg);
}
