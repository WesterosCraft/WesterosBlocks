package com.westeroscraft.westerosblocks.blocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCBeaconBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCBeaconBlock(props, def)), false, false);
        }
    }
    
    private static WesterosBlockDef.Cuboid[] cuboids = { 
            new WesterosBlockDef.Cuboid(0f, 0f, 0f, 0f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 0f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 1f, 0f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(1f, 0f, 0f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            //new WesterosBlockDef.Cuboid(0f, 1f, 0f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
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
    
    protected WCBeaconBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def);
        def.cuboids = cuboidlist;
        SHAPE_BY_INDEX[0] = getBoundingBoxFromCuboidList(def.getCuboidList());
    }
}
