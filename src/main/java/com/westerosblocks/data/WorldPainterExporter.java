package com.westerosblocks.data;

import com.westerosblocks.WesterosBlocks;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Exports block definitions to a WorldPainter-compatible CSV file.
 * See: https://www.worldpainter.net/trac/wiki/CustomBlocks
 */
public class WorldPainterExporter {

    private static final String CSV_FILENAME = "westerosblocks-worldpainter.csv";

    /**
     * Export all registered blocks to a WorldPainter CSV file in the config directory.
     */
    public static void exportToCSV() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File csvFile = new File(configDir, CSV_FILENAME);

        WesterosBlocks.LOGGER.info("Exporting blocks to WorldPainter CSV: {}", csvFile.getAbsolutePath());

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write CSV header
            writeHeader(writer);

            // Get all block definitions
            Collection<BlockDefinition> definitions = BlockDefinitionRegistry.getInstance().getAllDefinitions();
            WesterosBlocks.LOGGER.info("Exporting {} block definitions...", definitions.size());

            // Write each block definition
            for (BlockDefinition def : definitions) {
                writeBlockRow(writer, def);
            }

            WesterosBlocks.LOGGER.info("Successfully exported {} blocks to {}", definitions.size(), csvFile.getName());

        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to export WorldPainter CSV", e);
        }
    }

    /**
     * Write the CSV header row
     */
    private static void writeHeader(FileWriter writer) throws IOException {
        List<String> headers = new ArrayList<>();
        headers.add("name");                    // Mandatory: block namespace and name
        headers.add("opacity");                 // Light blocking (0-15)
        headers.add("receivesLight");           // Whether fully opaque block is lit
        headers.add("insubstantial");           // If block can be easily replaced
        headers.add("blockLight");              // Light emission (0-15)
        headers.add("colour");                  // Rendering color in hex
        headers.add("properties");              // Block properties (for variants)
        headers.add("horizontal_orientation_schemes"); // Rotation support
        headers.add("vertical_orientation_scheme");    // Vertical mirroring
        headers.add("tileEntity");              // Has special behavior
        headers.add("transparency");            // For translucent blocks

        writer.write(String.join(",", headers));
        writer.write("\n");
    }

    /**
     * Write a single block definition as a CSV row
     */
    private static void writeBlockRow(FileWriter writer, BlockDefinition def) throws IOException {
        List<String> values = new ArrayList<>();

        // Name (mandatory) - namespace:blockname format
        values.add(quote("westerosblocks:" + def.getBlockName()));

        // Opacity (0-15) - 15 for solid, 0 for transparent
        boolean isOpaque = !def.isNonOpaque();
        values.add(String.valueOf(isOpaque ? 15 : 0));

        // Receives light - true if opaque (counter-intuitive but correct per WorldPainter)
        values.add(String.valueOf(isOpaque));

        // Insubstantial - can be easily replaced (plants, torches, etc.)
        boolean insubstantial = isInsubstantial(def);
        values.add(String.valueOf(insubstantial));

        // Block light (0-15)
        int luminance = def.getLuminance();
        values.add(String.valueOf(luminance));

        // Color (hex format)
        String color = getColorValue(def);
        values.add(color.isEmpty() ? "" : quote(color));

        // Properties - block states if any
        String properties = getProperties(def);
        values.add(properties.isEmpty() ? "" : quote(properties));

        // Horizontal orientation - for directional blocks
        String horizontalOrientation = getHorizontalOrientation(def);
        values.add(horizontalOrientation.isEmpty() ? "" : quote(horizontalOrientation));

        // Vertical orientation - for logs and pillars
        String verticalOrientation = getVerticalOrientation(def);
        values.add(verticalOrientation.isEmpty() ? "" : quote(verticalOrientation));

        // Tile entity - for special blocks (furnaces, etc.)
        boolean hasTileEntity = hasTileEntity(def);
        values.add(String.valueOf(hasTileEntity));

        // Transparency - for translucent blocks
        boolean hasTransparency = hasTransparency(def);
        values.add(String.valueOf(hasTransparency));

        writer.write(String.join(",", values));
        writer.write("\n");
    }

    /**
     * Determine if a block is insubstantial (can be easily replaced)
     */
    private static boolean isInsubstantial(BlockDefinition def) {
        String blockType = def.getBlockType();

        // Plants, torches, crops, vines, etc. are insubstantial
        return blockType.equals("plant") ||
               blockType.equals("crop") ||
               blockType.equals("torch") ||
               blockType.equals("fire") ||
               blockType.equals("vines") ||
               blockType.equals("rail") ||
               blockType.equals("fan") ||
               def.hasNoCollision();
    }

    /**
     * Get color value from block definition
     */
    private static String getColorValue(BlockDefinition def) {
        if (def.getColorMult() != null && !def.getColorMult().isEmpty()) {
            return def.getColorMult(); // Already in hex format
        }
        return "";
    }

    /**
     * Get block properties string (for blocks with multiple states)
     */
    private static String getProperties(BlockDefinition def) {
        List<String> properties = new ArrayList<>();

        String blockType = def.getBlockType();

        // Add common properties based on block type
        switch (blockType) {
            case "stair":
                properties.add("facing=north,south,east,west");
                properties.add("half=top,bottom");
                properties.add("shape=straight,inner_left,inner_right,outer_left,outer_right");
                break;
            case "slab":
                properties.add("type=top,bottom,double");
                break;
            case "door":
            case "halfdoor":
                properties.add("facing=north,south,east,west");
                properties.add("open=true,false");
                properties.add("hinge=left,right");
                break;
            case "fencegate":
                properties.add("facing=north,south,east,west");
                properties.add("open=true,false");
                break;
            case "log":
                properties.add("axis=x,y,z");
                break;
            case "wall":
                properties.add("north=none,low,tall");
                properties.add("south=none,low,tall");
                properties.add("east=none,low,tall");
                properties.add("west=none,low,tall");
                properties.add("up=true,false");
                break;
            case "fence":
            case "pane":
                properties.add("north=true,false");
                properties.add("south=true,false");
                properties.add("east=true,false");
                properties.add("west=true,false");
                break;
            case "ladder":
            case "torch":
                properties.add("facing=north,south,east,west");
                break;
            case "layer":
                properties.add("layers=1,2,3,4,5,6,7,8");
                break;
        }

        // Add state properties if the block has states
        if (def.hasStates()) {
            List<String> stateIds = new ArrayList<>();
            for (BlockDefinition.StateVariant state : def.getStates()) {
                stateIds.add(state.getStateID());
            }
            if (!stateIds.isEmpty()) {
                properties.add("state=" + String.join(",", stateIds));
            }
        }

        return String.join(";", properties);
    }

    /**
     * Get horizontal orientation scheme
     */
    private static String getHorizontalOrientation(BlockDefinition def) {
        String blockType = def.getBlockType();

        // Blocks that can be rotated horizontally
        switch (blockType) {
            case "stair":
            case "door":
            case "halfdoor":
            case "fencegate":
            case "ladder":
            case "furnace":
            case "bed":
            case "chair":
                return "CARDINAL_DIRECTIONS";
            case "log":
                return "CARDINAL_DIRECTIONS,CARDINAL_DIRECTIONS_HORIZONTAL";
            default:
                return "";
        }
    }

    /**
     * Get vertical orientation scheme
     */
    private static String getVerticalOrientation(BlockDefinition def) {
        String blockType = def.getBlockType();

        // Logs and pillars can be oriented vertically
        if (blockType.equals("log")) {
            return "MIRROR";
        }

        return "";
    }

    /**
     * Check if block has a tile entity (special behavior)
     */
    private static boolean hasTileEntity(BlockDefinition def) {
        String blockType = def.getBlockType();

        // Furnaces, beds, and other special blocks have tile entities
        return blockType.equals("furnace") ||
               blockType.equals("bed") ||
               blockType.equals("beacon") ||
               blockType.equals("particle");
    }

    /**
     * Check if block has transparency
     */
    private static boolean hasTransparency(BlockDefinition def) {
        return def.isAlphaRender() ||
               def.getRenderLayer() != null &&
               (def.getRenderLayer().equals("translucent") ||
                def.getRenderLayer().equals("cutout"));
    }

    /**
     * Quote a string value for CSV
     */
    private static String quote(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        // Escape quotes and wrap in quotes
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
