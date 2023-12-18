package com.westeroscraft.westerosblocks;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import com.westeroscraft.westerosblocks.WesterosBlockDef.HarvestLevel;
import com.westeroscraft.westerosblocks.WesterosBlockDef.RandomTextureSet;

//
// Template for block set configuration data (populated using GSON)
//
public class WesterosBlockSetDef {
	private static final float DEF_FLOAT = -999.0F;
	public static final int DEF_INT = -999;

  public static final String[] SUPPORTED_VARIANTS = { "solid", "stairs", "slab", "wall", "fence", "hopper" };
  public static final Map<String, String> VARIANT_TYPES = new HashMap<String, String>();
  static { // For any variant not listed here, it is assumed that the type is the same as the variant string
    VARIANT_TYPES.put("stairs", "stair");
    VARIANT_TYPES.put("hopper", "cuboid");
  }
	
	public String baseBlockName; // Unique name to be used as a base for all the generated block names
  public String baseLabel; // Base label associated with blocks in set
  public List<String> variants = null; // List of supported variants to create (solid, stair, slab, wall, fence, hopper)
                  // By default, all of the above variants will be created

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
	public List<String> customTags = null;	// If block should add any custom tags

	public String type = ""; // Type field (used for plant types or other block type specific values)
	
	public boolean alphaRender = false; // If true, do render on pass 2 (for alpha blending)
	public Boolean ambientOcclusion = null; // Set ambient occlusion (default is true)
	public boolean nonOpaque = false; // If true, does not block visibility of shared faces (solid blocks) and doesn't allow torches

  public List<String> textures = null; // List of textures to use for each variant (for single texture set)
	public List<RandomTextureSet> randomTextures = null;	// Defines sets of textures used for additional random models,
                    // for each variant (if supported)
	public List<String> overlayTextures = null; // List of overlay textures (for types supporting overlays)
	public float lightValue = 0.0F; // Emitted light level (0.0-1.0)
	public String colorMult = "#FFFFFF"; // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')


  public List<WesterosBlockDef> generateBlockDefs() {
    List<WesterosBlockDef> blockDefs = new LinkedList<WesterosBlockDef>();

    for (String variant : WesterosBlockSetDef.SUPPORTED_VARIANTS) {
      if (this.variants != null && !variants.contains(variant))
        continue;
      
      WesterosBlockDef variantDef = new WesterosBlockDef();
      
      // Automatically derive name, label, and type for variant
      String suffix = (variant == "solid") ? "" : "_" + variant;
      String suffix_label = suffix.replace("_", "");
      if (!suffix_label.isEmpty())
        suffix_label = suffix_label.substring(0, 1).toUpperCase() + suffix_label.substring(1);
      String blockType = WesterosBlockSetDef.VARIANT_TYPES.get(variant);
      if (blockType == null)
        blockType = variant;

      variantDef.blockName = this.baseBlockName + suffix;
      variantDef.label = this.baseLabel + " " + suffix_label;
      variantDef.blockType = blockType;

      // Copy general block definition properties to variant
      variantDef.hardness = this.hardness;
      variantDef.stepSound = this.stepSound;
      variantDef.material = this.material;
      variantDef.resistance = this.resistance;
      variantDef.lightOpacity = this.lightOpacity;
      variantDef.harvestLevel = this.harvestLevel;
      variantDef.fireSpreadSpeed = this.fireSpreadSpeed;
      variantDef.flamability = this.flamability;
      variantDef.creativeTab = this.creativeTab;
      variantDef.customTags = this.customTags;
      variantDef.type = this.type;
      variantDef.alphaRender = this.alphaRender;
      variantDef.ambientOcclusion = this.ambientOcclusion;
      variantDef.nonOpaque = this.nonOpaque;

      // Copy general block state record properties to variant
      variantDef.textures = this.textures;
      variantDef.randomTextures = this.randomTextures;
      variantDef.overlayTextures = this.overlayTextures;
      variantDef.lightValue = this.lightValue;
      variantDef.colorMult = this.colorMult;

      // Process types with special attributes
      if (variant == "stairs") {
        variantDef.modelBlockName = this.baseBlockName;
      }
      else if (variant == "hopper") {
        WesterosBlockDef.Cuboid[] cuboids = { 
          new WesterosBlockDef.Cuboid(0.3755f, 0f, 0.3755f, 0.6245f, 0.275f, 0.6245f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0.25f, 0.275f, 0.25f, 0.75f, 0.625f, 0.75f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0f, 0.625f, 0f, 1f, 1f, 1f, new int[] { 0, 0, 0, 0, 0, 0 }),
        };
        variantDef.cuboids = Arrays.asList(cuboids);
      }

      blockDefs.add(variantDef);
    }

    return blockDefs;
  }
}
