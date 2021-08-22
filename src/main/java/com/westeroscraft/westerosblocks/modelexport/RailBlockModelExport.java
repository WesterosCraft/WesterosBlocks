package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class RailBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectRailFlat {
        public String parent = "minecraft:block/rail_flat";    // Use 'rail_flst' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailCurved {
        public String parent = "minecraft:block/rail_curved";    // Use 'rail_curved' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailRaisedNE {
        public String parent = "minecraft:block/rail_raised_ne";    // Use 'rail_raised_ne' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailRaisedSW {
        public String parent = "minecraft:block/rail_raised_sw";    // Use 'rail_raised_sw' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String rail;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public Texture0 textures = new Texture0();
    }
    public static class Texture0 {
        public String layer0;
    }
    
    public RailBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    private static class ModelRec {
    	String cond;
    	String ext;
    	int y;
    	ModelRec(String cond, String ext, int y) {
    		this.cond = cond; this.ext = ext; this.y = y;
    	}
    };
    private static final ModelRec[] MODELS = {
    	new ModelRec("shape=north_south", "flat", 0),
        new ModelRec("shape=east_west", "flat", 90),
        new ModelRec("shape=ascending_east", "raised_ne", 90),
        new ModelRec("shape=ascending_west", "raised_sw", 90),
        new ModelRec("shape=ascending_north", "raised_ne", 0),
        new ModelRec("shape=ascending_south", "raised_sw", 0),
        new ModelRec("shape=south_east", "curved", 0),
        new ModelRec("shape=south_west", "curved", 90),
        new ModelRec("shape=north_west", "curved", 180),
        new ModelRec("shape=north_east", "curved", 270)
    };
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (ModelRec rec : MODELS) {
        	Variant var = new Variant();
        	var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(rec.ext, 0);
        	if (rec.y != 0) var.y = rec.y;
        	so.addVariant(rec.cond, var, null);
        }
        
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        String txt_norm = getTextureID(def.getTextureByIndex(0));
        String txt_curved = getTextureID(def.getTextureByIndex(1));
        
        ModelObjectRailFlat mod = new ModelObjectRailFlat();
        mod.textures.rail = txt_norm; 
        this.writeBlockModelFile(getModelName("flat", 0), mod);
        
        ModelObjectRailCurved modc = new ModelObjectRailCurved();
        modc.textures.rail = txt_curved; 
        this.writeBlockModelFile(getModelName("curved", 0), modc);
        
        ModelObjectRailRaisedNE modne = new ModelObjectRailRaisedNE();
        modne.textures.rail = txt_norm;
        this.writeBlockModelFile(getModelName("raised_ne", 0), modne);

        ModelObjectRailRaisedSW modsw = new ModelObjectRailRaisedSW();
        modsw.textures.rail = txt_norm;
        this.writeBlockModelFile(getModelName("raised_sw", 0), modsw);

        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = txt_norm;
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
    	for (String railshape : RAILSHAPE) {
    		oldstate.put("shape", railshape);
    		newstate.put("shape", railshape);
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
