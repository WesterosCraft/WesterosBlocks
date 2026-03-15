package com.westerosblocks.entity.custom;

import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RopeEntity extends Entity {
    private static final TrackedData<Long> START_POS;
    private static final TrackedData<Long> END_POS;
    private static final TrackedData<Float> START_OFFSET_X;
    private static final TrackedData<Float> START_OFFSET_Y;
    private static final TrackedData<Float> START_OFFSET_Z;
    private static final TrackedData<Float> END_OFFSET_X;
    private static final TrackedData<Float> END_OFFSET_Y;
    private static final TrackedData<Float> END_OFFSET_Z;
    private static final TrackedData<Integer> TENSION_LEVEL;
    private static final TrackedData<Integer> VARIANT;

    public RopeEntity(EntityType<? extends RopeEntity> type, World world) {
        super(type, world);
        this.intersectionChecked = true;
        this.ignoreCameraFrustum = true;
    }

    public RopeEntity(World world, BlockPos start, Vec3d startOffset, BlockPos end, Vec3d endOffset) {
        this(ModEntities.ROPE_ENTITY, world);
        this.setStart(start);
        this.setEnd(end);
        this.setStartOffset(startOffset);
        this.setEndOffset(endOffset);
        this.setTensionLevel(6);
        this.setPosition(Vec3d.ofCenter(start));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(START_POS, BlockPos.ORIGIN.asLong());
        builder.add(END_POS, BlockPos.ORIGIN.asLong());
        builder.add(START_OFFSET_X, 0.5f);
        builder.add(START_OFFSET_Y, 0.5f);
        builder.add(START_OFFSET_Z, 0.5f);
        builder.add(END_OFFSET_X, 0.5f);
        builder.add(END_OFFSET_Y, 0.5f);
        builder.add(END_OFFSET_Z, 0.5f);
        builder.add(TENSION_LEVEL, 6);
        builder.add(VARIANT, 0);
    }

    @Override
    public boolean damage(net.minecraft.entity.damage.DamageSource source, float amount) {
        if (this.getWorld().isClient) return true;
        if (source.getAttacker() instanceof net.minecraft.entity.player.PlayerEntity player) {
            if (!player.isCreative()) return false;
            this.dropItem(ModItems.ROPE);
            this.discard();
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos startPos = this.getStart();
        BlockPos endPos = this.getEnd();
        if (!startPos.equals(BlockPos.ORIGIN) && !endPos.equals(BlockPos.ORIGIN)) {
            if (this.age % 20 == 0) {
                Vec3d a = this.getStartWorldPoint();
                Vec3d b = this.getEndWorldPoint();
                Vec3d mid = a.add(b).multiply(0.5);
                this.setPosition(mid);
                this.setVelocity(Vec3d.ZERO);
                this.setBoundingBox(new Box(mid, mid).expand(0.25));
            }
        }
        if (!this.getWorld().isClient) {
            BlockPos start = this.getStart();
            BlockPos end = this.getEnd();
            if (this.age >= 10 || (!start.equals(BlockPos.ORIGIN) && !end.equals(BlockPos.ORIGIN))) {
                if (!start.equals(BlockPos.ORIGIN) && !end.equals(BlockPos.ORIGIN)) {
                    if (this.getWorld().isChunkLoaded(start) && this.getWorld().isChunkLoaded(end)) {
                        if (!this.getWorld().getBlockState(start).isAir() &&
                                !this.getWorld().getBlockState(end).isAir()) {
                        } else {
                            this.discard();
                        }
                    }
                } else {
                    this.discard();
                }
            }
        }
    }

    @Override
    protected Box calculateBoundingBox() {
        BlockPos start = this.getStart();
        BlockPos end = this.getEnd();

        if (!start.equals(BlockPos.ORIGIN) && !end.equals(BlockPos.ORIGIN)) {
            Vec3d a = this.getStartWorldPoint();
            Vec3d b = this.getEndWorldPoint();
            Vec3d mid = a.add(b).multiply(0.5);
            return new Box(mid, mid).expand(0.25);

        }
        return super.calculateBoundingBox();
    }

    public void setStart(BlockPos pos) {
        this.getDataTracker().set(START_POS, pos.asLong());
    }

    public BlockPos getStart() {
        return BlockPos.fromLong(this.getDataTracker().get(START_POS));
    }

    public void setEnd(BlockPos pos) {
        this.getDataTracker().set(END_POS, pos.asLong());
    }

    public BlockPos getEnd() {
        return BlockPos.fromLong(this.getDataTracker().get(END_POS));
    }

    public Vec3d getStartOffset() {
        var data = this.getDataTracker();
        return new Vec3d(
                data.get(START_OFFSET_X),
                data.get(START_OFFSET_Y),
                data.get(START_OFFSET_Z));
    }

    public void setStartOffset(Vec3d offset) {
        var data = this.getDataTracker();
        data.set(START_OFFSET_X, MathHelper.clamp((float) offset.x, 0.0f, 1.0f));
        data.set(START_OFFSET_Y, MathHelper.clamp((float) offset.y, 0.0f, 1.0f));
        data.set(START_OFFSET_Z, MathHelper.clamp((float) offset.z, 0.0f, 1.0f));
    }

    public Vec3d getEndOffset() {
        var data = this.getDataTracker();
        return new Vec3d(
                data.get(END_OFFSET_X),
                data.get(END_OFFSET_Y),
                data.get(END_OFFSET_Z));
    }

    public void setEndOffset(Vec3d offset) {
        var data = this.getDataTracker();
        data.set(END_OFFSET_X, MathHelper.clamp((float) offset.x, 0.0f, 1.0f));
        data.set(END_OFFSET_Y, MathHelper.clamp((float) offset.y, 0.0f, 1.0f));
        data.set(END_OFFSET_Z, MathHelper.clamp((float) offset.z, 0.0f, 1.0f));
    }

    public Vec3d getStartWorldPoint() {
        BlockPos pos = this.getStart();
        Vec3d offset = this.getStartOffset();
        return new Vec3d(pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z);
    }

    public Vec3d getEndWorldPoint() {
        BlockPos pos = this.getEnd();
        Vec3d offset = this.getEndOffset();
        return new Vec3d(pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z);
    }

    public int getTensionLevel() {
        return this.getDataTracker().get(TENSION_LEVEL);
    }

    public void setTensionLevel(int level) {
        this.getDataTracker().set(TENSION_LEVEL, MathHelper.clamp(level, 0, 6));
    }

    public void cycleTensionLevel() {
        this.setTensionLevel((this.getTensionLevel() + 1) % 7);
    }

    public String getTensionName() {
        return switch (this.getTensionLevel()) {
            case 0 -> "Very Saggy";
            case 1 -> "Somewhat Saggy";
            case 2 -> "Loose";
            case 3 -> "Medium";
            case 4 -> "Medium Loose";
            case 5 -> "Taut";
            default -> "Very Taut";
        };
    }

    public int getVariant() {
        return this.getDataTracker().get(VARIANT);
    }

    public void setVariant(int variant) {
        this.getDataTracker().set(VARIANT, variant);
    }

    public void cycleVariant() {
        this.setVariant((this.getVariant() + 1) % 2);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        if (this.getWorld().isClient) return ActionResult.SUCCESS;
        if (!player.isCreative()) return ActionResult.PASS;
        if (player.isSneaking()) {
            this.discard();
            return ActionResult.SUCCESS;
        }
        if (player.getStackInHand(hand).isEmpty()) {
            this.cycleVariant();
            player.sendMessage(
                    Text.literal("Rope Variant: " + this.getVariant()),
                    true);
            return ActionResult.SUCCESS;
        }
        if (player.getStackInHand(hand).isOf(ModItems.ROPE)) {
            this.cycleTensionLevel();
            player.sendMessage(
                    Text.literal("Rope Tension: " + this.getTensionName()),
                    true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putLong("Start", this.getDataTracker().get(START_POS));
        nbt.putLong("End", this.getDataTracker().get(END_POS));
        nbt.putFloat("SOx", this.getDataTracker().get(START_OFFSET_X));
        nbt.putFloat("SOy", this.getDataTracker().get(START_OFFSET_Y));
        nbt.putFloat("SOz", this.getDataTracker().get(START_OFFSET_Z));
        nbt.putFloat("EOx", this.getDataTracker().get(END_OFFSET_X));
        nbt.putFloat("EOy", this.getDataTracker().get(END_OFFSET_Y));
        nbt.putFloat("EOz", this.getDataTracker().get(END_OFFSET_Z));
        nbt.putInt("TensionLevel", this.getDataTracker().get(TENSION_LEVEL));
        nbt.putInt("Variant", this.getDataTracker().get(VARIANT));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Start")) this.getDataTracker().set(START_POS, nbt.getLong("Start"));
        if (nbt.contains("End")) this.getDataTracker().set(END_POS, nbt.getLong("End"));
        if (nbt.contains("SOx")) this.getDataTracker().set(START_OFFSET_X, nbt.getFloat("SOx"));
        if (nbt.contains("SOy")) this.getDataTracker().set(START_OFFSET_Y, nbt.getFloat("SOy"));
        if (nbt.contains("SOz")) this.getDataTracker().set(START_OFFSET_Z, nbt.getFloat("SOz"));
        if (nbt.contains("EOx")) this.getDataTracker().set(END_OFFSET_X, nbt.getFloat("EOx"));
        if (nbt.contains("EOy")) this.getDataTracker().set(END_OFFSET_Y, nbt.getFloat("EOy"));
        if (nbt.contains("EOz")) this.getDataTracker().set(END_OFFSET_Z, nbt.getFloat("EOz"));
        if (nbt.contains("TensionLevel")) this.getDataTracker().set(TENSION_LEVEL, nbt.getInt("TensionLevel"));
        if (nbt.contains("Variant")) this.getDataTracker().set(VARIANT, nbt.getInt("Variant"));
    }

    static {
        START_POS = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.LONG);
        END_POS = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.LONG);
        START_OFFSET_X = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.FLOAT);
        START_OFFSET_Y = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.FLOAT);
        START_OFFSET_Z = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.FLOAT);
        END_OFFSET_X = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.FLOAT);
        END_OFFSET_Y = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.FLOAT);
        END_OFFSET_Z = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.FLOAT);
        TENSION_LEVEL = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.INTEGER);
        VARIANT = DataTracker.registerData(RopeEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
