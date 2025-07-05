# ModelExport2 - Enhanced Model Export Utility

## Overview

`ModelExport2` is a new abstract utility class designed to simplify and standardize the creation of model export classes for WesterosBlocks. It provides common functionality for creating block states, models, and handling rotations that are frequently used across different block types.

## Key Features

### 1. Common Constants
- `GENERATED_PATH` and `CUSTOM_PATH` for consistent path handling
- `TEXTURE_KEY`, `TEXTURE_KEY_1`, `PARTICLE_KEY` for texture mapping
- `CARDINAL_DIRECTIONS`, `CARDINAL_ROTATIONS`, `CARDINAL_ROTATION_VARIANTS` for direction handling

### 2. Model Creation Utilities
- `createModel()` - Creates models with custom texture keys
- `createModelWithKey1()` - Creates models with the "1" texture key
- `createBlockIdentifier()` - Creates block identifiers from texture paths
- `createModelId()` - Creates model identifiers for blocks

### 3. Variant Creation Utilities
- `createVariant()` - Creates BlockStateVariants with optional rotations
- `getRotation()` - Converts degrees to VariantSettings.Rotation
- `createCardinalVariants()` - Creates variants for all cardinal directions

### 4. Block State Registration
- `registerBlockState()` - Registers block states with variant maps
- `registerMultipartBlockState()` - Registers multipart block states

### 5. Item Model Generation
- `generateItemModel()` - Generates simple item models that inherit from block models

## Usage Examples

### Basic Implementation

```java
public class MyBlockExport extends ModelExport2 {
    
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create models
        Identifier modelId = createModel(generator, block, "parent/model/path", texturePath, "variant");
        
        // Create variants for cardinal directions
        BlockStateVariantMap variants = BlockStateVariantMap.create(MyBlock.FACING)
            .register(Direction.NORTH, createVariant(modelId))
            .register(Direction.EAST, createVariant(modelId, 90))
            .register(Direction.SOUTH, createVariant(modelId, 180))
            .register(Direction.WEST, createVariant(modelId, 270));
        
        // Register the block state
        registerBlockState(generator, block, variants);
    }
    
    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        generateItemModel(generator, block, "variant");
    }
}
```

### Using Cardinal Variants Utility

```java
// For blocks with the same model for all directions
BlockStateVariantMap variants = createCardinalVariants(modelId);

// For blocks with different models per direction
BlockStateVariantMap variants = createCardinalVariants(
    northModel, eastModel, southModel, westModel
);
```

### Complex Block States

```java
// For blocks with multiple properties
BlockStateVariantMap variants = BlockStateVariantMap.create(MyBlock.FACING, MyBlock.TYPE)
    .register(Direction.NORTH, MyType.SINGLE, createVariant(singleModel))
    .register(Direction.EAST, MyType.SINGLE, createVariant(singleModel, 90))
    .register(Direction.NORTH, MyType.DOUBLE, createVariant(doubleModel))
    .register(Direction.EAST, MyType.DOUBLE, createVariant(doubleModel, 90));
```

## Migration from Existing Classes

### Before (ChairBlockExport.java)
```java
public class ChairBlockExport {
    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        Identifier cardinalModelId = ModelExportUtils.createModel(
            generator, block, 
            "westerosblocks:block/custom/chair/simple_chair", 
            texturePath, "cardinal"
        );
        // ... more code ...
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }
}
```

### After (ChairBlockExport2.java)
```java
public class ChairBlockExport2 extends ModelExport2 {
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        Identifier cardinalModelId = createModel(
            generator, block, 
            "westerosblocks:block/custom/chair/simple_chair", 
            texturePath, "cardinal"
        );
        // ... more code ...
        registerBlockState(generator, block, variants);
    }
}
```

## Benefits

1. **Reduced Code Duplication** - Common patterns are abstracted into utility methods
2. **Consistent API** - All model export classes follow the same structure
3. **Easier Maintenance** - Changes to common functionality only need to be made in one place
4. **Better Readability** - Code is more focused on the specific logic for each block type
5. **Type Safety** - Abstract methods ensure all required functionality is implemented

## Abstract Methods

Classes extending `ModelExport2` must implement:

- `generateBlockStateModels(BlockStateModelGenerator, Block, String)` - Generate block state models
- `generateItemModels(ItemModelGenerator, Block)` - Generate item models

## Compatibility

`ModelExport2` is designed to work alongside the existing `ModelExport` and `ModelExportUtils` classes. It provides additional utilities while maintaining compatibility with existing code patterns. 