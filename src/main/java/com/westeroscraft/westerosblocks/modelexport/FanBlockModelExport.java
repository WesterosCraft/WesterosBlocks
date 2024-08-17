package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;

public class FanBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectFan {
        public String parent = "westerosblocks:block/untinted/fan";
        public Texture textures = new Texture();
    }
    public static class ModelObjectWallFan {
        public String parent = "westerosblocks:block/untinted/wall_fan";
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String fan;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public Texture0 textures = new Texture0();
    }
    public static class Texture0 {
        public String layer0;
    }
    
    public FanBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();

        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);

            // Make floor block
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", setidx);
            var.weight = set.weight;
            so.addVariant("", var, null);
            this.writeBlockStateFile(def.blockName, so);

            // Make wall block
            so = new StateObject();
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", setidx);
            var.y = 90;
            so.addVariant("facing=east", var, null);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", setidx);
            var.y = 180;
            so.addVariant("facing=south", var, null);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", setidx);
            var.y = 270;
            so.addVariant("facing=west", var, null);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", setidx);
            var.y = 0;
            so.addVariant("facing=north", var, null);
            
            this.writeBlockStateFile("wall_" + def.blockName, so);
        }
    }

    protected void doFanModel(boolean isTinted, int setidx) throws IOException {
        WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        String txt = getTextureID(set.getTextureByIndex(0));
        ModelObjectFan mod = new ModelObjectFan();
        mod.parent = (isTinted) ? "westerosblocks:block/tinted/fan" : "westerosblocks:block/untinted/fan";
        mod.textures.fan = txt;
        this.writeBlockModelFile(getModelName("base", setidx), mod);
    }

    protected void doWallFanModel(boolean isTinted, int setidx) throws IOException {
        WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        String txt = getTextureID(set.getTextureByIndex(0));
        ModelObjectWallFan mod = new ModelObjectWallFan();
        mod.parent = (isTinted) ? "westerosblocks:block/tinted/wall_fan" : "westerosblocks:block/untinted/wall_fan";
        mod.textures.fan = txt;
        this.writeBlockModelFile(getModelName("wall", setidx), mod);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            doFanModel(isTinted, setidx);
            doWallFanModel(isTinted, setidx);
        }       
                
        // Build simple item model that refers to block model
        String txt = getTextureID(def.getRandomTextureSet(0).getTextureByIndex(0));
        ModelObjectFan mod = new ModelObjectFan();
        mod.parent = (isTinted) ? "westerosblocks:block/tinted/fan" : "westerosblocks:block/untinted/fan";
        mod.textures.fan = txt;
        this.writeItemModelFile(def.blockName, mod);
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
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	oldstate.put("facing", "north");
    	newstate.put("facing", "north");
        addWorldConverterRecord(oldID, oldstate, "wall_" + def.getBlockName(), newstate);
    	oldstate.put("facing", "south");
    	newstate.put("facing", "south");
        addWorldConverterRecord(oldID, oldstate, "wall_" + def.getBlockName(), newstate);
    	oldstate.put("facing", "east");
    	newstate.put("facing", "east");
        addWorldConverterRecord(oldID, oldstate, "wall_" + def.getBlockName(), newstate);
    	oldstate.put("facing", "west");
    	newstate.put("facing", "west");
        addWorldConverterRecord(oldID, oldstate, "wall_" + def.getBlockName(), newstate);
    	oldstate.put("facing", "up");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), null);
    }
}
