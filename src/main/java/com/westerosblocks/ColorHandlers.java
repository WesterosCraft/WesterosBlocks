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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

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
                    String colorMapResource = def.getBlockColorMapResource();
                    if (colorMapResource != null) {
                        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
                            if (view == null || pos == null) return -1;
                            return getColor(colorMapResource, view, pos);
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
                            if (view == null || pos == null) return -1;
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
                    String colorMapResource = def.getBlockColorMapResource();
                    if (colorMapResource != null) {
                        ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                                getDefaultColor(colorMapResource), block.asItem());
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
                        ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                                getDefaultColor(map.colorMult), block.asItem());
                    }
                }
            }
        }
    }

    private static int getColor(String colorMap, BlockRenderView view, BlockPos pos) {
        if (colorMap.startsWith("#")) {
            return Integer.parseInt(colorMap.substring(1), 16);
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
        if (colorMap.startsWith("#")) {
            return Integer.parseInt(colorMap.substring(1), 16);
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