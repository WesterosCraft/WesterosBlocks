//package com.westerosblocks.block.custom;
//
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockFactory;
//import com.westerosblocks.block.WesterosBlockLifecycle;
//import net.minecraft.block.AbstractBlock;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.FurnaceBlock;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.particle.ParticleTypes;
//import net.minecraft.stat.Stats;
//import net.minecraft.state.property.Properties;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.math.random.Random;
//import net.minecraft.world.World;
//
//// Custom furnace block
//public class WCFurnaceBlock extends FurnaceBlock implements WesterosBlockLifecycle {
//	public static class Factory extends WesterosBlockFactory {
//		@Override
//		public Block buildBlockClass(WesterosBlockDef def) {
//			final boolean alwaysOnVal = def.getTypeValue("always-on").equals("true");
//			AbstractBlock.Settings settings = def.makeBlockSettings().lightLevel((state) -> {
//				return (alwaysOnVal || state.get(Properties.LIT)) ? (int) (16 * def.lightValue) : 0;
//			});
//			Block blk = new WCFurnaceBlock(settings, def);
//			Block rblk = def.registerRenderType(blk, true, def.nonOpaque);
//			// Register tile entity
//			WesterosBlockDef.registerBlockEntity(WCFurnaceBlockEntity.ENTITYTYPE, WCFurnaceBlockEntity::new, rblk);
//			return rblk;
//
//		}
//	}
//
//	private WesterosBlockDef def;
//	private boolean alwaysOn;
//
//	protected WCFurnaceBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
//		super(settings);
//		this.def = def;
//		alwaysOn = def.getTypeValue("always-on").equals("true");
//		this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
//	}
//
//	@Override
//	public WesterosBlockDef getWBDefinition() {
//		return def;
//	}
//
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
//		boolean lit = state.get(LIT);
//		boolean active = alwaysOn || lit;
//
//		if (active) {
//			double d0 = (double) pos.getX() + 0.5D;
//			double d1 = (double) pos.getY();
//			double d2 = (double) pos.getZ() + 0.5D;
//			if (random.nextDouble() < 0.1D) {
//				world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F,
//						false);
//			}
//
//			Direction direction = state.get(FACING);
//			Direction.Axis direction$axis = direction.getAxis();
//			double d4 = random.nextDouble() * 0.6D - 0.3D;
//			double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
//			double d6 = random.nextDouble() * 6.0D / 16.0D;
//			double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
//			world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
//			world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
//		}
//	}
//
//	@Override
//	public BlockEntity newBlockEntity(BlockPos pos, BlockState bs) {
//		return new WCFurnaceBlockEntity(pos, bs);
//	}
//
//	@Override
//	protected void openContainer(Level world, BlockPos pos, Player player) {
//		BlockEntity blockentity = world.getBlockEntity(pos);
//		if (blockentity instanceof WCFurnaceBlockEntity) {
//	         player.openMenu((MenuProvider)blockentity);
//	         player.awardStat(Stats.INTERACT_WITH_FURNACE);
//		}
//	}
//
//	private static String[] TAGS = {};
//
//	@Override
//	public String[] getBlockTags() {
//		return TAGS;
//	}
//}
