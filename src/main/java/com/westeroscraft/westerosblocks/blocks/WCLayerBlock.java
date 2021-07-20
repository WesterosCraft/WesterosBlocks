package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

public class WCLayerBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	
        	newLAYERS = IntegerProperty.create("layers", 1, getLayerCount(def));

        	return def.registerRenderType(def.registerBlock(new WCLayerBlock(props, def)), false, false);
        }
    }
    
    private static int getLayerCount(WesterosBlockDef def) {
        int layerCount = 8;
        int off = def.type.indexOf("cnt:");
        if (off >= 0) {
            try {
                layerCount = Integer.parseInt(def.type.substring(off+4));
            } catch (NumberFormatException nfx) {
                WesterosBlocks.log.info("Error parsing 'cnt:' in " + def.blockName);
            }
        }
        if (layerCount < 2) layerCount = 2;
        if (layerCount > 16) layerCount = 16;
        return layerCount;
    }
    
    protected VoxelShape[] SHAPE_BY_LAYER;

    private WesterosBlockDef def;
    public int layerCount;
    public IntegerProperty LAYERS;
    public static IntegerProperty newLAYERS;

    protected WCLayerBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        this.layerCount = getLayerCount(def);
        this.LAYERS = newLAYERS;
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(1)));
        SHAPE_BY_LAYER = new VoxelShape[layerCount + 1];
        SHAPE_BY_LAYER[0] = VoxelShapes.empty();
        for (int i = 1; i <= layerCount; i++) {
            float f = (float)i / (float)layerCount;
            SHAPE_BY_LAYER[i] = Block.box(0, 0, 0, 16, 16*f, 16);
        }
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE_BY_LAYER[p_220053_1_.getValue(LAYERS)];
    }
    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return SHAPE_BY_LAYER[p_220071_1_.getValue(LAYERS) - 1];
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState p_230335_1_, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
        return SHAPE_BY_LAYER[p_230335_1_.getValue(LAYERS)];
    }
    @Override
    public VoxelShape getVisualShape(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_, ISelectionContext p_230322_4_) {
    	return SHAPE_BY_LAYER[p_230322_1_.getValue(LAYERS)];
    }
    @Override
    public boolean useShapeForLightOcclusion(BlockState p_220074_1_) {
        return true;
    }
    @Override
    public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType pathType) {
         if (pathType == PathType.LAND) {
            return state.getValue(LAYERS) <= (layerCount / 2);
         }
         return false;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    public boolean canBeReplaced(BlockState state, BlockItemUseContext itemContext) {
        int i = state.getValue(LAYERS);
        if (itemContext.getItemInHand().getItem() == this.asItem() && i < layerCount) {
           if (itemContext.replacingClickedOnBlock()) {
              return itemContext.getClickedFace() == Direction.UP;
           } else {
              return true;
           }
        } else {
           return i == 1;
        }
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext itemContext) {
       BlockState blockstate = itemContext.getLevel().getBlockState(itemContext.getClickedPos());
       if (blockstate.is(this)) {
          int i = blockstate.getValue(LAYERS);
          return blockstate.setValue(LAYERS, Integer.valueOf(Math.min(layerCount, i + 1)));
       } else {
          return super.getStateForPlacement(itemContext);
       }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
       stateContainer.add(newLAYERS);
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, 0, TransparencyMode.TRANSPARENT, 0, layerCount);
        /* Make models for each layer thickness */
        for (int i = 0; i < layerCount; i++) {
            BoxBlockModel mod = md.addBoxModel(blkname);
            mod.setYRange(0.0, (double)(i+1) / (double) layerCount);
            mod.setMetaValue(i);
        }
    }
    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
