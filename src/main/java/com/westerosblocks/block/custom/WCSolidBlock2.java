package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Solid block implementation for the builder system.
 * Provides basic solid block functionality with optional state properties.
 */
public class WCSolidBlock2 extends Block {
    protected final String name;
    protected final String creativeTab;
    protected final boolean toggleOnUse;
    protected final double[] boundingBox;
    protected final List<String> tooltips;
    
    // State properties
    public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
    public static final BooleanProperty SYMMETRICAL = BooleanProperty.of("symmetrical");
    
    // Instance-specific properties
    protected final boolean hasConnectState;
    protected final boolean hasSymmetrical;
    protected final Boolean symmetricalDefault;
    public final Property<String> stateProperty;
    protected final String stateDefault;
    protected final Map<String, String> stateTextures;
    
    // Voxel shapes
    protected final VoxelShape collisionBox;
    protected final VoxelShape supportBox;

    public WCSolidBlock2(AbstractBlock.Settings settings, String name, String creativeTab, 
                        boolean toggleOnUse, double[] boundingBox, List<String> tooltips,
                        List<String> stateValues, String stateDefaultValue, Map<String, String> stateTextures) {
        super(settings);
        this.name = name;
        this.creativeTab = creativeTab;
        this.toggleOnUse = toggleOnUse;
        this.boundingBox = boundingBox;
        this.tooltips = tooltips;
        this.stateTextures = stateTextures != null ? stateTextures : new HashMap<>();
        
        // Initialize state properties based on builder configuration
        this.hasConnectState = false; // Can be configured via builder
        this.hasSymmetrical = false; // Can be configured via builder
        this.symmetricalDefault = false;
        
        // Initialize state property if provided
        if (stateValues != null && !stateValues.isEmpty()) {
            this.stateProperty = createStateProperty(stateValues);
            this.stateDefault = stateDefaultValue != null ? stateDefaultValue : stateValues.get(0);
        } else {
            this.stateProperty = null;
            this.stateDefault = null;
        }
        
        // Create voxel shapes
        if (boundingBox != null) {
            this.collisionBox = VoxelShapes.cuboid(
                boundingBox[0], boundingBox[2], boundingBox[4],
                boundingBox[1], boundingBox[3], boundingBox[5]
            );
        } else {
            this.collisionBox = VoxelShapes.fullCube();
        }
        this.supportBox = this.collisionBox;
        
        // Set default state
        BlockState defaultState = this.getDefaultState();
        if (hasConnectState) {
            defaultState = defaultState.with(CONNECTSTATE, 0);
        }
        if (hasSymmetrical) {
            defaultState = defaultState.with(SYMMETRICAL, symmetricalDefault);
        }
        if (stateProperty != null) {
            defaultState = defaultState.with(stateProperty, stateDefault);
        }
        this.setDefaultState(defaultState);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (hasConnectState) {
            builder.add(CONNECTSTATE);
        }
        if (hasSymmetrical) {
            builder.add(SYMMETRICAL);
        }
        if (stateProperty != null) {
            builder.add(stateProperty);
        }
        super.appendProperties(builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return collisionBox;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return collisionBox;
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return supportBox;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // For now, use collision box for camera collision
        return collisionBox;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        // For now, use default behavior
        return super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        // Default ambient occlusion
        return 0.2F;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState bs = super.getPlacementState(ctx);
        if (stateProperty != null && bs != null && bs.contains(stateProperty)) {
            bs = bs.with(stateProperty, stateDefault);
        }
        return bs;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (stateProperty != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            if (state.contains(stateProperty)) {
                state = state.cycle(stateProperty);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        if (tooltips != null) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.translatable(tooltipText));
            }
        }
        super.appendTooltip(stack, context, tooltip, options);
    }

    // Builder configuration methods
    public WCSolidBlock2 withConnectState() {
        // This would require a more complex builder pattern to modify existing instance
        // For now, return this instance
        return this;
    }

    public WCSolidBlock2 withSymmetrical(boolean defaultValue) {
        // This would require a more complex builder pattern to modify existing instance
        // For now, return this instance
        return this;
    }

    public WCSolidBlock2 withStateProperty(IntProperty property, int defaultValue) {
        // This would require a more complex builder pattern to modify existing instance
        // For now, return this instance
        return this;
    }

    /**
     * Gets the texture for a specific state value.
     * 
     * @param stateValue The state value
     * @return The texture path for the state, or null if not found
     */
    public String getTextureForState(String stateValue) {
        return stateTextures.get(stateValue);
    }

    /**
     * Gets all available state textures.
     * 
     * @return Map of state values to texture paths
     */
    public Map<String, String> getStateTextures() {
        return new HashMap<>(stateTextures);
    }

    /**
     * Creates a state property from a list of string values.
     * 
     * @param values List of possible state values
     * @return The created state property
     */
    private Property<String> createStateProperty(List<String> values) {
        return new Property<String>("state", String.class) {
            @Override
            public Collection<String> getValues() {
                return values;
            }

            @Override
            public String name(String value) {
                return value;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                } else if (obj instanceof Property<?> property && super.equals(obj)) {
                    return this.getValues().equals(property.getValues());
                } else {
                    return false;
                }
            }

            @Override
            public Optional<String> parse(String value) {
                return values.contains(value) ? Optional.of(value) : Optional.empty();
            }
        };
    }
} 