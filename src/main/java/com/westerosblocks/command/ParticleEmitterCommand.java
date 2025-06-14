package com.westerosblocks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.westerosblocks.block.custom.WCParticleEmitterBlock;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ParticleEmitterCommand {
    private static final int DEFAULT_RADIUS = 16;
    private static final int MAX_RADIUS = 100;
    private static final int MIN_RADIUS = 1;

    public static void register() {
        CommandRegistrationCallback.EVENT.register(ParticleEmitterCommand::registerCommands);
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, 
                                       CommandRegistryAccess registryAccess, 
                                       CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("particleemitter")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("list")
                        .then(CommandManager.argument("radius", IntegerArgumentType.integer(MIN_RADIUS, MAX_RADIUS))
                                .executes(context -> listParticleEmitters(context, IntegerArgumentType.getInteger(context, "radius"))))
                        .executes(context -> listParticleEmitters(context, DEFAULT_RADIUS)))
                .then(CommandManager.literal("highlight")
                        .then(CommandManager.argument("radius", IntegerArgumentType.integer(MIN_RADIUS, MAX_RADIUS))
                                .executes(context -> highlightParticleEmitters(context, IntegerArgumentType.getInteger(context, "radius"))))
                        .executes(context -> highlightParticleEmitters(context, DEFAULT_RADIUS)))
                .then(CommandManager.literal("help")
                        .executes(ParticleEmitterCommand::showHelp)));
    }

    private static int listParticleEmitters(CommandContext<ServerCommandSource> context, int radius) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        List<BlockPos> emitters = findParticleEmittersInRadius(source, radius);

        if (emitters.isEmpty()) {
            sendNoEmittersFoundMessage(source, radius);
            return 0;
        }

        source.sendFeedback(() -> Text.literal("Found " + emitters.size() + " particle emitter(s) within " + radius + " blocks:")
                .formatted(Formatting.GREEN), false);

        for (BlockPos pos : emitters) {
            sendEmitterStatusMessage(source, pos, source.getWorld().getBlockState(pos), source.getPlayerOrThrow().getBlockPos());
        }

        return emitters.size();
    }

    private static int highlightParticleEmitters(CommandContext<ServerCommandSource> context, int radius) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        List<BlockPos> emitters = findParticleEmittersInRadius(source, radius);
        
        if (emitters.isEmpty()) {
            sendNoEmittersFoundMessage(source, radius);
            return 0;
        }
        
        highlightEmitters(source.getWorld(), emitters);
        source.sendFeedback(() -> Text.literal("Highlighted " + emitters.size() + " particle emitter(s) within " + radius + " blocks. Red = ON/Invisible, Green = OFF/Visible")
                .formatted(Formatting.AQUA), false);
        
        return emitters.size();
    }

    private static int showHelp(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        source.sendFeedback(() -> Text.literal("Particle Emitter Commands:").formatted(Formatting.GOLD), false);
        source.sendFeedback(() -> Text.literal("  /particleemitter list [radius] - List nearby particle emitters").formatted(Formatting.WHITE), false);
        source.sendFeedback(() -> Text.literal("  /particleemitter highlight [radius] - Show particles around emitters").formatted(Formatting.WHITE), false);
        source.sendFeedback(() -> Text.literal("  /particleemitter help - Show this help").formatted(Formatting.WHITE), false);
        source.sendFeedback(() -> Text.literal("Default radius is " + DEFAULT_RADIUS + " blocks. Red highlights = ON/Invisible, Green = OFF/Visible")
                .formatted(Formatting.GRAY), false);

        return 1;
    }

    private static List<BlockPos> findParticleEmittersInRadius(ServerCommandSource source, int radius) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        BlockPos center = source.getPlayerOrThrow().getBlockPos();
        return findParticleEmitters(world, center, radius);
    }

    private static void sendNoEmittersFoundMessage(ServerCommandSource source, int radius) {
        source.sendFeedback(() -> Text.literal("No particle emitters found within " + radius + " blocks.")
                .formatted(Formatting.YELLOW), false);
    }

    private static void sendEmitterStatusMessage(ServerCommandSource source, BlockPos pos, BlockState state, BlockPos playerPos) {
        boolean isPowered = state.get(WCParticleEmitterBlock.POWERED);
        String status = isPowered ? "ON (invisible)" : "OFF (visible)";
        Formatting color = isPowered ? Formatting.RED : Formatting.GREEN;

        double distance = Math.sqrt(playerPos.getSquaredDistance(pos));

        source.sendFeedback(() -> Text.literal(String.format("  %s [%d, %d, %d] - %s (%.1f blocks away)", 
                status, pos.getX(), pos.getY(), pos.getZ(), status, distance))
                .formatted(color), false);
    }

    private static void highlightEmitters(ServerWorld world, List<BlockPos> emitters) {
        for (BlockPos pos : emitters) {
            BlockState state = world.getBlockState(pos);
            boolean isPowered = state.get(WCParticleEmitterBlock.POWERED);

            Vector3f color = isPowered ? new Vector3f(1.0f, 0.0f, 0.0f) : new Vector3f(0.0f, 1.0f, 0.0f);
            DustParticleEffect particleEffect = new DustParticleEffect(color, 2.0f);

            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            
            for (int i = 0; i < 20; i++) {
                double offsetX = (Math.random() - 0.5) * 1.2;
                double offsetY = (Math.random() - 0.5) * 1.2;
                double offsetZ = (Math.random() - 0.5) * 1.2;
                
                world.spawnParticles(particleEffect, x + offsetX, y + offsetY, z + offsetZ, 
                        1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }
    
    private static List<BlockPos> findParticleEmitters(World world, BlockPos center, int radius) {
        List<BlockPos> emitters = new ArrayList<>();
        
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for (int y = center.getY() - radius; y <= center.getY() + radius; y++) {
                for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() instanceof WCParticleEmitterBlock) {
                        emitters.add(pos);
                    }
                }
            }
        }
        
        return emitters;
    }
} 