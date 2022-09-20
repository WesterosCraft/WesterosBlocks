package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.CuboidBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboidNEBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x7);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, new int[] { 0 })) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCCuboidNEBlock(def) };
        }
    }
    
    private static final List<EnumFacing> VALIDFACING = new ArrayList<EnumFacing>() {
    	private static final long serialVersionUID = 1L;

    	{
    		add(EnumFacing.EAST);
    		add(EnumFacing.NORTH);
    	}};
    public static final PropertyDirection FACING = PropertyDirection.create("facing", VALIDFACING);
    
    private List<WesterosBlockDef.Cuboid> cuboids_by_meta[];
    
    @SuppressWarnings("unchecked")
    protected WCCuboidNEBlock(WesterosBlockDef def) {
        super(def);
        
        meta_per_sub = 2;	// For NE cubiod - 2 per sub
        cuboids_by_meta = (List<WesterosBlockDef.Cuboid>[])new List[16];
        for (int i = 0; i < 8; i++) {
            List<WesterosBlockDef.Cuboid> lst = def.getCuboidList(i);
            if (lst == null) continue;
            cuboids_by_meta[i] = lst;
            cuboids_by_meta[i+8] = new ArrayList<WesterosBlockDef.Cuboid>();
            for (WesterosBlockDef.Cuboid c : lst) {
                cuboids_by_meta[i+8].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
            }
            setBoundingBoxFromCuboidList(i);
            setBoundingBoxFromCuboidList(i+8);
        }
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        if (new_variant != null) {
            variant = new_variant;
            new_variant = null;
        }
        return new BlockStateContainer(this, new IProperty[] { FACING, variant });
    }
    
    // map from state to meta and vice verca - use highest bit for polished boolean, use low 2 bits for variant
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(variant, meta & 0x7).withProperty(FACING, ((meta&8)!=0) ? EnumFacing.NORTH : EnumFacing.EAST);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer) state.getValue(variant) + ((state.getValue(FACING) == EnumFacing.NORTH)?8:0);
    }

    /**
     *  Get cuboid list at given meta
     *  @param meta
     */
    public List<WesterosBlockDef.Cuboid> getCuboidList(int meta) {
        return cuboids_by_meta[meta];
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = this.getStateFromMeta(meta & 0x7);
        EnumFacing enumfacing = (placer != null) ? EnumFacing.fromAngle((double)placer.rotationYaw) : facing;

        if ((enumfacing == EnumFacing.EAST) || (enumfacing == EnumFacing.WEST))
            state = state.withProperty(FACING, EnumFacing.EAST);
        else
            state = state.withProperty(FACING, EnumFacing.NORTH);
        return state;
    }

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
    
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        WesterosBlockDef def = this.getWBDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 6, TransparencyMode.TRANSPARENT, meta_per_sub);
        for (int meta = 0; meta < 15; meta++) {
            List<Cuboid> cl = cuboids_by_meta[meta];   
            if (cl == null) continue;
            CuboidBlockModel mod = md.addCuboidModel(blkname);
            for (Cuboid c : cl) {
                if (WesterosBlockDef.SHAPE_CROSSED.equals(c.shape)) {   // Crosed
                    mod.addCrossedPatches(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax, c.sideTextures[0]);
                }
                else {
                    mod.addCuboid(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax, c.sideTextures);
                }
            }
            mod.setMetaValue(meta);
        }
    }

}
