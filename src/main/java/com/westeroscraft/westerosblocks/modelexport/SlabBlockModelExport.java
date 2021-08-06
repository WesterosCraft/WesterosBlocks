package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class SlabBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, List<Variant>> variants = new HashMap<String, List<Variant>>();
    }
    public static class Variant {
        public String model;
        public Integer weight;
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
    
    private String getModelName(String mod, int setidx) {
    	return def.getBlockName() + "_" + mod + ((setidx == 0) ? "" : ("_v" + (setidx+1)));
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Do state for top half block
        String bn = def.getBlockName();
        List<Variant> vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = WesterosBlocks.MOD_ID + ":block/" + getModelName("top", setidx);
        	var.weight = set.weight;
        	vars.add(var);
        }
    	so.variants.put("type=top", vars);
    	// Do bottom half slab
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = WesterosBlocks.MOD_ID + ":block/" + getModelName("bottom", setidx);
        	var.weight = set.weight;
        	vars.add(var);
        }
        so.variants.put("type=bottom", vars);
        // Do full slab
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = WesterosBlocks.MOD_ID + ":block/" + getModelName("double", setidx);
        	var.weight = set.weight;
        	vars.add(var);
        }
        so.variants.put("type=double", vars);
        this.writeBlockStateFile(bn, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        boolean isOccluded = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	// Double block model
        	ModelObjectCube mod = new ModelObjectCube(isOccluded, isTinted);
        	mod.textures.down = getTextureID(set.getTextureByIndex(0));
        	mod.textures.up = getTextureID(set.getTextureByIndex(1));
        	mod.textures.north = mod.textures.south = mod.textures.west = mod.textures.east = getTextureID(set.getTextureByIndex(2));
        	mod.textures.particle = getTextureID(set.getTextureByIndex(2));
        	this.writeBlockModelFile(getModelName("double", setidx), mod);
        	// Lower half block model
        	ModelObjectHalfLower modl = new ModelObjectHalfLower(isOccluded, isTinted);
        	modl.textures.bottom = getTextureID(set.getTextureByIndex(0));
        	modl.textures.top = getTextureID(set.getTextureByIndex(1));
        	modl.textures.side = modl.textures.particle = getTextureID(set.getTextureByIndex(2));
        	this.writeBlockModelFile(getModelName("bottom", setidx), modl);
        	// Upper half block model
        	ModelObjectHalfUpper modu = new ModelObjectHalfUpper(isOccluded, isTinted);
        	modu.textures.bottom = getTextureID(set.getTextureByIndex(0));
        	modu.textures.top = getTextureID(set.getTextureByIndex(1));
        	modu.textures.side = modu.textures.particle = getTextureID(set.getTextureByIndex(2));
        	this.writeBlockModelFile(getModelName("top", setidx), modu);
        }
        // Build simple item model that refers to lower block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + getModelName("bottom", 0);
        this.writeItemModelFile(def.getBlockName(), mo);
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
            }
        }
    }
}
