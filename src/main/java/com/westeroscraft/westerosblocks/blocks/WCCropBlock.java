package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

public class WCCropBlock extends WCPlantBlock implements WesterosBlockDynmapSupport {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            WCPlantBlock.new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCCropBlock(def) };
        }
    }

    protected WCCropBlock(WesterosBlockDef def) {
        super(def);
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

    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }
}
