package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.core.Direction;

public class WCStairBlock extends StairBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            if (def.modelBlockName == null) {
                WesterosBlocks.log.error("Type 'stair' requires modelBlockName settings");
                return null;
            }
            // Try to find model block
            Block blk = WesterosBlocks.findBlockByName(def.modelBlockName);
            if (blk == null) {
                WesterosBlocks.log.error(String.format("modelBlockName '%s' not found for block '%s'",
                        def.modelBlockName, def.blockName));
                return null;
            }
            if (blk.defaultBlockState().isAir()) {
                WesterosBlocks.log.error(String.format(
                        "modelBlockName '%s' not yet defined for block '%s' - must be defined before block",
                        def.modelBlockName, def.blockName));
                return null;
            }
            WesterosBlockDef mbdef = null;
            // If a WB, look up def
            if (blk instanceof WesterosBlockLifecycle) {
                mbdef = ((WesterosBlockLifecycle) blk).getWBDefinition();
            }
            BlockBehaviour.Properties props = def.makeAndCopyProperties(blk);
            return def.registerRenderType(def.registerBlock(new WCStairBlock(blk.defaultBlockState(), props, def)),
                    false, false);
        }
    }

    private WesterosBlockDef def;
    public final boolean no_uvlock;

    protected WCStairBlock(BlockState modelstate, BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(() -> modelstate, props);
        this.def = def;
        String t = def.getType();
        boolean no_uvlock = false;
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("no-uvlock")) {
                	no_uvlock = true;
                }
            }
        }
        this.no_uvlock = no_uvlock;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        super.createBlockStateDefinition(StateDefinition);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState bs = super.getStateForPlacement(ctx);
        return bs;
    }

    private static String[] TAGS = { "stairs" };

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
