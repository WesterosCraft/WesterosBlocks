package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCBeaconBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCBeaconBlock(def) };
        }
    }
    
    private static WesterosBlockDef.Cuboid[] cuboids = { 
            new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),
            new WesterosBlockDef.Cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
            new WesterosBlockDef.Cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 })
    };
    @SuppressWarnings("unchecked")
    private static List<WesterosBlockDef.Cuboid> cuboidlist = Arrays.asList(cuboids);
    
    protected WCBeaconBlock(WesterosBlockDef def) {
        super(def);
        for (Subblock sb : def.subBlocks) {
            sb.cuboids = cuboidlist;
            setBoundingBoxFromCuboidList(sb.meta);
        }
    }
            
    /**
     *  Get cuboid list at given meta
     *  @param meta
     */
    public List<WesterosBlockDef.Cuboid> getCuboidList(int meta) {
        return cuboidlist;
    }
}
