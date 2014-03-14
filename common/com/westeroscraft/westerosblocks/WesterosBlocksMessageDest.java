package com.westeroscraft.westerosblocks;

import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public interface WesterosBlocksMessageDest {
    /**
     * Handle message received
     * 
     * @param manager - network manager
     * @param player - player sending the message (if from client)
     * @param msg - message buffer
     * @param msgoff - offset of start of message in buffer
     */
    public void deliverMessage(INetworkManager manager, Player player, byte[] msg, int msgoff);
}
