package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.blocks.WCSlabBlock;
import com.westeroscraft.westerosblocks.modelexport.StairsBlockModelExport.Display;
import com.westeroscraft.westerosblocks.modelexport.StairsBlockModelExport.Element;
import com.westeroscraft.westerosblocks.modelexport.StairsBlockModelExport.Face;
import com.westeroscraft.westerosblocks.modelexport.StairsBlockModelExport.Rotation;

import net.minecraft.block.Block;

public class SlabBlockModelExport extends ModelExport {
    private WCSlabBlock blk;
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
    }
    private static Map<String, Display> getBlockDisplay() {
    	Display d;
    	Map<String, Display> display = new HashMap<String, Display>();
		// Add display from block/block
    	d = new Display();
    	d.rotation = new int[] { 30, 225, 0 };
    	d.translation = new double[] { 0, 0, 0 };
    	d.scale = new double[] { 0.625, 0.625, 0.625 };
    	display.put("gui", d);
    	d = new Display();
    	d.rotation = new int[] { 0, 0, 0 };
    	d.translation = new double[] { 0, 3, 0 };
    	d.scale = new double[] { 0.25, 0.25, 0.25 };
		display.put("ground", d);
    	d = new Display();
    	d.rotation = new int[] { 0, 0, 0 };
    	d.translation = new double[] { 0, 0, 0 };
    	d.scale = new double[] { 0.5, 0.5, 0.5 };
		display.put("fixed", d);
    	d = new Display();
    	d.rotation = new int[] { 75, 45, 0 };
    	d.translation = new double[] {  0, 2.5, 0 };
    	d.scale = new double[] { 0.375, 0.375, 0.375 };
		display.put("thirdperson_righthand", d);
    	d = new Display();
    	d.rotation = new int[] { 0, 45, 0 };
    	d.translation = new double[] { 0, 0, 0 };
    	d.scale = new double[] { 0.40, 0.40, 0.40 };
		display.put("firstperson_righthand", d);
    	d = new Display();
    	d.rotation = new int[] { 0, 225, 0 };
    	d.translation = new double[] { 0, 0, 0 };
    	d.scale = new double[] { 0.40, 0.40, 0.40 };
		display.put("firstperson_lefthand", d);
		return display;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCube {
        public Boolean ambientocclusion;
        public Map<String, Display> display = getBlockDisplay();
        public Texture textures = new Texture();
        public List<Element> elements = new ArrayList<Element>();
        public ModelObjectCube() {
    		// Add elements
    		Element e = new Element();
    		e.from = new float[] { 0, 0, 0 };
    		e.to = new float[] { 16, 16, 16 };
    		Face f = new Face(); f.texture = "#down"; f.cullface = "down"; f.uv = null;
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#up"; f.cullface = "up"; f.uv = null;
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#north"; f.cullface = "north"; f.uv = null;
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#south"; f.cullface = "south"; f.uv = null;
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#west"; f.cullface = "west"; f.uv = null;
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#east"; f.cullface = "east"; f.uv = null;
    		e.faces.put("east", f);
			elements.add(e);
        }
    }
    public static class Texture {
        public String down, up, north, south, west, east, particle;
    }
    public static class ModelObjectHalfLower {
        public Boolean ambientocclusion;
        public Map<String, Display> display = getBlockDisplay();
        public TextureSlab textures = new TextureSlab();
        public List<Element> elements = new ArrayList<Element>();
        public ModelObjectHalfLower() {
    		// Add elements
    		Element e = new Element();
    		e.from = new float[] { 0, 0, 0 };
    		e.to = new float[] { 16, 8, 16 };
    		Face f = new Face(); f.texture = "#bottom"; f.cullface = "down";
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top";
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "north"; f.uv = new float[] { 0, 8, 16, 16 };
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south"; f.uv = new float[] { 0, 8, 16, 16 };
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "west"; f.uv = new float[] { 0, 8, 16, 16 };
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east"; f.uv = new float[] { 0, 8, 16, 16 };
    		e.faces.put("east", f);
			elements.add(e);
        }
    }
    public static class ModelObjectHalfUpper {
        public Boolean ambientocclusion;
        public Map<String, Display> display = getBlockDisplay();
        public TextureSlab textures = new TextureSlab();
        public List<Element> elements = new ArrayList<Element>();
        public ModelObjectHalfUpper() {
    		// Add elements
    		Element e = new Element();
    		e.from = new float[] { 0, 8, 0 };
    		e.to = new float[] { 16, 16, 16 };
    		Face f = new Face(); f.texture = "#bottom"; f.uv = new float[] { 0, 0, 16, 16 };
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top"; f.cullface = "up"; f.uv = new float[] { 0, 0, 16, 16  };
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "north"; f.uv = new float[] { 0, 0, 16,  8 };
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south"; f.uv = new float[] { 0, 0, 16,  8 };
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "west"; f.uv = new float[] { 0, 0, 16,  8 };
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east"; f.uv = new float[] { 0, 0, 16,  8 };
    		e.faces.put("east", f);
			elements.add(e);
        }
    }
    public static class TextureSlab {
        public String bottom, top, side, particle;
    }
    public static class ModelObject {
    	public String parent;
    }
    public static class Display {
        public int[] rotation = { 0, 90, 0 };
        public double[] translation = { 0, 0, 0 };
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

    
    public SlabBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.blk = (WCSlabBlock) blk;
        this.def = def;
        for (Subblock sb : def.subBlocks) {
            addNLSString(this.blk.getUnlocalizedName(sb.meta) + ".name", sb.label);
        }
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Do state for half block
        String bn = def.getBlockName(0);
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + bn + "_" + sb.meta + "_top";
            so.variants.put(String.format("half=top,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + bn + "_" + sb.meta + "_bottom";
            so.variants.put(String.format("half=bottom,variant=%d", sb.meta), var);
        }
        this.writeBlockStateFile(bn, so);
        // Do state for full block
        so = new StateObject();
        bn = def.getBlockName(1);
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + bn + "_" + sb.meta;
            so.variants.put(String.format("variant=%d", sb.meta), var);
        }
        this.writeBlockStateFile(bn, so);
    }

    private void setTinted(List<Element> e) {
    	for (Element em : e) {
    		for (Face f : em.faces.values()) {
    			f.tintindex = 0;
    		}
    	}
    }

    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            boolean isTinted = sb.isTinted(def);
        	// Double block model
            ModelObjectCube mod = new ModelObjectCube();
            mod.textures.down = getTextureID(sb.getTextureByIndex(0));
            mod.textures.up = getTextureID(sb.getTextureByIndex(1));
            mod.textures.north = getTextureID(sb.getTextureByIndex(2));
            mod.textures.south = getTextureID(sb.getTextureByIndex(3));
            mod.textures.west = getTextureID(sb.getTextureByIndex(4));
            mod.textures.east = getTextureID(sb.getTextureByIndex(5));
            mod.textures.particle = getTextureID(sb.getTextureByIndex(3));
            mod.ambientocclusion = sb.ambientOcclusion;
            if (isTinted) {
            	setTinted(mod.elements);
            }
            this.writeBlockModelFile(def.getBlockName(1) + "_" + sb.meta, mod);
            // Lower half block model
            ModelObjectHalfLower modl = new ModelObjectHalfLower();
            modl.textures.bottom = getTextureID(sb.getTextureByIndex(0));
            modl.textures.top = getTextureID(sb.getTextureByIndex(1));
            modl.textures.side = modl.textures.particle = getTextureID(sb.getTextureByIndex(2));
            modl.ambientocclusion = sb.ambientOcclusion;
            if (isTinted) {
            	setTinted(modl.elements);
            }
            this.writeBlockModelFile(def.getBlockName(0) + "_" + sb.meta + "_bottom", modl);
            // Upper half block model
            ModelObjectHalfUpper modu = new ModelObjectHalfUpper();
            modu.textures.bottom = getTextureID(sb.getTextureByIndex(0));
            modu.textures.top = getTextureID(sb.getTextureByIndex(1));
            modu.textures.side = modu.textures.particle = getTextureID(sb.getTextureByIndex(2));
            modu.ambientocclusion = sb.ambientOcclusion;
            if (isTinted) {
            	setTinted(modu.elements);
            }
            this.writeBlockModelFile(def.getBlockName(0) + "_" + sb.meta + "_top", modu);
            // Build simple item model that refers to lower block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.getBlockName(0) + "_" + sb.meta + "_bottom";
            this.writeItemModelFile(def.getBlockName(0) + "_" + sb.meta, mo);
            this.writeItemModelFile(def.getBlockName(1) + "_" + sb.meta, mo);
            // Handle tint resources
            if (isTinted) {
                String tintres = def.getBlockColorMapResource(sb);
                if (tintres != null) {
                    ModelExport.addTintingOverride(def.blockName, String.format("variant=%s", sb.meta), tintres);
                }
            }
        }
    }

}
