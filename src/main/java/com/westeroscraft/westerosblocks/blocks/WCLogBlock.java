package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import org.dynmap.modsupport.BlockSide;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
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

import it.unimi.dsi.fastutil.Arrays;

public class WCLogBlock extends BlockLog implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x3);
            // Make sure meta values only for 0-3 (other bits are orientation
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3 }, null)) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCLogBlock(def) };
        }
    }
    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    private static PropertyMeta new_variant = null;

    private WesterosBlockDef def;
    private boolean isSolidOpaque = true;
    
    private PropertyMeta variant;
    private static BlockLog.EnumAxis[] orientation = { BlockLog.EnumAxis.Y, BlockLog.EnumAxis.X, BlockLog.EnumAxis.Z, BlockLog.EnumAxis.NONE };
    
    protected WCLogBlock(WesterosBlockDef def) {
        super();
        this.isSolidOpaque = !def.nonOpaque;
        this.def = def;
        def.doStandardContructorSettings(this);
        setSoundType(def.getStepSound());
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }

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
    public BlockRenderLayer getBlockLayer()
    {
        return (def.alphaRender?BlockRenderLayer.TRANSLUCENT:BlockRenderLayer.SOLID);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return isSolidOpaque;
    }
    
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        String blkname = def.getBlockName(0);
        // Modifiers for each orientation
        TextureModifier tmod[][] = { 
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270 },
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE }
        };
        // Texture index for each orientation
        int txtid[][] = {
            { 0, 0, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 0, 0 },
            { 1, 1, 0, 0, 1, 1 },
            { 1, 1, 1, 1, 1, 1 }
        };

        for (WesterosBlockDef.Subblock sb : def.subBlocks) {
            if (sb == null) continue;
            if (sb.textures == null) continue;
            for (int i = 0; i < 4; i++) {   // 4 orientations
                BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
                mtr.setMetaValue(4*i + sb.meta);
                for (int face = 0; face < 6; face++) {
                    int fidx = txtid[i][face];
                    if (fidx >= sb.textures.size()) {
                        fidx = sb.textures.size() - 1;
                    }
                    String txt = sb.textures.get(fidx);
                    mtr.setSideTexture(txt.replace(':', '_'), tmod[i][face], BlockSide.valueOf("FACE_" + face));
                }
                def.setBlockColorMap(mtr, sb);
            }
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rnd) {
        def.randomDisplayTick(stateIn, worldIn, pos, rnd);
        super.randomDisplayTick(stateIn, worldIn, pos, rnd);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        if (new_variant != null) {
            variant = new_variant;
            new_variant = null;
        }
        return new BlockStateContainer(this, new IProperty[] { LOG_AXIS, variant });
    }
    
    // map from state to meta and vice verca - use highest bit for polished boolean, use low 2 bits for variant
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	
    	return this.getDefaultState().withProperty(LOG_AXIS, orientation[meta >> 2]).withProperty(this.variant, meta & 3);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        int idx = (Integer) state.getValue(this.variant);
        BlockLog.EnumAxis o = state.getValue(LOG_AXIS);
        int ord = 0;
        for (ord = 0; ord < orientation.length; ord++) {
        	if (orientation[ord] == o) {
        		return (4*ord) + idx;
        	}
        }
        return idx;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
    	return def.colorMultiplier();
    }
    
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, state.getValue(variant).intValue());
    }
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(variant).intValue();
    }

}
