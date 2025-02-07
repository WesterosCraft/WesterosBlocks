package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksDefLoader;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    HashMap<String, Block> customBlocks = ModBlocks.getCustomBlocksByName();
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

    }
}
