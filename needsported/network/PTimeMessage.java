package com.westerosblocks.needsported.network;

import net.minecraft.network.FriendlyByteBuf;

import com.westeroscraft.westerosblocks.WesterosBlocks;

public class PTimeMessage {
	public static int PTIME_MSGID = 0x01;
	public boolean relative; // Indicates whether time_off is fixed (false) or relative (true)
	public int time_off; // Absolute time in day, or offset, in ticks

	public PTimeMessage(boolean rel, int timeoff) {
		relative = rel;
		time_off = timeoff;
	}

	public PTimeMessage() {

	}

	public static PTimeMessage decode(FriendlyByteBuf buf) {
		try {
			return new PTimeMessage(buf.readBoolean(), buf.readInt());
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			WesterosBlocks.log.warn("Exception while reading PTimeMessage: " + e);
			return null;
		}
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(this.relative);
		buf.writeInt(this.time_off);
	}

	@Override
	public String toString() {
		return "PTimeMessage[relative=" + relative + ",time_off=" + time_off + "]";
	}
}