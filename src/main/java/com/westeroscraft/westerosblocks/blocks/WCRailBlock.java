package com.westeroscraft.westerosblocks.blocks;

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
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

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
        def.doStandardRegisterActions(this, ItemBlock.class);
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        def.getStandardCreativeItems(this, itemIn, tab, list);
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
    public BlockRenderLayer getBlockLayer() {
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:BlockRenderLayer.CUTOUT);
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

    // Update from BlockRailBase to prevent break when support lost
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
             this.updateState(state, worldIn, pos, blockIn);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, 0);
    }
    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

}
