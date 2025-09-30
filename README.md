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

### Datagen Properties
*Properties used for model and texture generation during datagen phase*

- `textures` - Array of texture file paths (relative to `textures/block/`). Order varies by block type.
- `randomTextures` - Array of texture variants with weights for random selection
  ```json
  "randomTextures": [
    {"textures": ["variant1"], "weight": 10},
    {"textures": ["variant2"], "weight": 5}
  ]
  ```
- `states` - Different texture sets for different block states
  ```json
  "states": [
    {"stateID": "age0", "textures": ["crop_stage_0"]},
    {"stateID": "age1", "textures": ["crop_stage_1"]}
  ]
  ```
- `overlayTextures` - Additional overlay textures for tinted blocks
- `colorMult` - Hex color for texture tinting (e.g., `"#FF0000"` for red, or `"textures/colormap/birch"` for biome tinting)
- `renderLayer` - Render layer: `"cutout"`, `"cutout_mipped"`, or `"translucent"`
- `isCustomModel` - Boolean indicating use of custom model files instead of generated ones
- `isTinted` - Boolean for grass-like color variation rendering
- `hasOverlay` - Boolean indicating block has overlay textures for tinting
- `hasBetterFoliage` - Boolean for OptiFine Better Foliage support
- `hasRotateRandom` - Boolean to randomly rotate block models
- `stack` - Array of stack elements for complex geometry (used by `cuboid-nsew-stack`)
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

### All Other Properties
*Properties used for block behavior, registration, and gameplay mechanics*

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

**Lighting and Visual:**
- `luminance` - Light level emitted by block (0-15, where 15 is brightest)

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
- `layerSensitive` - Plants only: breaks when supporting layer changes
- `allowUnsupported` - Block can be placed in unsupported locations
- `toggleOnUse` - Creative players can cycle states by right-clicking
- `locked` - Doors/gates are locked and cannot be opened
- `noParticle` - Torches don't emit particles
- `alwaysOn` - Furnaces are always lit (no off state)

**Block-Specific Properties:**
- `bedType` - Bed type for bed blocks (affects model and behavior)
- `wallSize` - Wall height: `"normal"` (16 blocks) or `"short"` (13 blocks)
- `connectState` - Boolean indicating walls have connect state cycling feature
- `unconnect` - Boolean indicating walls/panes don't connect to adjacent blocks when true
- `type` - Legacy string field with comma-separated flags (mostly replaced by specific boolean properties)

## Example Block Definitions

### Simple Solid Block
```json
{
  "blockName": "6sided_birch",
  "blockType": "solid",
  "strength": 2,
  "requiresTool": true,
  "soundGroup": "wood",
  "creativeTab": "westeros_logs_tab",
  "label": "6-Sided Birch",
  "textures": ["bark/birch/side"]
}
```

### Door Block with Wood Type
```json
{
  "blockName": "birch_door",
  "blockType": "door",
  "strength": 2,
  "soundGroup": "wood",
  "creativeTab": "westeros_wood_planks_tab",
  "label": "Birch Door",
  "textures": ["wood/birch/door_top", "wood/birch/door_bottom"],
  "allowUnsupported": true,
  "type": "allow-unsupported"
}
```

### Slab with Harvest Requirements
```json
{
  "blockName": "apple_basket_slab",
  "blockType": "slab",
  "soundGroup": "cloth",
  "harvestLevel": [{"tool": "shovel", "level": 1}],
  "creativeTab": "westeros_food_blocks_tab",
  "label": "Apple Basket Slab",
  "textures": [
    "crate_block/basket_bottom",
    "crate_block/basket_apple",
    "crate_block/basket_side_slab"
  ],
  "strength": 2
}
```

### Leaves with Random Textures and Tinting
```json
{
  "blockName": "apple_fruit_leaves",
  "blockType": "leaves",
  "soundGroup": "grass",
  "creativeTab": "westeros_foliage_tab",
  "type": "no-decay,better-foliage,overlay",
  "label": "Apple Fruit Leaves",
  "colorMult": "textures/colormap/birch",
  "rotateRandom": true,
  "randomTextures": [
    {"textures": ["leaves/birch/all", "transparent", "leaves/overlay/apple0"], "weight": 10},
    {"textures": ["leaves/birch/all", "transparent", "leaves/overlay/apple1"], "weight": 10},
    {"textures": ["leaves/birch/all", "transparent", "leaves/overlay/apple2"], "weight": 10},
    {"textures": ["leaves/birch/all", "transparent", "leaves/overlay/apple3"], "weight": 2}
  ]
}
```

### Crop with Growth States
```json
{
  "blockName": "crop_carrots",
  "blockType": "crop",
  "soundGroup": "grass",
  "type": "toggleOnUse,layerSensitive",
  "layerSensitive": true,
  "toggleOnUse": true,
  "renderLayer": "cutout",
  "harvestLevel": [{"tool": "pickaxe", "level": 3}],
  "creativeTab": "westeros_crops_herbs_tab",
  "label": "Carrots",
  "states": [
    {"stateID": "age0", "textures": ["carrots/carrots_stage_0"]},
    {"stateID": "age1", "textures": ["carrots/carrots_stage_1"]},
    {"stateID": "age2", "textures": ["carrots/carrots_stage_2"]},
    {"stateID": "age3", "textures": ["carrots/carrots_stage_3"]}
  ],
  "strength": 5
}
```

### Complex Stackable Block (Banner)
```json
{
  "blockName": "allyrion_banner",
  "blockType": "cuboid-nsew-stack",
  "soundGroup": "wood",
  "harvestLevel": [{"tool": "sword", "level": 1}],
  "creativeTab": "westeros_banners_tab",
  "type": "no-break-under,allowHalfBreak",
  "label": "Allyrion Banner",
  "itemTextureIndex": 2,
  "stack": [
    {
      "textures": ["transparent", "transparent", "transparent", "transparent", "banner/house/allyrion/bottom", "banner/house/allyrion/bottom"],
      "boundingBox": {"xMin": 0.96875, "xMax": 1, "yMin": 0, "yMax": 1, "zMin": 0, "zMax": 1}
    },
    {
      "textures": ["transparent", "transparent", "transparent", "transparent", "banner/house/allyrion/top", "banner/house/allyrion/top"],
      "boundingBox": {"xMin": 0.96875, "xMax": 1, "yMin": 0, "yMax": 1, "zMin": 0, "zMax": 1}
    }
  ],
  "strength": 1
}
```

This documentation serves as a comprehensive reference for understanding and implementing the WesterosBlocks block definition system.