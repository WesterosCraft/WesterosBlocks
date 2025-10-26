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

import java.util.Map;

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

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            String woodType = (String) parameters.getOrDefault("woodType", "oak");
            boolean locked = (Boolean) parameters.getOrDefault("locked", false);
            return new WCTrapDoorBlock(settings, null, woodType, locked);
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

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
