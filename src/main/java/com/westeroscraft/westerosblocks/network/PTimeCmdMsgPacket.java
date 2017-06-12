package com.westeroscraft.westerosblocks.network;

import io.netty.buffer.ByteBuf;

public class PTimeCmdMsgPacket extends WBPacket {
	public boolean relative;
	public int time_off;
	
	public PTimeCmdMsgPacket(boolean rel, int timeoff) {
		relative = rel;
		time_off = timeoff;
	}
	
    public PTimeCmdMsgPacket() {
        
    }
        
    @Override
    public void readData(ByteBuf data) {
    	relative = data.readBoolean();
    	time_off = data.readInt();
    }

    @Override
    public void writeData(ByteBuf data) {
    	data.writeBoolean(relative);
    	data.writeInt(time_off);
    }

}
