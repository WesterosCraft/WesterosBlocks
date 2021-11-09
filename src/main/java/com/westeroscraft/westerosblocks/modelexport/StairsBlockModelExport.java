package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.block.Block;

public class StairsBlockModelExport extends ModelExport {
    private final WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }

    public static class Variant {
        public String model = "";
        public Integer x;
        public Integer y;
        public Boolean uvlock;

        public Variant(final String blkname) {
            this(blkname, null, 0, 0);
        }

        public Variant(final String blkname, final String ext, final int yrot) {
            this(blkname, ext, 0, yrot);
        }

        public Variant(final String blkname, final String ext, final int xrot, final int yrot) {
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
        public String parent;
        public Texture textures = new Texture();

        public ModelObjectStair(final boolean ambientocclusion, final boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/stairs";
                } else {
                    parent = "block/stairs"; // Vanilla block is
                }
            } else {
                if (tinted) {
                    parent = "westerosblocks:block/tintednoocclusion/stairs";
                } else {
                    parent = "westerosblocks:block/noocclusion/stairs";
                }
            }
        }
    }

    public static class ModelObjectInnerStair {
        public String parent;
        public Texture textures = new Texture();

        public ModelObjectInnerStair(final boolean ambientocclusion, final boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/inner_stairs";
                } else {
                    parent = "block/inner_stairs"; // Vanilla block is
                }
            } else {
                if (tinted) {
                    parent = "westerosblocks:block/tintednoocclusion/inner_stairs";
                } else {
                    parent = "westerosblocks:block/noocclusion/inner_stairs";
                }
            }
        }
    }

    public static class ModelObjectOuterStair {
        public String parent;
        public Texture textures = new Texture();

        public ModelObjectOuterStair(final boolean ambientocclusion, final boolean tinted) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = "westerosblocks:block/tinted/outer_stairs";
                } else {
                    parent = "block/outer_stairs"; // Vanilla block is
                }
            } else {
                if (tinted) {
                    parent = "westerosblocks:block/tintednoocclusion/outer_stairs";
                } else {
                    parent = "westerosblocks:block/noocclusion/outer_stairs";
                }
            }
        }
    }

    public static class Texture {
        public String bottom, top, side;
        public String particle;
    }

    public static class ModelObject {
        public String parent;
    }

    public StairsBlockModelExport(final Block blk, final WesterosBlockDef def, final File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("tile." + def.blockName + ".name", def.subBlocks.get(0).label);
    }

    @Override
    public void doBlockStateExport() throws IOException {
        final StateObject so = new StateObject();
        final String bn = def.blockName;
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
        final boolean isTinted = def.subBlocks.get(0).isTinted(def);
        // Find base block for stairs - textures come from there
        final Block bblk = WesterosBlocks.findBlockByName(def.modelBlockName);
        if (bblk == null) {
            throw new IOException(
                    String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
        }
        String downtxt = null;
        String uptxt = null;
        String sidetxt = null;
        final boolean ambientOcclusion = (def.subBlocks.get(0).ambientOcclusion != null)
                ? def.subBlocks.get(0).ambientOcclusion
                : true;

        if (bblk instanceof WesterosBlockLifecycle) {
            final WesterosBlockDef bbdef = ((WesterosBlockLifecycle) bblk).getWBDefinition();
            if (bbdef == null) {
                throw new IOException(String.format("modelBlockName '%s' not found for block '%s' - no def",
                        def.modelBlockName, def.blockName));
            }
            final Subblock sb = bbdef.getByMeta(def.modelBlockMeta);
            downtxt = getTextureID(sb.getTextureByIndex(0));
            uptxt = getTextureID(sb.getTextureByIndex(1));
            sidetxt = getTextureID(sb.getTextureByIndex(2));
        } else { // Else, assume vanilla block: hack it for ones we use
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
                case "minecraft:hardened_clay":
                    downtxt = uptxt = sidetxt = "blocks/hardened_clay";
                    break;
                case "minecraft:gold_block":
                    downtxt = uptxt = sidetxt = "blocks/gold_block";                	
                    break;
                case "minecraft:ice":
                    downtxt = uptxt = sidetxt = "blocks/ice";                	
                    break;                	
                default:
                    throw new IOException(String.format("modelBlockName '%s:%d' not found for block '%s' - no vanilla",
                            def.modelBlockName, def.modelBlockMeta, def.blockName));
            }
        }
        // Base model
        final ModelObjectStair base = new ModelObjectStair(ambientOcclusion, isTinted);
        base.textures.bottom = downtxt;
        base.textures.top = uptxt;
        base.textures.side = base.textures.particle = sidetxt;
        this.writeBlockModelFile(def.blockName, base);
        // Outer model
        final ModelObjectOuterStair outer = new ModelObjectOuterStair(ambientOcclusion, isTinted);
        outer.textures.bottom = downtxt;
        outer.textures.top = uptxt;
        outer.textures.side = outer.textures.particle = sidetxt;
        this.writeBlockModelFile(def.blockName + "_outer", outer);
        // Inner model
        final ModelObjectInnerStair inner = new ModelObjectInnerStair(ambientOcclusion, isTinted);
        inner.textures.bottom = downtxt;
        inner.textures.top = uptxt;
        inner.textures.side = inner.textures.particle = sidetxt;
        this.writeBlockModelFile(def.blockName + "_inner", inner);
        // Build simple item model that refers to base block model
        final ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        this.writeItemModelFile(def.blockName, mo);

        // Handle tint resources
        if (isTinted) {
            final String tintres = def.getBlockColorMapResource(def.subBlocks.get(0));
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, null, tintres);
            }
        }
    }
}

