package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.block.Block;

public class CuboidNSEWStackBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWStackBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Export if not set to custom model
        if (!def.isCustomModel()) {
        	// Bottom model
            ModelObjectCuboid mod = new ModelObjectCuboid();
            WesterosBlockDef.StackElement se = def.getStackElementByIndex(0);
            String txt0 = se.getTextureByIndex(0);
            mod.textures.put("particle", getTextureID(se.getTextureByIndex(0)));
            int cnt = Math.max(6, se.textures.size());
            String[] textures = new String[cnt];
            for (int i = 0; i < cnt; i++) {
            	textures[i] = se.getTextureByIndex(i);
            }
            doCuboidModel(def.blockName, isTinted, txt0, textures, se.cuboids);
        	// Top model
            ModelObjectCuboid mod2 = new ModelObjectCuboid();
            WesterosBlockDef.StackElement se2 = def.getStackElementByIndex(1);
            String txt02 = se.getTextureByIndex(0);
            mod2.textures.put("particle", getTextureID(se2.getTextureByIndex(0)));
            int cnt2 = Math.max(6, se2.textures.size());
            String[] textures2 = new String[cnt2];
            for (int i = 0; i < cnt; i++) {
            	textures2[i] = se2.getTextureByIndex(i);
            }
            doCuboidModel(def.blockName+"_top", isTinted, txt02, textures2, se2.cuboids);
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = modelName();
        this.writeItemModelFile(def.blockName, mo);
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String mod = modelName();
        // Bottom states
        Variant var = new Variant();
        var.model = mod;
        var.y = 270;
        so.variants.put("facing=north,top=false", var);
        //
        var = new Variant();
        var.model = mod;
        so.variants.put("facing=east,top=false", var);
        //
        var = new Variant();
        var.model = mod;
        var.y = 90;
        so.variants.put("facing=south,top=false", var);
        //
        var = new Variant();
        var.model = mod;
        var.y = 180;
        so.variants.put("facing=west,top=false", var);
        // Top states
        mod = mod + "_top";
        var = new Variant();
        var.model = mod;
        var.y = 270;
        so.variants.put("facing=north,top=true", var);
        //
        var = new Variant();
        var.model = mod;
        so.variants.put("facing=east,top=true", var);
        //
        var = new Variant();
        var.model = mod;
        var.y = 90;
        so.variants.put("facing=south,top=true", var);
        //
        var = new Variant();
        var.model = mod;
        var.y = 180;
        so.variants.put("facing=west,top=true", var);
        
        this.writeBlockStateFile(def.blockName, so);
    }
}
