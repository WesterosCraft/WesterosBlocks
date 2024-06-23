package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WCParticleBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            BlockBehaviour.Properties props = def.makeProperties().noCollission().noOcclusion();

            return def.registerRenderType(def.registerBlock(new WCParticleBlock(props, def)), false, true);
        }
    }

    private final WesterosBlockDef def;
    protected static final VoxelShape OFF_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    protected static final VoxelShape ON_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty PARTICLE_RANGE = IntegerProperty.create("range", 1, 100);
    public static final IntegerProperty PARTICLE_STRENGTH = IntegerProperty.create("strength", 1, 100);


    protected WCParticleBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;

        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, Boolean.valueOf(false)).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1).setValue(WATERLOGGED, Boolean.valueOf(false)));
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
        final String ptype = def.getTypeValue("particleType");
        super.animateTick(state, level, pos, rnd);

        if (state.getValue(POWERED)) {
            for (int i = 0; i < state.getValue(PARTICLE_STRENGTH); i++) {
                level.addParticle(getParticleByType(ptype), true, (double) pos.getX() - (state.getValue(PARTICLE_RANGE) / 2) + rnd.nextDouble(state.getValue(PARTICLE_RANGE)), (double) pos.getY() - (state.getValue(PARTICLE_RANGE) / 2) + rnd.nextDouble(state.getValue(PARTICLE_RANGE)), (double) pos.getZ() - (state.getValue(PARTICLE_RANGE) / 2) + rnd.nextDouble(state.getValue(PARTICLE_RANGE)), 0.0D, 0.0D, 0.00);
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
        builder.add(POWERED, PARTICLE_STRENGTH, PARTICLE_RANGE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
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

//    public static void refreshBlockStates(Level level, BlockPos pos, BlockState state, Integer strengthChange, Integer rangeChange) {
//
//        //calculate the new blockstate value
//        int strength = state.getValue(PARTICLE_STRENGTH) + strengthChange;
//         int range = state.getValue(PARTICLE_RANGE) + rangeChange;
//
//        //test if new value is higher than the max value, and reset it if to high
//        if (strength > 10) 	strength = 1;
//        if (range > 10)     range = 1;
//
//        level.setBlock(pos, this.defaultBlockState().setValue(POWERED, true).setValue(PARTICLE_STRENGTH, strength).setValue(PARTICLE_RANGE, range).setValue(ParticleBlock.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER)), 11)
//
//    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitrslt) {

        level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

        // set POWER on first use
        if (!state.getValue(POWERED) && player.getMainHandItem().isEmpty()) {
            level.setBlock(pos, this.defaultBlockState().setValue(POWERED, true).setValue(PARTICLE_STRENGTH, state.getValue(PARTICLE_STRENGTH)).setValue(WATERLOGGED, Boolean.valueOf(level.getFluidState(pos).getType() == Fluids.WATER)), 11);
        }

        if (state.getValue(POWERED) && player.isShiftKeyDown()) {
            // testing - just cycle through strength of particle for now
//            this.refreshBlockStates(world, pos, state, +1, 0);

            //calculate the new blockstate value
            int strength = state.getValue(PARTICLE_STRENGTH);
            int range = state.getValue(PARTICLE_RANGE) + 1;

            //test if new value is higher than the max value, and reset it if to high
            if (strength > 10) 	strength = 1;
            if (range > 10)     range = 1;

            level.setBlock(pos, this.defaultBlockState().setValue(POWERED, true).setValue(PARTICLE_STRENGTH, strength).setValue(PARTICLE_RANGE, range).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER), 11);


        } else {
            return InteractionResult.PASS;
        }

        return InteractionResult.sidedSuccess(level.isClientSide);


//        if ( player.isCreative() && player.getMainHandItem().isEmpty()) {
////            state = state.cycle(this.STATE);
//            level.setBlock(pos, state, 10);
//            level.levelEvent(player, 1006, pos, 0);
//            return InteractionResult.sidedSuccess(level.isClientSide);
//        }
//        else {
//            return InteractionResult.PASS;
//        }
    }

    private static final String[] TAGS = {};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

    public static SimpleParticleType getParticleByType(String type) {
        return switch (type) {
            case "campfire_cosy_smoke" -> ParticleTypes.CAMPFIRE_COSY_SMOKE;
            case "campfire_signal_smoke" -> ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
            case "smoke" -> ParticleTypes.SMOKE;
            case "large_smoke" -> ParticleTypes.LARGE_SMOKE;
            case "ash" -> ParticleTypes.ASH;
            case "bubble" -> ParticleTypes.BUBBLE;
            default -> ParticleTypes.CLOUD;
        };
    }
}
