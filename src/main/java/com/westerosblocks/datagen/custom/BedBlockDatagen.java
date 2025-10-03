package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.enums.BedPart;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Exporter for bed blocks following block-models.md patterns.
 * Generates models for two-part bed blocks with directional facing and multiple bed types.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (bed head/foot variants with tinted/untinted support)</li>
 *   <li>TextureMap builders (6-texture system for head/foot parts)</li>
 *   <li>BlockStateSupplier methods (FACING and BED_PART properties)</li>
 *   <li>Clean datagen methods (BedBlockBuilder fluent API)</li>
 *   <li>BlockDefinition integration (registerCustomBedBlock)</li>
 * </ul>
 *
 * <p><b>Bed Block Variants:</b>
 * <ul>
 *   <li><b>Parts:</b> HEAD and FOOT (2 parts forming complete bed)</li>
 *   <li><b>Facing:</b> NORTH, EAST, SOUTH, WEST (4 directions)</li>
 *   <li><b>Total:</b> 8 blockstate variants (2 parts × 4 directions)</li>
 * </ul>
 *
 * <p><b>Bed Types:</b>
 * <ul>
 *   <li>bed - Standard Minecraft-style bed</li>
 *   <li>raised - Elevated bed with posts/frame</li>
 *   <li>hammock - Suspended hammock-style bed</li>
 * </ul>
 *
 * <p><b>Texture System (6 textures required):</b>
 * <ul>
 *   <li>[0] - Head top texture (also used as particle)</li>
 *   <li>[1] - Foot top texture</li>
 *   <li>[2] - Head side texture</li>
 *   <li>[3] - Foot side texture</li>
 *   <li>[4] - Head end texture</li>
 *   <li>[5] - Foot end texture</li>
 * </ul>
 *
 * <p><b>Item Model:</b>
 * Combines all 6 textures to render isometric bed preview using specialized bed_item template.
 *
 * <p><b>Tinting Support:</b>
 * Beds can be tinted for biome-specific coloring, using separate tinted/untinted model templates.
 *
 * @see ModTextureKey#BED_TOP
 * @see ModTextureKey#BED_SIDE
 * @see ModTextureKey#BED_END
 */
public class BedBlockDatagen {
    
    // Parent Block Models - following block-models.md #parent-block-model pattern
    private static Model createBedPartModel(String bedType, boolean isHead, boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String partSuffix = isHead ? "_head" : "_foot";
        
        // Apply different bed types based on bedType parameter
        String bedTypeName = switch (bedType) {
            case "raised" -> "bed_raised";
            case "hammock" -> "bed_hammock";
            default -> "bed";
        };
        
        String path = tintPath + bedTypeName + partSuffix;
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), 
            TextureKey.PARTICLE, ModTextureKey.BED_TOP, ModTextureKey.BED_SIDE, ModTextureKey.BED_END);
    }
    
    private static Model createBedItemModel(boolean tinted) {
        String path = tinted ? "item/tinted/bed_item" : "item/untinted/bed_item";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), 
            ModTextureKey.BED_TOP, ModTextureKey.BED_TOP2, ModTextureKey.BED_SIDE, 
            ModTextureKey.BED_SIDE2, ModTextureKey.BED_END, ModTextureKey.BED_END2);
    }

    // Builder pattern for bed block generation
    public static class BedBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block bedBlock;
        private final String bedName;
        private boolean isTinted = false;
        private String bedType = "bed"; // default bed type
        private final List<String> textures = new ArrayList<>();
        
        // Bed state variants - each bed has 8 states (4 facings × 2 parts)
        private static final BedVariant[] BED_VARIANTS = {
            new BedVariant("facing=north,part=foot", "foot", 180),
            new BedVariant("facing=east,part=foot", "foot", 270),
            new BedVariant("facing=south,part=foot", "foot", 0),
            new BedVariant("facing=west,part=foot", "foot", 90),
            new BedVariant("facing=north,part=head", "head", 180),
            new BedVariant("facing=east,part=head", "head", 270),
            new BedVariant("facing=south,part=head", "head", 0),
            new BedVariant("facing=west,part=head", "head", 90)
        };
        
        // Inner class to hold bed variant information
        public static class BedVariant {
            public final String condition;
            public final String part;
            public final int yRotation;
            
            public BedVariant(String condition, String part, int yRotation) {
                this.condition = condition;
                this.part = part;
                this.yRotation = yRotation;
            }
        }
        
        public BedBlockBuilder(BlockStateModelGenerator generator, Block bedBlock, String bedName) {
            this.generator = generator;
            this.bedBlock = bedBlock;
            this.bedName = bedName;
        }
        
        public BedBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }
        
        public BedBlockBuilder bedType(String bedType) {
            this.bedType = bedType;
            return this;
        }
        
        public BedBlockBuilder texture(String texturePath) {
            this.textures.add(texturePath);
            return this;
        }
        
        public void build() {
            if (textures.size() != 6) {
                throw new IllegalStateException("Bed block " + bedBlock + " requires exactly 6 textures, got " + textures.size());
            }
            
            List<Identifier> modelIds = generateBedModels();
            VariantsBlockStateSupplier blockStateSupplier = generateBlockStateVariants(modelIds);
            
            // Register blockstate
            generator.blockStateCollector.accept(blockStateSupplier);
            
            // Create bed item model
            generateBedItemModel();
        }
        
        private List<Identifier> generateBedModels() {
            List<Identifier> modelIds = new ArrayList<>();
            
            // Create texture map for head model
            // Head uses: texture[0]=bedtop+particle, texture[2]=bedside, texture[4]=bedend
            TextureMap headTextureMap = new TextureMap()
                    .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textures.get(0)))
                    .put(ModTextureKey.BED_TOP, WesterosBlocks.id("block/" + textures.get(0)))
                    .put(ModTextureKey.BED_SIDE, WesterosBlocks.id("block/" + textures.get(2)))
                    .put(ModTextureKey.BED_END, WesterosBlocks.id("block/" + textures.get(4)));
            
            // Create texture map for foot model  
            // Foot uses: texture[1]=bedtop+particle, texture[3]=bedside, texture[5]=bedend
            TextureMap footTextureMap = new TextureMap()
                    .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textures.get(1)))
                    .put(ModTextureKey.BED_TOP, WesterosBlocks.id("block/" + textures.get(1)))
                    .put(ModTextureKey.BED_SIDE, WesterosBlocks.id("block/" + textures.get(3)))
                    .put(ModTextureKey.BED_END, WesterosBlocks.id("block/" + textures.get(5)));
            
            // Generate head model
            String headModelSuffix = "/head_v1";
            Identifier headModelId = createBedPartModel(bedType, true, isTinted)
                    .upload(bedBlock, headModelSuffix, headTextureMap, generator.modelCollector);
            modelIds.add(headModelId);
            
            // Generate foot model
            String footModelSuffix = "/foot_v1";
            Identifier footModelId = createBedPartModel(bedType, false, isTinted)
                    .upload(bedBlock, footModelSuffix, footTextureMap, generator.modelCollector);
            modelIds.add(footModelId);
            
            return modelIds;
        }
        
        private VariantsBlockStateSupplier generateBlockStateVariants(List<Identifier> modelIds) {
            // Create a simple variants map by manually building the conditions
            // modelIds[0] is head, modelIds[1] is foot
            Identifier headModelId = modelIds.get(0);
            Identifier footModelId = modelIds.get(1);
            
            // Build variants map manually using the BED_VARIANTS array
            BlockStateVariant[] variants = new BlockStateVariant[BED_VARIANTS.length];
            
            for (int i = 0; i < BED_VARIANTS.length; i++) {
                BedVariant variant = BED_VARIANTS[i];
                
                // Choose the correct model based on part
                Identifier modelId = variant.part.equals("head") ? headModelId : footModelId;
                
                // Create variant with rotation if needed
                BlockStateVariant blockVariant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelId);
                
                // Add Y rotation if not 0
                if (variant.yRotation != 0) {
                    blockVariant = blockVariant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + variant.yRotation));
                }
                
                variants[i] = blockVariant;
            }
            
            // Create the supplier with manual variant mapping
            return VariantsBlockStateSupplier.create(bedBlock)
                    .coordinate(
                        // This maps each condition string to its corresponding variant
                        BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BED_PART)
                                .register(Direction.NORTH, BedPart.FOOT, variants[0])
                                .register(Direction.EAST, BedPart.FOOT, variants[1])
                                .register(Direction.SOUTH, BedPart.FOOT, variants[2])
                                .register(Direction.WEST, BedPart.FOOT, variants[3])
                                .register(Direction.NORTH, BedPart.HEAD, variants[4])
                                .register(Direction.EAST, BedPart.HEAD, variants[5])
                                .register(Direction.SOUTH, BedPart.HEAD, variants[6])
                                .register(Direction.WEST, BedPart.HEAD, variants[7])
                    );
        }
        
        private void generateBedItemModel() {
            // Map all 6 textures for the item model
            // Based on your example: texture[0]=bedtop, texture[1]=bedtop2, texture[2]=bedside, 
            // texture[3]=bedside2, texture[4]=bedend, texture[5]=bedend2
            TextureMap itemTextureMap = new TextureMap()
                    .put(ModTextureKey.BED_TOP, WesterosBlocks.id("block/" + textures.get(0)))
                    .put(ModTextureKey.BED_TOP2, WesterosBlocks.id("block/" + textures.get(1)))
                    .put(ModTextureKey.BED_SIDE, WesterosBlocks.id("block/" + textures.get(2)))
                    .put(ModTextureKey.BED_SIDE2, WesterosBlocks.id("block/" + textures.get(3)))
                    .put(ModTextureKey.BED_END, WesterosBlocks.id("block/" + textures.get(4)))
                    .put(ModTextureKey.BED_END2, WesterosBlocks.id("block/" + textures.get(5)));

            Identifier itemModelId = Identifier.of("westerosblocks", "item/" + bedName);
            createBedItemModel(isTinted)
                    .upload(itemModelId, itemTextureMap, generator.modelCollector);
        }
    }
    
    // Entry point for builder pattern
    public static BedBlockBuilder generateBedBlock(BlockStateModelGenerator generator, Block bedBlock, String bedName) {
        return new BedBlockBuilder(generator, bedBlock, bedName);
    }

    // Entry point for JSON definition system
    public static void registerCustomBedBlock(BlockStateModelGenerator generator, Block bedBlock, BlockDefinition definition) {
        BedBlockBuilder builder = new BedBlockBuilder(generator, bedBlock, definition.getBlockName());

        // Extract textures from definition - bed blocks need exactly 6 textures
        List<String> textures = definition.getTextures();
        if (textures == null || textures.size() != 6) {
            WesterosBlocks.LOGGER.warn("Bed block '{}' requires exactly 6 textures, got {}",
                definition.getBlockName(), textures != null ? textures.size() : 0);
            return;
        }

        // Add all 6 textures to the builder
        for (String texture : textures) {
            builder.texture(texture);
        }

        // Determine bed type from definition or fall back to default
        String bedType = definition.hasBedType() ? definition.getBedType() : "normal";
        builder.bedType(bedType);

        // Check if tinted (based on definition properties)
        if (definition.isTinted() || definition.hasColorMult()) {
            builder.isTinted();
        }

        builder.build();
    }
}