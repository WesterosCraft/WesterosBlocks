package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
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

public class BedBlockDatagen {
    
    // Parent Block Models - following block-models.md #parent-block-model pattern
    private static Model createBedPartModel(String bedType, boolean isHead, boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String partSuffix = isHead ? "_head" : "_foot";
        String path = tintPath + "bed" + partSuffix;
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), 
            TextureKey.PARTICLE, ModTextureKey.BED_TOP, ModTextureKey.BED_SIDE, ModTextureKey.BED_END);
    }
    
    private static Model createBedItemModel(boolean tinted) {
        String path = tinted ? "block/tinted/bed_item" : "block/untinted/bed_item";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), TextureKey.TEXTURE);
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
            
            // Create texture map for bed parts
            // Based on your example: texture[0]=particle, texture[1]=bedtop, texture[3]=bedside, texture[5]=bedend
            TextureMap bedTextureMap = new TextureMap()
                    .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textures.get(0)))
                    .put(ModTextureKey.BED_TOP, WesterosBlocks.id("block/" + textures.get(1)))
                    .put(ModTextureKey.BED_SIDE, WesterosBlocks.id("block/" + textures.get(3)))
                    .put(ModTextureKey.BED_END, WesterosBlocks.id("block/" + textures.get(5)));
            
            // Generate head model
            String headModelSuffix = "/head_v1";
            Identifier headModelId = createBedPartModel(bedType, true, isTinted)
                    .upload(bedBlock, headModelSuffix, bedTextureMap, generator.modelCollector);
            modelIds.add(headModelId);
            
            // Generate foot model
            String footModelSuffix = "/foot_v1";
            Identifier footModelId = createBedPartModel(bedType, false, isTinted)
                    .upload(bedBlock, footModelSuffix, bedTextureMap, generator.modelCollector);
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
            TextureMap itemTextureMap = new TextureMap()
                    .put(TextureKey.TEXTURE, WesterosBlocks.id("block/" + textures.getFirst()));
            
            // Generate item model using the proper method
            Identifier itemModelId = createBedItemModel(isTinted)
                    .upload(bedBlock, "_item", itemTextureMap, generator.modelCollector);
            
            // Register the item model
            generator.registerParentedItemModel(bedBlock, itemModelId);
        }
    }
    
    // Entry point for builder pattern
    public static BedBlockBuilder generateBedBlock(BlockStateModelGenerator generator, Block bedBlock, String bedName) {
        return new BedBlockBuilder(generator, bedBlock, bedName);
    }
}