package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

public class WCCuboidNSEWBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x3);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3 }, new int[] { 0 })) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCCuboidNSEWBlock(def) };
        }
    }
    
    private List<WesterosBlockDef.Cuboid> cuboids_by_meta[];
    protected static final List<EnumFacing> VALIDFACING = new ArrayList<EnumFacing>() {
    	private static final long serialVersionUID = 1L;
    	{
    		add(EnumFacing.EAST);
    		add(EnumFacing.SOUTH);
    		add(EnumFacing.WEST);
    		add(EnumFacing.NORTH);
    	}};
    public static final PropertyDirection FACING = PropertyDirection.create("facing", VALIDFACING);

    @SuppressWarnings("unchecked")
    protected WCCuboidNSEWBlock(WesterosBlockDef def) {
        super(def);
        cuboids_by_meta = (List<WesterosBlockDef.Cuboid>[])new List[16];
        for (int i = 0; i < 4; i++) {
            List<WesterosBlockDef.Cuboid> lst = def.getCuboidList(i);
            if (lst == null) continue;
            cuboids_by_meta[i] = lst;
            cuboids_by_meta[i+4] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+8] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+12] = new ArrayList<WesterosBlockDef.Cuboid>();
            for (WesterosBlockDef.Cuboid c : lst) {
                cuboids_by_meta[i+4].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
                cuboids_by_meta[i+8].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
                cuboids_by_meta[i+12].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
            }
            setBoundingBoxFromCuboidList(i);
            setBoundingBoxFromCuboidList(i+4);
            setBoundingBoxFromCuboidList(i+8);
            setBoundingBoxFromCuboidList(i+12);
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
        return this.getDefaultState().withProperty(variant, meta & 0x3).withProperty(FACING, VALIDFACING.get(meta >> 2));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer) state.getValue(variant) + (VALIDFACING.indexOf(state.getValue(FACING)) << 2);
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
        IBlockState state = this.getStateFromMeta(meta & 0x3);
        EnumFacing enumfacing;
        switch (facing) {
            case DOWN:
                pos = pos.down();
                enumfacing = EnumFacing.fromAngle((double)placer.rotationYaw + 180.0F);
                break;
            case UP:
                pos = pos.up();
                enumfacing = EnumFacing.fromAngle((double)placer.rotationYaw + 180.0F);
                break;
            default:
                enumfacing = facing.getOpposite();
                break;
        }
        return state.withProperty(FACING, enumfacing);
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

}
