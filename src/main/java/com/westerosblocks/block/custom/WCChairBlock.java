package com.westerosblocks.block.custom;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.entity.custom.ChairEntity;
import com.westerosblocks.util.ModWoodType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.block.WoodType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;

public class WCChairBlock extends HorizontalFacingBlock {
    public static final MapCodec<WCChairBlock> CODEC = createCodec(WCChairBlock::new);

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 7);

    // Simple single VoxelShape that encompasses the entire chair
    // Covers from ground level (0) to backrest height (21), and spans the full width
    private static final VoxelShape CHAIR_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 21, 16);

    private final String blockName;
    private final String creativeTab;
    private final WoodType woodType;

    public WCChairBlock(AbstractBlock.Settings settings) {
        this(settings, "chair", "building_blocks", "oak");
    }

    public WCChairBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, "oak");
    }

    public WCChairBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType) {
        this(settings, blockName, creativeTab, ModWoodType.getWoodType(woodType));
    }

    public WCChairBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, WoodType woodType) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
        setDefaultState(getDefaultState().with(ROTATION, 0));
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(!world.isClient()) {
            Entity entity = null;
            List<ChairEntity> entities = world.getEntitiesByType(ModEntities.CHAIR, new Box(pos), chair -> true);
            if(entities.isEmpty()) {
                entity = ModEntities.CHAIR.spawn((ServerWorld) world, pos, SpawnReason.TRIGGERED);
            } else {
                entity = entities.get(0);
            }

            player.startRiding(entity);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Use the same approach as cuboid blocks for 8-directional rotation
        // But adjust the rotation so the chair faces the player
        int rotation = MathHelper.floor((double) (ctx.getPlayerYaw() * 8.0F / 360.0F) + 0.5D) & 7;
        
        // Adjust rotation so chair faces the player (backrest towards player)
        rotation = (rotation + 4) % 8;
        
        return this.getDefaultState().with(ROTATION, rotation).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    private VoxelShape getShapeForState(BlockState state) {
        // Return the same simple shape regardless of rotation
        return CHAIR_SHAPE;
    }
} 