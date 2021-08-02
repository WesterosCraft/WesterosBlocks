package com.westeroscraft.westerosblocks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class PWeatherCommand {
   public static void register(CommandDispatcher<CommandSource> p_198862_0_) {
      p_198862_0_.register(Commands.literal("pweather").requires((p_198868_0_) -> {
         return p_198868_0_.hasPermission(2);
      }).then(Commands.literal("clear").executes((p_198861_0_) -> {
         return setClear(p_198861_0_.getSource(), 6000);
      }).then(Commands.argument("duration", IntegerArgumentType.integer(0, 1000000)).executes((p_198864_0_) -> {
         return setClear(p_198864_0_.getSource(), IntegerArgumentType.getInteger(p_198864_0_, "duration") * 20);
      }))).then(Commands.literal("rain").executes((p_198860_0_) -> {
         return setRain(p_198860_0_.getSource(), 6000);
      }).then(Commands.argument("duration", IntegerArgumentType.integer(0, 1000000)).executes((p_198866_0_) -> {
         return setRain(p_198866_0_.getSource(), IntegerArgumentType.getInteger(p_198866_0_, "duration") * 20);
      }))).then(Commands.literal("thunder").executes((p_198859_0_) -> {
         return setThunder(p_198859_0_.getSource(), 6000);
      }).then(Commands.argument("duration", IntegerArgumentType.integer(0, 1000000)).executes((p_198867_0_) -> {
         return setThunder(p_198867_0_.getSource(), IntegerArgumentType.getInteger(p_198867_0_, "duration") * 20);
      }))));
   }

   private static int setClear(CommandSource p_198869_0_, int p_198869_1_) {
      p_198869_0_.getLevel().setWeatherParameters(p_198869_1_, 0, false, false);
      p_198869_0_.sendSuccess(new TranslationTextComponent("commands.weather.set.clear"), true);
      return p_198869_1_;
   }

   private static int setRain(CommandSource p_198865_0_, int p_198865_1_) {
      p_198865_0_.getLevel().setWeatherParameters(0, p_198865_1_, true, false);
      p_198865_0_.sendSuccess(new TranslationTextComponent("commands.weather.set.rain"), true);
      return p_198865_1_;
   }

   private static int setThunder(CommandSource p_198863_0_, int p_198863_1_) {
      p_198863_0_.getLevel().setWeatherParameters(0, p_198863_1_, true, true);
      p_198863_0_.sendSuccess(new TranslationTextComponent("commands.weather.set.thunder"), true);
      return p_198863_1_;
   }
}