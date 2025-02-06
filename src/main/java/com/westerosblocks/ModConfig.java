package com.westerosblocks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

// example of how to run n your code
// boolean autoRestore = ModConfig.INSTANCE.autoRestoreAllHalfDoors;

@Config(name = WesterosBlocks.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("general")
    // TODO find where this was used
    public boolean snowInTaiga = false;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("general")
    public boolean blockDevMode = false;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("general")
    public boolean dumpWorldpainterCSV = false;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("general")
    @ConfigEntry.BoundedDiscrete(min = 5, max = 300)
    public int autoRestoreTime = 30;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("doors")
    public boolean autoRestoreAllHalfDoors = false;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("doors")
    public boolean doorSurviveAny = false;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("doors")
    // TODO find where this was used
    public boolean doorNoConnect = false;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Category("world")
    @ConfigEntry.BoundedDiscrete(min = 5, max = 300)
    public int seaLevelOverride = 33;

    public static ModConfig INSTANCE;

    public static void register() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}