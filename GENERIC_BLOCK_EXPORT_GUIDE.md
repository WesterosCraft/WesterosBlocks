# Generic Block Export Implementation Guide

## Overview
This guide provides a generic process for creating new block exporters in WesterosBlocks mod, applicable to any block type (vines, torches, doors, logs, etc.). It documents the pattern for creating builder-based block exporters that follow the established codebase conventions.

## Generic Step-by-Step Implementation

### Step 1: Analyze Existing Patterns
**Goal**: Understand the existing codebase patterns before creating new implementations.

**Actions**:
1. **Examine Existing Exporters** - Study similar block type exporters (e.g., `TorchBlockExport`, `DoorBlockExport`, `LogBlockExport`)
2. **Review `ModModelProvider2.java`** - Understand the static exporter pattern used throughout the codebase
3. **Analyze Block Class** - Understand the builder-based block structure and properties
4. **Check `ModTextureKey.java`** - Verify texture key system for the block type

**Key Findings**:
- Existing exporters use static instances with methods taking generator and block parameters
- Each block type has specific texture keys and model templates
- Builder-based blocks have different interfaces than definition-based ones
- Follow the established naming and method signature patterns

### Step 2: Create Generic Block Export Class
**Goal**: Create a new exporter specifically for builder-based block instances.

**File**: `src/main/java/com/westerosblocks/datagen/models/{BlockType}BlockExport.java`

**Key Design Decisions**:
1. **Follow Established Pattern**: Use static instance with methods taking generator and block parameters
2. **Simplified Interface**: Take texture paths directly instead of `ModBlock` definitions
3. **Builder-Based**: Designed for builder-based block system
4. **Same Output**: Produce identical block state and model structure as original

**Generic Implementation Template**:
```java
public class {BlockType}BlockExport {
    public {BlockType}BlockExport() {
    }

    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String... textures) {
        // Implementation specific to block type
        // Handle different block state properties
        // Generate appropriate model variants
    }

    public void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, String texture) {
        // Item model generation using primary texture
    }
}
```

**Features**:
- Generates block state models appropriate for the block type
- Handles block-specific properties and variants
- Uses appropriate model templates for the block type
- Supports multiple textures if needed
- Generates item models using the primary texture

### Step 3: Integrate into ModModelProvider2
**Goal**: Add the new exporter to the existing model generation system.

**Actions**:
1. **Add Import**: Import the block class if needed
2. **Add Static Instance**: `static {BlockType}BlockExport {blockType}Exporter = new {BlockType}BlockExport();`
3. **Add Block State Generation**: Add calls in `generateBlockStateModels()` method
4. **Add Item Model Generation**: Add calls in `generateItemModels()` method

**Integration Pattern**:
```java
// Block state generation
{blockType}Exporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.EXAMPLE_BLOCK, "texture1", "texture2");

// Item model generation  
{blockType}Exporter.generateItemModels(itemModelGenerator, ModBlocks2.EXAMPLE_BLOCK, "texture1");
```

### Step 4: Register Additional Blocks
**Goal**: Register all blocks of this type from JSON definitions in the builder-based system.

**Actions**:
1. **Add Block Registrations** in `ModBlocks2.java`:
   - Use appropriate `register{BlockType}Block()` method
   - Configure builder with appropriate properties
   - Set correct creative tab, hardness, resistance, etc.

2. **Add Model Generation** in `ModModelProvider2.java`:
   - Block state generation for all new blocks
   - Item model generation for all new blocks

3. **Add Translations** in `ModLanguageProvider.java`:
   - Display names for all new blocks
   - Follow naming convention: `"block.westerosblocks.{block_name}"`

### Step 5: Block Type Specific Considerations
**Goal**: Understand how different block types require different implementations.

**Common Block Types and Their Requirements**:

#### Vines Blocks
- **Class**: `StandaloneVinesBlock` extends `VineBlock`
- **Properties**: 6-direction support, conditional DOWN growth, waterlogging
- **Textures**: Base and top textures
- **Models**: `vine_1` template for base, `vine_u` template for top

#### Torch Blocks
- **Class**: `WCTorchBlock` with `WCWallTorchBlock`
- **Properties**: Wall variants, light levels, particle effects
- **Textures**: Single texture for both standing and wall variants
- **Models**: Standard torch templates

#### Door Blocks
- **Class**: `WCDoorBlock`
- **Properties**: Locked variants, wood types, secret doors
- **Textures**: Top and bottom textures
- **Models**: Door templates with proper rotations

#### Log Blocks
- **Class**: Custom log block classes
- **Properties**: Multiple textures (top, bottom, side), wood types
- **Textures**: Three textures (top, bottom, side)
- **Models**: Log templates with proper axis handling

## Generic File Structure Created/Modified

### New Files
- `src/main/java/com/westerosblocks/datagen/models/{BlockType}BlockExport.java`

### Modified Files
- `src/main/java/com/westerosblocks/block/ModBlocks2.java` - Added block registrations
- `src/main/java/com/westerosblocks/datagen/ModModelProvider2.java` - Added model generation
- `src/main/java/com/westerosblocks/datagen/ModLanguageProvider.java` - Added translations

## Generic Usage Pattern

### Registering New Blocks
```java
public static final Block NEW_BLOCK = register{BlockType}Block(
    "new_block",
    builder -> builder
        .creativeTab("westeros_appropriate_tab")
        .hardness(2.0f)
        .resistance(6.0f)
        .texture("texture_path") // or .textures() for multiple
);
```

### Adding to Model Generation
```java
// In generateBlockStateModels()
{blockType}Exporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NEW_BLOCK, 
    "texture1", "texture2"); // appropriate number of textures

// In generateItemModels()
{blockType}Exporter.generateItemModels(itemModelGenerator, ModBlocks2.NEW_BLOCK, "texture1");
```

### Adding Translations
```java
// In ModLanguageProvider.java
translationBuilder.add("block.westerosblocks.new_block", "New Block");
```

## Block Type Specific Implementation Guide

### For Vines Blocks
1. **Use `StandaloneVinesBlock`** as the block class
2. **Handle 6 directions** (NORTH, SOUTH, EAST, WEST, UP, DOWN)
3. **Conditional DOWN** based on `canGrowDownward()` property
4. **Use `ModTextureKey.VINES`** for texture mapping
5. **Use vine templates** (`vine_1` for base, `vine_u` for top)

### For Torch Blocks
1. **Use `WCTorchBlock`** as the block class
2. **Handle wall variants** automatically
3. **Use light level** property for brightness
4. **Use `ModTextureKey.TEXTURE_0`** for texture mapping
5. **Use torch templates** with proper rotations

### For Door Blocks
1. **Use `WCDoorBlock`** as the block class
2. **Handle locked variants** and wood types
3. **Use top and bottom textures**
4. **Use `ModTextureKey.TEXTURE_0` and `ModTextureKey.TEXTURE_1`**
5. **Use door templates** with proper rotations

### For Log Blocks
1. **Use custom log classes** as the block class
2. **Handle axis-based variants** (X, Y, Z)
3. **Use three textures** (top, bottom, side)
4. **Use `ModTextureKey.TEXTURE_0`, `ModTextureKey.TEXTURE_1`, `ModTextureKey.TEXTURE_2`**
5. **Use log templates** with proper axis handling

## Testing and Validation

### Compilation Check
- Ensure all imports are correct
- Verify method signatures match established patterns
- Check that all blocks are properly registered

### Runtime Validation
- Verify block state models are generated correctly
- Check that item models are created
- Confirm translations appear in-game
- Test block placement and behavior

## Future Enhancements

### Potential Improvements
1. **Random Textures**: Implement support for multiple texture sets
2. **Color Tinting**: Add Polytone support for biome-based color tinting
3. **Advanced Properties**: Support for additional block-specific properties
4. **Automated Registration**: Create system to automatically register from JSON definitions

### Polytone Integration
For blocks with `colorMult` properties, follow the cursor rules to convert to Polytone:
1. Create colormap configuration JSON
2. Copy texture to polytone/colormaps folder
3. Create block properties modifier
4. Create item modifier for tinting
5. Remove old `colorMult` property

## Conclusion

This generic approach ensures that any new block type can be implemented following the established WesterosBlocks patterns. The key is to:

1. **Study existing implementations** for similar block types
2. **Follow the established patterns** for exporters and integration
3. **Handle block-specific properties** appropriately
4. **Test thoroughly** to ensure proper functionality

The implementation maintains consistency with the existing codebase while providing the flexibility needed for different block types. 