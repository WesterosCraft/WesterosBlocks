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
import com.westerosblocks.block.ModBlockSet;
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
            "natural", "watery", "colour"
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
    public static void dumpBlockSets(ModBlockSet[] blockSets) {
        FileWriter fos = null;
        try {
            // Create output file format
            BlockSetFile bsf = new BlockSetFile();
            for (ModBlockSet blockSet : blockSets) {
                BlockSetFileSetDef bsf_set = new BlockSetFileSetDef();
                bsf_set.id = WesterosBlocks.MOD_ID + ":" + blockSet.baseBlockName;
                if (blockSet.baseLabel != null) {
                    bsf_set.altname = blockSet.baseLabel.replaceAll(" ", "_").toLowerCase();
                }
                // The following is duplicated from generateBlockDefs and can perhaps be refactored
                for (String variant : ModBlockSet.SUPPORTED_VARIANTS) {
                    if (blockSet.variants != null && !blockSet.variants.contains(variant))
                        continue;
                    else if (blockSet.variants == null && !ModBlockSet.DEFAULT_VARIANTS.contains(variant))
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
            fos = new FileWriter(new File(CONFIG_DIR.toFile(), BLOCKSET_PATH));
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(bsf, fos);

        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Could not write " + BLOCKSET_PATH);

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    WesterosBlocks.LOGGER.warn("error closing file writer");
                }
            }
        }
    }

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

            // Handle block properties with proper type definitions
            if (!properties.isEmpty()) {
                StringBuilder propStr = new StringBuilder();
                for (Property<?> prop : properties) {
                    if (!propStr.isEmpty()) {
                        propStr.append(",");
                    }
                    String propName = prop.getName();
                    String propType = getPropertyType(prop);
                    propStr.append(propName).append(":").append(propType);
                }
                row.put("properties", propStr.toString());
            } else {
                row.put("properties", null);
            }

            // Block characteristics
            row.put("opacity", def.lightOpacity != ModBlock.DEF_INT ? def.lightOpacity : 15);
            row.put("receivesLight", !def.nonOpaque);
            row.put("insubstantial", def.nonOpaque);
            row.put("blockLight", def.lightValue > 0 ? (int)def.lightValue : 0);

            // Determine if block is a resource (ore-like)
            boolean isResource = isResourceBlock(def);
            row.put("resource", isResource);

            // Determine if block is tile entity
            boolean isTileEntity = isTileEntityBlock(def);
            row.put("tileEntity", isTileEntity);
            row.put("tileEntityId", isTileEntity ? WesterosBlocks.MOD_ID + ":" + def.blockName : null);

            // Determine if block is tree-related
            boolean isTreeRelated = isTreeRelatedBlock(def);
            row.put("treeRelated", isTreeRelated);

            // Determine if block is vegetation
            boolean isVegetation = isVegetationBlock(def);
            row.put("vegetation", isVegetation);

            // Determine if block is natural
            boolean isNatural = isNaturalBlock(def);
            row.put("natural", isNatural);

            // Determine if block is watery
            boolean isWatery = isWateryBlock(def);
            row.put("watery", isWatery);

            // Set color (magenta for now, can be enhanced later)
            row.put("colour", "ffff00ff");

            // No discriminator needed for now
            row.put("discriminator", null);

            data.add(row);
        }

        try {
            writeCSV(data, WORLDPAINTER_COLS, wpPath.toFile());
            WesterosBlocks.LOGGER.info("WorldPainter CSV exported to: {}", wpPath);
        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Could not write {}", WORLDPAINTER_PATH, e);
        }
    }

    private static String getPropertyType(Property<?> prop) {
        if (prop.getValues().size() == 2 && prop.getValues().contains(true) && prop.getValues().contains(false)) {
            return "b";
        } else if (prop.getValues().stream().allMatch(v -> v instanceof Integer)) {
            int min = prop.getValues().stream().mapToInt(v -> (Integer) v).min().orElse(0);
            int max = prop.getValues().stream().mapToInt(v -> (Integer) v).max().orElse(15);
            return "i[" + min + "-" + max + "]";
        } else {
            // Enumeration
            String values = prop.getValues().stream()
                    .map(Object::toString)
                    .collect(java.util.stream.Collectors.joining(";"));
            return "e[" + values + "]";
        }
    }

    private static boolean isResourceBlock(ModBlock def) {
        if (def.customTags != null) {
            return def.customTags.contains("ore") || def.customTags.contains("resource");
        }
        return def.blockName.contains("ore") || def.blockName.contains("gem") || 
               def.blockName.contains("crystal") || def.blockName.contains("mineral");
    }

    private static boolean isTileEntityBlock(ModBlock def) {
        return "furnace".equals(def.blockType) || "beacon".equals(def.blockType) || 
               def.blockName.contains("furnace") || def.blockName.contains("beacon") ||
               def.blockName.contains("chest") || def.blockName.contains("barrel");
    }

    private static boolean isTreeRelatedBlock(ModBlock def) {
        if (def.customTags != null) {
            return def.customTags.contains("tree") || def.customTags.contains("log") || 
                   def.customTags.contains("leaves") || def.customTags.contains("wood");
        }
        return "log".equals(def.blockType) || "leaves".equals(def.blockType) || 
               def.blockName.contains("log") || def.blockName.contains("leaves") ||
               def.blockName.contains("wood") || def.blockName.contains("tree");
    }

    private static boolean isVegetationBlock(ModBlock def) {
        if (def.customTags != null) {
            return def.customTags.contains("plant") || def.customTags.contains("flower") || 
                   def.customTags.contains("crop") || def.customTags.contains("vegetation");
        }
        return "plant".equals(def.blockType) || def.blockName.contains("plant") || 
               def.blockName.contains("flower") || def.blockName.contains("crop") ||
               def.blockName.contains("grass") || def.blockName.contains("vine");
    }

    private static boolean isNaturalBlock(ModBlock def) {
        if (def.customTags != null) {
            return !def.customTags.contains("manmade") && !def.customTags.contains("artificial");
        }
        // Assume natural unless it's clearly manmade
        return !def.blockName.contains("brick") && !def.blockName.contains("concrete") && 
               !def.blockName.contains("metal") && !def.blockName.contains("furniture");
    }

    private static boolean isWateryBlock(ModBlock def) {
        if (def.customTags != null) {
            return def.customTags.contains("water") || def.customTags.contains("liquid");
        }
        return "liquid".equals(def.blockType) || def.blockName.contains("water") || 
               def.blockName.contains("lava") || def.blockName.contains("liquid");
    }

    public static void writeCSV(List<Map<String, Object>> data, String[] columns, File file)
            throws IOException {
        StringBuilder out = new StringBuilder(String.join(",", columns) + "\n");

        for (Map<String, Object> d : data) {
            ArrayList<String> row = new ArrayList<String>();
            for (String c : columns) {
                if (d.containsKey(c) && d.get(c) != null) {
                    Object val = d.get(c);
                    if (val instanceof Integer)
                        row.add(String.valueOf(val));
                    else if (val instanceof Boolean)
                        row.add(((Boolean) val) ? "true" : "false");
                    else {
                        String strVal = val.toString();
                        // Only quote strings that contain commas, quotes, or newlines
                        if (strVal.contains(",") || strVal.contains("\"") || strVal.contains("\n")) {
                            row.add("\"" + strVal.replace("\"", "\"\"") + "\"");
                        } else {
                            row.add(strVal);
                        }
                    }
                } else {
                    row.add("");
                }
            }
            out.append(String.join(",", row)).append("\n");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(out.toString());
        writer.close();
    }
}