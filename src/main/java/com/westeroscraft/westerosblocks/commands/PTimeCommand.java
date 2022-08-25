package com.westeroscraft.westerosblocks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.PTimeMessage;

import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.CommandSourceStack;

public class PTimeCommand {
	public static void register(CommandDispatcher<CommandSourceStack> source) {
		source.register(Commands.literal("ptime")
			.then(Commands.literal("reset").executes((ctx) -> {
				return resetTime(ctx.getSource());
			}))
			.then(Commands.literal("set")
			.then(Commands.literal("normal").executes((ctx) -> {
				return resetTime(ctx.getSource());
			}))
			.then(Commands.literal("default").executes((ctx) -> {
				return resetTime(ctx.getSource());
			}))
			.then(Commands.literal("sunrise").executes((ctx) -> {
				return setTime(ctx.getSource(), 23000, false);
			}))
			.then(Commands.literal("day").executes((ctx) -> {
				return setTime(ctx.getSource(), 0, false);
			}))
			.then(Commands.literal("morning").executes((ctx) -> {
				return setTime(ctx.getSource(), 1000, false);
			}))
			.then(Commands.literal("noon").executes((ctx) -> {
				return setTime(ctx.getSource(), 6000, false);
			}))
			.then(Commands.literal("afternoon").executes((ctx) -> {
				return setTime(ctx.getSource(), 9000, false);
			}))
			.then(Commands.literal("sunset").executes((ctx) -> {
				return setTime(ctx.getSource(), 12000, false);
			}))
			.then(Commands.literal("night").executes((ctx) -> {
				return setTime(ctx.getSource(), 14000, false);
			}))
			.then(Commands.literal("midnight").executes((ctx) -> {
				return setTime(ctx.getSource(), 18000, false);
			}))
			.then(Commands.argument("time", TimeArgument.time()).executes((ctx) -> {
				return setTime(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "time"), false);
			}))
		).then(Commands.literal("setrelative")
			.then(Commands.literal("ahead")
				.then(Commands.argument("time", TimeArgument.time()).executes((ctx) -> {
					return setTime(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "time"), true);
				}))
			)
			.then(Commands.literal("behind")
					.then(Commands.argument("time", TimeArgument.time()).executes((ctx) -> {
						return setTime(ctx.getSource(), -IntegerArgumentType.getInteger(ctx, "time"), true);
				}))
			)
		));
	}

	public static int resetTime(CommandSourceStack source) {
		if (source.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) source.getEntity();
			// Send relative of zero for reset
			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), new PTimeMessage(true, 0));
			source.sendSuccess(new TextComponent("Reset player time to server time"), true);
		} else {
			source.sendFailure(new TextComponent("Cannot be used by console"));
		}
		
		return 1;
	}

	public static int setTime(CommandSourceStack source, int timeticks, boolean relative) {
		if (source.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) source.getEntity();
			WesterosBlocks.log.info("Set time to " + player.getName().toString() + " to relative=" + relative
					+ ", offset=" + timeticks);
			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player),
					new PTimeMessage(relative, timeticks));
			source.sendSuccess(
					new TextComponent("Set player time to " + timeticks + (relative ? "(relative)" : "")), true);
		} else {
			source.sendFailure(new TextComponent("Cannot be used by console"));
		}
		return 1;
	}
}