package com.westeroscraft.westerosblocks.network;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.relauncher.Side;

public class WesterosBlocksChannelHandler extends FMLIndexedMessageToMessageCodec<WBPacket> {
    public static final String CHANNEL = "WesterosBlocks";
    public static final byte BLOCKMSG = 0x00;   // Command code for block message

    public WesterosBlocksChannelHandler() {
        addDiscriminator(0, BlockMsgPacket.class);
        addDiscriminator(1, PTimeCmdMsgPacket.class);
        addDiscriminator(2, PWeatherCmdMsgPacket.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, WBPacket packet, ByteBuf data) throws Exception {
        packet.writeData(data);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, WBPacket packet) {
        packet.readData(data);
    }
    
    public static void sendToPlayer(WBPacket packet , EntityPlayerMP player) {
        try {
            WesterosBlocks.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                    .set(FMLOutboundHandler.OutboundTarget.PLAYER);
            WesterosBlocks.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            WesterosBlocks.channels.get(Side.SERVER).writeOutbound(packet);
        } catch (Throwable t) {
            String name = player.getDisplayName().getUnformattedText();

            if (name == null) {
                name = "<no name>";
            }
            WesterosBlocks.log.warn("sentToPlayer \"" + name + "\" error: " + t.getMessage());
        }
    }
}
