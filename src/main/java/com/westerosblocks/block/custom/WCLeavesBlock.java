package com.westerosblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WCLeavesBlock extends LeavesBlock implements WCBlockDef {
    public static final MapCodec<WCLeavesBlock> CODEC = createCodec(settings -> new WCLeavesBlock(settings, null, false, false, false));
    protected BlockDefinition def;
    public final boolean betterFoliage;
    public final boolean overlay;
    public final boolean noDecay;

    @Override
    public MapCodec<? extends LeavesBlock> getCodec() {
        return CODEC;
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();

            boolean betterFoliage = definition.hasBetterFoliage();
            boolean overlay = definition.hasOverlay();
            boolean noDecay = definition.isNoDecay();

            return new WCLeavesBlock(settings, definition, betterFoliage, overlay, noDecay);
        }
    }

    protected WCLeavesBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean betterFoliage, boolean overlay, boolean noDecay) {
        super(0.0F, settings.nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, reader, pos) -> false));
        this.def = def;
        this.betterFoliage = betterFoliage;
        this.overlay = overlay;
        this.noDecay = noDecay;
        setDefaultState(this.getDefaultState().with(DISTANCE, 7).with(PERSISTENT, !noDecay));
    }

    @Override
    protected void spawnLeafParticle(World world, BlockPos pos, Random random) {
        // No leaf particles for WesterosBlocks leaves
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
