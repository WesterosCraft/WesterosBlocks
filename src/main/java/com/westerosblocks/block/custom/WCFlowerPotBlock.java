//package com.westerosblocks.block.custom;
//
//import com.google.common.base.Supplier;
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockFactory;
//import com.westerosblocks.block.WesterosBlockLifecycle;
//import net.minecraft.block.AbstractBlock;
//import net.minecraft.block.Block;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.FlowerPotBlock;
//import net.minecraft.registry.Registries;
//
//public class WCFlowerPotBlock extends FlowerPotBlock implements WesterosBlockLifecycle {
//
//    public static class Factory extends WesterosBlockFactory {
//        @Override
//        public Block buildBlockClass(WesterosBlockDef def) {
//			AbstractBlock.Settings settings = def.makeBlockSettings();
//        	String emptyPotID = "minecraft:flower_pot";
//        	String plantBlockID = null;
//
//        	if (def.type != null) {
//        		String[] toks = def.type.split(",");
//        		for (String tok : toks) {
//        			if (tok.startsWith("pot-id:")) {
//        				emptyPotID = tok.substring(tok.indexOf(':')+1).trim();
//        			}
//        			if (tok.equals("empty-pot")) {
//        				emptyPotID = null;
//        			}
//        			if (tok.startsWith("plant-id:")) {
//        				plantBlockID = tok.substring(tok.indexOf(':')+1).trim();
//        			}
//        		}
//        	}
//        	//WesterosBlocks.log.info(String.format("pot-id=%s, plant-id=%s", emptyPotID, plantBlockID));
//
//        	Supplier<FlowerPotBlock> emptyPot = null;
//        	Supplier<Block> plant = () -> null;
//            if (emptyPotID != null) {
//				emptyPotID = "flower_pot";
//            	FlowerPotBlock emptyPotBlk = (FlowerPotBlock) WesterosBlocks.findBlockByName(emptyPotID, "minecraft");
//            	if ((emptyPotBlk == null) || (emptyPotBlk == Blocks.AIR)) {
//                    WesterosBlocks.LOGGER.error(String.format("emptyPotID '%s' not found for block '%s'",
//                            emptyPotID, def.blockName));
//                    return null;
//            	}
//            	//WesterosBlocks.log.info(String.format("emptyPotBlk=%s", emptyPotBlk.getRegistryName()));
//            	emptyPot = () -> emptyPotBlk;
//            	if (plantBlockID != null) {
//            		Block plantBlk = WesterosBlocks.findBlockByName(plantBlockID, "minecraft");
//            		if ((plantBlk == null) || (plantBlk == Blocks.AIR)) {
//                        WesterosBlocks.LOGGER.error(String.format("plantBlockID '%s' not found for block '%s'",
//                        		plantBlockID, def.blockName));
//                        return null;
//            		}
//                	//WesterosBlocks.log.info(String.format("plantBlk=%s", plantBlk.getRegistryName()));
//            		plant = () -> plantBlk;
//            	}
//            }
//			Block blk = new WCFlowerPotBlock(emptyPot, plant, settings, def);
//			return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, def.nonOpaque);
//        }
//    }
//    protected WesterosBlockDef def;
//
//    protected WCFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<Block> plant, AbstractBlock.Settings settings, WesterosBlockDef def) {
//        super(emptyPot, plant, settings);
//        this.def = def;
//        Block pl = plant.get();
//        if (plant != null && emptyPot != null) {
//        	//WesterosBlocks.log.info(String.format("addPlant=%s to %s", pl.getRegistryName(), emptyPot.get().getRegistryName()));
//        	emptyPot.get().addPlant(Registries.BLOCK.getId(plant), () -> this);
//        }
//    }
//
////	protected WCFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<Block> plant, AbstractBlock.Settings settings, WesterosBlockDef def) {
////		super(emptyPot, plant, settings);
////		this.def = def;
////		Block pl = plant.get();
////		if ((pl != null) && (emptyPot != null) && (emptyPot.get() != null)) {
////			//WesterosBlocks.log.info(String.format("addPlant=%s to %s", pl.getRegistryName(), emptyPot.get().getRegistryName()));
////			emptyPot.get().addPlant(Registries.BLOCK.getId(pl), () -> this);
////		}
////	}
//
//    @Override
//    public WesterosBlockDef getWBDefinition() {
//        return def;
//    }
//
//    private static String[] TAGS = { "flower_pots" };
//    @Override
//    public String[] getBlockTags() {
//    	return TAGS;
//    }
//}
