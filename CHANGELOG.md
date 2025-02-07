## Changelog

Initial Fabric Port: Feb 2025

- "WesterosBlocks.json" is now split among different files in the `resources/definitions` folders. Blocksets and block
  definition files are now separate
- Any methods that loaded these json files is in the WesterosBlocksDefLoader class
- Blocks, BlockEntities, Tags, ColorHandlers, Sounds, Particles, Items have their own registry system and classes now
- Door models, and thus halfdoor models changed slightly
- The old model export code is converted to use the Fabric Datagen API
- Language files converted to use Datagen API, tooltips too
- Tags use new Datagen API
- New wildfire block
- Added custom wildfire particle
- You can now add custom tooltips for each block in their def file -
  ex: "tooltips": ["This is the first line of the tooltip!", "And the second"]
- Old material system no longer exists - replaced with ModBlockSettings.java system
- WesterosBlocksCompatibility class is in charge of dumping other files for use with 3rd parties tools, ex: WorldPainter
- New config options for dumping blockset and worldpainter.csv options
- Cloth Config and ModMenu integration