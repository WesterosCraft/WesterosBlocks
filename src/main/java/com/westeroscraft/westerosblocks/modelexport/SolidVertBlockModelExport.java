package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class SolidVertBlockModelExport extends ModelExport {
    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, List<Variant>> variants = new HashMap<String, List<Variant>>();
    }
    public static class StateObject1 {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer y;
        public Integer weight;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectBottomTop {
        public String parent = "minecraft:block/cube_bottom_top";    // Use 'cube' model for multiple textures
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String particle, bottom, top, side;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public SolidVertBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    private String getModelName(String ext, int setidx) {
    	return def.blockName + ext + ((setidx == 0)?"":("-v" + (setidx+1)));
    }
    @Override
    public void doBlockStateExport() throws IOException {
    	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
    	StateObject so = new StateObject();
        List<Variant> vars = new ArrayList<Variant>();
        List<Variant> vars2 = new ArrayList<Variant>();
        List<Variant> vars3 = new ArrayList<Variant>();
        List<Variant> vars4 = new ArrayList<Variant>();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	// Default for up=false, down=false
        	String model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("", setidx);
        	String model2 = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("_up", setidx);
        	String model3 = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("_down", setidx);
        	String model4 = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("_both", setidx);
            for (int i = 0; i < cnt; i++) {
            	// down=false,up=false
            	Variant var = new Variant();
            	var.model = model;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
            	vars.add(var);
            	// down=false,up=true
            	var = new Variant();
            	var.model = model2;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
            	vars2.add(var);
            	// down=true,up=false
            	var = new Variant();
            	var.model = model3;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
            	vars3.add(var);
            	// down=true,up=true
            	var = new Variant();
            	var.model = model4;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
            	vars4.add(var);
            }            
        }
    	so.variants.put("down=false,up=false", vars);
    	so.variants.put("down=false,up=true", vars2);
    	so.variants.put("down=true,up=false", vars3);
    	so.variants.put("down=true,up=true", vars4);
    	this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	// down=false, up=false
    		ModelObjectBottomTop mod = new ModelObjectBottomTop();
    		mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
    		mod.textures.top = getTextureID(set.getTextureByIndex(1));
    		mod.textures.side = getTextureID(set.getTextureByIndex(2));	// Standard side (up=false, down=false)
    		mod.textures.particle = getTextureID(set.getTextureByIndex(2));
    		if (isTinted) {
    			mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
    		}
        	this.writeBlockModelFile(getModelName("", setidx), mod);
        	// down=true, up=false
    		mod = new ModelObjectBottomTop();
    		mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
    		mod.textures.top = getTextureID(set.getTextureByIndex(1));
    		mod.textures.side = getTextureID(set.getTextureByIndex(3));	// (up=false, down=true)
    		mod.textures.particle = getTextureID(set.getTextureByIndex(2));
    		if (isTinted) {
    			mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
    		}
        	this.writeBlockModelFile(getModelName("_down", setidx), mod);
        	// down=false, up=true
    		mod = new ModelObjectBottomTop();
    		mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
    		mod.textures.top = getTextureID(set.getTextureByIndex(1));
    		mod.textures.side = getTextureID(set.getTextureByIndex(4));	// (up=true, down=false)
    		mod.textures.particle = getTextureID(set.getTextureByIndex(2));
    		if (isTinted) {
    			mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
    		}
        	this.writeBlockModelFile(getModelName("_up", setidx), mod);
        	// down=true, up=true
    		mod = new ModelObjectBottomTop();
    		mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
    		mod.textures.top = getTextureID(set.getTextureByIndex(1));
    		mod.textures.side = getTextureID(set.getTextureByIndex(5));	// (up=true, down=true)
    		mod.textures.particle = getTextureID(set.getTextureByIndex(2));
    		if (isTinted) {
    			mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
    		}
        	this.writeBlockModelFile(getModelName("_both", setidx), mod);
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("", 0);
        this.writeItemModelFile(def.blockName, mo);
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	addWorldConverterComment(def.legacyBlockID + " (need vert CTM handler)");
    	String oldVariant = def.getLegacyBlockVariant();
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	//TODO: need to add handler to WorldConverter for the vertical CTM states
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), null);
    }

}
