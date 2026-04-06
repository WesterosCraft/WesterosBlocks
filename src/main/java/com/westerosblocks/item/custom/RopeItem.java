package com.westerosblocks.item.custom;

import com.westerosblocks.entity.custom.RopeEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;

public class RopeItem extends Item {

    private static final String NBT_START_POS = "RopeStartPos";
    private static final String NBT_START_OFFSET_X = "RopeSOx";
    private static final String NBT_START_OFFSET_Y = "RopeSOy";
    private static final String NBT_START_OFFSET_Z = "RopeSOz";

    public RopeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient()) {
            return ActionResult.PASS;
        }
        ItemStack stack = context.getStack();
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        BlockPos clickedPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        Direction side = context.getSide();
        Vec3d hitPos = context.getHitPos();
        if (player != null && player.isUsingItem() && player.getActiveItem().isOf(net.minecraft.item.Items.SHEARS)) {
            RopeEntity rope = findRopeConnectedTo(world, clickedPos);
            if (rope != null) {
                rope.cycleVariant();
                player.sendMessage(Text.literal("Rope style toggled."), true);
                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(Text.literal("No rope found on this anchor."), true);
                return ActionResult.FAIL;
            }
        }
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(clickedPos);
        Vec3d attachWorld = projectToOutline(world, mutable, world.getBlockState(mutable), hitPos, side);
        Vec3d attachOffset = attachWorld.subtract(Vec3d.of(mutable));
        if (player != null && player.isSneaking()) {
            RopeEntity rope = findRopeConnectedTo(world, clickedPos);
            if (rope != null) {
                rope.cycleTensionLevel();
                player.sendMessage(Text.literal("Rope tension: " + rope.getTensionName()), true);
                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(Text.literal("No rope found on this anchor."), true);
                return ActionResult.FAIL;
            }
        }
        if (!nbt.contains(NBT_START_POS)) {
            nbt.putLong(NBT_START_POS, mutable.asLong());
            nbt.putFloat(NBT_START_OFFSET_X, (float) attachOffset.x);
            nbt.putFloat(NBT_START_OFFSET_Y, (float) attachOffset.y);
            nbt.putFloat(NBT_START_OFFSET_Z, (float) attachOffset.z);
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

            if (player != null) {
                player.sendMessage(Text.literal("Point 1 set!  Select Point 2!"), true);
            }
            return ActionResult.SUCCESS;
        }
        BlockPos startPos = BlockPos.fromLong(nbt.getLong(NBT_START_POS));
        Vec3d startOffset = new Vec3d(
                nbt.getFloat(NBT_START_OFFSET_X),
                nbt.getFloat(NBT_START_OFFSET_Y),
                nbt.getFloat(NBT_START_OFFSET_Z)
        );
        if (startPos.equals(mutable) && startOffset.squaredDistanceTo(attachOffset) < 1e-6) {
            if (player != null) {
                player.sendMessage(Text.literal("Pick a different second point."), true);
            }
            return ActionResult.FAIL;
        }
        RopeEntity rope = new RopeEntity(world, startPos, startOffset, mutable.toImmutable(), attachOffset);
        world.spawnEntity(rope);
        stack.remove(DataComponentTypes.CUSTOM_DATA);

        if (player != null) {
            player.sendMessage(Text.literal("Rope connected!"), true);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Right-click rope: toggle tension").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Empty hand on rope: cycle between rope and chain").formatted(Formatting.GRAY));
    }

    private static Vec3d projectToOutline(World world, BlockPos pos, net.minecraft.block.BlockState state,
                                          Vec3d hitWorld, Direction side) {
        VoxelShape shape = state.getOutlineShape(world, pos);
        if (shape.isEmpty()) {
            return Vec3d.ofCenter(pos);
        }
        Vec3d localHit = hitWorld.subtract(Vec3d.of(pos));
        double bestDist = Double.POSITIVE_INFINITY;
        Vec3d bestPoint = null;
        for (Box box : shape.getBoundingBoxes()) {
            double cx = MathHelper.clamp(localHit.x, box.minX, box.maxX);
            double cy = MathHelper.clamp(localHit.y, box.minY, box.maxY);
            double cz = MathHelper.clamp(localHit.z, box.minZ, box.maxZ);
            boolean inside = localHit.x > box.minX && localHit.x < box.maxX &&
                    localHit.y > box.minY && localHit.y < box.maxY &&
                    localHit.z > box.minZ && localHit.z < box.maxZ;
            if (inside) {
                if (side != null) {
                    switch (side) {
                        case EAST -> cx = box.maxX;
                        case WEST -> cx = box.minX;
                        case UP -> cy = box.maxY;
                        case DOWN -> cy = box.minY;
                        case SOUTH -> cz = box.maxZ;
                        case NORTH -> cz = box.minZ;
                    }
                } else {
                    double dxMin = Math.abs(localHit.x - box.minX);
                    double dxMax = Math.abs(box.maxX - localHit.x);
                    double dyMin = Math.abs(localHit.y - box.minY);
                    double dyMax = Math.abs(box.maxY - localHit.y);
                    double dzMin = Math.abs(localHit.z - box.minZ);
                    double dzMax = Math.abs(box.maxZ - localHit.z);
                    double min = dxMin;
                    cx = box.minX;
                    if (dxMax < min) {
                        min = dxMax;
                        cx = box.maxX;
                    }
                    if (dyMin < min) {
                        min = dyMin;
                        cy = box.minY;
                    }
                    if (dyMax < min) {
                        min = dyMax;
                        cy = box.maxY;
                    }
                    if (dzMin < min) {
                        min = dzMin;
                        cz = box.minZ;
                    }
                    if (dzMax < min) {
                        cz = box.maxZ;
                    }
                }
            }
            Vec3d candidate = new Vec3d(cx, cy, cz);
            double dist = candidate.squaredDistanceTo(localHit);
            if (dist < bestDist) {
                bestDist = dist;
                bestPoint = candidate;
            }
        }
        return bestPoint != null
                ? Vec3d.of(pos).add(bestPoint)
                : Vec3d.ofCenter(pos);
    }

    private static RopeEntity findRopeConnectedTo(World world, BlockPos anchor) {
        Box searchBox = new Box(anchor).expand(6.0);
        List<RopeEntity> ropes = world.getEntitiesByClass(RopeEntity.class, searchBox, e -> true);

        for (RopeEntity rope : ropes) {
            BlockPos start = rope.getStart();
            BlockPos end = rope.getEnd();
            if (anchor.equals(start) || anchor.equals(end) ||
                    start.isWithinDistance(anchor, 1.5) ||
                    end.isWithinDistance(anchor, 1.5)) {
                return rope;
            }
        }
        return null;
    }
}
