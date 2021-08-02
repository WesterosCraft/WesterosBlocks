package com.westeroscraft.westerosblocks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.PTimeMessage;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.command.arguments.TimeArgument;

public class PTimeCommand {
	public static void register(CommandDispatcher<CommandSource> source) {
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

	public static int resetTime(CommandSource source) {
		if (source.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			// Send relative of zero for reset
			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), new PTimeMessage(true, 0));
			source.sendSuccess(new StringTextComponent("Reset player time to server time"), true);
		} else {
			source.sendFailure(new StringTextComponent("Cannot be used by console"));
		}
		
		return 1;
	}

	public static int setTime(CommandSource source, int timeticks, boolean relative) {
		if (source.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			WesterosBlocks.log.info("Set time to " + player.getName().toString() + " to relative=" + relative
					+ ", offset=" + timeticks);
			WesterosBlocks.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player),
					new PTimeMessage(relative, timeticks));
			source.sendSuccess(
					new StringTextComponent("Set player time to " + timeticks + (relative ? "(relative)" : "")), true);
		} else {
			source.sendFailure(new StringTextComponent("Cannot be used by console"));
		}
		return 1;
	}
}