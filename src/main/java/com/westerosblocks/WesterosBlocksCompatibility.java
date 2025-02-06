package com.westerosblocks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.block.ModBlockSetDef;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;

// Contains methods for generating config files and other artifacts needed for compatibility with external mods
public class WesterosBlocksCompatibility {
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    public static final String BLOCKSET_PATH = "blocksets.json";
    public static final String WORLDPAINTER_PATH = "westerosblocks_worldpainter.csv";
    public static final String[] WORLDPAINTER_COLS = {
            "name", "discriminator", "properties", "opacity", "receivesLight", "insubstantial",
            "resource", "tileEntity", "tileEntityId", "treeRelated", "vegetation", "blockLight",
            "natural", "watery", "colour", "horizontal_orientation_schemes", "vertical_orientation_scheme"
    };

    /*
     * The following allows block set information to be dumped to a json for
     * the purpose of supporting external tools
     */
    private static class BlockSetFileDef {
        public String id = "";
        public String variant = "";
    }

    private static class BlockSetFileSetDef {
        public String id = "";
        public String altname = "";
        public List<BlockSetFileDef> blocks = new ArrayList<BlockSetFileDef>();
    }

    private static class BlockSetFile {
        public List<BlockSetFileSetDef> blocksets = new ArrayList<BlockSetFileSetDef>();
    }

    /*
     * Dump information about block sets for external tools
     */
    public static void dumpBlockSets(ModBlockSetDef[] blockSets, Path path) {
        FileWriter fos = null;
        try {
            // Create output file format
            BlockSetFile bsf = new BlockSetFile();
            for (ModBlockSetDef blockSet : blockSets) {
                BlockSetFileSetDef bsf_set = new BlockSetFileSetDef();
                bsf_set.id = WesterosBlocks.MOD_ID + ":" + blockSet.baseBlockName;
                if (blockSet.baseLabel != null) {
                    bsf_set.altname = blockSet.baseLabel.replaceAll(" ", "_").toLowerCase();
                }
                // The following is duplicated from generateBlockDefs and can perhaps be refactored
                for (String variant : ModBlockSetDef.SUPPORTED_VARIANTS) {
                    if (blockSet.variants != null && !blockSet.variants.contains(variant))
                        continue;
                    else if (blockSet.variants == null && !ModBlockSetDef.DEFAULT_VARIANTS.contains(variant))
                        continue;
                    BlockSetFileDef bsf_def = new BlockSetFileDef();
                    String suffix = (variant.equals("solid")) ? "" : variant;
                    if (blockSet.altNames != null && blockSet.altNames.containsKey(variant)) {
                        bsf_def.id = WesterosBlocks.MOD_ID + ":" + blockSet.altNames.get(variant);
                    } else {
                        bsf_def.id = WesterosBlocks.MOD_ID + ":" + blockSet.baseBlockName;
                        if (!suffix.isEmpty())
                            bsf_def.id += "_" + suffix;
                    }
                    bsf_def.variant = variant;
                    bsf_set.blocks.add(bsf_def);
                }
                bsf.blocksets.add(bsf_set);
            }
            // Write json
            fos = new FileWriter(new File(path.toFile(), BLOCKSET_PATH));
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(bsf, fos);

        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Could not write " + BLOCKSET_PATH);

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    return;
                }
            }
        }
    }

    // TODO make this work
    /*
     * Dump a CustomBlocks config file for WorldPainter
     * https://www.worldpainter.net/trac/wiki/CustomBlocks
     */
    public static void dumpWorldPainterConfig(Block[] blocks) {
        List<Map<String, Object>> data = new ArrayList<>();
        Path wpPath = CONFIG_DIR.resolve(WORLDPAINTER_PATH);

        for (Block blk : blocks) {
            if (!(blk instanceof ModBlockLifecycle)) {
                continue;
            }

            ModBlock def = ((ModBlockLifecycle) blk).getWBDefinition();

            StateManager<Block, BlockState> stateManager = blk.getStateManager();
            Collection<Property<?>> properties = stateManager.getProperties();

            Map<String, Object> row = new HashMap<>();
            row.put("name", WesterosBlocks.MOD_ID + ":" + def.blockName);

            // Handle block properties
            if (!properties.isEmpty()) {
                StringBuilder propStr = new StringBuilder();
                for (Property<?> prop : properties) {
                    if (!propStr.isEmpty()) {
                        propStr.append(",");
                    }
                    propStr.append(prop.getName());
                }
                row.put("properties", propStr.toString());
            } else {
                row.put("properties", null);
            }

            // Block characteristics
            row.put("opacity", def.lightOpacity);
            row.put("receivesLight", !def.nonOpaque);
            row.put("insubstantial", def.nonOpaque);
            row.put("blockLight", def.lightValue > 0 ? Math.round(15 * def.lightValue) : 0);

            // Default values for other fields
            row.put("discriminator", null);
            row.put("resource", null);
            row.put("tileEntity", null);
            row.put("tileEntityId", null);
            row.put("treeRelated", null);
            row.put("vegetation", null);
            row.put("natural", null);
            row.put("watery", null);
            row.put("colour", null);
            row.put("horizontal_orientation_schemes", null);
            row.put("vertical_orientation_scheme", null);

            data.add(row);
        }

        try {
            writeCSV(data, WORLDPAINTER_COLS, wpPath.toFile());
        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Could not write {}", WORLDPAINTER_PATH, e);
        }
    }


    public static void writeCSV(List<Map<String, Object>> data, String[] columns, File file)
            throws IOException {
        String out = String.join(",", columns) + "\n";

        for (Map<String, Object> d : data) {
            ArrayList<String> row = new ArrayList<String>();
            for (String c : columns) {
                if (d.containsKey(c) && d.get(c) != null) {
                    Object val = d.get(c);
                    if (val instanceof Integer)
                        row.add(String.valueOf(val));
                    else if (val instanceof Boolean)
                        row.add(((Boolean) val) ? "true" : "false");
                    else
                        row.add("\"" + val + "\"");
                } else {
                    row.add("");
                }
            }
            out += String.join(",", row) + "\n";
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(out);
        writer.close();
    }
}