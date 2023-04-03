package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;

public class CropBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectCrop {
        public String parent = "block/crop";    // Use 'crop' model for single texture
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
    
    public CropBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
    	for (int idx = 0; idx < def.states.size(); idx++) {
    		WesterosBlockStateRecord rec = def.states.get(idx);
			String id = (rec.stateID == null) ? "base" : rec.stateID;
	    	// Loop over the random sets we've got
	        for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(id, setidx);
        		so.addVariant("", var, (rec.stateID == null) ? null : Collections.singleton(rec.stateID));
	        }
    	}
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
    	for (int idx = 0; idx < def.states.size(); idx++) {
    		WesterosBlockStateRecord rec = def.states.get(idx);
			String id = (rec.stateID == null) ? "base" : rec.stateID;
	    	// Loop over the random sets we've got
	        for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
	        	WesterosBlockDef.RandomTextureSet set = rec.getRandomTextureSet(setidx);
	        	ModelObjectCrop mod = new ModelObjectCrop();
	        	mod.textures.crop = getTextureID(set.getTextureByIndex(0)); 
	        	this.writeBlockModelFile(getModelName(id, setidx), mod);
	        }
    	}
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = getTextureID(def.states.get(0).getTextureByIndex(0)); 
        this.writeItemModelFile(def.blockName, mo);
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
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
