package com.westerosblocks.needsported.blocks;

import java.util.Random;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.tileentity.WCFurnaceBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.RegisterEvent;

// Custom furnace block
public class WCFurnaceBlock extends FurnaceBlock implements WesterosBlockLifecycle {
	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
			final boolean alwaysOnVal = def.getTypeValue("always-on").equals("true");
			BlockBehaviour.Properties props = def.makeProperties().lightLevel((state) -> {
				return (alwaysOnVal || state.getValue(BlockStateProperties.LIT)) ? (int) (16 * def.lightValue) : 0;
			});
			Block blk = new WCFurnaceBlock(props, def);
			helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
			def.registerBlockItem(def.blockName, blk);
			Block rblk = def.registerRenderType(blk, true, def.nonOpaque);
			// Register tile entity
			WesterosBlockDef.registerBlockEntity(WCFurnaceBlockEntity.ENTITYTYPE, WCFurnaceBlockEntity::new, rblk);

			return rblk;

		}
	}

	private WesterosBlockDef def;
	private boolean alwaysOn;

	protected WCFurnaceBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
		super(props);
		this.def = def;
		alwaysOn = def.getTypeValue("always-on").equals("true");
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	public WesterosBlockDef getWBDefinition() {
		return def;
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		boolean lit = state.getValue(LIT);
		boolean active = alwaysOn || lit;

		if (active) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY();
			double d2 = (double) pos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F,
						false);
			}

			Direction direction = state.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d4 = random.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
			double d6 = random.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
			world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState bs) {
		return new WCFurnaceBlockEntity(pos, bs);
	}

	@Override
	protected void openContainer(Level world, BlockPos pos, Player player) {
		BlockEntity blockentity = world.getBlockEntity(pos);
		if (blockentity instanceof WCFurnaceBlockEntity) {
	         player.openMenu((MenuProvider)blockentity);
	         player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	} 

	private static String[] TAGS = {};

	@Override
	public String[] getBlockTags() {
		return TAGS;
	}
}
