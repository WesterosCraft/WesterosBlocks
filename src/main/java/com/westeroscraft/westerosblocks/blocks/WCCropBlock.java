package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

public class WCCropBlock extends WCPlantBlock implements WesterosBlockDynmapSupport, WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties().noCollission().instabreak();
        	return def.registerRenderType(def.registerBlock(new WCCropBlock(props, def)), false, false);
        }
    }

    protected WCCropBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props, def);
    }
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        WesterosBlockDef def = this.getWBDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 4);
        PatchBlockModel mod = md.addPatchModel(blkname);
        mod.addPatch(0.75, 0.0, 1.0, 0.75, 0.0, 0.0, 0.75, 1.0, 1.0);
        mod.addPatch(0.25, 0.0, 1.0, 0.25, 0.0, 0.0, 0.25, 1.0, 1.0);
        mod.addPatch(1.0, 0.0, 0.75, 0.0, 0.0, 0.75, 1.0, 1.0, 0.75);
        mod.addPatch(1.0, 0.0, 0.25, 0.0, 0.0, 0.25, 1.0, 1.0, 0.25);
    }
    private static String[] TAGS = { "crops" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
