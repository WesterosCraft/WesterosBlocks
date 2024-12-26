package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WCRailBlock extends RailBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeBlockSettings().nonOpaque().noCollision();

            Block blk = new WCRailBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    private final WesterosBlockDef def;
    private boolean allow_unsupported = false;

    protected WCRailBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                    break;
                }
            }
        }

    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allow_unsupported) return true;
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos,
                                  boolean notify) {
        if (!this.allow_unsupported) {
            super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        } else if (!world.isClient && world.getBlockState(pos).isOf(this)) {
            this.updateBlockState(state, world, pos, sourceBlock);
        }
    }

    private static final String[] TAGS = {"rails"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
