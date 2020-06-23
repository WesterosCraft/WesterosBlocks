package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;

public final class BlockSoundOverrider {
    private BlockSoundOverrider() {
        throw new AssertionError();
    }
    private static class Override {
        private final String blockName;
        private final int meta;
        private final SoundType sound;
        public Override(String blockName, int meta, SoundType sound) {
            this.blockName = WesterosBlocks.MOD_ID + ":" + blockName;
            this.meta = meta;
            this.sound = sound;
        }
        public boolean matches(String blockName, int meta) {
            return this.blockName.equals(blockName) && this.meta == meta;
        }
        public SoundType getSoundType() {
            return sound;
        }
    }
    private static ArrayList<Override> overrides = new ArrayList<>();
    static {
        // 6-Sided Stone Slab
        overrides.add(new Override("six_sided_blocks_0", 5, SoundType.STONE));
        // Thatch Light Fur
        overrides.add(new Override("wood_colours", 9, SoundType.PLANT));
        // Thatch Dark Fur
        overrides.add(new Override("wood_colours", 10, SoundType.PLANT));
        // Leather
        overrides.add(new Override("wood_colours", 11, SoundType.CLOTH));
        // Dirt Slab
        overrides.add(new Override("random_slab_block_0", 0, SoundType.GROUND));
        // Gravel Slab
        overrides.add(new Override("random_slab_block_0", 1, SoundType.GROUND));
        // Thatch Light Fur Slab
        overrides.add(new Override("random_slab_block_0", 2, SoundType.PLANT));
        // Thatch Dark Fur Slab
        overrides.add(new Override("random_slab_block_0", 3, SoundType.PLANT));
        // Leather Slab
        overrides.add(new Override("random_slab_block_0", 4, SoundType.CLOTH));
        // Stool
        overrides.add(new Override("random_slab_block_0", 5, SoundType.WOOD));
        // Stool Cushion
        overrides.add(new Override("random_slab_block_0", 6, SoundType.WOOD));
        // double Dirt Slab
        overrides.add(new Override("random_slab_block_0_2", 0, SoundType.GROUND));
        // double Gravel Slab
        overrides.add(new Override("random_slab_block_0_2", 1, SoundType.GROUND));
        // double Thatch Light Fur Slab
        overrides.add(new Override("random_slab_block_0_2", 2, SoundType.PLANT));
        // double Thatch Dark Fur Slab
        overrides.add(new Override("random_slab_block_0_2", 3, SoundType.PLANT));
        // double Leather Slab
        overrides.add(new Override("random_slab_block_0_2", 4, SoundType.CLOTH));
        // Pewter Plate
        overrides.add(new Override("cuboid_block_3", 0, SoundType.METAL));
        // Wooden Plate
        overrides.add(new Override("cuboid_block_3", 1, SoundType.WOOD));
        // Table
        overrides.add(new Override("cuboid_block_3", 6, SoundType.WOOD));
        // Spruce Hopper
        overrides.add(new Override("hopper_block_0", 0, SoundType.WOOD));
        // Birch Hopper
        overrides.add(new Override("hopper_block_0", 1, SoundType.WOOD));
        // Jungle Hopper
        overrides.add(new Override("hopper_block_0", 2, SoundType.WOOD));
        // Oak Hopper
        overrides.add(new Override("hopper_block_2", 3, SoundType.WOOD));
        // Hollow Oak Hopper
        overrides.add(new Override("hopper_block_2", 4, SoundType.WOOD));
        // Hollow Spruce Hopper
        overrides.add(new Override("hopper_block_2", 5, SoundType.WOOD));
        // Hollow Birch Hopper
        overrides.add(new Override("hopper_block_2", 6, SoundType.WOOD));
        // Hollow Jungle Hopper
        overrides.add(new Override("hopper_block_2", 7, SoundType.WOOD));
        // Dark Northern Wood Hopper
        overrides.add(new Override("hopper_block_2", 12, SoundType.WOOD));
        // Hollow Dark Northern Wood Hopper
        overrides.add(new Override("hopper_block_2", 13, SoundType.WOOD));
        // Grey Wood Hopper
        overrides.add(new Override("hopper_block_3", 9, SoundType.WOOD));
        // White Wood Hopper
        overrides.add(new Override("hopper_block_3", 10, SoundType.WOOD));
    }
    /**
     * gets a SoundType from a BlockState, taking into account possible overrides
     * @param blockState the BlockState whose sound to get
     * @return the SoundType of the BlockState, possibly overridden
     */
    public static SoundType getSoundType(IBlockState blockState) {
        Block block = blockState.getBlock();
        String name = block.getRegistryName().toString();
        int meta = block.getMetaFromState(blockState);
        for (int i = 0; i < overrides.size(); i++) {
            if (overrides.get(i).matches(name, meta)) {
                return overrides.get(i).getSoundType();
            }
        }
        return block.getSoundType();
    }
}
