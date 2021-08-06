package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCRailBlock extends RailBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties().noOcclusion();
        	return def.registerRenderType(def.registerBlock(new WCRailBlock(props, def)), false, false);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCRailBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
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
        String blkname = def.getBlockName();
        def.defaultRegisterTextures(mtd);
        WesterosBlockDef def = this.getWBDefinition();
        // Register textures 
        TextureModifier tmod = TextureModifier.NONE;
        if (def.nonOpaque) {
            tmod = TextureModifier.CLEARINSIDE;
        }
        // Make record for straight tracks
        BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
        mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
        // Set for all meta values for straight tracks
        for (int meta = 0; meta < 6; meta++) {
            mtr.setMetaValue(meta);
        }
        String txtid = def.getTextureByIndex(0);
        mtr.setPatchTexture(txtid.replace(':', '_'), tmod, 0);
        // Make record for curved tracks
        mtr = mtd.addBlockTextureRecord(blkname);
        mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
        // Set for all meta values for curved tracks
        for (int meta = 6; meta < 10; meta++) {
            mtr.setMetaValue(meta);
        }
        txtid = def.getTextureByIndex(1);
        mtr.setPatchTexture(txtid.replace(':', '_'), tmod, 0);
        def.setBlockColorMap(mtr);
        // Make models for flat tracks
        PatchBlockModel mod = md.addPatchModel(blkname);
        String patchFlat = mod.addPatch(0.0, 0.01, 0.0, 1.0, 0.01, 0.0, 0.0, 0.01, 1.0, SideVisible.BOTH);
        mod.setMetaValue(0);
        mod.setMetaValue(9);
        PatchBlockModel mod90 = md.addPatchModel(blkname);
        mod90.addRotatedPatch(patchFlat, 0, 90, 0);
        mod90.setMetaValue(1);
        mod90.setMetaValue(6);
        PatchBlockModel mod180 = md.addPatchModel(blkname);
        mod180.addRotatedPatch(patchFlat, 0, 180, 0);
        mod180.setMetaValue(7);
        PatchBlockModel mod270 = md.addPatchModel(blkname);
        mod270.addRotatedPatch(patchFlat, 0, 270, 0);
        mod270.setMetaValue(8);
        // Make models for sloped tracks
        PatchBlockModel modS0 = md.addPatchModel(blkname);
        String patchSlope = mod.addPatch(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, SideVisible.BOTH);
        modS0.setMetaValue(2);
        PatchBlockModel modS90 = md.addPatchModel(blkname);
        modS90.addRotatedPatch(patchSlope, 0, 90, 0);
        modS90.setMetaValue(5);
        PatchBlockModel modS180 = md.addPatchModel(blkname);
        modS180.addRotatedPatch(patchSlope, 0, 180, 0);
        modS180.setMetaValue(3);
        PatchBlockModel modS270 = md.addPatchModel(blkname);
        modS270.addRotatedPatch(patchSlope, 0, 270, 0);
        modS270.setMetaValue(4);
    }
    private static String[] TAGS = { "rails" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
