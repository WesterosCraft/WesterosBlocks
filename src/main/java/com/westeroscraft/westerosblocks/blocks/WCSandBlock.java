package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCSandBlock extends SandBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSandBlock(props, def)), true, false);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCSandBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(14406560, props);	// TODO: configurable dust color
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd);
    }
    
    private static String[] TAGS = { "sand" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
