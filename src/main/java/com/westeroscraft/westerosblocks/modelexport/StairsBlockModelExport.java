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
import com.westeroscraft.westerosblocks.modelexport.CuboidBlockModelExport.Display;
import com.westeroscraft.westerosblocks.modelexport.CuboidBlockModelExport.Element;
import com.westeroscraft.westerosblocks.modelexport.CuboidBlockModelExport.Face;
import com.westeroscraft.westerosblocks.modelexport.CuboidBlockModelExport.Rotation;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.block.Block;

public class StairsBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model = "";
        public Integer x;
        public Integer y;
        public Boolean uvlock;
        public Variant(String blkname) {
            this(blkname, null, 0, 0);
        }
        public Variant(String blkname, String ext, int yrot) {
            this(blkname, ext, 0, yrot);
        }
        public Variant(String blkname, String ext, int xrot, int yrot) {
            if (ext != null)
                model = WesterosBlocks.MOD_ID + ":" + blkname + "_" + ext;
            else
                model = WesterosBlocks.MOD_ID + ":" + blkname;
            if (xrot != 0)
                x = xrot;
            if (yrot != 0)
                y = yrot;
            if ((xrot != 0) || (yrot != 0))
                uvlock = true;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectStair {
        public String parent = "block/block";
        public Boolean ambientocclusion;
        public Map<String, Display> display = new HashMap<String, Display>();
        public Texture textures = new Texture();
        public List<Element> elements = new ArrayList<Element>();
        
        public ModelObjectStair() {
        	// Set up display to match block/stair
        	Display d = new Display();
        	d.rotation = new int[] { 30, 135, 0 };
        	d.translation = new double[] { 0, 0, 0 };
        	d.scale = new double[] { 0.625, 0.625, 0.625 };
        	display.put("gui", d);
        	d = new Display();
        	d.rotation = new int[] { 0, -90, 0 };
        	d.translation = new double[] { 0, 0, 0 };
        	d.scale = new double[] { 1, 1, 1 };
        	display.put("head", d);
        	d = new Display();
        	d.rotation = new int[] { 75, -135, 0 };
        	d.translation = new double[] { 0, 2.5, 0 };
        	d.scale = new double[] { 0.375, 0.375, 0.375 };
    		display.put("thirdperson_lefthand", d);
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
			e = new Element();
    		e.from = new float[] { 8, 8, 0 };
    		e.to = new float[] { 16, 16, 16 };
    		f = new Face(); f.texture = "#bottom"; f.cullface = "down";
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top"; f.cullface = "up";
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "north"; f.uv = new float[] { 0, 0, 8, 8 };
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south"; f.uv = new float[] { 8, 0, 16, 8 };
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.uv = new float[] { 8, 0, 16, 8 };
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east"; f.uv = new float[] { 8, 0, 16, 8 };
    		e.faces.put("east", f);
			elements.add(e);
        }
    }
    public static class ModelObjectInnerStair {
        public Boolean ambientocclusion;
        public Texture textures = new Texture();
        public List<Element> elements = new ArrayList<Element>();
        public ModelObjectInnerStair() {
    		// Add elements
    		Element e = new Element();
    		e.from = new float[] { 0, 0, 0 };
    		e.to = new float[] { 16, 8, 16 };
    		Face f = new Face(); f.texture = "#bottom"; f.cullface = "down";
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top";
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "north";
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south";
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "west";
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east";
    		e.faces.put("east", f);
			elements.add(e);
			e = new Element();
    		e.from = new float[] { 8, 8, 0 };
    		e.to = new float[] { 16, 16, 16 };
    		f = new Face(); f.texture = "#bottom"; f.cullface = "down";
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top"; f.cullface = "up";
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "north"; f.uv = new float[] { 0, 0, 8, 8 };
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south"; f.uv = new float[] { 8, 0, 16, 8 };
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.uv = new float[] { 8, 0, 16, 8 };
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east"; f.uv = new float[] { 8, 0, 16, 8 };
    		e.faces.put("east", f);
			elements.add(e);        	
			e = new Element();
    		e.from = new float[] { 0, 8, 8 };
    		e.to = new float[] { 8, 16, 16 };
    		f = new Face(); f.texture = "#bottom"; f.cullface = "down"; f.uv = new float[] { 0, 0,  8,  8 };
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top"; f.cullface = "up"; f.uv = new float[] { 0, 8,  8, 16 };
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.uv = new float[] { 8, 0, 16,  8 };
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south"; f.uv = new float[] { 0, 0,  8,  8 };
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "west"; f.uv = new float[] { 8, 0, 16,  8 };
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east"; f.uv = new float[] { 0, 0,  8,  8 };
    		e.faces.put("east", f);
			elements.add(e);        	
        }
    }
    public static class ModelObjectOuterStair {
        public Boolean ambientocclusion;
        public Texture textures = new Texture();
        public List<Element> elements = new ArrayList<Element>();
        public ModelObjectOuterStair() {
    		// Add elements
    		Element e = new Element();
    		e.from = new float[] { 0, 0, 0 };
    		e.to = new float[] { 16, 8, 16 };
    		Face f = new Face(); f.texture = "#bottom"; f.cullface = "down";
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top";
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "north";
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south";
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "west";
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east";
    		e.faces.put("east", f);
			elements.add(e);
			e = new Element();
    		e.from = new float[] { 8, 8, 8 };
    		e.to = new float[] { 16, 16, 16 };
    		f = new Face(); f.texture = "#bottom"; f.cullface = "down"; f.uv = new float[] { 8, 0, 16,  8 };
    		e.faces.put("down", f);
    		f = new Face(); f.texture = "#top"; f.cullface = "up"; f.uv = new float[] { 8, 8, 16, 16 };
    		e.faces.put("up", f);
    		f = new Face(); f.texture = "#side"; f.uv = new float[] { 0, 0,  8,  8 };
    		e.faces.put("north", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "south"; f.uv = new float[] { 8, 0, 16,  8 };
    		e.faces.put("south", f);
    		f = new Face(); f.texture = "#side"; f.uv = new float[] { 8, 0, 16,  8 };
    		e.faces.put("west", f);
    		f = new Face(); f.texture = "#side"; f.cullface = "east"; f.uv = new float[] { 0, 0,  8,  8 };
    		e.faces.put("east", f);
			elements.add(e);        	
        }
    }
    public static class Texture {
        public String bottom, top, side;
        public String particle;
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

    public static class ModelObject {
    	public String parent;
    }

    public StairsBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("tile." + def.blockName + ".name", def.subBlocks.get(0).label);
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = def.blockName;
        so.variants.put("facing=east,half=bottom,shape=straight", new Variant(bn));
        so.variants.put("facing=west,half=bottom,shape=straight", new Variant(bn, null, 180));
        so.variants.put("facing=south,half=bottom,shape=straight", new Variant(bn, null, 90));
        so.variants.put("facing=north,half=bottom,shape=straight", new Variant(bn, null, 270));
        so.variants.put("facing=east,half=bottom,shape=outer_right", new Variant(bn, "outer", 0));
        so.variants.put("facing=west,half=bottom,shape=outer_right", new Variant(bn, "outer", 180));
        so.variants.put("facing=south,half=bottom,shape=outer_right", new Variant(bn, "outer", 90));
        so.variants.put("facing=north,half=bottom,shape=outer_right", new Variant(bn, "outer", 270));
        so.variants.put("facing=east,half=bottom,shape=outer_left", new Variant(bn, "outer", 270));
        so.variants.put("facing=west,half=bottom,shape=outer_left", new Variant(bn, "outer", 90));
        so.variants.put("facing=south,half=bottom,shape=outer_left", new Variant(bn, "outer", 0));
        so.variants.put("facing=north,half=bottom,shape=outer_left", new Variant(bn, "outer", 180));
        so.variants.put("facing=east,half=bottom,shape=inner_right", new Variant(bn, "inner", 0));
        so.variants.put("facing=west,half=bottom,shape=inner_right", new Variant(bn, "inner", 180));
        so.variants.put("facing=south,half=bottom,shape=inner_right", new Variant(bn, "inner", 90));
        so.variants.put("facing=north,half=bottom,shape=inner_right", new Variant(bn, "inner", 270));
        so.variants.put("facing=east,half=bottom,shape=inner_left", new Variant(bn, "inner", 270));
        so.variants.put("facing=west,half=bottom,shape=inner_left", new Variant(bn, "inner", 90));
        so.variants.put("facing=south,half=bottom,shape=inner_left", new Variant(bn, "inner", 0));
        so.variants.put("facing=north,half=bottom,shape=inner_left", new Variant(bn, "inner", 180));
        so.variants.put("facing=east,half=top,shape=straight", new Variant(bn, null, 180, 0));
        so.variants.put("facing=west,half=top,shape=straight", new Variant(bn, null, 180, 180));
        so.variants.put("facing=south,half=top,shape=straight", new Variant(bn, null, 180, 90));
        so.variants.put("facing=north,half=top,shape=straight", new Variant(bn, null, 180, 270));
        so.variants.put("facing=east,half=top,shape=outer_right", new Variant(bn, "outer", 180, 90));
        so.variants.put("facing=west,half=top,shape=outer_right", new Variant(bn, "outer", 180, 270));
        so.variants.put("facing=south,half=top,shape=outer_right", new Variant(bn, "outer", 180, 180));
        so.variants.put("facing=north,half=top,shape=outer_right", new Variant(bn, "outer", 180, 0));
        so.variants.put("facing=east,half=top,shape=outer_left", new Variant(bn, "outer", 180, 0));
        so.variants.put("facing=west,half=top,shape=outer_left", new Variant(bn, "outer", 180, 180));
        so.variants.put("facing=south,half=top,shape=outer_left", new Variant(bn, "outer", 180, 90));
        so.variants.put("facing=north,half=top,shape=outer_left", new Variant(bn, "outer", 180, 270));
        so.variants.put("facing=east,half=top,shape=inner_right", new Variant(bn, "inner", 180, 90));
        so.variants.put("facing=west,half=top,shape=inner_right", new Variant(bn, "inner", 180, 270));
        so.variants.put("facing=south,half=top,shape=inner_right", new Variant(bn, "inner", 180, 180));
        so.variants.put("facing=north,half=top,shape=inner_right", new Variant(bn, "inner", 180, 0));
        so.variants.put("facing=east,half=top,shape=inner_left", new Variant(bn, "inner", 180, 0));
        so.variants.put("facing=west,half=top,shape=inner_left", new Variant(bn, "inner", 180, 180));
        so.variants.put("facing=south,half=top,shape=inner_left", new Variant(bn, "inner", 180, 90));
        so.variants.put("facing=north,half=top,shape=inner_left", new Variant(bn, "inner", 180, 270));
        this.writeBlockStateFile(def.blockName, so);
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
        boolean isTinted = def.subBlocks.get(0).isTinted(def);
        // Find base block for stairs - textures come from there
        Block bblk = WesterosBlocks.findBlockByName(def.modelBlockName);
        if (bblk == null) {
            throw new IOException(String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
        }
        String downtxt = null;
        String uptxt = null;
        String sidetxt = null;
        Boolean ambientOcclusion = def.subBlocks.get(0).ambientOcclusion;
        if (bblk instanceof WesterosBlockLifecycle) {
            WesterosBlockDef bbdef = ((WesterosBlockLifecycle) bblk).getWBDefinition();
            if (bbdef == null) {
                throw new IOException(String.format("modelBlockName '%s' not found for block '%s' - no def", def.modelBlockName, def.blockName));
            }
            Subblock sb = bbdef.getByMeta(def.modelBlockMeta);
            downtxt = getTextureID(sb.getTextureByIndex(0));
            uptxt = getTextureID(sb.getTextureByIndex(1));
            sidetxt = getTextureID(sb.getTextureByIndex(2));
        }
        else {  // Else, assume vanilla block: hack it for ones we use
            switch (def.modelBlockName) {
                case "minecraft:bedrock":
                    downtxt = uptxt = sidetxt = "blocks/bedrock";
                    break;
                case "minecraft:lapis_block":
                    downtxt = uptxt = sidetxt = "blocks/lapis_block";
                    break;
                case "minecraft:sandstone":
                    downtxt = uptxt = "blocks/sandstone_top";
                    sidetxt = "blocks/sandstone_smooth";
                    break;
                case "minecraft:iron_block":
                    downtxt = uptxt = sidetxt = "blocks/iron_block";
                    break;
                case "minecraft:double_stone_slab":
                    downtxt = uptxt = "blocks/stone_slab_top";
                    sidetxt = "blocks/stone_slab_side";
                    break;
                case "minecraft:snow":
                    downtxt = uptxt = sidetxt = "blocks/snow";
                    break;
                case "minecraft:emerald_block":
                    downtxt = uptxt = sidetxt = "blocks/emerald_block";
                    break;
                case "minecraft:obsidian":
                    downtxt = uptxt = sidetxt = "blocks/obsidian";
                    break;
                default:
                    throw new IOException(String.format("modelBlockName '%s:%d' not found for block '%s' - no vanilla", def.modelBlockName, def.modelBlockMeta, def.blockName));
            }
        }
        // Base model
        ModelObjectStair base = new ModelObjectStair();
        base.textures.bottom = downtxt;
        base.textures.top = uptxt;
        base.textures.side = base.textures.particle = sidetxt;
        base.ambientocclusion = ambientOcclusion;
        if (isTinted) {
        	setTinted(base.elements);
        }
        this.writeBlockModelFile(def.blockName, base);
        // Outer model
        ModelObjectOuterStair outer = new ModelObjectOuterStair();
        outer.textures.bottom = downtxt;
        outer.textures.top = uptxt;
        outer.textures.side = outer.textures.particle = sidetxt;
        outer.ambientocclusion = ambientOcclusion;
        if (isTinted) {
        	setTinted(outer.elements);
        }
        this.writeBlockModelFile(def.blockName + "_outer", outer);
        // Inner model
        ModelObjectInnerStair inner = new ModelObjectInnerStair();
        inner.textures.bottom = downtxt;
        inner.textures.top = uptxt;
        inner.textures.side = inner.textures.particle = sidetxt;
        inner.ambientocclusion = ambientOcclusion;
        if (isTinted) {
        	setTinted(inner.elements);
        }
        this.writeBlockModelFile(def.blockName + "_inner", inner);
        // Build simple item model that refers to base block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        this.writeItemModelFile(def.blockName, mo);
        
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource(def.subBlocks.get(0));
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, null, tintres);
            }
        }
    }
}

