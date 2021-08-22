package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.block.Block;

public class StairsBlockModelExport extends ModelExport {
    public static class OurVariant extends Variant {
        public OurVariant(final String modname, final int xrot, final int yrot, Integer weight) {
            model = WesterosBlocks.MOD_ID + ":block/generated/" + modname;
            if (xrot != 0)
                x = xrot;
            if (yrot != 0)
                y = yrot;
            if ((xrot != 0) || (yrot != 0))
                uvlock = true;
            this.weight = weight;
        }
    }

    protected String getModelName(String ext, int setidx) {
    	return def.getBlockName() + "/" + ext + ("_v" + (setidx+1));
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
                    parent = "minecraft:block/stairs"; // Vanilla block is
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
                    parent = "minecraft:block/inner_stairs"; // Vanilla block is
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
                    parent = "minecraft:block/outer_stairs"; // Vanilla block is
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

    private WesterosBlockDef bbdef = null;
    private int setcnt = 1;
    private boolean isTinted = false;
	private boolean ambientOcclusion = true;

    public StairsBlockModelExport(final Block blk, final WesterosBlockDef def, final File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    private List<Variant> buildVariantList(StateObject so, String cond, String ext, int xrot, int yrot) {
    	List<Variant> vars = new ArrayList<Variant>();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < setcnt; setidx++) {
    		String modname = getModelName(ext, setidx);
    		Variant var;
        	if (bbdef != null) {
        		WesterosBlockDef.RandomTextureSet set = bbdef.getRandomTextureSet(setidx);
        		var = new OurVariant(modname, xrot, yrot, set.weight);
        		so.addVariant(cond, var, set.condIDs);
        	}
        	else {
        		var = new OurVariant(modname, xrot, yrot, null);
        		so.addVariant(cond, var, null);
        	}
        }
    	return vars;
    }
    
    private Block getModelBlock() throws IOException {
        // Find base block for stairs - textures come from there
        Block bblk = WesterosBlocks.findBlockByName(def.modelBlockName);
        if (bblk == null) {
            throw new IOException(
                    String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
        }
        return bblk;
    }
    
    private void doInit() throws IOException {
        Block bblk = getModelBlock();
        // Find base block for stairs - textures come from there
    	if (bblk instanceof WesterosBlockLifecycle) {
    		bbdef = ((WesterosBlockLifecycle) bblk).getWBDefinition();
    		setcnt = bbdef.getRandomTextureSetCount();
    		isTinted = bbdef.isTinted();
    		ambientOcclusion = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;
    	}    	
    }
    @Override
    public void doBlockStateExport() throws IOException {
        final StateObject so = new StateObject();
        doInit();
        buildVariantList(so,"facing=east,half=bottom,shape=straight", "base", 0, 0);
        buildVariantList(so,"facing=west,half=bottom,shape=straight", "base", 0, 180);
        buildVariantList(so,"facing=south,half=bottom,shape=straight", "base", 0, 90);
        buildVariantList(so,"facing=north,half=bottom,shape=straight", "base", 0, 270);
        buildVariantList(so,"facing=east,half=bottom,shape=outer_right", "outer", 0, 0);
        buildVariantList(so,"facing=west,half=bottom,shape=outer_right", "outer", 0, 180);
        buildVariantList(so,"facing=south,half=bottom,shape=outer_right", "outer", 0, 90);
        buildVariantList(so,"facing=north,half=bottom,shape=outer_right", "outer", 0, 270);
        buildVariantList(so,"facing=east,half=bottom,shape=outer_left", "outer", 0, 270);
        buildVariantList(so,"facing=west,half=bottom,shape=outer_left", "outer", 0, 90);
        buildVariantList(so,"facing=south,half=bottom,shape=outer_left", "outer", 0, 0);
        buildVariantList(so,"facing=north,half=bottom,shape=outer_left", "outer", 0, 180);
        buildVariantList(so,"facing=east,half=bottom,shape=inner_right", "inner", 0, 0);
        buildVariantList(so,"facing=west,half=bottom,shape=inner_right", "inner", 0, 180);
        buildVariantList(so,"facing=south,half=bottom,shape=inner_right", "inner", 0, 90);
        buildVariantList(so,"facing=north,half=bottom,shape=inner_right", "inner", 0, 270);
        buildVariantList(so,"facing=east,half=bottom,shape=inner_left", "inner", 0, 270);
        buildVariantList(so,"facing=west,half=bottom,shape=inner_left", "inner", 0, 90);
        buildVariantList(so,"facing=south,half=bottom,shape=inner_left", "inner", 0, 0);
        buildVariantList(so,"facing=north,half=bottom,shape=inner_left", "inner", 0, 180);
        buildVariantList(so,"facing=east,half=top,shape=straight", "base", 180, 0);
        buildVariantList(so,"facing=west,half=top,shape=straight", "base", 180, 180);
        buildVariantList(so,"facing=south,half=top,shape=straight", "base", 180, 90);
        buildVariantList(so,"facing=north,half=top,shape=straight", "base", 180, 270);
        buildVariantList(so,"facing=east,half=top,shape=outer_right", "outer", 180, 90);
        buildVariantList(so,"facing=west,half=top,shape=outer_right", "outer", 180, 270);
        buildVariantList(so,"facing=south,half=top,shape=outer_right", "outer", 180, 180);
        buildVariantList(so,"facing=north,half=top,shape=outer_right", "outer", 180, 0);
        buildVariantList(so,"facing=east,half=top,shape=outer_left", "outer", 180, 0);
        buildVariantList(so,"facing=west,half=top,shape=outer_left", "outer", 180, 180);
        buildVariantList(so,"facing=south,half=top,shape=outer_left", "outer", 180, 90);
        buildVariantList(so,"facing=north,half=top,shape=outer_left", "outer", 180, 270);
        buildVariantList(so,"facing=east,half=top,shape=inner_right", "inner", 180, 90);
        buildVariantList(so,"facing=west,half=top,shape=inner_right", "inner", 180, 270);
        buildVariantList(so,"facing=south,half=top,shape=inner_right", "inner", 180, 180);
        buildVariantList(so,"facing=north,half=top,shape=inner_right", "inner", 180, 0);
        buildVariantList(so,"facing=east,half=top,shape=inner_left", "inner", 180, 0);
        buildVariantList(so,"facing=west,half=top,shape=inner_left", "inner", 180, 180);
        buildVariantList(so,"facing=south,half=top,shape=inner_left", "inner", 180, 90);
        buildVariantList(so,"facing=north,half=top,shape=inner_left", "inner", 180, 270);
        this.writeBlockStateFile(def.getBlockName(), so);
    }

    @Override
    public void doModelExports() throws IOException {
        doInit();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < setcnt; setidx++) {
        	String downtxt = null;
        	String uptxt = null;
        	String sidetxt = null;
        	if (bbdef != null) {
            	WesterosBlockDef.RandomTextureSet set = bbdef.getRandomTextureSet(setidx);        		
         		downtxt = getTextureID(set.getTextureByIndex(0));
         		uptxt = getTextureID(set.getTextureByIndex(1));
         		sidetxt = getTextureID(set.getTextureByIndex(2));
        	}
        	else {
        		switch (def.modelBlockName) {
                	case "minecraft:bedrock":
                		downtxt = uptxt = sidetxt = "minecraft:block/bedrock";
                		break;
                	case "minecraft:lapis_block":
                		downtxt = uptxt = sidetxt = "minecraft:block/lapis_block";
                		break;
                	case "minecraft:sandstone":
                		downtxt = "minecraft:block/sandstone_bottom"; 
                		uptxt = "minecraft:block/sandstone_top";
                		sidetxt = "minecraft:block/sandstone";
                		break;
                	case "minecraft:iron_block":
                		downtxt = uptxt = sidetxt = "minecraft:block/iron_block";
                		break;
                	case "minecraft:stone_slab":
                		downtxt = uptxt = "minecraft:block/stone";
                		sidetxt = "minecraft:block/stone";
                		break;
                	case "minecraft:snow":
                		downtxt = uptxt = sidetxt = "minecraft:block/snow";
                		break;
                	case "minecraft:emerald_block":
                		downtxt = uptxt = sidetxt = "minecraft:block/emerald_block";
                		break;
                	case "minecraft:obsidian":
                		downtxt = uptxt = sidetxt = "minecraft:block/obsidian";
                		break;
                	case "minecraft:terracotta":
                		downtxt = uptxt = sidetxt = "minecraft:block/terracotta";
                		break;
                	default:
                		throw new IOException(String.format("modelBlockName '%s' not found for block '%s' - no vanilla",
                            def.modelBlockName, def.blockName));
        		}
        	}
        	// Base model
        	final ModelObjectStair base = new ModelObjectStair(ambientOcclusion, isTinted);
        	base.textures.bottom = downtxt;
        	base.textures.top = uptxt;
        	base.textures.side = base.textures.particle = sidetxt;
        	this.writeBlockModelFile(getModelName("base", setidx), base);
        	// Outer model
        	final ModelObjectOuterStair outer = new ModelObjectOuterStair(ambientOcclusion, isTinted);
        	outer.textures.bottom = downtxt;
        	outer.textures.top = uptxt;
        	outer.textures.side = outer.textures.particle = sidetxt;
        	this.writeBlockModelFile(getModelName("outer", setidx), outer);
        	// Inner model
        	final ModelObjectInnerStair inner = new ModelObjectInnerStair(ambientOcclusion, isTinted);
        	inner.textures.bottom = downtxt;
        	inner.textures.top = uptxt;
        	inner.textures.side = inner.textures.particle = sidetxt;
        	this.writeBlockModelFile(getModelName("inner", setidx), inner);
        }
    	// Build simple item model that refers to base block model
        final ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
        this.writeItemModelFile(def.getBlockName(), mo);

        // Handle tint resources
        if (isTinted) {
            final String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), null, tintres);
            }
        }
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") (need stairs connection filter)");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	newstate.put("waterlogged", "false");
    	if ((bbdef != null) && (bbdef.condStates != null)) {
        	newstate.put("cond", bbdef.getDefaultCondID());    		
    	}
    	for (String facing : FACING) {
        	oldstate.put("facing", facing);
        	newstate.put("facing", facing);
    		for (String half : TOPBOTTOM) {
    	    	oldstate.put("half", half);
    	    	newstate.put("half", half);
    			//for (String shape : SHAPE5) {
    	    	{
    	    		String shape = "straight";	// This needs to be handled by filter - WorldConverter doesn't like duplicate meta values in mapping
    		    	oldstate.put("shape", shape);
    		    	newstate.put("shape", shape);
    		        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);    				
    			}
    		}
    	}
    }

}