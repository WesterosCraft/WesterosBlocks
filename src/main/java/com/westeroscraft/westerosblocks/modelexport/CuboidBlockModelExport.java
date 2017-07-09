package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCCuboidBlock;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class CuboidBlockModelExport extends ModelExport {
    protected WesterosBlockDef def;
    protected WCCuboidBlock blk;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer x;
        public Integer y;
        public Boolean uvlock;
    }
    
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
        this.def = def;
        this.blk = (WCCuboidBlock) blk;
        for (Subblock sb : def.subBlocks) {
            addNLSString("tile." + def.blockName + "_" + sb.meta + ".name", sb.label);
        }
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            so.variants.put(String.format("variant=%d", sb.meta), var);
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
     
    protected void doCuboidModel(Subblock sb, int meta, int modelmeta, String name, boolean isTinted) throws IOException {
        ModelObjectCuboid mod = new ModelObjectCuboid();
        mod.textures.put("particle", getTextureID(sb.getTextureByIndex(0)));
        int cnt = Math.max(6, sb.textures.size());
        for (int i = 0; i < cnt; i++) {
            mod.textures.put("txt" + i, getTextureID(sb.getTextureByIndex(i)));
        }
        List<Cuboid> cubs = blk.getCuboidList(modelmeta);
        for (Cuboid c : cubs) { 
            Face f;
            Element elem;
            int[] sidetxt = c.sideTextures;
            if (sidetxt == null) {  // If not mapped, use index=side
                sidetxt = STDTXTIDX;
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
                f = new Face();
                f.uv = new float[] { 0, 0, 16, 16 };
                f.texture = "#txt0";
                if (isTinted) f.tintindex = 0;
                elem.faces.put("north", f);
                elem.faces.put("south", f);
                mod.elements.add(elem);
                // Second stroke of X
                elem = new Element();
                elem.from[0] = 8;
                elem.from[1] = ymin;
                elem.from[2] = zmin;
                elem.from[0] = 8;
                elem.from[1] = ymax;
                elem.from[2] = zmax;
                elem.rotation = new Rotation();
                elem.shade = false;
                f = new Face();
                f.uv = new float[] { 0, 0, 16, 16 };
                f.texture = "#txt0";
                if (isTinted) f.tintindex = 0;
                elem.faces.put("west", f);
                elem.faces.put("east", f);
                mod.elements.add(elem);
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
                // Add down face
                f = new Face();
                f.uv[0] = xmin;
                f.uv[2] = xmax;
                f.uv[1] = 16-zmax;
                f.uv[3] = 16-zmin;
                f.texture = "#txt" + sidetxt[0];
                f.rotation = (siderot[0] != 0) ? siderot[0] : null;
                processRotation(f);
                if (isTinted) f.tintindex = 0;
                if (elem.from[1] <= 0) f.cullface = "down";
                elem.faces.put("down", f);
                // Add up face
                f = new Face();
                f.uv[0] = xmin;
                f.uv[2] = xmax;
                f.uv[1] = zmin;
                f.uv[3] = zmax;
                f.texture = "#txt" + sidetxt[1];
                f.rotation = (siderot[1] != 0) ? siderot[1] : null;
                processRotation(f);
                if (isTinted) f.tintindex = 0;
                if (elem.to[1] >= 16) f.cullface = "up";
                elem.faces.put("up", f);
                // Add north face
                f = new Face();
                f.uv[0] = 16-xmax;
                f.uv[2] = 16-xmin;
                f.uv[1] = 16-ymax;
                f.uv[3] = 16-ymin;
                f.texture = "#txt" + sidetxt[2];
                f.rotation = (siderot[2] != 0) ? siderot[2] : null;
                processRotation(f);
                if (isTinted) f.tintindex = 0;
                if (elem.from[2] <= 0) f.cullface = "north";
                elem.faces.put("north", f);
                // Add south face
                f = new Face();
                f.uv[0] = xmin;
                f.uv[2] = xmax;
                f.uv[1] = 16-ymax;
                f.uv[3] = 16-ymin;
                f.texture = "#txt" + sidetxt[3];
                f.rotation = (siderot[3] != 0) ? siderot[3] : null;
                processRotation(f);
                if (isTinted) f.tintindex = 0;
                if (elem.to[2] >= 16) f.cullface = "south";
                elem.faces.put("south", f);
                // Add west face
                f = new Face();
                f.uv[0] = zmin;
                f.uv[2] = zmax;
                f.uv[1] = 16-ymax;
                f.uv[3] = 16-ymin;
                f.texture = "#txt" + sidetxt[4];
                f.rotation = (siderot[4] != 0) ? siderot[4] : null;
                processRotation(f);
                if (isTinted) f.tintindex = 0;
                if (elem.from[0] <= 0) f.cullface = "west";
                elem.faces.put("west", f);
                // Add eath face
                f = new Face();
                f.uv[0] = 16-zmax;
                f.uv[2] = 16-zmin;
                f.uv[1] = 16-ymax;
                f.uv[3] = 16-ymin;
                f.texture = "#txt" + sidetxt[5];
                f.rotation = (siderot[5] != 0) ? siderot[5] : null;
                processRotation(f);
                if (isTinted) f.tintindex = 0;
                if (elem.to[0] >= 16) f.cullface = "east";
                elem.faces.put("east", f);
                mod.elements.add(elem);
            }
        }
        this.writeBlockModelFile(name + "_" + meta, mod);
    }
    private static final int[] STDTXTIDX = { 0, 1, 2, 3, 4, 5 };
    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            boolean isTinted = sb.isTinted(def);
            doCuboidModel(sb, sb.meta, sb.meta, def.blockName, isTinted);
            // Build simple item model that refers to block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_" + sb.meta;
            this.writeItemModelFile(def.blockName + "_" + sb.meta, mo);

            // Add tint overrides
            if (isTinted) {
                String tintres = def.getBlockColorMapResource(sb);
                if (tintres != null) {
                    ModelExport.addTintingOverride(def.blockName, String.format("variant=%s", sb.meta), tintres);
                }
            }
        }
    }

}
