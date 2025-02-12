package com.westerosblocks.block;

import net.minecraft.util.shape.VoxelShape;
import com.westerosblocks.block.ModBlock.BoundingBox;
import com.westerosblocks.block.ModBlock.Cuboid;
import com.westerosblocks.block.ModBlock.RandomTextureSet;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModBlockStateRecord {
    public String stateID = null; // If not defined, value is "stateN"
    public BoundingBox boundingBox = null; // Bounding box
    public List<Cuboid> cuboids = null; // List of cuboids composing block (for 'cuboid', and others)
    public List<BoundingBox> collisionBoxes = null; // For 'solid', used for raytrace (arrow shots)
    public List<BoundingBox> supportBoxes = null; // For 'solid', used for support/connection (fences, walls, torches)
    public Boolean isCustomModel = false; // If set and true, don't generate new custom model (hand crafted)
    public List<String> textures = null; // List of textures (for single texture set)
    public List<RandomTextureSet> randomTextures = null; // On supported blocks (solid, leaves, slabs, stairs), defines sets of textures used for additional random models. // If randomTextures is used, textures is ignored
    public List<String> overlayTextures = null; // List of overlay textures (for types supporting overlays)
    public boolean rotateRandom = false; // Set random rotation for supporting blocks (solid, leaves)
    public float lightValue = 0; // Emitted light level (0 - 15)
    public String colorMult = "#FFFFFF"; // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')
    public List<String> colorMults = null; // Allows for custom color handlers with multiple colormaps (accessed via tintindex in models)
    public int rotYOffset = 0; // Additional rotation around Y axis (only for cuboid blocks with rotation) - done in blockstate 0, 90, 180, 270

    public void doStateRecordInit() {
        // If just base textures, generate equivalent random textures (simpler logic for blocks that support them
        if (this.randomTextures == null) {
            if (this.textures == null) this.textures = new ArrayList<>(); // Always have at least array;
            this.randomTextures = new ArrayList<>();
            RandomTextureSet set = new RandomTextureSet();
            set.textures = textures;
            this.randomTextures.add(set);
        }
        // If we have bounding box, but no cuboids, make trivial cuboid
        if ((this.boundingBox != null) && (this.cuboids == null)) {
            Cuboid c = new Cuboid();
            c.xMin = this.boundingBox.xMin;
            c.xMax = this.boundingBox.xMax;
            c.yMin = this.boundingBox.yMin;
            c.yMax = this.boundingBox.yMax;
            c.zMin = this.boundingBox.zMin;
            c.zMax = this.boundingBox.zMax;
            this.cuboids = Collections.singletonList(c);
        }
        // If cuboids but no bounding box, compute bounding box
        if ((this.cuboids != null) && (this.boundingBox == null)) {
            this.boundingBox = new BoundingBox();
            this.boundingBox.xMin = this.boundingBox.yMin = this.boundingBox.zMin = 1.0F;
            this.boundingBox.xMax = this.boundingBox.yMax = this.boundingBox.zMax = 0.0F;
            for (BoundingBox bb : this.cuboids) {
                if (bb.xMin < this.boundingBox.xMin)
                    this.boundingBox.xMin = bb.xMin;
                if (bb.yMin < this.boundingBox.yMin)
                    this.boundingBox.yMin = bb.yMin;
                if (bb.zMin < this.boundingBox.zMin)
                    this.boundingBox.zMin = bb.zMin;
                if (bb.xMax > this.boundingBox.xMax)
                    this.boundingBox.xMax = bb.xMax;
                if (bb.yMax > this.boundingBox.yMax)
                    this.boundingBox.yMax = bb.yMax;
                if (bb.zMax > this.boundingBox.zMax)
                    this.boundingBox.zMax = bb.zMax;
            }
        }
    }

    public String getTextureByIndex(int idx) {
        RandomTextureSet set = getRandomTextureSet(0);
        if (set != null) {
            return set.getTextureByIndex(idx);
        }
        return null;
    }

    public String getOverlayTextureByIndex(int idx) {
        if (this.overlayTextures != null) {
            return this.overlayTextures.get(Math.min(idx, this.overlayTextures.size() - 1));
        }
        return null;
    }

    // Get number of random texture sets
    public int getRandomTextureSetCount() {
        if ((randomTextures != null) && (!randomTextures.isEmpty())) {
            return randomTextures.size();
        }
        return 0;
    }

    // Get given random texture set
    public RandomTextureSet getRandomTextureSet(int setnum) {
        if ((randomTextures != null) && (!randomTextures.isEmpty())) {
            if (setnum >= randomTextures.size()) {
                setnum = randomTextures.size() - 1;
            }
            return randomTextures.get(setnum);
        }
        return null;
    }

    public List<Cuboid> getCuboidList() {
        return cuboids;
    }

    // Get number of base textures
    public int getTextureCount() {
        RandomTextureSet set = getRandomTextureSet(0);
        if (set != null) {
            return set.getTextureCount();
        }
        return 0;
    }

    // Get customized support box
    public VoxelShape makeSupportBoxShape(List<BoundingBox> def) {
        VoxelShape s = VoxelShapes.empty();
        if (supportBoxes != null) {
            def = supportBoxes;
        }
        if (def != null) {
            for (BoundingBox b : def) {
                s = VoxelShapes.union(s, b.getAABB());
            }
        }
        return s;
    }

    public boolean isTinted() {
        return (((colorMult != null) && (!colorMult.equals("#FFFFFF"))) || (colorMults != null));
    }

    public boolean isCustomModel() {
        return this.isCustomModel;
    }

    public boolean equals(ModBlockStateRecord other) {
        if (this.stateID == null) {
            return (other.stateID == null);
        }
        return this.stateID.equals(other.stateID);
    }
}
