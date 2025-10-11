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
    private final boolean locked;

    public WCTrapDoorBlock(AbstractBlock.Settings settings, String woodType, boolean locked) {
        super(ModBlockSetType.getBlockSetType(woodType), settings);
        this.locked = locked;
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            // Handle null definition with sensible defaults
            String woodType = definition != null ? definition.getWoodType() : "oak";
            boolean locked = definition != null && definition.isLocked();
            return new WCTrapDoorBlock(settings, woodType, locked);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition, java.util.Map<String, Object> parameters) {
            // Extract woodType from parameters or definition
            String woodType = "oak";
            if (parameters != null && parameters.containsKey("woodType")) {
                woodType = (String) parameters.get("woodType");
            } else if (definition != null) {
                woodType = definition.getWoodType();
            }

            // Extract locked from parameters or definition
            boolean locked = false;
            if (parameters != null && parameters.containsKey("locked")) {
                locked = (Boolean) parameters.get("locked");
            } else if (definition != null) {
                locked = definition.isLocked();
            }

            return new WCTrapDoorBlock(settings, woodType, locked);
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
}
