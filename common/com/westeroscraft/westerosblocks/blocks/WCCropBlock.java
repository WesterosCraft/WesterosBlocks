package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCCropBlock extends WCPlantBlock {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            return new Block[] { new WCCropBlock(def) };
        }
    }

    protected WCCropBlock(WesterosBlockDef def) {
        super(def);
    }

    @Override
    public int getRenderType() {
        return 6;   // Just switch to crop renderer
    }
}
