package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WCSoundBlock extends WCSolidBlock {
	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def) {
			AbstractBlock.Settings settings = def.makeProperties().randomTicks();
			if ((def.soundList == null) || (def.soundList.size() == 0)) {
	            WesterosBlocks.LOGGER.error(String.format("Non-empty soundList rrquired for ''%s'", def.blockName));
	            return null;
			}
			if ((def.soundList != null) && (def.soundList.size() > 1)) {
				protoINDEX = IntProperty.of("index", 0, def.soundList.size()-1);
			}
			Block blk = new WCSoundBlock(settings, def);
			return def.registerRenderType(blk, true, def.nonOpaque);
		}
	}

	private static IntProperty protoINDEX; // Hand off for registration

	public static final BooleanProperty POWERED = Properties.POWERED;
	public IntProperty INDEX; // 0 = no sound selected, else soundList[val-1]
	public int playback_period;
	public int random_playback_addition;
	public int startTime;
	public int endTime;

	protected WCSoundBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
		super(settings, def);
		String type = def.getType();
		if (type != null) {
			String[] toks = type.split(",");
			for (String tok : toks) {
				String[] flds = tok.split(":");
				if (flds.length < 2)
					continue;
				if (flds[0].equals("period")) { // In seconds - convert to ticks
					playback_period = (int) Math.round(20.0 * Double.parseDouble(flds[1]));
				} else if (flds[0].equals("random-add")) { // In seconds - convert to ticks
					random_playback_addition = (int) Math.round(20.0 * Double.parseDouble(flds[1])); // In ticks
				} else if (flds[0].equals("start-time")) { // Start time (hour of day * 100) - convert to ticks
					startTime = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
				} else if (flds[0].equals("end-time")) { // End time (hour of day * 100) - convert to ticks
					endTime = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
				} else {
					WesterosBlocks.LOGGER.warn("Invalid type attribute '" + flds[0] + "' in " + def.blockName);
				}
			}
		}
		if (INDEX != null) {
			setDefaultState(getDefaultState().with(POWERED, Boolean.FALSE).with(INDEX, 1));
		}
		else {
			setDefaultState(getDefaultState().with(POWERED, Boolean.FALSE));
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos pos2, boolean chgflag) {
		boolean flag = world.hasNeighborSignal(pos);
		if (flag != state.get(POWERED)) {
			if (flag) {
				this.playNote(world, pos);
				world.scheduleTick(pos, this, getNextTriggerTick(world.random));
			}
			world.setBlock(pos, state.with(POWERED, Boolean.valueOf(flag)), 3);
		}

	}

	// Trigger block event to cause play of sound
	private void playNote(Level world, BlockPos pos) {
		world.blockEvent(pos, this, 0, 0);
	}

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                 BlockHitResult hit) {

		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else { // Rotate state on server side, and play new note
			if (INDEX != null) {
				int index = state.get(INDEX);
				index = (index + 1) % def.soundList.size();
				state = state.with(INDEX, index); // Rotate sounds selection
				world.setBlockState(pos, state, 3);
				//WesterosBlocks.log.info("WCSoundBlock.use(" + pos + ") - set to INDEX=" + index);
			}
			this.playNote(world, pos);
			world.scheduleTick(pos, this, getNextTriggerTick(world.random));
			return ActionResult.CONSUME;
		}
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos ppos, int eventID, int eventArg) {
		//WesterosBlocks.log.info("WCSoundBlock.trigger(" + ppos + ")");
		int i = 0;
		int sndindex = (INDEX != null) ? state.get(INDEX) : 0;
		String soundid = def.soundList.get(sndindex);
		SoundEvent event = WesterosBlocks.getRegisteredSound(soundid);
		if (event != null) {
			float f = (float) Math.pow(2.0D, (double) (i - 12) / 12.0D);
			world.playSound((Player) null, ppos, event, SoundSource.RECORDS, 3.0F, f);
			//WesterosBlocks.log.info("WCSoundBlock.trigger(" + ppos + ") - playing " + soundid);
		}
		return true;
	}

	// Compute time for next trigger
	private int getNextTriggerTick(RandomSource rnd) {
		return playback_period + rnd.nextInt(random_playback_addition + 1);
	}
	@SuppressWarnings("deprecation")
	@Override
   public void onPlace(BlockState state, Level world, BlockPos pos, BlockState state2, boolean flg) {
      super.onPlace(state, world, pos, state2, flg);
      // Add our tick schedule, if needed
      if (playback_period > 0) {
          world.scheduleTick(pos, this, getNextTriggerTick(world.random));
      }
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
	   return (playback_period >= 0);
   }

   @Override
   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
	   if (playback_period >= 0) {
			//WesterosBlocks.log.info("WCSoundBlock.randomTick(" + pos + ")");
		   // No tick?  Add it (migration)
		   if (!world.getBlockTicks().hasScheduledTick(pos, this)) {
				//WesterosBlocks.log.info("WCSoundBlock.randomTick(" + pos + ") - schuled");
				// Compute time for next trigger
				world.scheduleTick(pos, this, getNextTriggerTick(world.random));
		   }
	   }
   }

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rnd) {
		//WesterosBlocks.log.info("WCSoundBlock.tick(" + pos + ")");
		if (playback_period <= 0) { // Not periodic, so quit
			//WesterosBlocks.log.info("WCSoundBlock.tick(" + pos + ") - not periodic");
			return;
		}

		// Get time of day of trigger - see if we are going to trigger
		long wt = (world.getDayTime() % 24000);
		if (wt < 0)
			wt += 24000;
		//WesterosBlocks.log.info("WCSoundBlock.tick(" + pos + ") - consider sound at wt=" + wt + ",startTime=" + startTime + ",endTime=" + endTime);
		
		boolean trigger = true;
		if (this.startTime == this.endTime) {	// No limit?
			
		}
		else if (this.startTime > this.endTime) { // Split across 0 (e.g. nighttime)
			trigger = ((wt < this.endTime) || (wt >= this.startTime));// And between end and start
		}
		else {
			trigger = ((wt >= this.startTime) && (wt < this.endTime));
		}
		if (trigger) {
			//WesterosBlocks.log.info("WCSoundBlock.tick(" + pos + ") - trigger sound");
			playNote(world, pos);
		}
		// Compute time for next trigger
		world.scheduleTick(pos, this, getNextTriggerTick(world.random));
	}

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		if (protoINDEX != null) {
			INDEX = protoINDEX;
			protoINDEX = null;
		}
		// Only use index for blocks with multiple sounds in list
		if (INDEX != null) {
			builder.add(POWERED, INDEX);
		}
		else {
			builder.add(POWERED);
		}
	}
    private static String[] TAGS = {  };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
