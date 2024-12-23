//package com.westerosblocks.block.custom;
//
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockFactory;
//import com.westerosblocks.block.WesterosBlockLifecycle;
//import net.minecraft.block.AbstractBlock;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.DoorBlock;
//import net.minecraft.block.enums.DoubleBlockHalf;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldView;
//
//public class WCDoorBlock extends DoorBlock implements WesterosBlockLifecycle {
//
//    public static class Factory extends WesterosBlockFactory {
//        @Override
//        public Block buildBlockClass(WesterosBlockDef def) {
//            AbstractBlock.Settings settings = def.makeBlockSettings();
//            Block blk = new WCDoorBlock(settings, def);
//            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
//        }
//    }
//
//    private final WesterosBlockDef def;
//    private boolean locked = false;
//    private boolean allow_unsupported = false;
//
//    protected WCDoorBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
//        super(AuxileryUtils.getBlockSetType(settings, def), settings);
//        this.def = def;
//        String type = def.getType();
//        if (type != null) {
//            String[] toks = type.split(",");
//            for (String tok : toks) {
//                if (tok.equals("allow-unsupported")) {
//                    allow_unsupported = true;
//                }
//                String[] flds = tok.split(":");
//                if (flds.length < 2) continue;
//                if (flds[0].equals("locked")) {
//                    locked = flds[1].equals("true");
//                }
//            }
//        }
//    }
//
//    @Override
//    public WesterosBlockDef getWBDefinition() {
//        return def;
//    }
//
//    @Override
//    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
//                                 BlockHitResult hit) {
//        if (this.locked) {
//            return ActionResult.PASS;
//        } else {
//            return super.onUse(state, world, pos, player, hit);
//        }
//    }
//
//    @Override
//    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
//        if (allow_unsupported && (state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)) return true;
//        return super.canPlaceAt(state, world, pos);
//    }
//
//    private static final String[] TAGS = {"doors"};
//
//    @Override
//    public String[] getBlockTags() {
//        return TAGS;
//    }
//
//}
