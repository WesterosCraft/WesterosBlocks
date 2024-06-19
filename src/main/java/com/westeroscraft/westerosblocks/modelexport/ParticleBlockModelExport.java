package com.westeroscraft.westerosblocks.modelexport;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.world.level.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class ParticleBlockModelExport extends ModelExport {

    public static class ModelObjectCubeAll {
        public String parent = "minecraft:block/cube_all";    // Use 'cube_all' model for single texture
        public TextureAll textures = new TextureAll();
    }

    public static class TextureAll {
        public String all;
    }

    public static class ModelObject {
        public String parent = "item/generated";
        public TextureAll textures = new TextureAll();
    }

    public ParticleBlockModelExport(Block block, WesterosBlockDef def, File dest) {
        super(block, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    @Override
    public void doBlockStateExport() throws IOException {
//        StateObject so = new StateObject();
//
//        this.writeBlockStateFile(def.blockName, so);
    }

    protected void doParticleBlockModel(String name) throws IOException {
        Object model;

        ModelObjectCubeAll mod = new ModelObjectCubeAll();
        mod.textures.all = getTextureID(def.getTextureByIndex(0));
        model = mod;

        this.writeBlockModelFile(name, model);
    }

    @Override
    public void doModelExports() throws IOException {
        for (int idx = 0; idx < def.states.size(); idx++) {
            doParticleBlockModel(def.blockName);
        }
    }
}
