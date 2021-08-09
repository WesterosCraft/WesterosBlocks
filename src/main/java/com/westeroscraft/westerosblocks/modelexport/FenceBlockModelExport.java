package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class FenceBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
    	public List<States> multipart = new ArrayList<States>();
    }
    public static class States {
    	public List<Apply> apply = new ArrayList<Apply>();
    }
    public static class SideStates extends States {
    	public WhenRec when = new WhenRec();
    }
    public static class WhenRec {
    	String north, south, west, east;
    	public List<WhenRec> OR;
    }
    public static class Apply {
    	String model;
    	Integer x;
    	Integer y;
    	Boolean uvlock;
    	Integer weight;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectPost {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/fence_post";    // Use 'fence_post' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectSide {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/fence_side";    // Use 'fence_side' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String bottom, top, side;
    }
    public static class ModelObject {
    	public String parent = WesterosBlocks.MOD_ID + ":block/untinted/fence_inventory";
        public Texture textures = new Texture();
    }

    public FenceBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    private String getModelName(String ext, int setidx) {
    	return def.blockName + "_" + ext + ((setidx == 0)?"":("-v" + (setidx+1)));
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
    	// Add post based on our variant
    	States ps = new States();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Apply a = new Apply();
        	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("post", setidx);
        	a.weight = set.weight;
        	ps.apply.add(a);
        }
    	so.multipart.add(ps);
    	// Add north variant
    	SideStates ssn = new SideStates();
    	ssn.when.north = "true";
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Apply a = new Apply();
        	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", setidx);
        	a.weight = set.weight;
        	a.uvlock = true;
        	ssn.apply.add(a);
        }
    	so.multipart.add(ssn);
    	// Add east variant
    	ssn = new SideStates();
    	ssn.when.east = "true";
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Apply a = new Apply();
        	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", setidx);
        	a.weight = set.weight;
        	a.uvlock = true;
        	a.y = 90;
        	ssn.apply.add(a);
        }
    	so.multipart.add(ssn);
    	// Add south variant
        ssn = new SideStates();
    	ssn.when.south = "true";
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Apply a = new Apply();
        	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", setidx);
        	a.weight = set.weight;
        	a.uvlock = true;
        	a.y = 180;
        	ssn.apply.add(a);
        }
        so.multipart.add(ssn);
    	// Add west variant
        ssn = new SideStates();
    	ssn.when.west = "true";
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Apply a = new Apply();
        	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", setidx);
        	a.weight = set.weight;
        	a.uvlock = true;
        	a.y = 270;
        	ssn.apply.add(a);
        }
        so.multipart.add(ssn);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
    	WesterosBlockDef.RandomTextureSet set;

        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	set = def.getRandomTextureSet(setidx);
        
        	ModelObjectPost mod = new ModelObjectPost();
        	mod.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
        	mod.textures.top = getTextureID(set.getTextureByIndex(1)); 
        	mod.textures.side = getTextureID(set.getTextureByIndex(2)); 
        	if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_post";
        	this.writeBlockModelFile(getModelName("post", setidx), mod);
        	// Side model
        	ModelObjectSide smod = new ModelObjectSide();
        	smod.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
        	smod.textures.top = getTextureID(set.getTextureByIndex(1)); 
        	smod.textures.side = getTextureID(set.getTextureByIndex(2)); 
        	if (isTinted) smod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_side";
        	this.writeBlockModelFile(getModelName("side", setidx), smod);
        }
    	// Build simple item model that refers to fence inventory model
    	ModelObject mo = new ModelObject();
    	set = def.getRandomTextureSet(0);
    	mo.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
    	mo.textures.top = getTextureID(set.getTextureByIndex(1)); 
    	mo.textures.side = getTextureID(set.getTextureByIndex(2)); 
    	if (isTinted) mo.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_inventory";
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
