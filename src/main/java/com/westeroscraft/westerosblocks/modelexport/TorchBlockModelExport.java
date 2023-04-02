package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;

public class TorchBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectTorch {
        public String parent = "minecraft:block/template_torch";    // Use 'torch' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectTorchWall {
        public String parent = "minecraft:block/template_torch_wall";    // Use 'torch_wall' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String torch;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public Texture0 textures = new Texture0();
    }
    public static class Texture0 {
        public String layer0;
    }
    
    public TorchBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        
        Variant var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
        so.addVariant("", var, null);

        this.writeBlockStateFile(def.blockName, so);

        // Make wall block too
        so = new StateObject();

        var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", 0);
        var.y = 0;
        so.addVariant("facing=east", var, null);
        var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", 0);
        var.y = 90;
        so.addVariant("facing=south", var, null);
        var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", 0);
        var.y = 180;
        so.addVariant("facing=west", var, null);
        var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("wall", 0);
        var.y = 270;
        so.addVariant("facing=north", var, null);
        
        this.writeBlockStateFile("wall_" + def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        String txt = getTextureID(def.getTextureByIndex(0));
        ModelObjectTorch mod = new ModelObjectTorch();
        mod.textures.torch = txt; 
        this.writeBlockModelFile(getModelName("base", 0), mod);
        // Build wall block model
        ModelObjectTorchWall modw = new ModelObjectTorchWall();
        modw.textures.torch = txt; 
        this.writeBlockModelFile(getModelName("wall", 0), modw);        
                
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = txt;
        this.writeItemModelFile(def.blockName, mo);
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
