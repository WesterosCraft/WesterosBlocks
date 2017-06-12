package com.westeroscraft.westerosblocks.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class BlockMsgPacket extends WBPacket {
    public short blockID;
    public byte[] msgdata;

    public BlockMsgPacket() {
        
    }
    
    public BlockMsgPacket(int blkid, byte[] dat) {
        blockID = (short) blkid;
        msgdata = dat;
    }
    
    @Override
    public void readData(ByteBuf data) {
        blockID = data.readShort();
        int cnt = data.readableBytes();
        msgdata = new byte[cnt];
        data.readBytes(msgdata);
    }

    @Override
    public void writeData(ByteBuf data) {
        data.writeShort(blockID);
        data.writeBytes(msgdata);
    }

    @Override
    public void processPacket(INetHandler handler, EntityPlayer player) {
        Block blk = Block.getBlockById(this.blockID);
        if ((blk != null) && (blk instanceof WesterosBlocksMessageDest)) {
            ((WesterosBlocksMessageDest)blk).deliverMessage(handler, player, this.msgdata);
        }
    }
    
    public static void sendBlockMessage(Block blk, EntityPlayerMP player, byte msgid, byte[] data) {
        byte[] blkmsg = new byte[1 + data.length];
        blkmsg[0] = msgid;
        System.arraycopy(data, 0, blkmsg, 1, data.length);
        int blkid = Block.getIdFromBlock(blk);
        BlockMsgPacket pkt = new BlockMsgPacket(blkid, blkmsg);
        WesterosBlocksChannelHandler.sendToPlayer(pkt, player);
    }

}
