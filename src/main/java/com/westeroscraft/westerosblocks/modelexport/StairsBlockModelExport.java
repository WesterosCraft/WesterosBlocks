package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;

import net.minecraft.block.Block;

public class StairsBlockModelExport extends ModelExport {
    private WCStairBlock blk;
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
        public String parent = "block/stairs";    // Use 'stairs' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectInnerStair {
        public String parent = "block/inner_stairs";    // Use 'inner_stairs' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectOuterStair {
        public String parent = "block/outer_stairs";    // Use 'outer_stairs' model
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String bottom, top, side;
    }
    public static class ModelObject {
    	public String parent;
    }

    public StairsBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.blk = (WCStairBlock) blk;
        this.def = def;
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

    @Override
    public void doModelExports() throws IOException {
        // Find base block for stairs - textures come from there
        Block bblk = WesterosBlocks.findBlockByName(def.modelBlockName);
        if (bblk == null) {
            throw new IOException(String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
        }
        String downtxt = null;
        String uptxt = null;
        String sidetxt = null;
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
                case "7": // "Bedrock Stairs"
                    downtxt = uptxt = sidetxt = "blocks/bedrock";
                    break;
                case "22": // "Bronze Stairs"
                    downtxt = uptxt = sidetxt = "blocks/lapis_block";
                    break;
                case "24": // "Red Sandstone Stairs"
                    downtxt = uptxt = "blocks/sandstone_top";
                    sidetxt = "blocks/sandstone_smooth";
                    break;
                case "42": // "Iron Stairs"
                    downtxt = uptxt = sidetxt = "blocks/iron_block";
                    break;
                case "43": // "Stone Slab Stairs"
                    downtxt = uptxt = "blocks/stone_slab_top";
                    sidetxt = "blocks/stone_slab_side";
                    break;
                case "80": // "Snow Stairs"
                    downtxt = uptxt = sidetxt = "blocks/snow";
                    break;
                case "133": // "Steel Stairs"
                    downtxt = uptxt = sidetxt = "blocks/emerald_block";
                    break;
                default:
                    throw new IOException(String.format("modelBlockName '%s:%d' not found for block '%s' - no vanilla", def.modelBlockName, def.modelBlockMeta, def.blockName));
            }
        }
        // Base model
        ModelObjectStair base = new ModelObjectStair();
        base.textures.bottom = downtxt;
        base.textures.top = uptxt;
        base.textures.side = sidetxt;
        this.writeBlockModelFile(def.blockName, base);
        // Outer model
        ModelObjectOuterStair outer = new ModelObjectOuterStair();
        outer.textures.bottom = downtxt;
        outer.textures.top = uptxt;
        outer.textures.side = sidetxt;
        this.writeBlockModelFile(def.blockName + "_outer", outer);
        // Inner model
        ModelObjectInnerStair inner = new ModelObjectInnerStair();
        inner.textures.bottom = downtxt;
        inner.textures.top = uptxt;
        inner.textures.side = sidetxt;
        this.writeBlockModelFile(def.blockName + "_inner", inner);
        // Build simple item model that refers to base block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        this.writeItemModelFile(def.blockName, mo);
    }
}

