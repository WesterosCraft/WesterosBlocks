package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class CrossBlockModelExport extends ModelExport {
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
    
    public CrossBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
    	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
        StateObject so = new StateObject();
        
        List<Variant> varn = new ArrayList<Variant>();
    	// Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	for (int rot = 0; rot < cnt; rot++) {
        		Variant var = new Variant();
        		var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", setidx);
            	var.weight = set.weight;
        		if (def.isCustomModel())
        			var.model = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName("base", setidx);
        		if (rot > 0) var.y = (90 * rot);
        		so.addVariant("", var, set.condIDs);
        	}
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
    	boolean isTinted = def.isTinted();
    	// Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	ModelObjectCross mod = new ModelObjectCross();
        	mod.textures.cross = getTextureID(set.getTextureByIndex(0)); 
        	// Use tinted cross if 
        	if (isTinted) {
        		mod.parent = "minecraft:block/tinted_cross";
        	}
        	this.writeBlockModelFile(getModelName("base", setidx), mod);
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = getTextureID(def.getRandomTextureSet(0).getTextureByIndex(0));
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
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), null);
    }

}
