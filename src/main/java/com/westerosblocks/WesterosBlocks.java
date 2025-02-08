package com.westerosblocks;

import com.westerosblocks.block.*;
import com.westerosblocks.block.entity.ModBlockEntities;
import com.westerosblocks.config.ModConfig;
import com.westerosblocks.item.ModItems;
import com.westerosblocks.particle.ModParticles;
import com.westerosblocks.sound.ModSounds;
import com.westerosblocks.util.AutoDoorRestore;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WesterosBlocks implements ModInitializer {
    public static final String MOD_ID = "westerosblocks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        WesterosBlocksDefLoader.initialize();
        ModBlock.initialize();
        ModParticles.initialize();
        ModConfig.register();
        WesterosCreativeModeTabs.registerCreativeModeTabs();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModSounds.registerSounds();
        ModBlockEntities.registerModEntities();

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            // handles any pending door restores (force immediate)
            AutoDoorRestore.handlePendingHalfDoorRestores(true);
        });
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}