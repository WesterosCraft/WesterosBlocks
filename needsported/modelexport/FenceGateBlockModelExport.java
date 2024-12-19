package com.westerosblocks.needsported.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.world.level.block.Block;

public class FenceGateBlockModelExport extends ModelExport {
    public static class Texture {
        public String texture;
    }
    public static class ModelObject {
    	public String parent;
        public Texture textures = new Texture();
    }

    public FenceGateBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    public static class OurVariant extends Variant {
        public OurVariant(final String modname, final int yrot, Integer weight) {
            model = modname;
            if (yrot != 0)
                this.y = yrot;
            this.uvlock = true;
            this.weight = weight;
        }
    }

    protected String getModelName(String ext, int setidx) {
    	return def.getBlockName() + "/" + ext + ("_v" + (setidx+1));
    }

    public String modelFileName(String ext, int setidx) {
    	if (def.isCustomModel())
    		return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx);
    	else
    		return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx);
    }

    private List<Variant> buildVariantList(StateObject so, String cond, String ext, int yrot) {
    	List<Variant> vars = new ArrayList<Variant>();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
    		String modname = modelFileName(ext, setidx);
    		Variant var;
    		var = new OurVariant(modname, yrot, def.getRandomTextureSet(setidx).weight);
    		so.addVariant(cond, var, null);
        }
    	return vars;
    }

    private static final String[] facings = { "east", "north", "south", "west" };
    private static final int[] facing_yrot = { 270, 180, 0, 90 };
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Loop through facings
        for (int f = 0; f < facings.length; f++) {
            buildVariantList(so,"facing=" + facings[f] + ",in_wall=false,open=false", "gate", facing_yrot[f]);
            buildVariantList(so,"facing=" + facings[f] + ",in_wall=false,open=true", "gate_open", facing_yrot[f]);
            buildVariantList(so,"facing=" + facings[f] + ",in_wall=true,open=false", "gate_wall", facing_yrot[f]);
            buildVariantList(so,"facing=" + facings[f] + ",in_wall=true,open=true", "gate_wall_open", facing_yrot[f]);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        //boolean isOverlay = def.getOverlayTextureByIndex(0) != null;
    	WesterosBlockDef.RandomTextureSet set;

        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	set = def.getRandomTextureSet(setidx);
        	// Closed gate
        	ModelObject mod = new ModelObject();
        	mod.parent = isTinted ? WesterosBlocks.MOD_ID + ":block/tinted/template_fence_gate" : WesterosBlocks.MOD_ID + ":block/untinted/template_fence_gate";
        	mod.textures.texture = getTextureID(set.getTextureByIndex(0)); 
        	this.writeBlockModelFile(getModelName("gate", setidx), mod);
        	// Open gate
        	mod = new ModelObject();
        	mod.parent = isTinted ? WesterosBlocks.MOD_ID + ":block/tinted/template_fence_gate_open" : WesterosBlocks.MOD_ID + ":block/untinted/template_fence_gate_open";
        	mod.textures.texture = getTextureID(set.getTextureByIndex(0)); 
        	this.writeBlockModelFile(getModelName("gate_open", setidx), mod);
        	// Closed in wall gate
        	mod = new ModelObject();
        	mod.parent = isTinted ? WesterosBlocks.MOD_ID + ":block/tinted/template_fence_gate_wall" : WesterosBlocks.MOD_ID + ":block/untinted/template_fence_gate_wall";
        	mod.textures.texture = getTextureID(set.getTextureByIndex(0)); 
        	this.writeBlockModelFile(getModelName("gate_wall", setidx), mod);
        	// Open in wall gate
        	mod = new ModelObject();
        	mod.parent = isTinted ? WesterosBlocks.MOD_ID + ":block/tinted/template_fence_gate_wall_open" : WesterosBlocks.MOD_ID + ":block/untinted/template_fence_gate_wall_open";
        	mod.textures.texture = getTextureID(set.getTextureByIndex(0)); 
        	this.writeBlockModelFile(getModelName("gate_wall_open", setidx), mod);
        }
    	// Build simple item model that refers to fence inventory model
    	ModelObject mo = new ModelObject();
    	set = def.getRandomTextureSet(0);
    	mo.textures.texture = getTextureID(set.getTextureByIndex(0)); 
    	mo.parent = isTinted ? WesterosBlocks.MOD_ID + ":block/tinted/template_fence_gate" : WesterosBlocks.MOD_ID + ":block/untinted/template_fence_gate";
    	this.writeItemModelFile(def.getBlockName(), mo);
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
            }
        }
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	for (String facing : FACING) {
        	oldstate.put("facing", facing);
        	newstate.put("facing", facing);
    		for (String open : BOOLEAN) {
    	    	oldstate.put("open", open);
    	    	newstate.put("open", open);
        		for (String powered : BOOLEAN) {
        	    	oldstate.put("powered", powered);
        	    	newstate.put("powered", powered);
            		for (String inwall : BOOLEAN) {
            	    	oldstate.put("in_wall", inwall);
            	    	newstate.put("in_wall", inwall);
            	    	addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);    				
            		}
        		}
    		}
    	}
    }

}
