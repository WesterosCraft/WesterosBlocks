package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.*;
import com.westerosblocks.block.custom.WCTorchBlock;
import com.westerosblocks.block.custom.WCWallTorchBlock;
import com.westerosblocks.block.custom.WCFanBlock;
import com.westerosblocks.block.custom.WCWallFanBlock;
import com.westerosblocks.block.custom.WCVinesBlock;
import com.westerosblocks.block.custom.WCHalfDoorBlock;
import com.westerosblocks.block.custom.StandaloneFlowerPotBlock;
import com.westerosblocks.util.ModWoodType;
import com.westerosblocks.util.ModBlockSoundGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.WoodType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

import java.util.List;

/**
 * A fluent builder class for creating and registering custom blocks in WesterosBlocks.
 * 
 * <p>This builder provides a convenient way to configure block properties including:
 * <ul>
 *   <li>Physical properties (hardness, resistance, harvest requirements)</li>
 *   <li>Visual properties (textures, map colors, opacity)</li>
 *   <li>Behavioral properties (burnable, collision, drops)</li>
 *   <li>Creative tab placement</li>
 * </ul>
 * 
 * <p>Usage example:
 * <pre>{@code
 * new BlockBuilder("oak_table")
 *     .hardness(2.0f)
 *     .resistance(6.0f)
 *     .requiresAxe()
 *     .texture("oak_table")
 *     .woodType("oak")
 *     .register();
 * }</pre>
 * 
 */
public class BlockBuilder {
    /** The unique identifier for this block (used for registry and texture paths) */
    private final String name;
    
    /** The creative tab where this block will be placed (default: "westeros_decor_tab") */
    private String creativeTab = "westeros_decor_tab";
    
    /** Block hardness - affects mining speed and tool requirements (default: 2.0f) */
    private float hardness = 2.0f;
    
    /** Block resistance - affects explosion resistance (default: 6.0f) */
    private float resistance = 6.0f;
    
    /** Minimum tool tier required to harvest this block (0 = any tool, 1 = stone, 2 = iron, etc.) */
    private int harvestLevel = 0;
    
    /** Path to the block's texture file (relative to assets/westerosblocks/textures/block/) */
    private String texturePath;
    
    /** Multiple texture paths for blocks that need different textures on different faces */
    private String[] texturePaths;
    
    /** Wood type for wooden blocks (affects sounds, textures, and behavior) */
    private WoodType woodType = WoodType.OAK;
    
    /** Sound type for block interactions (e.g., "wood", "stone", "metal") */
    private String soundType = "wood";
    
    /** Whether this block is locked (affects trapdoor behavior) */
    private boolean locked = false;
    
    /** Whether this block is non-opaque (allows light to pass through) */
    private boolean nonOpaque = false;
    
    /** Whether this block can be burned by fire */
    private boolean burnable = false;
    
    /** Whether this block has no collision box (entities can pass through) */
    private boolean noCollision = false;
    
    /** Whether this block drops nothing when broken */
    private boolean dropsNothing = false;
    
    /** Whether this block is transparent for alpha blending */
    private boolean alphaRender = false;
    
    /** Map color for this block (affects how it appears on maps) */
    private MapColor mapColor = null;
    
    /** The type of custom block to create (set internally by registration methods) */
    private BlockType blockType;
    
    /** The tool type required to harvest this block (e.g., "pickaxe", "axe", "shovel") */
    private String harvestTool;
    
    /** Dye color for colored blocks (like beds) */
    private DyeColor dyeColor = DyeColor.RED;
    
    
    private boolean allowUnsupported = false;
    private boolean noParticle = false;
    private boolean canGrowDownward = false;
    private List<String> tooltips = null;
    private int lightLevel = 0;
    private Block wallBlock = null;
    private boolean layerSensitive = false;
    private boolean toggleOnUse = false;
    private double[] boundingBox = null; // [xMin, xMax, yMin, yMax, zMin, zMax]
    private Block plantContent = null; // For flower pot blocks

    /**
     * Creates a new BlockBuilder with the specified name.
     * 
     * @param name The unique identifier for this block (used for registry and texture paths)
     * @throws IllegalArgumentException if name is null or empty
     */
    public BlockBuilder(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Block name cannot be null or empty");
        }
        this.name = name;
    }

    /**
     * Sets the creative tab where this block will be placed.
     * 
     * @param creativeTab The creative tab identifier
     * @return this builder for method chaining
     */
    public BlockBuilder creativeTab(String creativeTab) {
        this.creativeTab = creativeTab;
        return this;
    }

    /**
     * Sets the block's hardness value.
     * 
     * <p>Hardness affects:
     * <ul>
     *   <li>How long it takes to break the block</li>
     *   <li>Minimum tool tier requirements</li>
     *   <li>Explosion resistance (when combined with resistance)</li>
     * </ul>
     * 
     * @param hardness The hardness value (higher = harder to break)
     * @return this builder for method chaining
     */
    public BlockBuilder hardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    /**
     * Sets the block's resistance value.
     * 
     * <p>Resistance affects:
     * <ul>
     *   <li>Explosion resistance</li>
     *   <li>Piston pushability</li>
     * </ul>
     * 
     * @param resistance The resistance value (higher = more resistant)
     * @return this builder for method chaining
     */
    public BlockBuilder resistance(float resistance) {
        this.resistance = resistance;
        return this;
    }

    /**
     * Sets the minimum tool tier required to harvest this block.
     * 
     * <p>Harvest levels:
     * <ul>
     *   <li>0: Any tool (or no tool)</li>
     *   <li>1: Stone tier (wooden tools)</li>
     *   <li>2: Iron tier (stone tools)</li>
     *   <li>3: Diamond tier (iron tools)</li>
     *   <li>4: Netherite tier (diamond tools)</li>
     * </ul>
     * 
     * @param harvestLevel The minimum tool tier required
     * @return this builder for method chaining
     */
    public BlockBuilder harvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
        return this;
    }

    /**
     * Sets the tool type required to harvest this block.
     * 
     * @param tool The tool type ("pickaxe", "axe", "shovel", "hoe", or "shears")
     * @return this builder for method chaining
     */
    public BlockBuilder harvestTool(String tool) {
        this.harvestTool = tool;
        return this;
    }

    /**
     * Sets both the tool type and harvest level required to harvest this block.
     * 
     * @param tool The tool type ("pickaxe", "axe", "shovel", "hoe", or "shears")
     * @param level The minimum tool tier required
     * @return this builder for method chaining
     */
    public BlockBuilder harvestTool(String tool, int level) {
        this.harvestTool = tool;
        this.harvestLevel = level;
        return this;
    }

    // Convenience methods for common harvest tools

    /**
     * Sets this block to require a pickaxe of any tier.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder requiresPickaxe() {
        return harvestTool("pickaxe", 1);
    }

    /**
     * Sets this block to require a pickaxe of the specified tier.
     * 
     * @param level The minimum pickaxe tier required
     * @return this builder for method chaining
     */
    public BlockBuilder requiresPickaxe(int level) {
        return harvestTool("pickaxe", level);
    }

    /**
     * Sets this block to require an axe of any tier.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder requiresAxe() {
        return harvestTool("axe", 1);
    }

    /**
     * Sets this block to require an axe of the specified tier.
     * 
     * @param level The minimum axe tier required
     * @return this builder for method chaining
     */
    public BlockBuilder requiresAxe(int level) {
        return harvestTool("axe", level);
    }

    /**
     * Sets this block to require a shovel of any tier.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder requiresShovel() {
        return harvestTool("shovel", 1);
    }

    /**
     * Sets this block to require a shovel of the specified tier.
     * 
     * @param level The minimum shovel tier required
     * @return this builder for method chaining
     */
    public BlockBuilder requiresShovel(int level) {
        return harvestTool("shovel", level);
    }

    /**
     * Sets this block to require a hoe of any tier.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder requiresHoe() {
        return harvestTool("hoe", 1);
    }

    /**
     * Sets this block to require a hoe of the specified tier.
     * 
     * @param level The minimum hoe tier required
     * @return this builder for method chaining
     */
    public BlockBuilder requiresHoe(int level) {
        return harvestTool("hoe", level);
    }

    /**
     * Sets the texture path for this block.
     * 
     * <p>The texture path will be automatically prefixed with "westerosblocks:block/".
     * For example, calling {@code texture("oak_table")} will use the texture at
     * {@code assets/westerosblocks/textures/block/oak_table.png}.
     * 
     * @param texturePath The texture path (without the "westerosblocks:block/" prefix)
     * @return this builder for method chaining
     */
    public BlockBuilder texture(String texturePath) {
        this.texturePath = "westerosblocks:block/" + texturePath;
        return this;
    }

    /**
     * Sets multiple texture paths for blocks that need different textures on different faces.
     * 
     * <p>For log blocks, the order is typically [bottom, top, side].
     * For other block types, the order depends on the specific block implementation.
     * 
     * @param texturePaths Array of texture paths (without .png extension)
     * @return this builder for method chaining
     */
    public BlockBuilder textures(String... texturePaths) {
        this.texturePaths = new String[texturePaths.length];
        for (int i = 0; i < texturePaths.length; i++) {
            this.texturePaths[i] = "westerosblocks:block/" + texturePaths[i];
        }
        return this;
    }

    /**
     * Gets the texture paths array for multi-texture blocks.
     * 
     * @return Array of texture paths, or null if not set
     */
    public String[] getTexturePaths() {
        return texturePaths;
    }

    /**
     * Gets the primary texture path for single-texture blocks.
     * 
     * @return The texture path, or null if not set
     */
    public String getTexturePath() {
        return texturePath;
    }

    /**
     * Gets the block name.
     * 
     * @return the block name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the creative tab.
     * 
     * @return the creative tab
     */
    public String getCreativeTab() {
        return creativeTab;
    }

    /**
     * Gets the hardness value.
     * 
     * @return the hardness value
     */
    public float getHardness() {
        return hardness;
    }

    /**
     * Gets the resistance value.
     * 
     * @return the resistance value
     */
    public float getResistance() {
        return resistance;
    }

    /**
     * Gets the sound type.
     * 
     * @return the sound type
     */
    public String getSoundType() {
        return soundType;
    }

    /**
     * Gets whether this block is non-opaque.
     * 
     * @return true if non-opaque
     */
    public boolean isNonOpaque() {
        return nonOpaque;
    }

    /**
     * Gets whether this block has no collision.
     * 
     * @return true if no collision
     */
    public boolean isNoCollision() {
        return noCollision;
    }

    /**
     * Gets whether this block drops nothing.
     * 
     * @return true if drops nothing
     */
    public boolean isDropsNothing() {
        return dropsNothing;
    }

    /**
     * Gets the map color.
     * 
     * @return the map color
     */
    public MapColor getMapColor() {
        return mapColor;
    }

    /**
     * Sets the wood type for this block using a WoodType enum.
     * 
     * @param woodType The wood type enum value
     * @return this builder for method chaining
     */
    public BlockBuilder woodType(WoodType woodType) {
        this.woodType = woodType;
        return this;
    }

    /**
     * Sets the wood type for this block using a string identifier.
     * 
     * <p>Supported wood types: "oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
     * "mangrove", "cherry", "bamboo", "crimson", "warped"
     * 
     * @param woodType The wood type string identifier
     * @return this builder for method chaining
     */
    public BlockBuilder woodType(String woodType) {
        this.woodType = ModWoodType.getWoodType(woodType);
        return this;
    }

    /**
     * Sets the sound type for this block.
     * 
     * <p>Common sound types: "wood", "stone", "metal", "glass", "cloth", "sand", "gravel"
     * 
     * @param soundType The sound type identifier
     * @return this builder for method chaining
     */
    public BlockBuilder soundType(String soundType) {
        this.soundType = soundType;
        return this;
    }

    /**
     * Sets whether this block is locked (affects trapdoor behavior).
     * 
     * @param locked Whether the block should be locked
     * @return this builder for method chaining
     */
    public BlockBuilder locked(boolean locked) {
        this.locked = locked;
        return this;
    }

    /**
     * Makes this block non-opaque (allows light to pass through).
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder nonOpaque() {
        this.nonOpaque = true;
        return this;
    }

    /**
     * Enables alpha blending for this block (for transparent textures).
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder alphaRender() {
        this.alphaRender = true;
        return this;
    }

    /**
     * Makes this block burnable (can be destroyed by fire).
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder burnable() {
        this.burnable = true;
        return this;
    }

    /**
     * Removes collision box from this block (entities can pass through).
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder noCollision() {
        this.noCollision = true;
        return this;
    }

    /**
     * Makes this block drop nothing when broken.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder dropsNothing() {
        this.dropsNothing = true;
        return this;
    }

    /**
     * Makes this block break instantly (like plants).
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder breakInstantly() {
        this.hardness = 0.0f;
        return this;
    }

    /**
     * Sets the map color for this block.
     * 
     * <p>Map colors affect how the block appears on maps and item frames.
     * 
     * @param mapColor The map color to use
     * @return this builder for method chaining
     */
    public BlockBuilder mapColor(MapColor mapColor) {
        this.mapColor = mapColor;
        return this;
    }

    /**
     * Sets the dye color for this block (used for colored blocks like beds).
     * 
     * @param color The dye color to use
     * @return this builder for method chaining
     */
    public BlockBuilder color(DyeColor color) {
        this.dyeColor = color;
        return this;
    }

    /**
     * Sets whether this torch can be placed without support.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder allowUnsupported() {
        this.allowUnsupported = true;
        return this;
    }

    /**
     * Sets whether this torch should emit particles.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder noParticle() {
        this.noParticle = true;
        return this;
    }

    /**
     * Sets whether this vines block can grow downward.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder canGrowDownward() {
        this.canGrowDownward = true;
        return this;
    }

    /**
     * Creates a door block with the specified properties.
     * 
     * <p>Doors are double-height blocks that can be opened and closed.
     * They require proper support blocks above and below when placed.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder door() {
        this.blockType = BlockType.DOOR;
        return this;
    }

    /**
     * Sets custom tooltips for this torch.
     * 
     * @param tooltips List of tooltip strings
     * @return this builder for method chaining
     */
    public BlockBuilder tooltips(List<String> tooltips) {
        this.tooltips = tooltips;
        return this;
    }

    /**
     * Sets the light level emitted by this block (0-15).
     * 
     * @param lightLevel The light level (0-15)
     * @return this builder for method chaining
     */
    public BlockBuilder lightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
        return this;
    }

    /**
     * Sets whether this plant block is sensitive to layer placement (for snow layers).
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder layerSensitive() {
        this.layerSensitive = true;
        return this;
    }

    /**
     * Sets whether this plant block can be toggled in creative mode.
     * 
     * @return this builder for method chaining
     */
    public BlockBuilder toggleOnUse() {
        this.toggleOnUse = true;
        return this;
    }

    /**
     * Sets the custom bounding box for this block
     * 
     * @param xMin Minimum X coordinate
     * @param xMax Maximum X coordinate
     * @param yMin Minimum Y coordinate
     * @param yMax Maximum Y coordinate
     * @param zMin Minimum Z coordinate
     * @param zMax Maximum Z coordinate
     * @return this builder for method chaining
     */
    public BlockBuilder boundingBox(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.boundingBox = new double[]{xMin, xMax, yMin, yMax, zMin, zMax};
        return this;
    }

    /**
     * Sets the plant content for flower pot blocks.
     * 
     * @param plantContent The block that will be placed inside the flower pot
     * @return this builder for method chaining
     */
    public BlockBuilder plantContent(Block plantContent) {
        this.plantContent = plantContent;
        return this;
    }

    /**
     * Sets the block type (called internally by registration methods).
     * 
     * @param blockType The type of custom block to create
     */
    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    /**
     * Helper method to register a wall block variant.
     * 
     * @param wallBlock The wall block to register
     * @param wallName The name for the wall block
     * @return The registered wall block
     */
    private Block registerWallBlock(Block wallBlock, String wallName) {
        // Register render type for transparency if needed (same as main block)
        if (alphaRender) {
            BlockRenderLayerMap.INSTANCE.putBlock(wallBlock, RenderLayer.getTranslucent());
        } else if (nonOpaque) {
            BlockRenderLayerMap.INSTANCE.putBlock(wallBlock, RenderLayer.getCutout());
        }
        
        Registry.register(Registries.BLOCK, WesterosBlocks.id(wallName), wallBlock);
        Registry.register(Registries.ITEM, WesterosBlocks.id(wallName), 
            new BlockItem(wallBlock, new Item.Settings()));
        
        // Note: Wall torch blocks are NOT added to creative tab
        // They are automatically created when placing standing torches against walls
        
        return wallBlock;
    }

    /**
     * Registers this block with the game registry and creates the appropriate block instance.
     * 
     * <p>This method:
     * <ul>
     *   <li>Validates that required properties are set</li>
     *   <li>Creates the block with the configured settings</li>
     *   <li>Registers the block with the game registry</li>
     *   <li>Creates and registers the corresponding item</li>
     *   <li>Adds the block to the specified creative tab</li>
     * </ul>
     * 
     * @return The registered block instance
     * @throws IllegalStateException if block type or texture path is not specified
     */
    public Block register() {
        // Validate required properties
        if (blockType == null) {
            throw new IllegalStateException("Block type not specified for " + name);
        }
        if (texturePath == null && (texturePaths == null || texturePaths.length == 0)) {
            throw new IllegalStateException("Texture path not specified for " + name);
        }

        // Create base block settings
        AbstractBlock.Settings settings = AbstractBlock.Settings.create()
            .strength(hardness, resistance)
            .sounds(ModBlockSoundGroup.getBlockSoundGroup(soundType));

        // Apply light level if specified
        if (lightLevel > 0) {
            settings = settings.luminance(state -> lightLevel);
        }

        // Apply harvest requirements
        if (harvestTool != null) {
            settings = settings.requiresTool();
        }

        // Apply harvest level by adjusting hardness if needed
        if (harvestLevel > 0) {
            // Ensure minimum hardness for harvest level requirements
            float minHardness = switch (harvestLevel) {
                case 1 -> 1.5f;  // Stone level (wooden tools)
                case 2 -> 3.0f;  // Iron level (stone tools)
                case 3 -> 5.0f;  // Diamond level (iron tools)
                case 4 -> 6.0f;  // Netherite level (diamond tools)
                default -> hardness;
            };

            if (hardness < minHardness) {
                settings = settings.hardness(minHardness);
            }
        }

        // Apply additional properties
        if (nonOpaque) {
            settings = settings.nonOpaque();
        }
        if (burnable) {
            settings = settings.burnable();
        }
        if (noCollision) {
            settings = settings.noCollision();
        }
        if (dropsNothing) {
            settings = settings.dropsNothing();
        }
        if (mapColor != null) {
            settings = settings.mapColor(mapColor);
        }

        // Create the block instance
        Block block = createBlock(settings);

        // Register render type for transparency if needed
        if (alphaRender) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());
        } else if (nonOpaque) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }

        // Register the block with the game registry
        Registry.register(Registries.BLOCK, WesterosBlocks.id(name), block);

        // Register the corresponding item
        if (block instanceof WCWaySignBlock waySignBlock) {
            // Special handling for way signs
            Registry.register(Registries.ITEM, WesterosBlocks.id(name),
                new com.westerosblocks.item.WCWaySignItem(block, new Item.Settings(), waySignBlock.getWoodType().toString()));
        } else {
            // Standard block item registration
            Registry.register(Registries.ITEM, WesterosBlocks.id(name),
                new BlockItem(block, new Item.Settings()));
        }

        // Add to creative tab
        ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(creativeTab)).register(entries -> {
            entries.add(block);
        });

        // Handle torch and fan wall block registration
        if ((blockType == BlockType.TORCH || blockType == BlockType.FAN) && wallBlock != null) {
            registerWallBlock(wallBlock, "wall_" + name);
        }

        return block;
    }

    /**
     * Creates the appropriate block instance based on the configured block type.
     * 
     * @param settings The block settings to use
     * @return The created block instance
     */
    private Block createBlock(AbstractBlock.Settings settings) {
        return switch (blockType) {
            case ARROW_SLIT -> new WCArrowSlitBlock(settings, name, creativeTab);
            case TABLE -> new WCTableBlock(settings, name, creativeTab, woodType);
            case CHAIR -> new WCChairBlock(settings, name, creativeTab, woodType);
            case WAY_SIGN -> {
//                // Register wall variant too
//                Block wallBlock = new WCWaySignWallBlock(settings, "wall_" + name, creativeTab, woodType);
//                Registry.register(Registries.BLOCK, WesterosBlocks.id("wall_" + name), wallBlock);
//                Registry.register(Registries.ITEM, WesterosBlocks.id("wall_" + name), new BlockItem(wallBlock, new Item.Settings()));
//                ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(creativeTab)).register(entries -> {
//                    entries.add(wallBlock);
//                });
                yield new WCWaySignBlock(settings, name, creativeTab, woodType);
            }
            case TRAPDOOR -> new WCTrapDoorBlock(settings, name, creativeTab, woodType, locked, soundType);
            case DOOR -> new WCDoorBlock(
                settings,
                woodType.toString().toLowerCase(),
                locked,
                allowUnsupported,
                "block." + WesterosBlocks.MOD_ID + "." + name,
                tooltips
            );
            case HALF_DOOR -> new WCHalfDoorBlock(
                settings,
                locked,
                allowUnsupported,
                "block." + WesterosBlocks.MOD_ID + "." + name
            );
            case LOG -> new WCLogBlock(settings, name, creativeTab, woodType.toString().toLowerCase(), texturePaths);
            case TORCH -> {
                // Create wall torch first
                WCWallTorchBlock wallTorch = new WCWallTorchBlock(
                    settings,
                    allowUnsupported,
                    noParticle,
                    "block." + WesterosBlocks.MOD_ID + ".wall_" + name,
                    tooltips
                );

                // Create and return standing torch
                WCTorchBlock standingTorch = new WCTorchBlock(
                    settings,
                    wallTorch,
                    allowUnsupported,
                    noParticle,
                    "block." + WesterosBlocks.MOD_ID + "." + name,
                    tooltips
                );

                // Store wall torch for registration in register() method
                this.wallBlock = wallTorch;
                yield standingTorch;
            }
            case SAND -> new WCSandBlock(settings, name, creativeTab, tooltips);
            case FAN -> {
                // Create wall fan first
                WCWallFanBlock wallFan = new WCWallFanBlock(
                    settings,
                    allowUnsupported,
                    "block." + WesterosBlocks.MOD_ID + ".wall_" + name,
                    tooltips
                );

                // Create and return standing fan
                WCFanBlock standingFan = new WCFanBlock(
                    settings,
                    wallFan,
                    allowUnsupported,
                    "block." + WesterosBlocks.MOD_ID + "." + name,
                    tooltips
                );

                // Store wall fan for registration in register() method
                this.wallBlock = wallFan;
                yield standingFan;
            }
            case VINES -> new WCVinesBlock(settings, name, creativeTab, allowUnsupported, noParticle, canGrowDownward);
            case PLANT -> {
                yield new StandaloneWCPlantBlock(settings, name, creativeTab, layerSensitive, toggleOnUse);
            }
            case FLOWER_POT -> new StandaloneFlowerPotBlock(plantContent != null ? plantContent : Blocks.AIR, settings);
        };
    }

    /**
     * Enumeration of supported custom block types.
     * 
     * <p>Each type corresponds to a specific custom block class that provides
     * specialized behavior and rendering.
     */
    public enum BlockType {
        /** Arrow slit blocks for defensive structures */
        ARROW_SLIT,
        /** Table blocks for furniture */
        TABLE,
        /** Chair blocks for furniture */
        CHAIR,
        /** Way sign blocks for directional markers */
        WAY_SIGN,
        /** Trapdoor blocks for entrances */
        TRAPDOOR,
        /** Door blocks for entrances */
        DOOR,
        /** Half door blocks for partial entrances */
        HALF_DOOR,
        /** Log blocks for wooden structures */
        LOG,
        /** Torch blocks for lighting */
        TORCH,
        /** Sand blocks for falling particles */
        SAND,
        /** Fan blocks for decorative purposes */
        FAN,
        /** Vines blocks for climbing and decoration */
        VINES,
        /** Plant blocks for flowers and vegetation */
        PLANT,
        /** Flower pot blocks for decorative plants */
        FLOWER_POT
    }
} 