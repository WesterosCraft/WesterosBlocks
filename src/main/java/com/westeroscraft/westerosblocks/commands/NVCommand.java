package com.westeroscraft.westerosblocks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.PTimeMessage;

import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.CommandSourceStack;

public class NVCommand {
	public static void register(CommandDispatcher<CommandSourceStack> source) {
		source.register(Commands.literal("nv").executes((ctx) -> {
			return nightVision(ctx.getSource());
		}));
	}

	public static int nightVision(CommandSourceStack source) {
		if (source.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) source.getEntity();
			MobEffectInstance nv = player.getEffect(MobEffects.NIGHT_VISION);
			if (nv == null) {
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE));
				source.sendSuccess(new TextComponent("Enable night vision"), true);
			}
			else {
				player.removeEffect(MobEffects.NIGHT_VISION);
				source.sendSuccess(new TextComponent("Disable night vision"), true);
			}
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