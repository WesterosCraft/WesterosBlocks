package com.westerosblocks.block.custom;


import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

public class WCSolidBlock extends Block implements WesterosBlockLifecycle {
    protected WesterosBlockDef def;
    protected VoxelShape collisionbox;
    protected VoxelShape supportbox;
    public static final IntegerProperty CONNECTSTATE = IntegerProperty.create("connectstate", 0, 3);
    protected static IntegerProperty tempCONNECTSTATE;
    public final boolean connectstate;

    public static final BooleanProperty SYMMETRICAL = BooleanProperty.create("symmetrical");
    protected static BooleanProperty tempSYMMETRICAL;

    public final boolean symmetrical;
    public final Boolean symmetricalDef;

    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected WesterosBlockDef.StateProperty STATE;

    protected boolean toggleOnUse = false;

    public WCSolidBlock(Settings settings) {
        super(settings);
    }

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def
//                , RegisterEvent.RegisterHelper<Block> helper
        ) {
            AbstractBlock.Settings settings = def.makeProperties();
            // See if we have a state property
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

            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
//            AuxileryUtils.registerCreativeTab(blk.asItem(), def.creativeTab);
            return def.registerRenderType(blk, true, def.nonOpaque);
            return null;
        }
    }

//    protected WCSolidBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doConnectstate, Boolean doSymmetrical) {
//        super(props);
//        this.def = def;
//
//        String t = def.getType();
//        if (t != null) {
//            String[] toks = t.split(",");
//            for (String tok : toks) {
//                if (tok.equals("toggleOnUse")) {
//                    toggleOnUse = true;
//                }
//            }
//        }
//
//        collisionbox = def.makeCollisionBoxShape();
//        if (def.supportBoxes == null) {
//            supportbox = collisionbox;
//        } else {
//            supportbox = def.makeSupportBoxShape(null);
//        }
//
//        connectstate = doConnectstate;
//        symmetrical = (doSymmetrical != null);
//        symmetricalDef = doSymmetrical;
//        BlockState defbs = this.stateDefinition.any();
//        if (connectstate) {
//            defbs = defbs.setValue(CONNECTSTATE, 0);
//        }
//        if (symmetrical) {
//            defbs = defbs.setValue(SYMMETRICAL, symmetricalDef);
//        }
//        if (STATE != null) {
//            defbs = defbs.setValue(STATE, STATE.defValue);
//        }
//        this.registerDefaultState(defbs);
//    }
//
//    protected WCSolidBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doConnectstate) {
//        this(props, def, doConnectstate, null);
//    }
//
//    protected WCSolidBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
//        this(props, def, false, null);
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
//        return collisionbox;
//    }
//
//    @Override
//    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
//        return collisionbox;
//    }
//
//    @Override
//    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
//        return supportbox;
//    }
//
//    @Override
//    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
//        if (def.nonOpaque)
//            return Shapes.empty();
//        else
//            return collisionbox;
//    }
//
//    @SuppressWarnings("deprecation")
//    public boolean skipRendering(BlockState state, BlockState other_state, Direction direction) {
//        if (def.nonOpaque)
//            return other_state.is(this) ? true : super.skipRendering(state, other_state, direction);
//        else
//            return false;
//    }
//
//
//    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
//        return (def.lightOpacity == 0) ? 1.0F : 0.2F;
//    }
//
//    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
//        return (def.lightOpacity == 0);
//    }
//

    //
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
//        if (tempCONNECTSTATE != null) {
//            StateDefinition.add(tempCONNECTSTATE);
//            tempCONNECTSTATE = null;
//        }
//        if (tempSYMMETRICAL != null) {
//            StateDefinition.add(tempSYMMETRICAL);
//            tempSYMMETRICAL = null;
//        }
//        if (tempSTATE != null) {
//            STATE = tempSTATE;
//            tempSTATE = null;
//        }
//        if (STATE != null) {
//            StateDefinition.add(STATE);
//        }
//        super.createBlockStateDefinition(StateDefinition);
//    }
//
//    @Override
//    @Nullable
//    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
//        BlockState bs = super.getStateForPlacement(ctx);
//        if (STATE != null) {
//            bs = bs.setValue(STATE, STATE.defValue);
//        }
//        return bs;
//    }
//
//    @Override
//    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
//        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandItem().isEmpty()) {
//            state = state.cycle(this.STATE);
//            level.setBlock(pos, state, 10);
//            level.levelEvent(player, 1006, pos, 0);
//            return InteractionResult.sidedSuccess(level.isClientSide);
//        } else {
//            return InteractionResult.PASS;
//        }
//    }
//
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
