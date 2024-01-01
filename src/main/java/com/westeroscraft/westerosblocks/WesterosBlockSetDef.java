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

  public static final List<String> DEFAULT_VARIANTS = Arrays.asList("solid", "stairs", "slab", "wall", "fence", "hopper");
  public static final List<String> SUPPORTED_VARIANTS = Arrays.asList("solid", "stairs", "slab", "wall", "fence", "hopper", "tip",
                                                                      "carpet", "fence_gate", "half_door", "cover", "hollow_hopper",
                                                                      "log", "directional", "layer", "pane",
                                                                      "window_frame", "arrow_slit", "arrow_slit_window", "arrow_slit_ornate");
  public static final Map<String, String> VARIANT_TYPES = new HashMap<String, String>();
  static { // For any variant not listed here, it is assumed that the type is the same as the variant string
    VARIANT_TYPES.put("stairs", "stair");
    VARIANT_TYPES.put("hopper", "cuboid");
    VARIANT_TYPES.put("tip", "cuboid");
    VARIANT_TYPES.put("carpet", "cuboid");
    VARIANT_TYPES.put("fence_gate", "fencegate");
    VARIANT_TYPES.put("half_door", "cuboid-nsew");
    VARIANT_TYPES.put("cover", "rail");
    VARIANT_TYPES.put("hollow_hopper", "cuboid");
    VARIANT_TYPES.put("directional", "cuboid-nsew");
    VARIANT_TYPES.put("window_frame", "solid");
    VARIANT_TYPES.put("arrow_slit", "solid");
    VARIANT_TYPES.put("arrow_slit_window", "solid");
    VARIANT_TYPES.put("arrow_slit_ornate", "solid");
  }
  public static final Map<String, String[]> VARIANT_TEXTURES = new HashMap<String, String[]>();
  static {
    VARIANT_TEXTURES.put("solid", new String[]{ "bottom", "top", "west", "east", "south", "north" });
    VARIANT_TEXTURES.put("stairs", new String[]{ "bottom", "top", "sides" });
    VARIANT_TEXTURES.put("slab", new String[]{ "bottom", "top", "sides" });
    VARIANT_TEXTURES.put("wall", new String[]{ "bottom", "top", "sides" });
    VARIANT_TEXTURES.put("fence", new String[]{ "bottom", "top", "sides" });
    VARIANT_TEXTURES.put("hopper", new String[]{ "sides" });
    VARIANT_TEXTURES.put("tip", new String[]{ "sides" });
    VARIANT_TEXTURES.put("carpet", new String[]{ "sides" });
    VARIANT_TEXTURES.put("fence_gate", new String[]{ "sides" });
    VARIANT_TEXTURES.put("half_door", new String[]{ "sides" });
    VARIANT_TEXTURES.put("cover", new String[]{ "cover" });
    VARIANT_TEXTURES.put("hollow_hopper", new String[]{ "sides" });
    VARIANT_TEXTURES.put("log", new String[]{ "bottom", "top", "sides" });
    VARIANT_TEXTURES.put("directional", new String[]{ "bottom", "top", "west", "east", "south", "north" });
    VARIANT_TEXTURES.put("layer", new String[]{ "sides" });
    VARIANT_TEXTURES.put("pane", new String[]{ "sides", "top" });
    VARIANT_TEXTURES.put("window_frame", new String[]{ "window-topbottom", "window-topbottom", "window-frame" });
    VARIANT_TEXTURES.put("arrow_slit", new String[]{ "window-topbottom", "window-topbottom", "arrow-slit" });
    VARIANT_TEXTURES.put("arrow_slit_window", new String[]{ "window-topbottom", "window-topbottom", "arrow-slit-window" });
    VARIANT_TEXTURES.put("arrow_slit_ornate", new String[]{ "window-topbottom", "window-topbottom", "arrow-slit-ornate" });
  }
	
	public String baseBlockName; // Unique name to be used as a base for all the generated block names
  public String baseLabel; // Base label associated with blocks in set
  public List<String> variants = null; // List of supported variants to create (solid, stair, slab, wall, fence, hopper)
                  // By default, all of the above variants will be created

  public Map<String, String> altNames = null; // Alternative name extensions to use for particular variants (optional)

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
  public Map<String, List<String>> altCustomTags = null; // Allows idiosyncratic tags for particular variants

  public Map<String, String> types = null; // Map of type attributes for each variant
	
	public boolean alphaRender = false; // If true, do render on pass 2 (for alpha blending)
	public Boolean ambientOcclusion = null; // Set ambient occlusion (default is true)
	public boolean nonOpaque = false; // If true, does not block visibility of shared faces (solid blocks) and doesn't allow torches

  public Map<String, List<String>> altTextures = null; // Allows idiosyncratic textures for particular variants
	public Map<String, List<RandomTextureSet>> altRandomTextures = null;	// Allows idiosyncratic randomTextures for particular variants
	public Map<String, List<String>> altOverlayTextures = null; // Allows idiosyncratic overlayTextures for particular variants

  public Map<String, String> textures = null; // Map of textures to use for each variant (for single texture set)
	public List<RandomTextureMap> randomTextures = null;	// Defines sets of textures used for additional random models,
                    // for each variant (if supported)
	public Map<String, String> overlayTextures = null; // Map of overlay textures (for types supporting overlays)

	public float lightValue = 0.0F; // Emitted light level (0.0-1.0)
	public String colorMult = "#FFFFFF"; // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')


	public static class RandomTextureMap {
		public Map<String, String> textures = null; // List of textures (for single texture set)
		public Integer weight = null;		// Weight for texture set (default = 1)
	};


  public List<WesterosBlockDef> generateBlockDefs() {
    List<WesterosBlockDef> blockDefs = new LinkedList<WesterosBlockDef>();
    
    this.types = preprocessVariantMap(this.types);
    this.altCustomTags = preprocessVariantMap(this.altCustomTags);
    this.altTextures = preprocessVariantMap(this.altTextures);
    this.altRandomTextures = preprocessVariantMap(this.altRandomTextures);
    this.altOverlayTextures = preprocessVariantMap(this.altOverlayTextures);

    for (String variant : WesterosBlockSetDef.SUPPORTED_VARIANTS) {
      if (this.variants != null && !variants.contains(variant))
        continue;
      else if (this.variants == null && !WesterosBlockSetDef.DEFAULT_VARIANTS.contains(variant))
        continue;
      
      WesterosBlockDef variantDef = new WesterosBlockDef();
      
      // Automatically derive name and label for variant (or use alt name if provided)
      if (this.altNames != null && this.altNames.containsKey(variant)) {
        variantDef.blockName = this.altNames.get(variant);
        variantDef.label = WesterosBlockSetDef.generateLabel(variantDef.blockName);
      }
      else {
        String suffix = (variant.equals("solid")) ? "" : variant;
        String suffix_label = (suffix.isEmpty()) ? "" : WesterosBlockSetDef.generateLabel(suffix);
        variantDef.blockName = this.baseBlockName;
        if (!suffix.isEmpty())
          variantDef.blockName += "_" + suffix;
        variantDef.label = this.baseLabel + " " + suffix_label;
      }
      
      // Set blocktype for variant
      String blockType = WesterosBlockSetDef.VARIANT_TYPES.get(variant);
      if (blockType == null)
        blockType = variant;
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
      variantDef.alphaRender = this.alphaRender;
      variantDef.ambientOcclusion = this.ambientOcclusion;
      variantDef.nonOpaque = this.nonOpaque;

      if (this.altCustomTags != null && this.altCustomTags.containsKey(variant)) {
        List<String> tags = altCustomTags.get(variant);
        variantDef.customTags = (!tags.isEmpty()) ? tags : null;
      }
      else
        variantDef.customTags = this.customTags;

      // Copy type attribute for variant
      if (this.types != null && this.types.containsKey(variant)) {
        variantDef.type = this.types.get(variant);
      }
      else {
        // Enforce defaults for particular blocktypes
        if (variant.matches("stairs|wall|fence|pane"))
          variantDef.type = "unconnect:false";
        else if (variant.contains("arrow_slit") || variant.contains("window_frame"))
          variantDef.type = "connectstate:true";
        else if (variant.equals("cover"))
          variantDef.type = "allow-unsupported";
        else
          variantDef.type = "";
      }

      // Copy general block state record properties to variant
      variantDef.lightValue = this.lightValue;
      variantDef.colorMult = this.colorMult;

      // Process blocktypes with special attributes
      if (variant.equals("stairs")) {
        if (this.altNames != null && this.altNames.containsKey("solid"))
          variantDef.modelBlockName = this.altNames.get("solid");
        else if (this.variants.contains("solid"))
          variantDef.modelBlockName = this.baseBlockName;
      }
      else if (variant.equals("hopper")) {
        WesterosBlockDef.Cuboid[] cuboids = { 
          new WesterosBlockDef.Cuboid(0.3755f, 0f, 0.3755f, 0.6245f, 0.275f, 0.6245f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0.25f, 0.275f, 0.25f, 0.75f, 0.625f, 0.75f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0f, 0.625f, 0f, 1f, 1f, 1f, new int[] { 0, 0, 0, 0, 0, 0 }),
        };
        variantDef.cuboids = Arrays.asList(cuboids);
      }
      else if (variant.equals("tip")) {
        WesterosBlockDef.Cuboid[] cuboids = { 
          new WesterosBlockDef.Cuboid(0.3755f, 0.625f, 0.3755f, 0.6245f, 1f, 0.6245f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0.25f, 0.275f, 0.25f, 0.75f, 0.625f, 0.75f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 0.275f, 1f, new int[] { 0, 0, 0, 0, 0, 0 }),
        };
        variantDef.cuboids = Arrays.asList(cuboids);
      }
      else if (variant.equals("carpet")) {
        WesterosBlockDef.Cuboid[] cuboids = { 
          new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 0.0625f, 1f, new int[] { 0, 0, 0, 0, 0, 0 })
        };
        variantDef.cuboids = Arrays.asList(cuboids);
      }
      else if (variant.equals("half_door")) {
        variantDef.boundingBox = new WesterosBlockDef.BoundingBox(0f, 0f, 0f, 0.1875f, 1f, 1f);
      }
      else if (variant.equals("hollow_hopper")) {
        WesterosBlockDef.Cuboid[] cuboids = { 
          new WesterosBlockDef.Cuboid(0.3755f, 0.16f, 0.3755f, 0.6245f, 0.275f, 0.6245f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0.25f, 0.275f, 0.25f, 0.75f, 0.625f, 0.75f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0f, 0.625f, 0f, 1f, 0.65f, 1f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0f, 0.625f, 0f, 0.125f, 1f, 1f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0.875f, 0.625f, 0f, 1f, 1f, 1f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0f, 0.625f, 0f, 1f, 1f, 0.125f, new int[] { 0, 0, 0, 0, 0, 0 }),
          new WesterosBlockDef.Cuboid(0f, 0.625f, 0.875f, 1f, 1f, 1f, new int[] { 0, 0, 0, 0, 0, 0 }),
        };
        variantDef.cuboids = Arrays.asList(cuboids);
      }
      else if (variant.equals("directional")) {
        WesterosBlockDef.Cuboid[] cuboids = { 
          new WesterosBlockDef.Cuboid(0f, 0f, 0f, 1f, 1f, 1f)
        };
        variantDef.cuboids = Arrays.asList(cuboids);
      }
      else if (variant.contains("arrow_slit") || variant.contains("window_frame")) {
        variantDef.nonOpaque = true;
        variantDef.lightOpacity = 0;
        WesterosBlockDef.BoundingBox[] collisionBoxes = {
          new WesterosBlockDef.BoundingBox(0f, 0f, 0f, 0.2f, 1f, 0.2f),
          new WesterosBlockDef.BoundingBox(0.8f, 0f, 0f, 1f, 1f, 0.2f),
          new WesterosBlockDef.BoundingBox(0f, 0f, 0.8f, 0.2f, 1f, 1f),
          new WesterosBlockDef.BoundingBox(0.8f, 0f, 0.8f, 1f, 1f, 1f)
        };
        variantDef.collisionBoxes = Arrays.asList(collisionBoxes);
        WesterosBlockDef.BoundingBox[] supportBoxes = {
          new WesterosBlockDef.BoundingBox(0f, 0f, 0f, 1f, 1f, 1f)
        };
        variantDef.supportBoxes = Arrays.asList(supportBoxes);
      }

      // If a variant has an alt texture list defined, use it, otherwise create texture lists for this variant type
      if (this.altTextures != null && this.altTextures.containsKey(variant))
        variantDef.textures = this.altTextures.get(variant);
      else
        variantDef.textures = WesterosBlockSetDef.getTexturesForVariant(WesterosBlockSetDef.preprocessTextureMap(this.textures), variant);

      if (this.altRandomTextures != null && this.altRandomTextures.containsKey(variant))
        variantDef.randomTextures = this.altRandomTextures.get(variant);
      else
        variantDef.randomTextures = WesterosBlockSetDef.getRandomTexturesForVariant(WesterosBlockSetDef.preprocessRandomTextureMaps(this.randomTextures), variant);

      if (this.altOverlayTextures != null && this.altOverlayTextures.containsKey(variant))
        variantDef.overlayTextures = this.altOverlayTextures.get(variant);
      else
        variantDef.overlayTextures = WesterosBlockSetDef.getTexturesForVariant(WesterosBlockSetDef.preprocessTextureMap(this.overlayTextures), variant);

      blockDefs.add(variantDef);
    }

    return blockDefs;
  }

  // Allow for multiple variants to be provided in one map entry, separated by commas
  public static <T> Map<String, T> preprocessVariantMap(Map<String, T> map) {
    if (map == null)
      return null;

    Map<String, T> newMap = new HashMap<String, T>();
    for (Map.Entry<String, T> entry : map.entrySet()) {
      String key = entry.getKey();
      T value = entry.getValue();
      if (!key.contains(","))
        newMap.put(key, value);
      else {
        String[] variants = key.split(",");
        for (String variant : variants)
          newMap.put(variant, value);
      }
    }
    return newMap;
  }

  public static String generateLabel(String name) {
    if (name.isEmpty() || name.equals("_"))
      return "";

    String[] words = name.split("_");
    String label = "";
    for (String word : words) {
      String wordCap = word.substring(0,1).toUpperCase() + word.substring(1);
      label += wordCap + " ";
    }
    return label.trim();
  }

  public static Map<String, String> preprocessTextureMap(Map<String, String> textureMap) {
    if (textureMap == null)
      return null;

    if (textureMap.containsKey("all")) {
      textureMap.put("bottom", textureMap.get("all"));
      textureMap.put("top", textureMap.get("all"));
      textureMap.put("sides", textureMap.get("all"));
    }

    if (!textureMap.containsKey("west") && textureMap.containsKey("sides"))
      textureMap.put("west", textureMap.get("sides"));
    if (!textureMap.containsKey("east") && textureMap.containsKey("sides"))
      textureMap.put("east", textureMap.get("sides"));
    if (!textureMap.containsKey("south") && textureMap.containsKey("sides"))
      textureMap.put("south", textureMap.get("sides"));
    if (!textureMap.containsKey("north") && textureMap.containsKey("sides"))
      textureMap.put("north", textureMap.get("sides"));
    
    // fallback if "sides" not explicitly specified
    if (!textureMap.containsKey("sides") && textureMap.containsKey("bottom"))
      textureMap.put("sides", textureMap.get("bottom"));

    // other variant-specific fallback rules
    // if (!textureMap.containsKey("window-topbottom") && textureMap.containsKey("bottom"))
    //   textureMap.put("window-topbottom", textureMap.get("bottom"));
    if (!textureMap.containsKey("window-topbottom"))
      textureMap.put("window-topbottom", "transparent");

    if (!textureMap.containsKey("cover") && textureMap.containsKey("sides"))
      textureMap.put("cover", textureMap.get("sides"));

    return textureMap;
  }

  public static List<RandomTextureMap> preprocessRandomTextureMaps(List<RandomTextureMap> randomTextureMaps) {
    if (randomTextureMaps == null)
      return null;

    for (int i = 0; i < randomTextureMaps.size(); i++) {
      RandomTextureMap updated = randomTextureMaps.get(i);
      updated.textures = preprocessTextureMap(updated.textures);
      randomTextureMaps.set(i, updated);
    }
    return randomTextureMaps;
  }

  public static List<String> getTexturesForVariant(Map<String, String> textureMap, String variant) {
    if (textureMap == null)
      return null;

    List<String> textureList = new LinkedList<String>();

    for (String texture : WesterosBlockSetDef.VARIANT_TEXTURES.get(variant)) {
      if (textureMap.containsKey(texture)) textureList.add(textureMap.get(texture));
    }

    return (!textureList.isEmpty()) ? textureList : null;
  }

  public static List<RandomTextureSet> getRandomTexturesForVariant(List<RandomTextureMap> randomTextureMaps, String variant) {
    if (randomTextureMaps == null)
      return null;

    List<RandomTextureSet> randomTextures = new LinkedList<RandomTextureSet>();

    for (RandomTextureMap randomTextureMap : randomTextureMaps) {
      RandomTextureSet randomTextureSet = new RandomTextureSet();
      randomTextureSet.textures = getTexturesForVariant(randomTextureMap.textures, variant);
      randomTextureSet.weight = randomTextureMap.weight;
      if (randomTextureSet.textures != null && !randomTextureSet.textures.isEmpty())
        randomTextures.add(randomTextureSet);
    }

    return (!randomTextures.isEmpty()) ? randomTextures : null;
  }
}
