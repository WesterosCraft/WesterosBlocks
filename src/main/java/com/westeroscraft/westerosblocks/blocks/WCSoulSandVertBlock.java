package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.state.properties.BlockStateProperties;

import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import javax.annotation.Nullable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class WCSoulSandVertBlock extends SoulSandBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSoulSandVertBlock(props, def)), true, false);
        }
    }
    
    private WesterosBlockDef def;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty UP = BlockStateProperties.UP;

    protected WCSoulSandVertBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
    	container.add(UP, DOWN);
    }
    
    private BlockState updateStateVertical(BlockState bs, IBlockReader reader, BlockPos pos) {
    	BlockState bsneighbor = reader.getBlockState(pos.above());
    	Boolean up = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.below());
    	Boolean down = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
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
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, (def.alphaRender ? TransparencyMode.TRANSPARENT : TransparencyMode.OPAQUE));
    }
    private static String[] TAGS = { "soul_speed_blocks" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
