package com.westeroscraft.westerosblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef.BoundingBox;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Cuboid;
import com.westeroscraft.westerosblocks.WesterosBlockDef.RandomTextureSet;

public class WesterosBlockStateRecord {
	public String stateID = null;	// If not defined, value is "stateN"
	public BoundingBox boundingBox = null; // Bounding box
	public List<Cuboid> cuboids = null; // List of cuboids composing block (for 'cuboid', and others)
	public List<BoundingBox> collisionBoxes = null; // For 'solid', used for raytrace (arrow shots)
	public Boolean isCustomModel = null; // If set and true, don't generate new custom model (hand crafted)
	public List<String> textures = null; // List of textures (for single texture set)
	public List<RandomTextureSet> randomTextures = null;	// On supported blocks (solid, leaves, slabs, stairs), 
										// defines sets of textures used for additional random models
										// If randomTextures is used, textures is ignored
	public boolean rotateRandom = false;	// Set random rotation for supporting blocks (solid, leaves)
	
	public void doStareRecordInit() {
		// If just base textures, generate equivalent random textures (simpler logic for blocks that support them
		if ((this.textures != null) && (this.randomTextures == null)) {
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
		if ((textures != null) && (textures.size() > 0)) {
			if (idx >= textures.size()) {
				idx = textures.size() - 1;
			}
			return textures.get(idx);
		}
		return null;
	}
	// Get number of random texture sets
	public int getRandomTextureSetCount() {
		if ((randomTextures != null) && (randomTextures.size() > 0)) {
			return randomTextures.size();
		}
		return 0;
	}
	
	// Get given random texture set
	public RandomTextureSet getRandomTextureSet(int setnum) {
		if ((randomTextures != null) && (randomTextures.size() > 0)) {
			if (setnum >= randomTextures.size()) {
				setnum = randomTextures.size() - 1;
			}
			return randomTextures.get(setnum);
		}
		return null;
	}
}
