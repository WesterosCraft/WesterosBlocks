package com.westerosblocks.block.custom;

import com.westerosblocks.util.ModBlockSetType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.List;

/**
 * Standalone door block that doesn't depend on the def system.
 * Provides the same functionality as WCDoorBlock but with direct configuration.
 */
public class StandaloneDoorBlock extends DoorBlock {

    private final boolean locked;
    private final boolean allowUnsupported;
    private final String translationKey;
    private final List<String> tooltips;

    /**
     * Creates a new standalone door block.
     *
     * @param settings Block settings
     * @param woodType The wood type for this door (affects sounds and behavior)
     * @param locked Whether this door is locked (prevents opening)
     * @param allowUnsupported Whether this door can be placed without proper support
     * @param translationKey The translation key for this block
     * @param tooltips Optional tooltips to display
     */
    public StandaloneDoorBlock(AbstractBlock.Settings settings, String woodType,
                              boolean locked, boolean allowUnsupported,
                              String translationKey, List<String> tooltips) {
        super(ModBlockSetType.getBlockSetType(woodType), settings);
        this.locked = locked;
        this.allowUnsupported = allowUnsupported;
        this.translationKey = translationKey;
        this.tooltips = tooltips;
    }

    /**
     * Creates a new standalone door block with default settings.
     *
     * @param settings Block settings
     * @param woodType The wood type for this door
     * @param translationKey The translation key for this block
     */
    public StandaloneDoorBlock(AbstractBlock.Settings settings, String woodType, String translationKey) {
        this(settings, woodType, false, false, translationKey, null);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (allowUnsupported && (state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)) {
            return true;
        }
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        if (tooltips != null && !tooltips.isEmpty()) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.literal(tooltipText));
            }
        }
        super.appendTooltip(stack, context, tooltip, options);
    }

    /**
     * Gets whether this door is locked.
     *
     * @return true if the door is locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Gets whether this door allows unsupported placement.
     *
     * @return true if the door can be placed without support
     */
    public boolean allowsUnsupported() {
        return allowUnsupported;
    }
} 