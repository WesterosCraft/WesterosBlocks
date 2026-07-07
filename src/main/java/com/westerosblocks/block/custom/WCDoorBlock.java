package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModBlockSetType;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WCDoorBlock extends DoorBlock implements WCBlockDef {
    protected BlockDefinition def;
    private final boolean locked;
    private final boolean allowUnsupported;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            String woodType = definition.getWoodType() != null ? definition.getWoodType() : "oak";
            boolean locked = definition.isLocked();
            boolean allowUnsupported = definition.isAllowUnsupported();
            return new WCDoorBlock(settings, definition, woodType, locked, allowUnsupported);
        }
    }

    public WCDoorBlock(AbstractBlock.Settings settings, BlockDefinition def, String woodType,
            boolean locked, boolean allowUnsupported) {
        super(ModBlockSetType.getBlockSetType(woodType), settings);
        this.def = def;
        this.locked = locked;
        this.allowUnsupported = allowUnsupported;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }

    // Ports 1.18.2 MixinDoorBlock's doorNoConnect=true behavior: no sturdy faces,
    // so walls/fences/panes never connect to doors regardless of open state.
    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (allowUnsupported && (state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)) {
            return true;
        }
        return super.canPlaceAt(state, world, pos);
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
