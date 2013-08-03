package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCropBlock extends WCPlantBlock {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(int index, WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            return new WCCropBlock(index, def);
        }
    }

    protected WCCropBlock(int def_index, WesterosBlockDef def) {
        super(def_index, def);
    }

    @Override
    public int getRenderType() {
        return 6;   // Just switch to crop renderer
    }
}
