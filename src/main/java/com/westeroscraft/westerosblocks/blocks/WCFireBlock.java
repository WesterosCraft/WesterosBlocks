package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;


public class WCFireBlock extends FireBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport
{
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties().noCollission().instabreak(); //.randomTicks();
        	return def.registerRenderType(def.registerBlock(new WCFireBlock(props, def)), false, false);
        }
    }
    private WesterosBlockDef def;

    protected WCFireBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        String blkname = def.getBlockName();
        // Register textures
        String txt1 = def.getTextureByIndex(0);
        String txt2 = def.getTextureByIndex(1);
        BlockTextureRecord btr = mtd.addBlockTextureRecord(blkname);
        btr.setTransparencyMode(TransparencyMode.TRANSPARENT);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 0);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 1);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 2);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 3);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 4);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 5);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 6);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 7);
        def.setBlockColorMap(btr);
        /* Make base model */
        PatchBlockModel mod = md.addPatchModel(blkname);
        // patchblock:id=51,data=*,patch0=VertX0,patch1=VertX0@90,patch2=VertX0@180,patch3=VertX0@270,patch4=SlopeXUpZTop675,patch5=SlopeXUpZTop675@90,patch6=SlopeXUpZTop675@180,patch4=SlopeXUpZTop675@270
        // patch:id=VertX0,Ox=0.0,Oy=0.0,Oz=1.0,Ux=0.0,Uy=0.0,Uz=0.0,Vx=0.0,Vy=1.0,Vz=1.0,visibility=bottom
        // patch:id=SlopeXUpZTop675,Ox=0.375,Oy=0.0,Oz=0.0,Ux=0.375,Uy=0.0,Uz=1.0,Vx=0.5,Vy=1.0,Vz=0.0,visibility=top
        String patch0 = mod.addPatch(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, SideVisible.BOTH);
        mod.addRotatedPatch(patch0, 0, 90, 0);
        mod.addRotatedPatch(patch0, 0, 180, 0);
        mod.addRotatedPatch(patch0, 0, 270, 0);
        String patch4 = mod.addPatch(0.375, 0.0, 0.0, 0.375, 0.0, 1.0, 0.5, 1.0, 0.0, SideVisible.BOTH);
        mod.addRotatedPatch(patch4, 0, 90, 0);
        mod.addRotatedPatch(patch4, 0, 180, 0);
        mod.addRotatedPatch(patch4, 0, 270, 0);
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
    	return true;
    }
    
    @Override
    public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
    }

    @Override
    public boolean canCatchFire(IBlockReader world, BlockPos pos, Direction face) {
    	return false;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    private static String[] TAGS = { "fire" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
