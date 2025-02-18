package com.westerosblocks;

import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockColorMap;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.block.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Environment(EnvType.CLIENT)
public class ColorHandlers {
    private static final Map<String, int[]> CUSTOM_COLOR_MAPS = new HashMap<>();
    public static ModBlockColorMap[] colorMaps;

    public static void registerColorProviders() {
        registerBlockColors();
        registerItemColors();
    }

    private static void registerBlockColors() {
        // Register custom blocks
        for (Block block : ModBlocks.customBlocks) {
            if (block instanceof ModBlockLifecycle) {
                ModBlock def = ((ModBlockLifecycle) block).getWBDefinition();
                if (def != null) {
                    // Handle blocks with multiple color multipliers
                    if (def.colorMults != null && !def.colorMults.isEmpty()) {
                        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
                            if (tintIndex >= 0 && tintIndex < def.colorMults.size()) {
                                return getColor(def.colorMults.get(tintIndex), view, pos);
                            }
                            return -1;
                        }, block);
                    }
                    // Handle blocks with single color multiplier
                    else if (def.colorMult != null && !def.colorMult.equals("#FFFFFF")) {
                        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
                            if (tintIndex != 0) return -1;
                            return getColor(def.colorMult, view, pos);
                        }, block);
                    }
                }
            }
        }

        // Register vanilla blocks
        if (colorMaps != null) {
            for (ModBlockColorMap map : colorMaps) {
                for (String blockName : map.blockNames) {
                    Block block = ModBlocks.findBlockByName(blockName, "minecraft");
                    if (block != null) {
                        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
                            if (tintIndex != 0) return -1;
                            return getColor(map.colorMult, view, pos);
                        }, block);
                    }
                }
            }
        }
    }

    private static void registerItemColors() {
        // Register custom items
        for (Block block : ModBlocks.customBlocks) {
            if (block instanceof ModBlockLifecycle) {
                ModBlock def = ((ModBlockLifecycle) block).getWBDefinition();
                if (def != null) {
                    // Handle items with multiple color multipliers
                    if (def.colorMults != null && !def.colorMults.isEmpty()) {
                        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                            if (tintIndex >= 0 && tintIndex < def.colorMults.size()) {
                                return getDefaultColor(def.colorMults.get(tintIndex));
                            }
                            return -1;
                        }, block.asItem());
                    }
                    // Handle items with single color multiplier
                    else if (def.colorMult != null && !def.colorMult.equals("#FFFFFF")) {
                        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                            if (tintIndex != 0) return -1;
                            return getDefaultColor(def.colorMult);
                        }, block.asItem());
                    }
                }
            }
        }

        // Register vanilla items
        if (colorMaps != null) {
            for (ModBlockColorMap map : colorMaps) {
                for (String blockName : map.blockNames) {
                    Block block = ModBlocks.findBlockByName(blockName, "minecraft");
                    if (block != null) {
                        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                            if (tintIndex != 0) return -1;
                            return getDefaultColor(map.colorMult);
                        }, block.asItem());
                    }
                }
            }
        }
    }

    public static void initColorMaps(ResourceManager resourceManager) {
        CUSTOM_COLOR_MAPS.clear();

        for (Identifier id : resourceManager.findResources("textures/colormap", path -> path.getPath().endsWith(".png")).keySet()) {
            try {
                Resource resource = resourceManager.getResource(id).orElse(null);
                if (resource == null) continue;

                BufferedImage image = ImageIO.read(resource.getInputStream());
                if (image.getWidth() != 256 || image.getHeight() != 256) {
                    WesterosBlocks.LOGGER.error("Invalid colormap size for {}: {}x{} (expected 256x256)",
                            id, image.getWidth(), image.getHeight());
                    continue;
                }

                int[] colors = new int[256 * 256];
                image.getRGB(0, 0, 256, 256, colors, 0, 256);

                String key = id.getPath().substring("textures/colormap/".length());
                key = key.substring(0, key.length() - 4); // Remove .png
                CUSTOM_COLOR_MAPS.put(key, colors);

                WesterosBlocks.LOGGER.info("Loaded colormap: {}", key);
            } catch (IOException e) {
                WesterosBlocks.LOGGER.error("Failed to load colormap: {}", id, e);
            }
        }
    }

    private static int parseHexColor(String colorHex) {
        if (colorHex == null || !colorHex.startsWith("#")) return -1;
        try {
            String hex = colorHex.substring(1);
            if (hex.length() != 6) {
                WesterosBlocks.LOGGER.warn("Invalid hex color length: " + colorHex);
                return -1;
            }
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            WesterosBlocks.LOGGER.warn("Invalid hex color format: " + colorHex);
            return -1;
        }
    }

    private static int getColor(String colorMap, BlockRenderView view, BlockPos pos) {
        if (colorMap == null) return -1;
        if (colorMap.startsWith("#")) {
            return parseHexColor(colorMap);
        }

        return switch (getColorType(colorMap)) {
            case GRASS -> BiomeColors.getGrassColor(view, pos);
            case FOLIAGE -> BiomeColors.getFoliageColor(view, pos);
            case WATER -> BiomeColors.getWaterColor(view, pos);
            case PINE -> 0xff618961;
            case BIRCH -> 0xff80a755;
            case BASIC -> 0xff48b518;
            default -> loadCustomColor(colorMap, view, pos);
        };
    }

    private static int getDefaultColor(String colorMap) {
        if (colorMap == null) return -1;
        if (colorMap.startsWith("#")) {
            return parseHexColor(colorMap);
        }

        return switch (getColorType(colorMap)) {
            case GRASS -> GrassColors.getColor(0.5, 1.0);
            case FOLIAGE -> FoliageColors.getDefaultColor();
            case WATER -> 0x3F76E4;
            case PINE -> 0xff618961;
            case BIRCH -> 0xff80a755;
            case BASIC -> 0xff48b518;
            default -> loadCustomDefaultColor(colorMap);
        };
    }

    private static ColorType getColorType(String colorMap) {
        if (colorMap.contains("grass")) return ColorType.GRASS;
        if (colorMap.contains("foliage")) return ColorType.FOLIAGE;
        if (colorMap.contains("water")) return ColorType.WATER;
        if (colorMap.contains("pine")) return ColorType.PINE;
        if (colorMap.contains("birch")) return ColorType.BIRCH;
        if (colorMap.contains("basic")) return ColorType.BASIC;
        return ColorType.CUSTOM;
    }

    private enum ColorType {
        GRASS,
        FOLIAGE,
        WATER,
        PINE,
        BIRCH,
        BASIC,
        CUSTOM
    }

    private static String extractResourcePath(String colorMap) {
        if (colorMap.startsWith("textures/colormap/")) {
            return colorMap.substring("textures/colormap/".length());
        }
        return colorMap;
    }

    private static int loadCustomColor(String colorMap, BlockRenderView view, BlockPos pos) {
        if (view == null || pos == null) return -1;

        String resourcePath = extractResourcePath(colorMap);
        int[] colors = CUSTOM_COLOR_MAPS.get(resourcePath);
        if (colors == null) return -1;

        // Use a ColorResolver to get temperature and downfall from biome
        return view.getColor(pos, (biome, x, z) -> {
            float temperature = MathHelper.clamp(biome.getTemperature(), 0.0f, 1.0f);
            // uses temperature and downfall internally
            int defaultColor = biome.getGrassColorAt(x, z);
            float downfall = ((defaultColor >> 8) & 0xFF) / 255.0f;

            // Convert to color map coordinates
            int i = (int) ((1.0 - temperature) * 255.0);
            int j = (int) ((1.0 - downfall) * 255.0);

            // Ensure we stay within bounds
            i = MathHelper.clamp(i, 0, 255);
            j = MathHelper.clamp(j, 0, 255);

            return colors[j * 256 + i];
        });
    }

    private static int loadCustomDefaultColor(String colorMap) {
        String resourcePath = extractResourcePath(colorMap);
        int[] colors = CUSTOM_COLOR_MAPS.get(resourcePath);
        if (colors == null) return -1;

        // For default color, use middle temperature and downfall values
        float temperature = 0.5f;
        float downfall = 0.5f;

        int i = (int) ((1.0 - temperature) * 255.0);
        int j = (int) ((1.0 - downfall) * 255.0);

        return colors[j * 256 + i];
    }
}