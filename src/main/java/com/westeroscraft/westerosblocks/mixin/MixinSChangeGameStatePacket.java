package com.westeroscraft.westerosblocks.mixin;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.ClientMessageHandler;
import com.westeroscraft.westerosblocks.network.PWeatherMessage;

import net.minecraft.network.play.server.SChangeGameStatePacket;

@Mixin(SChangeGameStatePacket.class)
public abstract class MixinSChangeGameStatePacket
{	
	@Shadow private SChangeGameStatePacket.State event;
	@Shadow private float param;

	// This constructor is fake and never used
	protected MixinSChangeGameStatePacket()
	{
	}
	
	@Inject(method = "read", at = @At("TAIL"))
	private void read(CallbackInfo ci)
	{
		// Record latest levels sent from server, for sake of reset
		if (event == SChangeGameStatePacket.START_RAINING) {
			ClientMessageHandler.savedRain = true;
			ClientMessageHandler.savedRainLevel = 1.0F;			
		}
		else if (event == SChangeGameStatePacket.STOP_RAINING) {
			ClientMessageHandler.savedRain = false;
			ClientMessageHandler.savedRainLevel = 0.0F;			
		}
		else if (event == SChangeGameStatePacket.RAIN_LEVEL_CHANGE) {
			ClientMessageHandler.savedRainLevel = param;
		}
		else if (event == SChangeGameStatePacket.THUNDER_LEVEL_CHANGE) {
			ClientMessageHandler.savedThunderLevel = param;
		}
						
		switch (ClientMessageHandler.weatherCond) {
		case RESET:
			// Nothing to do
			break;
		case CLEAR:
			if (event == SChangeGameStatePacket.START_RAINING) {
				event = SChangeGameStatePacket.STOP_RAINING; 	// Force it to be stop
				param = 0.0F;
			}
			else if (event == SChangeGameStatePacket.RAIN_LEVEL_CHANGE) {
				param = 0.0F;	// Force to zero				
			}
			else if (event == SChangeGameStatePacket.THUNDER_LEVEL_CHANGE) {
				param = 0.0F;	// Force to zero								
			}
			break;
		case RAIN:
			if (event == SChangeGameStatePacket.STOP_RAINING) {
				event = SChangeGameStatePacket.START_RAINING; 	// Force it to be start
				param = 0.0F;
			}
			else if (event == SChangeGameStatePacket.RAIN_LEVEL_CHANGE) {
				param = 1.0F;	// Force to one				
			}
			else if (event == SChangeGameStatePacket.THUNDER_LEVEL_CHANGE) {
				param = 0.0F;	// Force to zero								
			}
			break;
		case THUNDER:
			if (event == SChangeGameStatePacket.STOP_RAINING) {
				event = SChangeGameStatePacket.START_RAINING; 	// Force it to be start
				param = 0.0F;
			}
			else if (event == SChangeGameStatePacket.RAIN_LEVEL_CHANGE) {
				param = 1.0F;	// Force to one				
			}
			else if (event == SChangeGameStatePacket.THUNDER_LEVEL_CHANGE) {
				param = 1.0F;	// Force to one				
			}
			break;			
		}
	}
}