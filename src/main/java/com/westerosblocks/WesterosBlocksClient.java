package com.westerosblocks;

import com.westerosblocks.block.ColorHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class WesterosBlocksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new WesterosResourceReloadListener());
        initRenderRegistry();
    }

    private static class WesterosResourceReloadListener implements SimpleSynchronousResourceReloadListener {
        @Override
        public Identifier getFabricId() {
            return Identifier.of("westerosblocks", "resource_reload_listener");
        }

        @Override
        public void reload(ResourceManager manager) {
            ColorHandlers.reloadColorHandler(manager);
            WesterosBlocks.LOGGER.info("Handling resource reload completed");
        }
    }

    private static void initRenderRegistry() {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        ColorHandlers.reloadColorHandler(resourceManager);
    }
}
