package com.westerosblocks.needsported.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCCuboidBlock;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;

import net.minecraft.world.level.block.Block;

public class CuboidBlockModelExport extends ModelExport {
    protected WCCuboidBlock blk;
    
    // Template objects for Gson export of block models
    public static class ModelObjectCuboid {
        public String parent = "block/block";    // Use 'block' model for single texture
        public Boolean ambientocclusion;
        public Display display = new Display();
        public Map<String, String> textures = new HashMap<String, String>();
        public List<Element> elements = new ArrayList<Element>();
    }
    public static class Display {
        public int[] rotation = { 0, 90, 0 };
        public int[] translation = { 0, 0, 0 };
        public double[] scale = { 0.5, 0.5, 0.5 };
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
        public Rotation() { }
        public Rotation(float angle) {
        	this.angle = angle;
        	rescale = false;
        }
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
    
    public CuboidBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.blk = (WCCuboidBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    public String modelFileName(String ext, int setidx, boolean isCustom) {
    	if (isCustom)
    		return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx);
    	else
    		return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx);
    }

    @Override
    public void doBlockStateExport() throws IOException {
    	StateObject so = new StateObject();
    	
    	for (WesterosBlockStateRecord sr : def.states) {
    		boolean justBase = sr.stateID == null;
    		Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
    		String fname = justBase ? "base" : sr.stateID;
	        // Loop over the random sets we've got
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
	        	int cnt = sr.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
	            for (int i = 0; i < cnt; i++) {
	                Variant var = new Variant();
	                var.model = modelFileName(fname, setidx, sr.isCustomModel());
	            	var.weight = set.weight;
	            	int rot = (90*i + sr.rotYOffset) % 360;
	            	if (rot > 0) var.y = rot;
	    			so.addVariant("", var, stateIDs);	// Add our variant                	
	            }
	        }
    	}
    	this.writeBlockStateFile(def.blockName, so);        	
    }

    private static float getClamped(float v) {
        v = 16F * v;
        if (v < -16f) v = -16f;
        if (v > 32f) v = 32f;
        return v;
    }
    
    private static void processRotation(Face f) {
    	if (f.rotation != null) {
    		if ((f.rotation == 90) || (f.rotation == 270)) {
    			float vt;
    			vt = f.uv[1];
    			f.uv[1] = f.uv[0];
    			f.uv[0] = vt;
    			vt = f.uv[3];
    			f.uv[3] = f.uv[2];
    			f.uv[2] = vt;
    		}
    	}
    }

    protected void doCuboidModel(String name, boolean isTinted, int setidx, Float modelRotation, WesterosBlockStateRecord sr, int sridx) throws IOException {
        ModelObjectCuboid mod = new ModelObjectCuboid();
    	WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
        String txt0 = set.getTextureByIndex(0);
        mod.textures.put("particle", getTextureID(set.getTextureByIndex(0)));
        int cnt = Math.max(6, set.getTextureCount());
        String[] textures = new String[cnt];
        for (int i = 0; i < cnt; i++) {
        	textures[i] = set.getTextureByIndex(i);
        }
        doCuboidModel(name, isTinted, txt0, textures, blk.getModelCuboids(sridx), modelRotation);
    }
    
    protected void doCuboidModel(String name, boolean isTinted, String txt0, String[] textures, List<Cuboid> cubs,
    	Float modelRotate) throws IOException {
        ModelObjectCuboid mod = new ModelObjectCuboid();
        mod.textures.put("particle", getTextureID(txt0));
        for (int i = 0; i < textures.length; i++) {
        	if (isTransparentTexture(textures[i])) continue;
            mod.textures.put("txt" + i, getTextureID(textures[i]));
        }
        for (Cuboid c : cubs) { 
            Face f;
            Element elem;
            int[] sidetxt = c.sideTextures;
            if (sidetxt == null) {  // If not mapped, use index=side
                sidetxt = STDTXTIDX;
            }
            boolean[] noTint = c.noTint;
            if (noTint == null) {
            	noTint = NOTINTALL;
            }
            int[] siderot = c.sideRotations;
            if (siderot == null) {
            	siderot = new int[] { 0, 0, 0, 0, 0, 0 };
            }
            float xmin = getClamped(c.xMin);
            float ymin = getClamped(c.yMin);
            float zmin = getClamped(c.zMin);
            float xmax = getClamped(c.xMax);
            float ymax = getClamped(c.yMax);
            float zmax = getClamped(c.zMax);
            
            if (WesterosBlockDef.SHAPE_CROSSED.equals(c.shape)) {
                mod.ambientocclusion = false;
                // First stroke of X
                elem = new Element();
                elem.from[0] = xmin;
                elem.from[1] = ymin;
                elem.from[2] = 8;
                elem.from[0] = xmax;
                elem.from[1] = ymax;
                elem.from[2] = 8;
                elem.rotation = new Rotation();
                elem.shade = false;
                if (mod.textures.containsKey("txt0")) {
	                f = new Face();
	                f.uv = new float[] { 0, 0, 16, 16 };
	                f.texture = "#txt0";
	                if (isTinted && (!noTint[0])) f.tintindex = 0;
	                elem.faces.put("north", f);
	                elem.faces.put("south", f);
	                mod.elements.add(elem);
                }
                // Second stroke of X
                elem = new Element();
                elem.from[0] = 8;
                elem.from[1] = ymin;
                elem.from[2] = zmin;
                elem.from[0] = 8;
                elem.from[1] = ymax;
                elem.from[2] = zmax;
                elem.rotation = (modelRotate == null) ? new Rotation() : new Rotation(modelRotate);
                elem.shade = false;
                if (mod.textures.containsKey("txt0")) {
	                f = new Face();
	                f.uv = new float[] { 0, 0, 16, 16 };
	                f.texture = "#txt0";
	                if (isTinted && (!noTint[0])) f.tintindex = 0;
	                elem.faces.put("west", f);
	                elem.faces.put("east", f);
	                mod.elements.add(elem);
                }
            }
            else {
                // Handle normal cuboid
                elem = new Element();
                elem.from[0] = xmin;
                elem.from[1] = ymin;
                elem.from[2] = zmin;
                elem.to[0] = xmax;
                elem.to[1] = ymax;
                elem.to[2] = zmax;
                if (modelRotate != null) {
                	elem.rotation = new Rotation(modelRotate);
                }
                // Add down face
                String txtid;
                txtid = "txt" + sidetxt[0];
                if (mod.textures.containsKey(txtid)) {
	                f = new Face();
	                f.uv[0] = xmin;
	                f.uv[2] = xmax;
	                f.uv[1] = 16-zmax;
	                f.uv[3] = 16-zmin;
	                f.texture = "#" + txtid;
	                f.rotation = (siderot[0] != 0) ? siderot[0] : null;
	                processRotation(f);
	                if (isTinted && (!noTint[0])) f.tintindex = 0;
	                if (elem.from[1] <= 0) f.cullface = "down";
	                elem.faces.put("down", f);
                }
                // Add up face
                txtid = "txt" + sidetxt[1];
                if (mod.textures.containsKey(txtid)) {
	                f = new Face();
	                f.uv[0] = xmin;
	                f.uv[2] = xmax;
	                f.uv[1] = zmin;
	                f.uv[3] = zmax;
	                f.texture = "#" + txtid;
	                f.rotation = (siderot[1] != 0) ? siderot[1] : null;
	                processRotation(f);
	                if (isTinted && (!noTint[1])) f.tintindex = 0;
	                if (elem.to[1] >= 16) f.cullface = "up";
	                elem.faces.put("up", f);
                }
                // Add north face
                txtid = "txt" + sidetxt[2];
                if (mod.textures.containsKey(txtid)) {
	                f = new Face();
	                f.uv[0] = 16-xmax;
	                f.uv[2] = 16-xmin;
	                f.uv[1] = 16-ymax;
	                f.uv[3] = 16-ymin;
	                f.texture = "#" + txtid;
	                f.rotation = (siderot[2] != 0) ? siderot[2] : null;
	                processRotation(f);
	                if (isTinted && (!noTint[2])) f.tintindex = 0;
	                if (elem.from[2] <= 0) f.cullface = "north";
	                elem.faces.put("north", f);
                }
                // Add south face
                txtid = "txt" + sidetxt[3];
                if (mod.textures.containsKey(txtid)) {
	                f = new Face();
	                f.uv[0] = xmin;
	                f.uv[2] = xmax;
	                f.uv[1] = 16-ymax;
	                f.uv[3] = 16-ymin;
	                f.texture = "#" + txtid;
	                f.rotation = (siderot[3] != 0) ? siderot[3] : null;
	                processRotation(f);
	                if (isTinted && (!noTint[3])) f.tintindex = 0;
	                if (elem.to[2] >= 16) f.cullface = "south";
	                elem.faces.put("south", f);
                }
                // Add west face
                txtid = "txt" + sidetxt[4];
                if (mod.textures.containsKey(txtid)) {
	                f = new Face();
	                f.uv[0] = zmin;
	                f.uv[2] = zmax;
	                f.uv[1] = 16-ymax;
	                f.uv[3] = 16-ymin;
	                f.texture = "#" + txtid;
	                f.rotation = (siderot[4] != 0) ? siderot[4] : null;
	                processRotation(f);
	                if (isTinted && (!noTint[4])) f.tintindex = 0;
	                if (elem.from[0] <= 0) f.cullface = "west";
	                elem.faces.put("west", f);
                }
                // Add east face
                txtid = "txt" + sidetxt[5];
                if (mod.textures.containsKey(txtid)) {
	                f = new Face();
	                f.uv[0] = 16-zmax;
	                f.uv[2] = 16-zmin;
	                f.uv[1] = 16-ymax;
	                f.uv[3] = 16-ymin;
	                f.texture = "#" + txtid;
	                f.rotation = (siderot[5] != 0) ? siderot[5] : null;
	                processRotation(f);
	                if (isTinted && (!noTint[5])) f.tintindex = 0;
	                if (elem.to[0] >= 16) f.cullface = "east";
	                elem.faces.put("east", f);
                }
                mod.elements.add(elem);
            }
        }
        this.writeBlockModelFile(name, mod);
    }
    private static final int[] STDTXTIDX = { 0, 1, 2, 3, 4, 5 };
    private static final boolean[] NOTINTALL = { false, false, false, false, false, false };
    @Override
    public void doModelExports() throws IOException {
    	//WesterosBlocks.log.info(String.format("%s: size=%d", def.blockName, def.states.size()));
    	for (int idx = 0; idx < def.states.size(); idx++) {
    		WesterosBlockStateRecord rec = def.states.get(idx);
            // Export if not set to custom model
            if (rec.isCustomModel()) continue;
        	String id = (rec.stateID == null) ? "base" : rec.stateID;
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
            	doCuboidModel(getModelName(id, setidx), rec.isTinted(), setidx, null, rec, idx);
            }
    	}
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        WesterosBlockStateRecord sr0 = def.states.get(0);
        boolean isTinted = sr0.isTinted();
    	String id = (sr0.stateID == null) ? "base" : sr0.stateID;
        mo.parent = modelFileName(id, 0, sr0.isCustomModel());
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
    	newstate.put("waterlogged", "false");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
