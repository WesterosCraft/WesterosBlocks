package com.westeroscraft.westerosblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.westeroscraft.westerosblocks.blocks.WCBeaconBlock;
import com.westeroscraft.westerosblocks.blocks.WCBedBlock;
import com.westeroscraft.westerosblocks.blocks.WCCakeBlock;
import com.westeroscraft.westerosblocks.blocks.WCCropBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboid16WayBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNEBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWStackBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWUDBlock;
import com.westeroscraft.westerosblocks.blocks.WCDoorBlock;
import com.westeroscraft.westerosblocks.blocks.WCFenceBlock;
import com.westeroscraft.westerosblocks.blocks.WCFireBlock;
import com.westeroscraft.westerosblocks.blocks.WCFlowerPotBlock;
import com.westeroscraft.westerosblocks.blocks.WCFurnaceBlock;
import com.westeroscraft.westerosblocks.blocks.WCHalfDoorBlock;
import com.westeroscraft.westerosblocks.blocks.WCLadderBlock;
import com.westeroscraft.westerosblocks.blocks.WCLayerBlock;
import com.westeroscraft.westerosblocks.blocks.WCLeavesBlock;
import com.westeroscraft.westerosblocks.blocks.WCLogBlock;
import com.westeroscraft.westerosblocks.blocks.WCPaneBlock;
import com.westeroscraft.westerosblocks.blocks.WCPlantBlock;
import com.westeroscraft.westerosblocks.blocks.WCRailBlock;
import com.westeroscraft.westerosblocks.blocks.WCSandBlock;
import com.westeroscraft.westerosblocks.blocks.WCSlabBlock;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;
import com.westeroscraft.westerosblocks.blocks.WCSoulSandBlock;
import com.westeroscraft.westerosblocks.blocks.WCSoundBlock;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;
import com.westeroscraft.westerosblocks.blocks.WCTorchBlock;
import com.westeroscraft.westerosblocks.blocks.WCTrapDoorBlock;
import com.westeroscraft.westerosblocks.blocks.WCVinesBlock;
import com.westeroscraft.westerosblocks.blocks.WCWallBlock;
import com.westeroscraft.westerosblocks.blocks.WCWebBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.resources.LegacyStuffWrapper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.ColorResolver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.core.Registry;

//
// Template for block configuration data (populated using GSON)
//
public class WesterosBlockDef {
	private static final float DEF_FLOAT = -999.0F;
	public static final int DEF_INT = -999;

	public String blockName; // Locally unique block name
	public String blockType = "solid"; // Block type ('solid', 'liquid', 'plant', 'log', 'stairs', etc)
	public String modelBlockName; // Model block name (stairs use this)
	public float hardness = DEF_FLOAT; // Block hardness
	public String stepSound = null; // Step sound (powder, wood, gravel, grass, stone, metal, glass, cloth, sand,
									// snow, ladder, anvil)
	public String material = null; // Generic material (ai, grass, ground, wood, rock, iron, anvil, water, lava,
									// leaves, plants, vine, sponge, etc)
	public float resistance = DEF_FLOAT; // Explosion resistance
	public int lightOpacity = DEF_INT; // Light opacity
	public List<HarvestLevel> harvestLevel = null; // List of harvest levels
	public int fireSpreadSpeed = 0; // Fire spread speed
	public int flamability = 0; // Flamability
	public String creativeTab = null; // Creative tab for items
	public float lightValue = 0.0F; // Emitted light level (0.0-1.0)
	public BoundingBox boundingBox = null; // Bounding box
	public String colorMult = "#FFFFFF"; // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')
	public String type = ""; // Type field (used for plant types or other block type specific values)
	public boolean alphaRender = false; // If true, do render on pass 2 (for alpha blending)
	public boolean nonOpaque = false; // If true, does not block visibility of shared faces (solid blocks) and doesn't
										// allow torches
										// ('solid', 'sound', 'sand', 'soulsand' blocks)
	public String label; // Label for item associated with block
	public List<String> textures = null; // List of textures (for single texture set)
	public List<RandomTextureSet> randomTextures = null;	// On supported blocks (solid, leaves, slabs, stairs), 
										// defines sets of textures used for additional random models
										// If randomTextures is used, textures is ignored
	public String itemTexture = null; // Item texture, if any
	public int itemTextureIndex = 0; // Index of texture for item icon
	public List<Cuboid> cuboids = null; // List of cuboids composing block (for 'cuboid', and others)
	public List<BoundingBox> collisionBoxes = null; // For 'solid', used for raytrace (arrow shots)
	public List<String> soundList = null; // List of custom sound names or sound IDs (for 'sound' blocks)
	public Boolean ambientOcclusion = null; // Set ambient occlusion (default is true)
	public Boolean isCustomModel = null; // If set and true, don't generate new custom model (hand crafted)
	public List<StackElement> stack = null; // List of elements for a stack, first is bottom-most (for *-stack)
	public boolean rotateRandom = false;	// Set random rotation for supporting blocks (solid, leaves)
	public List<StateRecord> states = null;
	
	// List of states (corresponds to blocks state beyond those of base cuboid block, via state=<index in list>
	public static class StateRecord {
		public String stateID = null;	// If not defined, value is "stateN"
		public List<Cuboid> cuboids = null; // List of cuboids composing block (for 'cuboid', and others)
		public List<BoundingBox> collisionBoxes = null; // For 'solid', used for raytrace (arrow shots)
		public Boolean ambientOcclusion = null; // Set ambient occlusion (default is true)
		public Boolean isCustomModel = null; // If set and true, don't generate new custom model (hand crafted)
	};
	
	public String connectBy = "block";	// Connection logic - by block, material - only for CTM-like blocks
	
    public String legacyBlockID = null;
	       
	public boolean isConnectMatch(BlockState bs1, BlockState bs2) {
		if (this.connectBy.equals("material")) {
			return bs1.getMaterial() == bs2.getMaterial();
		}
		else {
			return bs1.getBlock() == bs2.getBlock();			
		}
	}
	
	public static class RandomTextureSet {
		public List<String> textures = null; // List of textures (for single texture set)
		public Integer weight = null;		// Weight for texture set (default = 1)
		// Get number of base textures
		public int getTextureCount() {
			if (textures != null) {
				return textures.size();
			}
			return 0;
		}

		public String getTextureByIndex(int idx) {
			int cnt = getTextureCount();
			if (cnt > 0) {
				if (idx >= cnt) {
					idx = cnt - 1;
				}
				return textures.get(idx);
			}
			return null;
		}

	};
	
	public static class StackElement {
		public List<String> textures = null; // List of textures
		public BoundingBox boundingBox = null; // Bounding box
		public List<Cuboid> cuboids = null; // List of cuboids composing block (for 'cuboid', and others)
		public List<BoundingBox> collisionBoxes = null; // For 'solid', used for raytrace (arrow shots)
		public List<RandomTextureSet> randomTextures = null;	// On supported blocks (solid, leaves, slabs, stairs), 
		// defines sets of textures used for additional random models
		// If randomTextures is used, textures is ignored

		public String getTextureByIndex(int idx) {
			if ((textures != null) && (textures.size() > 0)) {
				if (idx >= textures.size()) {
					idx = textures.size() - 1;
				}
				return textures.get(idx);
			}
			return null;
		}
		// Get number of random texture sets
		public int getRandomTextureSetCount() {
			if ((randomTextures != null) && (randomTextures.size() > 0)) {
				return randomTextures.size();
			}
			return 0;
		}
		
		// Get given random texture set
		public RandomTextureSet getRandomTextureSet(int setnum) {
			if ((randomTextures != null) && (randomTextures.size() > 0)) {
				if (setnum >= randomTextures.size()) {
					setnum = randomTextures.size() - 1;
				}
				return randomTextures.get(setnum);
			}
			return null;
		}

	};

	public static class HarvestLevel {
		public String tool;
		public int level;
	}

	public static class BoundingBox {
		public float xMin = 0.0F;
		public float xMax = 1.0F;
		public float yMin = 0.0F;
		public float yMax = 1.0F;
		public float zMin = 0.0F;
		public float zMax = 1.0F;

		private transient VoxelShape aabb = null;

		public VoxelShape getAABB() {
			if (aabb == null) {
				aabb = Shapes.box(xMin, yMin, zMin, xMax, yMax, zMax);
			}
			return aabb;
		}
	}

	public static class Vector {
		float x, y, z;

		private void rotate(int xcnt, int ycnt, int zcnt) {
			double xx, yy, zz;
			xx = x - 0.5F;
			yy = y - 0.5F;
			zz = z - 0.5F; // Shoft to center of block
			/* Do X rotation */
			double rot = Math.toRadians(xcnt);
			double nval = zz * Math.sin(rot) + yy * Math.cos(rot);
			zz = zz * Math.cos(rot) - yy * Math.sin(rot);
			yy = nval;
			/* Do Y rotation */
			rot = Math.toRadians(ycnt);
			nval = xx * Math.cos(rot) - zz * Math.sin(rot);
			zz = xx * Math.sin(rot) + zz * Math.cos(rot);
			xx = nval;
			/* Do Z rotation */
			rot = Math.toRadians(zcnt);
			nval = yy * Math.sin(rot) + xx * Math.cos(rot);
			yy = yy * Math.cos(rot) - xx * Math.sin(rot);
			xx = nval;
			x = (float) xx + 0.5F;
			y = (float) yy + 0.5F;
			z = (float) zz + 0.5F; // Shoft back to corner
			// Clip value
			if (x > 1.0F)
				x = 1.0F;
			if (y > 1.0F)
				y = 1.0F;
			if (z > 1.0F)
				z = 1.0F;
			if (x < 0.0F)
				x = 0.0F;
			if (y < 0.0F)
				y = 0.0F;
			if (z < 0.0F)
				z = 0.0F;
		}

	}

	public static enum CuboidRotation {
		NONE(0, 0, 0, new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 0, 0, 0, 0, 0, 0 }),
		ROTY90(0, 90, 0, new int[] { 0, 1, 4, 5, 3, 2 }, new int[] { 270, 90, 0, 0, 0, 0 }),
		ROTY180(0, 180, 0, new int[] { 0, 1, 3, 2, 5, 4 }, new int[] { 180, 180, 0, 0, 0, 0 }),
		ROTY270(0, 270, 0, new int[] { 0, 1, 5, 4, 2, 3 }, new int[] { 90, 270, 0, 0, 0, 0 }),
		ROTZ90(0, 0, 90, new int[] { 5, 4, 2, 3, 0, 1 }, new int[] { 270, 90, 270, 90, 90, 90 }),
		ROTZ270(0, 0, 270, new int[] { 4, 5, 2, 3, 1, 0 }, new int[] { 90, 270, 90, 270, 270, 270 });

		final int xrot, yrot, zrot;
		final int txtidx[];
		final int txtrot[];

		CuboidRotation(int xr, int yr, int zr, int[] txt_idx, int[] txt_rot) {
			xrot = xr;
			yrot = yr;
			zrot = zr;
			txtidx = txt_idx;
			txtrot = txt_rot;
		}
		public int getRotY() { return yrot; }
	}

	// Shape for normal cuboid (box)
	public static final String SHAPE_BOX = "box";
	// Shape for crossed squares (plant-style) (texture is index 0 in list)
	public static final String SHAPE_CROSSED = "crossed";

	public static class Cuboid extends BoundingBox {
		public int[] sideTextures = null;
		public int[] sideRotations = { 0, 0, 0, 0, 0, 0 };
		public String shape = SHAPE_BOX; // "box" = normal cuboid, "crossed" = plant-style crossed (texture 0)
		public boolean[] noTint;

		public Cuboid rotateCuboid(CuboidRotation rot) {
			Cuboid c = new Cuboid();
			Vector v0 = new Vector();
			Vector v1 = new Vector();
			v0.x = xMin;
			v0.y = yMin;
			v0.z = zMin;
			v1.x = xMax;
			v1.y = yMax;
			v1.z = zMax;
			// Rotate corners
			v0.rotate(rot.xrot, rot.yrot, rot.zrot);
			v1.rotate(rot.xrot, rot.yrot, rot.zrot);
			// Compute net min/max
			c.xMin = Math.min(v0.x, v1.x);
			c.xMax = Math.max(v0.x, v1.x);
			c.yMin = Math.min(v0.y, v1.y);
			c.yMax = Math.max(v0.y, v1.y);
			c.zMin = Math.min(v0.z, v1.z);
			c.zMax = Math.max(v0.z, v1.z);
			if (this.sideTextures != null) {
				c.sideTextures = new int[rot.txtidx.length];
				int cnt = this.sideTextures.length;
				for (int i = 0; i < c.sideTextures.length; i++) {
					int newidx = rot.txtidx[i];
					if (newidx < cnt) {
						c.sideTextures[i] = this.sideTextures[newidx];
					} else {
						c.sideTextures[i] = this.sideTextures[cnt - 1];
					}
				}
			} else {
				c.sideTextures = rot.txtidx;
			}
			c.sideRotations = rot.txtrot;
			c.shape = this.shape;
			return c;
		}

		public Cuboid() {
		}

		public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1) {
			this(x0, y0, z0, x1, y1, z1, null, null);
		}
		public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1, int[] sidetextures) {
			this(x0, y0, z0, x1, y1, z1, sidetextures, null);
		}

		public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1, int[] sidetextures, boolean noTint[]) {
			this.xMin = x0;
			this.xMax = x1;
			this.yMin = y0;
			this.yMax = y1;
			this.zMin = z0;
			this.zMax = z1;
			this.sideTextures = sidetextures;
			this.noTint = noTint;
		}
	}

	public List<Cuboid> getCuboidList() {
		return cuboids;
	}

	public List<BoundingBox> getCollisionBoxList() {
		if (this.collisionBoxes != null)
			return this.collisionBoxes;
		return Collections.emptyList();
	}

	public static class Particle {
		public float x = 0.5F, y = 0.5F, z = 0.5F; // Default position of effect
		public float vx = 0.0F, vy = 0.0F, vz = 0.0F; // Default velocity of effect
		public float xrand = 0.0F, yrand = 0.0F, zrand = 0.0F; // Default random position of effect (-rand to +rand)
		public float vxrand = 0.0F, vyrand = 0.0F, vzrand = 0.0F; // Default random velocity of effect (-rand to +rand)
		public float chance = 1.0F;
		public String particle;
	}

	// Base color multiplier (fixed)
	public static abstract class ColorMultHandler {
		ColorMultHandler() {
		}

		@OnlyIn(Dist.CLIENT)
		public int getItemColor(ItemStack stack, int tintIndex) {
			BlockColors blockColors = Minecraft.getInstance().getBlockColors();
			BlockState BlockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
			return blockColors.getColor(BlockState, null, null, tintIndex);
		}

		@OnlyIn(Dist.CLIENT)
		public abstract int getColor(BlockState arg0, BlockAndTintGetter arg1, BlockPos arg2, int arg3);

	}

	// Fixed color multiplier (fixed)
	public static class FixedColorMultHandler extends ColorMultHandler {
		protected int fixedMult;

		FixedColorMultHandler(int mult) {
			fixedMult = mult;
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState arg0, BlockAndTintGetter arg1, BlockPos arg2, int arg3) {
			return fixedMult;
		}

	}

	// Foliage color multiplier
	public static class FoliageColorMultHandler extends ColorMultHandler {
		FoliageColorMultHandler() {
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
			if (world != null && pos != null)
				return BiomeColors.getAverageFoliageColor(world, pos);
			else
				return FoliageColor.getDefaultColor();
		}
	}

	// Grass color multiplier
	public static class GrassColorMultHandler extends ColorMultHandler {
		GrassColorMultHandler() {
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
			if (world != null && pos != null)
				return BiomeColors.getAverageGrassColor(world, pos);
			else
				return GrassColor.get(0.5D, 1.0D);
		}
	}

	// Water color multiplier
	public static class WaterColorMultHandler extends ColorMultHandler {
		WaterColorMultHandler() {
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
			return BiomeColors.getAverageWaterColor(world, pos) | 0xFF000000;
		}
	}

	public static class PineColorMultHandler extends ColorMultHandler {
		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
			return FoliageColor.getEvergreenColor();
		}
	}

	public static class BirchColorMultHandler extends ColorMultHandler {
		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
			return FoliageColor.getBirchColor();
		}
	}

	public static class BasicColorMultHandler extends ColorMultHandler {
		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
			return FoliageColor.getDefaultColor();
		}
	}

	public boolean isTinted() {
		return ((colorMult != null) && (colorMult.equals("#FFFFFF") == false));
	}

	// Get number of base textures
	public int getTextureCount() {
		RandomTextureSet set = getRandomTextureSet(0);
		if (set != null) {
			return set.getTextureCount();
		}
		return 0;
	}
 
	public String getTextureByIndex(int idx) {
		RandomTextureSet set = getRandomTextureSet(0);
		if (set != null) {
			return set.getTextureByIndex(idx);
		}
		return null;
	}
	
	// Get number of random texture sets
	public int getRandomTextureSetCount() {
		if ((randomTextures != null) && (randomTextures.size() > 0)) {
			return randomTextures.size();
		}
		return 0;
	}
	
	// Get given random texture set
	public RandomTextureSet getRandomTextureSet(int setnum) {
		if ((randomTextures != null) && (randomTextures.size() > 0)) {
			if (setnum >= randomTextures.size()) {
				setnum = randomTextures.size() - 1;
			}
			return randomTextures.get(setnum);
		}
		return null;
	}

	public StackElement getStackElementByIndex(int idx) {
		if ((stack != null) && (stack.size() > 0)) {
			if (idx >= stack.size()) {
				idx = stack.size() - 1;
			}
			return stack.get(idx);
		}
		return null;
	}

	// Custom color multiplier
	public static class CustomColorMultHandler extends ColorMultHandler implements ColorResolver {
		private int[] colorBuffer = new int[65536];

		CustomColorMultHandler(String rname, String blockName) {
			super();

			loadColorMap(rname, blockName);
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int txtindx) {
			if ((world != null) && (pos != null) && (world instanceof LevelReader)) {
				LevelReader rdr = (LevelReader) world;
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int xx = -1; xx <= 1; ++xx) {
					for (int zz = -1; zz <= 1; ++zz) {
						BlockPos bp = pos.offset(xx, 0, zz);
						Biome biome = rdr.getBiome(bp).value();
						int mult = getColor(biome.getTemperature(bp), biome.getDownfall());
						red += (mult & 0xFF0000) >> 16;
						green += (mult & 0x00FF00) >> 8;
						blue += (mult & 0x0000FF);
					}
				}
				return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
			} else {
				return getColor(null, 0.5D, 1.0D);
			}
		}

		private int getColor(float tmp, float hum) {
			tmp = Mth.clamp(tmp, 0.0F, 1.0F);
			hum = Mth.clamp(hum, 0.0F, 1.0F);
			hum *= tmp;
			int i = (int) ((1.0D - tmp) * 255.0D);
			int j = (int) ((1.0D - hum) * 255.0D);
			return colorBuffer[j << 8 | i];
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public int getColor(Biome biome, double tmp, double hum) {
			tmp = Mth.clamp(tmp, 0.0F, 1.0F);
			hum = Mth.clamp(hum, 0.0F, 1.0F);
			hum *= tmp;
			int i = (int) ((1.0D - tmp) * 255.0D);
			int j = (int) ((1.0D - hum) * 255.0D);
			return colorBuffer[j << 8 | i];
		}

		@OnlyIn(Dist.CLIENT)
		private void loadColorMap(String rname, String blkname) {
			if (rname.indexOf(':') < 0)
				rname = WesterosBlocks.MOD_ID + ":" + rname;
			if (rname.endsWith(".png") == false)
				rname += ".png";
			try {
				colorBuffer = LegacyStuffWrapper.getPixels(Minecraft.getInstance().getResourceManager(),
						new ResourceLocation(rname));
			} catch (Exception e) {
				WesterosBlocks.log.error(String.format("Invalid color resource '%s' in block '%s'", rname, blkname));
				Arrays.fill(colorBuffer, 0xFFFFFF);
			}
		}
	}

	public String getType() {
		return this.type;
	}
	private transient Map<String, String> parsedType;
	public Map<String, String> getMappedType() {
		if (parsedType == null) {
			parsedType = new HashMap<String, String>();
			String[] toks = type.split(",");
			for (String tok : toks) {
				String[] flds = tok.split(":");
				if (flds.length < 2)
					continue;
				parsedType.put(flds[0], flds[1]);
			}
		}
		return parsedType;
	}
	public String getTypeValue(String key, String defval) {
		String v = getMappedType().get(key);
		if (v == null) v = defval;
		return v;
	}
	public String getTypeValue(String key) {
		return getTypeValue(key, "");
	}

	private transient boolean hasCollisionBoxes = false;

	private static final Map<String, Material> materialTable = new HashMap<String, Material>();
	private static final Map<String, SoundType> stepSoundTable = new HashMap<String, SoundType>();
	private static final Map<String, CreativeModeTab> tabTable = new HashMap<String, CreativeModeTab>();
	private static final Map<String, WesterosBlockFactory> typeTable = new HashMap<String, WesterosBlockFactory>();
	private static final Map<String, ColorMultHandler> colorMultTable = new HashMap<String, ColorMultHandler>();
	private static final Map<String, ParticleType<?>> particles = new HashMap<String, ParticleType<?>>();

	private transient boolean didInit = false;

	public void doInit() {
		if (didInit)
			return;
		// If just base textures, generate equivalent random textures (simpler logic for blocks that support them
		if ((textures != null) && (randomTextures == null)) {
			randomTextures = new ArrayList<RandomTextureSet>();
			RandomTextureSet set = new RandomTextureSet();
			set.textures = textures;
			randomTextures.add(set);
		}
		if (this.ambientOcclusion == null)
			this.ambientOcclusion = true; // Default to true
		// If we have bounding box, but no cuboids, make trivial cuboid
		if ((this.boundingBox != null) && (this.cuboids == null)) {
			Cuboid c = new Cuboid();
			c.xMin = this.boundingBox.xMin;
			c.xMax = this.boundingBox.xMax;
			c.yMin = this.boundingBox.yMin;
			c.yMax = this.boundingBox.yMax;
			c.zMin = this.boundingBox.zMin;
			c.zMax = this.boundingBox.zMax;
			this.cuboids = Collections.singletonList(c);
		}
		// If cuboids but no bounding box, compute bounding box
		if ((this.cuboids != null) && (this.boundingBox == null)) {
			this.boundingBox = new BoundingBox();
			this.boundingBox.xMin = this.boundingBox.yMin = this.boundingBox.zMin = 1.0F;
			this.boundingBox.xMax = this.boundingBox.yMax = this.boundingBox.zMax = 0.0F;
			for (BoundingBox bb : this.cuboids) {
				if (bb.xMin < this.boundingBox.xMin)
					this.boundingBox.xMin = bb.xMin;
				if (bb.yMin < this.boundingBox.yMin)
					this.boundingBox.yMin = bb.yMin;
				if (bb.zMin < this.boundingBox.zMin)
					this.boundingBox.zMin = bb.zMin;
				if (bb.xMax > this.boundingBox.xMax)
					this.boundingBox.xMax = bb.xMax;
				if (bb.yMax > this.boundingBox.yMax)
					this.boundingBox.yMax = bb.yMax;
				if (bb.zMax > this.boundingBox.zMax)
					this.boundingBox.zMax = bb.zMax;
			}
		}
		// If stacks, process these too
		if (this.stack != null) {
			for (StackElement se : this.stack) {
				// If just base texrures, generate equivalent random textures (simpler logic for blocks that support them
				if ((se.textures != null) && (se.randomTextures == null)) {
					se.randomTextures = new ArrayList<RandomTextureSet>();
					RandomTextureSet set = new RandomTextureSet();
					set.textures = se.textures;
					se.randomTextures.add(set);
				}
				// If we have bounding box, but no cuboids, make trivial cuboid
				if ((se.boundingBox != null) && (se.cuboids == null)) {
					Cuboid c = new Cuboid();
					c.xMin = se.boundingBox.xMin;
					c.xMax = se.boundingBox.xMax;
					c.yMin = se.boundingBox.yMin;
					c.yMax = se.boundingBox.yMax;
					c.zMin = se.boundingBox.zMin;
					c.zMax = se.boundingBox.zMax;
					se.cuboids = Collections.singletonList(c);
				}
				// If cuboids but no bounding box, compute bounding box
				if ((se.cuboids != null) && (se.boundingBox == null)) {
					se.boundingBox = new BoundingBox();
					se.boundingBox.xMin = se.boundingBox.yMin = se.boundingBox.zMin = 1.0F;
					se.boundingBox.xMax = se.boundingBox.yMax = se.boundingBox.zMax = 0.0F;
					for (BoundingBox bb : se.cuboids) {
						if (bb.xMin < se.boundingBox.xMin)
							se.boundingBox.xMin = bb.xMin;
						if (bb.yMin < se.boundingBox.yMin)
							se.boundingBox.yMin = bb.yMin;
						if (bb.zMin < se.boundingBox.zMin)
							se.boundingBox.zMin = bb.zMin;
						if (bb.xMax > se.boundingBox.xMax)
							se.boundingBox.xMax = bb.xMax;
						if (bb.yMax > se.boundingBox.yMax)
							se.boundingBox.yMax = bb.yMax;
						if (bb.zMax > se.boundingBox.zMax)
							se.boundingBox.zMax = bb.zMax;
					}
				}
			}
		}
		didInit = true;
	}

	public Block createBlock() {
		try {
			doInit(); // Prime the block model
		} catch (Exception x) {
			WesterosBlocks.log.error("Exception during doInit: blockName=" + this.blockName);
			throw x;
		}
		WesterosBlockFactory bf = typeTable.get(blockType);
		if (bf == null) {
			WesterosBlocks.log.error(String.format("Invalid blockType '%s' in block '%s'", blockType, blockName));
			return null;
		}
		return bf.buildBlockClass(this);
	}

	public Block registerBlock(Block block) {
		BlockItem itemBlock = new BlockItem(block, new Item.Properties().tab(getCreativeTab()));
		block.setRegistryName(this.blockName);
		itemBlock.setRegistryName(this.blockName);
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(itemBlock);
		return block;
	}

	public void registerWallOrFloorBlock(Block floorblock, Block wallblock) {
		BlockItem itemBlock = new StandingAndWallBlockItem(floorblock, wallblock,
				(new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS));
		floorblock.setRegistryName(this.blockName);
		wallblock.setRegistryName("wall_" + this.blockName);
		itemBlock.setRegistryName(this.blockName);
		ForgeRegistries.BLOCKS.register(floorblock);
		ForgeRegistries.BLOCKS.register(wallblock);
		ForgeRegistries.ITEMS.register(itemBlock);
	}

	public Block registerRenderType(Block block, boolean isSolid, boolean isTransparent) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			if (this.alphaRender) {
				ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
			} else if (!isSolid) {
				ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
			} else if (isTransparent) {
				ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
			}
		}
		return block;
	}

	public BlockBehaviour.Properties makeProperties() {
		return makeAndCopyProperties(null);
	}
	
    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

	public BlockBehaviour.Properties makeAndCopyProperties(Block blk) {
		BlockBehaviour.Properties props;
		if (blk != null) {
			props = BlockBehaviour.Properties.copy(blk);
		} else {
			Material mat = getMaterial();
			props = BlockBehaviour.Properties.of(mat); // TODO - material color?
		}
		if (hardness >= 0.0F) {
			if (resistance >= 0.0)
				props = props.strength(hardness, resistance);
			else
				props = props.strength(hardness);
		}
		if (stepSound != null) {
			props = props.sound(getSoundType());
		}
		if (lightValue > 0.0F) {
			props = props.lightLevel((state) -> (int) (16.0 * lightValue));
		}
		if ((!ambientOcclusion) || (nonOpaque)) { // If no ambient occlusion
			props = props.noOcclusion();
			props = props.isViewBlocking(WesterosBlockDef::never);
		}
		return props;
	}

	public boolean isCustomModel() {
		return (isCustomModel != null) && (isCustomModel.booleanValue() == true);
	}

	/**
	 * Returns this WesterosBlockDef's default Material
	 * 
	 * @return this WesterosBlockDef's default Material
	 */
	public Material getMaterial() {
		Material m = materialTable.get(material);
		if (m == null) {
			WesterosBlocks.log.warn(String.format("Invalid material '%s' in block '%s'", material, blockName));
			return Material.STONE;
		}
		return m;
	}

	/**
	 * Returns this WesterosBlockDef's default SoundType
	 * 
	 * @return this WesterosBlockDef's default SoundType
	 */
	public SoundType getSoundType() {
		SoundType ss = stepSoundTable.get(stepSound);
		if (ss == null) {
			WesterosBlocks.log.warn(String.format("Invalid step sound '%s' in block '%s'", stepSound, blockName));
			return SoundType.STONE;
		}
		return ss;
	}

	public CreativeModeTab getCreativeTab() {
		CreativeModeTab ct = tabTable.get(creativeTab);
		if (ct == null) {
			WesterosBlocks.log.warn(String.format("Invalid tab name '%s' in block '%s'", creativeTab, blockName));
			ct = WesterosBlocksCreativeTab.tabWesterosMisc;
		}
		return ct;
	}

	public static CreativeModeTab getCreativeTab(String tabName) {
		return tabTable.get(tabName);
	}

	public boolean hasCollisionBoxes() {
		return hasCollisionBoxes;
	}

	public String getBlockName() {
		return this.blockName;
	}

	public static void addCreativeTab(String name, CreativeModeTab tab) {
		tabTable.put(name, tab);
	}

	public static boolean sanityCheck(WesterosBlockDef[] defs) {
		HashSet<String> names = new HashSet<String>();
		// Make sure block IDs and names are unique
		for (WesterosBlockDef def : defs) {
			if (def == null)
				continue;
			if (def.blockName == null) {
				WesterosBlocks.log.error("Block definition is missing blockName");
				return false;
			}
			if (names.add(def.blockName) == false) { // If alreay defined
				WesterosBlocks.log.error(String.format("Block '%s' - blockName duplicated", def.blockName));
				return false;
			}
		}
		WesterosBlocks.log.info("WesterosBlocks.json passed sanity check");
		return true;
	}

	public static void initialize() {
		materialTable.put("air", Material.AIR);
		materialTable.put("grass", Material.GRASS);
		materialTable.put("ground", Material.DIRT);
		materialTable.put("wood", Material.WOOD);
		materialTable.put("rock", Material.STONE);
		materialTable.put("iron", Material.METAL);
		materialTable.put("anvil", Material.METAL);
		materialTable.put("water", Material.WATER);
		materialTable.put("lava", Material.LAVA);
		materialTable.put("leaves", Material.LEAVES);
		materialTable.put("plants", Material.PLANT);
		materialTable.put("vine", Material.PLANT);
		materialTable.put("sponge", Material.SPONGE);
		materialTable.put("cloth", Material.CLOTH_DECORATION);
		materialTable.put("fire", Material.FIRE);
		materialTable.put("sand", Material.SAND);
		materialTable.put("glass", Material.GLASS);
		materialTable.put("tnt", Material.EXPLOSIVE);
		materialTable.put("coral", Material.STONE);
		materialTable.put("ice", Material.ICE);
		materialTable.put("snow", Material.SNOW);
		materialTable.put("craftedSnow", Material.SNOW);
		materialTable.put("cactus", Material.CACTUS);
		materialTable.put("clay", Material.CLAY);
		materialTable.put("portal", Material.PORTAL);
		materialTable.put("cake", Material.CAKE);
		materialTable.put("web", Material.WEB);
		materialTable.put("piston", Material.PISTON);
		materialTable.put("decoration", Material.DECORATION);

		stepSoundTable.put("powder", SoundType.SAND);
		stepSoundTable.put("wood", SoundType.WOOD);
		stepSoundTable.put("gravel", SoundType.GRAVEL);
		stepSoundTable.put("grass", SoundType.GRASS);
		stepSoundTable.put("stone", SoundType.STONE);
		stepSoundTable.put("metal", SoundType.METAL);
		stepSoundTable.put("glass", SoundType.GLASS);
		stepSoundTable.put("cloth", SoundType.WOOL);
		stepSoundTable.put("sand", SoundType.SAND);
		stepSoundTable.put("snow", SoundType.SNOW);
		stepSoundTable.put("ladder", SoundType.LADDER);
		stepSoundTable.put("anvil", SoundType.ANVIL);
		stepSoundTable.put("plant", SoundType.CROP);
		stepSoundTable.put("slime", SoundType.FUNGUS);
		// Tab table
		tabTable.put("buildingBlocks", CreativeModeTab.TAB_BUILDING_BLOCKS);
		tabTable.put("decorations", CreativeModeTab.TAB_DECORATIONS);
		tabTable.put("redstone", CreativeModeTab.TAB_REDSTONE);
		tabTable.put("transportation", CreativeModeTab.TAB_TRANSPORTATION);
		tabTable.put("misc", CreativeModeTab.TAB_MISC);
		tabTable.put("food", CreativeModeTab.TAB_FOOD);
		tabTable.put("tools", CreativeModeTab.TAB_TOOLS);
		tabTable.put("combat", CreativeModeTab.TAB_COMBAT);
		tabTable.put("brewing", CreativeModeTab.TAB_BREWING);
		tabTable.put("materials", CreativeModeTab.TAB_MATERIALS);

		// Standard block types
		typeTable.put("solid", new WCSolidBlock.Factory());
		typeTable.put("stair", new WCStairBlock.Factory());
		typeTable.put("log", new WCLogBlock.Factory());
		typeTable.put("plant", new WCPlantBlock.Factory());
		typeTable.put("crop", new WCCropBlock.Factory());
		typeTable.put("slab", new WCSlabBlock.Factory());
		typeTable.put("wall", new WCWallBlock.Factory());
		typeTable.put("fence", new WCFenceBlock.Factory());
		typeTable.put("web", new WCWebBlock.Factory());
		typeTable.put("torch", new WCTorchBlock.Factory());
		typeTable.put("ladder", new WCLadderBlock.Factory());
		typeTable.put("cuboid", new WCCuboidBlock.Factory());
		typeTable.put("cuboid-nsew", new WCCuboidNSEWBlock.Factory());
		typeTable.put("cuboid-16way", new WCCuboid16WayBlock.Factory());
		typeTable.put("cuboid-ne", new WCCuboidNEBlock.Factory());
		typeTable.put("cuboid-nsewud", new WCCuboidNSEWUDBlock.Factory());
		typeTable.put("cuboid-nsew-stack", new WCCuboidNSEWStackBlock.Factory());
		typeTable.put("door", new WCDoorBlock.Factory());
		typeTable.put("fire", new WCFireBlock.Factory());
		typeTable.put("leaves", new WCLeavesBlock.Factory());
		typeTable.put("pane", new WCPaneBlock.Factory());
		typeTable.put("layer", new WCLayerBlock.Factory());
		typeTable.put("soulsand", new WCSoulSandBlock.Factory());
		typeTable.put("rail", new WCRailBlock.Factory());
		typeTable.put("cake", new WCCakeBlock.Factory());
		typeTable.put("bed", new WCBedBlock.Factory());
		typeTable.put("sand", new WCSandBlock.Factory());
		typeTable.put("halfdoor", new WCHalfDoorBlock.Factory());
		typeTable.put("furnace", new WCFurnaceBlock.Factory());
		typeTable.put("sound", new WCSoundBlock.Factory());
		typeTable.put("trapdoor", new WCTrapDoorBlock.Factory());
		typeTable.put("beacon", new WCBeaconBlock.Factory());
		typeTable.put("vines", new WCVinesBlock.Factory());
		typeTable.put("flowerpot", new WCFlowerPotBlock.Factory());

		// Standard color multipliers
		colorMultTable.put("#FFFFFF", new FixedColorMultHandler(0xFFFFFF));
		colorMultTable.put("water", new WaterColorMultHandler());
		colorMultTable.put("foliage", new FoliageColorMultHandler());
		colorMultTable.put("grass", new GrassColorMultHandler());
		colorMultTable.put("pine", new PineColorMultHandler());
		colorMultTable.put("birch", new BirchColorMultHandler());
		colorMultTable.put("basic", new BasicColorMultHandler());
		colorMultTable.put("lily", new FixedColorMultHandler(2129968));

		// Valid particle values
		particles.put("hugeexplosion", ParticleTypes.EXPLOSION);
		particles.put("largeexplode", ParticleTypes.EXPLOSION);
		particles.put("fireworksSpark", ParticleTypes.FIREWORK);
		particles.put("bubble", ParticleTypes.BUBBLE);
		particles.put("suspended", ParticleTypes.UNDERWATER);
		particles.put("depthsuspend", ParticleTypes.UNDERWATER);
		//particles.put("townaura", ParticleTypes.BARRIER);
		particles.put("crit", ParticleTypes.CRIT);
		particles.put("magicCrit", ParticleTypes.CRIT);
		particles.put("smoke", ParticleTypes.SMOKE);
		particles.put("mobSpell", ParticleTypes.ENCHANT);
		particles.put("mobSpellAmbient", ParticleTypes.ENCHANT);
		particles.put("spell", ParticleTypes.ENCHANT);
		particles.put("instantSpell", ParticleTypes.INSTANT_EFFECT);
		particles.put("witchMagic", ParticleTypes.WITCH);
		particles.put("note", ParticleTypes.NOTE);
		particles.put("portal", ParticleTypes.PORTAL);
		particles.put("enchantmenttable", ParticleTypes.POOF);
		particles.put("explode", ParticleTypes.EXPLOSION);
		particles.put("flame", ParticleTypes.FLAME);
		particles.put("lava", ParticleTypes.LAVA);
		particles.put("splash", ParticleTypes.SPLASH);
		particles.put("largesmoke", ParticleTypes.LARGE_SMOKE);
		particles.put("cloud", ParticleTypes.CLOUD);
		particles.put("snowballpoof", ParticleTypes.POOF);
		particles.put("dripWater", ParticleTypes.DRIPPING_WATER);
		particles.put("dripLava", ParticleTypes.DRIPPING_LAVA);
		particles.put("snowshovel", ParticleTypes.ITEM_SNOWBALL);
		particles.put("slime", ParticleTypes.ITEM_SLIME);
		particles.put("heart", ParticleTypes.HEART);
		particles.put("angryVillager", ParticleTypes.ANGRY_VILLAGER);
		particles.put("happyVillager", ParticleTypes.HAPPY_VILLAGER);
	}

	// Force reload of color handlers
	public static void reloadColorHandler() {
		Set<String> hndids = new HashSet<String>(colorMultTable.keySet());
		for (String hndid : hndids) {
			ColorMultHandler prev = colorMultTable.get(hndid);
			// Only reload those from resources
			if (prev instanceof CustomColorMultHandler) {
				colorMultTable.remove(hndid);
				if (getColorHandler(hndid, "<reload>") == null) {
					colorMultTable.put(hndid, prev);
				}
			}
		}		
	}
	// Get color muliplier
	public static ColorMultHandler getColorHandler(String hnd, String blockName) {
		String hndid = hnd.toLowerCase();
		ColorMultHandler cmh = colorMultTable.get(hndid);
		if (cmh == null) {
			// See if color code
			if ((hndid.length() == 7) && (hndid.charAt(0) == '#')) {
				try {
					cmh = new FixedColorMultHandler(Integer.parseInt(hndid.substring(1), 16));
					colorMultTable.put(hndid, cmh);
				} catch (NumberFormatException nfx) {
				}
			}
			// See if resource
			else {
				int idx = hnd.indexOf(':');
				if (idx < 0) {
					hnd = WesterosBlocks.MOD_ID + ":" + hnd;
					hndid = hnd.toLowerCase();
				}
				cmh = colorMultTable.get(hndid);
				if (cmh == null) {
					cmh = new CustomColorMultHandler(hnd, blockName);
					colorMultTable.put(hndid, cmh);
				}
			}
		}

		return cmh;
	}

	public String getBlockColorMapResource() {
		String res = null;
		String blockColor = colorMult;
		if ((blockColor != null) && (blockColor.startsWith("#") == false)) {
			String tok[] = blockColor.split(":");
			if (tok.length == 1) {
				if (tok[0].startsWith("textures/"))
					tok[0] = tok[0].substring(9);
				res = WesterosBlocks.MOD_ID + ":" + tok[0];
			} else {
				if (tok[1].startsWith("textures/"))
					tok[1] = tok[1].substring(9);
				res = tok[0] + ":" + tok[1];
			}
		}
		return res;
	}

	private static class BlockEntityRec {
		ArrayList<Block> blocks = new ArrayList<Block>();
		RegistryObject<BlockEntityType<?>> regobj;
	}

	private static HashMap<String, BlockEntityRec> te_rec = new HashMap<String, BlockEntityRec>();

	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WesterosBlocks.MOD_ID);

	public static <T extends BlockEntity> void registerBlockEntity(String name, BlockEntitySupplier<T> BlockEntitySupplier, Block blk)
	{
		BlockEntityRec rec = (BlockEntityRec) te_rec.get(name);
		if (rec == null) {
			rec = new BlockEntityRec();
			te_rec.put(name, rec);
			final BlockEntityRec frec = rec;
			rec.regobj = TILE_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(BlockEntitySupplier, frec.blocks.toArray(new Block[frec.blocks.size()])).build(null));
		}
		rec.blocks.add(blk);
	}
	public static RegistryObject<BlockEntityType<?>> getBlockEntityType(String name) {
		BlockEntityRec rec = te_rec.get(name);
		if (rec != null)
			return rec.regobj;
		return null;
	}
	
	// Handle registration of tint handling and other client rendering
	@OnlyIn(Dist.CLIENT)
	public void registerRender(Block blk) {
		if (this.isTinted()) {
			BlockColors blockColors = Minecraft.getInstance().getBlockColors();
			ItemColors itemColors = Minecraft.getInstance().getItemColors();
			ColorMultHandler handler = getColorHandler(this.colorMult, this.blockName);
			blockColors.register((BlockState state, BlockAndTintGetter world, BlockPos pos, int txtindx) -> handler
					.getColor(state, world, pos, txtindx), blk);
			itemColors.register((ItemStack stack, int tintIndex) -> handler.getItemColor(stack, tintIndex), blk);
		}
	}

	// Handle registration of tint handling and other client rendering
	@OnlyIn(Dist.CLIENT)
	public static void registerVanillaColorMap(String blockName, Block blk, String colorMult) {
		BlockColors blockColors = Minecraft.getInstance().getBlockColors();
		ItemColors itemColors = Minecraft.getInstance().getItemColors();
		ColorMultHandler handler = getColorHandler(colorMult, blockName);
		blockColors.register((BlockState state, BlockAndTintGetter world, BlockPos pos, int txtindx) -> handler
				.getColor(state, world, pos, txtindx), blk);
		itemColors.register((ItemStack stack, int tintIndex) -> handler.getItemColor(stack, tintIndex), blk);
	}

	public void registerSoundEvents() {
		if (this.soundList != null) {
			for (String snd : this.soundList) {
				WesterosBlocks.registerSound(snd);
			}
		}
	}
	// Get customized collision box for default solid block
	public VoxelShape makeCollisionBoxShape() {
		if (collisionBoxes == null) {
			return Shapes.block();	// Default to solid block
		}
		VoxelShape s = Shapes.empty();
		for (BoundingBox b : collisionBoxes) {
			s = Shapes.or(s, b.getAABB());
		}
		return s;		
	}
	
    public String getLegacyBlockName() {
    	if (legacyBlockID == null) return null;
    	String[] tok = legacyBlockID.split(":");
    	if (tok.length > 2) return tok[0] + ":" + tok[1];
    	return (tok.length >= 1) ? tok[0] : null;
    }
    public String getLegacyBlockVariant() {
    	if (legacyBlockID == null) return null;
    	String[] tok = legacyBlockID.split(":");
    	if (tok.length >= 2) {
    		return tok[tok.length - 1];
    	}
    	return null;
    }
    public Map<String, String> getLegacyBlockMap() {
    	if (legacyBlockID == null) return null;
    	Map<String, String> mval = new HashMap<String, String>();
    	String[] tok = legacyBlockID.split(":");
    	if (tok.length >= 2) {
    		String v = tok[tok.length - 1];
    		if (v.indexOf('=') < 0) {
    			if (!v.equals("default")) {
    				mval.put("variant", v);
    			}
    		}
    		else {
    			String[] stok = v.split(",");
    			for (String sv : stok) {
    				String[] vtok = sv.split("=");
    				if (vtok.length > 1)
    					mval.put(vtok[0], vtok[1]);
    			}
    		}
    		return mval;
    	}
    	return null;
    }

    public static class StateProperty extends Property<String> {
    	public ImmutableSet<String> values;
    	public ImmutableMap<String, String> valMap; 
    	public String defValue;
    	public StateProperty(List<String> stateIDs) {
    		super("state", String.class);
    		Map<String, String> map = Maps.newHashMap();
    		for (String s : stateIDs) {
    			map.put(s, s);
    		}
            this.values = ImmutableSet.copyOf(map.values());
            this.valMap = ImmutableMap.copyOf(map);
    		this.defValue = stateIDs.get(0);
    	}
    	@Override
    	public Collection<String> getPossibleValues() {
    		return this.values;
    	}
    	@Override
    	public boolean equals(Object obj) {
    		if (this == obj) {
				return true;
    		} else if (obj instanceof StateProperty && super.equals(obj)) {
		         StateProperty stateproperty = (StateProperty)obj;
		         return this.values.equals(stateproperty.values);
    		} else {
    		     return false;
    		}
    	}
    	@Override
    	public int generateHashCode() {
    		return 31 * super.generateHashCode() + this.values.hashCode();
    	}
    	@Override
    	public Optional<String> getValue(String key) {
			String val = this.valMap.get(key);
			return (val != null) ? Optional.of(val) : Optional.empty();
    	}
    	@Override
    	public String getName(String val) {
    		return val;
    	}
    }
        
    // Build 'state' property for the 
    public StateProperty buildCondProperty() {
    	if ((this.states == null) || (this.states.size() < 2)) return null;
    	
    	ArrayList<String> ids = new ArrayList<String>();    	
		for (int i = 0; i < states.size(); i++) {
			StateRecord rec = states.get(i);
			if (rec.stateID == null) rec.stateID = String.format("state%d", i);
    		ids.add(rec.stateID);
    	}
    	return new StateProperty(ids);
    }
    
    // Get default state ID
    public String getDefaultStateID() {
    	if (this.states == null) return null;
    	return this.states.get(0).stateID;
    }
}
