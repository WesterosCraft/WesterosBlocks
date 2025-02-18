package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.sound.ModSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;

import java.util.List;

public class WCSoundBlock extends WCSolidBlock {
    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {

            if ((def.soundList == null) || (def.soundList.isEmpty())) {
                WesterosBlocks.LOGGER.error("Non-empty soundList required for '{}'", def.blockName);
                return null;
            }

            if (def.soundList.size() > 1) {
                tempINDEX = IntProperty.of("index", 0, def.soundList.size() - 1);
            }

            AbstractBlock.Settings settings = def.applyCustomProperties();
            Block blk = new WCSoundBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), true, def.nonOpaque);
        }
    }

    private static IntProperty tempINDEX;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public final IntProperty INDEX; // 0 = no sound selected, else soundList[val-1]
    public final int playbackPeriod; // In ticks
    public final int randomPlaybackAddition; // In ticks
    public final int startTime; // In ticks (0-24000)
    public final int endTime; // In ticks (0-24000)

    protected WCSoundBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings, def);
        // Process type attributes
        int period = 0;
        int randomAdd = 0;
        int start = 0;
        int end = 0;

        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String[] flds = tok.split(":");
                if (flds.length < 2) continue;

                switch (flds[0]) {
                    case "period":
                        period = (int) Math.round(20.0 * Double.parseDouble(flds[1]));
                        break;
                    case "random-add":
                        randomAdd = (int) Math.round(20.0 * Double.parseDouble(flds[1]));
                        break;
                    case "start-time":
                        start = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
                        break;
                    case "end-time":
                        end = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
                        break;
                    default:
                        WesterosBlocks.LOGGER.warn("Invalid type attribute '{}' in {}", flds[0], def.blockName);
                }
            }
        }

        this.playbackPeriod = period;
        this.randomPlaybackAddition = randomAdd;
        this.startTime = start;
        this.endTime = end;

        if (tempINDEX != null) {
            this.INDEX = tempINDEX;
            tempINDEX = null;
            this.setDefaultState(this.getDefaultState()
                    .with(POWERED, false)
                    .with(INDEX, 1));
        } else {
            this.INDEX = null;
            this.setDefaultState(this.getDefaultState()
                    .with(POWERED, false));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
        if (INDEX != null) {
            builder.add(INDEX);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);
        if (powered != state.get(POWERED)) {
            if (powered) {
                playSound(world, pos);
                scheduleNextTick(world, pos);
            }
            world.setBlockState(pos, state.with(POWERED, powered), Block.NOTIFY_ALL);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) return null;

        state = state.with(POWERED, false);
        if (INDEX != null) {
            state = state.with(INDEX, 1);
        }
        return state;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        if (INDEX != null) {
            // rotate through available sounds
            int index = (state.get(INDEX) + 1) % def.soundList.size();
            world.setBlockState(pos, state.with(INDEX, index), Block.NOTIFY_ALL);
        }

        playSound(world, pos);
        scheduleNextTick(world, pos);
        return ActionResult.CONSUME;
    }

    private void scheduleNextTick(World world, BlockPos pos) {
        if (playbackPeriod > 0) {
            int delay = getNextTriggerDelay(world.getRandom());
            world.scheduleBlockTick(pos, this, delay, TickPriority.NORMAL);
        }
    }

    private int getNextTriggerDelay(Random random) {
        return playbackPeriod + random.nextInt(randomPlaybackAddition + 1);
    }

    private void playSound(World world, BlockPos pos) {
        int soundIndex = (INDEX != null) ? world.getBlockState(pos).get(INDEX) : 0;
        String soundId = def.soundList.get(soundIndex);
        SoundEvent sound = ModSounds.getRegisteredSound(soundId);

        if (sound != null) {
            world.playSound(null, pos, sound, SoundCategory.RECORDS, 3.0F, 1.0F);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            scheduleNextTick(world, pos);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (playbackPeriod <= 0) return;

        long timeOfDay = world.getTimeOfDay() % 24000;
        if (timeOfDay < 0) timeOfDay += 24000;

        boolean shouldPlay = true;
        if (startTime != endTime) {
            if (startTime > endTime) {
                shouldPlay = (timeOfDay < endTime || timeOfDay >= startTime);
            } else {
                shouldPlay = (timeOfDay >= startTime && timeOfDay < endTime);
            }
        }

        if (shouldPlay) {
            playSound(world, pos);
        }

        scheduleNextTick(world, pos);
    }

    // TODO make it so the sound cancels playin when block is broken
//    @Override
//    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//        // Stop the sound when the block is broken
//        if (!world.isClient) {
//            // Play a "stop" sound or cancel the sound logic here
//            world.playSound(null, pos, ModSounds.STOP_SOUND_EVENT, SoundCategory.RECORDS, 0.0F, 1.0F);
//        }
//
//        // Call the super method to handle default block-breaking behavior
//        super.onBreak(world, pos, state, player);
//        return state;
//    }

    private static final String[] TAGS = {};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }
}
