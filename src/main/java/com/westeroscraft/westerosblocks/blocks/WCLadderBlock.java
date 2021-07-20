package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCLadderBlock extends LadderBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties().noOcclusion();
        	return def.registerRenderType(def.registerBlock(new WCLadderBlock(props, def)), false, false);
        }
    }

    private WesterosBlockDef def;
    private boolean allow_unsupported;
    private boolean no_climb;

    protected WCLadderBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                if (tok.equals("no-climb")) {
                	no_climb = true;
                }
            }
        }
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 1);
        /* Make base model */
        PatchBlockModel mod = md.addPatchModel(blkname);
        String patch0 = mod.addPatch(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, SideVisible.BOTH);
        /* Make rotated models */
        PatchBlockModel mod90 = md.addPatchModel(blkname);
        mod90.addRotatedPatch(patch0, 0, 90, 0);
        PatchBlockModel mod180 = md.addPatchModel(blkname);
        mod180.addRotatedPatch(patch0, 0, 180, 0);
        PatchBlockModel mod270 = md.addPatchModel(blkname);
        mod270.addRotatedPatch(patch0, 0, 270, 0);
        
        mod.setMetaValue(0);
        mod90.setMetaValue(1);
        mod180.setMetaValue(2);
        mod270.setMetaValue(3);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return allow_unsupported || super.canSurvive(state, world, pos);
     }
    private static String[] TAGS = { "climbable" };
    private static String[] TAGS_NOCLIMB = {  };
    @Override
    public String[] getBlockTags() {
    	if (no_climb) return TAGS_NOCLIMB;
    	return TAGS;
    }    

}
