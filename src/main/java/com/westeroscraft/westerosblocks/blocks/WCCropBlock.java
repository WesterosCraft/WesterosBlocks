package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;

import net.minecraft.block.Block;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCropBlock extends WCPlantBlock implements WesterosBlockDynmapSupport {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            return new Block[] { new WCCropBlock(def) };
        }
    }

    protected WCCropBlock(WesterosBlockDef def) {
        super(def);
    }

    @Override
    public int getRenderType() {
        return 6;   // Just switch to crop renderer
    }
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        WesterosBlockDef def = this.getWBDefinition();
        int blkid = Block.getIdFromBlock(this);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 4);
        PatchBlockModel mod = md.addPatchModel(blkid);
        mod.addPatch(0.75, 0.0, 1.0, 0.75, 0.0, 0.0, 0.75, 1.0, 1.0);
        mod.addPatch(0.25, 0.0, 1.0, 0.25, 0.0, 0.0, 0.25, 1.0, 1.0);
        mod.addPatch(1.0, 0.0, 0.75, 0.0, 0.0, 0.75, 1.0, 1.0, 0.75);
        mod.addPatch(1.0, 0.0, 0.25, 0.0, 0.0, 0.25, 1.0, 1.0, 0.25);
    }

}
