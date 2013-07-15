package com.westeroscraft.westerosblocks.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

public class WCStairBlock extends BlockStairs implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(int index, WesterosBlockDef def) {
            if ((def.modelBlockName == null) || (def.modelBlockMeta < 0)) {
                WesterosBlocks.log.severe("Type 'stair' requires modelBlockName and modelBlockMeta settings");
                return null;
            }
            // Try to find model block
            Block blk = WesterosBlocks.findBlockByName(def.modelBlockName);
            if (blk == null) {
                WesterosBlocks.log.severe(String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
                return null;
            }
            // Validate meta : we require meta 0, and only allow it
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }

            return new WCStairBlock(index, def, blk);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCStairBlock(int def_index, WesterosBlockDef def, Block blk) {
        super(def.blockID, blk, def.modelBlockMeta);
        this.def = def;
        this.setCreativeTab(def.getCreativeTab());
        this.setUnlocalizedName(def.blockName);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }

}
