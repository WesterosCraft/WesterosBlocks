package com.westerosblocks.data;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Exports every registered WesterosBlocks block to a WorldPainter-compatible
 * "Custom Block Definition File" (CSV) so the world can be edited in WorldPainter.
 *
 * <p>Format reference: <a href="https://www.worldpainter.net/trac/wiki/CustomBlocks">
 * worldpainter.net/trac/wiki/CustomBlocks</a>. The column layout and value syntax
 * below were verified against WorldPainter's own bundled example CSV.
 *
 * <p>Key format rules (these tripped up the previous version):
 * <ul>
 *   <li>Columns are matched by <b>header name</b>, not position — unknown names are dropped.</li>
 *   <li>{@code properties} is a single quoted field listing the block's full state schema:
 *       {@code "name:type,name:type"} where type is {@code b} (boolean),
 *       {@code i[min-max]} (int range) or {@code e[v1;v2;...]} (enum, semicolon-separated).</li>
 *   <li>{@code colour} is 8 hex digits {@code ffRRGGBB} (opaque ARGB), lowercase, no {@code #}.</li>
 *   <li>{@code receivesLight} is {@code false} only for full opaque cubes; everything else is {@code true}.</li>
 * </ul>
 *
 * <p>We emit one row per block name with the full property schema (empty
 * {@code discriminator}); WorldPainter treats every blockstate of that block as
 * the same material, which is what we want for terrain editing. A per-state row
 * with a discriminator is only needed when terrain attributes differ between
 * states, which is not the case for our blocks.
 */
public class WorldPainterExporter {

    private static final String CSV_FILENAME = "westerosblocks-worldpainter.csv";

    /**
     * Canonical WorldPainter column order. WP matches by name, but a stable order keeps diffs clean.
     *
     * <p>Note the orientation columns: horizontal is <b>plural</b> ({@code horizontal_orientation_schemes})
     * and vertical is <b>singular</b> ({@code vertical_orientation_scheme}). This matches WorldPainter's
     * actual CSV parser ({@code Material.determineHorizontalOrientations} reads
     * {@code spec.get("horizontal_orientation_schemes")}); the wiki column table mislabels the horizontal
     * one as singular. A wrong column name is silently ignored, so this matters.
     */
    private static final String HEADER = String.join(",",
            "name", "discriminator", "properties", "opacity", "receivesLight",
            "insubstantial", "resource", "tileEntity", "tileEntityId", "treeRelated",
            "vegetation", "blockLight", "natural", "watery", "colour",
            "horizontal_orientation_schemes", "vertical_orientation_scheme");

    /** Export all registered blocks to a WorldPainter CSV in the config directory. */
    public static void exportToCSV() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File csvFile = new File(configDir, CSV_FILENAME);

        WesterosBlocks.LOGGER.info("Exporting blocks to WorldPainter CSV: {}", csvFile.getAbsolutePath());

        // Sorted by name so the output is deterministic and easy to diff/review.
        Map<String, Block> blocks = new TreeMap<>(ModBlocks.getAllAutoRegisteredBlocks());
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        int written = 0;
        // WorldPainter requires UTF-8 without a BOM; Files.newBufferedWriter(UTF_8) writes no BOM,
        // unlike FileWriter which would use the (Windows-1252 on the build box) platform charset.
        try (Writer writer = Files.newBufferedWriter(csvFile.toPath(), StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.write("\n");

            for (Map.Entry<String, Block> entry : blocks.entrySet()) {
                String name = entry.getKey();
                Block block = entry.getValue();
                if (block == null) continue;
                BlockDefinition def = registry.getDefinition(name); // may be null for synthesized variants
                writeBlockRow(writer, name, block, def);
                written++;
            }

            WesterosBlocks.LOGGER.info("Successfully exported {} blocks to {}", written, csvFile.getName());
        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to export WorldPainter CSV", e);
        }
    }

    private static void writeBlockRow(Writer writer, String name, Block block, BlockDefinition def)
            throws IOException {
        BlockState state = block.getDefaultState();

        boolean fullOpaqueCube = isFullOpaqueCube(state);
        boolean lightBlocking = isLightBlocking(def, fullOpaqueCube);

        List<String> v = new ArrayList<>(15);
        v.add(quote("westerosblocks:" + name));         // name
        v.add("");                                       // discriminator (single row covers all states)
        v.add(quote(buildProperties(block)));            // properties (full state schema)
        v.add(String.valueOf(lightBlocking ? 15 : 0));   // opacity
        v.add(String.valueOf(!fullOpaqueCube));          // receivesLight
        v.add(String.valueOf(isInsubstantial(def)));     // insubstantial
        v.add("false");                                  // resource (ore-like) — none of ours
        v.add(String.valueOf(block instanceof BlockEntityProvider)); // tileEntity
        v.add("");                                        // tileEntityId (optional)
        v.add(String.valueOf(isTreeRelated(def)));       // treeRelated
        v.add(String.valueOf(isVegetation(def)));        // vegetation
        v.add(String.valueOf(safeLuminance(state)));     // blockLight
        v.add("false");                                  // natural (world-generated) — n/a for editing
        v.add("false");                                  // watery
        v.add(quote(colour(state)));                     // colour ffRRGGBB
        v.add(horizontalOrientationOverride(block));     // horizontal_orientation_schemes
        v.add(verticalOrientationOverride(block));       // vertical_orientation_scheme

        writer.write(String.join(",", v));
        writer.write("\n");
    }

    /**
     * Build the WorldPainter {@code properties} string from the block's real state schema.
     * Returns "" for stateless blocks.
     */
    private static String buildProperties(Block block) {
        List<String> parts = new ArrayList<>();
        for (Property<?> prop : block.getStateManager().getProperties()) {
            parts.add(prop.getName() + ":" + propertyType(prop));
        }
        return String.join(",", parts);
    }

    private static String propertyType(Property<?> prop) {
        if (prop instanceof BooleanProperty) {
            return "b";
        }
        if (prop instanceof IntProperty intProp) {
            int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
            for (Integer val : intProp.getValues()) {
                min = Math.min(min, val);
                max = Math.max(max, val);
            }
            return "i[" + min + "-" + max + "]";
        }
        // Enum / direction / anything else: list the named values, semicolon-separated.
        List<String> names = new ArrayList<>();
        for (Object val : prop.getValues()) {
            names.add(namedValue(prop, val));
        }
        return "e[" + String.join(";", names) + "]";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static String namedValue(Property prop, Object val) {
        return prop.name((Comparable) val);
    }

    /**
     * Orientation override for the {@code horizontal_orientation_schemes} column.
     *
     * <p><b>We deliberately return empty (= let WorldPainter auto-detect) for everything that
     * auto-detects, which is the optimal choice — do not "helpfully" fill this in.</b> Rationale:
     * <ul>
     *   <li>WorldPainter auto-detects orientation from the <em>property names and values</em> present
     *       (its {@code determineHorizontalOrientations}: {@code facing}→FACING, {@code axis}→AXIS,
     *       {@code shape}→SHAPE/STAIR_CORNER, {@code type}→TYPE, {@code hinge}→HINGE,
     *       {@code north/east/south/west}→CARDINAL_DIRECTIONS, {@code rotation} 0-15→ROTATION).
     *       Our blocks use these exact vanilla names, and the {@code properties} column above emits
     *       the exact MC values — so stairs/slabs/walls/fences/panes/doors/gates/logs/ladders/layers
     *       all rotate and mirror correctly via auto-detection.</li>
     *   <li>Auto-detection produces a <em>multi-scheme</em> array (e.g. stairs = FACING + STAIR_CORNER).
     *       The CSV override path cannot: WorldPainter's parser has a bug where a comma-separated
     *       override is fed whole into {@code valueOf()} per token, so any multi-scheme override
     *       throws and is dropped entirely. Emitting an override would therefore <em>break</em>
     *       rotation for exactly the blocks that need more than one scheme.</li>
     * </ul>
     *
     * <p>Blocks whose orientation rides on a non-standard property WP can't detect — the 8-way
     * {@code rotation} (0-7) chairs, the {@code connection} enum on benches/tables, and
     * {@code connectstate} — have no clean single WP scheme to map to, so they are left blank too;
     * WP will place them but won't re-orient those properties on a 90°/180° map rotation. This is a
     * known, documented limitation, not something the CSV can fix.
     *
     * <p>The switch below is the sanctioned place to add a <em>single</em>-scheme override if a future
     * block ever needs one (use {@code "none"} to forbid rotation/mirroring).
     */
    private static String horizontalOrientationOverride(Block block) {
        return ""; // empty = WorldPainter auto-detects from the exported properties (preferred)
    }

    /** Vertical orientation override. Empty = auto-detect ({@code half}/{@code type}→top/bottom, {@code up}, etc.). See {@link #horizontalOrientationOverride}. */
    private static String verticalOrientationOverride(Block block) {
        return "";
    }

    /** True for blocks that fully fill the cube and block all light (sand, stone). */
    private static boolean isFullOpaqueCube(BlockState state) {
        try {
            return state.isOpaque();
        } catch (Throwable t) {
            return false;
        }
    }

    /** Whether the block attenuates skylight (opacity 15 vs 0). */
    private static boolean isLightBlocking(BlockDefinition def, boolean fullOpaqueCube) {
        if (fullOpaqueCube) return true;
        if (def == null) return true; // assume solid when we have no metadata
        // Transparent / pass-through blocks let light through.
        if (def.isNonOpaque() || def.hasNoCollision()) return false;
        String layer = def.getRenderLayer();
        if (layer != null && (layer.equals("translucent") || layer.equals("cutout")
                || layer.equals("cutout_mipped"))) {
            return false;
        }
        return true;
    }

    /** Plants, torches, fans, and anything with no collision are "easily replaced". */
    private static boolean isInsubstantial(BlockDefinition def) {
        if (def == null) return false;
        if (def.hasNoCollision()) return true;
        return switch (def.getBlockType()) {
            case "plant", "crop", "torch", "fire", "vines", "rail", "fan", "web", "flower" -> true;
            default -> false;
        };
    }

    // NOTE: treeRelated/vegetation drive WorldPainter's *destructive* "remove trees" / "remove
    // vegetation" merge operations. We deliberately exclude "log": in WesterosBlocks logs are
    // decorative structural timber (pillars, beams), not trees, and flagging them would let a
    // "remove trees" merge delete framed builds. Only genuine foliage is classified.

    private static boolean isVegetation(BlockDefinition def) {
        if (def == null) return false;
        return switch (def.getBlockType()) {
            case "plant", "crop", "vines", "flower", "leaves", "sapling" -> true;
            default -> false;
        };
    }

    private static boolean isTreeRelated(BlockDefinition def) {
        if (def == null) return false;
        return switch (def.getBlockType()) {
            case "leaves", "sapling" -> true;
            default -> false;
        };
    }

    private static int safeLuminance(BlockState state) {
        try {
            return state.getLuminance();
        } catch (Throwable t) {
            return 0;
        }
    }

    /**
     * Representative editor colour as {@code ffRRGGBB}. Pulls the block's resolved
     * MapColor (which datagen/{@code mapColor} already derives from the texture);
     * falls back to a neutral gray.
     */
    private static String colour(BlockState state) {
        int rgb = 0x7f7f7f;
        try {
            MapColor mc = state.getMapColor(null, null);
            if (mc != null && mc != MapColor.CLEAR) {
                rgb = mc.color & 0xFFFFFF;
            }
        } catch (Throwable t) {
            // Some tinted blocks dereference world/pos; neutral gray is fine for those.
        }
        return String.format("ff%06x", rgb);
    }

    /** Quote and escape a CSV field; returns "" unquoted for empty values. */
    private static String quote(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
