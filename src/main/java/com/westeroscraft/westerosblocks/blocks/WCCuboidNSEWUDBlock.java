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
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

public class WCCuboidNSEWUDBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x1);
            if (!def.validateMetaValues(new int[] { 0, 1 }, new int[] { 0 })) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCCuboidNSEWUDBlock(def) };
        }
    }
    
    private List<WesterosBlockDef.Cuboid> cuboids_by_meta[];

    private static final List<EnumFacing> VALIDFACING = new ArrayList<EnumFacing>() {
    	private static final long serialVersionUID = 1L;

    	{
    		add(EnumFacing.EAST);
    		add(EnumFacing.SOUTH);
    		add(EnumFacing.WEST);
    		add(EnumFacing.NORTH);
    		add(EnumFacing.DOWN);
    		add(EnumFacing.UP);
    	}};
    public static final PropertyDirection FACING = PropertyDirection.create("facing", VALIDFACING);

    @SuppressWarnings("unchecked")
    protected WCCuboidNSEWUDBlock(WesterosBlockDef def) {
        super(def);

        cuboids_by_meta = (List<WesterosBlockDef.Cuboid>[])new List[16];
        meta_per_sub = 6;	// For NSEWUD cubiod - 6 per sub
        for (int i = 0; i < 2; i++) {
            List<WesterosBlockDef.Cuboid> lst = def.getCuboidList(i);
            if (lst == null) continue;
            cuboids_by_meta[i] = lst;
            cuboids_by_meta[i+2] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+4] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+6] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+8] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+10] = new ArrayList<WesterosBlockDef.Cuboid>();
            for (WesterosBlockDef.Cuboid c : lst) {
                cuboids_by_meta[i+2].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
                cuboids_by_meta[i+4].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
                cuboids_by_meta[i+6].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
                cuboids_by_meta[i+8].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTZ90));
                cuboids_by_meta[i+10].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTZ270));
            }
            setBoundingBoxFromCuboidList(i);
            setBoundingBoxFromCuboidList(i+2);
            setBoundingBoxFromCuboidList(i+4);
            setBoundingBoxFromCuboidList(i+6);
            setBoundingBoxFromCuboidList(i+8);
            setBoundingBoxFromCuboidList(i+10);
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
        return this.getDefaultState().withProperty(variant, meta & 0x1).withProperty(FACING, VALIDFACING.get(meta >> 1));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer) state.getValue(variant) + (VALIDFACING.indexOf(state.getValue(FACING)) << 1);
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
        IBlockState state = this.getStateFromMeta(meta & 0x1);
        return state.withProperty(FACING, facing.getOpposite());
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
