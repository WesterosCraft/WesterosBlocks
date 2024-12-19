package com.westerosblocks.needsported.modelexport;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.world.level.block.Block;

import java.io.File;
import java.io.IOException;


public class ParticleBlockModelExport extends ModelExport {

    public static class ModelItemObject {
        public String parent = "item/generated";
    }

    public ParticleBlockModelExport(Block block, WesterosBlockDef def, File dest) {
        super(block, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();

        Variant var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/custom/particle_emitter/particle_emitter_off";
        so.addVariant("powered=false", var, null);

        var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/custom/particle_emitter/particle_emitter_on";
        so.addVariant("powered=true", var, null);

        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        // Build simple item model that refers to block model
        String item = WesterosBlocks.MOD_ID + ":block/custom/particle_emitter/particle_emitter_off";
        ModelItemObject mo = new ModelItemObject();
        mo.parent = item;
        this.writeItemModelFile(def.blockName, mo);
    }
}
