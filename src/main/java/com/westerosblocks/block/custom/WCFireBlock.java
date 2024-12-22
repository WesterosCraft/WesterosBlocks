package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class WCFireBlock extends FireBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties().noCollision().breakInstantly(); //.randomTicks();
            Block blk = new WCFireBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }
    private WesterosBlockDef def;

    protected WCFireBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    	return true;
    }


    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        super.tick(state, level, pos, random);
    }

    @Override
    public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
    	return false;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    private static String[] TAGS = { "fire" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
