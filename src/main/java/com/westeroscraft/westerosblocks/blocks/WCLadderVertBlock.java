package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.block.LadderBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.world.IWorldReader;

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

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCLadderVertBlock extends LadderBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties().noOcclusion();
        	return def.registerRenderType(def.registerBlock(new WCLadderVertBlock(props, def)), false, false);
        }
    }

    private WesterosBlockDef def;
    private boolean allow_unsupported;
    private boolean no_climb;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty UP = BlockStateProperties.UP;

    protected WCLadderVertBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                if (tok.equals("no-climb")) {
                	no_climb = true;
                }
            }
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    private BlockState updateStateVertical(BlockState bs, IBlockReader reader, BlockPos pos) {
    	BlockState bsneighbor = reader.getBlockState(pos.above());
    	boolean up = def.isConnectMatch(bs, bsneighbor);
    	bsneighbor = reader.getBlockState(pos.below());
    	boolean down = def.isConnectMatch(bs, bsneighbor);
    	return bs.setValue(UP, up).setValue(DOWN, down);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, IWorld world, BlockPos pos, BlockPos pos2) {
    	state = super.updateShape(state, dir, state2, world, pos, pos2);
    	if (state != null) {
    		state = updateStateVertical(state, world, pos);
    	}
    	return state;
    }

    @Nullable 
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs != null) {
    		bs = updateStateVertical(this.defaultBlockState(), ctx.getLevel(), ctx.getClickedPos());
    	}
    	return bs;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName();
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 1);
        /* Make base model */
        PatchBlockModel mod = md.addPatchModel(blkname);
        String patch0 = mod.addPatch(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, SideVisible.BOTH);
        /* Make rotated models */
        PatchBlockModel mod90 = md.addPatchModel(blkname);
        mod90.addRotatedPatch(patch0, 0, 90, 0);
        PatchBlockModel mod180 = md.addPatchModel(blkname);
        mod180.addRotatedPatch(patch0, 0, 180, 0);
        PatchBlockModel mod270 = md.addPatchModel(blkname);
        mod270.addRotatedPatch(patch0, 0, 270, 0);
        
        mod.setMetaValue(0);
        mod90.setMetaValue(1);
        mod180.setMetaValue(2);
        mod270.setMetaValue(3);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
        container.add(FACING, WATERLOGGED, UP, DOWN);
     }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return allow_unsupported || super.canSurvive(state, world, pos);
     }
    private static String[] TAGS = { "climbable" };
    private static String[] TAGS_NOCLIMB = {  };
    @Override
    public String[] getBlockTags() {
    	if (no_climb) return TAGS_NOCLIMB;
    	return TAGS;
    }    

}
