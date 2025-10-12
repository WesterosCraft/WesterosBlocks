
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