package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WCFlowerPotBlock extends FlowerPotBlock {
    private static final Logger LOGGER = LoggerFactory.getLogger(WCFlowerPotBlock.class);

    public WCFlowerPotBlock(Block content, AbstractBlock.Settings settings) {
        super(content, settings);
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            // Handle null definition (from BlockBuilder) with sensible defaults
            Block content = Blocks.AIR; // Default to empty pot

            if (definition != null) {
                // For now, we can't resolve plant blocks from BlockDefinition since it doesn't contain Block references
                // This would need to be handled by the JSON def system when it's implemented
                // TODO: Add support for plant resolution when JSON def system is ready
                String plantId = definition.getBlockName(); // Could use blockName as plant reference

                if (plantId != null && !plantId.isEmpty()) {
                    try {
                        Block plant = Registries.BLOCK.get(Identifier.of(plantId));
                        if (plant != Blocks.AIR) {
                            content = plant;
                            LOGGER.debug("Created flower pot with plant '{}'", plantId);
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Failed to resolve plant ID '{}': {}", plantId, e.getMessage());
                    }
                }
            }

            return new WCFlowerPotBlock(content, settings);
        }
    }
}
