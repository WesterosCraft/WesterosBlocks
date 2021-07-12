package com.westeroscraft.westerosblocks.network;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class PacketHandler extends SimpleChannelInboundHandler<WBPacket>  {
    @Override
    protected  void channelRead0(ChannelHandlerContext ctx, WBPacket packet) {
        try {
            INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
            EntityPlayer player = WesterosBlocks.proxy.getPlayerFromNetHandler(netHandler);

            if (packet instanceof WBPacket) {
            	((WBPacket) packet).processPacket(netHandler, player);
            }
            else {
            	WesterosBlocks.log.warn("Unknown packet received: " + packet.getClass().getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}