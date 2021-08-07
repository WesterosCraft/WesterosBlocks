package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCLeavesBlock;

import net.minecraft.block.Block;

public class LeavesBlockModelExport extends ModelExport {
    private WesterosBlockDef def;
    private WCLeavesBlock blk;

    // Template objects for Gson export of block state
    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, List<Variant>> variants = new HashMap<String, List<Variant>>();
    }
    public static class StateObject1 {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer x;
        public Integer y;
        public Integer weight;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectLeaves {
        public String parent = "minecraft:block/block";    // Use 'block'
        public Texture textures = new Texture();
        public List<Elements> elements = new ArrayList<Elements>();
    }
    public static class Texture {
        public String end, side, branch, particle;
    }
    public static class Rotation {
    	float angle;
    	String axis = "y";
    	int origin[] = { 8, 8, 8 };
    	public Rotation(float angle) {
    		this.angle = angle;
    	}
    };
    
    public static class Elements {
    	int[] from = { 0, 0, 0 };
    	int[] to = { 16, 16, 16 };
    	Rotation rotation;
    	Map<String, Faces> faces = new HashMap<String, Faces>();
    }
    public static class Faces {
    	int[] uv = { 0, 0, 16, 16 };
    	String texture;
    	Integer tintindex;
    	String cullface;
    	public Faces(String txt, String face, boolean isTinted) {
    		texture = txt;
    		cullface = face;
    		tintindex = (isTinted?0:null);
    	}
    }
    // Template objects for Gson export of block models
    public static class ModelObjectBFLeaves {
        public String parent = "minecraft:block/block";    // Use 'block'
        public Texture textures = new Texture();
        public Elements elements[] = { new Elements() };
    }

    public static class ModelObject {
    	public String parent;
    }

    
    public LeavesBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        this.blk = (WCLeavesBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    private String getModelName(String ext, int setidx) {
    	return def.blockName + ext + ((setidx == 0)?"":("-v" + (setidx+1)));
    }

    @Override
    public void doBlockStateExport() throws IOException {
        List<Variant> vars = new ArrayList<Variant>();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	String model = WesterosBlocks.MOD_ID + ":block/" + getModelName("", setidx);
        	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
            for (int i = 0; i < cnt; i++) {
            	Variant var = new Variant();
            	var.model = model;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
            	vars.add(var);
            }
            if (blk.betterfoliage) {
                for (int i = 0; i < cnt; i++) {
                	Variant var = new Variant();
                	var.model = model;
                	var.weight = set.weight;
                	var.x = 180;
                	if (i > 0) var.y = 90*i;
                	vars.add(var);
                }            	
            }
        }
        if (vars.size() == 1) {
            StateObject1 so = new StateObject1();
            Variant v = vars.get(0);
            v.weight = null;
        	so.variants.put("", v);
            this.writeBlockStateFile(def.blockName, so);        	
        }
        else {
        	StateObject so = new StateObject();
        	so.variants.put("", vars);
        	this.writeBlockStateFile(def.blockName, so);
        }
    }
    
    private ModelObjectLeaves makeBaseModel(int setidx, boolean isTinted) {
    	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
    	
        ModelObjectLeaves mod = new ModelObjectLeaves();
        Elements base = new Elements();
        base.faces.put("down", new Faces("#end", "down", isTinted));
        base.faces.put("up", new Faces("#end", "up", isTinted));
        base.faces.put("north", new Faces("#side", "north", isTinted));
        base.faces.put("south", new Faces("#side", "south", isTinted));
        base.faces.put("west", new Faces("#side", "west", isTinted));
        base.faces.put("east", new Faces("#side", "east", isTinted));
        mod.elements.add(base);
        mod.textures.end = getTextureID(set.getTextureByIndex(0)); 
        mod.textures.side = getTextureID(set.getTextureByIndex(1)); 
        mod.textures.particle = mod.textures.side;
        return mod;
    }
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	
            ModelObjectLeaves mod = makeBaseModel(setidx, isTinted);
            // If better foliage, add branch elements and texture
            if (blk.betterfoliage) {
            	int from[][] = { { -11, -11, 5 }, { -11, -11, 7 }, { 10, -11, -11 }, { 12, -11, -11 } };
            	int to[][] = { { 27, 27, 5 }, { 27, 27, 7 }, { 10, 27, 27 }, { 12, 27, 27 } };
            	float angle[] = { -22.5f, 22.5f, -22.5f, 22.5f }; 
            	for (int i = 0; i < 4; i++) {
            		Elements cross = new Elements();
            		cross.from = from[i];
            		cross.to = to[i];
            		cross.rotation = new Rotation(angle[i]);
                    cross.faces.put("down", new Faces("#branch", "down", isTinted));
                    cross.faces.put("up", new Faces("#branch", "up", isTinted));
                    cross.faces.put("north", new Faces("#branch", "north", isTinted));
                    cross.faces.put("south", new Faces("#branch", "south", isTinted));
                    cross.faces.put("west", new Faces("#branch", "west", isTinted));
                    cross.faces.put("east", new Faces("#branch", "east", isTinted));      
                    mod.elements.add(cross);
            	}
            	mod.textures.branch = getTextureID(set.getTextureByIndex(2)); 
            }
            this.writeBlockModelFile(getModelName("", setidx), mod);
        }
        // Build base model for item
        ModelObjectLeaves mo = makeBaseModel(0, isTinted);
        this.writeItemModelFile(def.getBlockName(), mo);
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
            }
        }
    }

}
