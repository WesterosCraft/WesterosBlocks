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
    	public Apply apply = new Apply();
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
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
    	// Add post based on our variant
    	States ps = new States();
    	ps.apply.model = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_post";
    	so.multipart.add(ps);
    	// Add north variant
    	SideStates ssn = new SideStates();
    	ssn.when.north = "true";
    	ssn.apply.model = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_side";
    	ssn.apply.uvlock = true;
    	so.multipart.add(ssn);
    	// Add east variant
    	ssn = new SideStates();
    	ssn.when.east = "true";
    	ssn.apply.model = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_side";
    	ssn.apply.uvlock = true;
    	ssn.apply.y = 90;
    	so.multipart.add(ssn);
    	// Add south variant
        ssn = new SideStates();
    	ssn.when.south = "true";
        ssn.apply.model = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_side";
        ssn.apply.uvlock = true;
        ssn.apply.y = 180;
        so.multipart.add(ssn);
    	// Add west variant
        ssn = new SideStates();
    	ssn.when.west = "true";
        ssn.apply.model = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_side";
        ssn.apply.uvlock = true;
        ssn.apply.y = 270;
        so.multipart.add(ssn);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        ModelObjectPost mod = new ModelObjectPost();
        mod.textures.bottom = getTextureID(def.getTextureByIndex(0)); 
        mod.textures.top = getTextureID(def.getTextureByIndex(1)); 
        mod.textures.side = getTextureID(def.getTextureByIndex(2)); 
        if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_post";
        this.writeBlockModelFile(def.blockName + "_post", mod);
        // Side model
        ModelObjectSide smod = new ModelObjectSide();
        smod.textures.bottom = getTextureID(def.getTextureByIndex(0)); 
        smod.textures.top = getTextureID(def.getTextureByIndex(1)); 
        smod.textures.side = getTextureID(def.getTextureByIndex(2)); 
        if (isTinted) smod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_side";
        this.writeBlockModelFile(def.blockName + "_side", smod);
        // Build simple item model that refers to fence inventory model
        ModelObject mo = new ModelObject();
        mo.textures.bottom = getTextureID(def.getTextureByIndex(0)); 
        mo.textures.top = getTextureID(def.getTextureByIndex(1)); 
        mo.textures.side = getTextureID(def.getTextureByIndex(2)); 
        if (isTinted) mo.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_inventory";
        this.writeItemModelFile(def.blockName, mo);
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }

}
