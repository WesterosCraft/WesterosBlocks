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
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class CakeBlockModelExport extends ModelExport {
    protected WesterosBlockDef def;

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
    public static class ModelObjectCake {
        public String parent = "block/thin_block";    // Use 'thin_block' model for single texture
        public Texture textures = new Texture();
        public List<Element> elements = new ArrayList<Element>();
    }
    public static class Texture {
        public String particle;
        public String bottom;
        public String top;
        public String side;
        public String inside;
    }
    public static class Element {
        public float[] from = { 1, 0, 1 };
        public float[] to = { 15, 8, 15 };
        public Map<String, Face> faces = new HashMap<String, Face>();
    }
    public static class Face {
        public String texture;
        public String cullface;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public CakeBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;

        addNLSString("tile." + def.blockName + ".name", def.subBlocks.get(0).label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        
        for (int i = 0; i < 7; i++) {
            Variant var = new Variant();
            if (i == 0)
                var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_uneaten";
            else
                var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_slice" + i;
            so.variants.put(String.format("bites=%d", i), var);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        Subblock sb = def.subBlocks.get(0);
        String bottxt = getTextureID(sb.getTextureByIndex(0));
        String toptxt = getTextureID(sb.getTextureByIndex(1));
        String sidetxt = getTextureID(sb.getTextureByIndex(2));
        String insidetxt = getTextureID(sb.getTextureByIndex(3));

        for (int i = 0; i < 7; i++) {
            ModelObjectCake mod = new ModelObjectCake();
            mod.textures.particle = sidetxt;
            mod.textures.bottom = bottxt;
            mod.textures.top = toptxt;
            mod.textures.side = sidetxt;
            mod.textures.inside = insidetxt;
            int xmin = 1 + (2*i);
            // Handle add element for cake
            Element elem = new Element();
            elem.from[0] = xmin;     // Set size
            // Add down face
            Face f = new Face();
            f.texture = "#bottom";
            f.cullface = "down";
            elem.faces.put("down", f);
            // Add up face
            f = new Face();
            f.texture = "#top";
            elem.faces.put("up", f);
            // Add north face
            f = new Face();
            f.texture = "#side";
            elem.faces.put("north", f);
            // Add south face
            f = new Face();
            f.texture = "#side";
            elem.faces.put("south", f);
            // Add west face
            f = new Face();
            if (i == 0)
                f.texture = "#side";
            else
                f.texture = "#inside";
            elem.faces.put("west", f);
            // Add eath face
            f = new Face();
            f.texture = "#side";
            elem.faces.put("east", f);
            mod.elements.add(elem);
            
            if (i == 0)
                this.writeBlockModelFile(def.blockName + "_uneaten", mod);
            else
                this.writeBlockModelFile(def.blockName + "_slice" + i, mod);
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_uneaten";
        this.writeItemModelFile(def.blockName, mo);
    }

}
