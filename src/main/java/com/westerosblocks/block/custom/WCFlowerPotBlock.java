package com.westerosblocks.block.custom;

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
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];

                Block plant = (Block) paramMap.get("plant");
                String plantId = (String) paramMap.get("plantId");

                // Resolve plant block by ID if not directly provided
                if (plant == null && plantId != null) {
                    try {
                        plant = Registries.BLOCK.get(Identifier.of(plantId));
                        if (plant == Blocks.AIR) {
                            LOGGER.error("Plant ID '{}' not found", plantId);
                            return null;
                        }
                    } catch (Exception e) {
                        LOGGER.error("Failed to resolve plant ID '{}': {}", plantId, e.getMessage());
                        return null;
                    }
                }

                // Use the resolved plant or default to AIR for empty pots
                Block content = plant != null ? plant : Blocks.AIR;

                // The FlowerPotBlock constructor automatically registers content to the CONTENT_TO_POTTED map
                WCFlowerPotBlock flowerPot = new WCFlowerPotBlock(content, settings);

                if (plant != null) {
                    LOGGER.debug("Created flower pot with plant '{}'", Registries.BLOCK.getId(plant));
                }

                return flowerPot;
            }

            // Fallback for legacy parameter style
            Block content = params.length > 0 && params[0] instanceof Block ? (Block) params[0] : Blocks.AIR;
            return new WCFlowerPotBlock(content, settings);
        }
    }
}
