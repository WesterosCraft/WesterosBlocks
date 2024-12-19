package com.westerosblocks.needsported.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
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

    private final WCFenceBlock fblk;

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

			for (WesterosBlockStateRecord sr : def.states) {
				boolean justBase = sr.stateID == null;
				Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
				// Loop through parts
				for (ModelPart mp : PARTS) {
					// Loop over the random sets we've got
					for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
						WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
						Apply a = new Apply();
						a.model = (justBase) ? WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(mp.modExt, setidx) :
																	 WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(mp.modExt, setidx, sr.stateID);
						a.weight = set.weight;
						if (mp.uvlock != null)
							a.uvlock = mp.uvlock;
						if (mp.y != null)
							a.y = mp.y;

						so.addStates(mp.when, a, stateIDs);
					}
				}
			}
			this.writeBlockStateFile(def.blockName, so);
    }

		protected void doFenceModel(boolean isTinted, boolean isOverlay, int setidx, WesterosBlockStateRecord sr, int sridx, String cond) throws IOException {
			WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);

			ModelObjectPost mod = new ModelObjectPost();
			mod.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
			mod.textures.top = getTextureID(set.getTextureByIndex(1)); 
			mod.textures.side = getTextureID(set.getTextureByIndex(2)); 
			if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_post";
			if (isOverlay) {
				mod.parent += "_overlay";
			mod.textures.bottom_ov = getTextureID(sr.getOverlayTextureByIndex(0));
			mod.textures.top_ov = getTextureID(sr.getOverlayTextureByIndex(1));
			mod.textures.side_ov = getTextureID(sr.getOverlayTextureByIndex(2));        		
			}
			this.writeBlockModelFile(getModelName("post", setidx, cond), mod);
			// Side model
			ModelObjectSide smod = new ModelObjectSide();
			smod.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
			smod.textures.top = getTextureID(set.getTextureByIndex(1)); 
			smod.textures.side = getTextureID(set.getTextureByIndex(2)); 
			if (isTinted) smod.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_side";
			if (isOverlay) {
				smod.parent += "_overlay";
			smod.textures.bottom_ov = getTextureID(sr.getOverlayTextureByIndex(0));
			smod.textures.top_ov = getTextureID(sr.getOverlayTextureByIndex(1));
			smod.textures.side_ov = getTextureID(sr.getOverlayTextureByIndex(2));        		
			}
			this.writeBlockModelFile(getModelName("side", setidx, cond), smod);
		}

    @Override
    public void doModelExports() throws IOException {
			for (int idx = 0; idx < def.states.size(); idx++) {
				WesterosBlockStateRecord rec = def.states.get(idx);
				boolean isTinted = rec.isTinted();
				boolean isOverlay = rec.getOverlayTextureByIndex(0) != null;
				String cond = rec.stateID;
				// Loop over the random sets we've got
				for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
					doFenceModel(isTinted, isOverlay, setidx, rec, idx, cond);
				}
			}

    	// Build simple item model that refers to fence inventory model
    	ModelObject mo = new ModelObject();
			WesterosBlockStateRecord sr0 = def.states.get(0);
			boolean isTinted = sr0.isTinted();
			WesterosBlockDef.RandomTextureSet set = sr0.getRandomTextureSet(0);
    	mo.textures.bottom = getTextureID(set.getTextureByIndex(0)); 
    	mo.textures.top = getTextureID(set.getTextureByIndex(1)); 
    	mo.textures.side = getTextureID(set.getTextureByIndex(2)); 
    	if (isTinted)
				mo.parent = WesterosBlocks.MOD_ID + ":block/tinted/fence_inventory";
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
