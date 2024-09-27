package com.westeroscraft.westerosblocks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.PWeatherMessage;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class PWeatherCommand {
   public static void register(CommandDispatcher<CommandSourceStack> source) {
		source.register(Commands.literal("pweather")
			.then(Commands.literal("reset").executes((ctx) -> {
				return resetWeather(ctx.getSource());
			}))
			.then(Commands.literal("clear").executes((ctx) -> {
				return setWeather(ctx.getSource(), PWeatherMessage.WeatherCond.CLEAR);
			}))
			.then(Commands.literal("rain").executes((ctx) -> {
				return setWeather(ctx.getSource(), PWeatherMessage.WeatherCond.RAIN);
			}))
			.then(Commands.literal("thunder").executes((ctx) -> {
				return setWeather(ctx.getSource(), PWeatherMessage.WeatherCond.THUNDER);
			}))
		);
   }
	public static int resetWeather(CommandSourceStack source) {
		if (source.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) source.getEntity();
			// Send relative of zero for reset
//			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), new PWeatherMessage(PWeatherMessage.WeatherCond.RESET));
			source.sendSuccess(() -> Component.literal("Reset player weather to server weather"), true);
		} else {
			source.sendFailure(Component.literal("Cannot be used by console"));
		}
		return 1;
	}

	public static int setWeather(CommandSourceStack source, PWeatherMessage.WeatherCond cond) {
		if (source.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) source.getEntity();
			// Send relative of zero for reset
			// TODO FIXME
//			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), new PWeatherMessage(cond));
			source.sendSuccess(() -> Component.literal("Set player weather to " + cond), true);
		} else {
			source.sendFailure(Component.literal("Cannot be used by console"));
		}
		return 1;
	}
}