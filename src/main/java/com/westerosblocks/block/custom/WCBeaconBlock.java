package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.List;

public class WCBeaconBlock extends WCCuboidBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeProperties();
            Block blk = new WCBeaconBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }
    
    private static WesterosBlockDef.Cuboid[] cuboids = { 
            new WesterosBlockDef.Cuboid(0f, 0f, 0f, 0f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 0f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 1f, 0f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(1f, 0f, 0f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0f, 1f, 0f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0f, 0f, 1f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0.125f, 0.00625f, 0.125f, 0.125f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
            new WesterosBlockDef.Cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.00625f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
            new WesterosBlockDef.Cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.125f, new int[] { 6, 7, 8, 9, 10, 11 }),
            new WesterosBlockDef.Cuboid(0.875f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
            new WesterosBlockDef.Cuboid(0.125f, 0.00625f, 0.1875f, 0.875f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
            new WesterosBlockDef.Cuboid(0.125f, 0.00625f, 0.875f, 0.875f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
            new WesterosBlockDef.Cuboid(0.1875f, 0.1875f, 0.1875f, 0.1875f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
            new WesterosBlockDef.Cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.1875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
            new WesterosBlockDef.Cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.1875f, new int[] { 12, 13, 14, 15, 16, 17 }),
            new WesterosBlockDef.Cuboid(0.8125f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
            new WesterosBlockDef.Cuboid(0.1875f, 0.875f, 0.1875f, 0.8125f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
            new WesterosBlockDef.Cuboid(0.1875f, 0.1875f, 0.8125f, 0.8125f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 })
    };
    private static List<WesterosBlockDef.Cuboid> cuboidlist = Arrays.asList(cuboids);
    
    protected WCBeaconBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings, def, 1);
        def.cuboids = cuboidlist;
        this.cuboid_by_facing[0] = cuboidlist;
        SHAPE_BY_INDEX[0] = getBoundingBoxFromCuboidList(this.cuboid_by_facing[0]);
    }
}
