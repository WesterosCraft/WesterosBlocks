package com.westerosblocks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;


// TODO: Example of how to use the config
// boolean snowInTaiga = ModConfig.get().snowInTaiga;

@Config(name = WesterosBlocks.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean snowInTaiga = false;

    @ConfigEntry.Gui.Tooltip
    public boolean blockDevMode = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 5, max = 300)
    public int autoRestoreTime = 30;

    @ConfigEntry.Gui.Tooltip
    public boolean autoRestoreAllHalfDoors = false;

    @ConfigEntry.Gui.Tooltip
    public boolean doorSurviveAny = false;

    @ConfigEntry.Gui.Tooltip
    public boolean doorNoConnect = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 5, max = 300)
    public int seaLevelOverride = 30;

    public static class Initializer implements ModInitializer {
        @Override
        public void onInitialize() {
            // Register the config
            AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        }
    }

    // Singleton access method
    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
