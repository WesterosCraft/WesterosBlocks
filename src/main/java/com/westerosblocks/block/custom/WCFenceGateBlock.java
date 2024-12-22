package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class WCFenceGateBlock extends FenceGateBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties();
            Block blk = new WCFenceGateBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean locked = false;

    protected WCFenceGateBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(AuxileryUtils.getWoodType(settings, def), settings);
        this.def = def;
        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String [] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("locked")) {
                    locked = flds[1].equals("true");
                }
            }
        }
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.locked) {
            if (player.isCreative() && player.getMainHandItem().isEmpty()) {
                return super.useWithoutItem(state, level, pos, player, hitResult);
            }
            else {
                return InteractionResult.PASS;
            }
        }
        else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    private static String[] TAGS = { "fence_gates" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
