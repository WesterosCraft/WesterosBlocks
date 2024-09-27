package com.westeroscraft.westerosblocks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.Component;

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
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
				source.sendSuccess(() -> Component.literal("Enable night vision"), true);
			}
			else {
				player.removeEffect(MobEffects.NIGHT_VISION);
				source.sendSuccess(() -> Component.literal("Disable night vision"), true);
			}

		} else {
			source.sendFailure(Component.literal("Cannot be used by console"));
		}
		
		return 1;
	}

	public static int setTime(CommandSourceStack source, int timeticks, boolean relative) {
		if (source.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) source.getEntity();
			WesterosBlocks.log.info("Set time to " + player.getName().toString() + " to relative=" + relative
					+ ", offset=" + timeticks);

			// TODO FIXME
//			WesterosBlocks.simpleChannel.send(new PTimeMessage(relative, timeticks), PacketDistributor.PLAYER.with(player));
			source.sendSuccess(
					() -> Component.literal("Set player time to " + timeticks + (relative ? "(relative)" : "")), true);
		} else {
			source.sendFailure(Component.literal("Cannot be used by console"));
		}
		return 1;
	}
}