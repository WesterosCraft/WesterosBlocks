package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.items.WCCuboidNSEWStackItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;

public class WCCuboidNSEWStackBlock extends WCCuboidNSEWBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x3);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3 }, new int[] { 0, 1 })) {
                return null;
            }
            // Force any top blocks to have no inventory item
            if (def.subBlocks != null) {
                int matches = 0;
                for (WesterosBlockDef.Subblock sb : def.subBlocks) {
                    if ((sb.meta & 0x1) == 1) {
                        sb.noInventoryItem = true;
                    }
                    matches |= (1 << sb.meta);
                }
                for (int i = 0; i < 2; i++) {
                    switch (matches >> (2*i)) {
                        case 0:
                        case 3:
                            break;
                        default:
                            WesterosBlocks.log.error(String.format("unmatched stacked subblocks %d in block '%s'", 2*i, def.blockType));
                            return null;
                    }
                }
            }
            // Build variant list (bit 1 of defined bottom blocks)
            List<Integer> metalist = def.getDefinedBaseMeta();
            ArrayList<Integer> variant_list = new ArrayList<Integer>();
            for (Integer id : metalist) {
                if ((id % 2) == 0)
                    variant_list.add(id / 2);
            }
            new_variant = PropertyMeta.create("variant", variant_list);

            return new Block[] { new WCCuboidNSEWStackBlock(def) };
        }
    }
    
    public static PropertyBool TOP = PropertyBool.create("top");

    private boolean noBreakUnder[] = new boolean[2];
    
    protected WCCuboidNSEWStackBlock(WesterosBlockDef def) {
        super(def);
        if (def.subBlocks != null) {
            for (WesterosBlockDef.Subblock sb : def.subBlocks) {
                if ((sb.meta & 1) == 0) {
                    String type = def.getType(sb.meta);
                    if (type == null) continue;
                    String[] toks = type.split(",");
                    for (String tok : toks) {
                        if (tok.equals("no-break-under")) {
                            noBreakUnder[sb.meta >> 1] = true;
                        }
                    }
                }
            }
        }
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, WCCuboidNSEWStackItem.class);
        
        return true;
    }
   
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        int var = state.getValue(variant).intValue();
        // Are we top half of block
        if (state.getValue(TOP).booleanValue()) {
            BlockPos blockposbottom = pos.down();
            IBlockState statebottom = worldIn.getBlockState(blockposbottom);
            // If bottom doesn't match top
            if ((statebottom.getBlock() != this) || (state.getValue(variant) != statebottom.getValue(variant))) {
                worldIn.setBlockToAir(pos);
            }
            else if (blockIn != this) {
                statebottom.neighborChanged(worldIn, blockposbottom, blockIn, fromPos);
            }
        }
        else {
            boolean didBreak = false;
            BlockPos blockpostop = pos.up();
            IBlockState statetop = worldIn.getBlockState(blockpostop);
            // If top doesn't match bottom
            if ((statetop.getBlock() != this) || (state.getValue(variant) != statetop.getValue(variant))) {
                worldIn.setBlockToAir(pos);
                didBreak = true;
            }

            if ((!noBreakUnder[var]) && (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn,  pos.down(), EnumFacing.UP))) {
                worldIn.setBlockToAir(pos);
                didBreak = true;

                if (statetop.getBlock() == this) {
                    worldIn.setBlockToAir(blockpostop);
                }
            }

            if (didBreak) {
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return pos.getY() >= worldIn.getHeight() - 1 ? false : worldIn.getBlockState(pos.down()).isSideSolid(worldIn,  pos.down(), EnumFacing.UP) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        if (new_variant != null) {
            variant = new_variant;
            new_variant = null;
        }
        return new BlockStateContainer(this, new IProperty[] { FACING, TOP, variant });
    }
    
    // map from state to meta and vice verca - use highest bit for polished boolean, use low 2 bits for variant
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(variant, (meta & 0x2) >> 1).withProperty(FACING, VALIDFACING.get(meta >> 2)).withProperty(TOP, (meta & 1) != 0);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(variant).intValue() << 1) + (VALIDFACING.indexOf(state.getValue(FACING)) << 2) + (state.getValue(TOP)?1:0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, state.getValue(variant).intValue() << 1);
    }
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(variant).intValue() << 1;
    }
}
