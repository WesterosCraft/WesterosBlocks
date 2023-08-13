package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

public class WCSolidBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSolidBlock(props, def)), true, def.nonOpaque);
        }
    }    
    protected WesterosBlockDef def;
    protected VoxelShape collisionbox;
    protected VoxelShape supportbox;
    
    protected WCSolidBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        collisionbox = def.makeCollisionBoxShape();
        supportbox = def.makeSupportBoxShape();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return collisionbox;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return collisionbox;
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return supportbox;
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    	if (def.nonOpaque)
    		return Shapes.empty();
    	else
    		return collisionbox;
    }
    
    public boolean skipRendering(BlockState state, BlockState other_state, Direction direction) {
    	if (def.nonOpaque)
    		return other_state.is(this) ? true : super.skipRendering(state, other_state, direction);
    	else
    		return false;
    }


     public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return (def.lightOpacity == 0) ? 1.0F : 0.2F;
     }

     public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
    	 return (def.lightOpacity == 0);
     }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
    	super.createBlockStateDefinition(StateDefinition);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	return bs;
    }

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
