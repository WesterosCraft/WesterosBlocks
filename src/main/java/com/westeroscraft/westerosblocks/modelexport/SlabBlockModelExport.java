package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class SlabBlockModelExport extends ModelExport {
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
        public String parent;
        public Texture textures = new Texture();
        public ModelObjectCube(boolean ambientocclusion, boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/cube";
                } 
                else {
                    parent = "minecraft:block/cube";    // Vanilla block is
                }
            }
            else {
                if (tinted) {
                    parent = "westerosblocks:block/tintednoocclusion/cube";
                }
                else {
                    parent = "westerosblocks:block/noocclusion/cube";
                }
            }
        }
    }
    public static class Texture {
        public String down, up, north, south, west, east, particle;
    }
    public static class ModelObjectHalfLower {
        public String parent;
        public TextureSlab textures = new TextureSlab();
        public ModelObjectHalfLower(boolean ambientocclusion, boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/half_slab";
                } 
                else {
                    parent = "minecraft:block/slab";    // Vanilla block is
                }
            }
            else {
                if (tinted) {
                    parent = "westerosblocks:block/tintednoocclusion/half_slab";
                }
                else {
                    parent = "westerosblocks:block/noocclusion/half_slab";
                }
            }
        }
    }
    public static class ModelObjectHalfUpper {
        public String parent;
        public TextureSlab textures = new TextureSlab();
        public ModelObjectHalfUpper(boolean ambientocclusion, boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/upper_slab";
                } 
                else {
                    parent = "minecraft:block/slab_top";    // Vanilla block is
                }
            }
            else {
                if (tinted) {
                    parent = "westerosblocks:block/tintednoocclusion/upper_slab";
                }
                else {
                    parent = "westerosblocks:block/noocclusion/upper_slab";
                }
            }
        }
    }
    public static class TextureSlab {
        public String bottom, top, side, particle;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public SlabBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Do state for half block
        String bn = def.getBlockName(0);
        Variant var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/" + bn + "_top";
        so.variants.put("type=top", var);
        var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/" + bn + "_bottom";
        so.variants.put("type=bottom", var);
        var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/" + bn + "_double";
        so.variants.put("type=double", var);
        this.writeBlockStateFile(bn, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        boolean isOccluded = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;
    	// Double block model
        ModelObjectCube mod = new ModelObjectCube(isOccluded, isTinted);
        mod.textures.down = getTextureID(def.getTextureByIndex(0));
        mod.textures.up = getTextureID(def.getTextureByIndex(1));
        mod.textures.north = getTextureID(def.getTextureByIndex(2));
        mod.textures.south = getTextureID(def.getTextureByIndex(3));
        mod.textures.west = getTextureID(def.getTextureByIndex(4));
        mod.textures.east = getTextureID(def.getTextureByIndex(5));
        mod.textures.particle = getTextureID(def.getTextureByIndex(3));
        this.writeBlockModelFile(def.getBlockName(0) + "_double", mod);
        // Lower half block model
        ModelObjectHalfLower modl = new ModelObjectHalfLower(isOccluded, isTinted);
        modl.textures.bottom = getTextureID(def.getTextureByIndex(0));
        modl.textures.top = getTextureID(def.getTextureByIndex(1));
        modl.textures.side = modl.textures.particle = getTextureID(def.getTextureByIndex(2));
        this.writeBlockModelFile(def.getBlockName(0) + "_bottom", modl);
        // Upper half block model
        ModelObjectHalfUpper modu = new ModelObjectHalfUpper(isOccluded, isTinted);
        modu.textures.bottom = getTextureID(def.getTextureByIndex(0));
        modu.textures.top = getTextureID(def.getTextureByIndex(1));
        modu.textures.side = modu.textures.particle = getTextureID(def.getTextureByIndex(2));
        this.writeBlockModelFile(def.getBlockName(0) + "_top", modu);
        // Build simple item model that refers to lower block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.getBlockName(0) + "_bottom";
        this.writeItemModelFile(def.getBlockName(0), mo);
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }

}
