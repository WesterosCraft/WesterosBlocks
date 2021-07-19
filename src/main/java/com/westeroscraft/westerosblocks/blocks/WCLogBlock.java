package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.BlockSide;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCLogBlock extends RotatedPillarBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCLogBlock(props, def)), false, false);
        }
    }
    protected WesterosBlockDef def;
    
    protected WCLogBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        String blkname = def.getBlockName(0);
        // Modifiers for each orientation
        TextureModifier tmod[][] = { 
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270 }
        };
        // Texture index for each orientation
        int txtid[][] = {
            { 0, 0, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 0, 0 },
            { 1, 1, 0, 0, 1, 1 }
        };

        for (int i = 0; i < 3; i++) {   // 3 orientations
            BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setMetaValue(i);
            for (int face = 0; face < 6; face++) {
                int fidx = txtid[i][face];
                if (fidx >= def.textures.size()) {
                    fidx = def.textures.size() - 1;
                }
                String txt = def.textures.get(fidx);
                mtr.setSideTexture(txt.replace(':', '_'), tmod[i][face], BlockSide.valueOf("FACE_" + face));
            }
            def.setBlockColorMap(mtr);
        }
    }
    private static String[] TAGS = { "logs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
