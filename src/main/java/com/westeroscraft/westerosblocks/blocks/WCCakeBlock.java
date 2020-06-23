package com.westeroscraft.westerosblocks.blocks;


import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import javax.annotation.Nullable;

public class WCCakeBlock extends BlockCake implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            // Validate meta : we require meta 0, and only allow it
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }

            return new Block[] { new WCCakeBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCCakeBlock(WesterosBlockDef def) {
        super();
        this.def = def;
        def.doStandardContructorSettings(this);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

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
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:BlockRenderLayer.SOLID);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rnd) {
        def.randomDisplayTick(stateIn, worldIn, pos, rnd);
        super.randomDisplayTick(stateIn, worldIn, pos, rnd);
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, 0, TransparencyMode.TRANSPARENT);
        // Get stair model
        BoxBlockModel mdl = md.addBoxModel(blkname);
        mdl.setYRange(0.0, 0.5);
        for (int i = 0; i < 16; i++) {
            mdl.setMetaValue(i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

    @Override
    public SoundType getSoundType(IBlockState blockState, World world, BlockPos blockPos, @Nullable Entity entity) {
        return def.getSoundType(blockState.getBlock().getMetaFromState(blockState));
    }

}
