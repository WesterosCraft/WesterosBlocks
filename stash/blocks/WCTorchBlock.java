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
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
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

import javax.annotation.Nullable;

public class WCTorchBlock extends BlockTorch implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCTorchBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCTorchBlock(WesterosBlockDef def) {
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
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        def.getStandardCreativeItems(this, tab, list);
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
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:super.getBlockLayer());
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
        def.registerPatchTextureBlock(mtd, 5);

        // Make vertical model
        PatchBlockModel mod0 = md.addPatchModel(blkname);
        String vside = mod0.addPatch(0.4375, 0.0, 0.0, 0.4375, 0.0, 1.0, 0.4375, 1.0, 0.0, SideVisible.TOP);
        mod0.addRotatedPatch(vside, 0, 90, 0);
        mod0.addRotatedPatch(vside, 0, 180, 0);
        mod0.addRotatedPatch(vside, 0, 270, 0);
        mod0.addPatch(0.0, 0.625, -0.0625, 1.0, 0.625, -0.0625, 0.0, 0.625, 0.9375, 0.4375, 0.5625, 0.5, 0.625, 100.0, SideVisible.BOTH);
        mod0.setMetaValue(0);
        mod0.setMetaValue(5);
        // Make side model
        PatchBlockModel mod1 = md.addPatchModel(blkname);
        mod1.addPatch(-0.5, 0.2, 0.4375, 0.5, 0.2, 0.4375, -0.1, 1.2, 0.4375, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.BOTTOM);
        mod1.addPatch(-0.5, 0.2, 0.5625, 0.5, 0.2, 0.5625, -0.1, 1.2, 0.5625, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.TOP);
        mod1.addPatch(0.0625, 0.2, 0.0, 0.0625, 0.2, 1.0, 0.4625, 1.2, 0.0, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.BOTTOM);
        mod1.addPatch(-0.0625, 0.2, 0.0, -0.0625, 0.2, 1.0, 0.3375, 1.2, 0.0, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.TOP);
        mod1.addPatch(0.0, 0.825, -0.3625, 1.0, 0.825, -0.3625, 0.0, 0.825, 0.6375, 0.4375, 0.5625, 0.5, 0.625, 100.0, SideVisible.BOTH);
        mod1.setMetaValue(1);
        // Rotate for other sides
        PatchBlockModel mod2 = md.addRotatedPatchModel(blkname, mod1, 0, 180, 0);
        mod2.setMetaValue(2);
        PatchBlockModel mod3 = md.addRotatedPatchModel(blkname, mod1, 0, 90, 0);
        mod3.setMetaValue(3);
        PatchBlockModel mod4 = md.addRotatedPatchModel(blkname, mod1, 0, 270, 0);
        mod4.setMetaValue(4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

}
