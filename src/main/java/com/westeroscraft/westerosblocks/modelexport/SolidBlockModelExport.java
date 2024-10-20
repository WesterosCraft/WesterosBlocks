package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;

import net.minecraft.world.level.block.Block;

public class SolidBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectCubeAll {
        public String parent = "minecraft:block/cube_all";    // Use 'cube_all' model for single texture
        public TextureAll textures = new TextureAll();
    }
    public static class TextureAll {
        public String all;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCube {
        public String parent = "minecraft:block/cube";    // Use 'cube' model for multiple textures
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String down, up, north, south, west, east, particle;
    }
    public static class OverlayTexture extends Texture {
        public String down_ov, up_ov, north_ov, south_ov, west_ov, east_ov;
    }
    
    public static class ModelObject {
    	public String parent;
    }

	private final WCSolidBlock sblk;
    
    public SolidBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
				sblk = (blk instanceof WCSolidBlock) ? (WCSolidBlock) blk : null;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    protected String getModelName(String ext, int setidx) {
    	return def.blockName + "/" + ext + ("_v" + (setidx+1));
    }

		protected String getModelName(String ext, int setidx, boolean symmetrical) {
			String dir = (symmetrical) ? "symmetrical" : "asymmetrical";
			return def.blockName + "/" + dir + "/" + ext + ("_v" + (setidx+1));
		}

    public String modelFileName(String ext, int setidx, boolean isCustom) {
    	return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx);
    }

    public String modelFileName(String ext, int setidx, boolean isCustom, boolean symmetrical) {
    	return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx, symmetrical);
    }

    @Override
    public void doBlockStateExport() throws IOException {
    	StateObject so = new StateObject();
    	
    	for (WesterosBlockStateRecord sr : def.states) {
    		boolean justBase = sr.stateID == null;
    		Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
    		String fname = justBase ? "base" : sr.stateID;
	      // Loop over the random sets we've got
	      for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
					WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
					int cnt = sr.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
					for (int i = 0; i < cnt; i++) {
						if (sblk != null && sblk.symmetrical) {
							Variant var_s = new Variant();
							var_s.model = modelFileName(fname, setidx, sr.isCustomModel(), true);
							var_s.weight = set.weight;
							if (i > 0) var_s.y = 90*i;
							so.addVariant("symmetrical=true", var_s, stateIDs);
							Variant var_as = new Variant();
							var_as.model = modelFileName(fname, setidx, sr.isCustomModel(), false);
							var_as.weight = set.weight;
							if (i > 0) var_as.y = 90*i;
							so.addVariant("symmetrical=false", var_as, stateIDs);
						}
						else {
							Variant var = new Variant();
							var.model = modelFileName(fname, setidx, sr.isCustomModel());
							var.weight = set.weight;
							if (i > 0) var.y = 90*i;
							so.addVariant("", var, stateIDs);	// Add our variant    
						}            	
	        }
	      }
    	}
    	this.writeBlockStateFile(def.blockName, so);        	
    }

		protected void doSolidModel(String name, boolean isTinted, boolean isOverlay, boolean isSymmetrical, int setidx, WesterosBlockStateRecord sr, int sridx) throws IOException {
			Object model;
			WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
			if (isOverlay) {
				ModelObjectCube mod = new ModelObjectCube();
				OverlayTexture ot = new OverlayTexture();
				ot.down = getTextureID(set.getTextureByIndex(0));
				ot.up = getTextureID(set.getTextureByIndex(1));
				ot.north = getTextureID(set.getTextureByIndex(2));
				ot.south = getTextureID(set.getTextureByIndex(3));
				ot.west = (isSymmetrical) ? getTextureID(set.getTextureByIndex(4)) :
																		getTextureID(set.getTextureByIndex(6));
				ot.east = (isSymmetrical) ? getTextureID(set.getTextureByIndex(5)) :
																		getTextureID(set.getTextureByIndex(7));
				ot.particle = getTextureID(set.getTextureByIndex(2));
				ot.down_ov = getTextureID(sr.getOverlayTextureByIndex(0));
				ot.up_ov = getTextureID(sr.getOverlayTextureByIndex(1));
				ot.north_ov = getTextureID(sr.getOverlayTextureByIndex(2));
				ot.south_ov = getTextureID(sr.getOverlayTextureByIndex(3));
				ot.west_ov = (isSymmetrical) ? getTextureID(sr.getOverlayTextureByIndex(4)) :
																			 getTextureID(sr.getOverlayTextureByIndex(6));
				ot.east_ov = (isSymmetrical) ? getTextureID(sr.getOverlayTextureByIndex(5)) :
																			 getTextureID(sr.getOverlayTextureByIndex(7));
				mod.textures = ot;
				mod.parent = isTinted ? 
					WesterosBlocks.MOD_ID + ":block/tinted/cube_overlay" : 
					WesterosBlocks.MOD_ID + ":block/untinted/cube_overlay";
				model = mod;
			}
			else if ((set.getTextureCount() > 1) || isTinted) { // More than one texture, or tinted?
				ModelObjectCube mod = new ModelObjectCube();
				mod.textures.down = getTextureID(set.getTextureByIndex(0));
				mod.textures.up = getTextureID(set.getTextureByIndex(1));
				mod.textures.north = getTextureID(set.getTextureByIndex(2));
				mod.textures.south = getTextureID(set.getTextureByIndex(3));
				mod.textures.west = (isSymmetrical) ? getTextureID(set.getTextureByIndex(4)) :
																							getTextureID(set.getTextureByIndex(6));
				mod.textures.east = (isSymmetrical) ? getTextureID(set.getTextureByIndex(5)) :
																							getTextureID(set.getTextureByIndex(7));
				mod.textures.particle = getTextureID(set.getTextureByIndex(2));
				if (isTinted) {
					mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube";
				}
				model = mod;
			}
			else {
				ModelObjectCubeAll mod = new ModelObjectCubeAll();
				mod.textures.all = getTextureID(set.getTextureByIndex(0)); 
				model = mod;
			}
			this.writeBlockModelFile(name, model);
		}

    @Override
    public void doModelExports() throws IOException {
			for (int idx = 0; idx < def.states.size(); idx++) {
				WesterosBlockStateRecord rec = def.states.get(idx);
				boolean isTinted = rec.isTinted();
        boolean isOverlay = rec.getOverlayTextureByIndex(0) != null;
				String id = (rec.stateID == null) ? "base" : rec.stateID;
				// Loop over the random sets we've got
				for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
					if (sblk != null && sblk.symmetrical) {
						doSolidModel(getModelName(id, setidx, true), isTinted, isOverlay, true, setidx, rec, idx);
						doSolidModel(getModelName(id, setidx, false), isTinted, isOverlay, false, setidx, rec, idx);
					}
					else {
						doSolidModel(getModelName(id, setidx), isTinted, isOverlay, false, setidx, rec, idx);
					}
				}
      }
      // Build simple item model that refers to block model
      ModelObject mo = new ModelObject();
      WesterosBlockStateRecord sr0 = def.states.get(0);
      boolean isTinted = sr0.isTinted();
    	String id = (sr0.stateID == null) ? "base" : sr0.stateID;
      mo.parent = (sblk != null && sblk.symmetrical) ? modelFileName(id, 0, sr0.isCustomModel(), true) :
																											 modelFileName(id, 0, sr0.isCustomModel());
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
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = null;
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
			if (sblk != null && sblk.connectstate) {
				if (newstate == null)
					newstate = new HashMap<String, String>();
				newstate.put("connectstate", "0");
			}
			if (sblk != null && sblk.symmetrical) {
				if (newstate == null)
					newstate = new HashMap<String, String>();
				newstate.put("symmetrical", "false");
			}
      addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }
}
