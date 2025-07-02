package com.westerosblocks.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class ChairEntity extends Entity {

    public ChairEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(Builder builder) {
        // No custom data to track for chair entity
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        // No custom data to read
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        // No custom data to write
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.kill();
    }
    
}
