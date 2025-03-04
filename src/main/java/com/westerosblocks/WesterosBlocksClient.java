package com.westerosblocks;

import com.westerosblocks.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class WesterosBlocksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new WesterosResourceReloadListener());
        ColorHandlers.registerColorProviders();
        ModParticles.initializeClient();
    }

    private static class WesterosResourceReloadListener implements SimpleSynchronousResourceReloadListener {
        @Override
        public Identifier getFabricId() {
            return Identifier.of(WesterosBlocks.MOD_ID, "resource_reload_listener");
        }

        @Override
        public void reload(ResourceManager manager) {
            ColorHandlers.initColorMaps(manager);
            ColorHandlers.colorMaps = WesterosBlocksDefLoader.getCustomConfig().colorMaps;
            WesterosBlocks.LOGGER.info("Handling resource reload completed");
        }
    }
}
