package com.westeroscraft.westerosblocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AuxMaterial {
    public static final AuxMaterial AIR = new AuxMaterial();
    public static final AuxMaterial STONE = new AuxMaterial();
    public static final AuxMaterial GRASS = new AuxMaterial();
    public static final AuxMaterial DIRT = new AuxMaterial();
    public static final AuxMaterial WOOD = new AuxMaterial();
    public static final AuxMaterial METAL = new AuxMaterial();
    public static final AuxMaterial WATER = new AuxMaterial();
    public static final AuxMaterial LAVA = new AuxMaterial();
    public static final AuxMaterial LEAVES = new AuxMaterial();
    public static final AuxMaterial PLANT = new AuxMaterial();
    public static final AuxMaterial SPONGE = new AuxMaterial();
    public static final AuxMaterial CLOTH_DECORATION = new AuxMaterial();
    public static final AuxMaterial FIRE = new AuxMaterial();
    public static final AuxMaterial SAND = new AuxMaterial();
    public static final AuxMaterial GLASS = new AuxMaterial();
    public static final AuxMaterial EXPLOSIVE = new AuxMaterial();
    public static final AuxMaterial ICE = new AuxMaterial();
    public static final AuxMaterial SNOW = new AuxMaterial();
    public static final AuxMaterial CACTUS = new AuxMaterial();
    public static final AuxMaterial CLAY = new AuxMaterial();
    public static final AuxMaterial PORTAL = new AuxMaterial();
    public static final AuxMaterial CAKE = new AuxMaterial();
    public static final AuxMaterial WEB = new AuxMaterial();
    public static final AuxMaterial PISTON = new AuxMaterial();
    public static final AuxMaterial DECORATION = new AuxMaterial();

    public static AuxMaterial getMaterial(BlockState blockState) {
        System.out.println("Material requested for block " + blockState);
        return null;
    }

    public static AuxMaterial getMaterial(BlockBehaviour.Properties props, WesterosBlockDef def) {
        return null;
    }
}
