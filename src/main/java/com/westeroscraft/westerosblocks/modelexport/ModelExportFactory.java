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
            case "sand":
                me = new SolidBlockModelExport(blk, def, dest);
                break;
            case "sound":
                me = new SoundBlockModelExport(blk, def, dest);
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
            case "logvert":
                me = new LogVertBlockModelExport(blk, def, dest);
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
            case "wall":
                me = new WallBlockModelExport(blk, def, dest);
                break;
            case "trapdoor":
                me = new TrapDoorBlockModelExport(blk, def, dest);
                break;
            case "torch":
                me = new TorchBlockModelExport(blk, def, dest);
                break;
            case "ladder":
                me = new LadderBlockModelExport(blk, def, dest);
                break;
            case "fire":
                me = new FireBlockModelExport(blk, def, dest);
                break;
            case "bed":
                me = new BedBlockModelExport(blk, def, dest);
                break;
            case "cuboid":
            case "beacon":
                me = new CuboidBlockModelExport(blk, def, dest);
                break;
            case "cuboid-ne":
                me = new CuboidNEBlockModelExport(blk, def, dest);
                break;
            case "cuboid-nsew":
                me = new CuboidNSEWBlockModelExport(blk, def, dest);
                break;
            case "cuboid-nsew-stack":
                me = new CuboidNSEWStackBlockModelExport(blk, def, dest);
                break;
            case "cuboid-nsewud":
                me = new CuboidNSEWUDBlockModelExport(blk, def, dest);
                break;
            case "layer":
                me = new LayerBlockModelExport(blk, def, dest);
                break;
            case "rail":
                me = new RailBlockModelExport(blk, def, dest);
                break;
            case "halfdoor":
                me = new HalfDoorBlockModelExport(blk, def, dest);
                break;
            case "cake":
                me = new CakeBlockModelExport(blk, def, dest);
                break;
            case "furnace":
                me = new FurnaceBlockModelExport(blk, def, dest);
                break;
            case "vines":
                me = new VinesBlockModelExport(blk, def, dest);
                break;
            case "solidvert":
            case "soulsandvert":
                me = new SolidVertBlockModelExport(blk, def, dest);
                break;
            case "laddervert":
                me = new LadderVertBlockModelExport(blk, def, dest);
                break;
            case "solidhoriz":
                me = new SolidHorizBlockModelExport(blk, def, dest);
                break;
            case "plantvert":
            case "webvert":
                me = new CrossBlockVertModelExport(blk, def, dest);
                break;
        }
        return me;
    }
}
