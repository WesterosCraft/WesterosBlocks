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

public class LogBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectCubeAll {
        public String parent = "minecraft:block/cube_all";    // Use 'cube_all' model for single texture
        public TextureAll textures = new TextureAll();
    }
    public static class TextureAll {
        public String all;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectColumn {
        public String parent;    // Use hacked 'cube_column' model (weirwood behaviors...)
        public Texture textures = new Texture();
        public ModelObjectColumn(boolean isTinted) {
        	if (isTinted) {
        		this.parent = "westerosblocks:block/tinted/cube_log";
        	}
        	else {
        		this.parent = "westerosblocks:block/untinted/cube_log";;
        	}
        }
    }
    public static class Texture {
        public String down, up, north, south, east, west, particle;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public LogBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    private static final String[] states = { "axis=x", "axis=y", "axis=z" };
    private static final int[] xrot = { 90, 0, 90 };
    private static final int[] yrot = { 90, 0, 0 };
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        
        for (int i = 0; i < states.length; i++) {
        	List<Variant> vars = new ArrayList<Variant>();
        	// Loop over the random sets we've got
        	for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        		WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        		Variant var = new Variant();
        		var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", setidx);
        		if (xrot[i] > 0) var.x = xrot[i];
        		if (yrot[i] > 0) var.y = yrot[i];
        		var.weight = set.weight;
        		so.addVariant(states[i], var, set.condIDs);
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
            ModelObjectColumn mod = new ModelObjectColumn(isTinted);
    		mod.textures.down = getTextureID(set.getTextureByIndex(0));
    		mod.textures.up = getTextureID(set.getTextureByIndex(1));
    		mod.textures.north = getTextureID(set.getTextureByIndex(2));
    		mod.textures.south = getTextureID(set.getTextureByIndex(3));
    		mod.textures.west = getTextureID(set.getTextureByIndex(4));
    		mod.textures.east = getTextureID(set.getTextureByIndex(5));
    		mod.textures.particle = getTextureID(set.getTextureByIndex(2));
            this.writeBlockModelFile(getModelName("base", setidx), mod);
    	}
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
        this.writeItemModelFile(def.blockName, mo);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	for (String axis : AXIS) {
    		oldstate.put("axis", axis);
    		newstate.put("axis", axis);
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
		oldstate.put("axis", "none");
		newstate.put("axis", "y");
		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
