package com.westeroscraft.westerosblocks.mixin;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.westeroscraft.westerosblocks.network.ClientMessageHandler;

import net.minecraft.network.protocol.game.ClientboundGameEventPacket;

@Mixin(ClientboundGameEventPacket.class)
public abstract class MixinClientboundGameEventPacket
{	
	@Shadow private ClientboundGameEventPacket.Type event;
	@Shadow private float param;

	// This constructor is fake and never used
	protected MixinClientboundGameEventPacket()
	{
	}
	
	@Inject(method = "handle", at = @At("TAIL"))
	private void handle(CallbackInfo ci)
	{
		// Record latest levels sent from server, for sake of reset
		if (event == ClientboundGameEventPacket.START_RAINING) {
			ClientMessageHandler.savedRain = true;
			ClientMessageHandler.savedRainLevel = 1.0F;			
		}
		else if (event == ClientboundGameEventPacket.STOP_RAINING) {
			ClientMessageHandler.savedRain = false;
			ClientMessageHandler.savedRainLevel = 0.0F;			
		}
		else if (event == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
			ClientMessageHandler.savedRainLevel = param;
		}
		else if (event == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
			ClientMessageHandler.savedThunderLevel = param;
		}
						
		switch (ClientMessageHandler.weatherCond) {
		case RESET:
			// Nothing to do
			break;
		case CLEAR:
			if (event == ClientboundGameEventPacket.START_RAINING) {
				event = ClientboundGameEventPacket.STOP_RAINING; 	// Force it to be stop
				param = 0.0F;
			}
			else if (event == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
				param = 0.0F;	// Force to zero				
			}
			else if (event == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
				param = 0.0F;	// Force to zero								
			}
			break;
		case RAIN:
			if (event == ClientboundGameEventPacket.STOP_RAINING) {
				event = ClientboundGameEventPacket.START_RAINING; 	// Force it to be start
				param = 0.0F;
			}
			else if (event == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
				param = 1.0F;	// Force to one				
			}
			else if (event == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
				param = 0.0F;	// Force to zero								
			}
			break;
		case THUNDER:
			if (event == ClientboundGameEventPacket.STOP_RAINING) {
				event = ClientboundGameEventPacket.START_RAINING; 	// Force it to be start
				param = 0.0F;
			}
			else if (event == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
				param = 1.0F;	// Force to one				
			}
			else if (event == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
				param = 1.0F;	// Force to one				
			}
			break;			
		}
	}
}