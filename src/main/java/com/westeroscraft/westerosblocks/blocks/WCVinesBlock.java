package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

import javax.annotation.Nullable;

public class WCVinesBlock extends BlockVine implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCVinesBlock(def) };
        }
    }

    private WesterosBlockDef def;
    private boolean allow_unsupported = false;
    private boolean no_climb = false;
    private boolean has_down = false;
    public static final PropertyBool DOWN = PropertyBool.create("down");

    protected WCVinesBlock(WesterosBlockDef def) {
        this.def = def;        
        def.doStandardContructorSettings(this);
        this.setTickRandomly(false);	// Our vines don't need ticking....
        String t = def.getType(0);
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                if (tok.equals("no-climb")) {
                	no_climb = true;
                }
                if (tok.equals("has-down")) {
                	has_down = true;
                }
            }
        }
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(DOWN, Boolean.valueOf(false)));        
    }
    @Override
    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }
    @Override
    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, ItemBlock.class);
        
        return true;
    }
    
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return def.getFireSpreadSpeed(world, pos, face);
    }
    
    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return def.getFlammability(world, pos, face);
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return def.getLightValue(state, world, pos);
    }
    
    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return def.getLightOpacity(state, world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:BlockRenderLayer.CUTOUT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rnd) {
        def.randomDisplayTick(stateIn, worldIn, pos, rnd);
        super.randomDisplayTick(stateIn, worldIn, pos, rnd);
    }
            
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 2);
        /* Make base model */
        // Build 16 models, for each combination
        for (int meta = 0; meta < 16; meta++) {        	
            PatchBlockModel mod = md.addPatchModel(blkname);
            if ((meta & 1) != 0) {	// South
                mod.addPatch(0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, SideVisible.BOTH);
            }
            if ((meta & 2) != 0) {	// West
                mod.addPatch(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, SideVisible.BOTH);
            }
            if ((meta & 4) != 0) {	// North
                mod.addPatch(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, SideVisible.BOTH);
            }
            if ((meta & 8) != 0) {	// East
                mod.addPatch(1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, SideVisible.BOTH);
            }
            if (meta == 0) {	// Bottom
                mod.addPatch(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, SideVisible.BOTH);            	
            }
            mod.setMetaValue(meta);
        }
    }
    
    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing facing)
    {
        IBlockState bs = worldIn.getBlockState(pos);
        if ((bs.getBlock() == this) && allow_unsupported) {
            return true;
        }
        return super.canPlaceBlockOnSide(worldIn, pos, facing);
    }
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        IBlockState bs = worldIn.getBlockState(pos);
        if ((bs.getBlock() == this) && allow_unsupported) {
            return;
        }
    	super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
    	return !no_climb;
	}

    @Override
    public SoundType getSoundType(IBlockState blockState, World world, BlockPos blockPos, @Nullable Entity entity) {
        return def.getSoundType(blockState.getBlock().getMetaFromState(blockState));
    }

    @SuppressWarnings("deprecation")
    @Override
    public Material getMaterial(IBlockState blockState) {
        return def.getMaterial(blockState.getBlock().getMetaFromState(blockState));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {UP, NORTH, EAST, SOUTH, WEST, DOWN});
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	if (has_down) {
    		// Special case if down is set
            if (((Boolean)state.getValue(DOWN)).booleanValue()) {
            	return 0;
            }
    	}
    	return super.getMetaFromState(state);
    }
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	if (has_down && (meta == 0)) {
    		return this.getDefaultState().withProperty(DOWN, Boolean.TRUE);
    	}
    	return super.getStateFromMeta(meta);
    }
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
    	if ((has_down) && state.getValue(DOWN).booleanValue()) {	// Special case
            BlockPos blockpos;
            // Check north
            blockpos = pos.north();
            state = state.withProperty(NORTH, Boolean.valueOf(worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, EnumFacing.SOUTH) == BlockFaceShape.SOLID));
            // Check south
            blockpos = pos.south();
            state = state.withProperty(SOUTH, Boolean.valueOf(worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, EnumFacing.NORTH) == BlockFaceShape.SOLID));
            // Check east
            blockpos = pos.east();
            state = state.withProperty(EAST, Boolean.valueOf(worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, EnumFacing.WEST) == BlockFaceShape.SOLID));
            // Check west
            blockpos = pos.west();
            state = state.withProperty(WEST, Boolean.valueOf(worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, EnumFacing.EAST) == BlockFaceShape.SOLID));
            // Check up
            blockpos = pos.up();
            state = state.withProperty(UP, Boolean.valueOf(worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID));
            return state;    		
    	}
    	return super.getActualState(state, worldIn, pos);
    }
}
