package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModBlockSetType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WCTrapDoorBlock extends TrapdoorBlock {
    protected BlockDefinition def;
    private final boolean locked;

    public WCTrapDoorBlock(AbstractBlock.Settings settings, BlockDefinition def, String woodType, boolean locked) {
        super(ModBlockSetType.getBlockSetType(woodType), settings);
        this.def = def;
        this.locked = locked;
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            String woodType = definition.getWoodType() != null ? definition.getWoodType() : "oak";
            boolean locked = definition.isLocked();
            return new WCTrapDoorBlock(settings, definition, woodType, locked);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
