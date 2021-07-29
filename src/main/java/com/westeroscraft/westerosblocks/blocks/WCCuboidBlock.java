package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import org.dynmap.modsupport.CuboidBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.state.StateContainer;

import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;

import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboidBlock(props, def)), false, false);
        }
    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    protected WesterosBlockDef def;
    
    protected VoxelShape[] SHAPE_BY_INDEX;

    protected WCCuboidBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        SHAPE_BY_INDEX = new VoxelShape[1];
        SHAPE_BY_INDEX[0] = getBoundingBoxFromCuboidList(def.getCuboidList());
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)));

    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
       stateContainer.add(WATERLOGGED);
    }

    protected int getIndexFromState(BlockState state) {
    	return 0;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public BlockState updateShape(BlockState state, Direction face, BlockState state2, IWorld world, BlockPos pos, BlockPos pos2) {
       if (state.getValue(WATERLOGGED)) {
          world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
       }
       return super.updateShape(state, face, state2, world, pos, pos2);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    @Override
    public boolean isPathfindable(BlockState state, IBlockReader reader, BlockPos pos, PathType pathtype) {
        switch(pathtype) {
        case LAND:
           return false;
        case WATER:
           return reader.getFluidState(pos).is(FluidTags.WATER);
        case AIR:
           return false;
        default:
           return false;
        }
    }


    protected VoxelShape getBoundingBoxFromCuboidList(List<WesterosBlockDef.Cuboid> cl) {
        if (cl.size() == 0) return VoxelShapes.empty();
        float xmin = 100.0F, ymin = 100.0F, zmin = 100.0F;
        float xmax = -100.0F, ymax = -100.0F, zmax = -100.0F;
        for(WesterosBlockDef.Cuboid c : cl) {
            if (c.xMin < xmin) xmin = c.xMin;
            if (c.yMin < ymin) ymin = c.yMin;
            if (c.zMin < zmin) zmin = c.zMin;
            if (c.xMax > xmax) xmax = c.xMax;
            if (c.yMax > ymax) ymax = c.yMax;
            if (c.zMax > zmax) zmax = c.zMax;
        }
        return Block.box(16*xmin, 16*ymin, 16*zmin, 16*xmax, 16*ymax, 16*zmax);
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        WesterosBlockDef def = this.getWBDefinition();
        String blkname = def.getBlockName();
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 6, TransparencyMode.TRANSPARENT, 1);
        List<Cuboid> cl = def.getCuboidList();   
        if (cl == null) return;
        CuboidBlockModel mod = md.addCuboidModel(blkname);
        for (Cuboid c : cl) {
            if (WesterosBlockDef.SHAPE_CROSSED.equals(c.shape)) {   // Crosed
                mod.addCrossedPatches(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax, c.sideTextures[0]);
            }
            else {
                mod.addCuboid(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax, c.sideTextures);
            }
        }
    }
    
    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
