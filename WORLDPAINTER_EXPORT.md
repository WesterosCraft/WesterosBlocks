# WorldPainter CSV Export for WesterosBlocks

This mod includes support for exporting a WorldPainter-compatible CSV file that defines all custom blocks for use in WorldPainter.

## What is WorldPainter?

WorldPainter is a tool for creating custom Minecraft worlds. It supports custom blocks from mods through CSV definition files that describe block properties, lighting, orientation schemes, and other characteristics.

## How to Export the CSV File

### Configuration Method
1. Edit the mod configuration file: `config/westerosblocks.json5`
2. Set `"dumpWorldpainterCSV": true`
3. Start your Minecraft world
4. The CSV file will be automatically generated at: `config/westerosblocks_worldpainter.csv`
5. Set the config back to `false` to avoid regenerating the file every time

## Using the CSV File in WorldPainter

1. Open WorldPainter
2. Go to **View → Preferences**
3. Navigate to the **Minecraft** tab
4. Click **Configure...** next to "Custom blocks"
5. Click **Import...** and select your `westerosblocks_worldpainter.csv` file
6. WorldPainter will now recognize all WesterosBlocks when painting your world

## CSV File Format

The exported CSV follows WorldPainter's specification with these columns:
- `name` - Block identifier (e.g., `westerosblocks:arbor_brick`)
- `discriminator` - Used for blocks with multiple variants
- `properties` - Block state properties (e.g., `facing:e[north;south;west;east]`)
- `opacity` - Light opacity (0-15)
- `receivesLight` - Whether block receives light updates
- `insubstantial` - Whether block is non-solid
- `resource` - Whether block is a resource
- `tileEntity` - Whether block has tile entity data
- `tileEntityId` - Tile entity identifier
- `treeRelated` - Whether block is part of trees
- `vegetation` - Whether block is vegetation
- `blockLight` - Light emission level (0-15)
- `natural` - Whether block generates naturally
- `watery` - Whether block is water-related
- `colour` - Block color in hex format (ARGB)

## Block Properties

The export automatically detects and includes:
- **Block states** and their valid values
- **Light emission** levels for glowing blocks
- **Vegetation classification** for plants and organic blocks
- **Tree classification** for wood-related blocks
- **Water classification** for liquid blocks
- **Opacity values** for lighting calculations

## Troubleshooting

- **File not generated**: Make sure the config option is set to `true` and restart Minecraft
- **WorldPainter import errors**: Ensure you're using a recent version of WorldPainter that supports custom blocks
- **Missing blocks**: The CSV includes all registered WesterosBlocks - if a block is missing, it may not be properly registered in the mod

## Technical Details

The CSV export is handled by `WesterosBlocksCompatibility.dumpWorldPainterConfig()` and includes comprehensive block data analysis to ensure maximum compatibility with WorldPainter's block painting system. 