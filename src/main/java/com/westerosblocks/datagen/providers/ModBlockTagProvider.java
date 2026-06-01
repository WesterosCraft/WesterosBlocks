package com.westerosblocks.datagen.providers;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.data.BlockTagDefinition;
import com.westerosblocks.data.BlockTagEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.westerosblocks.block.ModBlocks;

public class ModBlockTagProvider extends FabricTagProvider<Block> {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    /**
     * Check if a block definition is a test block (should be excluded from production)
     */
    private boolean isTestBlock(BlockDefinition definition) {
        return WesterosCreativeModeTabs.isTestBlock(definition);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        // Initialize all tag builders upfront
        FabricTagProvider<Block>.FabricTagBuilder doorTagBuilder = getOrCreateTagBuilder(BlockTags.DOORS);
        FabricTagProvider<Block>.FabricTagBuilder logTagBuilder = getOrCreateTagBuilder(BlockTags.LOGS);
        FabricTagProvider<Block>.FabricTagBuilder slabTagBuilder = getOrCreateTagBuilder(BlockTags.SLABS);
        FabricTagProvider<Block>.FabricTagBuilder flowerPotTagBuilder = getOrCreateTagBuilder(BlockTags.FLOWER_POTS);
        FabricTagProvider<Block>.FabricTagBuilder stairTagBuilder = getOrCreateTagBuilder(BlockTags.STAIRS);
        FabricTagProvider<Block>.FabricTagBuilder ladderTagBuilder = getOrCreateTagBuilder(BlockTags.CLIMBABLE);
        FabricTagProvider<Block>.FabricTagBuilder fenceTagBuilder = getOrCreateTagBuilder(BlockTags.FENCES);
        FabricTagProvider<Block>.FabricTagBuilder woodenFenceTagBuilder = getOrCreateTagBuilder(BlockTags.WOODEN_FENCES);
        FabricTagProvider<Block>.FabricTagBuilder fenceGateTagBuilder = getOrCreateTagBuilder(BlockTags.FENCE_GATES);
        FabricTagProvider<Block>.FabricTagBuilder leavesTagBuilder = getOrCreateTagBuilder(BlockTags.LEAVES);
        FabricTagProvider<Block>.FabricTagBuilder bedTagBuilder = getOrCreateTagBuilder(BlockTags.BEDS);
        FabricTagProvider<Block>.FabricTagBuilder cropTagBuilder = getOrCreateTagBuilder(BlockTags.CROPS);
        FabricTagProvider<Block>.FabricTagBuilder trapdoorTagBuilder = getOrCreateTagBuilder(BlockTags.TRAPDOORS);
        FabricTagProvider<Block>.FabricTagBuilder fireTagBuilder = getOrCreateTagBuilder(BlockTags.FIRE);
        FabricTagProvider<Block>.FabricTagBuilder flowersTagBuilder = getOrCreateTagBuilder(BlockTags.FLOWERS);
        FabricTagProvider<Block>.FabricTagBuilder railTagBuilder = getOrCreateTagBuilder(BlockTags.RAILS);
        FabricTagProvider<Block>.FabricTagBuilder sandTagBuilder = getOrCreateTagBuilder(BlockTags.SAND);
        FabricTagProvider<Block>.FabricTagBuilder torchBuilder = getOrCreateTagBuilder(BlockTags.WALL_POST_OVERRIDE);
        FabricTagProvider<Block>.FabricTagBuilder wallTagBuilder = getOrCreateTagBuilder(BlockTags.WALLS);
        FabricTagProvider<Block>.FabricTagBuilder coralTagBuilder = getOrCreateTagBuilder(BlockTags.CORALS);
        FabricTagProvider<Block>.FabricTagBuilder wallCoralTagBuilder = getOrCreateTagBuilder(BlockTags.WALL_CORALS);

        // Initialize custom tag builders and seed with external block IDs from block_tags.json
        Map<String, FabricTagProvider<Block>.FabricTagBuilder> customTagBuilders = new HashMap<>();
        BlockTagDefinition blockTagDef = registry.getBlockTags();
        if (blockTagDef != null) {
            for (BlockTagEntry entry : blockTagDef.getBlockTags()) {
                TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, WesterosBlocks.id(entry.getCustomTag()));
                FabricTagProvider<Block>.FabricTagBuilder builder = getOrCreateTagBuilder(tagKey);
                for (String blockId : entry.getBlockNames()) {
                    builder.addOptional(Identifier.of(blockId));
                }
                customTagBuilders.put(entry.getCustomTag(), builder);
            }
        }

        // Single loop through all definitions
        for (BlockDefinition definition : registry.getAllDefinitions()) {
            // Skip test blocks
            if (isTestBlock(definition)) {
                continue;
            }

            String blockType = definition.getBlockType();
            String blockName = definition.getBlockName();
            Block block = ModBlocks.getAutoRegisteredBlock(blockName);

            // Skip if block wasn't registered
            if (block == null) {
                continue;
            }

            // Add to appropriate tag(s) based on block type
            switch (blockType.toLowerCase()) {
                case "door":
                case "halfdoor":
                    doorTagBuilder.add(block);
                    break;

                case "bigdoor": {
                    FabricTagProvider<Block>.FabricTagBuilder bigDoorBuilder = customTagBuilders.computeIfAbsent(
                        "big_door", t -> getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, WesterosBlocks.id(t)))
                    );
                    bigDoorBuilder.add(block);
                    break;
                }

                case "log":
                    logTagBuilder.add(block);
                    break;

                case "slab":
                    slabTagBuilder.add(block);
                    break;

                case "flowerpot":
                    flowerPotTagBuilder.add(block);
                    break;

                case "stair":
                    stairTagBuilder.add(block);
                    break;

                case "ladder":
                    // Only add if climbable
                    if (!definition.isNoClimb()) {
                        ladderTagBuilder.add(block);
                    }
                    break;

                case "fence":
                    fenceTagBuilder.add(block);
                    // Also add to wooden fences if it's a wooden sound type
                    if ("wood".equalsIgnoreCase(definition.getSoundGroup())) {
                        woodenFenceTagBuilder.add(block);
                    }
                    break;

                case "fencegate":
                    fenceGateTagBuilder.add(block);
                    break;

                case "leaves":
                    leavesTagBuilder.add(block);
                    break;

                case "bed":
                    bedTagBuilder.add(block);
                    break;

                case "crop":
                    cropTagBuilder.add(block);
                    break;

                case "trapdoor":
                    trapdoorTagBuilder.add(block);
                    break;

                case "fire":
                    fireTagBuilder.add(block);
                    break;

                case "plant":
                    flowersTagBuilder.add(block);
                    break;

                case "rail":
                    railTagBuilder.add(block);
                    break;

                case "sand":
                    sandTagBuilder.add(block);
                    break;

                case "torch":
                    torchBuilder.add(block);
                    // Also add the wall variant if it exists
                    Block wallTorchBlock = ModBlocks.getAutoRegisteredBlock("wall_" + blockName);
                    if (wallTorchBlock != null) {
                        torchBuilder.add(wallTorchBlock);
                    }
                    break;

                case "wall":
                    wallTagBuilder.add(block);
                    break;

                case "fan":
                    coralTagBuilder.add(block);
                    // Also add the wall variant if it exists
                    Block wallFanBlock = ModBlocks.getAutoRegisteredBlock("wall_" + blockName);
                    if (wallFanBlock != null) {
                        wallCoralTagBuilder.add(wallFanBlock);
                    }
                    break;

                // Other block types don't need tags
                default:
                    break;
            }

            // Add to custom tags declared in block definitions (e.g., polished_stone_ctm, wool_ctm)
            List<String> customTags = definition.getCustomTags();
            if (customTags != null) {
                for (String tag : customTags) {
                    FabricTagProvider<Block>.FabricTagBuilder builder = customTagBuilders.computeIfAbsent(
                        tag, t -> getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, WesterosBlocks.id(t)))
                    );
                    builder.add(block);
                }
            }
        }
    }
}