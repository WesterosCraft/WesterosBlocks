package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;

public class WCSlabBlock extends SlabBlock implements WesterosBlockDynmapSupport, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSlabBlock(props, def)), false, false);
        }
    }
    protected WesterosBlockDef def;
    
    protected WCSlabBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        
        /* Add models for half slabs */
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextureBlock(mtd, 0, TransparencyMode.SEMITRANSPARENT, 0, 2);
        def.defaultRegisterTextureBlock(mtd, 0, TransparencyMode.OPAQUE, 2, 1);
        BoxBlockModel bottom = md.addBoxModel(blkname);
        bottom.setYRange(0.0, 0.5);
        BoxBlockModel top = md.addBoxModel(blkname);
        top.setYRange(0.5, 1.0);
        bottom.setMetaValue(0);
        top.setMetaValue(1);
    }
    private static String[] TAGS = { "slabs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
