package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
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
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
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
        so.variants.put("east=false,north=false,south=false,up=false,west=false", new Variant(m+"_1", 0));
        so.variants.put("east=false,north=false,south=true,up=false,west=false", new Variant(m+"_1", 0));
        so.variants.put("east=false,north=false,south=false,up=false,west=true", new Variant(m+"_1", 90));
		so.variants.put("east=false,north=true,south=false,up=false,west=false", new Variant(m+"_1", 180));
		so.variants.put("east=true,north=false,south=false,up=false,west=false", new Variant(m+"_1", 270));
		so.variants.put("east=true,north=true,south=false,up=false,west=false", new Variant(m+"_2", 0));
		so.variants.put("east=true,north=false,south=true,up=false,west=false", new Variant(m+"_2", 90));
		so.variants.put("east=false,north=false,south=true,up=false,west=true", new Variant(m+"_2", 180));
		so.variants.put("east=false,north=true,south=false,up=false,west=true", new Variant(m+"_2", 270));
		so.variants.put("east=true,north=false,south=false,up=false,west=true", new Variant(m+"_2_opposite", 0));
		so.variants.put("east=false,north=true,south=true,up=false,west=false", new Variant(m+"_2_opposite", 90));
		so.variants.put("east=true,north=true,south=true,up=false,west=false", new Variant(m+"_3", 0));
		so.variants.put("east=true,north=false,south=true,up=false,west=true", new Variant(m+"_3", 90));
		so.variants.put("east=false,north=true,south=true,up=false,west=true", new Variant(m+"_3", 180));
		so.variants.put("east=true,north=true,south=false,up=false,west=true", new Variant(m+"_3", 270));
		so.variants.put("east=true,north=true,south=true,up=false,west=true", new Variant(m+"_4", 0));
		so.variants.put("east=false,north=false,south=false,up=true,west=false", new Variant(m+"_u", 0));
		so.variants.put("east=false,north=false,south=true,up=true,west=false", new Variant(m+"_1u", 0));
		so.variants.put("east=false,north=false,south=false,up=true,west=true", new Variant(m+"_1u", 90));
		so.variants.put("east=false,north=true,south=false,up=true,west=false", new Variant(m+"_1u", 180));
		so.variants.put("east=true,north=false,south=false,up=true,west=false", new Variant(m+"_1u", 270));
		so.variants.put("east=true,north=true,south=false,up=true,west=false", new Variant(m+"_2u", 0));
		so.variants.put("east=true,north=false,south=true,up=true,west=false", new Variant(m+"_2u", 90));
		so.variants.put("east=false,north=false,south=true,up=true,west=true", new Variant(m+"_2u", 180));
		so.variants.put("east=false,north=true,south=false,up=true,west=true", new Variant(m+"_2u", 270));
		so.variants.put("east=true,north=false,south=false,up=true,west=true", new Variant(m+"_2u_opposite", 0));
		so.variants.put("east=false,north=true,south=true,up=true,west=false", new Variant(m+"_2u_opposite", 90));
		so.variants.put("east=true,north=true,south=true,up=true,west=false", new Variant(m+"_3u", 0));
		so.variants.put("east=true,north=false,south=true,up=true,west=true", new Variant(m+"_3u", 90));
		so.variants.put("east=false,north=true,south=true,up=true,west=true", new Variant(m+"_3u", 180));
		so.variants.put("east=true,north=true,south=false,up=true,west=true", new Variant(m+"_3u", 270));
		so.variants.put("east=true,north=true,south=true,up=true,west=true", new Variant(m+"_4u", 0));
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
    	Subblock sb = def.subBlocks.get(0);
    	String txt = getTextureID(sb.getTextureByIndex(0));
    	// Build models
        if (!sb.isCustomModel()) {
        	ModelObject mo1 = new ModelObject("1", txt);
            this.writeBlockModelFile(def.blockName + "_1", mo1);
        	ModelObject mo1u = new ModelObject("1u", txt);
            this.writeBlockModelFile(def.blockName + "_1u", mo1u);
        	ModelObject mo2_op = new ModelObject("2_opposite", txt);
            this.writeBlockModelFile(def.blockName + "_2_opposite", mo2_op);
        	ModelObject mo2 = new ModelObject("2", txt);
            this.writeBlockModelFile(def.blockName + "_2", mo2);
        	ModelObject mo2u_op = new ModelObject("2u_opposite", txt);
            this.writeBlockModelFile(def.blockName + "_2u_opposite", mo2u_op);
        	ModelObject mo2u = new ModelObject("2u", txt);
            this.writeBlockModelFile(def.blockName + "_2u", mo2u);
        	ModelObject mo3 = new ModelObject("3", txt);
            this.writeBlockModelFile(def.blockName + "_3", mo3);
        	ModelObject mo3u = new ModelObject("3u", txt);
            this.writeBlockModelFile(def.blockName + "_3u", mo3u);
        	ModelObject mo4 = new ModelObject("4", txt);
            this.writeBlockModelFile(def.blockName + "_4", mo4);
        	ModelObject mo4u = new ModelObject("4u", txt);
            this.writeBlockModelFile(def.blockName + "_4u", mo4u);
        	ModelObject mou = new ModelObject("u", txt);
            this.writeBlockModelFile(def.blockName + "_u", mou);
        }
        // Build simple item model that refers to block model
        ItemModelObject mo = new ItemModelObject();
        mo.textures.layer0 = txt;
        this.writeItemModelFile(def.blockName, mo);
    }
}
