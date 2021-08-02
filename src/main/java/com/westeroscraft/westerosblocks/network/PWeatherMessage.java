package com.westeroscraft.westerosblocks.network;

import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.westeroscraft.westerosblocks.WesterosBlocks;

public class PWeatherMessage {
	public static int PWEATHER_MSGID = 0x02;
	public enum WeatherCond {
		RESET, CLEAR, RAIN, THUNDER
	};
	public static final WeatherCond[] weathercondlist = WeatherCond.values();
	
	public WeatherCond weather;
	
	public PWeatherMessage(WeatherCond wthr) {
		weather = wthr;
	}

	public PWeatherMessage() {

	}

	public static PWeatherMessage decode(PacketBuffer buf) {
		try {
			int idx = buf.readInt();
			return new PWeatherMessage(weathercondlist[idx]);
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			WesterosBlocks.log.warn("Exception while reading PTimeMessage: " + e);
			return null;
		}
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(this.weather.ordinal());
	}

	@Override
	public String toString() {
		return "PWeatherMessage[" + weather + "]";
	}
}