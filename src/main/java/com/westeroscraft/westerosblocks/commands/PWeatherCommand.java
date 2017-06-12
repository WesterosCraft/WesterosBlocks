package com.westeroscraft.westerosblocks.commands;

import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.network.PWeatherCmdMsgPacket;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PWeatherCommand implements ICommand {
	public enum PWEATHER_SETTING {
		RESET,
		CLEAR,
		RAIN,
		THUNDER };	
	
	private final List<String> aliases;
	
    public PWeatherCommand() { 
        aliases = new ArrayList<String>(); 
        aliases.add("pweather"); 
    } 
  
	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getName() {
		return "pweather";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "pweather [reset|sun|clear|storm|thunder]"; 
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
        		if (args.length != 1) {
        			throw new WrongUsageException("Requires one argument");
        		}
        		PWEATHER_SETTING setting = PWEATHER_SETTING.RESET;
        		switch (args[0]) {
        			case "reset":
        				setting = PWEATHER_SETTING.RESET;
            			sender.sendMessage(new TextComponentString("Resetting player weather to match server weather"));
        				break;
        			case "clear":
        			case "sun":
        				setting = PWEATHER_SETTING.CLEAR;
            			sender.sendMessage(new TextComponentString("Setting player weather to CLEAR"));
        				break;
        			case "rain":
        			case "storm":
        				setting = PWEATHER_SETTING.RAIN;
            			sender.sendMessage(new TextComponentString("Setting player weather to RAIN"));
        				break;
        			case "thunder":
        				setting = PWEATHER_SETTING.THUNDER;
            			sender.sendMessage(new TextComponentString("Setting player weather to THUNDER"));
        				break;
        			default:
        				throw new WrongUsageException("Invalid argument - " + args[0]);
        		}
        		PWeatherCmdMsgPacket.sendCmdMessage(player, setting);
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
    
	/**
	 * Update weather setting
	 * @param setting
	 */
	public static void setWeatherSetting(PWEATHER_SETTING setting, World world) {
		// First, if existing setting is not RESET, restore saved settings
		if (weather_setting != PWEATHER_SETTING.RESET) {
			if ((world != null) && (world == weather_world)) {	// Same world?
				world.getWorldInfo().setRaining(saved_raining);
				world.setRainStrength(saved_rain_strength);
				world.setThunderStrength(saved_thunder_strength);
			}
			weather_world = null;
			weather_setting = PWEATHER_SETTING.RESET;
		}
		// If asserting new setting, save existing ones and apply
		if ((setting != PWEATHER_SETTING.RESET) && (world != null)) {
			weather_world = world;	// Save current world
			saved_raining = world.getWorldInfo().isRaining();
			saved_rain_strength = world.getRainStrength(0);
			saved_thunder_strength = world.getThunderStrength(0);
			// Apply new values
			world.getWorldInfo().setRaining(setting != PWEATHER_SETTING.CLEAR);
			world.setRainStrength((setting == PWEATHER_SETTING.CLEAR)?0.0f:1.0f);
			world.setThunderStrength((setting == PWEATHER_SETTING.THUNDER)?1.0f:0.0f);
			// Save new setting
			weather_setting = setting;
		}
	}
	
    private static PWEATHER_SETTING weather_setting = PWEATHER_SETTING.RESET;
    private static World weather_world = null;
    
    private static boolean saved_raining = false;
    private static float saved_rain_strength = 0.0F;
    private static float saved_thunder_strength = 0.0F;
    
    public static void processChangeGameState(SPacketChangeGameState pkt) {
    	if ((weather_world != null) && (weather_setting != PWEATHER_SETTING.RESET)) {
    		switch (pkt.state) {
    			case 1:	// Start raining?
    				saved_raining = true;
    				saved_rain_strength = 0.0f;
    				break;
    			case 2: // Stop raining?
    				saved_raining = false;
    				saved_rain_strength = 1.0f;
    				break;
    			case 7: // Rain strength?
    				saved_rain_strength = pkt.value;
    				break;
    			case 8: // Thunder strength?
    				saved_thunder_strength = pkt.value;
    				break;
    		}
    		// Override with either set or clear rain
    		if (weather_setting == PWEATHER_SETTING.CLEAR) {
    			pkt.state = 2;
    			pkt.value = 0.0f;
    		}
    		else {
    			pkt.state = 1;
    			pkt.value = 0.0f;
    		}
    	}
    }
}