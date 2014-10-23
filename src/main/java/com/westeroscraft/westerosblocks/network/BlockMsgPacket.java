package com.westeroscraft.westerosblocks.network;

import io.netty.buffer.ByteBuf;

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

}
