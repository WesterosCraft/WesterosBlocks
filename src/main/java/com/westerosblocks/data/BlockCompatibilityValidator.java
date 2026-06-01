package com.westerosblocks.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.westerosblocks.WesterosBlocks;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * World-save compatibility guard for block definitions. Ports the 1.18.2 source's
 * {@code WesterosBlockDef.sanityCheck()} (block names must be present and unique) and
 * {@code validateDefs()}/{@code compareBlockDefs()} (the current definitions must strictly
 * subsume a committed baseline, so a block's registry id and its state values never
 * disappear out from under existing saved worlds).
 *
 * <p>Two checks:
 * <ol>
 *   <li><b>Sanity</b> &ndash; every {@code blockName} is non-null/non-empty and unique.</li>
 *   <li><b>Subsume</b> &ndash; every block name and state id recorded in the committed
 *       baseline ({@value #BASELINE_RESOURCE}) is still present. Skipped when no baseline is
 *       bundled, mirroring the original behaviour when no {@code oldWesterosBlocks.json}
 *       exists.</li>
 * </ol>
 *
 * <p>A failure throws {@link IllegalStateException}, aborting mod initialization &mdash; the
 * same fail-fast behaviour as the original's {@code crash()}. When {@code reportOnly} is set
 * (config flag {@code exportKnownBlocks}) problems are logged but not thrown, so a fresh
 * baseline can be regenerated after an intentional removal or rename.
 */
public final class BlockCompatibilityValidator {

    /** Committed baseline manifest on the classpath: {@code {"blocks": {"name": ["stateId", ...]}}}. */
    public static final String BASELINE_RESOURCE = "/definitions/known_blocks.json";

    private BlockCompatibilityValidator() {}

    /**
     * Runs the sanity and subsume checks against the fully merged definition set.
     *
     * @param definitions    all loaded + expanded definitions
     * @param duplicateNames block names the loaders saw more than once (collapsed in the map)
     * @param reportOnly     when true, log problems instead of throwing (baseline-regeneration mode)
     */
    public static void validate(Collection<BlockDefinition> definitions, List<String> duplicateNames, boolean reportOnly) {
        List<String> errors = new ArrayList<>();

        Map<String, BlockDefinition> byName = new LinkedHashMap<>();
        for (BlockDefinition def : definitions) {
            if (def == null) {
                errors.add("Null block definition encountered");
                continue;
            }
            String name = def.getBlockName();
            if (name == null || name.isEmpty()) {
                errors.add("Block definition with missing/empty blockName (type '" + def.getBlockType() + "')");
                continue;
            }
            byName.put(name, def);
        }

        if (duplicateNames != null) {
            for (String dup : new TreeSet<>(duplicateNames)) {
                errors.add("Duplicate blockName '" + dup + "' - two definitions claim the same registry id");
            }
        }

        subsumeCheck(byName, errors);

        if (errors.isEmpty()) {
            WesterosBlocks.LOGGER.info("Block compatibility check passed ({} definitions).", byName.size());
            return;
        }

        WesterosBlocks.LOGGER.error("Block compatibility check found {} problem(s):", errors.size());
        for (String err : errors) {
            WesterosBlocks.LOGGER.error("  - {}", err);
        }
        if (reportOnly) {
            WesterosBlocks.LOGGER.warn("exportKnownBlocks is enabled - continuing despite the problems above so a new "
                + "baseline can be generated. Disable it once the definitions are clean.");
            return;
        }
        throw new IllegalStateException("WesterosBlocks block definitions failed the world-compatibility check ("
            + errors.size() + " problem(s); see log). This guard protects existing worlds from removed/renamed block "
            + "ids. Fix the definitions, or set exportKnownBlocks=true and re-commit definitions/known_blocks.json if "
            + "the change is intentional.");
    }

    private static void subsumeCheck(Map<String, BlockDefinition> byName, List<String> errors) {
        Map<String, Set<String>> baseline = loadBaseline();
        if (baseline == null) {
            WesterosBlocks.LOGGER.warn("No baseline bundled at {} - skipping world-compatibility subsume check. "
                + "Set exportKnownBlocks=true and commit the generated definitions/known_blocks.json to arm it.",
                BASELINE_RESOURCE);
            return;
        }
        for (Map.Entry<String, Set<String>> known : baseline.entrySet()) {
            String name = known.getKey();
            BlockDefinition def = byName.get(name);
            if (def == null) {
                errors.add("Known block '" + name + "' is no longer defined (removed or renamed) - breaks existing worlds");
                continue;
            }
            Set<String> current = stateIdsOf(def);
            for (String knownState : known.getValue()) {
                if (!current.contains(knownState)) {
                    errors.add("Known block '" + name + "' lost state '" + knownState + "' - breaks existing worlds");
                }
            }
        }
        WesterosBlocks.LOGGER.info("Subsume check ran against {} baseline block(s).", baseline.size());
    }

    /** State ids declared by a definition (empty when it has no multi-state property). */
    static Set<String> stateIdsOf(BlockDefinition def) {
        Set<String> ids = new LinkedHashSet<>();
        if (def.getStates() != null) {
            for (BlockDefinition.StateVariant sv : def.getStates()) {
                if (sv != null && sv.getStateID() != null) {
                    ids.add(sv.getStateID());
                }
            }
        }
        return ids;
    }

    /** Loads the committed baseline manifest from the classpath, or null if absent/unreadable. */
    private static Map<String, Set<String>> loadBaseline() {
        try (InputStream in = BlockCompatibilityValidator.class.getResourceAsStream(BASELINE_RESOURCE)) {
            if (in == null) {
                return null;
            }
            JsonElement parsed = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            if (!parsed.isJsonObject()) {
                return Collections.emptyMap();
            }
            JsonObject blocks = parsed.getAsJsonObject().getAsJsonObject("blocks");
            if (blocks == null) {
                return Collections.emptyMap();
            }
            Map<String, Set<String>> result = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                Set<String> states = new LinkedHashSet<>();
                if (entry.getValue() != null && entry.getValue().isJsonArray()) {
                    JsonArray arr = entry.getValue().getAsJsonArray();
                    for (JsonElement el : arr) {
                        states.add(el.getAsString());
                    }
                }
                result.put(entry.getKey(), states);
            }
            return result;
        } catch (Exception ex) {
            WesterosBlocks.LOGGER.warn("Failed to read baseline {} - skipping subsume check: {}",
                BASELINE_RESOURCE, ex.toString());
            return null;
        }
    }
}
