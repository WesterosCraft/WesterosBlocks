package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.modelexport.CrossBlockModelExport.ModelObject;
import com.westeroscraft.westerosblocks.modelexport.CrossBlockModelExport.ModelObjectCross;
import com.westeroscraft.westerosblocks.modelexport.ModelExport.StateObject;
import com.westeroscraft.westerosblocks.modelexport.ModelExport.Variant;

import net.minecraft.world.level.block.Block;

public class CropBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectCrop {
        public String parent = "westerosblocks:block/untinted/crop";
        public TextureCrop textures = new TextureCrop();
    }
    public static class TextureCrop {
        public String crop;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }
    
    protected boolean layerSensitive = false;
    public CropBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
        String t = def.getType();
        if ((t != null) && (t.indexOf(WesterosBlockDef.LAYER_SENSITIVE) >= 0)) {
        	layerSensitive = true;
        }        
    }
    
    private static String[] layerConds = { "layers=8", "layers=1", "layers=2", "layers=3", "layers=4", "layers=5", "layers=6", "layers=7" };

    public void doBlockStateExport() throws IOException {
    	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
        StateObject so = new StateObject();

        String[] conds = layerSensitive ? layerConds : new String[] { "" };
        
        for (int layer = 0; layer < conds.length; layer++) {
        	String cond = conds[layer];
	    	//WesterosBlocks.log.info(String.format("%s: size=%d", def.blockName, def.states.size()));
	    	for (int idx = 0; idx < def.states.size(); idx++) {
	    		WesterosBlockStateRecord rec = def.states.get(idx);
				String id = (rec.stateID == null) ? "base" : rec.stateID;
				if (layer > 0) {
					id = id + "_layer" + layer;
				}
	    		// Loop over the random sets we've got
	    		for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
		        	WesterosBlockDef.RandomTextureSet set = rec.getRandomTextureSet(setidx);
		        	for (int rot = 0; rot < cnt; rot++) {
		        		Variant var = new Variant();
		            	var.weight = set.weight;
		        		var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(id, setidx);
		        		if (rec.isCustomModel())
		        			var.model = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(id, setidx);
		        		if (rot > 0) var.y = (90 * rot);
		        		so.addVariant(cond, var, (rec.stateID == null) ? null : Collections.singleton(rec.stateID));
		        	}
		        }
	    	}
        }
        this.writeBlockStateFile(def.blockName, so);
    }
    
    @Override
    public void doModelExports() throws IOException {
    	boolean isTinted = def.isTinted();
    	//WesterosBlocks.log.info(String.format("%s: size=%d", def.blockName, def.states.size()));
        String[] conds = layerSensitive ? layerConds : new String[] { "" };
        
        for (int layer = 0; layer < conds.length; layer++) {
	    	for (int idx = 0; idx < def.states.size(); idx++) {
	    		WesterosBlockStateRecord rec = def.states.get(idx);
	    		if (rec.isCustomModel()) continue;
				String id = (rec.stateID == null) ? "base" : rec.stateID;
				if (layer > 0) {
					id = id + "_layer" + layer;
				}
		    	// Loop over the random sets we've got
		        for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
		        	WesterosBlockDef.RandomTextureSet set = rec.getRandomTextureSet(setidx);
		        	ModelObjectCrop mod = new ModelObjectCrop();
		        	if (isTinted) {
		        		mod.parent = "westerosblocks:block/tinted/crop";
		        	}
		        	mod.textures.crop = getTextureID(set.getTextureByIndex(0)); 
		        	if (layer > 0) {
		        		mod.parent = mod.parent + "_layer" + layer;
		        	}
		        	this.writeBlockModelFile(getModelName(id, setidx), mod);
		        }
	    	}
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = getTextureID(def.states.get(0).getTextureByIndex(0));
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
    	Map<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	newstate.put("waterlogged", "false");
    	if (layerSensitive) {
    		newstate.put("layers", "8");
    	}
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
