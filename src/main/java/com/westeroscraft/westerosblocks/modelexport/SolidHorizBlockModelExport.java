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

public class SolidHorizBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, List<Variant>> variants = new HashMap<String, List<Variant>>();
    }
    public static class StateObject1 {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer weight;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectBottomTop {
        public String parent = "minecraft:block/cube";
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String particle, up, down, north, south, east, west;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public SolidHorizBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    private String getModelName(String ext, int setidx) {
    	return def.blockName + ext + ((setidx == 0)?"":("-v" + (setidx+1)));
    }
    static final String state[] = {
		"north=false,south=false,east=false,west=false",
		"north=true,south=false,east=false,west=false",
		"north=false,south=true,east=false,west=false",
		"north=true,south=true,east=false,west=false",
		"north=false,south=false,east=true,west=false",
		"north=true,south=false,east=true,west=false",
		"north=false,south=true,east=true,west=false",
		"north=true,south=true,east=true,west=false",
		"north=false,south=false,east=false,west=true",
		"north=true,south=false,east=false,west=true",
		"north=false,south=true,east=false,west=true",
		"north=true,south=true,east=false,west=true",
		"north=false,south=false,east=true,west=true",
		"north=true,south=false,east=true,west=true",
		"north=false,south=true,east=true,west=true",
		"north=true,south=true,east=true,west=true"
    };
    static final String ext[] = {
		"", 
		"_n", 
		"_s", 
		"_ns",
		"_e",
		"_ne",
		"_se",
		"_nse",
		"_w",
		"_nw",
		"_sw",
		"_nsw",
		"_ew", 
		"_new", 
		"_sew", 
		"_nsew"
	};
    // Texture indexes - 2=none, 3=left, 4=right, 5=both
    static final int txtidx_n[] = {
    	2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5
    };
    static final int txtidx_s[] = {
    	2, 2, 2, 2, 4, 4, 4, 4,  3, 3, 3, 3, 5, 5, 5, 5
    };
    static final int txtidx_e[] = {
    	2, 4, 3, 5, 2, 4, 3, 5, 2, 4, 3, 5, 2, 4, 3, 5
    };
    static final int txtidx_w[] = {
    	2, 3, 4, 5, 2, 3, 4, 5, 2, 3, 4, 5, 2, 3, 4, 5, 
    };
    @Override
    public void doBlockStateExport() throws IOException {
    	StateObject so = new StateObject();
    	    	
    	// Loop over 16 combinations of cardinal connections
    	for (int link = 0; link < ext.length; link++) {
            List<Variant> vars = new ArrayList<Variant>();
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	String model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext[link], setidx);
            	Variant var = new Variant();
            	var.model = model;
            	var.weight = set.weight;
            	vars.add(var);
            }
            so.variants.put(state[link], vars);
    	}
    	this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        
    	// Loop over 16 combinations of cardinal connections
    	for (int link = 0; link < ext.length; link++) {
            List<Variant> vars = new ArrayList<Variant>();
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	// down=false, up=false
        		ModelObjectBottomTop mod = new ModelObjectBottomTop();
        		mod.textures.down = getTextureID(set.getTextureByIndex(0));
        		mod.textures.up = getTextureID(set.getTextureByIndex(1));
        		mod.textures.north = getTextureID(set.getTextureByIndex(txtidx_n[link]));
        		mod.textures.south = getTextureID(set.getTextureByIndex(txtidx_s[link]));
        		mod.textures.east = getTextureID(set.getTextureByIndex(txtidx_e[link]));
        		mod.textures.west = getTextureID(set.getTextureByIndex(txtidx_w[link]));
        		mod.textures.particle = getTextureID(set.getTextureByIndex(1));
        		if (isTinted) {
        			mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube";
        		}
            	this.writeBlockModelFile(getModelName(ext[link], setidx), mod);
            }
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

}
