package com.westerosblocks.needsported.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;

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

	private final WCStairBlock sblk;

    public StairsBlockModelExport(final Block blk, final WesterosBlockDef def, final File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
        this.sblk = (WCStairBlock) blk;
    }

    protected String getModelName(String ext, int setidx) {
    	return def.getBlockName() + "/" + ext + ("_v" + (setidx+1));
    }

    protected String getModelName(String ext, int setidx, String cond) {
        if (cond == null)
            return getModelName(ext, setidx);
        return def.getBlockName() + "/" + cond + "/" + ext + ("_v" + (setidx+1));
    }

    public String modelFileName(String ext, int setidx) {
        return modelFileName(ext, setidx, null);
    }

    public String modelFileName(String ext, int setidx, String cond) {
    	if (def.isCustomModel())
    		return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx, cond);
    	else
    		return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx, cond);
    }

    public String modelFileName(WesterosBlockStateRecord sr, String ext, int setidx, String cond) {
    	if (sr.isCustomModel())
    		return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx, cond);
    	else
    		return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx, cond);
    }

    private void buildVariantList(StateObject so, WesterosBlockStateRecord sr, String cond, String ext, int xrot, int yrot) {
        boolean justBase = sr.stateID == null;
        Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);

        // Loop over the random sets we've got
        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
            WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
            String modname = (justBase) ? modelFileName(ext, setidx) : modelFileName(sr, ext, setidx, sr.stateID);
            Integer weight = set.weight;
            Variant var = new OurVariant(modname, xrot, yrot, weight, sblk.no_uvlock);
            so.addVariant(cond, var, stateIDs);
        }
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();

        for (WesterosBlockStateRecord sr : def.states) {
            buildVariantList(so,sr,"facing=east,half=bottom,shape=straight", "base", 0, 0);
            buildVariantList(so,sr,"facing=west,half=bottom,shape=straight", "base", 0, 180);
            buildVariantList(so,sr,"facing=south,half=bottom,shape=straight", "base", 0, 90);
            buildVariantList(so,sr,"facing=north,half=bottom,shape=straight", "base", 0, 270);
            buildVariantList(so,sr,"facing=east,half=bottom,shape=outer_right", "outer", 0, 0);
            buildVariantList(so,sr,"facing=west,half=bottom,shape=outer_right", "outer", 0, 180);
            buildVariantList(so,sr,"facing=south,half=bottom,shape=outer_right", "outer", 0, 90);
            buildVariantList(so,sr,"facing=north,half=bottom,shape=outer_right", "outer", 0, 270);
            buildVariantList(so,sr,"facing=east,half=bottom,shape=outer_left", "outer", 0, 270);
            buildVariantList(so,sr,"facing=west,half=bottom,shape=outer_left", "outer", 0, 90);
            buildVariantList(so,sr,"facing=south,half=bottom,shape=outer_left", "outer", 0, 0);
            buildVariantList(so,sr,"facing=north,half=bottom,shape=outer_left", "outer", 0, 180);
            buildVariantList(so,sr,"facing=east,half=bottom,shape=inner_right", "inner", 0, 0);
            buildVariantList(so,sr,"facing=west,half=bottom,shape=inner_right", "inner", 0, 180);
            buildVariantList(so,sr,"facing=south,half=bottom,shape=inner_right", "inner", 0, 90);
            buildVariantList(so,sr,"facing=north,half=bottom,shape=inner_right", "inner", 0, 270);
            buildVariantList(so,sr,"facing=east,half=bottom,shape=inner_left", "inner", 0, 270);
            buildVariantList(so,sr,"facing=west,half=bottom,shape=inner_left", "inner", 0, 90);
            buildVariantList(so,sr,"facing=south,half=bottom,shape=inner_left", "inner", 0, 0);
            buildVariantList(so,sr,"facing=north,half=bottom,shape=inner_left", "inner", 0, 180);
            buildVariantList(so,sr,"facing=east,half=top,shape=straight", "base", 180, 0);
            buildVariantList(so,sr,"facing=west,half=top,shape=straight", "base", 180, 180);
            buildVariantList(so,sr,"facing=south,half=top,shape=straight", "base", 180, 90);
            buildVariantList(so,sr,"facing=north,half=top,shape=straight", "base", 180, 270);
            buildVariantList(so,sr,"facing=east,half=top,shape=outer_right", "outer", 180, 90);
            buildVariantList(so,sr,"facing=west,half=top,shape=outer_right", "outer", 180, 270);
            buildVariantList(so,sr,"facing=south,half=top,shape=outer_right", "outer", 180, 180);
            buildVariantList(so,sr,"facing=north,half=top,shape=outer_right", "outer", 180, 0);
            buildVariantList(so,sr,"facing=east,half=top,shape=outer_left", "outer", 180, 0);
            buildVariantList(so,sr,"facing=west,half=top,shape=outer_left", "outer", 180, 180);
            buildVariantList(so,sr,"facing=south,half=top,shape=outer_left", "outer", 180, 90);
            buildVariantList(so,sr,"facing=north,half=top,shape=outer_left", "outer", 180, 270);
            buildVariantList(so,sr,"facing=east,half=top,shape=inner_right", "inner", 180, 90);
            buildVariantList(so,sr,"facing=west,half=top,shape=inner_right", "inner", 180, 270);
            buildVariantList(so,sr,"facing=south,half=top,shape=inner_right", "inner", 180, 180);
            buildVariantList(so,sr,"facing=north,half=top,shape=inner_right", "inner", 180, 0);
            buildVariantList(so,sr,"facing=east,half=top,shape=inner_left", "inner", 180, 0);
            buildVariantList(so,sr,"facing=west,half=top,shape=inner_left", "inner", 180, 180);
            buildVariantList(so,sr,"facing=south,half=top,shape=inner_left", "inner", 180, 90);
            buildVariantList(so,sr,"facing=north,half=top,shape=inner_left", "inner", 180, 270);
        }
        this.writeBlockStateFile(def.getBlockName(), so);
    }

	protected void doStairModel(boolean isOccluded, boolean isTinted, boolean isOverlay, int setidx, WesterosBlockStateRecord sr, int sridx, String cond) throws IOException {
		WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
        // Textures
        String downtxt = null;
        String uptxt = null;
        String sidetxt = null;
        String downtxt_ov = null;
        String uptxt_ov = null;
        String sidetxt_ov = null;
        downtxt = getTextureID(set.getTextureByIndex(0));
        uptxt = getTextureID(set.getTextureByIndex(1));
        sidetxt = getTextureID(set.getTextureByIndex(2));
        if (isOverlay) {
            downtxt_ov = getTextureID(sr.getOverlayTextureByIndex(0));
            uptxt_ov = getTextureID(sr.getOverlayTextureByIndex(1));
            sidetxt_ov = getTextureID(sr.getOverlayTextureByIndex(2));
        }	        		
        // Base model
        ModelObjectStair base = new ModelObjectStair(isOccluded, isTinted, isOverlay);
        base.textures.bottom = downtxt;
        base.textures.top = uptxt;
        base.textures.side = base.textures.particle = sidetxt;
        if (isOverlay) {
            base.textures.bottom_ov = downtxt_ov;
            base.textures.top_ov = uptxt_ov;
            base.textures.side_ov = sidetxt_ov;
        }
        this.writeBlockModelFile(getModelName("base", setidx, cond), base);
        // Outer model
        final ModelObjectOuterStair outer = new ModelObjectOuterStair(isOccluded, isTinted, isOverlay);
        outer.textures.bottom = downtxt;
        outer.textures.top = uptxt;
        outer.textures.side = outer.textures.particle = sidetxt;
        if (isOverlay) {
            outer.textures.bottom_ov = downtxt_ov;
            outer.textures.top_ov = uptxt_ov;
            outer.textures.side_ov = sidetxt_ov;
        }
        this.writeBlockModelFile(getModelName("outer", setidx, cond), outer);
        // Inner model
        final ModelObjectInnerStair inner = new ModelObjectInnerStair(isOccluded, isTinted, isOverlay);
        inner.textures.bottom = downtxt;
        inner.textures.top = uptxt;
        inner.textures.side = inner.textures.particle = sidetxt;
        if (isOverlay) {
            inner.textures.bottom_ov = downtxt_ov;
            inner.textures.top_ov = uptxt_ov;
            inner.textures.side_ov = sidetxt_ov;
        }
        this.writeBlockModelFile(getModelName("inner", setidx, cond), inner);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isOccluded = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;
		for (int idx = 0; idx < def.states.size(); idx++) {
			WesterosBlockStateRecord rec = def.states.get(idx);
            // Export if not set to custom model
            if (!rec.isCustomModel()) {
                boolean isTinted = rec.isTinted();
                boolean isOverlay = rec.getOverlayTextureByIndex(0) != null;
                String cond = rec.stateID;
                // Loop over the random sets we've got
                for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
                    doStairModel(isOccluded, isTinted, isOverlay, setidx, rec, idx, cond);
                }
            }
        }

    	// Build simple item model that refers to base block model
        ModelObject mo = new ModelObject();
        WesterosBlockStateRecord sr0 = def.states.get(0);
        boolean isTinted = sr0.isTinted();
        String cond = sr0.stateID;
        mo.parent = modelFileName(sr0, "base", 0, cond);
        this.writeItemModelFile(def.getBlockName(), mo);
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), null, tintres);
            }
        }
    }

    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") (need stairs connection filter)");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	newstate.put("waterlogged", "false");
    	if (sblk.unconnect) {
        	newstate.put("unconnect", "false");    		
    	}
		if (sblk != null && sblk.connectstate) {
			newstate.put("connectstate", "0");
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