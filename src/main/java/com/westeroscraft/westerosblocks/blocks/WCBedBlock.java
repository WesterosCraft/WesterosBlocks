package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import javax.annotation.Nullable;


public class WCBedBlock extends BedBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	AbstractBlock.Properties props = def.makeProperties().noOcclusion();
        	return def.registerRenderType(def.registerBlock(new WCBedBlock(props, def)), false, false);
        }        
    }
    
    private WesterosBlockDef def;
    
    protected WCBedBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(DyeColor.RED, props);
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        WesterosBlockDef def = this.getWBDefinition();
        String blkname = def.getBlockName();
        if (def.textures != null) {
            // Register textures 
            TextureModifier tmod = TextureModifier.NONE;
            if (def.nonOpaque) {
                tmod = TextureModifier.CLEARINSIDE;
            }
            BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
            // Set for all meta values for foot
            for (int meta = 0; meta < 8; meta++) {
                mtr.setMetaValue(meta);
            }
            int[] face_to_idx = new int[] { 1, 1, 3, 3, 5, 5 };
            for (int face = 0; face < 6; face++) {
                int fidx = face_to_idx[face];
                if (fidx >= def.textures.size()) {
                    fidx = def.textures.size() - 1;
                }
                String txtid = def.textures.get(fidx);
                mtr.setPatchTexture(txtid.replace(':', '_'), tmod, face);
            }
            def.setBlockColorMap(mtr);
            // Set for head
            mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
            // Set for all meta values for head
            for (int meta = 8; meta < 16; meta++) {
                mtr.setMetaValue(meta);
            }
            face_to_idx = new int[] { 0, 0, 2, 2, 4, 4 };
            for (int face = 0; face < 6; face++) {
                int fidx = face_to_idx[face];
                if (fidx >= def.textures.size()) {
                    fidx = def.textures.size() - 1;
                }
                String txtid = def.textures.get(fidx);
                mtr.setPatchTexture(txtid.replace(':', '_'), tmod, face);
            }
            def.setBlockColorMap(mtr);
        }
        // Create east facing model
        PatchBlockModel mod = md.addPatchModel(blkname);
        mod.addPatch(0, 0.1875, 0, 1, 0.1875, 0, 0, 0.1875, 1, SideVisible.TOP); // Bottom
        mod.addPatch(0, 0.5625, 1, 1, 0.5625, 1, 0, 0.5625, 0, SideVisible.TOP); // Top
        mod.addPatch(0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0.5625, 100, SideVisible.BOTTOM); // Z- (flip)
        mod.addPatch(0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0.5625, 100, SideVisible.TOP); // Z+
        mod.addPatch(0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0.5625, 100, SideVisible.TOP); // X-
        mod.addPatch(1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0.5625, 100, SideVisible.TOP); // X+
        mod.setMetaValue(3);
        mod.setMetaValue(7);
        mod.setMetaValue(11);
        mod.setMetaValue(15);
        // Make north facing model
        PatchBlockModel nmod = md.addRotatedPatchModel(blkname, mod, 0, 270, 0);
        nmod.setMetaValue(2);
        nmod.setMetaValue(6);
        nmod.setMetaValue(10);
        nmod.setMetaValue(14);
        // Make south facing model
        PatchBlockModel smod = md.addRotatedPatchModel(blkname, mod, 0, 90, 0);
        smod.setMetaValue(0);
        smod.setMetaValue(4);
        smod.setMetaValue(8);
        smod.setMetaValue(12);
        // Make west facing model
        PatchBlockModel wmod = md.addRotatedPatchModel(blkname, mod, 0, 180, 0);
        wmod.setMetaValue(1);
        wmod.setMetaValue(5);
        wmod.setMetaValue(9);
        wmod.setMetaValue(13);
    }
    
    @Override
    // Force to model for now
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Override
    public TileEntity newBlockEntity(IBlockReader reader) {
        return null;
     }

    private static String[] TAGS = { "beds" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
