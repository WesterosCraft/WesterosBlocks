package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;

public class HalfDoorBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectTop {
        public String parent = "westerosblocks:block/untinted/half_door";
        public Texture textures = new Texture();
        public Display display = new Display();
    }
    public static class ModelObjectTopRH {
        public String parent = "westerosblocks:block/untinted/half_door_rh";
        public Texture textures = new Texture();
    }
    public static class Display {
    	public DisplayInst gui = new DisplayInst(30, 225, 0, 0, 0.625);
    	public DisplayInst ground = new DisplayInst(0, 0, 0, 3, 0.25);
    	public DisplayInst fixed = new DisplayInst(0, 0, 0, 0, 0.5);
    	public DisplayInst thirdperson_righthand = new DisplayInst(75, 45, 0, 2.5, 0.375);
    	public DisplayInst firstperson_righthand = new DisplayInst(0, 45, 0, 0, 0.4);
    	public DisplayInst firstperson_lefthand = new DisplayInst(0, 225, 0, 0, 0.4);
    }
    public static class DisplayInst {
        public int[] rotation;
        public double[] translation;
        public double[] scale;
        public DisplayInst(int rotx, int roty, int rotz, double transy, double scale) {
        	rotation = new int[] { rotx, roty, rotz };
        	translation = new double[] { 0, transy, 0 };
        	this.scale = new double[] { scale, scale, scale };
        }
    }

    public static class Texture {
        public String top;
        public String bottom;
    }
    public static class ModelObject {
        public String parent;
    }

    public HalfDoorBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
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
        new ModelRec("facing=east,hinge=left,open=false", "base", 0),
        new ModelRec("facing=south,hinge=left,open=false", "base", 90),
        new ModelRec("facing=west,hinge=left,open=false", "base", 180),
        new ModelRec("facing=north,hinge=left,open=false", "base", 270),
        new ModelRec("facing=east,hinge=right,open=false", "rh", 0),
        new ModelRec("facing=south,hinge=right,open=false", "rh", 90),
        new ModelRec("facing=west,hinge=right,open=false", "rh", 180),
        new ModelRec("facing=north,hinge=right,open=false", "rh", 270),
        new ModelRec("facing=east,hinge=left,open=true", "rh", 90),
        new ModelRec("facing=south,hinge=left,open=true", "rh", 180),
        new ModelRec("facing=west,hinge=left,open=true", "rh", 270),
        new ModelRec("facing=north,hinge=left,open=true", "rh", 0),
        new ModelRec("facing=east,hinge=right,open=true", "base", 270),
        new ModelRec("facing=south,hinge=right,open=true", "base", 0),
        new ModelRec("facing=west,hinge=right,open=true", "base", 90),
        new ModelRec("facing=north,hinge=right,open=true", "base", 180)
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
        String uptxt = getTextureID(def.getTextureByIndex(0));
        // Top
        ModelObjectTop top = new ModelObjectTop();
        top.textures.top = uptxt;
        top.textures.bottom = uptxt;
        this.writeBlockModelFile(getModelName("base", 0), top);
        // Top RH
        ModelObjectTopRH trh = new ModelObjectTopRH();
        trh.textures.top = uptxt;
		trh.textures.bottom = uptxt;
        this.writeBlockModelFile(getModelName("rh", 0), trh);
        // Build simple item model that refers to base block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
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
    	newstate.put("powered", "false");
    	for (String facing : FACING) {
    		oldstate.put("facing", facing); 
        	newstate.put("facing", facing);
        	for (String hinge : LEFTRIGHT) {
            	oldstate.put("hinge", hinge);
            	newstate.put("hinge", hinge);
            	for (String open : BOOLEAN) {
                	oldstate.put("open", open);
                	newstate.put("open", open);
                    addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);                    		
            	}
        	}
    	}
    }
}

