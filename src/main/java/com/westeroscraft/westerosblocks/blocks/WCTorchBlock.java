package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.particles.ParticleTypes;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCTorchBlock extends TorchBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties floorprops = def.makeProperties().noCollission().instabreak();
        	Block floorblock = new WCTorchBlock(floorprops, def);
        	AbstractBlock.Properties wallprops = def.makeProperties().noCollission().instabreak().dropsLike(floorblock);
        	Block wallblock = new WCWallTorchBlock(wallprops, def);
        	def.registerWallOrFloorBlock(floorblock, wallblock);
        	
        	def.registerRenderType(floorblock, false, false);
        	def.registerRenderType(wallblock, false, false);
        	return floorblock;
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCTorchBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props, ParticleTypes.FLAME);
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
        def.registerPatchTextureBlock(mtd, 5);

        // Make vertical model
        PatchBlockModel mod0 = md.addPatchModel(blkname);
        String vside = mod0.addPatch(0.4375, 0.0, 0.0, 0.4375, 0.0, 1.0, 0.4375, 1.0, 0.0, SideVisible.TOP);
        mod0.addRotatedPatch(vside, 0, 90, 0);
        mod0.addRotatedPatch(vside, 0, 180, 0);
        mod0.addRotatedPatch(vside, 0, 270, 0);
        mod0.addPatch(0.0, 0.625, -0.0625, 1.0, 0.625, -0.0625, 0.0, 0.625, 0.9375, 0.4375, 0.5625, 0.5, 0.625, 100.0, SideVisible.BOTH);
        
        
        // And handle wall blocks
        md = mtd.getModelDefinition();
        blkname = "wall_" + def.getBlockName();
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 5);

        // Make side model
        PatchBlockModel mod1 = md.addPatchModel(blkname);
        mod1.addPatch(-0.5, 0.2, 0.4375, 0.5, 0.2, 0.4375, -0.1, 1.2, 0.4375, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.BOTTOM);
        mod1.addPatch(-0.5, 0.2, 0.5625, 0.5, 0.2, 0.5625, -0.1, 1.2, 0.5625, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.TOP);
        mod1.addPatch(0.0625, 0.2, 0.0, 0.0625, 0.2, 1.0, 0.4625, 1.2, 0.0, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.BOTTOM);
        mod1.addPatch(-0.0625, 0.2, 0.0, -0.0625, 0.2, 1.0, 0.3375, 1.2, 0.0, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.TOP);
        mod1.addPatch(0.0, 0.825, -0.3625, 1.0, 0.825, -0.3625, 0.0, 0.825, 0.6375, 0.4375, 0.5625, 0.5, 0.625, 100.0, SideVisible.BOTH);
        mod1.setMetaValue(0);
        // Rotate for other sides
        PatchBlockModel mod2 = md.addRotatedPatchModel(blkname, mod1, 0, 180, 0);
        mod2.setMetaValue(1);
        PatchBlockModel mod3 = md.addRotatedPatchModel(blkname, mod1, 0, 90, 0);
        mod3.setMetaValue(2);
        PatchBlockModel mod4 = md.addRotatedPatchModel(blkname, mod1, 0, 270, 0);
        mod4.setMetaValue(3);    
    }
    private static String[] TAGS = { "wall_post_override" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
