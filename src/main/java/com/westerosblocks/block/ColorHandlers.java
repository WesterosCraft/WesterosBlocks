package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.ColorResolver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ColorHandlers {
    public static abstract class ColorMultHandler implements BlockColorProvider, ItemColorProvider {
        ColorMultHandler() {
        }

        @Environment(EnvType.CLIENT)
        public int getColor(ItemStack stack, int tintIndex) {
            if (stack.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockState defaultState = block.getDefaultState();

                BlockColorProvider colorProvider = ColorProviderRegistry.BLOCK.get(block);

                if (colorProvider != null) {
                    return colorProvider.getColor(defaultState, null, null, tintIndex);
                }
            }

            // Fallback to default color
            return 0xFFFFFF;
        }

        @Environment(EnvType.CLIENT)
        public abstract int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex);

        public abstract int getColor(Biome biome, double x, double z);
    }

    public static class FixedColorMultHandler extends ColorMultHandler {
        protected int fixedMult;

        FixedColorMultHandler(int mult) {
            fixedMult = mult;
        }

        @Override
        @Environment(EnvType.CLIENT)
        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
            return fixedMult;
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return fixedMult;
        }
    }

    public static class FoliageColorMultHandler extends ColorMultHandler {
        FoliageColorMultHandler() {
        }

        @Override
        @Environment(EnvType.CLIENT)
        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
            if (world != null && pos != null)
                return BiomeColors.getFoliageColor(world, pos);
            else
                return 0x48B518; // Default green foliage color in Minecraft
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return biome.getFoliageColor();
        }
    }

    public static class GrassColorMultHandler extends ColorMultHandler {
        GrassColorMultHandler() {
        }

        @Override
        @Environment(EnvType.CLIENT)
        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
            if (world != null && pos != null)
                return BiomeColors.getGrassColor(world, pos);
            else
                return GrassColor.get(0.5D, 1.0D);
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return biome.getGrassColorAt(x, z);
        }
    }

    public static class WaterColorMultHandler extends ColorMultHandler {
        WaterColorMultHandler() {
        }

        @Override
        @Environment(EnvType.CLIENT)
        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
            if (world != null && pos != null)
                return BiomeColors.getWaterColor(world, pos);
            else
                return 0xFF000000;
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return biome.getWaterColor();
        }
    }

    public static class PineColorMultHandler extends ColorMultHandler {
        @Override
        @Environment(EnvType.CLIENT)
        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
            return -10380959;
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return -10380959;
        }
    }

    public static class BirchColorMultHandler extends ColorMultHandler {
        @Override
        @Environment(EnvType.CLIENT)
        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
            return -8345771;
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return -8345771;
        }
    }

    public static class BasicColorMultHandler extends ColorMultHandler {
        @Override
        @Environment(EnvType.CLIENT)
        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
            return -12012264;
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return -12012264;
        }
    }

    // TODO lots of fixes needed here
//    public static class CustomColorMultHandler extends ColorMultHandler implements ColorResolver {
//        private final List<int[]> colorBuffers;
//        private final List<String> rnames;
//        private boolean brokenCustomRenderer = false;
//
//        CustomColorMultHandler(String rname, String blockName) {
//            this(Collections.singletonList(rname), blockName);
//        }
//
//        CustomColorMultHandler(List<String> rnames, String blockName) {
//            super();
//            this.colorBuffers = new ArrayList<int[]>();
//            this.rnames = rnames;
//            for (String rname : rnames) {
//                colorBuffers.add(new int[65536]);
//            }
//        }
//
//        public int calculateColor(World world, BlockPos pos, int txtindx) {
//            int red = 0;
//            int green = 0;
//            int blue = 0;
//
//            for (int xx = -1; xx <= 1; ++xx) {
//                for (int zz = -1; zz <= 1; ++zz) {
//                    BlockPos bp = pos.offset(xx, 0, zz);
//                    Biome biome = world.getBiome(bp).value();
//                    int mult = getColor(biome.getTemperature(), biome.getModifiedClimateSettings().downfall(), txtindx);
//                    red += (mult & 0xFF0000) >> 16;
//                    green += (mult & 0x00FF00) >> 8;
//                    blue += (mult & 0x0000FF);
//                }
//            }
//            return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
//        }
//
//        @Override
//        @Environment(EnvType.CLIENT)
//        public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
//            if ((world != null) && (pos != null)) {
//                LevelReader rdr = null;
//
//                if (world instanceof RenderChunkRegion) {
//                    // TODO: idk if using Minecraft.getInstance is correct
////					rdr = ((RenderChunkRegion)world).level;
//                    rdr = Minecraft.getInstance().level;
//                } else if (world instanceof LevelReader) {
//                    rdr = (LevelReader) world;
//                }
//                if (rdr != null) {
//                    return calculateColor(rdr, pos, txtindx);
//                }
//                // Workaround to attempt to support custom renderers such as Optifine or Embeddium
//                else {
//                    if (!brokenCustomRenderer) {
//                        // First try to access non-thread-safe level reader from minecraft client instance...
//                        try {
//                            rdr = Minecraft.getInstance().level;
//                            return calculateColor(rdr, pos, txtindx);
//                        } catch (Exception x) {
//                            // If fails, try to use color resolver method...
//                            try {
//                                return world.getBlockTint(pos, this);
//                            } catch (Exception y) {
//                                // If both fail, biome colors will be broken, but should not explode.
//                                brokenCustomRenderer = true;
//                            }
//                        }
//                    }
//                }
//            }
//            return getColor(null, 0.5D, 1.0D, txtindx);
//        }
//
//        private int getColor(double tmp, double hum, int txtindx) {
//            tmp = Mth.clamp(tmp, 0.0F, 1.0F);
//            hum = Mth.clamp(hum, 0.0F, 1.0F);
//            hum *= tmp;
//            int i = (int) ((1.0D - tmp) * 255.0D);
//            int j = (int) ((1.0D - hum) * 255.0D);
//            return colorBuffers.get(txtindx)[j << 8 | i];
//        }
//
//        @Override
//        public int getColor(Biome biome, double x, double z) {
//            return getColor(biome, x, z, 0);
//        }
//
//        public int getColor(Biome biome, double x, double z, int txtindx) {
//            float hum = 1.0F;
//            float tmp = 0.5F;
//            if (biome != null) {
//                hum = biome.getModifiedClimateSettings().downfall();
//                tmp = biome.getBaseTemperature();
//            }
//            tmp = Mth.clamp(tmp, 0.0F, 1.0F);
//            hum = Mth.clamp(hum, 0.0F, 1.0F);
//            hum *= tmp;
//            int i = (int) ((1.0D - tmp) * 255.0D);
//            int j = (int) ((1.0D - hum) * 255.0D);
//            return colorBuffers.get(txtindx)[j << 8 | i];
//        }
//
//        @Environment(EnvType.CLIENT)
//        public void loadColorMaps(ResourceManager resMgr) {
//            int txtindx = 0;
//            for (String resName : rnames) {
//                if (resName.indexOf(':') < 0)
//                    resName = WesterosBlocks.MOD_ID + ":" + resName;
//                if (resName.endsWith(".png") == false)
//                    resName += ".png";
//                try {
//
//                    colorBuffers.set(txtindx, LegacyStuffWrapper.getPixels(resMgr, ResourceLocation.parse(resName)));
//                    WesterosBlocks.LOGGER.debug(String.format("Loaded color resource '%s'", resName));
//                } catch (Exception e) {
//                    WesterosBlocks.LOGGER.error(String.format("Invalid color resource '%s'", resName), e);
//                    Arrays.fill(colorBuffers.get(txtindx), 0xFFFFFF);
//                }
//                txtindx++;
//            }
//        }
//    }

    public static class GrassColor {
        private static int[] pixels = new int[65536];

        public GrassColor() {
        }

        public static void init(int[] grassBuffer) {
            pixels = grassBuffer;
        }

        public static int get(double temperature, double humidity) {
            humidity *= temperature;
            int i = (int)((1.0 - temperature) * 255.0);
            int j = (int)((1.0 - humidity) * 255.0);
            int k = j << 8 | i;
            return k >= pixels.length ? -65281 : pixels[k];
        }

        public static int getDefaultColor() {
            return get(0.5, 1.0);
        }
    }
}