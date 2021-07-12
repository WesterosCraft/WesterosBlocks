package com.westeroscraft.westerosblocks.commands;

import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.PTimeCmdMsgPacket;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
		return "ptime [reset|day|night|dawn|17:30|4pm|4000ticks]"; 
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
        		EntityPlayerMP player = (EntityPlayerMP) sender;
        		Long off = null;
        		boolean rel = true;
        		if (args.length != 1) {
        			throw new WrongUsageException("Requires one argument");
        		}
        		String timearg = args[0];
        		if (timearg.startsWith("@")) { // Is fixed?
    				rel = false;
    				timearg = timearg.substring(1);
        		}
        		// Parse it
        		try {
        			off = timestrToTick(timearg);
    			} catch (IllegalArgumentException x) {
        			throw new WrongUsageException(x.getMessage());
        		}
        		if (off == null) {	// Doing reset
        			off = Long.valueOf(0);
        			rel = true;
        			sender.sendMessage(new TextComponentString("Resetting player time to world time"));
        		}
        		else if (rel) {
        			sender.sendMessage(new TextComponentString("Setting player time relative to world time"));
        			long curtime = player.getServerWorld().getWorldTime();
        			off = (off - curtime + 24000) % 24000;
        		}
        		else {
        			sender.sendMessage(new TextComponentString("Setting player time to fixed"));
        		}
        		PTimeCmdMsgPacket.sendCmdMessage(player, rel, off.intValue());
        	}
        	else {
        		WesterosBlocks.log.info("Command only usable by player");
        	}
        } 			
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
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
	
	public static final int ticksAtMidnight = 18000;
	public static final int ticksPerDay = 24000;
	public static final int ticksPerHour = 1000;

	/**
	 * Parse time string, including reset and time-of-day aliases
	 * 
	 * @param timestr - ptime argument
	 * @return null if reset, ticks otherwise
	 * @throws IllegalArgumentException if invalid format
	 */
	public static Long timestrToTick(String timestr) throws IllegalArgumentException {
		timestr = timestr.toLowerCase();
		switch(timestr) {
			// Reset
			case "reset":
			case "normal":
			case "default":
				return null;
			case "sunrise":
			case "dawn":
				return Long.valueOf(23000);
			case "daystart":
			case "day":
				return Long.valueOf(0);
			case "morning":
				return Long.valueOf(1000);
			case "midday":
			case "noon":
				return Long.valueOf(6000);
			case "afternoon":
				return Long.valueOf(9000);
			case "sunset":
			case "dusk":
			case "sundown":
			case "nightfall":
				return Long.valueOf(12000);
			case "nightstart":
			case "night":
				return Long.valueOf(14000);
			case "midnight":
				return Long.valueOf(18000);
		}
		// If ticks (#t or #ticks)
		if(timestr.matches("^[0-9]+ti?c?k?s?$")) {
			return Long.parseLong(timestr.replaceAll("[^0-9]", "")) % 24000;
		}
		// If 24 hour format
		else if(timestr.matches("^[0-9]{2}[^0-9]?[0-9]{2}$")) {
			String pname = timestr.replaceAll("[^0-9]", "");
			if (pname.length() != 4) {
				throw new IllegalArgumentException("Bad 24 hour time - " + timestr);
			}
			return hoursMinutesToTicks(Integer.parseInt(pname.substring(0, 2)), Integer.parseInt(pname.substring(2, 4)));
		}
		// If 12 hour format
		else if(timestr.matches("^[0-9]{1,2}([^0-9]?[0-9]{2})?(pm|am)$")) {
			String ptime = timestr.replaceAll("[^0-9]", "");	// Strip am/pm
			int hrs = 0, mins = 0;
			switch(ptime.length()) {
				case 4: // hhmm
					hrs = Integer.parseInt(ptime.substring(0, 2));
					mins = Integer.parseInt(ptime.substring(2, 4));
					break;
				case 3: // hmm
					hrs = Integer.parseInt(ptime.substring(0, 1));
					mins = Integer.parseInt(ptime.substring(1, 3));
					break;
				case 2: // hh
				case 1: // h
					hrs = Integer.parseInt(ptime);
					break;
				default:
					throw new IllegalArgumentException("Bad 12 hour time - " + timestr);
			}
			// Add 12 hours for anything PM except 12
			if (timestr.endsWith("pm") && (hrs != 12)) {
				hrs += 12;	
			}
			// Subtract 12 for 12 AM
			else if (timestr.endsWith("am") && (hrs == 12)) {
				hrs -= 12;
			}
			return hoursMinutesToTicks(hrs, mins);
		}
		else {
			throw new IllegalArgumentException("Bad time - " + timestr);
		}
	}
	
	public static long hoursMinutesToTicks(int hours, int min) {
		return (ticksAtMidnight + (hours * ticksPerHour) + ((min * ticksPerHour)/60)) % ticksPerDay;
	}

	// Implementation of hooks for processing worldtime changes
    public static void setTimeOffset(boolean relative, int offset_ticks) {
		relative_off = relative;
		time_off = offset_ticks;
    }
    
    private static boolean relative_off = true;
    private static long time_off = 0;
    
    public static long processWorldTime(long wt) {
    	if (wt >= 0) {
    		if (relative_off) {
    			wt = (wt + 24000 + time_off) % 24000;
    		}
    		else {
    			wt = wt - (wt % 24000) + time_off;
    		}
    	}
        return wt;
    }

    public static long processTotalWorldTime(long twt) {
		return twt;
    }
}