package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.Block;
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
        				emptyPotID = tok.substring(tok.indexOf('=')+1).trim();
        			}
        			if (tok.equals("empty-pot")) {
        				emptyPotID = null;
        			}
        			if (tok.startsWith("plant-id:")) {
        				plantBlockID = tok.substring(tok.indexOf('=')+1).trim();
        			}
        		}
        	}
        	FlowerPotBlock emptyPotBlk = null;
            Block plantBlk = null;
            if (emptyPotID != null) {
            	emptyPotBlk = (FlowerPotBlock) WesterosBlocks.findBlockByName(emptyPotID);
            	if (emptyPotBlk == null) {
                    WesterosBlocks.log.error(String.format("emptyPotID '%s' not found for block '%s'",
                            emptyPotID, def.blockName));
                    return null;            		
            	}
            	if (plantBlockID != null) {
            		plantBlk = WesterosBlocks.findBlockByName(plantBlockID);
            		if (plantBlk == null) {
                        WesterosBlocks.log.error(String.format("plantBlockID '%s' not found for block '%s'",
                        		plantBlockID, def.blockName));
                        return null;            		            			
            		}
            	}
            }            
        	return def.registerRenderType(def.registerBlock(new WCFlowerPotBlock(emptyPotBlk, plantBlk, props, def)), false, def.nonOpaque);
        }
    }    
    protected WesterosBlockDef def;
    
    protected WCFlowerPotBlock(FlowerPotBlock emptyPotBlock, Block plantBlock, BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(() -> emptyPotBlock, () -> plantBlock, props);
        this.def = def;
    }
    
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
}
