# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew build          # Build mod JAR (output in build/libs/)
./gradlew runClient      # Run Minecraft client with the mod
./gradlew runDatagen     # Generate block states, models, and lang files
./gradlew clean          # Clean build artifacts
```

No test suite exists. Verification is done by building (`./gradlew build`) and running datagen.

## Architecture

**Fabric 1.21.1 mod** (Java 21, Yarn mappings) that adds 3000+ blocks via a data-driven JSON definition system. Mod ID: `westerosblocks`.

### Data Loading Pipeline

1. `WesterosBlocks.onInitialize()` → `BlockDefinitionRegistry.initialize()`
2. `ConsolidatedDefinitionLoader` loads the single `resources/definitions/WesterosBlocks.json` file: the `blocks` array holds individual `BlockDefinition`s, the `blockSets` array holds `BlockSetDefinition`s. **Array order is authoritative** — it drives registration and creative-tab ordering (blocks first, then expanded sets)
3. `BlockSetExpander` expands set templates into individual `BlockDefinition` instances (one entry → multiple variant blocks like solid, stairs, slab, wall)
4. All definitions stored in singleton `BlockDefinitionRegistry`

### Block Registration Pipeline

1. `ModBlocks` static initializer iterates all definitions
2. Each `BlockDefinition.blockType` string maps to a factory class (e.g., `"solid"` → `WCSolidBlock.Factory`)
3. Factory's `buildBlockClass()`: calls `definition.makeSettings()`, builds state properties, sets `protected static temp*` fields, constructs block instance
4. Block + auto-generated `BlockItem` registered to Minecraft registry

### The temp* Field Pattern (Critical)

Block classes use `protected static temp*` fields (e.g., `tempSTATE`, `tempCONNECTSTATE`) to pass data from Factory into the constructor. This exists because `appendProperties()` is called from the super constructor *before* the subclass constructor body runs. The factory sets the static temps, calls `new WCFooBlock(settings)`, and `appendProperties()` reads the temps into instance fields. **Never make state-holding fields static** — they must be instance fields assigned in `appendProperties()`.

### Key Source Directories

- `src/main/java/com/westerosblocks/block/custom/` — 48 block classes, each with a nested `Factory` inner class
- `src/main/java/com/westerosblocks/data/` — `BlockDefinitionRegistry`, `BlockDefinition`, `BlockSetExpander`, JSON loaders
- `src/main/java/com/westerosblocks/datagen/` — Model/blockstate/lang generation; each block type has an exporter in `custom/`
- `src/main/resources/definitions/WesterosBlocks.json` — all block + block set definitions in one file (`color_maps.json` and `block_tags.json` sit alongside it)
- `src/main/java/com/westerosblocks/utils/ModProperties.java` — Custom block state properties

### Block Definition JSON Format

`definitions/WesterosBlocks.json` has two sections (validated by `schemas/westerosblocks.schema.json`):

```json
{
  "blocks": [
    {
      "blockName": "chair_oak",
      "blockType": "chair",
      "soundGroup": "wood",
      "textures": ["bark/oak/side"],
      "label": "Oak Chair"
    }
  ],
  "blockSets": [ ... ]
}
```

Each `blockSets` entry defines `baseBlockName`, `variants` array (e.g., `["solid","stairs","slab","wall"]`), shared textures/properties, and optional `states` for multi-state blocks. `BlockSetExpander` generates one `BlockDefinition` per variant. Reordering entries only changes creative-tab display order — registry IDs are name-keyed, so world saves are unaffected.

### Common Patterns

- **onUse toggle**: Check `toggleOnUse && STATE != null && player.isCreative() && player.getMainHandStack().isEmpty()` with `state.contains(STATE)` guard. Use `Block.NOTIFY_ALL` for world notifications.
- **Wall variants**: Torch/fan wall variants are named `"wall_" + blockName`
- **Registry lookups**: `Registries.BLOCK.get()` returns `air` (not null) for missing IDs — always use `containsId()` first
- **Lambda captures**: In `BlockDefinition.makeSettings()`, capture local variables (not `this.method()`) to avoid stale references
- **Layer blocks**: `softLayer` is an `options` flag (`OptionsProperties` / `BlockDefinition.isSoftLayer()`); plants/snow sink into soft layers. `layerCount` is fixed at 8 (as in the 1.18.2 source).

### Registration ordering

Block/item/block-entity registration is invoked **explicitly and in order** from `WesterosBlocks.onInitialize()` (`initializeBlockDefinitions()` → `ModBlocks.registerModBlocks()` → … → `ModBlockEntities.registerModBlockEntities()`). Do **not** reintroduce `static {}` initializer blocks to trigger registration — registration must run *after* `BlockDefinitionRegistry` is initialized, and the explicit call order guarantees that.

### World-Save Compatibility Guard

`BlockCompatibilityValidator` (run from `WesterosBlocks.initializeBlockDefinitions()`) protects existing worlds from broken block IDs, porting the 1.18.2 `sanityCheck()`/`compareBlockDefs()`:
- **Sanity**: block names must be non-empty and unique (duplicates → hard fail at startup).
- **Subsume**: every block name + state ID in the committed baseline `src/main/resources/definitions/known_blocks.json` must still exist; removing or renaming one is a hard fail. Skipped if no baseline is bundled.

To (re)establish the baseline after an intentional add/remove/rename: set `exportKnownBlocks: true` in the config, run the game or datagen once (this also downgrades the guard to report-only), then copy the generated `<config>/known_blocks.json` into `src/main/resources/definitions/` and commit it. This mirrors maintaining the original's `oldWesterosBlocks.json`.

### Dependencies

Fabric API, AzureLib (shield rendering), Shield API. Versions managed in `gradle.properties`.
