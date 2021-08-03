package com.westeroscraft.westerosblocks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.PWeatherMessage;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.command.arguments.TimeArgument;

public class PWeatherCommand {
   public static void register(CommandDispatcher<CommandSource> source) {
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
	public static int resetWeather(CommandSource source) {
		if (source.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			// Send relative of zero for reset
			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), new PWeatherMessage(PWeatherMessage.WeatherCond.RESET));
			source.sendSuccess(new StringTextComponent("Reset player weather to server weather"), true);
		} else {
			source.sendFailure(new StringTextComponent("Cannot be used by console"));
		}
		return 1;
	}

	public static int setWeather(CommandSource source, PWeatherMessage.WeatherCond cond) {
		if (source.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			// Send relative of zero for reset
			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), new PWeatherMessage(cond));
			source.sendSuccess(new StringTextComponent("Set player weather to " + cond), true);
		} else {
			source.sendFailure(new StringTextComponent("Cannot be used by console"));
		}
		return 1;
	}
}