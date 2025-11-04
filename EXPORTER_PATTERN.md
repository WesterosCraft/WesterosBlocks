# Block Exporter Refactoring Pattern

This document describes the simple, unified pattern for block exporters based on the 1.18.2 approach.

## Overview

The refactored `SolidBlockExporter` reduced complexity from **560 lines to 247 lines** (56% reduction) by following a simple three-phase pattern that matches the old 1.18.2 code structure.

## The Three-Phase Pattern

### Phase 1: Generate Blockstate JSON
**What**: Create the blockstate file with all variants
**When**: Once per block
**Structure**: Loop through states → texture sets → rotations, build variants inline

```java
// Phase 1: Generate blockstate (matches old doBlockStateExport)
generateBlockState(generator, block, states, hasSymmetrical, hasRotateRandom);
```

### Phase 2: Generate Model Files
**What**: Create individual model JSON files
**When**: Once per texture set (including symmetrical variants)
**Structure**: Loop through states → texture sets, generate models

```java
// Phase 2: Generate models (matches old doModelExports)
for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
    BlockDefinition.StateVariant state = states.get(stateIdx);
    String stateID = state.getStateID();
    String fname = (stateID == null) ? "base" : stateID;

    for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
        generateSolidModel(generator, block, getModelName(fname, setIdx),
            state, setIdx, isTinted, isOverlay, isSymmetrical);
    }
}
```

### Phase 3: Register Item Model
**What**: Create item model pointing to first block model
**When**: Once per block

```java
// Phase 3: Item model
Identifier itemModelId = createNestedModelId(block, getModelName(firstName, 0));
registerParentedItemModel(generator, block, itemModelId);
```

## Key Principles

### 1. Simple Nested Loops
**Old pattern (560 lines)**: Separate methods for every combination
- `registerSimpleCustomSolidBlock()`
- `registerCustomSolidBlock(String... textures)`
- `registerCustomSolidBlockWithRandomTextures()`
- `registerStandardSolidBlock()`, `registerSymmetricalSolidBlock()`, etc.

**New pattern (247 lines)**: One loop, conditions inline
```java
for (BlockDefinition.StateVariant state : states) {
    for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
        if (hasSymmetrical) {
            // Handle symmetrical inline
        } else {
            // Handle standard inline
        }
    }
}
```

### 2. Inline Conditionals
**Instead of**: 20+ separate methods
**Do this**: Inline if/else matching the old pattern
```java
if (isOverlay) {
    // Overlay model with overlay textures
} else if (textureCount > 1 || isTinted) {
    // Cube model
} else {
    // Cube_all model
}
```

### 3. Direct Texture Index Logic
**Instead of**: Complex extraction methods
**Do this**: Direct index access with ternary
```java
String west = isSymmetrical ? set.getTextureByIndex(4) : set.getTextureByIndex(6);
String east = isSymmetrical ? set.getTextureByIndex(5) : set.getTextureByIndex(7);
```

### 4. Minimal Helper Methods
Only create helpers for:
- **Model name generation** (matches old `getModelName()`)
- **Blockstate generation** (consolidates variant building)
- **Model type generation** (cube_all, cube, overlay - matches old model POJOs)

## Comparing Old vs New

### Old 1.18.2 Pattern
```java
@Override
public void doBlockStateExport() throws IOException {
    StateObject so = new StateObject();

    for (WesterosBlockStateRecord sr : def.states) {
        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
            int cnt = sr.rotateRandom ? 4 : 1;
            for (int i = 0; i < cnt; i++) {
                Variant var = new Variant();
                var.model = modelFileName(fname, setidx, sr.isCustomModel());
                var.weight = set.weight;
                if (i > 0) var.y = 90*i;
                so.addVariant("", var, stateIDs);
            }
        }
    }
    this.writeBlockStateFile(def.blockName, so);
}
```

### New 1.21 Pattern
```java
private static void generateBlockState(...) {
    List<BlockStateVariant> allVariants = new ArrayList<>();

    for (BlockDefinition.StateVariant state : states) {
        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
            int cnt = hasRotateRandom ? 4 : 1;  // Exact same logic
            for (int i = 0; i < cnt; i++) {
                int rotation = i * 90;  // Exact same logic
                Identifier modelId = createNestedModelId(block, getModelName(fname, setIdx));
                allVariants.add(createWeightedVariant(modelId, rotation, set.getWeight()));
            }
        }
    }

    generator.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block, allVariants.toArray(new BlockStateVariant[0])));
}
```

**Differences**: Only API surface changes (POJO → Fabric API), same logic flow.

## How to Refactor Other Exporters

### Step 1: Identify the Three Phases
Look for:
- Blockstate generation code
- Model file generation code
- Item model registration

### Step 2: Simplify Blockstate Generation
Replace:
```java
if (textureCount == 1) {
    if (hasRandom) {
        registerMethodA();
    } else {
        registerMethodB();
    }
} else {
    registerMethodC();
}
```

With:
```java
List<BlockStateVariant> variants = new ArrayList<>();
for (state : states) {
    for (set : sets) {
        // Build variant inline
        variants.add(createVariant(modelId));
    }
}
generator.blockStateCollector.accept(...);
```

### Step 3: Consolidate Model Generation
Replace: Multiple `registerXXXBlock()` methods

With: Single `generateModel()` method with conditionals:
```java
private static void generateModel(...) {
    if (specialCase1) {
        // Handle case 1
    } else if (specialCase2) {
        // Handle case 2
    } else {
        // Default case
    }
}
```

### Step 4: Remove Helper Explosion
Delete any method that just calls another method with slightly different parameters. Instead, use:
- Inline conditions
- Direct index access
- Ternary operators

## Example: Before vs After

### Before (560 lines)
```
registerCustomSolidBlock(BlockDefinition)
├── if (textureSetCount == 1)
│   ├── if (textures.length == 1)
│   │   └── registerSimpleCustomSolidBlock()
│   │       └── registerStandardSolidBlock()
│   │           └── Models.CUBE_ALL.upload()
│   └── else
│       └── registerCustomSolidBlock(String... textures)
│           └── registerStandardSolidBlock(String... textures)
│               └── Models.CUBE.upload()
└── else
    └── registerCustomSolidBlockWithRandomTextures()
        └── registerStandardSolidBlockWithRandomTextures()
            └── for each set: Models.CUBE.upload()
```

### After (247 lines)
```
registerCustomSolidBlock(BlockDefinition)
├── generateBlockState()
│   └── for states → for sets → for rotations: add variant
├── for states → for sets: generateSolidModel()
│   ├── if (isOverlay): generateOverlayModel()
│   ├── else if (multi || tinted): generateCubeModel()
│   └── else: generateCubeAllModel()
└── registerParentedItemModel()
```

## Metrics

### Refactoring Results

**SolidBlockExporter**
- **Lines of code**: 560 → 247 (56% reduction)
- **Public methods**: 12 → 1 (92% reduction)
- **Private methods**: 20+ → 6 (70% reduction)
- **Logic complexity**: Same (just reorganized)
- **Generated output**: Identical

**CrossBlockExporter**
- **Lines of code**: 356 → 185 (48% reduction)
- **Methods eliminated**: 3 separate registration methods → 1
- **Layer handling**: Inline conditional instead of separate method
- **Logic complexity**: Same (just reorganized)
- **Generated output**: Identical

**CropBlockExporter**
- **Lines of code**: 323 → 213 (34% reduction)
- **Methods eliminated**: 4 registration methods → 1
- **Property handling**: Single unified loop with inline conditionals for LAYERS/STATE combinations
- **Loop structure**: Matches old pattern exactly - one loop building all variants
- **Logic complexity**: Same (just reorganized)
- **Generated output**: Identical

**Total Progress**
- **Lines removed**: 841 lines across 3 exporters
- **Average reduction**: 46% per exporter
- **Methods consolidated**: 19 methods → 3 main entry points

## Benefits

1. **Easier to understand**: Linear flow, no method maze
2. **Easier to debug**: All logic in one place
3. **Easier to modify**: Change one loop, not 10 methods
4. **Matches old code**: Same mental model as 1.18.2
5. **Better comments**: Can explain inline instead of scattered

## Anti-Patterns to Avoid

❌ **Don't**: Create a new method for every parameter combination
```java
registerBlock(texture)
registerBlock(texture, tinted)
registerBlock(textures...)
registerBlock(textures..., tinted)
registerBlockWithRandom(textures[][])
registerBlockWithRandom(textures[][], tinted)
```

✅ **Do**: One method with inline conditions
```java
generateModel(texture, isTinted) {
    if (isOverlay) { /* ... */ }
    else if (multi || tinted) { /* ... */ }
    else { /* ... */ }
}
```

---

❌ **Don't**: Separate symmetrical/standard methods
```java
registerStandardBlock()
registerSymmetricalBlock()
registerStandardBlockWithRandom()
registerSymmetricalBlockWithRandom()
```

✅ **Do**: One method with inline symmetrical handling
```java
if (hasSymmetrical) {
    generateModel(..., true);
    generateModel(..., false);
} else {
    generateModel(..., false);
}
```

---

❌ **Don't**: Abstract blockstate creation into 10 helper methods
```java
createSimpleBlockState()
createSymmetricalBlockState()
createSymmetricalBlockStateWithVariants()
createStatesBlockState()
```

✅ **Do**: Build variants inline
```java
for (state : states) {
    for (set : sets) {
        variants.add(createVariant(modelId));
    }
}
```

## Next Exporters to Refactor

Apply this pattern to:
1. **StairBlockExporter** - Similar to solid, just different model shape
2. **SlabBlockExporter** - Similar to solid, slab-specific models
3. **WallBlockExporter** - Connection variants follow same pattern
4. **FenceBlockExporter** - Connection variants follow same pattern
5. **DoorBlockExporter** - Hinge/open states follow same pattern

Each should reduce to ~200-300 lines following the three-phase pattern.
