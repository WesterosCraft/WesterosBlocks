package com.westerosblocks.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModConfig config = ModConfig.load();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("config.westerosblocks.title"))
                    .setSavingRunnable(config::save);

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // General Settings Category
            ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.westerosblocks.category.general"));

            general.addEntry(entryBuilder.startBooleanToggle(
                    Text.translatable("config.westerosblocks.dumpWorldPainterCSV"),
                    config.dumpWorldPainterCSV)
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("config.westerosblocks.dumpWorldPainterCSV.tooltip"))
                    .setSaveConsumer(newValue -> config.dumpWorldPainterCSV = newValue)
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(
                    Text.translatable("config.westerosblocks.translucencyPaintings"),
                    config.translucencyPaintings)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.westerosblocks.translucencyPaintings.tooltip"))
                    .setSaveConsumer(newValue -> config.translucencyPaintings = newValue)
                    .build());

            return builder.build();
        };
    }
}
