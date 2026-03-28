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

/**
 * Exports block set information to blocksets.json for external tools.
 * Matches the format from the 1.18.2 WesterosBlocksCompatibility.dumpBlockSets().
 */
public class BlockSetExporter {

    private static final String BLOCKSET_FILENAME = "blocksets.json";

    private static class BlockSetFileDef {
        public String id = "";
        public String variant = "";
    }

    private static class BlockSetFileSetDef {
        public String id = "";
        public String altname = "";
        public List<BlockSetFileDef> blocks = new ArrayList<>();
    }

    private static class BlockSetFile {
        public List<BlockSetFileSetDef> blocksets = new ArrayList<>();
    }

    public static void export() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File outputFile = new File(configDir, BLOCKSET_FILENAME);

        WesterosBlocks.LOGGER.info("Exporting block sets to: {}", outputFile.getAbsolutePath());

        List<BlockSetDefinition> blockSets = BlockDefinitionRegistry.getInstance().getBlockSetDefinitions();

        BlockSetFile bsf = new BlockSetFile();
        for (BlockSetDefinition blockSet : blockSets) {
            BlockSetFileSetDef bsfSet = new BlockSetFileSetDef();
            bsfSet.id = WesterosBlocks.MOD_ID + ":" + blockSet.getBaseBlockName();
            if (blockSet.getBaseLabel() != null) {
                bsfSet.altname = blockSet.getBaseLabel().replaceAll(" ", "_").toLowerCase();
            }

            List<String> variantsToCreate = blockSet.hasVariants()
                    ? blockSet.getVariants()
                    : BlockSetExpander.DEFAULT_VARIANTS;

            for (String variant : variantsToCreate) {
                if (!BlockSetExpander.SUPPORTED_VARIANTS.contains(variant)) continue;

                BlockSetFileDef bsfDef = new BlockSetFileDef();
                Map<String, String> altNames = blockSet.getAltNames();
                if (altNames != null && altNames.containsKey(variant)) {
                    bsfDef.id = WesterosBlocks.MOD_ID + ":" + altNames.get(variant);
                } else {
                    String suffix = variant.equals("solid") ? "" : "_" + variant;
                    bsfDef.id = WesterosBlocks.MOD_ID + ":" + blockSet.getBaseBlockName() + suffix;
                }
                bsfDef.variant = variant;
                bsfSet.blocks.add(bsfDef);
            }

            bsf.blocksets.add(bsfSet);
        }

        try (FileWriter writer = new FileWriter(outputFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(bsf, writer);
            WesterosBlocks.LOGGER.info("Successfully exported {} block sets to {}", blockSets.size(), BLOCKSET_FILENAME);
        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to export " + BLOCKSET_FILENAME, e);
        }
    }
}
