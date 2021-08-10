package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.util.math.BlockPos;

import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import javax.annotation.Nullable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

// Solid block, with horizontal CTM - adds four boolean states for north, south, east, west (boolean)
public class WCSolidHorizBlock extends WCSolidBlock implements WesterosBlockDynmapSupport, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSolidHorizBlock(props, def)), true, def.nonOpaque);
        }
    }    
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    
    protected WCSolidHorizBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props, def);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)));
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
    	container.add(NORTH, SOUTH, EAST, WEST);
    }
    
    private BlockState updateStateHorizontal(BlockState bs, IBlockReader reader, BlockPos pos) {
    	BlockState bsneighbor = reader.getBlockState(pos.north());
    	Boolean north = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.south());
    	Boolean south = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.east());
    	Boolean east = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.west());
    	Boolean west = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	return bs.setValue(NORTH, north).setValue(SOUTH, south).setValue(EAST, east).setValue(WEST, west);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, IWorld world, BlockPos pos, BlockPos pos2) {
    	state = super.updateShape(state, dir, state2, world, pos, pos2);
    	if (state != null) {
    		state = updateStateHorizontal(state, world, pos);
    	}
    	return state;
    }

    @Nullable 
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs != null) {
    		bs = updateStateHorizontal(this.defaultBlockState(), ctx.getLevel(), ctx.getClickedPos());
    	}
    	return bs;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, (def.alphaRender ? TransparencyMode.TRANSPARENT : TransparencyMode.OPAQUE));
    }
}
