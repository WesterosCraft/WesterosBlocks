package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.world.level.block.Block;

public class StairsBlockModelExport extends ModelExport {
    public static class OurVariant extends Variant {
        public OurVariant(final String modname, final int xrot, final int yrot, Integer weight, boolean no_uvlock) {
            model = modname;
            if (xrot != 0)
                this.x = xrot;
            if (yrot != 0)
                this.y = yrot;
            if ((!no_uvlock) && ((xrot != 0) || (yrot != 0))) {
                this.uvlock = true;
            }
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

        public ModelObjectStair(final boolean ambientocclusion, final boolean tinted, boolean overlay) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tinted/stairs_overlay" : "westerosblocks:block/tinted/stairs";
                } else {
                    parent = overlay ? "westerosblocks:block/untinted/stairs_overlay" : "westerosblocks:block/untinted/stairs";
                }
            } else {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tintednoocclusion/stairs_overlay" : "westerosblocks:block/tintednoocclusion/stairs";
                } else {
                    parent = overlay ? "westerosblocks:block/noocclusion/stairs_overlay" : "westerosblocks:block/noocclusion/stairs";
                }
            }
        }
    }

    public static class ModelObjectInnerStair {
        public String parent;
        public Texture textures = new Texture();

        public ModelObjectInnerStair(final boolean ambientocclusion, final boolean tinted, boolean overlay) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tinted/inner_stairs_overlay" : "westerosblocks:block/tinted/inner_stairs";
                } else {
                    parent = overlay ? "westerosblocks:block/untinted/inner_stairs_overlay" : "westerosblocks:block/untinted/inner_stairs";
                }
            } else {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tintednoocclusion/inner_stairs_overlay" : "westerosblocks:block/tintednoocclusion/inner_stairs";
                } else {
                    parent = overlay ? "westerosblocks:block/noocclusion/inner_stairs_overlay" : "westerosblocks:block/noocclusion/inner_stairs";
                }
            }
        }
    }

    public static class ModelObjectOuterStair {
        public String parent;
        public Texture textures = new Texture();

        public ModelObjectOuterStair(final boolean ambientocclusion, final boolean tinted, boolean overlay) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tinted/outer_stairs_overlay" : "westerosblocks:block/tinted/outer_stairs";
                } else {
                    parent = overlay ? "westerosblocks:block/untinted/outer_stairs_overlay" : "westerosblocks:block/untinted/outer_stairs";
                }
            } else {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tintednoocclusion/outer_stairs_overlay" : "westerosblocks:block/tintednoocclusion/outer_stairs";
                } else {
                    parent = overlay ? "westerosblocks:block/noocclusion/outer_stairs_overlay" : "westerosblocks:block/noocclusion/outer_stairs";
                }
            }
        }
    }

    public static class Texture {
        public String bottom, top, side;
        public String bottom_ov, top_ov, side_ov;
        public String particle;
    }

    public static class ModelObject {
        public String parent;
    }

    private WesterosBlockDef bbdef = null;
    private int setcnt = 1;
    private boolean isTinted = false;
	private boolean ambientOcclusion = true;
	private WCStairBlock sblk;

    public StairsBlockModelExport(final Block blk, final WesterosBlockDef def, final File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
        this.sblk = (WCStairBlock) blk;
    }

    private List<Variant> buildVariantList(StateObject so, String cond, String ext, int xrot, int yrot) {
    	List<Variant> vars = new ArrayList<Variant>();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < setcnt; setidx++) {
    		String modname = modelFileName(ext, setidx);
    		Variant var;
        	if (bbdef != null) {
        		WesterosBlockDef.RandomTextureSet set = bbdef.getRandomTextureSet(setidx);
        		var = new OurVariant(modname, xrot, yrot, set.weight, sblk.no_uvlock);
        		so.addVariant(cond, var, null);
        	}
        	else {
        		var = new OurVariant(modname, xrot, yrot, null, sblk.no_uvlock);
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
    
    public String modelFileName(String ext, int setidx) {
    	if (def.isCustomModel())
    		return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx);
    	else
    		return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx);
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
        // Export if not set to custom model
        if (!def.isCustomModel()) {
            boolean isOverlay = def.getOverlayTextureByIndex(0) != null;	
	        // Loop over the random sets we've got
	        for (int setidx = 0; setidx < setcnt; setidx++) {
	        	String downtxt = null;
	        	String uptxt = null;
	        	String sidetxt = null;
	        	String downtxt_ov = null;
	        	String uptxt_ov = null;
	        	String sidetxt_ov = null;
	        	if (bbdef != null) {
	            	WesterosBlockDef.RandomTextureSet set = bbdef.getRandomTextureSet(setidx);        		
	         		downtxt = getTextureID(set.getTextureByIndex(0));
	         		uptxt = getTextureID(set.getTextureByIndex(1));
	         		sidetxt = getTextureID(set.getTextureByIndex(2));
	         		if (isOverlay) {
	         			downtxt_ov = getTextureID(def.getOverlayTextureByIndex(0));
	         			uptxt_ov = getTextureID(def.getOverlayTextureByIndex(1));
	         			sidetxt_ov = getTextureID(def.getOverlayTextureByIndex(2));
	         		}
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
	                    case "minecraft:gold_block":
	                        downtxt = uptxt = sidetxt = "minecraft:block/gold_block";                	
	                        break;
	                    case "minecraft:ice":
	                        downtxt = uptxt = sidetxt = "minecraft:block/ice";                	
	                        break;                	
	                	default:
	                		throw new IOException(String.format("modelBlockName '%s' not found for block '%s' - no vanilla",
	                            def.modelBlockName, def.blockName));
	        		}
	        	}
	        	// Base model
	        	final ModelObjectStair base = new ModelObjectStair(ambientOcclusion, isTinted, isOverlay);
	        	base.textures.bottom = downtxt;
	        	base.textures.top = uptxt;
	        	base.textures.side = base.textures.particle = sidetxt;
	        	if (isOverlay) {
	        		base.textures.bottom_ov = downtxt_ov;
	        		base.textures.top_ov = uptxt_ov;
	        		base.textures.side_ov = sidetxt_ov;
	        	}
	        	this.writeBlockModelFile(getModelName("base", setidx), base);
	        	// Outer model
	        	final ModelObjectOuterStair outer = new ModelObjectOuterStair(ambientOcclusion, isTinted, isOverlay);
	        	outer.textures.bottom = downtxt;
	        	outer.textures.top = uptxt;
	        	outer.textures.side = outer.textures.particle = sidetxt;
	        	if (isOverlay) {
	        		outer.textures.bottom_ov = downtxt_ov;
	        		outer.textures.top_ov = uptxt_ov;
	        		outer.textures.side_ov = sidetxt_ov;
	        	}
	        	this.writeBlockModelFile(getModelName("outer", setidx), outer);
	        	// Inner model
	        	final ModelObjectInnerStair inner = new ModelObjectInnerStair(ambientOcclusion, isTinted, isOverlay);
	        	inner.textures.bottom = downtxt;
	        	inner.textures.top = uptxt;
	        	inner.textures.side = inner.textures.particle = sidetxt;
	        	if (isOverlay) {
	        		inner.textures.bottom_ov = downtxt_ov;
	        		inner.textures.top_ov = uptxt_ov;
	        		inner.textures.side_ov = sidetxt_ov;
	        	}
	        	this.writeBlockModelFile(getModelName("inner", setidx), inner);
	        }
        }
    	// Build simple item model that refers to base block model
        final ModelObject mo = new ModelObject();
        mo.parent = modelFileName("base", 0);
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