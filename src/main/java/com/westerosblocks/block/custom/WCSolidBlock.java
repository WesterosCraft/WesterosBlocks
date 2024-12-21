package com.westerosblocks.block.custom;


import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WCSolidBlock extends Block implements WesterosBlockLifecycle {
    protected WesterosBlockDef def;
    protected VoxelShape collisionbox;
    protected VoxelShape supportbox;
    public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
    protected static IntProperty tempCONNECTSTATE;
    public final boolean connectstate;
    public static final BooleanProperty SYMMETRICAL = BooleanProperty.of("symmetrical");
    protected static BooleanProperty tempSYMMETRICAL;
    public final boolean symmetrical;
    public final Boolean symmetricalDef;
    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected WesterosBlockDef.StateProperty STATE;
    protected boolean toggleOnUse = false;

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties();
            WesterosBlockDef.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            // Process types
            String t = def.getType();
            boolean doConnectstate = false;
            Boolean doSymmetrical = null;
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                    String[] parts = tok.split(":");
                    // See if we have connectstate
                    if (parts[0].equals("connectstate")) {
                        doConnectstate = true;
                        tempCONNECTSTATE = CONNECTSTATE;
                    }
                    // See if we have symmetrical
                    if (parts[0].equals("symmetrical")) {
                        doSymmetrical = "true".equals(parts[1]);
                        tempSYMMETRICAL = SYMMETRICAL;
                    }
                }
            }
            boolean finalDoConnectstate = doConnectstate;
            Boolean finalDoSymmetrical = doSymmetrical;
            Block blk = new WCSolidBlock(settings, def, finalDoConnectstate, finalDoSymmetrical);

            return def.registerRenderType(blk, true, def.nonOpaque);
        }
    }

    protected WCSolidBlock(AbstractBlock.Settings props, WesterosBlockDef def, boolean doConnectstate, Boolean doSymmetrical) {
        super(props);
        this.def = def;

        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                    break;
                }
            }
        }

        collisionbox = def.makeCollisionBoxShape();
        if (def.supportBoxes == null) {
            supportbox = collisionbox;
        } else {
            supportbox = def.makeSupportBoxShape(null);
        }

        connectstate = doConnectstate;
        symmetrical = (doSymmetrical != null);
        symmetricalDef = doSymmetrical;
        BlockState defbs = getDefaultState();
        if (connectstate) {
            defbs = defbs.with(CONNECTSTATE, 0);
        }
        if (symmetrical) {
            defbs = defbs.with(SYMMETRICAL, symmetricalDef);
        }
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        setDefaultState(defbs);
    }

    protected WCSolidBlock(AbstractBlock.Settings props, WesterosBlockDef def, boolean doConnectstate) {
        this(props, def, doConnectstate, null);
    }

    protected WCSolidBlock(AbstractBlock.Settings props, WesterosBlockDef def) {
        this(props, def, false, null);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (tempCONNECTSTATE != null) {
            builder.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
        if (tempSYMMETRICAL != null) {
            builder.add(tempSYMMETRICAL);
            tempSYMMETRICAL = null;
        }
        if (tempSTATE != null) {
            STATE = tempSTATE;
            tempSTATE = null;
        }
        if (STATE != null) {
            builder.add(STATE);
        }
        super.appendProperties(builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return collisionbox;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return collisionbox;
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return supportbox;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (def.nonOpaque)
            return VoxelShapes.empty();
        else
            return collisionbox;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (def.nonOpaque) {
            return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
        } else {
            return false;
        }
    }

    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return (def.lightOpacity == 0) ? 1.0F : 0.2F;
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return (def.lightOpacity == 0);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState bs = super.getPlacementState(ctx);
        if (STATE != null && bs != null) {
            bs = bs.with(STATE, STATE.defValue);
        }
        return bs;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                 BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            state = state.cycle(this.STATE);
            world.setBlockState(pos, state, 10);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    private static final String[] TAGS = {};

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
