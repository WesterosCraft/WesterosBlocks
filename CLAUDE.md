# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is WesterosBlocks, a Minecraft Fabric mod that adds a vast collection of decorative blocks inspired by the world of Game of Thrones/A Song of Ice and Fire. The mod provides building materials for medieval-style construction with regional themes.

## Build System & Commands

### Essential Commands
- `./gradlew build` - Build the mod
- `./gradlew runClient` - Launch Minecraft client with mod for testing
- `./gradlew runDatagen` - Generate data files (models, recipes, lang files)
- `./gradlew clean` - Clean build artifacts

### Development Environment
- **Java Version**: 21 (sourceCompatibility and targetCompatibility)
- **Minecraft Version**: 1.21.1
- **Fabric Loader**: 0.16.14
- **Fabric API**: 0.116.4+1.21.1
- **Loom Version**: 1.11-SNAPSHOT

## Architecture Overview

### Block Definition System
The mod uses a factory pattern approach for block creation:

**Reference Files** (`src/main/resources/defs/`): JSON files organized by type serve as reference material for the refactoring effort:
- `solid/` - Standard cube blocks
- `door/` - Door blocks with special behaviors  
- `log/` - Pillar blocks (rotatable)
- `slab/` - Half-height blocks
- `halfdoor/` - Half-door blocks (shutters)

**Factory Pattern**: Custom block classes use factory patterns to create instances with specific settings:
- `WCSolidBlock.Factory()` - For standard blocks
- `WCDoorBlock.Factory()` - For doors with wood types and lock states
- `WCLogBlock.Factory()` - For directional/rotatable blocks
- `WCSlabBlock.Factory()` - For slab variants
- `WCTableBlock.Factory()` - For table blocks

### Block Registration Flow
1. `ModBlocks.java` directly registers blocks using factory constructors with Minecraft settings
2. Data generation creates models, blockstates, and language files
3. OptiFine CTM textures provide connected texture support

### Data Generation Architecture
The mod has a sophisticated data generation system in `src/main/java/com/westerosblocks/datagen/`:

- **ModModelProvider**: Main provider that coordinates model generation
- **ModBlockStateModelGenerator**: Core generator with builder pattern for different block types
- **Custom Exporters**: Specialized exporters for each block type (SolidBlockExporter, DoorBlockExporter, etc.)

### Texture System
Textures are organized hierarchically in `src/main/resources/assets/westerosblocks/textures/`:
- Regional stone variants (`ashlar_half`, `ashlar_quarter`, etc.)
- Wood types (`bark/oak`, `bark/birch`, etc.)
- Specialty textures (`crate_block`, `barrel_sides`, etc.)

### OptiFine Integration
Extensive OptiFine CTM (Connected Texture Mod) support with properties files in `src/main/resources/assets/westerosblocks/optifine/ctm/` providing seamless texture connections for architectural blocks.

## Key Implementation Patterns

### Block Factory Usage (Legacy)
```java
public static final Block EXAMPLE_BLOCK = registerBlock(
    "block_name",
    new WCSolidBlock.Factory().buildBlockClass(
        AbstractBlock.Settings.create()
            .strength(2.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.WOOD)
    )
);
```

### Block Builder Pattern (Preferred)
Use the BlockBuilder pattern for cleaner, self-documenting block registration:

```java
public static final Block EXAMPLE_BLOCK = registerBlock(
    "block_name",
    BlockBuilder.solid()
        .hardness(def.getHardness())
        .requiresTool()
        .sounds(BlockSoundGroup.WOOD)
        .build()
);
```

**Available Block Types:**
- `BlockBuilder.solid()` - For WCSolidBlock
- `BlockBuilder.halfDoor()` - For WCHalfDoorBlock (shutters)
- `BlockBuilder.door()` - For WCDoorBlock
- `BlockBuilder.pane()` - For WCPaneBlock
- `BlockBuilder.slab()` - For WCSlabBlock
- `BlockBuilder.log()` - For WCLogBlock
- `BlockBuilder.table()` - For WCTableBlock

**Common Settings Methods:**
- `.strength(float)` - Block hardness
- `.resistance(float)` - Explosion resistance
- `.requiresTool()` - Requires proper tool to break
- `.sounds(BlockSoundGroup)` - Sound effects
- `.nonOpaque()` - For transparent blocks
- `.noCollision()` - For non-solid blocks

**Block-Specific Parameters:**
- `.locked()` - For doors/shutters that can't be opened
- `.allowUnsupported()` - For blocks that can float
- `.woodType(String)` - For doors requiring wood type
- `.hasRecipe()` - For recipe generation
- `.unconnect()` - For pane blocks
- `.legacyModel()` - For pane model variants
- `.barsModel()` - For pane bar models

### Data Generation Builder Pattern (Unified)
All block types now use a consistent unified builder pattern following block-models.md conventions:

```java
// Solid blocks - single texture
registerCustomSolidBlock(bsmg, ModBlocks.EXAMPLE_BLOCK)
    .texture("texture/path")
    .build();

// Solid blocks - multiple textures (down, up, north, south, east, west)
registerCustomSolidBlock(bsmg, ModBlocks.EXAMPLE_BLOCK)
    .textures("bottom", "top", "side")
    .build();

// Solid blocks - random texture variants
registerCustomSolidBlock(bsmg, ModBlocks.EXAMPLE_BLOCK)
    .randomTexture("variant1", "variant2", "variant3")
    .randomTexture("variant4", "variant5", "variant6")
    .build();

// Solid blocks - state variants
registerCustomSolidBlock(bsmg, ModBlocks.EXAMPLE_BLOCK)
    .state("state1_bottom", "state1_top", "state1_side")
    .state("state2_bottom", "state2_top", "state2_side")
    .build();

// Door blocks
registerCustomDoorBlock(bsmg, ModBlocks.EXAMPLE_DOOR)
    .textures("door_top", "door_bottom")
    .build();

// Half door blocks (shutters)
registerCustomHalfDoorBlock(bsmg, ModBlocks.EXAMPLE_SHUTTERS)
    .texture("shutters_texture")
    .build();

// Log blocks
registerCustomLogBlock(bsmg, ModBlocks.EXAMPLE_LOG)
    .textures("log_side", "log_end")
    .build();

// Slab blocks
registerCustomSlabBlock(bsmg, ModBlocks.EXAMPLE_SLAB)
    .textures("bottom", "top", "side")
    .build();

// Pane blocks
registerCustomPaneBlock(bsmg, ModBlocks.EXAMPLE_PANE)
    .texture("pane_texture")
    .build();

// Pane blocks with random textures
registerCustomPaneBlock(bsmg, ModBlocks.EXAMPLE_NET)
    .randomTexture("net1")
    .randomTexture("net2")
    .randomTexture("net3")
    .build();

// Branch blocks
registerCustomBranchBlock(bsmg, ModBlocks.EXAMPLE_BRANCH)
    .texture("branch_texture")
    .build();

// Torch blocks
registerCustomTorchBlock(bsmg, ModBlocks.EXAMPLE_TORCH)
    .texture("torch_texture")
    .build();

// Chair blocks
registerCustomChairBlock(bsmg, ModBlocks.EXAMPLE_CHAIR)
    .texture("chair_texture")
    .build();

// Table blocks
registerCustomTableBlock(bsmg, ModBlocks.EXAMPLE_TABLE)
    .texture("table_texture")
    .build();
```

### Door Block Registration
Door blocks require wood type, lock state, and recipe generation parameters:
```java
// Legacy approach
new WCDoorBlock.Factory().buildBlockClass(settings, "woodType", isLocked, hasRecipe)

// Preferred BlockBuilder approach
BlockBuilder.door()
    .strength(2.0f)
    .requiresTool()
    .sounds(BlockSoundGroup.WOOD)
    .woodType("oak")
    .locked(false)
    .hasRecipe(true)
    .build()
```

### Half Door Block Registration
Half door blocks (shutters) require locked state and unsupported allowance parameters:
```java
// Legacy approach
new WCHalfDoorBlock.Factory().buildBlockClass(settings, isLocked, allowUnsupported)

// Preferred BlockBuilder approach
BlockBuilder.halfDoor()
    .strength(2.0f)
    .requiresTool()
    .sounds(BlockSoundGroup.WOOD)
    .locked(false)
    .allowUnsupported(true)
    .build()
```

## Important Development Notes

### Entity System
The mod includes a chair entity system (`ChairEntity`, `ChairRenderer`) for interactive furniture blocks, demonstrating custom entity integration.

### Utility Blocks
Special utility blocks for world-building purposes are included (approval_utility_block, domestic_utility_block, etc.) for server administrative features.

### Sound and Material Properties
Blocks are carefully categorized with appropriate sound groups (WOOD, STONE, METAL, GRASS) and material properties for realistic gameplay feel.

### Creative Tab Integration
All blocks are automatically added to Minecraft's Building Blocks creative tab via `ItemGroupEvents.modifyEntriesEvent()`.

## Datagen Execution
Always run `./gradlew runDatagen` after adding new blocks to regenerate:
- Block models and item models
- Blockstate JSON files  
- Language files with proper block names
- Recipe files (where applicable)

This mod demonstrates advanced Fabric modding techniques including factory patterns, builder patterns, data generation, custom entities, and extensive texture system integration.

## Block Registration Implementation Steps

When implementing any new block from definition files, follow these steps:

### 1. Block Registration (ModBlocks.java)
```java
// Add appropriate imports
import com.westerosblocks.block.custom.WC[BlockType]Block;
import com.westerosblocks.block.custom.BlockBuilder;

// Register block with BlockBuilder pattern (preferred)
public static final Block BLOCK_NAME = registerBlock(
    "block_name", // Must match definition file blockName
    BlockBuilder.[blockType]()
        .strength([hardness]f)
        .resistance([resistance]f)
        .requiresTool()
        .sounds(BlockSoundGroup.[SOUND_TYPE]) // From stepSound field
        .[parameter](value) // Block-specific parameters with clear names
        .build()
);

// Legacy factory pattern (still supported)
public static final Block BLOCK_NAME = registerBlock(
    "block_name",
    new WC[BlockType]Block.Factory().buildBlockClass(
        AbstractBlock.Settings.create().strength([hardness]f).sounds([SOUND_TYPE]),
        [additional_parameters] // Varies by block type
    )
);

// Add to creative tabs (around line 1171)
entries.add(ModBlocks.BLOCK_NAME);
```

### 2. Data Generation (ModModelProvider.java)
```java
// Import the unified builder
import static com.westerosblocks.datagen.ModBlockStateModelGenerator.*;

// Register in generateBlockStateModels() method using unified builder pattern
registerCustom[BlockType]Block(bsmg, ModBlocks.BLOCK_NAME)
    .texture("texture/path")
    .build();

// Examples for different block types:
registerCustomSolidBlock(bsmg, ModBlocks.SOLID_BLOCK).texture("texture_path").build();
registerCustomDoorBlock(bsmg, ModBlocks.DOOR_BLOCK).textures("top_texture", "bottom_texture").build();
registerCustomSlabBlock(bsmg, ModBlocks.SLAB_BLOCK).textures("bottom", "top", "side").build();
registerCustomLogBlock(bsmg, ModBlocks.LOG_BLOCK).textures("side_texture", "end_texture").build();
registerCustomPaneBlock(bsmg, ModBlocks.PANE_BLOCK).texture("pane_texture").build();
registerCustomHalfDoorBlock(bsmg, ModBlocks.SHUTTER_BLOCK).texture("shutter_texture").build();
registerCustomTorchBlock(bsmg, ModBlocks.TORCH_BLOCK).texture("torch_texture").build();
registerCustomChairBlock(bsmg, ModBlocks.CHAIR_BLOCK).texture("chair_texture").build();
registerCustomTableBlock(bsmg, ModBlocks.TABLE_BLOCK).texture("table_texture").build();
registerCustomBranchBlock(bsmg, ModBlocks.BRANCH_BLOCK).texture("branch_texture").build();
```

### 3. Language Generation (ModLanguageProvider.java)
```java
// Add translation entry
translationBuilder.add("block.westerosblocks.block_name", "Display Name"); // From label field
```

### 4. Critical Requirements
- **Block name** must exactly match the `blockName` field in definition JSON
- **Texture paths** come from the `textures` array in definition file
- **Factory parameters** vary by block type (check existing examples)
- **Sound groups** map: wood→WOOD, stone→STONE, etc.
- **Item models** are auto-generated by exporters - DO NOT call `generateItemModels()` separately to avoid duplicates
- Always run `./gradlew runDatagen` after registration to generate models and assets

### 5. Block Type Specific Notes

**All block types now use unified builder pattern methods:**

- **Solid blocks**: `registerCustomSolidBlock(bsmg, block).texture("path").build()`
- **Door blocks**: `registerCustomDoorBlock(bsmg, block).textures("top", "bottom").build()`
- **Half doors**: `registerCustomHalfDoorBlock(bsmg, block).texture("shutter_texture").build()`
- **Slab blocks**: `registerCustomSlabBlock(bsmg, block).textures("bottom", "top", "side").build()`
- **Log blocks**: `registerCustomLogBlock(bsmg, block).textures("side", "end").build()`
- **Pane blocks**: `registerCustomPaneBlock(bsmg, block).texture("pane_texture").build()`
- **Torch blocks**: `registerCustomTorchBlock(bsmg, block).texture("torch_texture").build()`
- **Chair blocks**: `registerCustomChairBlock(bsmg, block).texture("chair_texture").build()`
- **Table blocks**: `registerCustomTableBlock(bsmg, block).texture("table_texture").build()`
- **Branch blocks**: `registerCustomBranchBlock(bsmg, block).texture("branch_texture").build()`

**Builder Methods Available:**
- `.texture(String)` - Single texture for all sides
- `.textures(String...)` - Multiple textures for different sides
- `.randomTexture(String...)` - Add random texture variants (solid/pane blocks)
- `.state(String...)` - Add state variants (solid blocks only)

**Important:** 
- Always use the unified builder pattern - DO NOT call exporters directly
- All builders require `.build()` at the end
- The system automatically handles model creation, blockstate generation, and item model registration
- Complex block logic is handled internally by specialized exporters