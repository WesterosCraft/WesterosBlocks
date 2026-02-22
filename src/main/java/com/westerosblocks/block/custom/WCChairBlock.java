package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.entity.custom.ChairEntity;
import com.westerosblocks.utils.ModWoodType;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

import com.westerosblocks.data.BlockDefinition;

public class WCChairBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;

    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 7);
    private static final VoxelShape CHAIR_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 18, 14);

    private final Map<BlockState, VoxelShape> shapeByIndex;

    public WCChairBlock(AbstractBlock.Settings settings) {
        this(settings, null, "chair", "building_blocks", "oak");
    }

    public WCChairBlock(AbstractBlock.Settings settings, BlockDefinition def, String blockName, String creativeTab, String woodType) {
        this(settings, def, blockName, creativeTab, ModWoodType.getWoodType(woodType));
    }

    public WCChairBlock(AbstractBlock.Settings settings, BlockDefinition def, String blockName, String creativeTab, WoodType woodType) {
        super(settings);
        this.def = def;

        setDefaultState(getDefaultState().with(ROTATION, 0));

        this.shapeByIndex = this.makeShapes();
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            // Handle null definition for manual block creation
            AbstractBlock.Settings settings = definition != null
                    ? definition.makeSettings()
                    : AbstractBlock.Settings.create();
            String blockName = definition != null ? definition.getBlockName() : "chair";
            String creativeTab = definition != null ? definition.getCreativeTab() : "building_blocks";
            String woodType = definition != null ? definition.getWoodType() : "oak";

            return new WCChairBlock(settings, definition, blockName, creativeTab, woodType);
        }
    }

    private Map<BlockState, VoxelShape> makeShapes() {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (int rotation = 0; rotation < 8; rotation++) {
            VoxelShape shape = CHAIR_SHAPE;

            BlockState state = this.getDefaultState()
                    .with(ROTATION, rotation);

            builder.put(state, shape);
        }

        return builder.build();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            Entity entity = null;
            List<ChairEntity> entities = world.getEntitiesByType(ModEntities.CHAIR, new Box(pos), chair -> true);
            if (entities.isEmpty()) {
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
        builder.add(ROTATION);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        int rotation = MathHelper.floor((double) (ctx.getPlayerYaw() * 8.0F / 360.0F) + 0.5D) & 7;

        return this.getDefaultState().with(ROTATION, rotation);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
