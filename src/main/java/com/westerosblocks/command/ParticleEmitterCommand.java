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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ParticleEmitterCommand {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register(ParticleEmitterCommand::registerCommands);
    }
    
    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, 
                                       CommandRegistryAccess registryAccess, 
                                       CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("particleemitter")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("list")
                        .then(CommandManager.argument("radius", IntegerArgumentType.integer(1, 100))
                                .executes(context -> listParticleEmitters(context, IntegerArgumentType.getInteger(context, "radius"))))
                        .executes(context -> listParticleEmitters(context, 16)))
                // .then(CommandManager.literal("toggle")
                //         .then(CommandManager.argument("radius", IntegerArgumentType.integer(1, 100))
                //                 .executes(context -> toggleParticleEmitters(context, IntegerArgumentType.getInteger(context, "radius"))))
                //         .executes(context -> toggleParticleEmitters(context, 16)))
                .then(CommandManager.literal("highlight")
                        .then(CommandManager.argument("radius", IntegerArgumentType.integer(1, 100))
                                .executes(context -> highlightParticleEmitters(context, IntegerArgumentType.getInteger(context, "radius"))))
                        .executes(context -> highlightParticleEmitters(context, 16)))
                .then(CommandManager.literal("help")
                        .executes(ParticleEmitterCommand::showHelp)));
    }
    
    private static int listParticleEmitters(CommandContext<ServerCommandSource> context, int radius) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = source.getWorld();
        BlockPos playerPos = player.getBlockPos();
        
        List<BlockPos> emitters = findParticleEmitters(world, playerPos, radius);
        
        if (emitters.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No particle emitters found within " + radius + " blocks.")
                    .formatted(Formatting.YELLOW), false);
            return 0;
        }
        
        source.sendFeedback(() -> Text.literal("Found " + emitters.size() + " particle emitter(s) within " + radius + " blocks:")
                .formatted(Formatting.GREEN), false);
        
        for (BlockPos pos : emitters) {
            BlockState state = world.getBlockState(pos);
            boolean isPowered = state.get(WCParticleEmitterBlock.POWERED);
            String status = isPowered ? "ON (invisible)" : "OFF (visible)";
            Formatting color = isPowered ? Formatting.RED : Formatting.GREEN;
            
            double distance = Math.sqrt(playerPos.getSquaredDistance(pos));
            
            source.sendFeedback(() -> Text.literal(String.format("  %s [%d, %d, %d] - %s (%.1f blocks away)", 
                    status, pos.getX(), pos.getY(), pos.getZ(), status, distance))
                    .formatted(color), false);
        }
        
        return emitters.size();
    }
    
    private static int toggleParticleEmitters(CommandContext<ServerCommandSource> context, int radius) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = source.getWorld();
        BlockPos playerPos = player.getBlockPos();
        
        List<BlockPos> emitters = findParticleEmitters(world, playerPos, radius);
        
        if (emitters.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No particle emitters found within " + radius + " blocks.")
                    .formatted(Formatting.YELLOW), false);
            return 0;
        }
        
        int toggledCount = 0;
        for (BlockPos pos : emitters) {
            BlockState state = world.getBlockState(pos);
            BlockState newState = state.cycle(WCParticleEmitterBlock.POWERED);
            world.setBlockState(pos, newState);
            toggledCount++;
        }
        
        final int finalToggledCount = toggledCount;
        source.sendFeedback(() -> Text.literal("Toggled " + finalToggledCount + " particle emitter(s) within " + radius + " blocks.")
                .formatted(Formatting.GREEN), false);
        
        return toggledCount;
    }
    
    private static int highlightParticleEmitters(CommandContext<ServerCommandSource> context, int radius) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = source.getWorld();
        BlockPos playerPos = player.getBlockPos();
        
        List<BlockPos> emitters = findParticleEmitters(world, playerPos, radius);
        
        if (emitters.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No particle emitters found within " + radius + " blocks.")
                    .formatted(Formatting.YELLOW), false);
            return 0;
        }
        
        // Create highlighting particles around each emitter
        for (BlockPos pos : emitters) {
            BlockState state = world.getBlockState(pos);
            boolean isPowered = state.get(WCParticleEmitterBlock.POWERED);
            
            // Use different colors for powered vs unpowered
            Vector3f color = isPowered ? new Vector3f(1.0f, 0.0f, 0.0f) : new Vector3f(0.0f, 1.0f, 0.0f); // Red for ON, Green for OFF
            DustParticleEffect particleEffect = new DustParticleEffect(color, 2.0f);
            
            // Create a cube outline of particles around the block
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            
            // Create particles in a frame around the block
            for (int i = 0; i < 20; i++) {
                double offsetX = (Math.random() - 0.5) * 1.2;
                double offsetY = (Math.random() - 0.5) * 1.2;
                double offsetZ = (Math.random() - 0.5) * 1.2;
                
                world.spawnParticles(particleEffect, x + offsetX, y + offsetY, z + offsetZ, 
                        1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        
        source.sendFeedback(() -> Text.literal("Highlighted " + emitters.size() + " particle emitter(s) within " + radius + " blocks. Red = ON/Invisible, Green = OFF/Visible")
                .formatted(Formatting.AQUA), false);
        
        return emitters.size();
    }
    
    private static int showHelp(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        source.sendFeedback(() -> Text.literal("Particle Emitter Commands:").formatted(Formatting.GOLD), false);
        source.sendFeedback(() -> Text.literal("  /particleemitter list [radius] - List nearby particle emitters").formatted(Formatting.WHITE), false);
        source.sendFeedback(() -> Text.literal("  /particleemitter toggle [radius] - Toggle all particle emitters in area").formatted(Formatting.WHITE), false);
        source.sendFeedback(() -> Text.literal("  /particleemitter highlight [radius] - Show particles around emitters").formatted(Formatting.WHITE), false);
        source.sendFeedback(() -> Text.literal("  /particleemitter help - Show this help").formatted(Formatting.WHITE), false);
        source.sendFeedback(() -> Text.literal("Default radius is 16 blocks. Red highlights = ON/Invisible, Green = OFF/Visible").formatted(Formatting.GRAY), false);
        
        return 1;
    }
    
    private static List<BlockPos> findParticleEmitters(World world, BlockPos center, int radius) {
        List<BlockPos> emitters = new ArrayList<>();
        
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for (int y = center.getY() - radius; y <= center.getY() + radius; y++) {
                for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    
                    if (state.getBlock() instanceof WCParticleEmitterBlock) {
                        emitters.add(pos);
                    }
                }
            }
        }
        
        return emitters;
    }
} 