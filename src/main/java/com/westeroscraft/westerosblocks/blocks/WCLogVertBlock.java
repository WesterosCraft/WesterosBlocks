package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.BlockSide;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import javax.annotation.Nullable;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCLogVertBlock extends RotatedPillarBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCLogVertBlock(props, def)), false, false);
        }
    }
    protected WesterosBlockDef def;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    
    protected WCLogVertBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));
    }
    
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
        container.add(AXIS, UP, DOWN);
    }

    // See if connected - avoid connection if ladders aren't on same facing
    private BlockState updateStateVertical(BlockState bs, IBlockReader reader, BlockPos pos) {
    	Axis val = bs.getValue(AXIS);
    	BlockState above, below;
    	if (val == Axis.Y) {
    		above = reader.getBlockState(pos.above());
    		below = reader.getBlockState(pos.below());
    	}
    	else if (val == Axis.X) {
    		above = reader.getBlockState(pos.east());
    		below = reader.getBlockState(pos.west());    		
    	}
    	else {
    		above = reader.getBlockState(pos.north());
    		below = reader.getBlockState(pos.south());    		    		
    	}
    	// If we connect to above
    	Boolean up = def.isConnectMatch(bs, above);
    	// If we connect to below
    	Boolean down = def.isConnectMatch(bs, below);
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
    		bs = updateStateVertical(bs, ctx.getLevel(), ctx.getClickedPos());
    	}
    	return bs;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        String blkname = def.getBlockName();
        // Modifiers for each orientation
        TextureModifier tmod[][] = { 
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270 }
        };
        // Texture index for each orientation
        int txtid[][] = {
            { 0, 0, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 0, 0 },
            { 1, 1, 0, 0, 1, 1 }
        };

        for (int i = 0; i < 3; i++) {   // 3 orientations
            BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setMetaValue(i);
            for (int face = 0; face < 6; face++) {
                int fidx = txtid[i][face];
                String txt = def.getTextureByIndex(fidx);
                mtr.setSideTexture(txt.replace(':', '_'), tmod[i][face], BlockSide.valueOf("FACE_" + face));
            }
            def.setBlockColorMap(mtr);
        }
    }
    private static String[] TAGS = { "logs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
