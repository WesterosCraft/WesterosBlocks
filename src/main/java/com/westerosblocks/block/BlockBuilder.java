package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.*;
import com.westerosblocks.util.ModWoodType;
import com.westerosblocks.util.ModBlockSoundGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.WoodType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

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
     * Sets the block type (called internally by registration methods).
     * 
     * @param blockType The type of custom block to create
     */
    void setBlockType(BlockType blockType) {
        this.blockType = blockType;
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
        if (texturePath == null) {
            throw new IllegalStateException("Texture path not specified for " + name);
        }

        // Create base block settings
        AbstractBlock.Settings settings = AbstractBlock.Settings.create()
            .strength(hardness, resistance)
            .sounds(ModBlockSoundGroup.getBlockSoundGroup(soundType));

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
                // Register wall variant too
                Block wallBlock = new WCWaySignWallBlock(settings, "wall_" + name, creativeTab, woodType);
                Registry.register(Registries.BLOCK, WesterosBlocks.id("wall_" + name), wallBlock);
                Registry.register(Registries.ITEM, WesterosBlocks.id("wall_" + name), new BlockItem(wallBlock, new Item.Settings()));
                ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(creativeTab)).register(entries -> {
                    entries.add(wallBlock);
                });
                yield new WCWaySignBlock(settings, name, creativeTab, woodType);
            }
                case TRAPDOOR -> new WCTrapDoorBlock(settings, name, creativeTab, woodType, locked, soundType);
        };
    }

    /**
     * Enumeration of supported custom block types.
     * 
     * <p>Each type corresponds to a specific custom block class that provides
     * specialized behavior and rendering.
     */
    enum BlockType {
        /** Arrow slit blocks for defensive structures */
        ARROW_SLIT,
        /** Table blocks for furniture */
        TABLE,
        /** Chair blocks for furniture */
        CHAIR,
        /** Way sign blocks for directional markers */
        WAY_SIGN,
        /** Trapdoor blocks for entrances */
        TRAPDOOR
    }
} 