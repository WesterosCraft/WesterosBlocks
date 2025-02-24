package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.block.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WCCuboidVertical8WayBlock extends WCCuboidBlock implements ModBlockLifecycle {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 7);
    private static final int ROTATIONS = 8;
    private boolean allow_unsupported = false;

    private static final ModBlock.CuboidRotation[] shape_rotations = {
            ModBlock.CuboidRotation.NONE,
            ModBlock.CuboidRotation.ROTY90,
            ModBlock.CuboidRotation.ROTY180,
            ModBlock.CuboidRotation.ROTY270
    };

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.applyCustomProperties();
            ModBlock.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            Block blk = new WCCuboidVertical8WayBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    protected WCCuboidVertical8WayBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings, def, ROTATIONS);

        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
            }
        }

        // Build rotations - one set for each state, if needed
        int stcnt = def.states.size();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            int idx = ROTATIONS * stidx;
            for (int i = 1; i < ROTATIONS; i++) {
                for (ModBlock.Cuboid c : cuboid_by_facing[idx]) {
                    cuboid_by_facing[idx + i].add(c.rotateCuboid(shape_rotations[i / 2]));
                }
            }
        }
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
            }
        }

        BlockState defbs = this.getDefaultState()
                .with(ROTATION, 0)
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false);
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, ROTATION);
    }

    @Override
    protected int getIndexFromState(BlockState state) {
        if (STATE != null) {
            return (STATE.getIndex(state.get(STATE)) * ROTATIONS) + state.get(ROTATION);
        } else {
            return state.get(ROTATION);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (allow_unsupported) return true;
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                BlockState state = this.getDefaultState()
                        .with(FACING, opposite)
                        .with(ROTATION, 0)
                        .with(WATERLOGGED, world.getFluidState(pos).isIn(FluidTags.WATER));

                if (STATE != null) {
                    state = state.with(STATE, STATE.defValue);
                }

                if (state.canPlaceAt(world, pos)) {
                    return state;
                }
            }
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
                .with(ROTATION, rotation.rotate(state.get(ROTATION), 8));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(FACING, mirror.apply(state.get(FACING)))
                .with(ROTATION, mirror.mirror(state.get(ROTATION), 8));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.isSneaking() && player.isCreative()) {
            if (player.getMainHandStack().isEmpty() || player.getMainHandStack().isOf(this.asItem())) {
                if (!world.isClient) {
                    int currentRotation = state.get(ROTATION);
                    int newRotation = (currentRotation + 1) % 8;

                    BlockState newState = state.with(ROTATION, newRotation);
                    world.setBlockState(pos, newState, Block.NOTIFY_ALL);
                    world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
                return ActionResult.success(world.isClient);
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }
}