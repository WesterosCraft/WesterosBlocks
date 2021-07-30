package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.CuboidBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import javax.annotation.Nullable;

public class WCHalfDoorBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCHalfDoorBlock(props, def)), false, false);
        }        
    }

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    private WesterosBlockDef def;
    private boolean locked = false;
    
    protected WCHalfDoorBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String type = def.getType();
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
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)).setValue(HINGE, DoorHingeSide.LEFT).setValue(POWERED, Boolean.valueOf(false)));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext selCtx) {
        Direction direction = state.getValue(FACING);
        boolean flag = !state.getValue(OPEN);
        boolean flag1 = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        switch(direction) {
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

    @Override
     public boolean isPathfindable(BlockState state, IBlockReader reader, BlockPos pos, PathType path) {
        switch(path) {
        case LAND:
           return state.getValue(OPEN);
        case WATER:
           return false;
        case AIR:
           return state.getValue(OPEN);
        default:
           return false;
        }
     }

     private int getCloseSound() {
        return this.material == Material.METAL ? 1011 : 1012;
     }

     private int getOpenSound() {
        return this.material == Material.METAL ? 1005 : 1006;
     }

     @Nullable
     @Override
     public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        if (blockpos.getY() < 255 && ctx.getLevel().getBlockState(blockpos.above()).canBeReplaced(ctx)) {
           World world = ctx.getLevel();
           boolean flag = world.hasNeighborSignal(blockpos) || world.hasNeighborSignal(blockpos.above());
           return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(HINGE, this.getHinge(ctx)).setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag));
        } else {
           return null;
        }
     }

     private DoorHingeSide getHinge(BlockItemUseContext ctx) {
        IBlockReader iblockreader = ctx.getLevel();
        BlockPos blockpos = ctx.getClickedPos();
        Direction direction = ctx.getHorizontalDirection();
        BlockPos blockpos1 = blockpos.above();
        Direction direction1 = direction.getCounterClockWise();
        BlockPos blockpos2 = blockpos.relative(direction1);
        BlockState blockstate = iblockreader.getBlockState(blockpos2);
        BlockPos blockpos3 = blockpos1.relative(direction1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos3);
        Direction direction2 = direction.getClockWise();
        BlockPos blockpos4 = blockpos.relative(direction2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos4);
        BlockPos blockpos5 = blockpos1.relative(direction2);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos5);
        int i = (blockstate.isCollisionShapeFullBlock(iblockreader, blockpos2) ? -1 : 0) + (blockstate1.isCollisionShapeFullBlock(iblockreader, blockpos3) ? -1 : 0) + (blockstate2.isCollisionShapeFullBlock(iblockreader, blockpos4) ? 1 : 0) + (blockstate3.isCollisionShapeFullBlock(iblockreader, blockpos5) ? 1 : 0);
        boolean flag = blockstate.is(this);
        boolean flag1 = blockstate2.is(this);
        if ((!flag || flag1) && i <= 0) {
           if ((!flag1 || flag) && i >= 0) {
              int j = direction.getStepX();
              int k = direction.getStepZ(); 
              Vector3d vector3d = ctx.getClickLocation();
              double d0 = vector3d.x - (double)blockpos.getX();
              double d1 = vector3d.z - (double)blockpos.getZ();
              return (j >= 0 || !(d1 < 0.5D)) && (j <= 0 || !(d1 > 0.5D)) && (k >= 0 || !(d0 > 0.5D)) && (k <= 0 || !(d0 < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
           } else {
              return DoorHingeSide.LEFT;
           }
        } else {
           return DoorHingeSide.RIGHT;
        }
     }
 
     @Override
     public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity entity, Hand hand, BlockRayTraceResult rtresult) { 
        if (this.locked) {
           return ActionResultType.PASS; 
        } else {
           state = state.cycle(OPEN);
           world.setBlock(pos, state, 10);
           world.levelEvent(entity, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
           return ActionResultType.sidedSuccess(world.isClientSide);
        }
     }

     public boolean isOpen(BlockState state) {
        return state.getValue(OPEN);
     }

     public void setOpen(World world, BlockState state, BlockPos pos, boolean open) {
        if (state.is(this) && state.getValue(OPEN) != open) {
           world.setBlock(pos, state.setValue(OPEN, Boolean.valueOf(open)), 10);
           this.playSound(world, pos, open);
        }
     }

     @Override
     public void neighborChanged(BlockState state, World world, BlockPos ppos, Block block, BlockPos pos2, boolean chg) {
        boolean flag = world.hasNeighborSignal(ppos);
        if (block != this && flag != state.getValue(POWERED)) {
           if (flag != state.getValue(OPEN)) {
              this.playSound(world, ppos, flag);
           }

           world.setBlock(ppos, state.setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag)), 2);
        }

     }

     @Override
     public boolean canSurvive(BlockState state, IWorldReader reader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        return blockstate.isFaceSturdy(reader, blockpos, Direction.UP);
     }

     private void playSound(World world, BlockPos pos, boolean open) {
        world.levelEvent((PlayerEntity)null, open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
     }

     @Override
     public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
     }

     @Override
     public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
     }

     @Override
     public BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.getValue(FACING))).cycle(HINGE);
     }

     @OnlyIn(Dist.CLIENT) 
     @Override
     public long getSeed(BlockState state, BlockPos pos) {
        return MathHelper.getSeed(pos.getX(), pos.below(0).getY(), pos.getZ());
     }
     
     @Override
     protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> state) {
        state.add(FACING, OPEN, HINGE, POWERED);
     }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName();
        def.defaultRegisterTextures(mtd);
        // Register texture, and flip version
        if ((def.textures == null) || (def.textures.size() == 0)) return;
        String txt = def.textures.get(0);
        BlockTextureRecord btr = mtd.addBlockTextureRecord(blkname);
        btr.setTransparencyMode(TransparencyMode.TRANSPARENT);
        btr.setPatchTexture(txt, TextureModifier.NONE, 0);
        btr.setPatchTexture(txt, TextureModifier.FLIPHORIZ, 1);
        def.setBlockColorMap(btr);
        // Register model for each meta
        for (int meta = 0; meta < 16; meta++) {
            CuboidBlockModel mod = md.addCuboidModel(blkname);
            mod.setMetaValue(meta);
            int[] txtids = new int[] { 0, 0, 0, 0, 0, 0 };
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
    private static String[] TAGS = { "doors" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
}
