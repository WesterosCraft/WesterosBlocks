# WesterosBlocks - Block Types and Properties Documentation

## Block Definition System Overview

WesterosBlocks uses a JSON-based block definition system where each block is defined in a JSON file located in `src/main/resources/block_definitions/`. These definitions are structured according to the `BlockDefinition.java` class and contain all the properties needed to automatically register blocks with their appropriate behaviors, textures, and characteristics.

The system supports over 30 different block types, each with their own specific properties and behaviors. Block definitions are organized into directories by type (e.g., `solid/`, `door/`, `slab/`, etc.) and loaded at runtime to create the corresponding Minecraft blocks.

## Complete Block Type Catalog

- `beacon` - Special beacon blocks for signaling and lighting
- `bed` - Bed blocks with different bed types and materials
- `branch` - Tree branch decorations that can connect to other blocks
- `chair` - Interactive seating furniture with entity support
- `crop` - Agricultural crops with multiple growth stages
- `cuboid` - Basic cuboid blocks with simple geometry
- `cuboid-16way` - 16-directional cuboid blocks for complex orientations
- `cuboid-ne` - Northeast-oriented cuboids for diagonal placement
- `cuboid-nsew` - 4-directional cuboid blocks (north, south, east, west)
- `cuboid-nsew-stack` - Stackable 4-directional blocks (banners, signs, etc.)
- `cuboid-nsewud` - 6-directional cuboid blocks (all cardinal directions plus up/down)
- `door` - Standard door blocks with wood types and lock states
- `fan` - Decorative fan blocks for ceiling/wall mounting
- `fence` - Fence blocks with connecting behavior
- `fencegate` - Fence gate blocks with opening/closing functionality
- `fire` - Fire effect blocks with particle emissions
- `flowerbed` - Decorative flower bed blocks
- `flowerpot` - Potted plant blocks with various plant types
- `furnace` - Functional furnace blocks with lighting states
- `halfdoor` - Half-door/shutter blocks for windows
- `ladder` - Climbable ladder blocks
- `leaves` - Tree leaf blocks with decay, tinting, and Better Foliage support
- `log` - Rotatable log blocks with bark textures
- `pane` - Glass pane and bar blocks with connecting behavior
- `particle` - Particle effect blocks for ambient effects
- `plant` - Various plant blocks including flowers and grasses
- `rail` - Rail/track blocks for transportation
- `slab` - Half-height slab blocks
- `solid` - Standard solid cube blocks (most common type)
- `table` - Table furniture blocks with connecting behavior
- `test` - Testing blocks for development purposes
- `torch` - Light-emitting torch blocks with particle effects
- `vines` - Climbable vine blocks that spread on surfaces
- `wall` - Connecting wall blocks with various heights
- `web` - Web blocks with collision properties

## Block Properties

### Properties Available for All Block Definitions

**Core Identification:**
- `blockName` - Unique identifier for the block (e.g., `"oak_table"`, `"stone_wall"`)
- `blockType` - Block class type determining which implementation to use
- `label` - Display name shown in-game (e.g., `"Oak Table"`, `"Stone Wall"`)
- `creativeTab` - Creative mode tab for block placement

**Physical Properties:**
- `hardness` / `strength` - Block hardness (affects break time)
- `resistance` - Blast resistance value
- `soundGroup` - Sound effects when walking/placing/breaking:
  - `"wood"` - Wood sounds
  - `"stone"` - Stone sounds
  - `"metal"` - Metal sounds
  - `"grass"` - Grass/plant sounds
  - `"cloth"` - Fabric sounds
  - `"powder"` - Powder/sand sounds

**Visual Properties:**
- `textures` - Array of texture file paths (relative to `textures/block/`). Order varies by block type.
- `luminance` - Light level emitted by block (0-15, where 15 is brightest)
- `renderLayer` - Render layer: `"cutout"`, `"cutout_mipped"`, or `"translucent"`

**Tool Requirements:**
- `harvestLevel` - Array of tool requirements for efficient breaking
  ```json
  "harvestLevel": [
    {"tool": "pickaxe", "level": 1},
    {"tool": "shovel", "level": 2}
  ]
  ```

**Behavior Flags:**
- `nonOpaque` - Block is transparent/non-opaque (lets light through)
- `noCollision` - Block has no collision box (can walk through)
- `allowUnsupported` - Block can be placed in unsupported locations
- `toggleOnUse` - Creative players can cycle states by right-clicking
- `type` - Legacy string field with comma-separated flags (mostly replaced by specific boolean properties)

### Properties Available for Specific Block Types

**Datagen Properties** *(used for model and texture generation)*:
- `randomTextures` - Array of texture variants with weights for random selection *(solid, leaves, pane)*
  ```json
  "randomTextures": [
    {"textures": ["variant1"], "weight": 10},
    {"textures": ["variant2"], "weight": 5}
  ]
  ```
- `states` - Different texture sets for different block states *(crop, solid)*
  ```json
  "states": [
    {"stateID": "age0", "textures": ["crop_stage_0"]},
    {"stateID": "age1", "textures": ["crop_stage_1"]}
  ]
  ```
- `overlayTextures` - Additional overlay textures for tinted blocks *(leaves)*
- `colorMult` - Enables texture tinting *(leaves)* (e.g., `"#FF0000"` for red, or `"textures/colormap/birch"` for biome tinting)
- `isCustomModel` - Boolean indicating use of custom model files *(furnace)*
- `isTinted` - Boolean for grass-like color variation rendering *(leaves)*
- `hasOverlay` - Boolean indicating block has overlay textures for tinting *(leaves)*
- `hasBetterFoliage` - Boolean for OptiFine Better Foliage support *(leaves)*
- `hasRotateRandom` - Boolean to randomly rotate block models *(leaves)*
- `stack` - Array of stack elements for complex geometry *(cuboid-nsew-stack)*
  ```json
  "stack": [
    {
      "textures": ["texture1", "texture2", "texture3", "texture4", "texture5", "texture6"],
      "boundingBox": {
        "xMin": 0.0, "xMax": 1.0,
        "yMin": 0.0, "yMax": 1.0,
        "zMin": 0.0, "zMax": 1.0
      }
    }
  ]
  ```

**Block-Specific Behavior Properties**:
- `layerSensitive` - Breaks when supporting layer changes *(plant, crop)*
- `locked` - Doors/gates are locked and cannot be opened *(door, halfdoor, fencegate)*
- `noParticle` - Torches don't emit particles *(torch)*
- `alwaysOn` - Furnaces are always lit (no off state) *(furnace)*
- `bedType` - Bed type for bed blocks (affects model and behavior) *(bed)*
- `wallSize` - Wall height: `"normal"` (16 blocks) or `"short"` (13 blocks) *(wall)*
- `connectState` - Boolean indicating walls have connect state cycling feature *(wall)*
- `unconnect` - Boolean indicating walls/panes don't connect to adjacent blocks when true *(wall, pane)*


This documentation serves as a comprehensive reference for understanding and implementing the WesterosBlocks block definition system.