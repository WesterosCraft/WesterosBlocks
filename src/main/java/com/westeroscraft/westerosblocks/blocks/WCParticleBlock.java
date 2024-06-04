package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WCParticleBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {
    private final WesterosBlockDef def;
    protected static final VoxelShape OFF_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    protected static final VoxelShape ON_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty PARTICLE_RANGE = IntegerProperty.create("range", 1, 100);
    public static final IntegerProperty PARTICLE_TYPE = IntegerProperty.create("type", 1, 100);
    public static final IntegerProperty PARTICLE_STRENGTH = IntegerProperty.create("strength", 1, 100);


    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            BlockBehaviour.Properties props = def.makeProperties();

            return def.registerRenderType(def.registerBlock(new WCParticleBlock(props, def)), false, true);
        }
    }

    protected WCParticleBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(POWERED)) {
            return ON_SHAPE;
        } else {
            return OFF_SHAPE;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
        super.animateTick(state, level, pos, rnd);

        if (state.getValue(POWERED)) {
            for (int i = 0; i < state.getValue(PARTICLE_STRENGTH); i++) {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double) pos.getX() - (state.getValue(PARTICLE_RANGE) / 2) + rnd.nextDouble(state.getValue(PARTICLE_RANGE)), (double) pos.getY() - (state.getValue(PARTICLE_RANGE) / 2) + rnd.nextDouble(state.getValue(PARTICLE_RANGE)), (double) pos.getZ() - (state.getValue(PARTICLE_RANGE) / 2) + rnd.nextDouble(state.getValue(PARTICLE_RANGE)), 0.0D, 0.0D, 0.00);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter p_58056_, BlockPos p_58057_,
                                        CollisionContext p_58058_) {
        if (state.getValue(POWERED)) {
            return Shapes.empty();
        } else {
            return OFF_SHAPE;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, PARTICLE_TYPE, PARTICLE_STRENGTH, PARTICLE_RANGE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_TYPE, 1).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor world, BlockPos pos,
                                  BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return super.updateShape(state, dir, state2, world, pos, pos2);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private static final String[] TAGS = {};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
