package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class WCFlowerPotBlock extends FlowerPotBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            Block content = Blocks.AIR;
            String emptyPotID = "minecraft:flower_pot";
            String plantBlockID = null;

            if (def.getType() != null) {
                String[] toks = def.getType().split(",");
                for (String tok : toks) {
                    if (tok.startsWith("pot-id:")) {
                        emptyPotID = tok.substring(tok.indexOf(':') + 1).trim();
                    }
                    if (tok.equals("empty-pot")) {
                        emptyPotID = null;
                    }
                    if (tok.startsWith("plant-id:")) {
                        plantBlockID = tok.substring(tok.indexOf(':') + 1).trim();
                    }
                }
            }

            // Get empty pot block
            if (emptyPotID != null) {
                Block potBlock = ModBlocks.findBlockByName(emptyPotID, "minecraft");
                if (!(potBlock instanceof FlowerPotBlock) || potBlock == Blocks.AIR) {
                    WesterosBlocks.LOGGER.error("emptyPotID '{}' not found or invalid for block '{}'",
                            emptyPotID, def.blockName);
                    return null;
                }

                // Get plant block if specified
                if (plantBlockID != null) {
                    Block plant = ModBlocks.findBlockByName(plantBlockID, "minecraft");
                    if (plant == null || plant == Blocks.AIR) {
                        WesterosBlocks.LOGGER.error("plantBlockID '{}' not found for block '{}'",
                                plantBlockID, def.blockName);
                        return null;
                    }
                    content = plant;
                }
            }

            // Create and register the flower pot block
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCFlowerPotBlock(content, settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, def.nonOpaque);
        }
    }

    protected final WesterosBlockDef def;

    protected WCFlowerPotBlock(Block content, AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(content, settings);
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static final String[] TAGS = {"flower_pots"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}