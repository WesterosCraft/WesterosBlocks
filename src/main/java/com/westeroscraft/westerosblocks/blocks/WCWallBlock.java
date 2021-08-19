package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.WallBlock;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.WallFenceBlockModel;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCWallBlock extends WallBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCWallBlock(props, def)), false, false);
        }
    }
    private WesterosBlockDef def;
    public static enum WallSize {
    	NORMAL,	// 14/16 high wall
    	SHORT	// 13/16 high wall
    };
    public final WallSize wallSize;	// "normal", or "short"
    protected WCWallBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String height = def.getTypeValue("size", "normal");
        if (height.equals("short")) { 
        	wallSize = WallSize.SHORT;
        }
        else {
        	wallSize = WallSize.NORMAL;
        }
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
        def.registerPatchTextureBlock(mtd, 3);
        // Get plant model, and set for all defined meta
        WallFenceBlockModel pbm = md.addWallFenceModel(blkname, WallFenceBlockModel.FenceType.WALL);
    }
    private static String[] TAGS = { "walls" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
