//package com.westeroscraft.westerosblocks.network;
//import com.westeroscraft.westerosblocks.WesterosBlocks;
//
//import net.minecraft.world.level.Level;
//import net.neoforged.fml.LogicalSide;
//import net.neoforged.neoforge.common.util.LogicalSidedProvider;
//
//import java.util.Optional;
//
//public class ClientMessageHandler {
//
//	/**
//	 * Called when PTimeMessage received.
//	 * CALLED BY THE NETWORK THREAD, NOT THE CLIENT THREAD.
//	 */
//	public static void onPTimeMessageReceived(final PTimeMessage message,
//			Supplier<NetworkEvent.Context> ctxSupplier) {
//		NetworkEvent.Context ctx = ctxSupplier.get();
//		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
//		ctx.setPacketHandled(true);
//
//		if (sideReceived != LogicalSide.CLIENT) {
//			WesterosBlocks.log.warn("PTimeMessage received on wrong side:" + ctx.getDirection().getReceptionSide());
//			return;
//		}
//		Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
//		if (!clientWorld.isPresent()) {
//			WesterosBlocks.log.warn("PTimeMessage context could not provide a ClientWorld.");
//			return;
//		}
//		// Enqueue processing to happen on client thread next tick
//		ctx.enqueueWork(() -> processPTimeMessage(clientWorld.get(), message));
//	}
//
//	public static boolean ptimeRelative = true;
//	public static int ptimeOffset = 0;
//
//	// This message is called from the Client thread.
//	// It spawns a number of Particle particles at the target location within a
//	// short range around the target location
//	private static void processPTimeMessage(Level worldClient, PTimeMessage message) {
//		WesterosBlocks.log.info("Got PTimeMessage: relative=" + message.relative + ", time_off=" + message.time_off);
//		ptimeRelative = message.relative;
//		ptimeOffset = message.time_off;
//	}
//
//	/**
//	 * Called when PWeatherMessage received.
//	 * CALLED BY THE NETWORK THREAD, NOT THE CLIENT THREAD.
//	 */
//	public static void onPWeatherMessageReceived(final PWeatherMessage message,
//			Supplier<NetworkEvent.Context> ctxSupplier) {
//		NetworkEvent.Context ctx = ctxSupplier.get();
//		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
//		ctx.setPacketHandled(true);
//
//		if (sideReceived != LogicalSide.CLIENT) {
//			WesterosBlocks.log.warn("PWeatherMessage received on wrong side:" + ctx.getDirection().getReceptionSide());
//			return;
//		}
//		Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
//		if (!clientWorld.isPresent()) {
//			WesterosBlocks.log.warn("PWeatherMessage context could not provide a ClientWorld.");
//			return;
//		}
//		// Enqueue processing to happen on client thread next tick
//		ctx.enqueueWork(() -> processPWeatherMessage(clientWorld.get(), message));
//	}
//
//	public static PWeatherMessage.WeatherCond weatherCond = PWeatherMessage.WeatherCond.RESET;
//	public static boolean savedRain = false;
//	public static float savedRainLevel = 0.0F;
//	public static float savedThunderLevel = 0.0F;
//	// This message is called from the Client thread.
//	// It spawns a number of Particle particles at the target location within a
//	// short range around the target location
//	private static void processPWeatherMessage(Level worldClient, PWeatherMessage message) {
//		WesterosBlocks.log.info("Got PWeatherMessage: " + message.weather);
//		weatherCond = message.weather;
//		switch (weatherCond) {
//		case RESET:
//			worldClient.getLevelData().setRaining(savedRain);
//			worldClient.setRainLevel(savedRainLevel);
//			worldClient.setThunderLevel(savedThunderLevel);
//			break;
//		case CLEAR:
//			worldClient.getLevelData().setRaining(false);
//			worldClient.setRainLevel(0.0F);
//			worldClient.setThunderLevel(0.0F);
//			break;
//		case RAIN:
//			worldClient.getLevelData().setRaining(true);
//			worldClient.setRainLevel(1.0F);
//			worldClient.setThunderLevel(0.0F);
//			break;
//		case THUNDER:
//			worldClient.getLevelData().setRaining(true);
//			worldClient.setRainLevel(1.0F);
//			worldClient.setThunderLevel(1.0F);
//			break;
//		}
//	}
//
//	public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
//		return WesterosBlocks.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
//	}
//}