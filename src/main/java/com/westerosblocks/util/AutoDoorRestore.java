package com.westerosblocks.util;

import com.westerosblocks.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AutoDoorRestore {
    private static class PendingRestore {
        BlockPos pos;
        World world;

        PendingRestore(World lvl, BlockPos p) {
            this.world = lvl;
            this.pos = p;
        }

        @Override
        public int hashCode() {
            return pos.hashCode() ^ world.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PendingRestore) {
                PendingRestore pdo = (PendingRestore) o;
                return (pdo.world == this.world) && (pdo.pos.asLong() == this.pos.asLong());
            }
            return false;
        }
    };

    private static class RestoreInfo {
        long secCount;
        Boolean open;
    };
    private static Map<PendingRestore, RestoreInfo> pendingHalfDoorRestore = new HashMap<>();
    private static int ticks = 0;
    private static long secCount = 0;

    public static boolean isAutoRestoreHalfDoor(Block blk) {
        if (ModConfig.get().autoRestoreAllHalfDoors) return true;
        return false;
    }

    public static void setPendingHalfDoorRestore(World world, BlockPos pos, boolean isOpen, boolean isCreative) {
        PendingRestore pdc = new PendingRestore(world, pos);
        RestoreInfo ri = pendingHalfDoorRestore.get(pdc);
        if ((ri == null) && (!isCreative)) {    // New one, and not creative mode, add record
            ri = new RestoreInfo();
            ri.open = isOpen;
            ri.secCount = secCount + ModConfig.get().autoRestoreTime;
            pendingHalfDoorRestore.put(pdc, ri);
        }
        // Else, if restore record pending, but creative change, drop it
        else if (ri != null) {
            if (isCreative) {
                pendingHalfDoorRestore.remove(pdc);
            } else {    // Else, reset restore time
                ri.secCount = secCount + ModConfig.get().autoRestoreTime;
            }
        }
    }

    public static void handlePendingHalfDoorRestores(boolean now) {
        // Handle pending door close checks
        Set<Map.Entry<PendingRestore, RestoreInfo>> kvset = pendingHalfDoorRestore.entrySet();
        Iterator<Map.Entry<PendingRestore, RestoreInfo>> iter = kvset.iterator();    // So that we can remove during iteration
        while (iter.hasNext()) {
            Map.Entry<PendingRestore, RestoreInfo> kv = iter.next();
            PendingRestore pdc = kv.getKey();
            RestoreInfo ri = kv.getValue();
            if (now || (ri.secCount <= secCount)) {
                BlockState bs = pdc.world.getBlockState(pdc.pos);    // Get the block state
                if (bs != null) {
                    Block blk = bs.getBlock();
                    // TODO
//                    if ((blk instanceof WCHalfDoorBlock) && isAutoRestoreHalfDoor(blk)) {    // Still right type of door
//                        if (bs.getValue(DoorBlock.OPEN) != ri.open) {    // And still wrong state?
//                            WCHalfDoorBlock dblk = (WCHalfDoorBlock) blk;
//                            dblk.setOpen(null, pdc.world, bs, pdc.pos, ri.open);
//                        }
//                    }
                }
                iter.remove();    // And remove it from the set
            }
        }
    }

    //    @SubscribeEvent
//    public void countTicks(ServerTickEvent event) {
//        if (event.phase != TickEvent.Phase.END) return;
//        ticks++;
//        if (ticks >= 20) {
//            secCount++;
//            // Handle any pending door restores
//            handlePendingHalfDoorRestores(false);
//
//            ticks = 0;
//        }
//    }
}
