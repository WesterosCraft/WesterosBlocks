package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class LeavesBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectLeaves {
        public String parent = "block/block";    // Use 'block'
        public Texture textures = new Texture();
        public Elements elements[] = { new Elements() };
    }
    public static class Texture {
        public String end, side, particle;
    }
    public static class Elements {
    	int[] from = { 0, 0, 0 };
    	int[] to = { 16, 16, 16 };
    	Map<String, Faces> faces = new HashMap<String, Faces>();
    }
    public static class Faces {
    	int[] uv = { 0, 0, 16, 16 };
    	String texture;
    	int tintindex = 0;
    	String cullface;
    	public Faces(String txt, String face) {
    		texture = txt;
    		cullface = face;
    	}
    }
    public static class ModelObject {
    	public String parent;
    }

    
    public LeavesBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
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
    
    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            ModelObjectLeaves mod = new ModelObjectLeaves();
            mod.elements[0].faces.put("down", new Faces("#end", "down"));
            mod.elements[0].faces.put("up", new Faces("#end", "up"));
            mod.elements[0].faces.put("north", new Faces("#side", "north"));
            mod.elements[0].faces.put("south", new Faces("#side", "south"));
            mod.elements[0].faces.put("west", new Faces("#side", "west"));
            mod.elements[0].faces.put("east", new Faces("#side", "east"));
            mod.textures.end = getTextureID(sb.getTextureByIndex(2)); 
            mod.textures.side = getTextureID(sb.getTextureByIndex(3)); 
            mod.textures.particle = mod.textures.side;
            this.writeBlockModelFile(def.blockName + "_" + sb.meta, mod);
            // Build simple item model that refers to block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_" + sb.meta;
            this.writeItemModelFile(def.blockName + "_" + sb.meta, mo);
        }
    }

}
