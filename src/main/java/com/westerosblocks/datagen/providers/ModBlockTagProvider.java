package com.westerosblocks.datagen.providers;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.westerosblocks.block.ModBlocks;

public class ModBlockTagProvider extends FabricTagProvider<Block> {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        // Automatically add all door blocks and halfdoor blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder doorTagBuilder = getOrCreateTagBuilder(BlockTags.DOORS);
        List<BlockDefinition> doorBlocks = registry.getByType("door");
        List<BlockDefinition> halfdoorBlocks = registry.getByType("halfdoor");

        for (BlockDefinition doorDef : doorBlocks) {
            String blockName = doorDef.getBlockName();

            Block doorBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (doorBlock != null) {
                doorTagBuilder.add(doorBlock);
            }
        }

        for (BlockDefinition halfdoorDef : halfdoorBlocks) {
            String blockName = halfdoorDef.getBlockName();

            Block doorBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (doorBlock != null) {
                doorTagBuilder.add(doorBlock);
            }
        }

        // Automatically add all log blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder logTagBuilder = getOrCreateTagBuilder(BlockTags.LOGS);
        List<BlockDefinition> logBlocks = registry.getByType("log");

        for (BlockDefinition logDef : logBlocks) {
            String blockName = logDef.getBlockName();

            Block logBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (logBlock != null) {
                logTagBuilder.add(logBlock);
            }
        }

        // Automatically add all slab blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder slabTagBuilder = getOrCreateTagBuilder(BlockTags.SLABS);
        List<BlockDefinition> slabBlocks = registry.getByType("slab");

        for (BlockDefinition slabDef : slabBlocks) {
            String blockName = slabDef.getBlockName();

            Block slabBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (slabBlock != null) {
                slabTagBuilder.add(slabBlock);
            }
        }

        // Automatically add all flowerpot blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder flowerPotTagBuilder = getOrCreateTagBuilder(BlockTags.FLOWER_POTS);
        List<BlockDefinition> flowerPotBlocks = registry.getByType("flowerpot");

        for (BlockDefinition flowerPotDef : flowerPotBlocks) {
            String blockName = flowerPotDef.getBlockName();

            Block flowerPotBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (flowerPotBlock != null) {
                flowerPotTagBuilder.add(flowerPotBlock);
            }
        }

        // Automatically add all stair blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder stairTagBuilder = getOrCreateTagBuilder(BlockTags.STAIRS);
        List<BlockDefinition> stairBlocks = registry.getByType("stair");

        for (BlockDefinition stairDef : stairBlocks) {
            String blockName = stairDef.getBlockName();

            Block stairBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (stairBlock != null) {
                stairTagBuilder.add(stairBlock);
            }
        }

        // Automatically add all ladder blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder ladderTagBuilder = getOrCreateTagBuilder(BlockTags.CLIMBABLE);
        List<BlockDefinition> ladderBlocks = registry.getByType("ladder");

        for (BlockDefinition ladderDef : ladderBlocks) {
            String blockName = ladderDef.getBlockName();
            boolean isNoClimb = ladderDef.isNoClimb();

            Block ladderBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (ladderBlock != null && !isNoClimb) {
                ladderTagBuilder.add(ladderBlock);
            }
        }

        // Automatically add all fence blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder fenceTagBuilder = getOrCreateTagBuilder(BlockTags.FENCES);
        FabricTagProvider<Block>.FabricTagBuilder woodenFenceTagBuilder = getOrCreateTagBuilder(BlockTags.WOODEN_FENCES);
        List<BlockDefinition> fenceBlocks = registry.getByType("fence");

        for (BlockDefinition fenceDef : fenceBlocks) {
            String blockName = fenceDef.getBlockName();
            Block fenceBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (fenceBlock != null) {
                fenceTagBuilder.add(fenceBlock);

                // Also add to wooden fences if it's a wooden sound type (wood blocks)
                // TODO probably get this a different way?
                String stepSound = fenceDef.getSoundGroup();
                if ("wood".equalsIgnoreCase(stepSound)) {
                    woodenFenceTagBuilder.add(fenceBlock);
                }
            }
        }

        // Automatically add all fencegate blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder fenceGateTagBuilder = getOrCreateTagBuilder(BlockTags.FENCE_GATES);
        List<BlockDefinition> fenceGateBlocks = registry.getByType("fencegate");

        for (BlockDefinition fenceGateDef : fenceGateBlocks) {
            String blockName = fenceGateDef.getBlockName();

            Block fenceGateBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (fenceGateBlock != null) {
                fenceGateTagBuilder.add(fenceGateBlock);
            }
        }

        // Automatically add all leaves blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder leavesTagBuilder = getOrCreateTagBuilder(BlockTags.LEAVES);
        List<BlockDefinition> leavesBlocks = registry.getByType("leaves");

        for (BlockDefinition leavesDef : leavesBlocks) {
            String blockName = leavesDef.getBlockName();

            Block leavesBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (leavesBlock != null) {
                leavesTagBuilder.add(leavesBlock);
            }
        }

        // Automatically add all bed blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder bedTagBuilder = getOrCreateTagBuilder(BlockTags.BEDS);
        List<BlockDefinition> bedBlocks = registry.getByType("bed");

        for (BlockDefinition bedDef : bedBlocks) {
            String blockName = bedDef.getBlockName();

            Block bedBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (bedBlock != null) {
                bedTagBuilder.add(bedBlock);
            }
        }

        // Automatically add all crop blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder cropTagBuilder = getOrCreateTagBuilder(BlockTags.CROPS);
        List<BlockDefinition> cropBlocks = registry.getByType("crop");

        for (BlockDefinition cropDef : cropBlocks) {
            String blockName = cropDef.getBlockName();

            Block cropBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (cropBlock != null) {
                cropTagBuilder.add(cropBlock);
            }
        }

        // Automatically add all trapdoor blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder trapdoorTagBuilder = getOrCreateTagBuilder(BlockTags.TRAPDOORS);
        List<BlockDefinition> trapdoorBlocks = registry.getByType("trapdoor");

        for (BlockDefinition trapDef : trapdoorBlocks) {
            String blockName = trapDef.getBlockName();

            Block trapdoorBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (trapdoorBlock != null) {
                trapdoorTagBuilder.add(trapdoorBlock);
            }
        }

        // Automatically add all fire blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder fireTagBuilder = getOrCreateTagBuilder(BlockTags.FIRE);
        List<BlockDefinition> fireBlocks = registry.getByType("fire");

        for (BlockDefinition fireDef : fireBlocks) {
            String blockName = fireDef.getBlockName();

            Block fireBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (fireBlock != null) {
                fireTagBuilder.add(fireBlock);
            }
        }

        // Automatically add all fire blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder railTagBuilder = getOrCreateTagBuilder(BlockTags.FIRE);
        List<BlockDefinition> railBlocks = registry.getByType("rail");

        for (BlockDefinition railDef : railBlocks) {
            String blockName = railDef.getBlockName();

            Block railBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (railBlock != null) {
                railTagBuilder.add(railBlock);
            }
        }

        // Automatically add all wall blocks from registry
        FabricTagProvider<Block>.FabricTagBuilder wallTagBuilder = getOrCreateTagBuilder(BlockTags.WALLS);
        List<BlockDefinition> wallBlocks = registry.getByType("wall");

        for (BlockDefinition wallDef : wallBlocks) {
            String blockName = wallDef.getBlockName();

            Block wallBlock = ModBlocks.getAutoRegisteredBlock(blockName);

            if (wallBlock != null) {
                wallTagBuilder.add(wallBlock);
            }
        }

        // Automatically add all fan blocks from registry to coral tags if they're coral fans
        FabricTagProvider<Block>.FabricTagBuilder coralTagBuilder = getOrCreateTagBuilder(BlockTags.CORALS);
        FabricTagProvider<Block>.FabricTagBuilder wallCoralTagBuilder = getOrCreateTagBuilder(BlockTags.WALL_CORALS);
        List<BlockDefinition> fanBlocks = registry.getByType("fan");

        for (BlockDefinition fanDef : fanBlocks) {
            String blockName = fanDef.getBlockName();

                Block fanBlock = ModBlocks.getAutoRegisteredBlock(blockName);
                if (fanBlock != null) {
                    coralTagBuilder.add(fanBlock);
                }

                // Also add the wall variant
                Block wallFanBlock = ModBlocks.getAutoRegisteredBlock("wall_" + blockName);
                if (wallFanBlock != null) {
                    wallCoralTagBuilder.add(wallFanBlock);
                }
            }

    }
}