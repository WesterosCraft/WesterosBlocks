package com.westeroscraft.westerosblocks.network;

import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import java.util.Optional;
import java.util.function.Supplier;

public class ClientMessageHandler {

	/**
	 * Called when PTimeMessage received.
	 * CALLED BY THE NETWORK THREAD, NOT THE CLIENT THREAD.
	 */
	public static void onPTimeMessageReceived(final PTimeMessage message,
			Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived != LogicalSide.CLIENT) {
			WesterosBlocks.log.warn("PTimeMessage received on wrong side:" + ctx.getDirection().getReceptionSide());
			return;
		}
		Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
		if (!clientWorld.isPresent()) {
			WesterosBlocks.log.warn("PTimeMessage context could not provide a ClientWorld.");
			return;
		}
		// Enqueue processing to happen on client thread next tick
		ctx.enqueueWork(() -> processMessage(clientWorld.get(), message));
	}

	public static boolean ptimeRelative = true;
	public static int ptimeOffset = 0;
	
	// This message is called from the Client thread.
	// It spawns a number of Particle particles at the target location within a
	// short range around the target location
	private static void processMessage(ClientWorld worldClient, PTimeMessage message) {
		WesterosBlocks.log.info("Got PTimeMessage: relative=" + message.relative + ", time_off=" + message.time_off);
		ptimeRelative = message.relative;
		ptimeOffset = message.time_off;
		return;
	}

	public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
		return WesterosBlocks.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
	}
}