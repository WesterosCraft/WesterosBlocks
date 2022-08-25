package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;

public class TrapDoorBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectTrapdoorBottom {
        public String parent = "minecraft:block/template_trapdoor_bottom";    // Use 'trapdoor_bottom' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectTrapdoorTop {
        public String parent = "minecraft:block/template_trapdoor_top";    // Use 'trapdoor_top' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectTrapdoorOpen {
        public String parent = "minecraft:block/template_trapdoor_open";    // Use 'trapdoor_open' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String texture;
    }
    public static class ModelObject {
    	public String parent;
    }

    
    public TrapDoorBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    private static class ModelRec {
    	String cond;
    	String ext;
    	int y;
    	ModelRec(String c, String e, int y) {
    		cond = c; ext = e; this.y = y;
    	}
    };
    private static final ModelRec[] MODELS = {
        new ModelRec("facing=north,half=bottom,open=false", "bottom", 0),
        new ModelRec("facing=south,half=bottom,open=false", "bottom", 0),
        new ModelRec("facing=east,half=bottom,open=false", "bottom", 0),
        new ModelRec("facing=west,half=bottom,open=false", "bottom", 0),
        new ModelRec("facing=north,half=top,open=false", "top", 0),
        new ModelRec("facing=south,half=top,open=false", "top", 0),
        new ModelRec("facing=east,half=top,open=false", "top", 0),
        new ModelRec("facing=west,half=top,open=false", "top", 0),
        new ModelRec("facing=north,half=bottom,open=true", "open", 0),
        new ModelRec("facing=south,half=bottom,open=true", "open", 180),
        new ModelRec("facing=east,half=bottom,open=true", "open", 90),
        new ModelRec("facing=west,half=bottom,open=true", "open", 270),
        new ModelRec("facing=north,half=top,open=true", "open", 0),
        new ModelRec("facing=south,half=top,open=true", "open", 180),
        new ModelRec("facing=east,half=top,open=true", "open", 90),
        new ModelRec("facing=west,half=top,open=true", "open", 270)
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
        String txt = getTextureID(def.getTextureByIndex(0));
        ModelObjectTrapdoorBottom modb = new ModelObjectTrapdoorBottom();
        modb.textures.texture = txt; 
        this.writeBlockModelFile(getModelName("bottom", 0), modb);
        ModelObjectTrapdoorTop modt = new ModelObjectTrapdoorTop();
        modt.textures.texture = txt; 
        this.writeBlockModelFile(getModelName("top", 0), modt);
        ModelObjectTrapdoorOpen modo = new ModelObjectTrapdoorOpen();
        modo.textures.texture = txt; 
        this.writeBlockModelFile(getModelName("open", 0), modo);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("bottom", 0);
        this.writeItemModelFile(def.blockName, mo);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	newstate.put("powered", "false");
    	newstate.put("waterlogged", "false");
    	for (String facing : FACING) {
    		oldstate.put("facing", facing); 
        	newstate.put("facing", facing);
        	for (String open : BOOLEAN) {
            	oldstate.put("open", open);
            	newstate.put("open", open);
            	for (String half : TOPBOTTOM) {
                	oldstate.put("half", half);
                	newstate.put("half", half);
                	addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);            		
            	}
        	}
    	}
    }

}
