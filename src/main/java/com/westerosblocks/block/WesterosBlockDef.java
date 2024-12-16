package com.westerosblocks.block;

import java.util.List;

public class WesterosBlockDef {
    private static final float DEF_FLOAT = -999.0F;
    public static final int DEF_INT = -999;
    public String blockName; // Locally unique block name
    public String blockType = "solid"; // Block type ('solid', 'liquid', 'plant', 'log', 'stairs', etc)
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
    public List<String> customTags = null;    // If block should add any custom tags

    public String type = ""; // Type field (used for plant types or other block type specific values)

    public boolean alphaRender = false; // If true, do render on pass 2 (for alpha blending)
    public Boolean ambientOcclusion = null; // Set ambient occlusion (default is true)
    public boolean nonOpaque = false; // If true, does not block visibility of shared faces (solid blocks) and doesn't
    // allow torches
    // ('solid', 'sound', 'sand', 'soulsand' blocks)
    public String label; // Label for item associated with block
    public String itemTexture = null; // Item texture, if any
    public int itemTextureIndex = 0; // Index of texture for item icon
    public List<String> soundList = null; // List of custom sound names or sound IDs (for 'sound' blocks)

    public List<WesterosBlockStateRecord> stack = null; // List of elements for a stack, first is bottom-most (for *-stack)

    public List<WesterosBlockStateRecord> states = null;

//    private StateProperty stateProp = null;

    public String connectBy = "block";

    public static class HarvestLevel {
        public String tool;
        public int level;
    }
}
