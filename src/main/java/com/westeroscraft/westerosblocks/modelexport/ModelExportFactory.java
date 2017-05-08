package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.block.Block;

public class ModelExportFactory {
    public static ModelExport   forBlock(Block blk, WesterosBlockDef def, File dest) {
        ModelExport me = null;
        switch (def.blockType) {
            case "solid":
            case "soulsand":
            case "sound":
                me = new SolidBlockModelExport(blk, def, dest);
                break;
            case "stair":
                me = new StairsBlockModelExport(blk, def, dest);
                break;
            case "leaves":
                me = new LeavesBlockModelExport(blk, def, dest);
                break;
            case "log":
                me = new LogBlockModelExport(blk, def, dest);
                break;
            case "plant":
            case "web":
                me = new CrossBlockModelExport(blk, def, dest);
                break;
            case "pane":
                me = new PaneBlockModelExport(blk, def, dest);
                break;
            case "crop":
                me = new CropBlockModelExport(blk, def, dest);
                break;
            case "door":
                me = new DoorBlockModelExport(blk, def, dest);
                break;
            case "slab":
                me = new SlabBlockModelExport(blk, def, dest);
                break;
            case "fence":
                me = new FenceBlockModelExport(blk, def, dest);
                break;
            case "trapdoor":
                me = new TrapDoorBlockModelExport(blk, def, dest);
                break;
        }
        return me;
    }
}
