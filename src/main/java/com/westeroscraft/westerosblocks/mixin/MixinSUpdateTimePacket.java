package com.westeroscraft.westerosblocks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.ClientMessageHandler;

import net.minecraft.network.play.server.SUpdateTimePacket;

@Mixin(SUpdateTimePacket.class)
public abstract class MixinSUpdateTimePacket
{	
	@Shadow private long gameTime;
	@Shadow private long dayTime;
	
	// This constructor is fake and never used
	protected MixinSUpdateTimePacket()
	{
	}
	
	@Inject(method = "read", at = @At("TAIL"))
	private void read(CallbackInfo ci)
	{
		if (ClientMessageHandler.ptimeRelative) {	// If we are relative mode
			if (ClientMessageHandler.ptimeOffset != 0) {
				gameTime += ClientMessageHandler.ptimeOffset;
				dayTime += (ClientMessageHandler.ptimeOffset % 24000);
			}
		}
		else {	// Else, absolute
			gameTime = ClientMessageHandler.ptimeOffset;
			dayTime = (ClientMessageHandler.ptimeOffset % 24000);			
		}
	}
}
