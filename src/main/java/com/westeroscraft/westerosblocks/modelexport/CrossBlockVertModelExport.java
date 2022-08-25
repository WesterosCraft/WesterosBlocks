package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;

public class CrossBlockVertModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectCross {
        public String parent = "minecraft:block/cross";    // Use 'cross' model for single texture
        public TextureCross textures = new TextureCross();
    }
    public static class TextureCross {
        public String cross;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }

	static final String up[] = { "false", "false", "true", "true" };
	static final String down[] = { "false", "true", "false", "true" };
	static final String ext[] = { "base", "down", "up", "both" };

    public CrossBlockVertModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
    	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
        
		for (int i = 0; i < ext.length; i++) {
	    	// Loop over the random sets we've got
	        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
	        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
	        	for (int rot = 0; rot < cnt; rot++) {
	        		Variant var = new Variant();
	        		var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext[i], setidx);
	        		if (def.isCustomModel())
	        			var.model = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext[i], setidx);
	        		if (rot > 0) var.y = (90 * rot);
	                so.addVariant("up=" + up[i] + ",down=" + down[i], var, set.condIDs);
	        	}
	        }
		}
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Export if not set to custom model
        if (!def.isCustomModel()) {
    		for (int i = 0; i < ext.length; i++) {
    	        List<Variant> varn = new ArrayList<Variant>();
    	    	// Loop over the random sets we've got
    	        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
    	        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
    	            ModelObjectCross mod = new ModelObjectCross();
    	            if (isTinted) {
    	                mod.parent = "minecraft:block/tinted_cross";
    	            }
    	            mod.textures.cross = getTextureID(set.getTextureByIndex(i)); 
    	        	this.writeBlockModelFile(getModelName(ext[i], setidx), mod);
    	        }
    		}        	
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        if (!def.isCustomModel())
        	mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
        else
        	mo.parent = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName("base", 0);        	
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
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") (need vert CTM handler)");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	newstate.put("up", "false");
    	newstate.put("down", "false");
    	if (def.condStates != null) {
    		newstate.put("cond", def.getDefaultCondID());    		
    	}
    	newstate.put("waterlogged", "false");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
