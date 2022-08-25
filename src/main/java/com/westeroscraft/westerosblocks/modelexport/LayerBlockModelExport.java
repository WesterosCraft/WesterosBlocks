package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCLayerBlock;

import net.minecraft.world.level.block.Block;

public class LayerBlockModelExport extends ModelExport {
    protected WCLayerBlock blk;
    
    // Template objects for Gson export of block models
    public static class ModelObjectCuboid {
        public String parent = "minecraft:block/thin_block";    // Use 'thin_block' model for single texture
        public Map<String, String> textures = new HashMap<String, String>();
        public List<Element> elements = new ArrayList<Element>();
    }
    public static class Element {
        public float[] from = { 0, 0, 0 };
        public float[] to = { 16, 16, 16 };
        public Rotation rotation;
        public Boolean shade;
        public Map<String, Face> faces = new HashMap<String, Face>();
    }
    public static class Rotation {
        public float[] origin = { 8, 8, 8 };
        public String axis = "y";
        public float angle = 45;
        public Boolean rescale = true;
    }
    public static class Face {
        public float[] uv = { 0, 0, 16, 16 };
        public String texture;
        public Integer rotation;
        public String cullface;
        public Integer tintindex;
    }
    public static class ModelObject {
    	public String parent;
    }

    public LayerBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.blk = (WCLayerBlock) blk;

        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        
        for (int i = 0; i < this.blk.layerCount; i++) {
        	List<Variant> vars = new ArrayList<Variant>();
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	Variant var = new Variant();
            	var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("layer" + (i+1), setidx);
            	so.addVariant(String.format("layers=%d", i+1), var, set.condIDs);
            }
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean is_tinted = def.isTinted();
        for (int i = 0; i < blk.layerCount; i++) {
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	ModelObjectCuboid mod = new ModelObjectCuboid();
            	mod.textures.put("particle", getTextureID(set.getTextureByIndex(0)));
            	int cnt = Math.max(6, set.getTextureCount());
            	for (int j = 0; j < cnt; j++) {
            		mod.textures.put("txt" + j, getTextureID(set.getTextureByIndex(j)));
            	}
            	float ymax = (float)((16.0 / blk.layerCount) * (i+1));
            	// Handle normal cuboid
	            Element elem = new Element();
	            elem.from[0] = 0;
	            elem.from[1] = 0;
	            elem.from[2] = 0;
	            elem.to[0] = 16;
	            elem.to[1] = ymax;
	            elem.to[2] = 16;
	            // Add down face
	            Face f = new Face();
	            f.uv[0] = 0;
	            f.uv[2] = 16;
	            f.uv[1] = 0;
	            f.uv[3] = 16;
	            f.texture = "#txt0";
	            f.cullface = "down";
	            if (is_tinted) f.tintindex = 0;
	            elem.faces.put("down", f);
	            // Add up face
	            f = new Face();
	            f.uv[0] = 0;
	            f.uv[2] = 16;
	            f.uv[1] = 0;
	            f.uv[3] = 16;
	            f.texture = "#txt1";
	            if (elem.to[1] >= 16) f.cullface = "up";
	            if (is_tinted) f.tintindex = 0;
	            elem.faces.put("up", f);
	            // Add north face
	            f = new Face();
	            f.uv[0] = 0;
	            f.uv[2] = 16;
	            f.uv[1] = 16-ymax;
	            f.uv[3] = 16;
	            f.texture = "#txt2";
	            f.cullface = "north";
	            if (is_tinted) f.tintindex = 0;
	            elem.faces.put("north", f);
	            // Add south face
	            f = new Face();
	            f.uv[0] = 0;
	            f.uv[2] = 16;
	            f.uv[1] = 16-ymax;
	            f.uv[3] = 16;
	            f.texture = "#txt3";
	            f.cullface = "south";
	            if (is_tinted) f.tintindex = 0;
	            elem.faces.put("south", f);
	            // Add west face
	            f = new Face();
	            f.uv[0] = 0;
	            f.uv[2] = 16;
	            f.uv[1] = 16-ymax;
	            f.uv[3] = 16;
	            f.texture = "#txt4";
	            f.cullface = "west";
	            if (is_tinted) f.tintindex = 0;
	            elem.faces.put("west", f);
	            // Add eath face
	            f = new Face();
	            f.uv[0] = 0;
	            f.uv[2] = 16;
	            f.uv[1] = 16-ymax;
	            f.uv[3] = 16;
	            f.texture = "#txt5";
	            f.cullface = "east";
	            if (is_tinted) f.tintindex = 0;
	            elem.faces.put("east", f);
	            mod.elements.add(elem);
	            this.writeBlockModelFile(getModelName("layer" + (i+1), setidx), mod);
            }
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("layer1", 0);
        this.writeItemModelFile(def.blockName, mo);
        // Handle tint resources
        if (is_tinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, null, tintres);
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
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	newstate.put("waterlogged", "false");
        for (int i = 1; i <= blk.layerCount; i++) {
        	oldstate.put("layers", Integer.toString(i));
        	newstate.put("layers", Integer.toString(i));
        	addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
        }
    }

}
