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

### Block Factory Usage
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

### Data Generation Builder Pattern
```java
registerCustomSolidBlock(bsmg, ModBlocks.EXAMPLE_BLOCK)
    .texture("texture/path")
    .randomTexture("variant1", "variant2", "variant3")
    .build();
```

### Door Block Registration
Door blocks require wood type, lock state, and recipe generation parameters:
```java
new WCDoorBlock.Factory().buildBlockClass(settings, "woodType", isLocked, hasRecipe)
```

### Half Door Block Registration
Half door blocks (shutters) require locked state and unsupported allowance parameters:
```java
new WCHalfDoorBlock.Factory().buildBlockClass(settings, isLocked, allowUnsupported)
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
// Add appropriate import
import com.westerosblocks.block.custom.WC[BlockType]Block;

// Register block with factory pattern
public static final Block BLOCK_NAME = registerBlock(
    "block_name", // Must match definition file blockName
    new WC[BlockType]Block.Factory().buildBlockClass(
        AbstractBlock.Settings.create()
            .strength([hardness]f)
            .resistance([resistance]f)
            .requiresTool()
            .sounds(BlockSoundGroup.[SOUND_TYPE]), // From stepSound field
        [additional_parameters] // Varies by block type
    )
);

// Add to creative tabs (around line 1171)
entries.add(ModBlocks.BLOCK_NAME);
```

### 2. Data Generation (ModModelProvider.java)
```java
// Add appropriate exporter import
import com.westerosblocks.datagen.custom.[BlockType]BlockExporter;

// Register in generateBlockStateModels() method
[BlockType]BlockExporter.register[BlockType]Block(bsmg, ModBlocks.BLOCK_NAME, "texture/path");
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
- **Solid blocks**: Use `SolidBlockExporter.registerCustomSolidBlock()`
- **Door blocks**: Require wood type, lock state, recipe parameters
- **Half doors**: Require lock state, unsupported allowance parameters  
- **Slab blocks**: Use `SlabBlockExporter.registerSlabBlock()`
- **Log blocks**: Use `LogBlockExporter.registerLogBlock()`