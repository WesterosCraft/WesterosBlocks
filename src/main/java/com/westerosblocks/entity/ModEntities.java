package com.westerosblocks.entity;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.entity.custom.ChairEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {


    public static final EntityType<ChairEntity> CHAIR = Registry.register(Registries.ENTITY_TYPE, 
    Identifier.of(WesterosBlocks.MOD_ID, "chair_entity"), 
    EntityType.Builder.<ChairEntity>create(ChairEntity::new,SpawnGroup.MISC).dimensions(0.5f, 0.5f).build());

    public static void registerModEntities() {
        WesterosBlocks.LOGGER.info("Registering Mod Entities for " + WesterosBlocks.MOD_ID);
    }
        
}
