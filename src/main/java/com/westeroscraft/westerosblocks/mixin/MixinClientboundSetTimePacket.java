package com.westeroscraft.westerosblocks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.ClientMessageHandler;

import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;

@Mixin(ClientboundSetTimePacket.class)
public abstract class MixinClientboundSetTimePacket
{	
	@Shadow private long gameTime;
	@Shadow private long dayTime;
	
	// This constructor is fake and never used
	protected MixinClientboundSetTimePacket()
	{
	}
	
	@Inject(method = "handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at = @At("HEAD"))
	private void handle(ClientGamePacketListener listener, CallbackInfo ci)
	{
		//WesterosBlocks.log.info("SetTimePacket(" + gameTime + "," + dayTime + ")");
		if (ClientMessageHandler.ptimeRelative) {	// If we are relative mode
			if (ClientMessageHandler.ptimeOffset != 0) {
				//gameTime += ClientMessageHandler.ptimeOffset;
				dayTime = dayTime + ClientMessageHandler.ptimeOffset;
			}
		}
		else {	// Else, absolute
			//gameTime = ClientMessageHandler.ptimeOffset;
			dayTime = -ClientMessageHandler.ptimeOffset;		
			if (dayTime == 0) dayTime = -1;
		}
		//WesterosBlocks.log.info("SetTimePacket(exit)(" + gameTime + "," + dayTime + ")");
	}
}
