# WesterosBlocks Custom Block Documentation

This document provides comprehensive documentation for all custom block types in the WesterosBlocks mod, their properties, methods, and associated datagen exporters.

---

## 1. WCSolidBlock
### Description
Standard cube blocks with optional connection states, toggle functionality, multiple states, and symmetrical variants.

### Properties
- `toggleOnUse: boolean` - Allows cycling through states in creative mode
- `connectState: boolean` - Enables connection state property
- `symmetrical: boolean` - Whether block has symmetrical/asymmetrical variants
---

## 2. WCDoorBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCDoorBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/DoorBlockDatagen.java`

### Description
Door blocks with configurable wood types, lock states, and placement restrictions.

### Properties
- `locked: boolean` - Whether the door can be opened/closed
- `allowUnsupported: boolean` - Whether door can exist without bottom support

### Factory Parameters
```java
// Map-based parameters
Map<String, Object> params = {
    "woodType": String,          // Wood type for block set (default: "oak")
    "locked": boolean,           // Cannot be opened if true
    "allowUnsupported": boolean  // Can float without support
}

// Legacy parameters
new WCDoorBlock(settings, woodType, locked, allowUnsupported)
```

### Custom Methods
- `onUse()` - Prevents opening/closing if locked
- `canPlaceAt()` - Checks support requirements if not allowUnsupported

### Datagen Registration Methods
```java
registerCustomDoorBlock(bsmg, block)
    .textures("door_top", "door_bottom")
    .build()
```

---

## 3. WCSlabBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCSlabBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/SlabBlockExporter.java`

### Description
Half-height blocks with optional connection states and toggle functionality.

### Properties
- `toggleOnUse: boolean` - Allows cycling through states in creative mode
- `connectState: boolean` - Enables connection state property
- `CONNECTSTATE: IntProperty` - Connection state property
- `STATE: StateProperty` - Custom state property for multiple variants

### Factory Parameters
```java
// Legacy parameters only
new WCSlabBlock(settings, connectState, toggleOnUse, addStates)
```

### Custom Methods
- `onUse()` - Handles state cycling in creative mode when `toggleOnUse` is true

### Datagen Registration Methods
```java
registerCustomSlabBlock(bsmg, block)
    .textures("bottom", "top", "side")
    .build()
```

---

## 4. WCPaneBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCPaneBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/PaneBlockExporter.java`

### Description
Glass pane-like blocks with connection behavior and model variants.

### Properties
- `UNCONNECT: BooleanProperty` - Controls connection behavior
- `unconnect: boolean` - Whether block connects to adjacent blocks
- `legacy_model: boolean` - Uses legacy model style
- `bars_model: boolean` - Uses bars model style

### Factory Parameters
```java
// Map-based parameters
Map<String, Object> params = {
    "unconnect": boolean,     // Disables connections
    "legacyModel": boolean,   // Uses legacy model
    "barsModel": boolean      // Uses bars model
}

// Legacy string parameters (comma-separated)
"unconnect:true,legacy_model:false,bars_model:true"
```

### Custom Methods
- `getStateForNeighborUpdate()` - Custom connection logic

### Datagen Registration Methods
```java
// Single texture
registerCustomPaneBlock(bsmg, block).texture("pane_texture").build()

// Random texture variants
registerCustomPaneBlock(bsmg, block)
    .randomTexture("variant1")
    .randomTexture("variant2")
    .build()
```

---

## 5. WCHalfDoorBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCHalfDoorBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/HalfDoorBlockExporter.java`

### Description
Single-height door blocks (shutters) with lock and placement options.

### Properties
- `locked: boolean` - Whether the door can be opened/closed
- `allowUnsupported: boolean` - Whether door can exist without bottom support

### Factory Parameters
```java
// Map-based parameters
Map<String, Object> params = {
    "locked": boolean,           // Cannot be opened if true
    "allowUnsupported": boolean  // Can float without support
}

// Legacy parameters
new WCHalfDoorBlock(settings, locked, allowUnsupported)
```

### Custom Methods
- `onUse()` - Prevents opening/closing if locked
- `canPlaceAt()` - Checks support requirements

### Datagen Registration Methods
```java
registerCustomHalfDoorBlock(bsmg, block)
    .texture("shutter_texture")
    .build()
```

---

## 6. WCLogBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCLogBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/LogBlockExporter.java`

### Description
Pillar blocks with directional placement (logs, columns).

### Properties
- Inherits from PillarBlock (AXIS property for rotation)

### Factory Parameters
```java
// No custom parameters - uses standard PillarBlock constructor
new WCLogBlock(settings)
```

### Custom Methods
- Inherits standard PillarBlock rotation behavior

### Datagen Registration Methods
```java
registerCustomLogBlock(bsmg, block)
    .textures("log_side", "log_end")
    .build()
```

---

## 7. WCChairBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCChairBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/ChairBlockExporter.java`

### Description
Sittable furniture blocks with entity interaction.

### Properties
- Inherits directional properties (FACING)
- Integrates with ChairEntity for sitting functionality

### Custom Methods
- `onUse()` - Spawns ChairEntity for player sitting
- `getOutlineShape()` - Custom chair collision shape

### Datagen Registration Methods
```java
registerCustomChairBlock(bsmg, block)
    .texture("chair_texture")
    .build()
```

---

## 8. WCTorchBlock / WCWallTorchBlock

**Files**: 
- `src/main/java/com/westerosblocks/block/custom/WCTorchBlock.java`
- `src/main/java/com/westerosblocks/block/custom/WCWallTorchBlock.java`

**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/TorchBlockExporter.java`

### Description
Light-emitting torch blocks with ground and wall variants.

### Properties
- Light emission level
- Wall attachment properties (WCWallTorchBlock)

### Datagen Registration Methods
```java
registerCustomTorchBlock(bsmg, block)
    .texture("torch_texture")
    .build()
```

---

## 9. WCFanBlock / WCWallFanBlock

**Files**:
- `src/main/java/com/westerosblocks/block/custom/WCFanBlock.java`
- `src/main/java/com/westerosblocks/block/custom/WCWallFanBlock.java`

**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/FanBlockExporter.java`

### Description
Decorative fan-shaped blocks (plant-like) with ground and wall variants.

### Datagen Registration Methods
```java
registerCustomFanBlock(bsmg, block)
    .texture("fan_texture")
    .build()
```

---

## 10. WCTableBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCTableBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/TableBlockExporter.java`

### Description
Table furniture blocks with custom shapes and connection behavior.

### Custom Methods
- `getOutlineShape()` - Custom table collision shape
- Connection logic for table combinations

### Datagen Registration Methods
```java
registerCustomTableBlock(bsmg, block)
    .texture("table_texture")
    .build()
```

---

## 11. WCBranchBlock

**File**: `src/main/java/com/westerosblocks/block/custom/WCBranchBlock.java`  
**Exporter**: `src/main/java/com/westerosblocks/datagen/custom/BranchBlockExporter.java`

### Description
Tree branch decorative blocks with complex connection states.

### Properties
- Multiple connection properties for different directions
- Complex shape calculations

### Datagen Registration Methods
```java
registerCustomBranchBlock(bsmg, block)
    .texture("branch_texture")
    .build()
```

---

## Common Patterns

### Factory Pattern
All blocks implement a `Factory` inner class that extends `BlockFactory`:
```java
public static class Factory extends BlockFactory {
    @Override
    public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
        // Parameter parsing and block creation
    }
}
```

### Builder Pattern Registration
Modern registration uses the BlockBuilder pattern:
```java
public static final Block EXAMPLE = registerBlock(
    "example",
    BlockBuilder.solid()
        .strength(2.0f)
        .requiresTool()
        .sounds(BlockSoundGroup.STONE)
        .parameter("connectState", true)
        .build()
);
```

### Datagen Unified Builder
All exporters support a unified builder pattern:
```java
registerCustom[BlockType]Block(bsmg, block)
    .texture("path")           // Single texture
    .textures("p1", "p2")     // Multiple textures
    .randomTexture("p1")      // Add random variant
    .state("p1", "p2")        // Add state variant
    .build();                 // Required to finalize
```

### Common Properties
- **Connection States**: Many blocks support `CONNECTSTATE` property
- **Toggle States**: Creative mode state cycling with `toggleOnUse`
- **Custom States**: Multi-value state properties with `StateProperty`
- **Symmetry**: Blocks can have symmetrical/asymmetrical variants

---

## Development Notes

1. **Always run datagen** after adding new blocks: `./gradlew runDatagen`
2. **Use builder patterns** for new implementations over factory constructors
3. **Follow naming conventions**: Block names must match JSON definition files
4. **Texture organization**: Textures are hierarchically organized by material type
5. **Model consistency**: All exporters follow block-models.md patterns for consistency