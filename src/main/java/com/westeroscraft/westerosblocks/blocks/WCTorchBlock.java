package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return def.doStandardIconGet(side, 0);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itm, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, Item.getIdFromItem(itm), tab, list);
    }
    @Override
    public int damageDropped(int meta) {
        return meta;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return def.getFireSpreadSpeed(world, x, y, z, face);
    }
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return def.getFlammability(world, x, y, z, face);
    }
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return def.getLightValue(world, x, y, z);
    }
    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return def.getLightOpacity(this, world, x, y, z);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor() {
        return def.getBlockColor();
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int meta)
    {
        return def.getRenderColor(meta);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        return def.colorMultiplier(access, x, y, z);
    }
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {
        def.doRandomDisplayTick(world, x, y, z, rnd);
        super.randomDisplayTick(world, x, y, z, rnd);
    }
    
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        int blkid = Block.getIdFromBlock(this);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 5);

        // Make vertical model
        PatchBlockModel mod0 = md.addPatchModel(blkid);
        String vside = mod0.addPatch(0.4375, 0.0, 0.0, 0.4375, 0.0, 1.0, 0.4375, 1.0, 0.0, SideVisible.TOP);
        mod0.addRotatedPatch(vside, 0, 90, 0);
        mod0.addRotatedPatch(vside, 0, 180, 0);
        mod0.addRotatedPatch(vside, 0, 270, 0);
        mod0.addPatch(0.0, 0.625, -0.0625, 1.0, 0.625, -0.0625, 0.0, 0.625, 0.9375, 0.4375, 0.5625, 0.5, 0.625, 100.0, SideVisible.BOTH);
        mod0.setMetaValue(0);
        mod0.setMetaValue(5);
        // Make side model
        PatchBlockModel mod1 = md.addPatchModel(blkid);
        mod1.addPatch(-0.5, 0.2, 0.4375, 0.5, 0.2, 0.4375, -0.1, 1.2, 0.4375, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.BOTTOM);
        mod1.addPatch(-0.5, 0.2, 0.5625, 0.5, 0.2, 0.5625, -0.1, 1.2, 0.5625, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.TOP);
        mod1.addPatch(0.0625, 0.2, 0.0, 0.0625, 0.2, 1.0, 0.4625, 1.2, 0.0, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.BOTTOM);
        mod1.addPatch(-0.0625, 0.2, 0.0, -0.0625, 0.2, 1.0, 0.3375, 1.2, 0.0, 0.0, 1.0, 0.0, 0.8, 100.0, SideVisible.TOP);
        mod1.addPatch(0.0, 0.825, -0.3625, 1.0, 0.825, -0.3625, 0.0, 0.825, 0.6375, 0.4375, 0.5625, 0.5, 0.625, 100.0, SideVisible.BOTH);
        mod1.setMetaValue(1);
        // Rotate for other sides
        PatchBlockModel mod2 = md.addRotatedPatchModel(blkid, mod1, 0, 180, 0);
        mod2.setMetaValue(2);
        PatchBlockModel mod3 = md.addRotatedPatchModel(blkid, mod1, 0, 90, 0);
        mod3.setMetaValue(3);
        PatchBlockModel mod4 = md.addRotatedPatchModel(blkid, mod1, 0, 270, 0);
        mod4.setMetaValue(4);
    }

}
