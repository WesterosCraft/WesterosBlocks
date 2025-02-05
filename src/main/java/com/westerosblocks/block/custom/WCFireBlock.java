package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.particle.ModParticleEffect;
import com.westerosblocks.particle.WesterosParticleSystem;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public class WCFireBlock extends FireBlock implements ModBlockLifecycle {
    private ModBlock def;

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings().noCollision().breakInstantly(); //.randomTicks();
            Block blk = new WCFireBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    protected WCFireBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
    }

    // prevents fire from being replaced by other fire blocks
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    // prevents vanilla fire spread mechanics. might wanna control via prop type at some point or just let it spread and let anti-grief mods handle it
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        //no-op to prevent spread
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(24) == 0) {
            world.playSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    SoundEvents.BLOCK_FIRE_AMBIENT,
                    SoundCategory.BLOCKS,
                    1.0F + random.nextFloat(),
                    random.nextFloat() * 0.7F + 0.3F,
                    false
            );
        }

        if (def.particle != null) {
            WesterosParticleSystem.spawnFireParticles(world, pos, random, def.particle);
        }
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BASE_SHAPE;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(this)) {
            super.onBlockAdded(state, world, pos, oldState, notify);
        }
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    private static final String[] TAGS = {"fire"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
