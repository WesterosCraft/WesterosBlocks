# Block Set Definition System

## Overview

The Block Set Definition System allows you to create entire families of related blocks (solid, stairs, slabs, walls, fences, etc.) from a single JSON definition file, using a shared texture pool. This follows the Minecraft BlockFamily pattern from the Fabric datagen documentation.

## Architecture

### Core Components

1. **BlockSetDefinition** - Parses JSON definitions with all block set properties
2. **BlockSetLoader** - Loads JSON files from `src/main/resources/block_set_definitions/`
3. **BlockSetRegistry** - Auto-registers all blocks in a set using BlockBuilder pattern
4. **BlockSetContext** - Shared singleton context for definitions and registered blocks
5. **ModBlockStateModelGenerator** - Generates models/blockstates using texture pool pattern
6. **ModLanguageProvider** - Auto-generates translations for all variants

## JSON Definition Format

Place your block set definitions in `src/main/resources/block_set_definitions/`

### Basic Example

```json
{
    "baseBlockName": "oak",
    "variants": [
        "solid",
        "stairs",
        "slab",
        "wall",
        "fence",
        "fence_gate"
    ],
    "hardness": 5,
    "resistance": 4,
    "stepSound": "wood",
    "material": "wood",
    "creativeTab": "westeros_wood_planks_tab",
    "baseLabel": "Oak Wood",
    "harvestLevel": [
        {
            "tool": "axe",
            "level": 3
        }
    ],
    "textures": {
        "all": "wood/oak/all"
    }
}
```

This will create:
- `oak_solid` - Oak Wood
- `oak_stairs` - Oak Wood Stairs
- `oak_slab` - Oak Wood Slab
- `oak_wall` - Oak Wood Wall
- `oak_fence` - Oak Wood Fence
- `oak_fence_gate` - Oak Wood Fence Gate

### Advanced Features

#### Alt Names
Use custom names for specific variants:

```json
{
    "baseBlockName": "oak",
    "altNames": {
        "solid": "oak_planks",
        "fence_gate": "locked_oak_fence_gate"
    }
}
```

#### Variant-Specific Textures
Override textures for specific variants:

```json
{
    "textures": {
        "all": "wood/oak/all",
        "cover": "wood/oak/cover"
    },
    "altTextures": {
        "solid": [
            "wood/oak/all",
            "wood/oak/all",
            "wood/oak/woodctm"
        ],
        "stairs,slab,wall": [
            "wood/oak/all",
            "wood/oak/all",
            "wood/oak/plankctm"
        ]
    }
}
```

#### Random Textures
Add weighted random texture variants:

```json
{
    "randomTextures": [
        {
            "textures": {
                "all": "cobblestone/brown_grey/all1"
            },
            "weight": 5
        },
        {
            "textures": {
                "all": "cobblestone/brown_grey/all2"
            },
            "weight": 10
        }
    ]
}
```

#### Block States
Create multiple states with overlay textures:

```json
{
    "states": [
        {
            "stateID": "base"
        },
        {
            "stateID": "red",
            "excludeVariants": "hopper",
            "overlayTextures": {
                "all": "test_block/overlay_red"
            }
        }
    ]
}
```

#### Type Parameters
Apply variant-specific parameters:

```json
{
    "types": {
        "fence_gate": "locked:true",
        "fence": "unconnect:true",
        "stairs,wall": "unconnect:false"
    }
}
```

Supported parameters:
- `locked:true/false` - For doors/fence gates
- `unconnect:true/false` - For fences/walls/stairs
- And more (see BlockBuilder documentation)

## Supported Block Variants

- `solid` - Basic cube block
- `stairs` - Stair blocks with all rotations
- `slab` - Slab blocks (bottom/top/double)
- `wall` - Wall blocks with connections
- `fence` - Fence blocks with connections
- `fence_gate` - Fence gate blocks
- `hopper` - Hopper-shaped blocks
- `hollow_hopper` - Hollow hopper variant
- `tip` - Tip-shaped blocks
- `carpet` - Carpet/layer blocks
- `cover` - Cover blocks
- `window_frame` - Window frame panes
- `window_frame_mullion` - Window frame with mullion
- `arrow_slit` - Arrow slit blocks
- `arrow_slit_window` - Arrow slit with window
- `arrow_slit_ornate` - Ornate arrow slit

## How It Works

### 1. Block Registration Flow

1. **Load Definitions**: BlockSetLoader loads all JSON files from `block_set_definitions/`
2. **Store in Context**: Definitions are stored in BlockSetContext singleton
3. **Register Blocks**: BlockSetRegistry creates and registers all blocks using BlockBuilder
4. **Store Blocks**: Registered blocks are stored in BlockSetContext

### 2. Datagen Flow

1. **Model Generation**: ModModelProvider retrieves definitions and blocks from BlockSetContext
2. **Texture Pool**: Each block set uses `registerBlockSet()` with shared texture pool
3. **Translations**: ModLanguageProvider generates translations for all variants
4. **Output**: Models, blockstates, and language files are generated

## Integration Guide

### Step 1: Create JSON Definition

Create `src/main/resources/block_set_definitions/my_block_set.json`:

```json
{
    "baseBlockName": "my_block_set",
    "variants": ["solid", "stairs", "slab"],
    "hardness": 3,
    "stepSound": "stone",
    "material": "rock",
    "creativeTab": "westeros_blocks_tab",
    "baseLabel": "My Block Set",
    "textures": {
        "all": "my_texture"
    }
}
```

### Step 2: Load and Register (in mod initialization)

```java
// Load block set definitions
BlockSetLoader loader = new BlockSetLoader("block_set_definitions");
Map<String, BlockSetDefinition> definitions = loader.loadAllBlockSets();

// Store in context for datagen
BlockSetContext.getInstance().setDefinitions(definitions);

// Register all block sets
BlockSetRegistry registry = new BlockSetRegistry();
for (BlockSetDefinition definition : definitions.values()) {
    Map<String, Block> blocks = registry.registerBlockSet(definition);
    BlockSetContext.getInstance().addBlockSet(definition.getBaseBlockName(), blocks);
}
```

### Step 3: Run Datagen

```bash
./gradlew runDatagen
```

This will generate:
- Block models in `assets/westerosblocks/models/block/`
- Blockstate JSONs in `assets/westerosblocks/blockstates/`
- Item models in `assets/westerosblocks/models/item/`
- Translations in `assets/westerosblocks/lang/en_us.json`

## Examples

See existing block set definitions:
- `test_block_set.json` - Basic example
- `oak.json` - Complex example with alt textures
- `brown_grey_cobblestone.json` - Random textures example
- `test_multistate_overlay_block_set.json` - Block states example

## Benefits

1. **Single Source of Truth**: Define entire block families in one JSON file
2. **Texture Pool Pattern**: Share textures across variants efficiently
3. **Automatic Generation**: Models, blockstates, and translations auto-generated
4. **Type Safety**: Uses BlockBuilder pattern with compile-time checks
5. **Scalability**: Add hundreds of block sets without code changes
6. **Consistency**: All variants follow the same naming and structure

## Technical Notes

- Textures are referenced relative to `assets/westerosblocks/textures/block/`
- Block names follow pattern: `{baseBlockName}_{variant}` (unless altName specified)
- Translation keys: `block.westerosblocks.{blockName}`
- Creative tabs must exist in your mod
- Sound groups: wood, stone, metal, grass, gravel, sand, snow, wool, glass

## Limitations

Currently, stairs/walls/fences/fence_gates have placeholder model generation. Full implementation requires:
- Custom stair model variants (base, inner, outer)
- Wall connection models (post, side, tall)
- Fence connection models
- Fence gate open/closed states

For now, these will log warnings but allow compilation. Implement the full exporters as needed.
