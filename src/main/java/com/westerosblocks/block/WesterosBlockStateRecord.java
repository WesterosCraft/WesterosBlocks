package com.westerosblocks.block;

import net.minecraft.util.shape.VoxelShape;
import com.westerosblocks.block.WesterosBlockDef.BoundingBox;
import com.westerosblocks.block.WesterosBlockDef.Cuboid;
import com.westerosblocks.block.WesterosBlockDef.RandomTextureSet;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WesterosBlockStateRecord {
    // If not defined, value is "stateN"
    public String stateID = null;
    // Bounding box
    public BoundingBox boundingBox = null;
    // List of cuboids composing block (for 'cuboid', and others)
    public List<Cuboid> cuboids = null;
    // For 'solid', used for raytrace (arrow shots)
    public List<BoundingBox> collisionBoxes = null;
    // For 'solid', used for support/connection (fences, walls, torches)
    public List<BoundingBox> supportBoxes = null;
    // If set and true, don't generate new custom model (hand crafted)
    public Boolean isCustomModel = false;
    // List of textures (for single texture set)
    public List<String> textures = null;
    // On supported blocks (solid, leaves, slabs, stairs), defines sets of textures used for additional random models
    // If randomTextures is used, textures is ignored
    public List<RandomTextureSet> randomTextures = null;
    // List of overlay textures (for types supporting overlays)
    public List<String> overlayTextures = null;
    // Set random rotation for supporting blocks (solid, leaves)
    public boolean rotateRandom = false;
    // Emitted light level (0.0-1.0)
    public float lightValue = 0.0F;
    // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')
    public String colorMult = "#FFFFFF";
    // Allows for custom color handlers with multiple colormaps (accessed via tintindex in models)
    public List<String> colorMults = null;
    // Additional rotation around Y axis (only for cuboid blocks with rotation) - done in blockstate 0, 90, 180, 270
    public int rotYOffset = 0;

    public void doStateRecordInit() {
        // If just base textures, generate equivalent random textures (simpler logic for blocks that support them
        if (this.randomTextures == null) {
            if (this.textures == null) this.textures = new ArrayList<String>();    // Always have at least array;
            this.randomTextures = new ArrayList<RandomTextureSet>();
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

    public List<BoundingBox> getCollisionBoxList() {
        if (this.collisionBoxes != null)
            return this.collisionBoxes;
        return Collections.emptyList();
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

    public boolean equals(WesterosBlockStateRecord other) {
        if (this.stateID == null) {
            return (other.stateID == null);
        }
        return this.stateID.equals(other.stateID);
    }
}
