package com.westerosblocks.block.blockentity.custom;

import com.westerosblocks.block.blockentity.ModBlockEntities;
import com.westerosblocks.block.custom.WCBigDoorBlock;
import mod.azure.azurelib.common.animation.easing.AzEasingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks only the transient swing animation for a big door.
 * Authoritative open/closed state lives on the blockstate property
 * ({@link WCBigDoorBlock#OPEN}), synced to clients by vanilla block-update machinery.
 */
public class WCBigDoorBlockEntity extends BlockEntity {

    public static final int SWING_DURATION_TICKS = 40;
    public static final float MAX_DOOR_ANGLE_DEGREES = 90.0f;

    public enum SwingDirection {
        NONE, OPENING, CLOSING
    }

    private SwingDirection swingDir = SwingDirection.NONE;
    private long swingStartTick = 0L;

    public WCBigDoorBlockEntity(BlockPos pos, BlockState state, String blockName) {
        super(ModBlockEntities.getBlockEntityType(blockName), pos, state);
    }

    public boolean isSwinging() {
        return swingDir != SwingDirection.NONE;
    }

    /**
     * Called server-side after a toggle flips the OPEN blockstate on all 9 parts.
     * Starts a cosmetic swing animation. If the door was already mid-swing,
     * back-dates swingStartTick so the new animation resumes from the current
     * angle instead of snapping to the far end and re-animating.
     */
    public void startSwingAnimation(@Nullable PlayerEntity player, boolean targetOpen) {
        if (world == null || world.isClient()) {
            return;
        }
        // Angle currently being displayed, BEFORE we change any state. Derived
        // from targetOpen, not the OPEN blockstate: onUse calls this BEFORE
        // flipping OPEN (so the BE sync packet precedes the blockstate updates),
        // and at rest the displayed pose is the opposite of targetOpen.
        float currentDegrees;
        switch (swingDir) {
            case OPENING -> currentDegrees = getSwingProgress(0f) * MAX_DOOR_ANGLE_DEGREES;
            case CLOSING -> currentDegrees = (1f - getSwingProgress(0f)) * MAX_DOOR_ANGLE_DEGREES;
            default -> currentDegrees = targetOpen ? 0f : MAX_DOOR_ANGLE_DEGREES;
        }

        SwingDirection newDir = targetOpen ? SwingDirection.OPENING : SwingDirection.CLOSING;
        // Progress (post-easing) needed so the new direction's angle equals currentDegrees.
        float easedProgress = (newDir == SwingDirection.OPENING)
                ? currentDegrees / MAX_DOOR_ANGLE_DEGREES
                : 1f - currentDegrees / MAX_DOOR_ANGLE_DEGREES;
        easedProgress = Math.max(0f, Math.min(1f, easedProgress));
        // Invert quadratic easing (p = t^2 -> t = sqrt(p)) to recover the linear t.
        float linearT = (float) Math.sqrt(easedProgress);

        swingDir = newDir;
        swingStartTick = world.getTime() - (long) (linearT * SWING_DURATION_TICKS);
        playSound(targetOpen);
        world.emitGameEvent(player, targetOpen ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        markDirty();
        sync();
    }

    /** Server-side: once the animation duration elapses, clear the swing state. */
    public static void serverTick(World world, BlockPos pos, BlockState state, WCBigDoorBlockEntity be) {
        if (!be.isSwinging()) {
            return;
        }
        if (world.getTime() - be.swingStartTick >= SWING_DURATION_TICKS) {
            be.swingDir = SwingDirection.NONE;
            be.markDirty();
            be.sync();
        }
    }

    /**
     * Leaf rotation in radians for animation rendering.
     * Drives both doors; apply -value to left_door, +value to right_door.
     */
    public float getDoorAngleRadians(float partialTicks) {
        boolean open = getCachedState().get(WCBigDoorBlock.OPEN);
        float degrees;
        switch (swingDir) {
            case OPENING -> degrees = getSwingProgress(partialTicks) * MAX_DOOR_ANGLE_DEGREES;
            case CLOSING -> degrees = (1.0f - getSwingProgress(partialTicks)) * MAX_DOOR_ANGLE_DEGREES;
            default -> degrees = open ? MAX_DOOR_ANGLE_DEGREES : 0.0f;
        }
        return (float) Math.toRadians(degrees);
    }

    private float getSwingProgress(float partialTicks) {
        if (world == null) {
            return 0.0f;
        }
        float elapsed = (world.getTime() - swingStartTick) + partialTicks;
        float t = Math.max(0.0f, Math.min(1.0f, elapsed / SWING_DURATION_TICKS));
        return (float) AzEasingUtil.quadratic(t);
    }

    private void playSound(boolean targetOpen) {
        if (world == null) return;
        world.playSound(
                null,
                pos,
                targetOpen ? SoundEvents.BLOCK_WOODEN_DOOR_OPEN : SoundEvents.BLOCK_WOODEN_DOOR_CLOSE,
                SoundCategory.BLOCKS,
                1.0F,
                world.getRandom().nextFloat() * 0.1F + 0.9F
        );
    }

    private void sync() {
        if (world != null && !world.isClient()) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putString("SwingDir", swingDir.name());
        nbt.putLong("SwingStartTick", swingStartTick);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        try {
            swingDir = SwingDirection.valueOf(nbt.getString("SwingDir"));
        } catch (IllegalArgumentException e) {
            swingDir = SwingDirection.NONE;
        }
        swingStartTick = nbt.getLong("SwingStartTick");
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registries);
        nbt.putString("SwingDir", swingDir.name());
        nbt.putLong("SwingStartTick", swingStartTick);
        return nbt;
    }
}
