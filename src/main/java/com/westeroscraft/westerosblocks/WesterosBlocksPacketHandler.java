package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class WesterosBlocksPacketHandler implements IPacketHandler {
    public static final String CHANNEL = "WesterosBlocks";

    public static final byte BLOCKMSG = 0x00;   // Command code for block message
    
    @Override
    public void onPacketData(INetworkManager manager,
            Packet250CustomPayload packet, Player player) {
        // Not our channnel?
        if (!packet.channel.equals(CHANNEL)) {
            return;
        }
        if (packet.data.length < 1) {
            return;
        }
        System.out.println("onPacketData(" + packet.data[0] + "," + packet.data.length + ")");

        switch (packet.data[0]) {   // Look at first byte for message type
            case BLOCKMSG:          // Block message
                if (packet.data.length < 3) {
                    return;
                }
                int blkid = ((0xFF & (int) packet.data[1]) << 8) | (0xFF & (int) packet.data[2]);   // Get block ID;
                Block blk = Block.blocksList[blkid];
                if ((blk != null) && (blk instanceof WesterosBlocksMessageDest)) {
                    System.out.println("deliverMessage(" + blkid + ")");
                    ((WesterosBlocksMessageDest)blk).deliverMessage(manager, player, packet.data, 3);
                }
                break;
        }
    }
    
    public static void sendBlockMessage(Block blk, EntityPlayerMP player, byte msgid, byte[] data) {
        byte[] buf = new byte[3 + 1 + data.length];
        buf[0] = BLOCKMSG;
        buf[1] = (byte) ((blk.blockID >> 8) & 0xFF);
        buf[2] = (byte) (blk.blockID & 0xFF);
        buf[3] = msgid;
        System.arraycopy(data, 0, buf, 4, data.length);
        Packet250CustomPayload pkt = PacketDispatcher.getPacket(CHANNEL, buf);
        player.playerNetServerHandler.sendPacketToPlayer(pkt);
        System.out.println("sendBlockMessge(" + blk.blockID + ", " + player.getEntityName() + "," + msgid);
    }
}
