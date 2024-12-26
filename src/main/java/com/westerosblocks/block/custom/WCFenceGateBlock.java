package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import com.westerosblocks.util.WoodTypeUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WCFenceGateBlock extends FenceGateBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCFenceGateBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    private final WesterosBlockDef def;
    private boolean locked = false;

    protected WCFenceGateBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(WoodTypeUtil.getWoodType(def.getWoodType()), settings);
        this.def = def;
        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String[] flds = tok.split(":");
                if (flds.length < 3) continue;
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                 BlockHitResult hit) {
        if (this.locked) {
            Hand hand = player.getActiveHand();
            if (player.isCreative() && player.getStackInHand(hand).isEmpty()) {
                return super.onUse(state, world, pos, player, hit);
            } else {
                return ActionResult.PASS;
            }
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }

    private static final String[] TAGS = {"fence_gates"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
