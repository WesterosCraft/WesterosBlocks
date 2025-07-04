package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksDefLoader;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    Map<String, Block> customBlocks = ModBlocks.getCustomBlocks();
    ModBlock[] customBlockDefs = WesterosBlocksDefLoader.getCustomBlockDefs();

    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        // do custom block translations
        for (ModBlock customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
            Block currentBlock = customBlocks.get(customBlockDef.blockName);

            translationBuilder.add(currentBlock, customBlockDef.label);

            // tooltips
            if (customBlockDef.tooltips != null && !customBlockDef.tooltips.isEmpty()) {
                boolean hasMultipleTooltips = customBlockDef.tooltips.size() > 1;

                for (int i = 0; i < customBlockDef.tooltips.size(); i++) {
                    translationBuilder.add(String.format("tooltip.%s.%s.tooltip%s",
                                    WesterosBlocks.MOD_ID,
                                    customBlockDef.blockName,
                                    hasMultipleTooltips ? "." + i : ""),
                            customBlockDef.tooltips.get(i));
                }
            }
        }

        // Items
        translationBuilder.add("item.westerosblocks.longclaw", "Longclaw");
        translationBuilder.add("item.westerosblocks.valyrian_steel_ingot", "Valyrian Steel Ingot");
//        translationBuilder.add("item.westerosblocks.lannister_shield", "Lannister Shield");
        translationBuilder.add("item.westerosblocks.stark_kite_shield", "Stark Kite Shield");
        translationBuilder.add("item.westerosblocks.tully_heater_shield", "Tully Heater Shield");
        translationBuilder.add("item.westerosblocks.hedge_knight_heater_shield", "Hedge Knight Heater Shield");
        translationBuilder.add("item.westerosblocks.laughing_tree_heater_shield", "Laughing Tree Heater Shield");
        translationBuilder.add("item.westerosblocks.targaryen_heater_shield", "Targaryen Heater Shield");
        translationBuilder.add("item.westerosblocks.bracken_heater_shield", "Bracken Heater Shield");
        translationBuilder.add("item.westerosblocks.greyjoy_round_shield", "Greyjoy Round Shield");
        translationBuilder.add("item.westerosblocks.blackfyre_heater_shield", "Blackfyre Heater Shield");
        translationBuilder.add("item.westerosblocks.blackwood_heater_shield", "Blackwood Heater Shield");

        // Config translations
        translationBuilder.add("text.autoconfig.westerosblocks.title", "WesterosBlocks Config");
        translationBuilder.add("text.autoconfig.westerosblocks.category.general", "General");
        translationBuilder.add("text.autoconfig.westerosblocks.category.doors", "Doors");
        translationBuilder.add("text.autoconfig.westerosblocks.category.world", "World");
        translationBuilder.add("text.autoconfig.westerosblocks.option.snowInTaiga", "Snow in Taiga");
        translationBuilder.add("text.autoconfig.westerosblocks.option.snowInTaiga.@Tooltip[0]", "Enable snow in taiga biome");
        translationBuilder.add("text.autoconfig.westerosblocks.option.blockDevMode", "Block Dev Mode");
        translationBuilder.add("text.autoconfig.westerosblocks.option.blockDevMode.@Tooltip[0]", "Block development mode");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreTime", "Auto Restore Time");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreTime.@Tooltip[0]", "Number of seconds before auto-restore");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreAllHalfDoors", "Auto Restore All Half-Doors");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreAllHalfDoors.@Tooltip[0]", "Auto restore all half-door blocks");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorSurviveAny", "Door Survive Any");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorSurviveAny.@Tooltip[0]", "Allow door to survive on any surface");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorNoConnect", "Door No Connect");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorNoConnect.@Tooltip[0]", "Avoid doors connecting to walls/panes/etc");
        translationBuilder.add("text.autoconfig.westerosblocks.option.seaLevelOverride", "Sea Level Override");
        translationBuilder.add("text.autoconfig.westerosblocks.option.seaLevelOverride.@Tooltip[0]", "Override sea level (default for Westeros=33, 0=disable override)");

        // Block translations

        translationBuilder.add("block.westerosblocks.arbor_brick_arrow_slit", "Arbor Brick Arrow Slit");
        translationBuilder.add("block.westerosblocks.black_granite_arrow_slit", "Black Granite Arrow Slit");

        translationBuilder.add("block.westerosblocks.oak_table", "Oak Table");
        translationBuilder.add("block.westerosblocks.spruce_table", "Spruce Table");
        translationBuilder.add("block.westerosblocks.birch_table", "Birch Table");

        translationBuilder.add("block.westerosblocks.oak_chair", "Oak Chair");
        translationBuilder.add("block.westerosblocks.birch_chair", "Birch Chair");
        translationBuilder.add("block.westerosblocks.spruce_chair", "Spruce Chair");

        translationBuilder.add("block.westerosblocks.oak_way_sign", "Oak Way Sign");
        translationBuilder.add("block.westerosblocks.wall_oak_way_sign", "Oak Way Sign");

        translationBuilder.add("block.westerosblocks.kings_landing_sewer_manhole", "King's Landing Sewer Manhole");
        translationBuilder.add("block.westerosblocks.oldtown_sewer_manhole", "Oldtown Sewer Manhole");
        translationBuilder.add("block.westerosblocks.sewer_manhole", "Generic Sewer Manhole");
        translationBuilder.add("block.westerosblocks.white_harbor_sewer_manhole", "White Harbor Sewer Manhole");

        // TODO can prob grab the item tag def json and generate this automatically when/if we get more
        // The format desired is tag.item.<namespace>.<path> for the translation key with slashes in path turned into periods."
        translationBuilder.add("tag.item.c.shield", "Shield");
    }
}
