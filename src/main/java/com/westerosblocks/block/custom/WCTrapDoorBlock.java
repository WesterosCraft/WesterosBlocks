package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.util.BlockSetTypeUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WCTrapDoorBlock extends TrapdoorBlock implements ModBlockLifecycle {
    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCTrapDoorBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    private final ModBlock def;
    private boolean locked = false;

    protected WCTrapDoorBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(BlockSetTypeUtil.getBlockSetType(settings, def), settings);
        this.def = def;
        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String[] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("locked")) {
                    locked = flds[1].equals("true");
                }
            }
        }
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                 BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        } else {
            state = state.cycle(OPEN);
            world.setBlockState(pos, state, 2);
            if (state.get(WATERLOGGED)) {
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            this.playToggleSound(player, world, pos, state.get(OPEN));
            return ActionResult.success(world.isClient);
        }
    }

    private static final String[] TAGS = {"trapdoors"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
