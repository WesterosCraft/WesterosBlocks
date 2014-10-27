package com.westeroscraft.westerosblocks.blocks;

import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
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
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCRailBlock extends BlockRail implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCRailBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCRailBlock(WesterosBlockDef def) {
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
        if (meta >= 6)
            return def.doStandardIconGet(1, 0);
        else
            return def.doStandardIconGet(0, 0);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itm, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, Item.getIdFromItem(itm), tab, list);
    }
    @Override
    public int damageDropped(int meta) {
        return 0;
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
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     * Modified from BlockRailBase: don't break when support blocks break
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        if (!par1World.isRemote)
        {
            int meta = par1World.getBlockMetadata(par2, par3, par4);

            this.func_150048_a(par1World, par2, par3, par4, meta, meta, par5);
        }
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
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        WesterosBlockDef def = this.getWBDefinition();
        Subblock sb = def.getByMeta(0);
        if ((sb != null) && (sb.textures != null)) {
            // Register textures 
            TextureModifier tmod = TextureModifier.NONE;
            if (def.nonOpaque) {
                tmod = TextureModifier.CLEARINSIDE;
            }
            // Make record for straight tracks
            BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
            // Set for all meta values for straight tracks
            for (int meta = 0; meta < 6; meta++) {
                mtr.setMetaValue(meta);
            }
            for (int meta = 10; meta < 14; meta++) {
                mtr.setMetaValue(meta);
            }
            int fidx = 0;
            if (fidx >= sb.textures.size()) {
                fidx = sb.textures.size() - 1;
            }
            String txtid = sb.textures.get(fidx);
            mtr.setPatchTexture(txtid.replace(':', '_'), tmod, 0);
            // Make record for curved tracks
            mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
            // Set for all meta values for curved tracks
            for (int meta = 6; meta < 10; meta++) {
                mtr.setMetaValue(meta);
            }
            fidx = 1;
            if (fidx >= sb.textures.size()) {
                fidx = sb.textures.size() - 1;
            }
            txtid = sb.textures.get(fidx);
            mtr.setPatchTexture(txtid.replace(':', '_'), tmod, 0);
            def.setBlockColorMap(mtr, sb);
        }
        // Make models for flat tracks
        PatchBlockModel mod = md.addPatchModel(blkname);
        String patchFlat = mod.addPatch(0.0, 0.01, 0.0, 1.0, 0.01, 0.0, 0.0, 0.01, 1.0, SideVisible.BOTH);
        mod.setMetaValue(0);
        mod.setMetaValue(9);
        PatchBlockModel mod90 = md.addPatchModel(blkname);
        mod90.addRotatedPatch(patchFlat, 0, 90, 0);
        mod90.setMetaValue(1);
        mod90.setMetaValue(6);
        PatchBlockModel mod180 = md.addPatchModel(blkname);
        mod180.addRotatedPatch(patchFlat, 0, 180, 0);
        mod180.setMetaValue(7);
        PatchBlockModel mod270 = md.addPatchModel(blkname);
        mod270.addRotatedPatch(patchFlat, 0, 270, 0);
        mod270.setMetaValue(8);
        // Make models for sloped tracks
        PatchBlockModel modS0 = md.addPatchModel(blkname);
        String patchSlope = mod.addPatch(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, SideVisible.BOTH);
        modS0.setMetaValue(2);
        modS0.setMetaValue(10);
        PatchBlockModel modS90 = md.addPatchModel(blkname);
        modS90.addRotatedPatch(patchSlope, 0, 90, 0);
        modS90.setMetaValue(5);
        modS90.setMetaValue(13);
        PatchBlockModel modS180 = md.addPatchModel(blkname);
        modS180.addRotatedPatch(patchSlope, 0, 180, 0);
        modS180.setMetaValue(3);
        modS180.setMetaValue(11);
        PatchBlockModel modS270 = md.addPatchModel(blkname);
        modS270.addRotatedPatch(patchSlope, 0, 270, 0);
        modS270.setMetaValue(4);
        modS270.setMetaValue(12);
    }
}
