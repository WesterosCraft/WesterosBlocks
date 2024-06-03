package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WCParticleBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {
    private final WesterosBlockDef def;
    protected static final VoxelShape BLOCK_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty PARTICLE_TYPE = IntegerProperty.create("type", 1, 100);
    public static final IntegerProperty PARTICLE_STRENGTH = IntegerProperty.create("strength", 1, 100);
    public static final IntegerProperty PARTICLE_RANGE = IntegerProperty.create("range", 1, 100);

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
            return BLOCK_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter p_58056_, BlockPos p_58057_,
                                        CollisionContext p_58058_) {
        return BLOCK_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, PARTICLE_TYPE, PARTICLE_STRENGTH, PARTICLE_RANGE, WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_TYPE, 1).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    private static final String[] TAGS = { "particles" };

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
