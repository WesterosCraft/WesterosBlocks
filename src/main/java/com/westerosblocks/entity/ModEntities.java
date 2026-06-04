package com.westerosblocks.entity;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.entity.custom.ChairEntity;

import com.westerosblocks.entity.custom.RopeEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    // The chair seat surface sits at 18px (1.125 blocks). The entity spawns at the block's
    // bottom, so raise the passenger attachment to seat height so the player sits on the seat
    // instead of clipping through it.
    public static final EntityType<ChairEntity> CHAIR = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(WesterosBlocks.MOD_ID, "chair_entity"),
            EntityType.Builder.<ChairEntity>create(ChairEntity::new,SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .passengerAttachments(0.7f)
                    .build());

    public static final EntityType<RopeEntity> ROPE_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(WesterosBlocks.MOD_ID, "rope_entity"),
            EntityType.Builder.<RopeEntity>create(RopeEntity::new,SpawnGroup.MISC).dimensions(0.5f, 0.5f).build());

    public static void registerModEntities() {
        WesterosBlocks.LOGGER.info("Registering Mod Block Entities for " + WesterosBlocks.MOD_ID);
    }
}
