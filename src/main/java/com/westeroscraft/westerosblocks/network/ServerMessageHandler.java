package com.westeroscraft.westerosblocks.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import java.util.function.Supplier;

public class ServerMessageHandler {

	/**
	 * Called when a message is received of the appropriate type. CALLED BY THE
	 * NETWORK THREAD, NOT THE SERVER THREAD
	 */
	public static void onMessageReceived(final PTimeMessage message,
			Supplier<NetworkEvent.Context> ctxSupplier) {
		// Get and mark packet handled
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived != LogicalSide.SERVER) {
			WesterosBlocks.log.warn("PTimeMessage received on wrong side:" + ctx.getDirection().getReceptionSide());
			return;
		}
	    final ServerPlayerEntity sendingPlayer = ctx.getSender();
	    if (sendingPlayer == null) {
	    	WesterosBlocks.log.warn("EntityPlayerMP was null when AirstrikeMessageToServer was received");
	    }
		// Enqueue processing to happen on server thread next tick
		ctx.enqueueWork(() -> processMessage(sendingPlayer, message));
	}

	// This message is called from the Client thread.
	// It spawns a number of Particle particles at the target location within a
	// short range around the target location
	private static void processMessage(ServerPlayerEntity sendingPlayer, PTimeMessage message) {
		WesterosBlocks.log.info("Got PTimeMessage");
		return;
	}

	public static boolean isThisProtocolAcceptedByServer(String protocolVersion) {
		return WesterosBlocks.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
	}
}