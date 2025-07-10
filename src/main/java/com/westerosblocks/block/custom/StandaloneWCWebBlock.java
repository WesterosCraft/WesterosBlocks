package com.westerosblocks.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Standalone web block that recreates WCWebBlock functionality without definition system dependency.
 * 
 * <p>This class provides the same features as WCWebBlock:
 * <ul>
 *   <li>Waterlogging support</li>
 *   <li>Layer-sensitive placement (for snow layers)</li>
 *   <li>Toggle functionality for creative mode</li>
 *   <li>Custom web behavior (slow movement or no web effect)</li>
 *   <li>Tooltip support</li>
 * </ul>
 * 
 * <p>Usage example:
 * <pre>{@code
 * new StandaloneWCWebBlock(settings, "spider_web", "westeros_decor_tab", true, false, false);
 * }</pre>
 */
public class StandaloneWCWebBlock extends Block {

    /**
     * Simple state property for toggle functionality.
     */
    private static class SimpleStateProperty extends Property<String> {
        private final List<String> values;
        private final String defValue;

        public SimpleStateProperty(List<String> values) {
            super("state", String.class);
            this.values = values;
            this.defValue = values.get(0);
        }

        @Override
        public Collection<String> getValues() {
            return values;
        }

        @Override
        public String name(String value) {
            return value;
        }

        @Override
        public Optional<String> parse(String value) {
            return values.contains(value) ? Optional.of(value) : Optional.empty();
        }
    }

    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BooleanProperty.of("waterlogged");

    private final String blockName;
    private final String creativeTab;
    private final boolean layerSensitive;
    private final boolean toggleOnUse;
    private final boolean noInWeb;
    private final List<String> tooltips;
    
    // State properties (set during initialization)
    private IntProperty LAYERS;
    private SimpleStateProperty STATE;

    /**
     * Creates a new StandaloneWCWebBlock with default settings.
     * 
     * @param settings The block settings
     */
    public StandaloneWCWebBlock(AbstractBlock.Settings settings) {
        this(settings, "web", "building_blocks", false, false, false, null);
    }

    /**
     * Creates a new StandaloneWCWebBlock with basic configuration.
     * 
     * @param settings The block settings
     * @param blockName The block name
     * @param creativeTab The creative tab
     */
    public StandaloneWCWebBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, false, false, false, null);
    }

    /**
     * Creates a new StandaloneWCWebBlock with full configuration.
     * 
     * @param settings The block settings
     * @param blockName The block name
     * @param creativeTab The creative tab
     * @param layerSensitive Whether this block is sensitive to layer placement
     * @param toggleOnUse Whether this block can be toggled in creative mode
     * @param noInWeb Whether this block should not apply web movement effects
     * @param tooltips Optional tooltips to display
     */
    public StandaloneWCWebBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, 
                               boolean layerSensitive, boolean toggleOnUse, boolean noInWeb, List<String> tooltips) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.layerSensitive = layerSensitive;
        this.toggleOnUse = toggleOnUse;
        this.noInWeb = noInWeb;
        this.tooltips = tooltips;
        
        // Set default state - only set WATERLOGGED here, layers will be handled in getPlacementState
        BlockState bsdef = this.getDefaultState().with(WATERLOGGED, false);
        setDefaultState(bsdef);
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public boolean isLayerSensitive() {
        return layerSensitive;
    }

    public boolean isToggleOnUse() {
        return toggleOnUse;
    }

    public boolean isNoInWeb() {
        return noInWeb;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!noInWeb) {
            super.onEntityCollision(state, world, pos, entity);
        } else {
            // Custom web-like behavior without the full web effect
            entity.slowMovement(state, new Vec3d(0.25D, 0.05F, 0.25D));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (layerSensitive) {
            LAYERS = Properties.LAYERS;
            builder.add(LAYERS);
        }
        if (toggleOnUse) {
            // Create a simple state property for toggle functionality
            STATE = new SimpleStateProperty(List.of("off", "on"));
            builder.add(STATE);
        }
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState bs = super.getPlacementState(ctx);
        if (bs == null) return null;
        
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        bs = bs.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
        
        if (toggleOnUse && STATE != null) {
            bs = bs.with(STATE, STATE.defValue);
        }
        
        if (layerSensitive && LAYERS != null) {
            BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
            if (below != null && below.contains(Properties.LAYERS)) {
                Block blk = below.getBlock();
                Integer layer = below.get(Properties.LAYERS);
                // See if soft layer
                if (blk == Blocks.SNOW || (blk instanceof WCLayerBlock && ((WCLayerBlock) blk).softLayer)) {
                    layer = (layer > 2) ? layer - 2 : 1;
                }
                bs = bs.with(LAYERS, layer);
            } else if (below != null && below.getBlock() instanceof SlabBlock) {
                SlabType slabtype = below.get(Properties.SLAB_TYPE);
                if (slabtype == SlabType.BOTTOM) {
                    bs = bs.with(LAYERS, 4);
                }
            } else {
                // Default layer value for layer-sensitive blocks
                bs = bs.with(LAYERS, 8);
            }
        }
        
        return bs;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(blockState);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return switch (type) {
            case LAND -> false;
            case WATER -> state.getFluidState().isIn(FluidTags.WATER);
            case AIR -> false;
        };
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (toggleOnUse && STATE != null && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            state = state.cycle(STATE);
            world.setBlockState(pos, state, 10);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
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
} 