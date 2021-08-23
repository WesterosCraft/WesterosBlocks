package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;

public class DoorBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectBottom {
        public String parent = "minecraft:block/door_bottom";    // Use 'door_bottom' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectBottomRH {
        public String parent = "minecraft:block/door_bottom_rh";    // Use 'door_bottom_rh' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectTop {
        public String parent = "minecraft:block/door_top";    // Use 'door_top' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectTopRH {
        public String parent = "minecraft:block/door_top_rh";    // Use 'door_top_rh' model
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String bottom, top;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
        public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }

    public DoorBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
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
        new ModelRec("facing=east,half=lower,hinge=left,open=false", "bottom", 0),
        new ModelRec("facing=south,half=lower,hinge=left,open=false", "bottom", 90),
        new ModelRec("facing=west,half=lower,hinge=left,open=false", "bottom", 180),
        new ModelRec("facing=north,half=lower,hinge=left,open=false", "bottom", 270),
        new ModelRec("facing=east,half=lower,hinge=right,open=false", "bottom_rh", 0),
        new ModelRec("facing=south,half=lower,hinge=right,open=false", "bottom_rh", 90),
        new ModelRec("facing=west,half=lower,hinge=right,open=false", "bottom_rh", 180),
        new ModelRec("facing=north,half=lower,hinge=right,open=false", "bottom_rh", 270),
        new ModelRec("facing=east,half=lower,hinge=left,open=true", "bottom_rh", 90),
        new ModelRec("facing=south,half=lower,hinge=left,open=true", "bottom_rh", 180),
        new ModelRec("facing=west,half=lower,hinge=left,open=true", "bottom_rh", 270),
        new ModelRec("facing=north,half=lower,hinge=left,open=true", "bottom_rh", 0),
        new ModelRec("facing=east,half=lower,hinge=right,open=true", "bottom", 270),
        new ModelRec("facing=south,half=lower,hinge=right,open=true", "bottom", 0),
        new ModelRec("facing=west,half=lower,hinge=right,open=true", "bottom", 90),
        new ModelRec("facing=north,half=lower,hinge=right,open=true", "bottom", 180),
        new ModelRec("facing=east,half=upper,hinge=left,open=false", "top", 0),
        new ModelRec("facing=south,half=upper,hinge=left,open=false", "top", 90),
        new ModelRec("facing=west,half=upper,hinge=left,open=false", "top", 180),
        new ModelRec("facing=north,half=upper,hinge=left,open=false", "top", 270),
        new ModelRec("facing=east,half=upper,hinge=right,open=false", "top_rh", 0),
        new ModelRec("facing=south,half=upper,hinge=right,open=false", "top_rh", 90),
        new ModelRec("facing=west,half=upper,hinge=right,open=false", "top_rh", 180),
        new ModelRec("facing=north,half=upper,hinge=right,open=false", "top_rh", 270),
        new ModelRec("facing=east,half=upper,hinge=left,open=true", "top_rh", 90),
        new ModelRec("facing=south,half=upper,hinge=left,open=true", "top_rh", 180),
        new ModelRec("facing=west,half=upper,hinge=left,open=true", "top_rh", 270),
        new ModelRec("facing=north,half=upper,hinge=left,open=true", "top_rh", 0),
        new ModelRec("facing=east,half=upper,hinge=right,open=true", "top", 270),
        new ModelRec("facing=south,half=upper,hinge=right,open=true", "top", 0),
        new ModelRec("facing=west,half=upper,hinge=right,open=true", "top", 90),
        new ModelRec("facing=north,half=upper,hinge=right,open=true", "top", 180)
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
        String downtxt = getTextureID(def.getTextureByIndex(1));
        String uptxt = getTextureID(def.getTextureByIndex(0));
        // Bottom
        ModelObjectBottom bot = new ModelObjectBottom();
        bot.textures.bottom = downtxt;
        bot.textures.top = uptxt;
        this.writeBlockModelFile(getModelName("bottom", 0), bot);
        // Bottom RH
        ModelObjectBottomRH brh = new ModelObjectBottomRH();
        brh.textures.bottom = downtxt;
        brh.textures.top = uptxt;
        this.writeBlockModelFile(getModelName("bottom_rh", 0), brh);
        // Top
        ModelObjectTop top = new ModelObjectTop();
        top.textures.bottom = downtxt;
        top.textures.top = uptxt;
        this.writeBlockModelFile(getModelName("top", 0), top);
        // Top RH
        ModelObjectTopRH trh = new ModelObjectTopRH();
        trh.textures.bottom = downtxt;
        trh.textures.top = uptxt;
        this.writeBlockModelFile(getModelName("top_rh", 0), trh);
        // Build simple item model that refers to base block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = uptxt;
        this.writeItemModelFile(def.blockName, mo);
    }
    
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") (needs filter)");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	// Split state - upper has HINGE and POWEED
    	oldstate.put("half", "upper");
    	newstate.put("half", "upper");
    	oldstate.put("facing", "north");
    	newstate.put("facing", "north");
    	oldstate.put("open", "false");
    	newstate.put("open", "false");
    	for (String hinge : LEFTRIGHT) {
        	oldstate.put("hinge", hinge);
        	newstate.put("hinge", hinge);
        	for (String powered : BOOLEAN) {
            	oldstate.put("powered", powered);
            	newstate.put("powered", powered);
                addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);                    		
        	}
    	}
    	// lower has FACING and OPEN
    	oldstate.put("half", "lower");
    	newstate.put("half", "lower");
    	oldstate.put("powered", "false");
    	newstate.put("powered", "false");
    	oldstate.put("hinge", "left");
    	newstate.put("hinge", "left");
    	for (String facing : FACING) {
    		oldstate.put("facing", facing); 
        	newstate.put("facing", facing);
        	for (String open : BOOLEAN) {
            	oldstate.put("open", open);
            	newstate.put("open", open);
                addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);                    		
        	}
    	}
    }
}

