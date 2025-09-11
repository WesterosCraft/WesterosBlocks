package com.westerosblocks.block.custom;

import com.westerosblocks.utils.ModProperties;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.Map;

public class WCWebBlock extends CobwebBlock {
    // Waterlogging support
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    
    // Layer support for stacking behavior
    protected static IntProperty tempLAYERS;
    public IntProperty LAYERS;
    
    // State management for toggle functionality
    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;
    
    // Behavior flags
    protected boolean toggleOnUse = false;
    protected boolean noInWeb = false;
    protected boolean layerSensitive = false;
    
    // Layer shapes for different layer counts
    private static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{
        VoxelShapes.empty(),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                
                boolean doToggleOnUse = (Boolean) paramMap.getOrDefault("toggleOnUse", false);
                boolean doNoInWeb = (Boolean) paramMap.getOrDefault("noInWeb", false);
                boolean doLayerSensitive = (Boolean) paramMap.getOrDefault("layerSensitive", false);
                Integer numStates = (Integer) paramMap.get("states");
                boolean doAddStates = numStates != null && numStates > 0;
                
                if (doLayerSensitive) {
                    tempLAYERS = Properties.LAYERS;
                }
                
                if (doAddStates) {
                    ArrayList<String> stateIds = new ArrayList<>();
                    for (int i = 0; i < numStates; i++) {
                        stateIds.add("state" + i);
                    }
                    ModProperties.StateProperty STATE = new ModProperties.StateProperty(stateIds);
                    tempSTATE = STATE;
                }
                
                // Apply noCollision for web blocks
                settings = settings.noCollision();
                
                return new WCWebBlock(settings, doToggleOnUse, doNoInWeb, doLayerSensitive, doAddStates);
            }
            
            // Fallback for legacy parameter style
            boolean doToggleOnUse = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            boolean doNoInWeb = params.length > 1 && params[1] instanceof Boolean ? (Boolean) params[1] : false;
            boolean doLayerSensitive = params.length > 2 && params[2] instanceof Boolean ? (Boolean) params[2] : false;
            boolean doAddStates = params.length > 3 && params[3] instanceof Integer && (Integer) params[3] > 0;
            
            if (doLayerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }
            
            if (doAddStates) {
                int numStates = (Integer) params[3];
                ArrayList<String> stateIds = new ArrayList<>();
                for (int i = 0; i < numStates; i++) {
                    stateIds.add("state" + i);
                }
                ModProperties.StateProperty STATE = new ModProperties.StateProperty(stateIds);
                tempSTATE = STATE;
            }
            
            // Apply noCollision for web blocks
            settings = settings.noCollision();
            
            return new WCWebBlock(settings, doToggleOnUse, doNoInWeb, doLayerSensitive, doAddStates);
        }
    }
    
    protected WCWebBlock(AbstractBlock.Settings settings, boolean doToggleOnUse, boolean doNoInWeb, 
                        boolean doLayerSensitive, boolean doAddStates) {
        super(settings);
        
        this.toggleOnUse = doToggleOnUse;
        this.noInWeb = doNoInWeb;
        this.layerSensitive = doLayerSensitive;
        
        // Set default state
        BlockState defaultState = this.getDefaultState()
                .with(WATERLOGGED, Boolean.FALSE);
        
        if (layerSensitive && LAYERS != null) {
            defaultState = defaultState.with(LAYERS, 8);
        }
        
        if (doAddStates && STATE != null) {
            defaultState = defaultState.with(STATE, STATE.defValue);
        }
        
        this.setDefaultState(defaultState);
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        
        if (tempLAYERS != null) {
            LAYERS = tempLAYERS;
            tempLAYERS = null;
            builder.add(LAYERS);
        }
        
        if (tempSTATE != null) {
            STATE = tempSTATE;
            builder.add(tempSTATE);
            tempSTATE = null;
        }
        
        super.appendProperties(builder);
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) return null;
        
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        state = state.with(WATERLOGGED, fluidState.isIn(FluidTags.WATER));
        
        if (STATE != null) {
            state = state.with(STATE, STATE.defValue);
        }
        
        // Layer sensitivity for stacking behavior
        if (layerSensitive && LAYERS != null) {
            BlockState belowState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.DOWN));
            
            if (belowState != null && belowState.contains(Properties.LAYERS)) {
                Block belowBlock = belowState.getBlock();
                Integer layer = belowState.get(Properties.LAYERS);
                
                // Adjust layer based on block type below
                if (belowBlock instanceof SnowBlock || 
                    (belowBlock instanceof WCLayerBlock wcLayer && wcLayer.softLayer)) {
                    layer = (layer > 2) ? layer - 2 : 1;
                }
                state = state.with(LAYERS, layer);
            } else if (belowState != null && belowState.getBlock() instanceof SlabBlock) {
                SlabType slabType = belowState.get(Properties.SLAB_TYPE);
                if (slabType == SlabType.BOTTOM) {
                    state = state.with(LAYERS, 4);
                }
            }
        }
        
        return state;
    }
    
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, 
                                              WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        switch (type) {
            case LAND:
                return false;
            case WATER:
                return state.getFluidState().isIn(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }
    
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!noInWeb) {
            super.onEntityCollision(state, world, pos, entity);
        }
    }
    
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            if (state.contains(STATE)) {
                state = state.cycle(STATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (layerSensitive && LAYERS != null && state.contains(LAYERS)) {
            return SHAPE_BY_LAYER[state.get(LAYERS)];
        }
        return VoxelShapes.fullCube();
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // Web blocks typically have no collision
        return VoxelShapes.empty();
    }
}
