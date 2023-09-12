package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCFenceBlock;

import net.minecraft.world.level.block.Block;

public class FenceBlockModelExport extends ModelExport {
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
        public String bottom_ov, top_ov, side_ov;
    }
    public static class ModelObject {
    	public String parent = WesterosBlocks.MOD_ID + ":block/untinted/fence_inventory";
        public Texture textures = new Texture();
    }

    WCFenceBlock fblk;
    public FenceBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        fblk = (WCFenceBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    private static class ModelPart {
    	String modExt;
    	WhenRec when;
    	Boolean uvlock;
    	Integer y;
    	ModelPart(String mx, String n, String s, String e, String w, Boolean uvlock, Integer y) {
    		this.modExt = mx;
    		if ((n != null) || (s != null) || (e != null) || (w != null)) {
        		this.when = new WhenRec();
        		this.when.north = n; this.when.south = s; 
        		this.when.east = e; this.when.west = w; 
    		}
    		this.uvlock = uvlock;
    		this.y = y;
    	}
    };
    private static ModelPart[] PARTS = {
		// Post
		new ModelPart("post", null, null, null, null, null, null),
		// North low
		new ModelPart("side", "true", null, null, null, true, null),    		
		// East low
		new ModelPart("side", null, null, "true", null, true, 90),    		
		// South low
		new ModelPart("side", null, "true", null, null, true, 180),    		
		// East low
		new ModelPart("side", null, null, null, "true", true, 270),    		
    };
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Loop through parts
        for (ModelPart mp : PARTS) {
        	// Add post based on our variant
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	Apply a = new Apply();
            	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(mp.modExt, setidx);
            	a.weight = set.weight;
            	if (mp.uvlock != null) a.uvlock = mp.uvlock;
            	if (mp.y != null) a.y = mp.y;

            	so.addStates(mp.when, a, null);
            }
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        boolean isOverlay = def.getOverlayTextureByIndex(0) != null;
    	WesterosBlockDef.RandomTextureSet set;

        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	set = def.getRandomTextureSet(setidx);
        
        	ModelObjectPost mod = new ModelObjectPost();
        	mod.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
        	mod.textures.top = getTextureID(set.getTextureByIndex(1)); 
        	mod.textures.side = getTextureID(set.getTextureByIndex(2)); 
        	if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_post";
        	if (isOverlay) {
        		mod.parent += "_overlay";
     			mod.textures.bottom_ov = getTextureID(def.getOverlayTextureByIndex(0));
     			mod.textures.top_ov = getTextureID(def.getOverlayTextureByIndex(1));
     			mod.textures.side_ov = getTextureID(def.getOverlayTextureByIndex(2));        		
        	}
        	this.writeBlockModelFile(getModelName("post", setidx), mod);
        	// Side model
        	ModelObjectSide smod = new ModelObjectSide();
        	smod.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
        	smod.textures.top = getTextureID(set.getTextureByIndex(1)); 
        	smod.textures.side = getTextureID(set.getTextureByIndex(2)); 
        	if (isTinted) smod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_side";
        	if (isOverlay) {
        		smod.parent += "_overlay";
     			smod.textures.bottom_ov = getTextureID(def.getOverlayTextureByIndex(0));
     			smod.textures.top_ov = getTextureID(def.getOverlayTextureByIndex(1));
     			smod.textures.side_ov = getTextureID(def.getOverlayTextureByIndex(2));        		
        	}
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
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") - need fence connection mapping");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	// No metadata other than variant - need filter for all of this - just pass one combination
    	oldstate.put("north", "false");
    	newstate.put("north", "false");
    	oldstate.put("south", "false");
    	newstate.put("south", "false");
    	oldstate.put("east", "false");
    	newstate.put("east", "false");
    	oldstate.put("west", "false");
    	newstate.put("west", "false");
    	newstate.put("waterlogged", "false");
    	if (fblk.unconnect) {
        	newstate.put("unconnect", fblk.unconnectDef.toString());
    	}
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
