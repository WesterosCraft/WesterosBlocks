# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

WesterosBlocks is a Minecraft 1.21.1 Fabric mod that adds thousands of custom blocks with a data-driven JSON definition system. The mod uses a sophisticated block registration and data generation architecture to create blocks from JSON definitions rather than hardcoding them.

**Mod ID**: `westerosblocks`
**Minecraft Version**: 1.21.1
**Mod Loader**: Fabric
**Java Version**: 21

## Build Commands

```bash
# Build the mod (creates JAR in build/libs/)
./gradlew build

# Run Minecraft client with the mod
./gradlew runClient

# Run data generation (generates block states, models, lang files)
./gradlew runDatagen

# Clean build artifacts
./gradlew clean
```

## Core Architecture

### Block Definition System

The mod uses a two-tier JSON definition system to define blocks:

#### 1. Individual Block Definitions (`block_definitions/`)
JSON files that define single blocks with all properties. Located in `src/main/resources/block_definitions/` organized by block type subdirectories (solid, door, plant, etc.).

**Example**: `block_definitions/door/oak_door.json`
```json
{
  "blockName": "oak_door",
  "blockType": "door",
  "soundGroup": "wood",
  "creativeTab": "westeros_wood_planks_tab",
  "label": "Oak Door",
  "textures": ["wood/oak/door_top", "wood/oak/door_bottom"],
  "hardness": 2.0,
  "resistance": 3.0
}
```

**Key Properties**:
- `blockName`: Unique identifier (used for registry ID)
- `blockType`: Determines which custom block class to use (see Block Types below)
- `textures`: Texture paths relative to `assets/westerosblocks/textures/block/`
- `randomTextures`: Multiple texture variants with weights for random selection
- `states`: Different texture sets for different block states
- `isCustomModel`: Use pre-made model files instead of generation
- `isTinted`: Block uses grass-like color tinting
- `hasOverlay`: Block has overlay textures for tinting

#### 2. Block Set Definitions (`block_set_definitions/`)
JSON files that expand into multiple block variants (solid, stairs, slab, wall, fence, etc.) sharing common properties. Located in `src/main/resources/block_set_definitions/`.

**Example**: `block_set_definitions/arbor_brick.json`
```json
{
  "baseBlockName": "arbor_brick",
  "baseLabel": "Medium Ashlar Arbor",
  "variants": ["solid", "stairs", "slab", "wall", "fence"],
  "hardness": 1.5,
  "soundGroup": "stone",
  "harvestLevel": [{"tool": "pickaxe", "level": 1}],
  "textures": {
    "all": "ashlar_third/arbor/all",
    "arrow-slit": "ashlar_third/arbor/arrow_slit"
  }
}
```

This generates: `arbor_brick`, `arbor_brick_stairs`, `arbor_brick_slab`, `arbor_brick_wall`, `arbor_brick_fence`

**Texture Mapping for Variants**:
- `all`: Used for all faces (expanded to bottom/top/sides as needed)
- `sides`, `bottom`, `top`: Specific faces
- `north`, `south`, `east`, `west`: Directional faces
- Special keys: `arrow-slit`, `window-frame`, `cover` (for variant-specific textures)

### Block Types

The `blockType` field determines which custom block class is instantiated. Each type has specific behavior and properties:

| Block Type | Class | Description |
|------------|-------|-------------|
| `solid` | `WCSolidBlock` | Standard cube blocks (most common) |
| `stair` | `WCStairBlock` | Stair blocks with corner shapes |
| `slab` | `WCSlabBlock` | Half-height slabs that double-stack |
| `wall` | `WCWallBlock` | Walls that connect to adjacent blocks |
| `fence` | `WCFenceBlock` | Fences with connection logic |
| `fencegate` | `WCFenceGateBlock` | Gates with open/close states |
| `door` | `WCDoorBlock` | Double-height doors with hinges |
| `halfdoor` | `WCHalfDoorBlock` | Single-height doors |
| `pane` | `WCPaneBlock` | Glass panes that connect (bars variant supported) |
| `plant` | `WCPlantBlock` | Cross-model plants |
| `crop` | `WCCropBlock` | Growable crops with age states |
| `leaves` | `WCLeavesBlock` | Leaf blocks with decay/betterFoliage |
| `log` | `WCLogBlock` | Rotatable log blocks |
| `torch` | `WCTorchBlock` | Standing torches (+ `WCWallTorchBlock`) |
| `ladder` | `WCLadderBlock` | Climbable ladder blocks |
| `fire` | `WCFireBlock` | Decorative fire with animation |
| `bed` | `WCBedBlock` | Beds with multiple bed types |
| `chair` | `WCChairBlock` | Sittable chair blocks |
| `table` | `WCTableBlock` | Tables with connection logic |
| `branch` | `WCBranchBlock` | Tree branches with connections |
| `vines` | `WCVinesBlock` | Climbable vines |
| `layer` | `WCLayerBlock` | Snow layer-like stackable blocks |
| `sand` | `WCSandBlock` | Falling sand/gravel blocks |
| `rail` | `WCRailBlock` | Rail-like decorative blocks |
| `flowerpot` | `WCFlowerPotBlock` | Flower pot blocks |
| `beacon` | `WCBeaconBlock` | Beacon-like emissive blocks |
| `furnace` | `WCFurnaceBlock` | Furnace blocks with lit state |
| `particle` | `WCParticleEmitterBlock` | Blocks that emit particles |
| `fan` | `WCFanBlock` | Wall/floor fans (+ `WCWallFanBlock`) |
| `web` | `WCWebBlock` | Cobweb-like blocks |
| `soul-sand` | `WCSoulSandBlock` | Soul sand-like blocks |
| `arrow-slit` | `WCArrowSlitBlock` | Special decorative blocks |
| `cuboid` | `WCCuboidBlock` | Custom bounding box blocks |
| `cuboid-ne` | `WCCuboidNEBlock` | 2-directional cuboid |
| `cuboid-nsew` | `WCCuboidNSEWBlock` | 4-directional cuboid |
| `cuboid-nsew-ud` | `WCCuboidNSEWUDBlock` | 6-directional cuboid |
| `cuboid-16way` | `WCCuboid16WayBlock` | 16-direction cuboid |
| `cuboid-nsew-stack` | `WCCuboidNSEWStackBlock` | Stackable cuboid layers |

### Block Registration Flow

```
JSON Definition Files (resources/)
    ↓
BlockDefinitionLoader (loads JSON)
    ↓
BlockSetExpander (expands block sets)
    ↓
BlockDefinitionRegistry (centralizes all definitions)
    ↓
ModBlocks.registerModBlocks()
    ↓
BlockBuilder (creates block instances)
    ↓
BlockFactory (type-specific instantiation)
    ↓
Block Registration (Minecraft registry)
```

**Key Classes**:
- `BlockDefinition.java`: POJO representing a block definition with all properties
- `BlockSetDefinition.java`: POJO representing a block set with variants
- `BlockDefinitionLoader.java`: Loads individual block definitions from JSON files
- `BlockSetDefinitionLoader.java`: Loads block set definitions
- `BlockSetExpander.java`: Expands block sets into individual BlockDefinitions
- `BlockDefinitionRegistry.java`: Singleton that manages all loaded definitions
- `BlockBuilder.java`: Builds block instances from definitions with proper settings
- `BlockFactory.java`: Abstract factory for creating type-specific block instances

### Data Generation System

The mod uses Fabric's data generation to create blockstates, models, and language files automatically from block definitions.

**Run data generation**: `./gradlew runDatagen`

**Key Classes**:
- `ModBlockStateModelGenerator.java`: Main datagen entry point, dispatches to exporters
- `ModModelProvider.java`: Provider that runs the model generator
- `ModLanguageProvider.java`: Generates en_us.json language files

**Exporter Pattern**:
Each block type has a dedicated exporter in `datagen/custom/`:
- `SolidBlockExporter.java`: Handles solid blocks
- `DoorBlockExporter.java`: Handles door blocks
- `StairBlockExporter.java`: Handles stair blocks
- etc. (40+ exporters, one per block type)

Exporters implement a common pattern:
1. Extract textures from BlockDefinition (handles randomTextures, states, etc.)
2. Generate blockstate JSON (using `BlockStateVariant` and `VariantsBlockStateSupplier`)
3. Generate model JSON files (using `Models` and `TextureMap`)
4. Register item models

**Generated Files** (in `src/main/generated/`):
- `assets/westerosblocks/blockstates/*.json`: Blockstate definitions
- `assets/westerosblocks/models/block/*.json`: Block models
- `assets/westerosblocks/models/item/*.json`: Item models
- `data/westerosblocks/lang/en_us.json`: English translations

## Important Development Notes

### Adding a New Block Type

1. Create the block class in `block/custom/WC[Type]Block.java` extending appropriate base
2. Create a factory in `block/custom/[Type]Factory.java` extending `BlockFactory`
3. Register factory in `BlockBuilder.BLOCK_FACTORIES` map
4. Create exporter in `datagen/custom/[Type]BlockExporter.java` extending `BaseBlockExporter`
5. Register exporter in `ModBlockStateModelGenerator.BLOCK_EXPORTERS` map
6. Add block type to `BlockSetExpander.VARIANT_TYPES` if used in block sets
7. Define texture requirements in `BlockSetExpander.VARIANT_TEXTURES`

### Custom Models

Blocks with `"isCustomModel": true` skip model generation and require:
- Model files in `assets/westerosblocks/models/block/custom/[type]/`
- Blockstate files still generated by exporters but reference these models
- Used for complex geometry that can't be auto-generated (branches, chairs, tables)

### Tinted Blocks

Blocks can use tinting in three ways:
- `"isTinted": true`: Grass-like biome tinting (uses `BlockColorProvider`)
- `"colorMult": "#RRGGBB"`: Fixed color multiplier
- `"hasOverlay": true`: Separate overlay textures (e.g., leaves with untinted base + tinted overlay)

Tinted blocks use models in `assets/westerosblocks/models/block/tinted/` directory.

### Block States with Properties

Blocks with `"states"` array create blocks with multiple visual states (not Minecraft blockstates):
- Each state has a unique `stateID` and textures
- A `STATE` property is created with string values
- Used for blocks like particle emitters with multiple particle types

### OptiFine Integration

The mod includes OptiFine CTM (Connected Textures Mod) support:
- CTM properties in `assets/minecraft/optifine/ctm/`
- Better Foliage support via `"betterFoliage": true` or `"hasBetterFoliage": true`
- CTM texture overrides for vanilla blocks (wool, glass, sandstone, etc.)

### Creative Tabs

Blocks are organized into custom creative tabs defined in `WesterosCreativeModeTabs.java`:
- `westeros_stone_tab`: Stone blocks
- `westeros_wood_planks_tab`: Wood blocks
- `westeros_medium_ashlar_tab`: Medium ashlar blocks
- etc. (20+ tabs)

Set via `"creativeTab"` in block definitions.

## Project Structure

```
src/main/java/com/westerosblocks/
├── block/
│   ├── ModBlocks.java                 # Main block registration
│   ├── blockentity/                   # Block entities (furnaces, etc.)
│   └── custom/                        # Custom block classes (WC*Block.java)
│       ├── BlockBuilder.java          # Builds blocks from definitions
│       ├── BlockFactory.java          # Factory interface
│       └── WC*Block.java              # 40+ custom block implementations
├── data/
│   ├── BlockDefinition.java           # Block definition POJO
│   ├── BlockSetDefinition.java        # Block set POJO
│   ├── BlockDefinitionLoader.java     # JSON loader for blocks
│   ├── BlockSetDefinitionLoader.java  # JSON loader for block sets
│   ├── BlockSetExpander.java          # Expands sets to definitions
│   └── BlockDefinitionRegistry.java   # Central registry
├── datagen/
│   ├── ModBlockStateModelGenerator.java  # Main datagen coordinator
│   ├── custom/                           # Block type exporters
│   │   ├── BaseBlockExporter.java        # Exporter base class
│   │   └── *BlockExporter.java           # Type-specific exporters
│   └── providers/                        # Data providers
│       ├── ModModelProvider.java         # Model generation
│       └── ModLanguageProvider.java      # Translation generation
├── item/                              # Custom items (shields, swords)
├── entity/                            # Custom entities (chair sitting)
├── config/                            # Mod configuration
└── WesterosBlocks.java                # Main mod class

src/main/resources/
├── block_definitions/                 # Individual block JSONs
│   ├── solid/, door/, plant/, etc.    # Organized by type
│   └── *.json                         # Block definition files
├── block_set_definitions/             # Block set JSONs
│   └── *.json                         # Block set files
├── assets/westerosblocks/
│   ├── textures/block/                # Block textures
│   ├── models/block/custom/           # Custom model files
│   ├── geo/                           # GeckoLib models (shields)
│   └── optifine/ctm/                  # OptiFine CTM configs
└── data/westerosblocks/
    └── tags/                          # Block/item tags
```

## Key Dependencies

- **Fabric API**: Core Fabric API
- **AzureLib**: Animation library for shields (GeckoLib fork)
- **ShieldLib**: Shield rendering library
- **Cloth Config**: Configuration UI (via ModMenu)
- **ModMenu**: Mod menu integration
- **MidnightLib**: Additional library utilities

See `gradle.properties` for version numbers.

## Common Patterns

### Reading Block Definitions

```java
BlockDefinition def = BlockDefinitionRegistry.getInstance()
    .getDefinition("oak_door");
String blockType = def.getBlockType();
String[] textures = def.getTexturesAsArray();
```

### Creating a Block

```java
// Via BlockBuilder (preferred)
Block block = BlockBuilder.create(definition)
    .withSettings(settings)
    .build();

// Via Factory directly
BlockFactory factory = new DoorFactory();
Block block = factory.buildBlockClass(settings, definition);
```

### Extracting Textures in Exporters

```java
// Use priority-based extraction
Object textureData = definition.extractTextures();
if (textureData instanceof String[] simpleTextures) {
    // Handle simple texture array
} else if (textureData instanceof String[][] variants) {
    // Handle random textures or states
}

// Or use specific methods
if (definition.hasRandomTextures()) {
    List<TextureVariantSet> variants =
        definition.getRandomTextureVariantSets();
}
```

### Generating Models in Exporters

```java
// Create texture map
TextureMap textures = new TextureMap()
    .put(TextureKey.TOP, topTexture)
    .put(TextureKey.BOTTOM, bottomTexture);

// Upload model
Identifier modelId = Models.CUBE_BOTTOM_TOP.upload(
    block, textures, modelCollector
);

// Create blockstate
BlockStateSupplier supplier = BlockStateModelGenerator
    .createSingletonBlockState(block, modelId);
blockStateCollector.accept(supplier);
```

## Testing

The mod is tested by:
1. Running `./gradlew runClient` and visually inspecting blocks
2. Running `./gradlew runDatagen` and verifying generated files
3. Checking logs for warnings/errors during block registration

No automated unit tests are currently present.
