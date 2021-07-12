package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
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
import com.westeroscraft.westerosblocks.items.WCBedItem;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import javax.annotation.Nullable;

public class WCBedBlock extends BlockBed implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCBedBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCBedBlock(WesterosBlockDef def) {
        super();
        this.hasTileEntity = false;	// No tile entity for us
        this.def = def;
        def.doStandardContructorSettings(this);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        WCBedItem.block = this;
        def.doStandardRegisterActions(this, WCBedItem.class);
        
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
        def.defaultRegisterTextures(mtd);
        WesterosBlockDef def = this.getWBDefinition();
        String blkname = def.getBlockName(0);
        Subblock sb = def.getByMeta(0);
        if ((sb != null) && (sb.textures != null)) {
            // Register textures 
            TextureModifier tmod = TextureModifier.NONE;
            if (def.nonOpaque) {
                tmod = TextureModifier.CLEARINSIDE;
            }
            BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
            // Set for all meta values for foot
            for (int meta = 0; meta < 8; meta++) {
                mtr.setMetaValue(meta);
            }
            int[] face_to_idx = new int[] { 1, 1, 3, 3, 5, 5 };
            for (int face = 0; face < 6; face++) {
                int fidx = face_to_idx[face];
                if (fidx >= sb.textures.size()) {
                    fidx = sb.textures.size() - 1;
                }
                String txtid = sb.textures.get(fidx);
                mtr.setPatchTexture(txtid.replace(':', '_'), tmod, face);
            }
            def.setBlockColorMap(mtr, sb);
            // Set for head
            mtr = mtd.addBlockTextureRecord(blkname);
            mtr.setTransparencyMode(TransparencyMode.TRANSPARENT);
            // Set for all meta values for head
            for (int meta = 8; meta < 16; meta++) {
                mtr.setMetaValue(meta);
            }
            face_to_idx = new int[] { 0, 0, 2, 2, 4, 4 };
            for (int face = 0; face < 6; face++) {
                int fidx = face_to_idx[face];
                if (fidx >= sb.textures.size()) {
                    fidx = sb.textures.size() - 1;
                }
                String txtid = sb.textures.get(fidx);
                mtr.setPatchTexture(txtid.replace(':', '_'), tmod, face);
            }
            def.setBlockColorMap(mtr, sb);
        }
        // Create east facing model
        PatchBlockModel mod = md.addPatchModel(blkname);
        mod.addPatch(0, 0.1875, 0, 1, 0.1875, 0, 0, 0.1875, 1, SideVisible.TOP); // Bottom
        mod.addPatch(0, 0.5625, 1, 1, 0.5625, 1, 0, 0.5625, 0, SideVisible.TOP); // Top
        mod.addPatch(0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0.5625, 100, SideVisible.BOTTOM); // Z- (flip)
        mod.addPatch(0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0.5625, 100, SideVisible.TOP); // Z+
        mod.addPatch(0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0.5625, 100, SideVisible.TOP); // X-
        mod.addPatch(1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0.5625, 100, SideVisible.TOP); // X+
        mod.setMetaValue(3);
        mod.setMetaValue(7);
        mod.setMetaValue(11);
        mod.setMetaValue(15);
        // Make north facing model
        PatchBlockModel nmod = md.addRotatedPatchModel(blkname, mod, 0, 270, 0);
        nmod.setMetaValue(2);
        nmod.setMetaValue(6);
        nmod.setMetaValue(10);
        nmod.setMetaValue(14);
        // Make south facing model
        PatchBlockModel smod = md.addRotatedPatchModel(blkname, mod, 0, 90, 0);
        smod.setMetaValue(0);
        smod.setMetaValue(4);
        smod.setMetaValue(8);
        smod.setMetaValue(12);
        // Make west facing model
        PatchBlockModel wmod = md.addRotatedPatchModel(blkname, mod, 0, 180, 0);
        wmod.setMetaValue(1);
        wmod.setMetaValue(5);
        wmod.setMetaValue(9);
        wmod.setMetaValue(13);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    @Override
    public IProperty<?>[] getNonRenderingProperties() { 
        return new IProperty<?>[] { BlockBed.OCCUPIED };
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
    // Force to use model for now
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return false;
    }
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.CLOTH;
    }
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return null;
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

}
