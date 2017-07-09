package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.modelexport.CuboidBlockModelExport.ModelObject;

import net.minecraft.block.Block;

public class CuboidNSEWUDBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWUDBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            so.variants.put(String.format("facing=north,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 90;
            so.variants.put(String.format("facing=east,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 180;
            so.variants.put(String.format("facing=south,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 270;
            so.variants.put(String.format("facing=west,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.x = 270;
            so.variants.put(String.format("facing=up,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.x = 90;
            so.variants.put(String.format("facing=down,variant=%d", sb.meta), var);
        }
        this.writeBlockStateFile(def.blockName, so);
    }
    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            boolean isTinted = sb.isTinted(def);
            doCuboidModel(sb, sb.meta, sb.meta + 6, def.blockName, isTinted);
            // Build simple item model that refers to block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_" + sb.meta;
            this.writeItemModelFile(def.blockName + "_" + sb.meta, mo);
            // Handle tint resources
            if (isTinted) {
                String tintres = def.getBlockColorMapResource(sb);
                if (tintres != null) {
                    ModelExport.addTintingOverride(def.blockName, String.format("variant=%s", sb.meta), tintres);
                }
            }
        }
    }
}
