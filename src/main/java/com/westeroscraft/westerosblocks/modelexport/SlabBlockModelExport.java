package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.blocks.WCSlabBlock;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;

import net.minecraft.block.Block;

public class SlabBlockModelExport extends ModelExport {
    private WCSlabBlock blk;
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCube {
        public String parent = "block/cube";    // Use 'cube' model for multiple textures
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String down, up, north, south, west, east, particle;
    }
    public static class ModelObjectHalfLower {
        public String parent = "block/half_slab";    // Use 'half_slab' model for multiple textures
        public TextureSlab textures = new TextureSlab();
    }
    public static class ModelObjectHalfUpper {
        public String parent = "block/upper_slab";    // Use 'upper_slab' model for multiple textures
        public TextureSlab textures = new TextureSlab();
    }
    public static class TextureSlab {
        public String bottom, top, side;
    }
    public static class ModelObject {
    	public String parent;
    }

    
    public SlabBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.blk = (WCSlabBlock) blk;
        this.def = def;
        for (Subblock sb : def.subBlocks) {
            addNLSString(this.blk.getUnlocalizedName(sb.meta) + ".name", sb.label);
        }
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Do state for half block
        String bn = def.getBlockName(0);
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + bn + "_" + sb.meta + "_top";
            so.variants.put(String.format("half=top,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + bn + "_" + sb.meta + "_bottom";
            so.variants.put(String.format("half=bottom,variant=%d", sb.meta), var);
        }
        this.writeBlockStateFile(bn, so);
        // Do state for full block
        so = new StateObject();
        bn = def.getBlockName(1);
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + bn + "_" + sb.meta;
            so.variants.put(String.format("variant=%d", sb.meta), var);
        }
        this.writeBlockStateFile(bn, so);
    }

    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
        	// Double block model
            ModelObjectCube mod = new ModelObjectCube();
            mod.textures.down = getTextureID(sb.getTextureByIndex(0));
            mod.textures.up = getTextureID(sb.getTextureByIndex(1));
            mod.textures.north = getTextureID(sb.getTextureByIndex(2));
            mod.textures.south = getTextureID(sb.getTextureByIndex(3));
            mod.textures.west = getTextureID(sb.getTextureByIndex(4));
            mod.textures.east = getTextureID(sb.getTextureByIndex(5));
            mod.textures.particle = getTextureID(sb.getTextureByIndex(3));
            this.writeBlockModelFile(def.getBlockName(1) + "_" + sb.meta, mod);
            // Lower half block model
            ModelObjectHalfLower modl = new ModelObjectHalfLower();
            modl.textures.bottom = getTextureID(sb.getTextureByIndex(0));
            modl.textures.top = getTextureID(sb.getTextureByIndex(1));
            modl.textures.side = getTextureID(sb.getTextureByIndex(2));
            this.writeBlockModelFile(def.getBlockName(0) + "_" + sb.meta + "_bottom", modl);
            // Upper half block model
            ModelObjectHalfUpper modu = new ModelObjectHalfUpper();
            modu.textures.bottom = getTextureID(sb.getTextureByIndex(0));
            modu.textures.top = getTextureID(sb.getTextureByIndex(1));
            modu.textures.side = getTextureID(sb.getTextureByIndex(2));
            this.writeBlockModelFile(def.getBlockName(0) + "_" + sb.meta + "_top", modu);
            // Build simple item model that refers to lower block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.getBlockName(0) + "_" + sb.meta + "_bottom";
            this.writeItemModelFile(def.getBlockName(0) + "_" + sb.meta, mo);
        }
    }

}
