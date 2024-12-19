//package com.westerosblocks.needsported;
//
//// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
//// Demonstrates how to use Neo's config APIs
//@EventBusSubscriber(modid = WesterosBlocks.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
//public class Config
//{
//    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
//
//    private static final ModConfigSpec.BooleanValue SNOW_IN_TAIGA = BUILDER
//            .translation("westerosblocks.configuration.snowInTaiga").comment("Enable snow in taiga biome")
//            .define("snowInTaiga", false);
//
//    private static final ModConfigSpec.BooleanValue BLOCK_DEV_MODE = BUILDER
//            .translation("westerosblocks.configuration.blockDevMode").comment("Block development mode")
//            .define("blockDevMode", false);
//
//    private static final ModConfigSpec.IntValue AUTO_RESTORE_TIME = BUILDER
//            .translation("westerosblocks.configuration.autoRestoreTime").comment("Number of seconds before auto-restore")
//            .defineInRange("autoRestoreTime", 30, 5, 300);
//
//    private static final ModConfigSpec.BooleanValue AUTO_RESTORE_ALL_HALF_DOORS = BUILDER
//            .translation("westerosblocks.configuration.autoRestoreAllHalfDoors").comment("Auto restore all half-door blocks")
//            .define("autoRestoreAllHalfDoors", false);
//
//    private static final ModConfigSpec.BooleanValue DOOR_SURVIVE_ANY = BUILDER
//            .translation("westerosblocks.configuration.doorSurviveAny").comment("Allow door to survive on any surface")
//            .define("doorSurviveAny", false);
//
//    private static final ModConfigSpec.BooleanValue DOOR_NO_CONNECT = BUILDER
//            .translation("westerosblocks.configuration.doorNoConnect").comment("Avoid doors connecting to walls/panes/etc")
//            .define("doorNoConnect", false);
//
//    private static final ModConfigSpec.IntValue SEA_LEVEL_OVERRIDE = BUILDER
//            .translation("westerosblocks.configuration.seaLevelOverride").comment("Override sea level (default for Westeros=33, 0=disable override)")
//            .defineInRange("seaLevelOverride", 30, 5, 300);
//
//
//    static final ModConfigSpec SPEC = BUILDER.build();
//
//    public static boolean snowInTaiga;
//    public static boolean blockDevMode;
//    public static int autoRestoreTime;
//    public static boolean autoRestoreAllHalfDoors;
//    public static boolean doorSurviveAny;
//    public static boolean doorNoConnect;
//    public static int seaLevelOverride;
//
//
//
//    @SubscribeEvent
//    static void onLoad(final ModConfigEvent event)
//    {
//        snowInTaiga = SNOW_IN_TAIGA.get();
//        blockDevMode = BLOCK_DEV_MODE.get();
//        autoRestoreTime = AUTO_RESTORE_TIME.get();
//        autoRestoreAllHalfDoors = AUTO_RESTORE_ALL_HALF_DOORS.get();
//        doorSurviveAny = DOOR_SURVIVE_ANY.get();
//        doorNoConnect = DOOR_NO_CONNECT.get();
//        seaLevelOverride = SEA_LEVEL_OVERRIDE.get();
//    }
//}
