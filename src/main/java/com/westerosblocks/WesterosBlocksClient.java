package com.westerosblocks;

import com.westerosblocks.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class WesterosBlocksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new WesterosResourceReloadListener());
        initRenderRegistry();
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
            // TODO see if this works the same way
//            ColorHandlers.reloadColorHandler(manager);
            WesterosBlocks.LOGGER.info("Handling resource reload completed");
        }
    }

    private static void initRenderRegistry() {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
//        ColorHandlers.reloadColorHandler(resourceManager);
    }
}
