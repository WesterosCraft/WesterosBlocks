package com.westeroscraft.westerosblocks.blocks;

import java.util.Random;

import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.CuboidBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCHalfDoorBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0);
            // Validate meta : we require meta 0, and only allow it
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCHalfDoorBlock(def) };
        }
    }
    
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

    private WesterosBlockDef def;
    private boolean locked = false;
    
    protected WCHalfDoorBlock(WesterosBlockDef def) {
        super(def.getMaterial());
        this.def = def;
        def.doStandardContructorSettings(this);
        String type = def.getType(0);
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String [] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("locked")) {
                    locked = flds[1].equals("true");
                }
            }
        }
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
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
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        def.getStandardCreativeItems(this, tab, list);
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
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }    

    private int getIconIndex(int meta, int side) {
        boolean flag1 = false;
        if (side != 1 && side != 0) {
            int direction = meta & 3;
            boolean flag = (meta & 4) != 0;

            if (flag) {
                if (direction == 0 && side == 2) {
                    flag1 = !flag1;
                } else if (direction == 1 && side == 5) {
                    flag1 = !flag1;
                } else if (direction == 2 && side == 3) {
                    flag1 = !flag1;
                } else if (direction == 3 && side == 4) {
                    flag1 = !flag1;
                }
            } else {
                if (direction == 0 && side == 5) {
                    flag1 = !flag1;
                } else if (direction == 1 && side == 3) {
                    flag1 = !flag1;
                } else if (direction == 2 && side == 4) {
                    flag1 = !flag1;
                } else if (direction == 3 && side == 2) {
                    flag1 = !flag1;
                }

                if ((meta & 8) != 0) {
                    flag1 = !flag1;
                }
            }
        }
        return (flag1 ? 1 : 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rnd) {
        def.randomDisplayTick(stateIn, worldIn, pos, rnd);
        super.randomDisplayTick(stateIn, worldIn, pos, rnd);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {BlockDoor.FACING, BlockDoor.OPEN, BlockDoor.HINGE});
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName(0);
        def.defaultRegisterTextures(mtd);
        // Register texture, and flip version
        Subblock sb = def.getByMeta(0);
        if ((sb == null) || (sb.textures == null) || (sb.textures.size() == 0)) return;
        String txt = sb.textures.get(0);
        BlockTextureRecord btr = mtd.addBlockTextureRecord(blkname);
        btr.setTransparencyMode(TransparencyMode.TRANSPARENT);
        btr.setPatchTexture(txt, TextureModifier.NONE, 0);
        btr.setPatchTexture(txt, TextureModifier.FLIPHORIZ, 1);
        def.setBlockColorMap(btr, sb);
        // Register model for each meta
        for (int meta = 0; meta < 16; meta++) {
            CuboidBlockModel mod = md.addCuboidModel(blkname);
            mod.setMetaValue(meta);
            int[] txtids = new int[] { this.getIconIndex(meta, 0), this.getIconIndex(meta, 1), this.getIconIndex(meta, 2),
                    this.getIconIndex(meta, 3), this.getIconIndex(meta, 4), this.getIconIndex(meta, 5) };
            switch (meta) {
                case 0:
                case 7:
                case 8:
                case 13:
                    mod.addCuboid(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0, txtids);
                    break;
                case 1:
                case 4:
                case 9:
                case 14:
                    mod.addCuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875, txtids);
                    break;
                case 3:
                case 6:
                case 11:
                case 12:
                    mod.addCuboid(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0, txtids);
                    break;
                case 5:
                case 2:
                case 10:
                case 15:
                    mod.addCuboid(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0, txtids);
                    break;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor() {
        return def.colorMultiplier();
    }
    
    @Override
    public IProperty<?>[] getNonRenderingProperties() { return null; }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockDoor.FACING, rot.rotate((EnumFacing)state.getValue(BlockDoor.FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return mirrorIn == Mirror.NONE ? state : state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(BlockDoor.FACING))).cycleProperty(BlockDoor.HINGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	IBlockState bs = this.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW());
    	bs = bs.withProperty(BlockDoor.HINGE, (meta & 8) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT);
    	return bs.withProperty(BlockDoor.OPEN, Boolean.valueOf((meta & 4) > 0));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (state.getValue(BlockDoor.HINGE) == BlockDoor.EnumHingePosition.RIGHT) {
            i |= 8;
        }
        i = i | ((EnumFacing)state.getValue(BlockDoor.FACING)).rotateY().getHorizontalIndex();
        if (((Boolean)state.getValue(BlockDoor.OPEN)).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }
    
    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(BlockDoor.OPEN).booleanValue();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (this.locked)
        {
            return false; //Allow items to interact with the door
        }
        else
        {
            state = state.cycleProperty(BlockDoor.OPEN);
            worldIn.setBlockState(pos, state, 10);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playEvent(playerIn, ((Boolean)state.getValue(BlockDoor.OPEN)).booleanValue() ? this.getOpenSound() : this.getCloseSound(), pos, 0);
            return true;
        }
    }

    private int getCloseSound() {
    	return this.blockMaterial == Material.IRON ? 1011 : 1012;
    }

    private int getOpenSound() {
    	return this.blockMaterial == Material.IRON ? 1005 : 1006;
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = this.getStateFromMeta(meta & 0x3);
        EnumFacing enumfacing;
        switch (facing) {
            case DOWN:
                enumfacing = EnumFacing.fromAngle((double)placer.rotationYaw + 180.0F);
                break;
            case UP:
                enumfacing = EnumFacing.fromAngle((double)placer.rotationYaw + 180.0F);
                break;
            default:
                enumfacing = facing;
                break;
        }
        int i = enumfacing.getFrontOffsetX();
        int j = enumfacing.getFrontOffsetZ();
        boolean isRightHinge = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;

        return state.withProperty(BlockDoor.FACING, enumfacing).withProperty(BlockDoor.HINGE, isRightHinge?BlockDoor.EnumHingePosition.RIGHT:BlockDoor.EnumHingePosition.LEFT);
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(BlockDoor.FACING);
        boolean flag = !((Boolean)state.getValue(BlockDoor.OPEN)).booleanValue();
        boolean flag1 = state.getValue(BlockDoor.HINGE) == BlockDoor.EnumHingePosition.RIGHT;

        switch (enumfacing)
        {
            case EAST:
            default:
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST:
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
        }
    }

    // Modified to prevent breaking when unsupported
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean flag = worldIn.isBlockPowered(pos);

        if (blockIn != this && (flag || blockIn.getDefaultState().canProvidePower()) && flag != ((Boolean)state.getValue(BlockDoor.OPEN)).booleanValue()) {
            worldIn.setBlockState(pos, state.withProperty(BlockDoor.OPEN, Boolean.valueOf(flag)), 2);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playEvent((EntityPlayer)null, flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
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
