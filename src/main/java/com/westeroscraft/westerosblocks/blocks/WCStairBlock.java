package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.block.StairsBlock;
import org.dynmap.modsupport.CopyBlockTextureRecord;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class WCStairBlock extends StairsBlock implements WesterosBlockDynmapSupport, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            if (def.modelBlockName == null) {
                WesterosBlocks.log.error("Type 'stair' requires modelBlockName settings");
                return null;
            }
            // Try to find model block
            Block blk = WesterosBlocks.findBlockByName(def.modelBlockName);
            if (blk == null) {
                WesterosBlocks.log.error(String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
                return null;
            }
        	AbstractBlock.Properties props = def.makeAndCopyProperties(blk);
        	return def.registerBlock(new WCStairBlock(blk.defaultBlockState(), props, def));
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCStairBlock(BlockState modelstate, AbstractBlock.Properties props, WesterosBlockDef def) {
        super(() -> modelstate, props);
        this.def = def;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        String modblkname = def.modelBlockName;
        // Make copy of model block textu def
        CopyBlockTextureRecord btr = mtd.addCopyBlockTextureRecord(blkname, modblkname, 0);
        btr.setTransparencyMode(TransparencyMode.SEMITRANSPARENT);
        // Get stair model
        md.addStairModel(blkname);
    }
}
