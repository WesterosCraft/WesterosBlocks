package com.westeroscraft.westerosblocks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.westeroscraft.westerosblocks.WesterosBlocks;

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
		WesterosBlocks.log.info("SChangeGameStatePacket: " + getState(this.event) + "," + this.param);
	}

	private String getState(SChangeGameStatePacket.State st) {
		if (st == SChangeGameStatePacket.NO_RESPAWN_BLOCK_AVAILABLE) {
			return "NO_RESPAWN_BLOCK_AVAILABLE";
		}
		else if (st == SChangeGameStatePacket.START_RAINING) {
			return "START_RAINING";
		}
		else if (st == SChangeGameStatePacket.STOP_RAINING) {
			return "STOP_RAINING";
		}
		else if (st == SChangeGameStatePacket.CHANGE_GAME_MODE) {
			return "CHANGE_GAME_MODE";
		}
		else if (st == SChangeGameStatePacket.WIN_GAME) {
			return "WIN_GAME";
		}
		else if (st == SChangeGameStatePacket.DEMO_EVENT) {
			return "DEMO_EVENT";
		}
		else if (st == SChangeGameStatePacket.ARROW_HIT_PLAYER) {
			return "ARROW_HIT_PLAYER";
		}
		else if (st == SChangeGameStatePacket.RAIN_LEVEL_CHANGE) {
			return "RAIN_LEVEL_CHANGE";
		}
		else if (st == SChangeGameStatePacket.THUNDER_LEVEL_CHANGE) {
			return "THUNDER_LEVEL_CHANGE";
		}
		else if (st == SChangeGameStatePacket.PUFFER_FISH_STING) {
			return "PUFFER_FISH_STING";
		}
		else if (st == SChangeGameStatePacket.GUARDIAN_ELDER_EFFECT) {
			return "GUARDIAN_ELDER_EFFECT";
		}
		else if (st == SChangeGameStatePacket.IMMEDIATE_RESPAWN) {
			return "IMMEDIATE_RESPAWN";
		}
		else {
			return "???";
		}
	}
}