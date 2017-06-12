package com.westeroscraft.westerosblocks.commands;

import java.awt.TextComponent;
import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.WesterosBlocksChannelHandler;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PTimeCommand implements ICommand {
	private final List<String> aliases;
	
    public PTimeCommand() { 
        aliases = new ArrayList<String>(); 
        aliases.add("ptime"); 
    } 
  
	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getName() {
		return "ptime";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "ptime [list|reset|day|night|dawn|17:30|4pm|4000ticks]"; 
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = sender.getEntityWorld(); 
        
        if (!world.isRemote) { 
        	if (sender instanceof EntityPlayerMP) {
        		Long off = null;
        		Boolean rel = null;
        		
        		WesterosBlocksChannelHandler.sendPTimeCmdMessage((EntityPlayerMP) sender, true, 12000);
        	}
        	else {
        		WesterosBlocks.log.info("Command onlu usable by player");
        	}
        } 			
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		WesterosBlocks.log.info("PTimeCommand.checkPermission()"); 
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}