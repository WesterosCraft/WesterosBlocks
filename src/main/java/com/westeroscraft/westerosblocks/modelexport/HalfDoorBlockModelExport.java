package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class HalfDoorBlockModelExport extends ModelExport {
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
            this(blkname, null, 0);
        }
        public Variant(String blkname, String ext, int yrot) {
        	if (ext != null)
        		model = WesterosBlocks.MOD_ID + ":" + blkname + "_" + ext;
        	else
        		model = WesterosBlocks.MOD_ID + ":" + blkname;
            if (yrot != 0) {
                y = yrot;
                uvlock = true;
            }
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectTop {
        public String parent = "block/door_top";    // Use 'door_top' model
        public Texture textures = new Texture();
        public Display display = new Display();
    }
    public static class ModelObjectTopRH {
        public String parent = "block/door_top_rh";    // Use 'door_top_rh' model
        public Texture textures = new Texture();
    }
    public static class Display {
        public int[] rotation = { 30, 225, 0 };
        public int[] translation = { 0, 0, 0 };
        public double[] scale = { 0.625, 0.625, 0.625 };
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
        this.def = def;
        addNLSString("tile." + def.blockName + ".name", def.subBlocks.get(0).label);
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = def.blockName;
        so.variants.put("facing=east,hinge=left,open=false", new Variant(bn));
        so.variants.put("facing=south,hinge=left,open=false", new Variant(bn, null, 90));
        so.variants.put("facing=west,hinge=left,open=false", new Variant(bn, null, 180));
        so.variants.put("facing=north,hinge=left,open=false", new Variant(bn, null, 270));
        so.variants.put("facing=east,hinge=right,open=false", new Variant(bn, "rh", 0));
        so.variants.put("facing=south,hinge=right,open=false", new Variant(bn, "rh", 90));
        so.variants.put("facing=west,hinge=right,open=false", new Variant(bn, "rh", 180));
        so.variants.put("facing=north,hinge=right,open=false", new Variant(bn, "rh", 270));
        so.variants.put("facing=east,hinge=left,open=true", new Variant(bn, "rh", 90));
        so.variants.put("facing=south,hinge=left,open=true", new Variant(bn, "rh", 180));
        so.variants.put("facing=west,hinge=left,open=true", new Variant(bn, "rh", 270));
        so.variants.put("facing=north,hinge=left,open=true", new Variant(bn, "rh", 0));
        so.variants.put("facing=east,hinge=right,open=true", new Variant(bn, null, 270));
        so.variants.put("facing=south,hinge=right,open=true", new Variant(bn));
        so.variants.put("facing=west,hinge=right,open=true", new Variant(bn, null, 90));
        so.variants.put("facing=north,hinge=right,open=true", new Variant(bn, null, 180));
        
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        Subblock sb = def.subBlocks.get(0);
        String uptxt = getTextureID(sb.getTextureByIndex(0));
        // Top
        ModelObjectTop top = new ModelObjectTop();
        top.textures.top = uptxt;
        top.textures.bottom = uptxt;
        this.writeBlockModelFile(def.blockName, top);
        // Top RH
        ModelObjectTopRH trh = new ModelObjectTopRH();
        trh.textures.top = uptxt;
		trh.textures.bottom = uptxt;
        this.writeBlockModelFile(def.blockName + "_rh", trh);
        // Build simple item model that refers to base block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        this.writeItemModelFile(def.blockName, mo);
    }
}

