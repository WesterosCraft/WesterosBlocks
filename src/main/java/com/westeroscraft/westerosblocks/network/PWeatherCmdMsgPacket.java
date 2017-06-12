package com.westeroscraft.westerosblocks.network;

import com.westeroscraft.westerosblocks.commands.PTimeCommand;
import com.westeroscraft.westerosblocks.commands.PWeatherCommand;
import com.westeroscraft.westerosblocks.commands.PWeatherCommand.PWEATHER_SETTING;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class PWeatherCmdMsgPacket extends WBPacket {
	private PWEATHER_SETTING setting = PWEATHER_SETTING.RESET;
	
	public PWeatherCmdMsgPacket(PWEATHER_SETTING setting) {
		this.setting = setting;
	}
	
    public PWeatherCmdMsgPacket() {
    }
        
    @Override
    public void readData(ByteBuf data) {
    	int ord = data.readInt();
    	this.setting = PWEATHER_SETTING.values()[ord];
    }

    @Override
    public void writeData(ByteBuf data) {
    	data.writeInt(this.setting.ordinal());
    }

    @Override
    public void processPacket(INetHandler handler, EntityPlayer player) {
    	PWeatherCommand.setWeatherSetting(this.setting, player.getEntityWorld());
    }
    
    public static void sendCmdMessage(EntityPlayerMP player, PWEATHER_SETTING setting) {
        PWeatherCmdMsgPacket pkt = new PWeatherCmdMsgPacket(setting);
        WesterosBlocksChannelHandler.sendToPlayer(pkt, player);
    }

}
