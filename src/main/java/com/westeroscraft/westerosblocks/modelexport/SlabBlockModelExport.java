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
        public Texture textures = new Texture();
        public ModelObjectHalfLower(boolean ambientocclusion, boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/half_slab";
                } 
                else {
                    parent = "westerosblocks:block/untinted/half_slab";
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
        public Texture textures = new Texture();
        public ModelObjectHalfUpper(boolean ambientocclusion, boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/upper_slab";
                } 
                else {
                    parent = "westerosblocks:block/untinted/upper_slab";
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
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    protected String getModelName(String ext, int setidx) {
    	return def.getBlockName() + "/" + ext + ("_v" + (setidx+1));
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Do state for top half block
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("top", setidx);
        	var.weight = set.weight;
        	so.addVariant("type=top", var, set.condIDs);
        }
    	// Do bottom half slab
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("bottom", setidx);
        	var.weight = set.weight;
        	so.addVariant("type=bottom", var, set.condIDs);
        }
        // Do full slab
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("double", setidx);
        	var.weight = set.weight;
        	so.addVariant("type=double", var, set.condIDs);
        }
        this.writeBlockStateFile(def.getBlockName(), so);
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
        	mod.textures.north = getTextureID(set.getTextureByIndex(2));
			mod.textures.south = getTextureID(set.getTextureByIndex(3));
			mod.textures.west = getTextureID(set.getTextureByIndex(4));
			mod.textures.east = getTextureID(set.getTextureByIndex(5));
        	mod.textures.particle = getTextureID(set.getTextureByIndex(2));
        	this.writeBlockModelFile(getModelName("double", setidx), mod);
        	// Lower half block model
        	ModelObjectHalfLower modl = new ModelObjectHalfLower(isOccluded, isTinted);
        	modl.textures.down = getTextureID(set.getTextureByIndex(0));
        	modl.textures.up = getTextureID(set.getTextureByIndex(1));
        	modl.textures.north = getTextureID(set.getTextureByIndex(2));
			modl.textures.south = getTextureID(set.getTextureByIndex(3));
			modl.textures.west = getTextureID(set.getTextureByIndex(4));
			modl.textures.east = getTextureID(set.getTextureByIndex(5));
        	modl.textures.particle = getTextureID(set.getTextureByIndex(2));
        	this.writeBlockModelFile(getModelName("bottom", setidx), modl);
        	// Upper half block model
        	ModelObjectHalfUpper modu = new ModelObjectHalfUpper(isOccluded, isTinted);
        	modu.textures.down = getTextureID(set.getTextureByIndex(0));
        	modu.textures.up = getTextureID(set.getTextureByIndex(1));
        	modu.textures.north = getTextureID(set.getTextureByIndex(2));
			modu.textures.south = getTextureID(set.getTextureByIndex(3));
			modu.textures.west = getTextureID(set.getTextureByIndex(4));
			modu.textures.east = getTextureID(set.getTextureByIndex(5));
        	modu.textures.particle = getTextureID(set.getTextureByIndex(2));
        	this.writeBlockModelFile(getModelName("top", setidx), modu);
        }
        // Build simple item model that refers to lower block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("bottom", 0);
        this.writeItemModelFile(def.getBlockName(), mo);
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
            }
        }
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	newstate.put("waterlogged", "false");
    	if (def.condStates != null) {
    		newstate.put("cond", def.getDefaultCondID());    		
    	}
    	// Bottom half
    	oldstate.put("half", "bottom");
    	newstate.put("type", "bottom");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
       	// Top half
       	oldstate.put("half", "top");
    	newstate.put("type", "top");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
       	// Double slab
    	oldstate.remove("half");
    	newstate.put("type", "double");
        addWorldConverterRecord(oldID + "_2", oldstate, def.getBlockName(), newstate);
    }

}
