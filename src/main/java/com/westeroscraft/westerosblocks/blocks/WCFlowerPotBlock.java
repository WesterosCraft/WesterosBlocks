package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WCFlowerPotBlock extends FlowerPotBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	String emptyPotID = "minecraft:flower_pot";
        	String plantBlockID = null;
        	
        	if (def.type != null) {
        		String[] toks = def.type.split(",");
        		for (String tok : toks) {
        			if (tok.startsWith("pot-id:")) {
        				emptyPotID = tok.substring(tok.indexOf(':')+1).trim();
        			}
        			if (tok.equals("empty-pot")) {
        				emptyPotID = null;
        			}
        			if (tok.startsWith("plant-id:")) {
        				plantBlockID = tok.substring(tok.indexOf(':')+1).trim();
        			}
        		}
        	}
        	//WesterosBlocks.log.info(String.format("pot-id=%s, plant-id=%s", emptyPotID, plantBlockID));
        	
        	Supplier<FlowerPotBlock> emptyPot = null;
        	Supplier<Block> plant = () -> null;
            if (emptyPotID != null) {
				emptyPotID = "flower_pot";
            	FlowerPotBlock emptyPotBlk = (FlowerPotBlock) WesterosBlocks.findBlockByName(emptyPotID, "minecraft");
            	if ((emptyPotBlk == null) || (emptyPotBlk == Blocks.AIR)) {
                    WesterosBlocks.log.error(String.format("emptyPotID '%s' not found for block '%s'",
                            emptyPotID, def.blockName));
                    return null;
            	}
            	//WesterosBlocks.log.info(String.format("emptyPotBlk=%s", emptyPotBlk.getRegistryName()));
            	emptyPot = () -> emptyPotBlk;
            	if (plantBlockID != null) {
            		Block plantBlk = WesterosBlocks.findBlockByName(plantBlockID, "minecraft");
            		if ((plantBlk == null) || (plantBlk == Blocks.AIR)) {
                        WesterosBlocks.log.error(String.format("plantBlockID '%s' not found for block '%s'",
                        		plantBlockID, def.blockName));
                        return null;            		            			
            		}
                	//WesterosBlocks.log.info(String.format("plantBlk=%s", plantBlk.getRegistryName()));
            		plant = () -> plantBlk;
            	}
            }                        
        	return def.registerRenderType(def.registerBlock(new WCFlowerPotBlock(emptyPot, plant, props, def)), false, def.nonOpaque);
        }
    }    
    protected WesterosBlockDef def;
    
    protected WCFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<Block> plant, BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(emptyPot, plant, props);
        this.def = def;
        Block pl = plant.get();
        if ((pl != null) && (emptyPot != null) && (emptyPot.get() != null)) {
        	//WesterosBlocks.log.info(String.format("addPlant=%s to %s", pl.getRegistryName(), emptyPot.get().getRegistryName()));
        	emptyPot.get().addPlant(BuiltInRegistries.BLOCK.getKey(pl), () -> this);
        }
    }
    
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static String[] TAGS = { "flower_pots" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
}
