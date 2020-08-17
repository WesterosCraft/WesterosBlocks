package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.modelexport.FireBlockModelExport.ModelObject;
import com.westeroscraft.westerosblocks.modelexport.FireBlockModelExport.TextureLayer0;

import net.minecraft.block.Block;

public class VinesBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public ArrayList<Conditions> multipart = new ArrayList<Conditions>();
    };
    public static class CondVal {
    	public String north;
    	public String south;
    	public String east;
    	public String west;
    	public String up;
    	public String down;
    };
    public static class CondModel {
    	public String model;
    	public Integer x;
    	public Integer y;
    };
    public static class Conditions {
    	public CondVal when = new CondVal();
    	public CondModel apply = new CondModel();
    };
    public static class Variant {
        public String model;
        public Integer y;
        Variant(String m, int y) {
        	model = m;
        	if (y != 0) this.y = y;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObject {
        public String parent;
        public Texture textures = new Texture();
        ModelObject(String ext, String txt) {
        	parent = WesterosBlocks.MOD_ID + ":block/vines/vine_" + ext;
        	textures.vines = txt;
        }
    }
    public static class Texture {
        public String vines;
    }
    public static class ItemModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }
    
    public VinesBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        Subblock sb = def.subBlocks.get(0);
        addNLSString("tile." + def.blockName + ".name", sb.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String m = WesterosBlocks.MOD_ID + ":" + def.blockName;
        // Add part for each face
        Conditions cond;
        // South
        cond = new Conditions();
        cond.when.south = "true"; cond.apply.model = m + "_1";
        so.multipart.add(cond);
        // West
        cond = new Conditions();
        cond.when.west = "true"; cond.apply.model = m + "_1"; cond.apply.y = 90;
        so.multipart.add(cond);
        // North
        cond = new Conditions();
        cond.when.north = "true"; cond.apply.model = m + "_1"; cond.apply.y = 180;
        so.multipart.add(cond);
        // East
        cond = new Conditions();
        cond.when.east = "true"; cond.apply.model = m + "_1"; cond.apply.y = 270;
        so.multipart.add(cond);
        // Up
        cond = new Conditions();
        cond.when.up = "true"; cond.apply.model = m + "_u";
        so.multipart.add(cond);
        // Down
        cond = new Conditions();
        cond.when.down = "true"; cond.apply.model = m + "_u"; cond.apply.x = 180;
        so.multipart.add(cond);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
    	Subblock sb = def.subBlocks.get(0);
    	String txt = getTextureID(sb.getTextureByIndex(0));
    	String txt2 = getTextureID(sb.getTextureByIndex(1));
    	// Build models
        if (!sb.isCustomModel()) {
        	ModelObject mo1 = new ModelObject("1", txt);
            this.writeBlockModelFile(def.blockName + "_1", mo1);
        	ModelObject mou = new ModelObject("u", txt2);
            this.writeBlockModelFile(def.blockName + "_u", mou);
        }
        // Build simple item model that refers to block model
        ItemModelObject mo = new ItemModelObject();
        mo.textures.layer0 = txt;
        this.writeItemModelFile(def.blockName, mo);
    }
}
