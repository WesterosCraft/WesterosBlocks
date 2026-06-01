package com.westerosblocks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.westerosblocks.WesterosBlocks;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Writes the current block names and their state ids to {@code known_blocks.json} in the
 * config directory. That file is the baseline consumed by {@link BlockCompatibilityValidator}'s
 * world-save subsume check.
 *
 * <p>Workflow: set {@code exportKnownBlocks: true} in the config, launch the game (or run
 * datagen) once, then copy the emitted {@code known_blocks.json} into
 * {@code src/main/resources/definitions/} and commit it. Do this only when you intentionally
 * add, remove, or rename blocks and want to re-establish the "last known good" baseline &mdash;
 * the equivalent of updating the 1.18.2 source's {@code oldWesterosBlocks.json}.
 */
public final class KnownBlocksExporter {

    private static final String FILENAME = "known_blocks.json";

    private KnownBlocksExporter() {}

    private static final class KnownBlocksFile {
        // TreeMap keeps the output stable and diff-friendly across runs.
        final Map<String, List<String>> blocks = new TreeMap<>();
    }

    public static void export() {
        File outputFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), FILENAME);
        WesterosBlocks.LOGGER.info("Exporting known-blocks baseline to: {}", outputFile.getAbsolutePath());

        KnownBlocksFile out = new KnownBlocksFile();
        for (BlockDefinition def : BlockDefinitionRegistry.getInstance().getAllDefinitions()) {
            if (def == null || def.getBlockName() == null) {
                continue;
            }
            out.blocks.put(def.getBlockName(), new ArrayList<>(BlockCompatibilityValidator.stateIdsOf(def)));
        }

        try (FileWriter writer = new FileWriter(outputFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(out, writer);
            WesterosBlocks.LOGGER.info("Exported {} known blocks to {} - copy into src/main/resources/definitions/ to "
                + "arm the world-compatibility guard.", out.blocks.size(), FILENAME);
        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to export " + FILENAME, e);
        }
    }
}
