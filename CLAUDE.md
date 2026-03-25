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
2. Registry loads individual definitions from `resources/definitions/block_definitions/` and block set definitions from `resources/definitions/block_set_definitions/`
3. `BlockSetExpander` expands set templates into individual `BlockDefinition` instances (one JSON → multiple variant blocks like solid, stairs, slab, wall)
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
- `src/main/resources/definitions/` — JSON block definitions (individual and set-based)
- `src/main/java/com/westerosblocks/utils/ModProperties.java` — Custom block state properties

### Block Definition JSON Format

Individual definition (`block_definitions/`):
```json
{
  "blockName": "chair_oak",
  "blockType": "chair",
  "soundGroup": "wood",
  "textures": ["bark/oak/side"],
  "label": "Oak Chair"
}
```

Set definition (`block_set_definitions/`): defines `baseBlockName`, `variants` array (e.g., `["solid","stairs","slab","wall"]`), shared textures/properties, and optional `states` for multi-state blocks. `BlockSetExpander` generates one `BlockDefinition` per variant.

### Common Patterns

- **onUse toggle**: Check `toggleOnUse && STATE != null && player.isCreative() && player.getMainHandStack().isEmpty()` with `state.contains(STATE)` guard. Use `Block.NOTIFY_ALL` for world notifications.
- **Wall variants**: Torch/fan wall variants are named `"wall_" + blockName`
- **Registry lookups**: `Registries.BLOCK.get()` returns `air` (not null) for missing IDs — always use `containsId()` first
- **Lambda captures**: In `BlockDefinition.makeSettings()`, capture local variables (not `this.method()`) to avoid stale references

### Dependencies

Fabric API, AzureLib (shield rendering), Shield API, ModMenu, Cloth Config. Versions managed in `gradle.properties`.
