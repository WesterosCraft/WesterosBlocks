package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.tileentity.WCFurnaceTileEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.util.SoundEvents;

// Custom furnace block
public class WCFurnaceBlock extends FurnaceBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {
	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def) {
			final boolean alwaysOnVal = def.getTypeValue("always-on").equals("true");
			AbstractBlock.Properties props = def.makeProperties().lightLevel((state) -> {
				return (alwaysOnVal || state.getValue(BlockStateProperties.LIT)) ? (int) (16 * def.lightValue) : 0;
			});
			Block blk = def.registerRenderType(def.registerBlock(new WCFurnaceBlock(props, def)), true, def.nonOpaque);
			// Register tile entity
			WesterosBlockDef.registerTileEntity(WCFurnaceTileEntity.ENTITYTYPE, WCFurnaceTileEntity::new, blk);

			return blk;

		}
	}

	private WesterosBlockDef def;
	private boolean alwaysOn;

	protected WCFurnaceBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
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
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		boolean lit = state.getValue(LIT);
		boolean active = alwaysOn || lit;

		if (active) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY();
			double d2 = (double) pos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F,
						false);
			}

			Direction direction = state.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = random.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
			double d6 = random.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
			world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader reader) {
		return new WCFurnaceTileEntity();
	}

	@Override
	protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof WCFurnaceTileEntity) {
			player.openMenu((INamedContainerProvider) tileentity);
		}
	} 

	@Override
	public void registerDynmapRenderData(ModTextureDefinition mtd) {
		def.defaultRegisterTextures(mtd);
		def.defaultRegisterTextureBlock(mtd);
	}

	private static String[] TAGS = {};

	@Override
	public String[] getBlockTags() {
		return TAGS;
	}
}
